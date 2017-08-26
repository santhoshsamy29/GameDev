package com.example.application.project;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;

public class Player {

    private Bitmap bitmap,bitmap1;
    private Bitmap ship,ground,ground1;
    Context context;
    private int x;
    private int y;
    private int maxY;
    private int minY;
    int screenX,screenY;
    private boolean upBoosting,downBoosting;
    private Rect detectCollision;
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();

    private boolean jump;


    public Player(Context context,int screenX,int screenY) {

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion);
        ground1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ground);

        ground = Bitmap.createScaledBitmap(ground1,screenX,ground1.getHeight(),false);
        ship = bitmap;


        x = 75;
        y = screenY- 5*(bitmap.getHeight()/4);
        this.screenX = screenX;
        this.screenY = screenY;
        this.context = context;
        maxY = screenY - bitmap.getHeight();
        minY = 0;
        upBoosting = false;
        downBoosting = false;
        detectCollision = new Rect(x,y,bitmap.getWidth(),bitmap.getHeight());


        jump = false;
    }

    public void setUpBoosting(){
        upBoosting = true;
    }

    public void stopUpBoosting(){
        upBoosting = false;
    }

    public void setDownBoosting(){
        downBoosting = true;
    }

    public void stopDownBoosting(){
        downBoosting = false;
    }


    public void update(){

        if(upBoosting){
            y-=18;
        }

        if(downBoosting){
            y+=18;
        }

        if (y < minY) {
            y = minY;
        }
        if (y > maxY - 50) {
            y = maxY - 50;
        }


        //detectCollision.left = x + (bitmap.getWidth()/2);
        detectCollision.left = x;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.top = y;
        detectCollision.bottom = y + bitmap.getHeight();
    }

    public void update2(){

        if(jump){
            y-=22;
        }else{
            y+=22;
        }

        if(y > (screenY- 5*(bitmap.getHeight()/4))){
            y= screenY- 5*(bitmap.getHeight()/4);
        }
        if(y<(screenY-9*(bitmap.getHeight()/2))){
            stopJump();
        }

        detectCollision.left = x;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.top = y;
        detectCollision.bottom = y + bitmap.getHeight();

    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public Bitmap getShip() {
        return ship;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void shoot() {

        if(Audio.laser.isPlaying()){
            try {
                Audio.laser.stop();
                Audio.laser.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Audio.laser.start();

        Bullet p = new Bullet(context,screenX,x + bitmap.getWidth() , y + (bitmap.getHeight()/2));
        bullets.add(p);
    }

    public ArrayList getBullets() {
        return bullets;
    }

    public void setExplosion(){
        ship = bitmap1;
    }

    public void stopExplosion(){
        ship = bitmap;
    }

    public void setJump(){
        jump = true;
    }

    public void stopJump(){
        jump = false;
    }

    public Bitmap getGround() {
        return ground;
    }
}