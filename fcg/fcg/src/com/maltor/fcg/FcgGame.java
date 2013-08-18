package com.maltor.fcg;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class FcgGame implements ApplicationListener {
	Stage activeStage;

	@Override
	public void create() {
		activeStage = new PhysicsStage();
		Gdx.input.setInputProcessor(activeStage);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {
//		Gdx.gl.glClearColor(1, 0, 0.5f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
			Gdx.app.exit();

		activeStage.act(Gdx.graphics.getDeltaTime());
		activeStage.draw();
	}

	@Override
	public void resize(int width, int height) {
		activeStage.setViewport(width, height, true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
