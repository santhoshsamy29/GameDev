package com.example.application.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class Obstacle {

    private Bitmap bitmap;
    private int minX,maxX,minY,maxY;
    private int x,y,speed;

    private Rect detectCollision;


    public Obstacle(Context context,int screenX,int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.obstacle);
        this.minX = 0;
        this.maxY = 0;
        this.maxX = screenX;
        this.maxY = screenY;

        Random generator = new Random();
        x= generator.nextInt(maxX/2) + 4*(maxX/3);
        y =  maxY - 5*(bitmap.getHeight()/4);
        speed=10;
        detectCollision = new Rect(x,y,bitmap.getWidth(),bitmap.getHeight());


    }


    public void update(){

        x-=speed;

        if(x<minX-bitmap.getWidth()){
            Random generator = new Random();
            x =  generator.nextInt(maxX/3) + maxX + maxX/3;
        }

        detectCollision.left = x;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.top = y;
        detectCollision.bottom = y + bitmap.getHeight();

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rect getDetectCollision() {
        return detectCollision;
    }
}
