package com.maltor.fcg.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class BulletActor extends PhysicsActor {
	private float dx, dy;
	public int damage = 10;

	public BulletActor(float dx, float dy) {
		super(new Texture(Gdx.files.internal("data/bullet.png")));

		this.dx = dx;
		this.dy = dy;
	}

	@Override
	public void instantiate(World world, float x, float y, short groupIndex) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		bodyDef.bullet = true;
		bodyDef.linearVelocity.set(dx, dy);
		
		body = world.createBody(bodyDef);
		
		PolygonShape rect = new PolygonShape();
		rect.setAsBox(2, 4, new Vector2(0, 0), getRotation());
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = rect;
		fixtureDef.density = 1f;
		fixtureDef.restitution = 0.2f;
		fixtureDef.friction = 0.4f;
		fixtureDef.filter.groupIndex = groupIndex;
		
		body.createFixture(fixtureDef);
		rect.dispose();
		super.instantiate(world, x, y, groupIndex);
	}
}
