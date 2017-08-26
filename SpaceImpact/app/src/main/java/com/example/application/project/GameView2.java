package com.example.application.project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class GameView2 extends SurfaceView implements Runnable {

    private boolean isPlaying;
    Canvas canvas;
    Paint paint;
    private Thread gameThread = null;
    private SurfaceHolder surfaceHolder;
    private Context context;

    int screenX,screenY;
    int minX,minY;
    int count;
    int score;
    int highScoreA[] = new int[3];
    SharedPreferences sharedPreferences;

    Player player;
    Enemy enemy;
    Obstacle obstacle;
    Bullet p;
    ArrayList<Star> stars = new ArrayList<>();
    Explosion explosion;
    Meteor meteor;
    ArrayList<Fire> fires = new ArrayList<Fire>();
    Fire f;
    int tempScore;

    public GameView2(Context context,int screenX,int screenY) {
        super(context);

        this.context = context;
        this.screenX = screenX;
        this.screenY = screenY;

        tempScore = 0;

        sharedPreferences  = context.getSharedPreferences("LEVEL_TWO_HIGH", Context.MODE_PRIVATE);
        highScoreA[0] = sharedPreferences.getInt("aScore1",0);
        highScoreA[1] = sharedPreferences.getInt("aScore2",0);
        highScoreA[2] = sharedPreferences.getInt("aScore3",0);

        surfaceHolder = getHolder();
        paint = new Paint();

        player = new Player(context,screenX,screenY);
        enemy = new Enemy(context,screenX,screenY);
        enemy.level2 = true;
        enemy.createEnemy();
        obstacle = new Obstacle(context,screenX,screenY);
        explosion = new Explosion(context);
        meteor = new Meteor(context,screenX,screenY);

        int starNums = 100;
        for (int i = 0; i < starNums; i++) {
            Star s = new Star(screenX, screenY);
            stars.add(s);
        }


        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                score++;
                handler.postDelayed(this,250);
            }
        });

    }

    @Override
    public void run() {
        while (isPlaying){
            update();
            draw();
            control();
        }
    }

    public void update(){

        player.update2();
        enemy.update2();
        //obstacle.update();
        meteor.update();
        for (Star s : stars) {
            s.update();
        }


        explosion.setX(-300);
        explosion.setY(-300);

        if(meteor.getY() > screenY-80-meteor.getBitmap().getHeight()){

            Fire fire = new Fire(context,screenX, meteor.getX(), meteor.getY()+(meteor.getBitmap().getHeight()/2));
            fires.add(fire);

        }

        for (int j = 0; j < fires.size(); j++) {
            f = fires.get(j);
            if (f.isVisible() == true) {
                f.update();
            } else {
                fires.remove(j);
            }

            if(Rect.intersects(player.getDetectCollision(),f.getDetectCollision())){

                if(Audio.explosion.isPlaying()){
                    try {
                        Audio.explosion.stop();
                        Audio.explosion.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Audio.explosion.start();
                player.setExplosion();
                lifeLineDialog();
                f.setX(-2000);
                isPlaying = false;

            }

        }

        if(Rect.intersects(player.getDetectCollision(),enemy.getDetectCollision())){

            if(Audio.explosion.isPlaying()){
                try {
                    Audio.explosion.stop();
                    Audio.explosion.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Audio.explosion.start();

            player.setExplosion();
            lifeLineDialog();
            enemy.setX(-2000);
            isPlaying = false;

        }

        if(Rect.intersects(player.getDetectCollision(),obstacle.getDetectCollision())){

            /*
            if(audio.explode.isPlaying()){
                try {
                    audio.explode.stop();
                    audio.explode.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            audio.explode.start();
            */
            player.setExplosion();
            lifeLineDialog();
            obstacle.setX(-2000);
            isPlaying = false;
        }

        ArrayList bullets = player.getBullets();
        for (int i = 0; i < bullets.size(); i++) {
            p = (Bullet) bullets.get(i);
            if (p.isVisible() == true) {
                p.update();
            } else {
                bullets.remove(i);
            }

            if(Rect.intersects(p.getDetectCollision(),enemy.getDetectCollision())){

                explosion.setX(enemy.getX());
                explosion.setY(enemy.getY());
                enemy.setX(-2000);
                p.setVisible(false);
            }
        }
    }

    public void draw(){

        if(surfaceHolder.getSurface().isValid()){
            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(Color.BLACK);

            paint.setColor(Color.WHITE);
            for(Star s:stars){
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }

            canvas.drawBitmap(player.getGround(),
                    minX,
                    screenY- 5*(player.getShip().getHeight()/4) + player.getShip().getHeight()/2,
                    paint);

            canvas.drawBitmap(player.getShip(),
                    player.getX(),
                    player.getY(),
                    paint);

            canvas.drawBitmap(enemy.getBitmap(),
                    enemy.getX(),
                    enemy.getY(),
                    paint);


            canvas.drawBitmap(obstacle.getBitmap(),
                    obstacle.getX(),
                    obstacle.getY(),
                    paint);

            ArrayList bullets = player.getBullets();
            for (int i = 0; i < bullets.size(); i++) {
                Bullet p = (Bullet) bullets.get(i);
                canvas.drawBitmap(
                        p.getBitmap(),
                        p.getX(), p.getY(), paint);
            }

            for (int j = 0; j < fires.size(); j++) {
                f = fires.get(j);
                canvas.drawBitmap(
                        f.getBitmap(),
                        f.getX(), f.getY(), paint);
            }

            canvas.drawBitmap(explosion.getBitmap(),
                    explosion.getX(),
                    explosion.getY(),
                    paint);

            canvas.drawBitmap(meteor.getBitmap(),
                    meteor.getX(),
                    meteor.getY(),
                    paint);


            paint.setTextSize(40);
            paint.setFakeBoldText(true);
            canvas.drawText("Score : " + score, 50, 50, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }

    private void control(){
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause(){
        tempScore = score;
        Log.e("SAN","SCORE : "+ tempScore);
        isPlaying = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void resume(){
        score = tempScore;
        Log.e("SAN","SCORE on resume : "+ tempScore);
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(player.getY() == player.screenY- 5*(player.getShip().getHeight()/4)){
            switch (event.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_UP:
                    player.stopJump();
                    break;


                case MotionEvent.ACTION_DOWN:
                    player.setJump();
                    break;
            }
        }

        return super.onTouchEvent(event);
    }

    public void shootIt(){
        player.shoot();
    }

    public void lifeLineDialog(){

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(count != 0){
                    addHighScore(score);
                    gameOverDialog();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.activity_life,null,false);
                builder.setView(view);
                builder.setCancelable(false);

                final AlertDialog lifeDialog = builder.create();
                lifeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final ImageButton life2,quit;
                life2 =(ImageButton)view.findViewById(R.id.life2);
                quit =(ImageButton)view.findViewById(R.id.quit_button);

                life2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        player.stopExplosion();
                        lifeDialog.dismiss();
                        resume();
                        life2.setVisibility(INVISIBLE);
                        count++;
                    }
                });

                quit.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addHighScore(score);
                        gameOverDialog();
                    }
                });
                lifeDialog.show();
                pause();
            }
        });
    }

    public void gameOverDialog(){

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.activity_gameover,null);
                builder.setView(view);
                builder.setCancelable(false);
                final AlertDialog over_dialog = builder.create();
                view.findViewById(R.id.gameover_tv);
                TextView scoreTv = (TextView)view.findViewById(R.id.score_tv);
                scoreTv.setText("Score : " + score);

                TextView bestTv = (TextView)view.findViewById(R.id.best_tv);
                bestTv.setText("Best : " + highScoreA[0]);
                view.findViewById(R.id.gameover_home).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mIntent = new Intent(context,MainActivity.class);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(mIntent);
                    }
                });

                view.findViewById(R.id.gameover_replay).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent gIntent = new Intent(context,GameActivity2.class);
                        gIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(gIntent);
                    }
                });

                over_dialog.show();
            }
        });
    }

    public void addHighScore(int score){
        for(int j=0 ; j<3 ; j++){
            if(highScoreA[j] < score){
                if(j == 0){
                    highScoreA[j+2] = highScoreA[j+1];
                    highScoreA[j+1] = highScoreA[j];
                    highScoreA[j] = score;
                    break;
                }
                if(j == 1){
                    highScoreA[j+1] = highScoreA[j];
                    highScoreA[j] = score;
                    break;
                }
                if(j == 2){
                    highScoreA[j] = score;
                    break;
                }
            }
        }
        sharedPreferences = context.getSharedPreferences("LEVEL_TWO_HIGH",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for(int k=0;k<3;k++){
            int j = k+1;
            editor.putInt("aScore"+j,highScoreA[k]);
        }
        editor.apply();
    }

    public int getScore() {
        return score;
    }
}
