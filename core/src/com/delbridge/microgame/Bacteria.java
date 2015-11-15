package com.delbridge.microgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Bacteria {
    private Vector2 position;
    private Vector2 velocity;
    public float rotation;
    public float radius;
    public float pulse;
    public float speed;
    ArrayList<Pulse> pulseRipples;
    float angle;
    Player player;
    boolean alive;
    boolean abdorbed;
    public Bacteria(Player p, int w, int h, int x, int y){
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        rotation = 0f;
        radius = h*.05f;
        pulse = (float)(Math.random()*Math.PI*2);
        pulseRipples = new ArrayList<>();
        speed = (float)(Math.random()*.5f + .3f);
        angle = 0;
        player = p;
        alive = true;
        abdorbed = false;
    }

    public void update(float dt){
        if(alive) {
            float dx = position.x - player.getPosition().x;
            float dy = position.y - player.getPosition().y;
            velocity.set((dx) * speed, (dy) * speed);
            Gdx.app.log("bactspeed", velocity.toString());
            //keeps cell from traveling super fast
//        if(velocity.x > maxMovement) velocity.x = maxMovement;
//        else if(velocity.x < -maxMovement) velocity.x = -maxMovement;
//
//        if(velocity.y > maxMovement) velocity.y = maxMovement;
//        else if(velocity.y < -maxMovement) velocity.y = -maxMovement;
            //adds velocity to position (scaled by dt)
            velocity.scl(dt);
            position.add(-velocity.x, -velocity.y);
            velocity.scl(1 / dt);
        }
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

        if(abdorbed) radius -= 20*dt;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public String toString(){
        return
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
