package com.maltor.fcg.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class PropNodeActor extends NodeActor {
	public final float enginePower = 1000;

	public PropNodeActor() {
		super(new Texture(Gdx.files.internal("data/node_def.png")));
		setColor(Color.RED);
	}

	@Override
	public void act(float delta) {
		boolean applyForce = false;
		Vector2 direction = new Vector2(0, 0);
		
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			direction.y += enginePower;
			applyForce = true;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			direction.y -= enginePower;
			applyForce = true;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			direction.x -= enginePower;
			applyForce = true;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			direction.x += enginePower;
			applyForce = true;
		}
			
		if(applyForce) {
			body.applyForceToCenter(direction, true);
			setRotation(direction.angle() - 90);
//			NodeActor exhaust = new NodeActor();
//			Vector2 pLoc = new Vector2(0, 0);
//			pLoc.x = getX() + (direction.x + enginePower) * getWidth() / enginePower;
//			pLoc.y = getY() + (direction.y + enginePower) * getHeight() / enginePower;
//			exhaust.instantiate(world, pLoc.x, pLoc.y);
//			exhaust.body.applyLinearImpulse(direction.scl(-1f), exhaust.body.getWorldCenter(), true);
//			getStage().addActor(exhaust);
		}
		
		super.act(delta);
	}
}
