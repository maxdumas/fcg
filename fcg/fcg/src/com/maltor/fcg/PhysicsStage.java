package com.maltor.fcg;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.maltor.fcg.actors.BasicActor;
import com.maltor.fcg.actors.DummyNodeActor;
import com.maltor.fcg.actors.NodeActor;
import com.maltor.fcg.actors.PhysicsActor;
import com.maltor.fcg.actors.PropNodeActor;
import com.maltor.fcg.actors.TurretNodeActor;
import com.maltor.fcg.actors.listeners.BulletContactListener;

public class PhysicsStage extends Stage {
	protected World world = new World(new Vector2(0, 0), true);
	protected Graph playerGraph, enemyGraph;
	protected Box2DDebugRenderer dbRenderer = new Box2DDebugRenderer();
	protected Actor ghostNode = new BasicActor(new Texture(Gdx.files.internal("data/node_def.png")));
	protected NodeActor selectedNode;
	public ActionState actionState = ActionState.SELECTING;
	
	public float mouseWorldX, mouseWorldY;

	public PhysicsStage() {
		super();
		world.setContactListener(new BulletContactListener());
		
		addActor(ghostNode);
		ghostNode.setVisible(false);
		
		playerGraph = new Graph(this);
		NodeActor p, q, r;

		p = new PropNodeActor();
		q = new DummyNodeActor();
		r = new TurretNodeActor();
		playerGraph.addNode(p, world, 100, 100);
		playerGraph.addNode(q, world, 200, 200);
		playerGraph.addNode(r, world, 100, 200);
		playerGraph.connectNodes(p, q);
		playerGraph.connectNodes(q, r);
		playerGraph.connectNodes(p, r);
		
		enemyGraph = new Graph(this);
		NodeActor t, u, v;
		t = new DummyNodeActor();
		u = new DummyNodeActor();
		v = new TurretNodeActor();
		enemyGraph.addNode(t, world, 500, 100);
		enemyGraph.addNode(u, world, 550, 150);
		enemyGraph.addNode(v, world, 525, 50);
		enemyGraph.connectNodes(t, u);
		enemyGraph.connectNodes(u, v);
		enemyGraph.connectNodes(v, t);
		
		// Floor creation
		BodyDef groundBodyDef = new BodyDef();  
		groundBodyDef.position.set(new Vector2(0, 10));  
		Body groundBody = world.createBody(groundBodyDef);  
		PolygonShape groundBox = new PolygonShape();  
		groundBox.setAsBox(getCamera().viewportWidth, 10.0f);
		groundBody.createFixture(groundBox, 0.0f); 
		groundBox.dispose();
	}
	
	@Override
	public void dispose() {
		world.dispose();
		dbRenderer.dispose();
		
		super.dispose();
	}

	@Override
	public void act(float delta) {
		Vector2 v = new Vector2(Gdx.input.getX(), Gdx.input.getY());
		v = screenToStageCoordinates(v);
		mouseWorldX = v.x;
		mouseWorldY = v.y;
		
		ghostNode.setPosition(mouseWorldX - 8, mouseWorldY - 8);
		
		world.step(1 / 60f, 6, 2);

		// Ensure the real positions and rotations of all actors reflect the positions of their bodies.
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		Iterator<Body> bi = bodies.iterator();
		while (bi.hasNext()) {
			Body b = bi.next();
			
			PhysicsActor p = (PhysicsActor) b.getUserData();
			
			if(p != null) {
				if(p.markedForRemoval) p.remove(); // If we should remove this actor, do that! TODO: Make sure removal in the middle of here doesn't cause issues.
				else {
					p.setPosition(b.getPosition().x - p.getWidth() / 2, b.getPosition().y - p.getHeight() / 2);
					p.setRotation(b.getAngle() * MathUtils.radiansToDegrees);
				}
			}
		}
		//
		
		super.act(delta);
	}

	@Override
	public void draw() {
		dbRenderer.render(world, getCamera().combined);
		super.draw();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// If we are in placing mode, place a new node and connect it to the nearest existing node!
		if(actionState == ActionState.PLACING) {
			NodeActor n = playerGraph.getNearestNode(mouseWorldX, mouseWorldY);
			NodeActor m = new TurretNodeActor();
			m.instantiate(world, mouseWorldX, mouseWorldY, playerGraph.groupIndex);

			playerGraph.connectNodes(n, m);
		}
		// If we are in selection mode and there is a node selected and the RMB is pressed
		else if(selectedNode != null && button == Input.Buttons.RIGHT) 
			playerGraph.connectNodes(selectedNode, playerGraph.getContainedNode(mouseWorldX, mouseWorldY));
		// If we are in selection mode and a mouse button is pressed
		else {
			if(selectedNode != null) selectedNode.setColor(Color.WHITE);
			selectedNode = playerGraph.getContainedNode(mouseWorldX, mouseWorldY);
			if(selectedNode != null) selectedNode.setColor(Color.YELLOW);
		}
		return super.touchDown(screenX, screenY, pointer, button);
	}
	
	@Override
	public boolean keyDown(int keyCode) {
		if(keyCode == Input.Keys.SPACE)
		{
			actionState = (actionState == ActionState.PLACING) ? ActionState.SELECTING : ActionState.PLACING;
			System.out.println("Now in mode: " + actionState.toString());
			ghostNode.setVisible(!ghostNode.isVisible());
		}
		
		return super.keyDown(keyCode);
	}
}
