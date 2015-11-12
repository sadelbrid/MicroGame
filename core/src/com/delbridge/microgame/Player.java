package com.delbridge.microgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Seth on 8/10/15.
 */
public class Player {
    public int maxMovement = 100;
    public int movement;
    private Vector2 position;
    private Vector2 velocity;
    public float rotation;
    public float radius;
    public Player(int w, int h){
        position = new Vector2(w/2, h/2);
        velocity = new Vector2(0, 0);
        rotation = 0f;
        movement = 0;
        position.y = MicroGame.HEIGHT*.1f;
        radius = h*.1f;
    }

    public void update(float dt){
        velocity.scl(dt);
        position.add(velocity.x, velocity.y);
        velocity.scl(1 / dt);
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
                Integer.toString(movement) + ", " +
                position.toString() + ", " +
                velocity.toString() + ", " +
                Float.toString(rotation);
    }
}
