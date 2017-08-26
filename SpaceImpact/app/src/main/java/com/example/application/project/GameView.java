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
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import java.io.IOException;
import java.util.ArrayList;

public class GameView  extends SurfaceView implements Runnable{

    volatile boolean isPlaying;
    private int count =0;
    boolean pro = false;

    private Thread gameThread = null;
    private Player player;
    private Enemy[] enemies;
    private Boss boss;
    private int enemyCount = 4;
    private ArrayList<Star> stars = new ArrayList<Star>();
    private Explosion explosion;

    private Canvas canvas;
    private Paint paint;
    private SurfaceHolder surfaceHolder;

    int screenX;
    int screenY;
    private int countMisses;
    boolean flag ;
    private boolean isGameOver ;
    int score;
    int tempScore;
    int highScore[] = new int[3];
    private int bossKill = 0;
    SharedPreferences sharedPreferences;
    Bullet p;

    Context context;
    public GameView(final Context context, final int screenX, final int screenY) {
        super(context);
        this.context = context;
        this.screenX = screenX;
        this.screenY = screenY;

        countMisses = 0;
        isGameOver = false;
        tempScore = 1;

        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME",Context.MODE_PRIVATE);
        highScore[0] = sharedPreferences.getInt("score1",0);
        highScore[1] = sharedPreferences.getInt("score2",0);
        highScore[2] = sharedPreferences.getInt("score3",0);

        surfaceHolder = getHolder();
        paint = new Paint();

        player = new Player(context,screenX,screenY);
        explosion = new Explosion(context);
        int starNums = 100;
        for (int i = 0; i < starNums; i++) {
            Star s = new Star(screenX, screenY);
            stars.add(s);
        }
        enemies = new Enemy[enemyCount];
        for(int i=0; i<enemyCount; i++){
            enemies[i] = new Enemy(context, screenX, screenY);
            enemies[i].level1 = true;
            enemies[i].createEnemy();
        }
        boss = new Boss(context,screenX,screenY);
    }

    @Override
    public void run() {
        while (isPlaying){
            update();
            draw();
            control();
        }
    }

    private void update(){

        player.update();
        boss.update();
        for (Star s : stars) {
            s.update();
        }

        if(tempScore % 20 ==0){

            for(int i=0; i<enemyCount; i++) {
                enemies[i].setBoss();
            }
            boss.setBoss();
        }

        explosion.setX(-300);
        explosion.setY(-300);

        for(int i=0; i<enemyCount; i++) {
            if (enemies[i].getX() == screenX) {
                flag = true;
            }
            enemies[i].update();

            if(pro) {
                if (flag) {
                    if (enemies[i].getDetectCollision().exactCenterX() < 0) {
                        countMisses++;
                        flag = false;
                        if (countMisses == 5) {
                            lifeLineDialog();
                            isPlaying = false;
                        }
                    }
                }
            }

        }

        for(int j=0 ; j<enemyCount ; j++){

            if (Rect.intersects(player.getDetectCollision(), enemies[j].getDetectCollision())) {
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
                enemies[j].setX(-2000);
                isPlaying = false;
            }
        }


        if(Rect.intersects(player.getDetectCollision(),boss.getDetectCollision())){
            player.setExplosion();
            if(Audio.explosion.isPlaying()){
                try {
                    Audio.explosion.stop();
                    Audio.explosion.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Audio.explosion.start();
            for(int j=0; j<enemyCount; j++) {
                enemies[j].removeBoss();
            }
            boss.removeBoss();
            tempScore++;
            lifeLineDialog();
            boss.setX(-2000);
            isPlaying = false;
        }


        ArrayList bullets = player.getBullets();
        for(int i = 0; i < bullets.size(); i++) {
            p = (Bullet) bullets.get(i);
            if (p.isVisible() == true) {
                p.update();
            } else {
                bullets.remove(i);
            }

            for(int j=0 ; j<enemyCount ; j++){
                if (Rect.intersects(p.getDetectCollision(), enemies[j].getDetectCollision())) {
                    explosion.setX(enemies[j].getX());
                    explosion.setY(enemies[j].getY());
                    score++;
                    tempScore++;
                    enemies[j].setX(-2000);
                    p.setVisible(false);
                }
            }

            if(Rect.intersects(boss.getDetectCollision(),p.getDetectCollision())){
                bossKill++;
                bullets.remove(i);
                if(bossKill % 25 ==0){
                    if(Audio.explosion.isPlaying()){
                        try {
                            Audio.explosion.stop();
                            Audio.explosion.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Audio.explosion.start();

                    explosion.setX(boss.getX() + boss.getBitmap().getWidth()/3);
                    explosion.setY(boss.getY() + boss.getBitmap().getHeight()/3);
                    boss.setX(-2000);
                    p.setVisible(false);
                    for(int j=0; j<enemyCount; j++) {
                        enemies[j].removeBoss();
                    }
                    boss.removeBoss();
                    score = score + 5;
                }
            }
        }

    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(Color.BLACK);

            paint.setColor(Color.WHITE);
            for (Star s : stars) {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }

            canvas.drawBitmap(player.getShip(),
                    player.getX(),
                    player.getY(),
                    paint);

            for (int i = 0; i < enemyCount; i++) {
                canvas.drawBitmap(enemies[i].getBitmap(),
                        enemies[i].getX(),
                        enemies[i].getY(), paint);
            }

            canvas.drawBitmap(explosion.getBitmap(),
                    explosion.getX(),
                    explosion.getY(),
                    paint);

            ArrayList bullets = player.getBullets();
            for (int i = 0; i < bullets.size(); i++) {
                Bullet p = (Bullet) bullets.get(i);
                canvas.drawBitmap(
                        p.getBitmap(),
                        p.getX(), p.getY(), paint);
            }

            canvas.drawBitmap(boss.getBitmap(),
                    boss.getX(),
                    boss.getY(),
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
        isPlaying = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume(){
        if(!isGameOver){
            isPlaying = true;
            gameThread = new Thread(this);
            gameThread.start();

        }
    }

    public void addHighScore(int score){
        for(int j=0 ; j<3 ; j++){
            if(highScore[j] < score){
                if(j == 0){
                    highScore[j+2] = highScore[j+1];
                    highScore[j+1] = highScore[j];
                    highScore[j] = score;
                    break;
                }
                if(j == 1){
                    highScore[j+1] = highScore[j];
                    highScore[j] = score;
                    break;
                }
                if(j == 2){
                    highScore[j] = score;
                    break;
                }
            }
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for(int k=0;k<3;k++){
            int j = k+1;
            editor.putInt("score"+j,highScore[k]);
        }
        editor.apply();
    }

    public int getScore() {
        return score;
    }

    public Player getPlayer() {
        return player;
    }

    public void gameOverDialog(){

        isGameOver = true;
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
                bestTv.setText("Best : " + highScore[0]);
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
                        Intent gIntent = new Intent(context,GameActivity.class);
                        gIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        gIntent.putExtra("PRO",pro);
                        context.startActivity(gIntent);
                    }
                });

                over_dialog.show();
            }
        });
    }

    public void lifeLineDialog(){

        isGameOver = true;
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
                        life2.setVisibility(INVISIBLE);
                        count++;
                        countMisses = 0;
                        isGameOver = false;
                        resume();
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

            }
        });
    }

    public void shootIt(){
        player.shoot();
    }

}
