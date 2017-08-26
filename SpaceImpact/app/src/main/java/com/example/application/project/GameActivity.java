package com.example.application.project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    ImageButton upButton,downButton,pauseButton;
    static ImageButton shootButton1;

    LinearLayout LL;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        overridePendingTransition(0,0);

        boolean extra = getIntent().getExtras().getBoolean("PRO");


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        LL = (LinearLayout) findViewById(R.id.lLayout);

        gameView = new GameView(this,size.x,size.y);
        if(extra){
            gameView.pro = true;
        }
        gameView.setClickable(true);
        LL.addView(gameView);


        upButton = (ImageButton) findViewById(R.id.up_button);
        downButton = (ImageButton) findViewById(R.id.down_button);
        pauseButton = (ImageButton) findViewById(R.id.pause_button);
        shootButton1 = (ImageButton) findViewById(R.id.shoot_button1);

        upButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        gameView.getPlayer().stopUpBoosting();
                        break;
                    case MotionEvent.ACTION_DOWN:
                        gameView.getPlayer().setUpBoosting();
                        break;
                }
                return true;
            }
        });

        downButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        gameView.getPlayer().stopDownBoosting();
                        break;
                    case MotionEvent.ACTION_DOWN:
                        gameView.getPlayer().setDownBoosting();
                        break;
                }
                return true;
            }
        });



        shootButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.shootIt();
            }
        });


        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.pause();

                final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                View view = LayoutInflater.from(GameActivity.this).inflate(R.layout.activity_pause,null);
                builder.setView(view);
                builder.setCancelable(false);
                final AlertDialog pause_dialog = builder.create();
                pause_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                view.findViewById(R.id.home_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int tempScore = gameView.getScore();
                        gameView.addHighScore(tempScore);
                        Intent mIntent = new Intent(GameActivity.this,MainActivity.class);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mIntent);
                    }
                });
                view.findViewById(R.id.play_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        gameView.resume();
                        pause_dialog.dismiss();

                    }
                });
                pause_dialog.show();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    public void onBackPressed() {
        gameView.pause();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(GameActivity.this).inflate(R.layout.activity_endgame,null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog end_dialog = builder.create();
        end_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view.findViewById(R.id.end_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tempScore = gameView.getScore();
                gameView.addHighScore(tempScore);
                Intent mIntent = new Intent(GameActivity.this,MainActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mIntent);

            }
        });

        view.findViewById(R.id.end_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameView.resume();
                end_dialog.dismiss();

            }
        });
        end_dialog.show();
    }
}