package com.maltor.fcg.client;

import com.maltor.fcg.FcgGame;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(800, 600);
		cfg.fps = 60;
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new FcgGame();
	}
}