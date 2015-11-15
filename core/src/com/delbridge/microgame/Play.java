package com.delbridge.microgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

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
    Texture antibody;
    ArrayList<Particle> particles;
    ArrayList<Bullet> bullets;
    ArrayList<Bacteria> bacteria;
    int numBacteriaAlive;
    Random random;
    MicroInputProcessor inputProcessor;
    Part directionArrow;
    float totalTime;
    private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("corbel.ttf"));
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    private BitmapFont font;
    boolean loss;
    public Play(GameStateManager gsm){
        super(gsm);
        player = new Player(MicroGame.WIDTH, MicroGame.HEIGHT);
        sr = new ShapeRenderer();
        cam.setToOrtho(false, MicroGame.WIDTH, MicroGame.HEIGHT);
        vignette = new Texture("vignette.png");
        bg = new Texture("background.png");
        antibody = new Texture("antibody.png");
        random = new Random(System.currentTimeMillis());
        particles = new ArrayList<>();
        for(int i = 0; i< Particle.NUM_PARTICLES; i++){
            particles.add(new Particle(random.nextInt(MicroGame.WIDTH), random.nextInt(MicroGame.HEIGHT)));
        }
        bullets = new ArrayList<>();
        inputProcessor = new MicroInputProcessor(player, bullets);
        Gdx.input.setInputProcessor(inputProcessor);
        directionArrow = new Part(3, 10, Part.TYPE_POLY, player.getPosition().x, player.getPosition().y, cam);
        directionArrow.setColor(new Color(Color.GRAY));
        bacteria = new ArrayList<>();
        totalTime = 0;
        while(bacteria.size() < 3){
            int temp = random.nextInt(4);
            int x = 0, y = 0;
            if(temp == 0){
                //top
                y = MicroGame.HEIGHT;
                x = random.nextInt(MicroGame.WIDTH);
            }
            else if(temp == 1){
                //right
                x = MicroGame.WIDTH;
                y = random.nextInt(MicroGame.HEIGHT);
            }
            else if(temp == 2){
                //bottom
                x = random.nextInt(MicroGame.WIDTH);
            }
            else{
                //left
                y = random.nextInt(MicroGame.HEIGHT);
            }
            bacteria.add(new Bacteria(player, MicroGame.WIDTH, MicroGame.HEIGHT, x, y));
        }
        numBacteriaAlive = 3;

        parameter.size = 50;
        parameter.characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'. ";

        font = generator.generateFont(parameter);
        generator.dispose();
        loss = false;
    }
    @Override
    protected void handleInput() {
    }



    @Override
    public void update(float dt) {
        totalTime += dt;
        player.update(dt);

        //update direction arrow
        directionArrow.setRotation(player.joyStick.angle);
        //Update particles
        for (int i = 0; i < Particle.NUM_PARTICLES; i++) {
            particles.get(i).update(dt);
        }

        //Update bullets
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update(dt);
            if(bullets.get(i).x < player.getPosition().x - cam.viewportWidth/2 ||
                    bullets.get(i).x > player.getPosition().x + cam.viewportWidth/2 ||
                    bullets.get(i).y < player.getPosition().y - cam.viewportHeight/2 ||
                    bullets.get(i).y > player.getPosition().y + cam.viewportHeight/2){

                bullets.remove(i--);

            }
        }
        //Update/spawn bacteria
        if(numBacteriaAlive < 3){
            int temp = random.nextInt(4);
            float x = -MicroGame.WIDTH*.2f, y = -MicroGame.HEIGHT*.2f;
            if(temp == 0){
                //top
                y = MicroGame.HEIGHT * 1.2f;
                x = random.nextInt(MicroGame.WIDTH);
            }
            else if(temp == 1){
                //right
                x = MicroGame.WIDTH*1.2f;
                y = random.nextInt(MicroGame.HEIGHT);
            }
            else if(temp == 2){
                //bottom
                x = random.nextInt(MicroGame.WIDTH);
            }
            else{
                //left
                y = random.nextInt(MicroGame.HEIGHT);
            }
            bacteria.add(new Bacteria(player, MicroGame.WIDTH, MicroGame.HEIGHT, (int)x, (int)y));
            numBacteriaAlive++;
        }

        for (int i = 0; i <bacteria.size(); i++){
            bacteria.get(i).update(dt);
            if(bacteria.get(i).radius < 0 || bacteria.get(i).getPosition().x < player.getPosition().x - cam.viewportWidth*.8 ||
                    bacteria.get(i).getPosition().x > player.getPosition().x + cam.viewportWidth*.8 ||
                    bacteria.get(i).getPosition().y < player.getPosition().y - cam.viewportHeight*.8 ||
                    bacteria.get(i).getPosition().y > player.getPosition().y + cam.viewportHeight*.8){
                if(bacteria.get(i).alive) numBacteriaAlive--;
                bacteria.remove(i--);

            }
        }

        //Update bullets
        for (int i = 0; i < bullets.size(); i++) {
            boolean hit = false;
            bullets.get(i).update(dt);
            //Check for collision with bacteria
            for(int j = 0; j < bacteria.size(); j++){
                float dis = (float)Math.sqrt(Math.pow(
                        bullets.get(i).x
                        - bacteria.get(j).getPosition().x, 2)
                        + Math.pow(bullets.get(i).y
                        - bacteria.get(j).getPosition().y, 2));
                if(dis < bacteria.get(j).radius){
                    //hit
                    hit = true;
                    bacteria.get(j).alive = false;
                    numBacteriaAlive--;
                }
            }

            if(hit || bullets.get(i).x < player.getPosition().x - cam.viewportWidth*.65 ||
                    bullets.get(i).x > player.getPosition().x + cam.viewportWidth*.65 ||
                    bullets.get(i).y < player.getPosition().y - cam.viewportHeight*.65 ||
                    bullets.get(i).y > player.getPosition().y + cam.viewportHeight*.65)
                bullets.remove(i--);
        }

        //Check if player can absorb
        for(int i = 0; i<bacteria.size(); i++){
            float dis = (float)Math.sqrt(Math.pow(player.getPosition().x - bacteria.get(i).getPosition().x, 2)
                    + Math.pow(player.getPosition().y - bacteria.get(i).getPosition().y, 2));
            if(dis < player.radius+bacteria.get(i).radius){
                if(!bacteria.get(i).alive) {
                    bacteria.get(i).abdorbed = true;
                    player.health += .1;
                }
                else{
                    player.health -= .2;
                    numBacteriaAlive--;
                    bacteria.remove(i--);
                }
            }
        }

        //Check for death
        if(player.health<=0 && !loss) {
            loss = true;
            gsm.push(new UponLoss(gsm, totalTime));
            player.alive = false;
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
        sr.setColor(.75f + player.health/4, .75f + player.health/4, .75f + player.health/4, 1f);
        sr.circle(player.getPosition().x, player.getPosition().y,
                player.radius + 4 * (float) Math.cos(player.pulse));

        if(player.alive) {
            sr.setColor(directionArrow.color);
            sr.triangle(directionArrow.rep[0] + player.getPosition().x - cam.viewportWidth / 2,
                    directionArrow.rep[1] + player.getPosition().y - cam.viewportHeight / 2,
                    directionArrow.rep[2] + player.getPosition().x - cam.viewportWidth / 2,
                    directionArrow.rep[3] + player.getPosition().y - cam.viewportHeight / 2,
                    directionArrow.rep[4] + player.getPosition().x - cam.viewportWidth / 2,
                    directionArrow.rep[5] + player.getPosition().y - cam.viewportHeight / 2);
        }

        //draw particles
        for(int i = 0; i< Particle.NUM_PARTICLES; i++){
            sr.setColor(255/255f, 120f/255f, 120f/255f, (float)Math.abs(Math.sin(particles.get(i).flow)));
            sr.rect(particles.get(i).x, particles.get(i).y, 6, 6);
        }

        //draw bullets
        sr.end();
        sb.begin();
        for(int i = 0; i< bullets.size(); i++){
            sb.draw(antibody, bullets.get(i).x-antibody.getWidth()/2, bullets.get(i).y, antibody.getWidth() / 2, 0,
                    antibody.getWidth(), antibody.getHeight(), 1, 1, (float)player.joyStick.angle, 0, 0, antibody.getWidth(),
                    antibody.getHeight(), false, false);
        }
        sb.end();

        //draw bacteria
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for(Bacteria b : bacteria){
            for(Bacteria.Pulse p : b.pulseRipples) {
                sr.setColor(0, .75f, 0f, p.alpha);
                sr.circle(b.getPosition().x, b.getPosition().y,
                        p.size);
            }
            sr.setColor(0, .75f, 0, 1);
            sr.circle(b.getPosition().x, b.getPosition().y, b.radius + 4 * (float) Math.cos(b.pulse));
        }

        //draw energ/health
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        //Draw health
        sr.setColor(1, 1, 1, .5f);
        sr.rect(player.getPosition().x - cam.viewportWidth / 2, player.getPosition().y + cam.viewportHeight / 2 - 20, cam.viewportWidth,
                20);
        sr.setColor(1, 1, 1, 1f);
        sr.rect(player.getPosition().x - cam.viewportWidth / 2, player.getPosition().y + cam.viewportHeight / 2 - 20, player.health * cam.viewportWidth,
                20);
        //draw energy

        sr.setColor(0, .6f, 1, .5f);
        sr.rect(player.getPosition().x - cam.viewportWidth / 2, player.getPosition().y + cam.viewportHeight / 2 - 40, cam.viewportWidth,
                20);
        sr.setColor(0, .6f, 1, .4f);
        sr.rect(player.getPosition().x - cam.viewportWidth / 2, player.getPosition().y + cam.viewportHeight / 2 - 40, player.energy * cam.viewportWidth,
                20);
        sr.end();
        player.joyStick.draw(sr, cam);

        sb.begin();
        sb.setColor(Color.WHITE);
        if(!loss)
        font.draw(sb, String.format("%.2f", totalTime), player.getPosition().x - cam.viewportWidth*.45f,
                player.getPosition().y - cam.viewportHeight*.3f);
        //Vignette
        //sb.begin();
        sb.setColor(1, 1, 1, .5f);
        sb.draw(vignette, player.getPosition().x - cam.viewportWidth/2, player.getPosition().y - cam.viewportHeight/2);
        sb.end();
    }

    @Override
    public void dispose() {
        sr.dispose();
        vignette.dispose();
        bg.dispose();
        antibody.dispose();
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
