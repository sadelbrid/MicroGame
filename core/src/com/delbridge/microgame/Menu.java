package com.delbridge.microgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by Seth on 9/1/15.
 */
public class Menu extends State {
    private Random random;
    private Texture bg;
    private Texture vignette;
    private Texture title;
    private ShapeRenderer sr;
    private float whiteValue;
    private float whiteOverlay;
    private boolean fadingIn;
    private boolean fadingOut;
    public Menu(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, MicroGame.WIDTH, MicroGame.HEIGHT);
        vignette = new Texture("vignette.png");
        random = new Random(System.currentTimeMillis());
        sr = new ShapeRenderer();
        whiteValue = 1f;
        fadingIn = true;
        fadingOut = false;
        whiteOverlay = 1;
    }

    @Override
    protected void handleInput() {
        if(!fadingIn && Gdx.input.justTouched()){
            fadingOut = true;
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        if(fadingIn){
            whiteOverlay -= dt;
            if(whiteOverlay < 0) {
                whiteOverlay = 0f;
                fadingIn = false;
            }
        }
        else if (fadingOut) {
            whiteValue -= .75f*dt;
            if(whiteValue < 0) whiteValue = 0;
            if(whiteValue == 0) {
                dispose();
                gsm.currentState = GameStateManager.PLAY;
                gsm.setState(new Play(gsm));
            }
        }
    }



    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.setColor(whiteValue, whiteValue, whiteValue, 1f);
        sb.begin();
        //sb.draw(bg, 0, 0, MicroGame.WIDTH, MicroGame.HEIGHT);
        //sb.draw(title, MicroGame.WIDTH/2 - title.getWidth()/2, MicroGame.HEIGHT*.6f - title.getHeight()/2);
        sb.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);

        //Vignette;
        sb.begin();
        sb.setColor(whiteValue, whiteValue, whiteValue, .5f);
        sb.draw(vignette, 0, 0, MicroGame.WIDTH , MicroGame.HEIGHT);
        sb.end();
        if(fadingIn) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setProjectionMatrix(cam.combined);
            sr.setColor(1f, 1f, 1f, whiteOverlay);
            sr.rect(0, 0, MicroGame.WIDTH, MicroGame.HEIGHT);
            sr.end();
        }
    }

    @Override
    public void dispose() {
        //bg.dispose();
        vignette.dispose();
        sr.dispose();
        //title.dispose();
    }

    private class Shimmer{
        public static final int MAX_LIFE = 100;
        public float x, y;
        public float size;
        private float life;

        public Shimmer(float x, float y, float s, float l){
            Shimmer.this.x = x;
            Shimmer.this.y = y;
            Shimmer.this.size = s;
            Shimmer.this.life = l;
        }

        public void update(float dt){
            Shimmer.this.life += 75*dt;
        }
    }
}
