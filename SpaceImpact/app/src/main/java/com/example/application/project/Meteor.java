package com.example.application.project;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.io.IOException;
import java.util.Random;

public class Meteor {

    private int x,y,speed;
    private int screenX,screenY;
    private Bitmap bitmap;
    private Context context;
    Rect detectCollision;


    public Meteor(Context context,int screenX,int screenY) {

        bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.meteor);

        this.context = context;
        this.screenX = screenX;
        this.screenY = screenY;

        Random generator = new Random();
        x = generator.nextInt((screenX/3)) + 2*(screenX/3);
        y = 0;

        detectCollision = new Rect(x,y,bitmap.getWidth(),bitmap.getHeight());
    }

    public void update(){

        x-=13;
        y+=18;

        if(y > screenY-60-bitmap.getHeight()){
            Random generator = new Random();
            x = generator.nextInt((screenX/3)) + 3*(screenX/4);
            y = -(screenY/3);
        }

        detectCollision.left = x;
        detectCollision.right = x+ bitmap.getWidth();
        detectCollision.top = y;
        detectCollision.bottom = y+ bitmap.getHeight();


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

    public Rect getDetectCollision() {
        return detectCollision;
    }
}
