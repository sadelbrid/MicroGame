package com.delbridge.microgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Player class. Represents the white blood cell.
 * Vector2s hold position and velocity
 * radius is the radius of the circle
 * pulse is radians that add to the main radius when drawn
 * acceleration is the rate velocity is changed
 *
 */
public class Player {
    public int maxMovement = 100;
    private Vector2 position;
    private Vector2 velocity;
    public float rotation;
    public float radius;
    public float pulse;
    public int acceleration;
    ArrayList<Pulse> pulseRipples;
    public Player(int w, int h){
        position = new Vector2(w/2, h/2);
        velocity = new Vector2(0, 0);
        rotation = 0f;
        radius = h*.05f;
        pulse = 0f;
        pulseRipples = new ArrayList<>();
        acceleration = 5;
    }

    public void update(float dt){
        //keeps cell from traveling super fast
        if(velocity.x > maxMovement) velocity.x = maxMovement;
        if(velocity.y > maxMovement) velocity.y = maxMovement;
        //adds velocity to position (scaled by dt)
        velocity.scl(dt);
        position.add(velocity.x, velocity.y);
        velocity.scl(1 / dt);

        //update pulse
        pulse += 3*dt;
        if(pulse > Math.PI *2) {
            pulse -= Math.PI*2;
            pulseRipples.add(new Pulse(radius + 4*(float)Math.cos(pulse), 1f));
        }

        //Update pulse ripples (probably only one)
        for(int i = 0; i < pulseRipples.size(); i++){
            pulseRipples.get(i).update(dt);
            if(pulseRipples.get(i).alpha < 0) pulseRipples.remove(i--);
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public String toString(){
        return
                Integer.toString(maxMovement) + ", " +
                position.toString() + ", " +
                velocity.toString() + ", " +
                Float.toString(rotation);
    }

    public class Pulse{
        float size;
        float alpha;
        public Pulse(float s, float a){
            Pulse.this.size = s;
            Pulse.this.alpha = a;
        }
        public void update(float dt){
            size+= 10 * dt;
            alpha -= dt;
        }
    }
}
