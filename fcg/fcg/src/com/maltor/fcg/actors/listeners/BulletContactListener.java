package com.maltor.fcg.actors.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.maltor.fcg.actors.BulletActor;
import com.maltor.fcg.actors.NodeActor;
import com.maltor.fcg.actors.ParticleActor;

public class BulletContactListener implements ContactListener, RayCastCallback {
	private BulletActor bullet;
	private NodeActor node;
	
	private static final int numRays = 64;
	private static final float blastRadius = 100;
	private static final float blastPower = 50000;
	
	@Override
	public void beginContact(Contact contact) {
		final Object a = contact.getFixtureA().getBody().getUserData();
		final Object b = contact.getFixtureB().getBody().getUserData();
		
		if(a instanceof BulletActor) {
			bullet = (BulletActor) a;
			if(b instanceof NodeActor) node = (NodeActor) b;
			else return;
		}
		else if(b instanceof BulletActor) {
			bullet = (BulletActor) b;
			if(a instanceof NodeActor) node = (NodeActor) a;
			else return;
		}
		else return;
	}
	
	@Override
	public void endContact(Contact contact) {
		if(node == null || bullet == null) return;
		
		node.health -= bullet.damage;
		bullet.markedForRemoval = true;
		System.out.println("Node hit! Node health now at: " + node.health);
		
		for(float i = 0; i < numRays; ++i) {
			final float angle = (i / numRays) * 360;
			final Vector2 rayDir = new Vector2(MathUtils.sinDeg(angle), MathUtils.cosDeg(angle));
			final Vector2 rayEnd = bullet.getCenter().add(rayDir.scl(blastRadius));
//			System.out.println("Spawning ray #" + i + ". Ray is projected at " + angle + " degrees and ends at " + rayEnd);
			bullet.world.rayCast(this, bullet.getCenter(), rayEnd);
		}
		
		if(bullet.getStage() != null) bullet.getStage().addActor(new ParticleActor("expl_01", bullet.getCenterX(), bullet.getCenterY()));
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
		final Vector2 blastDir = point.sub(bullet.getCenter());
		final float distance = blastDir.len();
		if(distance == 0) return -1;
		final float invDistance = 1f / distance;
		final float impulseMag = (blastPower / numRays) * invDistance * invDistance;
		fixture.getBody().applyLinearImpulse(blastDir.scl(impulseMag), point, true);
		System.out.println("Explosion affected body at location: " + fixture.getBody().getWorldCenter());
		return 0;
	}

}
