package com.delbridge.microgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Seth on 10/6/15.
 */
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
public class UponLoss extends State {
    private float opacity;
    private float messageOpacity;
    private ShapeRenderer sr;
    private boolean changing;
    private boolean message;
    private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("corbel.ttf"));
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    private BitmapFont font;
    float time;
    public UponLoss(GameStateManager gsm, float time){
        super(gsm);
        cam.setToOrtho(false, MicroGame.WIDTH, MicroGame.HEIGHT);
        opacity = 0f;
        sr = new ShapeRenderer();
        changing = false;
        message = false;
        messageOpacity = 0;

        parameter.size = 50;
        parameter.characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'. ";

        font = generator.generateFont(parameter);
        generator.dispose();
        loss = false;
        this.time = time;

    }
    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()){
            changing = true;
            message = false;
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        opacity += dt;
        if(!changing && opacity > .5f) {
            opacity = .5f;
            message = true;
        }
        else if (opacity > 1f){
            dispose();
            this.gsm.pop();
            gsm.peek().dispose();
            gsm.setState(new Play(gsm));
        }

        if(message){
            messageOpacity += dt;
            if(messageOpacity > 1) messageOpacity = 1;
        }
        else{
            messageOpacity -= 2*dt;
            if(messageOpacity < 0) messageOpacity = 0;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        sr.setAutoShapeType(true);
        sr.setProjectionMatrix(cam.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(0, 0, 0, opacity);
        sr.rect(cam.position.x - cam.viewportWidth / 2, 0, cam.viewportWidth, cam.viewportHeight);
        sr.end();

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.setColor(1, 1, 1, messageOpacity);
        font.draw(sb, "You lasted " + String.format("%.2f", time) + " seconds. \n   Your host has died." ,
                cam.viewportWidth/2 - 225, cam.viewportHeight/2 + 50);
        sb.end();
    }

    @Override
    public void dispose() {
        sr.dispose();
    }
}
