package com.delbridge.microgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Seth on 11/12/15.
 */
public class Play extends State {
    Player player;
    ShapeRenderer sr;
    Texture vignette;
    Texture bg;
    ArrayList<Particle> particles;
    Random random;
    public Play(GameStateManager gsm){
        super(gsm);
        player = new Player(MicroGame.WIDTH, MicroGame.HEIGHT);
        sr = new ShapeRenderer();
        cam.setToOrtho(false, MicroGame.WIDTH, MicroGame.HEIGHT);
        vignette = new Texture("vignette.png");
        bg = new Texture("background.png");
        random = new Random(System.currentTimeMillis());
        particles = new ArrayList<>();
        for(int i = 0; i< Particle.NUM_PARTICLES; i++){
            particles.add(new Particle(random.nextInt(MicroGame.WIDTH), random.nextInt(MicroGame.HEIGHT)));
        }
    }
    @Override
    protected void handleInput() {
        if(Gdx.input.isTouched()){
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();
            //Move right
            if(x > cam.viewportWidth/2) player.getVelocity().add(player.acceleration, 0);
            //Move left
            else player.getVelocity().sub(player.acceleration, 0);

            //Move up
            if(y > cam.viewportHeight/2) player.getVelocity().sub(0, player.acceleration);
            //Move down
            else player.getVelocity().add(0, player.acceleration);
        }
    }



    @Override
    public void update(float dt) {
        handleInput();
        player.update(dt);

        //Update particles
        for (int i = 0; i < Particle.NUM_PARTICLES; i++) {
            particles.get(i).update(dt);

        }

        cam.position.x = player.getPosition().x;
        cam.position.y = player.getPosition().y;
        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        //Allows transparency

        //Draw bg
        sb.setProjectionMatrix(cam.combined);
        sb.setColor(Color.WHITE);
        sb.begin();
        sb.draw(bg, player.getPosition().x - cam.viewportWidth / 2, player.getPosition().y - cam.viewportHeight / 2);
        sb.end();

        //Draw player
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.setProjectionMatrix(cam.combined);
        sr.setAutoShapeType(true);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for(Player.Pulse p : player.pulseRipples) {
            sr.setColor(1f, 1f, 1f, p.alpha);
            sr.circle(player.getPosition().x, player.getPosition().y,
                    p.size);
        }
        sr.setColor(1, 1, 1, 1f);
        sr.circle(player.getPosition().x, player.getPosition().y,
                player.radius + 4 * (float) Math.cos(player.pulse));


        //draw particles
        for(int i = 0; i< Particle.NUM_PARTICLES; i++){
            sr.setColor(100f/255f, 225f/255f, 150f/255f, (float)Math.abs(Math.sin(particles.get(i).flow)));
            sr.rect(particles.get(i).x, particles.get(i).y, 3, 3);
        }

        sr.end();

        //Vignette
        sb.begin();
        sb.setColor(1, 1, 1, .5f);
        sb.draw(vignette, player.getPosition().x - cam.viewportWidth/2, player.getPosition().y - cam.viewportHeight/2);
        sb.end();
    }

    @Override
    public void dispose() {
        sr.dispose();
        vignette.dispose();
        bg.dispose();
    }

    private class Particle{
        public static final int MAX_LIFE = 100;
        public static final int NUM_PARTICLES = 45;

        public float x, y;
        public float life;
        public float flow;

        public Particle(int x, int y){
            this.x = x;
            this.y = y;
            life = (int)(Math.random()*30) + 100;
            flow = (float)(random.nextFloat()*Math.PI*2);

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

}
