package com.delbridge.microgame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Represents a game state: menu, play, pause, gameover, etc
 */
public abstract class State {
    protected OrthographicCamera cam;
    protected GameStateManager gsm;
    public boolean paused;
    protected boolean loss;
    public State(GameStateManager gsm){
        this.gsm = gsm;
        cam = new OrthographicCamera();
        paused = false;
        loss = false;
    }

    protected abstract void handleInput();
    public abstract void render(SpriteBatch sb);
    public abstract void update(float dt);
    public abstract void dispose();
}
