package com.example.application.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Boss {

    private int x,y,speed;
    private int screenX,screenY;
    private Bitmap bitmap;
    private boolean isBoss;
    private boolean goDown = false,goUp = false;
    Rect detectCollision;

    public Boss(Context context,int screenX,int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.boss);
        isBoss = false;
        this.screenX = screenX;
        this.screenY = screenY;

        x=screenX;
        y = -2;
        detectCollision = new Rect(x,y,bitmap.getWidth(),bitmap.getHeight());
    }

    public void update(){


        if(isBoss) {
            x -= 4;
            if (y < 0) {
                goDown = true;
                goUp = false;
            }
            if (y > screenY - bitmap.getHeight()) {
                goDown = false;
                goUp = true;
            }

            if (goDown) {
                y += 4;
            }
            if (goUp) {
                y -= 4;
            }
        }else{
            x=screenX;
            y = -2;
        }

        detectCollision.left = x;
        detectCollision.right = x+ bitmap.getWidth();
        detectCollision.top = y;
        detectCollision.bottom = y+ bitmap.getHeight();

    }

    public void setBoss(){
        isBoss = true;
    }

    public void removeBoss(){
        isBoss = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public void setX(int x) {
        this.x = x;
    }
}
