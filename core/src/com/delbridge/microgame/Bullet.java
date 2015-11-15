package com.delbridge.microgame;

/**
 * Created by Seth on 11/14/15.
 */
public class Bullet{
    int x, y;
    float xvel, yvel;
    public Bullet(int x, int y, float xvel, float yvel){
        Bullet.this.x = x;
        Bullet.this.y = y;
        Bullet.this.xvel = xvel;
        Bullet.this.yvel = yvel;

    }
    public void update(float dt){
        Bullet.this.x += xvel*dt;
        Bullet.this.y += yvel*dt;
    }
}
