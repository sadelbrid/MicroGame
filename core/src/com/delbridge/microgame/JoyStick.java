package com.delbridge.microgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by dgallagher on 6/26/15.
 */
public class JoyStick {
    public Vector2 position;
    public int radius;
    int screenWidth, screenHeight;
    double angle;
    Player player;
    boolean touching;
    GridPoint2 pointTravelTo;
    public GridPoint2 touchPoint;
    int maxSpeed;
    int acceleration;
    int touchPointer;
    public JoyStick(Player p, int w, int h){

        screenWidth = w;
        screenHeight = h;
        radius = (int)(h*.1);
        position = new Vector2(w-2*radius, 2*radius);
        player = p;
        touching = false;
        angle = 0;
        touchPoint = new GridPoint2();
        maxSpeed = (int)(250*(double)w/(double)h);
        acceleration = 1;
        touchPointer = -1;
    }

    public void calculateTravelPoint(){
//        if(angle > cornerAngleRightBottom || angle < cornerAngleRightTop){ //right
//            pointTravelTo.x = screenWidth;
//            pointTravelTo.y = (int)(y-x*Math.tan(angle));
//        }
//        else if(angle > cornerAngleLeftBottom){//Bottom
//            pointTravelTo.x = 2*(int)(Math.tan((angle - cornerAngleLeftBottom)/2.0)*((screenHeight-y)));
//            pointTravelTo.y = screenHeight-60;
//        }
//        else if(angle > cornerAngleLeftTop){ //left
//            pointTravelTo.x = 0;
//            pointTravelTo.y = (int)(y-Math.tan(angle)*-x);
//        }
//        else{ //top
//            pointTravelTo.x = x + (int)((double)y/Math.tan(angle));
//            pointTravelTo.y = 0;
//        }
    }

    public void draw(ShapeRenderer sr, Camera cam) {
        //Assume projection matrix is set
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sr.begin();
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(0.0f, 0.0f, 0.0f, 0.25f);
        //Draw main
        sr.circle(cam.position.x - cam.viewportWidth/2 + position.x, cam.position.y - cam.viewportHeight/2 + position.y, radius);
        //draw point
        if(touching) {
            sr.setColor(0.0f, 0.0f, 0.0f, 0.5f);
            sr.circle(cam.position.x - cam.viewportWidth / 2 + touchPoint.x,
                    cam.position.y - cam.viewportHeight / 2 + touchPoint.y, radius / 4);
        }
        sr.end();
    }
}