package com.delbridge.microgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;

/**
 * Created by Seth on 11/14/15.
 */
public class MicroInputProcessor implements InputProcessor {
    Player player;
    ArrayList<Bullet> bullets;
    public MicroInputProcessor(Player p, ArrayList<Bullet> b){
        player = p;
        bullets = b;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(!player.joyStick.touching && screenX > Gdx.graphics.getWidth()/2){
            player.joyStick.touchPointer = pointer;
            player.joyStick.touching = true;

//            float scalex = (float)Gdx.graphics.getWidth() / (float)MicroGame.WIDTH;
//            float scaley = (float)Gdx.graphics.getHeight() / (float)MicroGame.HEIGHT;
//
//            player.joyStick.position.x = (float)screenX/scalex;
//            player.joyStick.position.y = (float)screenY/scaley;
//            player.joyStick.position.y = MicroGame.HEIGHT - player.joyStick.position.y;

            screenY = Gdx.graphics.getHeight() - screenY;
            double deltaX = screenX - player.joyStick.position.x;
            double deltaY = screenY - player.joyStick.position.y;
            player.joyStick.angle = Math.atan2(deltaY, deltaX);
            if (screenY < player.joyStick.position.y) {
                player.joyStick.angle *= -1;
                player.joyStick.angle = 2 * Math.PI - player.joyStick.angle;
            }
        }
        if(screenX < Gdx.graphics.getWidth()/2){
            if(screenY < Gdx.graphics.getHeight()/2) {
                //fire bullet
                if(player.energy > .2) {
                    float xvel = 150 * (float) Math.cos(player.joyStick.angle) + player.getVelocity().x;
                    float yvel = 150 * (float) Math.sin(player.joyStick.angle) + player.getVelocity().y;
                    bullets.add(new Bullet((int) player.getPosition().x, (int) player.getPosition().y,
                            xvel, yvel));
                    player.energy -= .2f;
                }
            }
            else {
                player.movingPointer = pointer;
                player.moving = true;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if(pointer == player.joyStick.touchPointer){
            player.joyStick.touching = false;
            player.joyStick.touchPointer = -1;
        }
        else if(pointer == player.movingPointer){
            player.moving = false;
            player.movingPointer = -1;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer == player.joyStick.touchPointer){
            //normalize
            float scalex = (float)Gdx.graphics.getWidth() / (float)MicroGame.WIDTH;
            float scaley = (float)Gdx.graphics.getHeight() / (float)MicroGame.HEIGHT;

            player.joyStick.touchPoint.x = (int)(screenX/scalex);
            player.joyStick.touchPoint.y = (int)(screenY/scaley);
            player.joyStick.touchPoint.y = MicroGame.HEIGHT - player.joyStick.touchPoint.y;

            //screenY = Gdx.graphics.getHeight() - screenY;
            double deltaX = player.joyStick.touchPoint.x - player.joyStick.position.x;
            double deltaY = player.joyStick.touchPoint.y - player.joyStick.position.y;
            player.joyStick.angle = Math.atan2(deltaY, deltaX);
            if (screenY < player.joyStick.position.y) {
                player.joyStick.angle *= -1;
                player.joyStick.angle = 2 * Math.PI - player.joyStick.angle;
            }
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
