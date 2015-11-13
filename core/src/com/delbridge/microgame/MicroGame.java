package com.delbridge.microgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MicroGame extends ApplicationAdapter {
	SpriteBatch batch;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;
	public static boolean paused;
	GameStateManager gsm;

	@Override
	public void create () {
		batch = new SpriteBatch();
		paused = false;
		gsm = new GameStateManager();
		gsm.currentState = GameStateManager.MENU;
		gsm.push(new Play(gsm));
	}

	@Override
	public void render () {
		if(!paused) {
			Gdx.gl.glClearColor(1, 1, 1, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			float deltaTime = Gdx.graphics.getDeltaTime();
			if(deltaTime != 0) {
				gsm.update(deltaTime);
				gsm.render(batch);
			}
		}
	}
}
