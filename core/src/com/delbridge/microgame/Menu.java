package com.delbridge.microgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
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
    Player player;
    ArrayList<Particle> particles;

    public Menu(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, MicroGame.WIDTH, MicroGame.HEIGHT);
        vignette = new Texture("vignette.png");
        bg = new Texture("background.png");
        title = new Texture("title.png");
        random = new Random(System.currentTimeMillis());
        sr = new ShapeRenderer();
        whiteValue = 1f;
        fadingIn = true;
        fadingOut = false;
        whiteOverlay = 1;
        player = new Player(MicroGame.WIDTH, MicroGame.HEIGHT);
        particles = new ArrayList<>();
        for(int i = 0; i< Particle.NUM_PARTICLES; i++){
            particles.add(
                    new Particle(random.nextInt(MicroGame.WIDTH),
                            random.nextInt(MicroGame.HEIGHT),
                            player,
                            cam));
        }
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
        player.update(dt);

        //Update particles
        for (int i = 0; i < Particle.NUM_PARTICLES; i++) {
            particles.get(i).update(dt);
        }

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
        sb.draw(title, 0, 0);
        sb.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);

        //Draw player
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.setProjectionMatrix(cam.combined);
        sr.setAutoShapeType(true);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for(Player.Pulse p : player.pulseRipples) {
            sr.setColor(1f, 1f, 1f, p.alpha);
            sr.circle(MicroGame.WIDTH/2, MicroGame.HEIGHT/2-50,
                    p.size);
        }
        sr.setColor(.75f + player.health / 4, .75f + player.health / 4, .75f + player.health / 4, whiteValue);
        sr.circle(MicroGame.WIDTH / 2, MicroGame.HEIGHT / 2 - 50,
                player.radius + 4 * (float) Math.cos(player.pulse));

        //draw particles
        for(int i = 0; i< Particle.NUM_PARTICLES; i++){
            sr.setColor(whiteValue*255/255f , whiteValue*120f/255f, whiteValue*120f/255f, whiteValue*(float)Math.abs(Math.sin(particles.get(i).flow)));
            sr.rect(particles.get(i).x, particles.get(i).y, 6, 6);
        }
        sr.end();


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
        bg.dispose();
        vignette.dispose();
        sr.dispose();
        title.dispose();
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
