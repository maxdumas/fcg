package com.maltor.fcg.actors;

import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.maltor.fcg.Graph;

public abstract class NodeActor extends PhysicsActor {
	public final HashMap<NodeActor, LinkActor> neighbors = new HashMap<NodeActor, LinkActor>();
	public Graph graph;
	public int health = 100;
	
	public NodeActor(Texture texture) {
		super(texture);
	}

	@Override
	public void instantiate(World world, float x, float y, short groupIndex) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		
		body = world.createBody(bodyDef);
		
		CircleShape circle = new CircleShape();
		circle.setRadius(getWidth() / 2f);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 5f; // Density of 5 here to offset density of 1 for bullet.
		fixtureDef.restitution = 0.2f;
		fixtureDef.friction = 0.4f;
		fixtureDef.filter.groupIndex = groupIndex;
		System.out.println("Instantiated with a groupIndex of " + groupIndex);
		body.createFixture(fixtureDef);
		
		circle.dispose();
		
		super.instantiate(world, x, y, groupIndex);
	}

	@Override
	public void act(float delta) {
		if(health < 0)
			markedForRemoval = true;
		super.act(delta);
	}

	public void connectTo(NodeActor... nodes) {
		if(nodes == null) return;
		
		// TODO: Consider making this a static reference and then have a static initializer do this stuff.
		DistanceJointDef dDef = new DistanceJointDef();
		dDef.frequencyHz = 0.5f;
		dDef.dampingRatio = 0.2f; // Perfect! Makes joints "springy"
		dDef.collideConnected = true;
		
		for(NodeActor n : nodes) {
			if(n.neighbors.containsKey(this)) continue;
			
			dDef.initialize(body, n.body, body.getWorldCenter(), n.body.getWorldCenter());
			Joint joint = world.createJoint(dDef);
			LinkActor link = new LinkActor(this, n, joint);
			n.neighbors.put(this, link);
			this.neighbors.put(n, link);
		}
	}
	
	public boolean disconnectFrom(final NodeActor that, boolean deleteRef) {
		if(!neighbors.containsKey(that)) return false;
		
		LinkActor l = neighbors.get(that);
		world.destroyJoint(l.joint);
		l.remove();
		
		if(deleteRef) {
			this.neighbors.remove(that);
			that.neighbors.remove(this);
		}
		
		return true;
	}
	
	@Override
	public boolean remove() {
		Iterator<NodeActor> iter = neighbors.keySet().iterator();
		while(iter.hasNext()) {
			NodeActor n = iter.next();
			disconnectFrom(n, false);
			n.neighbors.remove(this);
			iter.remove();
		}
		
		
		neighbors.clear();
		return super.remove();
	}
}
