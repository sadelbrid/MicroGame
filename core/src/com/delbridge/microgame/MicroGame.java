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
		gsm.push(new Menu(gsm));
	}

	@Override
	/*
	This method is called as fast as possible on loop until
	the program terminates
	 */
	public void render () {
		//only render if not paused
		if(!paused) {
			//First clear the screen
			Gdx.gl.glClearColor(1, 1, 1, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			//Delta time is the time since the last frame was drawn
			float deltaTime = Gdx.graphics.getDeltaTime();
			//Update game states and draw id dt isn't 0 (prevents divide by 0 errors)
			if(deltaTime != 0) {
				//Update all game states
				gsm.update(deltaTime);
				//Draw all game states to screen
				gsm.render(batch);
			}
		}
	}
}
