package com.delbridge.microgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;

import java.util.Random;

/**
 * Created by Seth on 11/15/15.
 */
public class Particle{
    public static final int NUM_PARTICLES = 45;

    public float x, y;
    public float life;
    public float flow;
    Random random;
    Player player;
    Camera cam;
    public Particle(int x, int y, Player p, Camera c){
        this.x = x;
        this.y = y;
        life = (float)Math.random()*30 + 100;
        flow = (float)(Math.random()*Math.PI*2);
        player = p;
        cam = c;
        //Gdx.app.log("flowval", "" + random.nextFloat());
    }

    public void update(float dt){
        Particle.this.x += 10*dt;
        Particle.this.y -= 10f*dt;
        life += 5*dt;
        flow += dt;
        if(flow > Math.PI*2) flow -= Math.PI*2;

        //Bounds checking
        if (x < player.getPosition().x-cam.viewportWidth/2) x += cam.viewportWidth;
        else if (x > player.getPosition().x + cam.viewportWidth/2) x -= cam.viewportWidth;
        if (y < player.getPosition().y - cam.viewportHeight/2) y += cam.viewportHeight;
        else if (y > player.getPosition().y + cam.viewportHeight/2) y -= cam.viewportHeight;

    }
}