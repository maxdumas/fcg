package com.maltor.fcg.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BasicActor extends Actor {
	public TextureRegion region;
	public boolean markedForRemoval = false;

	public BasicActor(Texture texture) {
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		region = new TextureRegion(texture);
		this.setWidth(region.getRegionWidth());
		this.setHeight(region.getRegionHeight());
		this.setOrigin(region.getRegionWidth() / 2, region.getRegionHeight() / 2);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
//        batch.draw(region.getTexture(), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 
//        		getScaleX(), getScaleY(), getRotation(), 0, 0, region.getRegionWidth(), region.getRegionHeight(), false, false);
		super.draw(batch, parentAlpha);
	}
	
	public Vector2 getPosition() {
		return new Vector2(getX(), getY());
	}
	
	public float getCenterX() {
		return getX() + getOriginX();
	}
	
	public float getCenterY() {
		return getY() + getOriginY();
	}
	
	public Vector2 getCenter() {
		return new Vector2(getCenterX(), getCenterY());
	}
}
