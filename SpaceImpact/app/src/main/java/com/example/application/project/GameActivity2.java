package com.example.application.project;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class GameActivity2 extends AppCompatActivity {

    LinearLayout linearLayout;
    GameView2 gameView2;
    ImageButton shootButton,pauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);

        overridePendingTransition(0,0);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        linearLayout = (LinearLayout)findViewById(R.id.game2_ll);

        gameView2 = new GameView2(this,size.x,size.y);
        linearLayout.addView(gameView2);

        shootButton = (ImageButton)findViewById(R.id.level2_shoot);
        shootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView2.shootIt();
            }
        });


        pauseButton = (ImageButton)findViewById(R.id.level2_pause);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameView2.pause();

                final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity2.this);
                View view = LayoutInflater.from(GameActivity2.this).inflate(R.layout.activity_pause,null);
                builder.setView(view);
                builder.setCancelable(false);
                final AlertDialog pause_dialog = builder.create();
                pause_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                view.findViewById(R.id.home_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int tempScore = gameView2.getScore();
                        gameView2.addHighScore(tempScore);
                        Intent mIntent = new Intent(GameActivity2.this,MainActivity.class);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mIntent);
                    }
                });
                view.findViewById(R.id.play_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        gameView2.resume();
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
        gameView2.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView2.resume();
    }

    @Override
    public void onBackPressed() {
        gameView2.pause();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(GameActivity2.this).inflate(R.layout.activity_endgame,null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog end_dialog = builder.create();
        end_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view.findViewById(R.id.end_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tempScore = gameView2.getScore();
                gameView2.addHighScore(tempScore);
                Intent mIntent = new Intent(GameActivity2.this,MainActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mIntent);

            }
        });

        view.findViewById(R.id.end_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameView2.resume();
                end_dialog.dismiss();

            }
        });
        end_dialog.show();
    }
}
