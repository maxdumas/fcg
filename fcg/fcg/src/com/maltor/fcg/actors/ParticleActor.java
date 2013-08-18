package com.maltor.fcg.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParticleActor extends Actor {
	public final ParticleEffect effect;
	
	public ParticleActor(String effectName, float x, float y) {
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("particles/" + effectName), Gdx.files.internal("particles"));
		effect.setPosition(x, y);
		effect.start();
	}
	
	@Override
	public void act(float delta) {
		if(effect.isComplete()) remove();
		effect.update(delta);
		super.act(delta);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		effect.draw(batch);
		super.draw(batch, parentAlpha);
	}

	@Override
	public boolean remove() {
		if(getParent() != null) effect.dispose();
		return super.remove();
	}
}
