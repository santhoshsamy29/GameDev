package com.example.application.project;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Fire {

    private Bitmap bitmap;
    private int x;
    private int y;
    private boolean visible;
    private int speedX,screenX;
    Rect detectCollision;

    public Fire(Context context,int screenX,int startX,int startY) {

        bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.fire);
        this.screenX = screenX;
        x = startX;
        y = startY;
        visible = true;
        speedX = 20;
        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(){

        x -= speedX;
        if (x > screenX){
            visible = false;
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

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Rect getDetectCollision() {
        return detectCollision;
    }
}
