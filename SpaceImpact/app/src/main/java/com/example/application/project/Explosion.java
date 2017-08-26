package com.example.application.project;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Explosion {

    private Bitmap bitmap;
    private int x;
    private int y;

    public Explosion(Context context) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion);
        x = -250;
        y = -250;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
