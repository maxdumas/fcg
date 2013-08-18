package com.maltor.fcg.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class DummyNodeActor extends NodeActor {

	public DummyNodeActor() {
		super(new Texture(Gdx.files.internal("data/node_def.png")));
	}
}
