package com.maltor.fcg.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;

public class LinkActor extends BasicActor {
	public NodeActor a, b;
	public Joint joint;
	
	public LinkActor(NodeActor a, NodeActor b, Joint joint) {
		super(new Texture(Gdx.files.internal("data/link.png")));
		this.a = a;
		this.b = b;
		this.joint = joint;
	}
	
	@Override
	public void act(float delta) {
		setPosition(a.getCenterX(), a.getCenterY());
		setRotation(MathUtils.radiansToDegrees * MathUtils.atan2(a.getCenterX() - b.getCenterX(), b.getCenterY() - a.getCenterY()));
		setHeight(new Vector2(a.getCenterX(), a.getCenterY()).dst(b.getCenterX(), b.getCenterY()));
		
		super.act(delta);
	}
}
