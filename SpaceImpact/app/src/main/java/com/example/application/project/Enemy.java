package com.example.application.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

public class Enemy {

    private Bitmap bitmap;
    private int x;
    private int y;
    private int speed = 1;
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;
    private Rect detectCollision;
    private boolean isBoss;
    boolean level1;
    boolean level2;


    public Enemy(Context context, int screenX, int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);

        level1 = false;
        level2 = false;
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        isBoss = false;

        detectCollision = new Rect(x,y,bitmap.getWidth(),bitmap.getHeight());

    }
    public void createEnemy(){
        Random generator = new Random();
        speed = 20;
        if(level1){
            speed = 20;
            x = generator.nextInt(maxX) + maxX;
            y = generator.nextInt(maxY - 200) + 200 - bitmap.getHeight();
        }else if(level2){
            speed = 15;
            x = maxX;
            y = maxY - 5 * (bitmap.getHeight() / 4);
        }
    }

    public void update() {
        x -= speed;

        if(isBoss){

        }else {
            if (x < minX - bitmap.getWidth()) {
                Log.d("SAN",String.valueOf(bitmap.getWidth()));

                Random generator = new Random();
                speed = generator.nextInt(2)+ 20;
                x =  maxX;
                y = generator.nextInt(maxY - 200) + 200 - bitmap.getHeight();
            }
        }


        detectCollision.left = x;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.top = y;
        detectCollision.bottom = y + bitmap.getHeight();
    }

    public void update2(){

        x-=speed;

        if(x<minX-bitmap.getWidth()){
            Random generator = new Random();
            x =  generator.nextInt(maxX/3) + maxX;
        }

        detectCollision.left = x;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.top = y;
        detectCollision.bottom = y + bitmap.getHeight();

    }

    public void setX(int x) {
        this.x = x;
    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setBoss(){
        isBoss = true;
    }

    public void removeBoss(){
        isBoss = false;
    }

}
