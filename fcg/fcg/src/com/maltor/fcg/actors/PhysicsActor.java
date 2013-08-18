package com.maltor.fcg.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class PhysicsActor extends BasicActor {

	public PhysicsActor(Texture texture) {
		super(texture);
	}

	public TextureRegion region;
	public World world;
	public Body body;
	
	/**
	 * Used to instantiate the specific properties of a PhysicsActor. Can be done externally via {@link PhysicsActor#body} if you don't want to create a new class.
	 * @param world Box2D world to place the Actor in
	 * @param x X-Position to set the actor to be at
	 * @param y Y-Position to set the actor to be at
	 */
	public void instantiate(World world, float x, float y, short groupIndex) {
		this.world = world;
		body.setUserData(this);
	}

	@Override
	public boolean remove() {
//		StringBuilder stackTrace = new StringBuilder();
//		for(StackTraceElement s : new Throwable().getStackTrace())
//			stackTrace.append(s.toString() + "\n");
//		
//		System.out.println("A(n) " + getClass().getSimpleName() + " has been removed! Stack trace:\n" + stackTrace.toString());
		
		if(getParent() != null)	world.destroyBody(body);
		
		return super.remove();
	}
}