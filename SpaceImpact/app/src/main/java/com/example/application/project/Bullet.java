package com.example.application.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class Bullet {

    private int x, y, speedX;
    private boolean visible;
    Bitmap bitmap;
    private int screenX;
    Rect detectCollision;

    public Bullet(Context context,int screenX,int startX, int startY){

        this.screenX = screenX;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
        x = startX;
        y = startY;
        speedX = 13;
        visible = true;
        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(){
        x += speedX;
        if (x > screenX){
            visible = false;
        }
        detectCollision.left = x;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.top = y;
        detectCollision.bottom = y + bitmap.getHeight();

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeedX() {
        return speedX;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Rect getDetectCollision() {
        return detectCollision;
    }
}
