package com.delbridge.microgame;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;

/**
 * Created by Seth Delbridge on 7/2/15.
 */
public class Part {
    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_POLY = 1;
    public static final int TYPE_CUSTOM = 2;

    public float radius;
    public int type;
    public double rotation;
    public float[] rep;
    int numPoints;
    float x_vel, y_vel;
    Point center;
    public Color color;
    private Camera cam;

    //number of points, radius, type of part, intial x, intial y
    public Part(int numPoints, int r, int t, double init_x, double init_y, Camera cam) {
        this.numPoints = numPoints;
        rep = new float[numPoints*2];
        int xAverage = 0, yAverage = 0;
        x_vel = y_vel = 0;
        this.radius = r;
        this.type = t;
        this.rotation = 0;
        for(int i = 0; i< numPoints; i++){
            double x_pos = Math.cos((2.0*Math.PI/numPoints)*i)*radius + init_x;
            double y_pos = Math.sin((2.0*Math.PI/numPoints)*i)*radius + init_y;
            //rep.add(new Point((int)x_pos, (int)y_pos));
            rep[i*2] = (int)x_pos;
            rep[i*2+1] = (int)y_pos;
            xAverage += (int)x_pos;
            yAverage += (int)y_pos;
        }

        center = new Point(xAverage/numPoints, yAverage/numPoints);
        color = new Color();
        this.cam = cam;
    }

    public Part(ArrayList<Point> points, Camera cam) {
        rep = new float[points.size()*2];
        this.numPoints = points.size();
        int xAverage = 0, yAverage = 0;
        x_vel = y_vel = 0;
        this.type = TYPE_CUSTOM;
        this.rotation = 0;
        for(int i =0; i<numPoints; i++){
            Point p = points.remove(0);
            xAverage += p.x;
            yAverage += p.y;
            rep[2*i] = (float)p.x;
            rep[2*i+1] = (float)p.y;
        }
        center = new Point(xAverage/numPoints, yAverage/numPoints);
        color = new Color();
        this.cam = cam;
    }

    public void setColor(Color c){
        this.color.set(c);
    }

    public boolean contains(float x, float y) {
        if(this.type != TYPE_CIRCLE) {
            int i, j, count = 0;
            for (i = 0, j = numPoints - 1; i < numPoints; j = i++) {
                if (((rep[2*i+1] > y) != (rep[2*j+1] > y)) &&
                        (x < (rep[2*j] - rep[2*i]) * (y - rep[2*i+1]) / (rep[2*j+1] - rep[2*i+1]) + rep[2*i])) {
                    count++;
                }
            }
            //Return true if count is odd
            return count % 2 != 0;
        }
        else{
            double distance = Math.abs(Math.sqrt(Math.pow(x - rep[0], 2) + Math.pow(y - rep[1], 2)));
            return distance <= this.radius;
        }
    }

    public void update(float dt) {
        center.x += x_vel*dt;
        center.y += y_vel*dt;
        for(int i = 0; i < numPoints; i++){
            rep[2*i] += x_vel*dt;
            rep[2*i+1] += y_vel*dt;
        }
    }

    public void draw(ShapeRenderer s, Color c) {
        s.setProjectionMatrix(cam.combined);
        s.setAutoShapeType(true);
        s.begin(ShapeRenderer.ShapeType.Line);
        s.setColor(c);
        if(type != TYPE_CIRCLE) {
            s.polygon(rep);
            //Draw with triangles
            for(int i = 0; i < numPoints; i++){
                if(i < numPoints-1){
                    s.triangle((float)center.x, (float)center.y, rep[2*i], rep[2*i+1], rep[2*i+2], rep[2*i+3]);
                }
                else{
                    s.triangle((float)center.x, (float)center.y, rep[2*i], rep[2 * i + 1], rep[0], rep[1]);
                }
            }
        }
        else{
            s.circle((float)center.x, (float)center.y, radius);
        }
        s.end();
    }

    /*
    Translation methods
     */

    public void translateX(float dX) {
        center.x += dX;
        for (int i = 0; i < numPoints; i++)
            rep[2*i] += dX;
    }

    public void translateY(float dY){
        center.y += dY;
        for(int i = 0; i < numPoints; i++)
            rep[2*i+1] += dY;
    }

    public void translateXY(float dX, float dY){
        for(int i = 0; i < numPoints; i++) {
            rep[2*i] += dX;
            rep[2*i+1] += dY;
        }
        center.x += dX;
        center.y += dY;
    }

    public void translateByAngle(double angleRad, float amount){
        /*
        sin(angle) = dY/amount;
        cos(angle) = dX/amount;
         */
        int dX = (int)(Math.cos(angleRad)*amount);
        int dY = (int)(Math.sin(angleRad)*amount);
        for(int i = 0; i < numPoints; i++) {
            rep[2*i] += dX;
            rep[2*i+1] += dY;
        }
        center.x += dX;
        center.y += dY;
    }

    public void rotate(float angleRad) {
        rotateAboutPoint(this.center.x, this.center.y, angleRad);
    }

    public void rotateAboutPoint(double x, double y, double angleRad){
        rotation += angleRad;
        if(rotation >= Math.PI*100) rotation -= Math.PI*100;
        for(int i = 0; i<numPoints; i++){
            double temp = (Math.cos(angleRad) * (rep[2*i] - x) - Math.sin(angleRad)*(rep[2*i+1] - y) + x);
            rep[2*i+1] = (float)(Math.sin(angleRad) * (rep[2*i] - x) + Math.cos(angleRad)*(rep[2*i+1] - y) + y);
            rep[2*i] = (float)temp;
        }
        double temp = (Math.cos(angleRad) * (this.center.x - x) - Math.sin(angleRad)*(this.center.y - y) + x);
        this.center.y = (Math.sin(angleRad) * (this.center.x - x) + Math.cos(angleRad)*(this.center.y - y) + y);
        this.center.x = temp;
    }

    public void changeSize(float amount) {
        radius += amount;
        rep = new float[numPoints*2];
        for (int i = 0; i < numPoints; i++) {
            double x_pos = Math.cos(((2.0*Math.PI/numPoints)*i) + rotation)*radius + center.x;
            double y_pos = Math.sin(((2.0*Math.PI/numPoints)*i) + rotation)*radius + center.y;
            rep[2*i] = (float)x_pos;
            rep[2*i+1] = (float)y_pos;
        }
    }

    public static class Point {
        public double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
