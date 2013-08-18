package com.maltor.fcg.actors;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class TurretNodeActor extends NodeActor {
	public final List<BulletActor> bullets;
	float fireRate = 0.5f, timeSinceFired = 0f;

    public TurretNodeActor() {
        super(new Texture(Gdx.files.internal("data/node_def.png")));
        setColor(Color.GREEN);
        bullets = new ArrayList<BulletActor>();
    }

	@Override
	public void act(float delta) {
		timeSinceFired += delta;
		
		if(Gdx.input.isButtonPressed(Buttons.LEFT) && graph.id == 2 && timeSinceFired > fireRate)
		{
			fire();
			timeSinceFired = 0f;
		}
		
		
		super.act(delta);
	}

	public void fire() {
		Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
		mousePos = getStage().screenToStageCoordinates(mousePos);
		final float theta = MathUtils.atan2(getCenterX() - mousePos.x, mousePos.y - getCenterY());
		final float sin = -MathUtils.sin(theta), cos = MathUtils.cos(theta);
		BulletActor bullet = new BulletActor(100f * sin, 100f * cos);
		bullet.instantiate(world, getCenterX() + getWidth() * sin, getCenterY() + getHeight() * cos, graph.groupIndex);
		bullet.setRotation(theta * MathUtils.radiansToDegrees);
		bullet.body.setTransform(bullet.body.getPosition(), theta);
		getStage().addActor(bullet);
		bullets.add(bullet);
		// Apply the equal and opposite force to the bullet firing.
		body.applyLinearImpulse(bullet.body.getLinearVelocity().scl(-bullet.body.getMass()), new Vector2(getCenterX(), getCenterY()), true);
	}
}
