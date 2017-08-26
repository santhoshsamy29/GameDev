package com.example.application.project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageButton buttonPlay;
    private ImageButton buttonHighScore;
    private ImageButton buttonHelp;
    private ImageButton sound;
    private ImageView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Audio.strt(this);


        SharedPreferences prefs = getSharedPreferences("Sound",Context.MODE_PRIVATE);
        Audio.volume = prefs.getInt("volume", 0);

        title =(ImageView)findViewById(R.id.title_img);
        buttonPlay = (ImageButton)findViewById(R.id.playNow);
        buttonHighScore = (ImageButton)findViewById(R.id.highScore);
        buttonHelp = (ImageButton)findViewById(R.id.help);
        sound = (ImageButton)findViewById(R.id.vol_button);

        title.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.title));
        buttonPlay.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.title));
        buttonHelp.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.title));
        buttonHighScore.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.title));
        sound.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.title));


        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.level,null);
                builder.setView(view);
                final AlertDialog level_dialog = builder.create();
                level_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ImageButton level1 = (ImageButton) view.findViewById(R.id.play_level1);
                ImageButton level2 = (ImageButton) view.findViewById(R.id.play_level2);
                level1.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.classic));
                level2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.arcade));
                level1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gameOnedialog();
                        level_dialog.dismiss();
                    }
                });

                level2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,GameActivity2.class);
                        startActivity(intent);
                        level_dialog.dismiss();
                    }
                });
                level_dialog.show();
            }
        });


        buttonHighScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hIntent = new Intent(MainActivity.this,HighScore.class);
                startActivity(hIntent);
            }
        });


        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hIntent = new Intent(MainActivity.this,Help.class);
                startActivity(hIntent);
            }
        });


        if(Audio.volume == 1){
            sound.setImageResource(R.drawable.audioon);
            Audio.setVolume();
        }
        else if(Audio.volume == 0){
            sound.setImageResource(R.drawable.audiooff);
            Audio.setVolume();
        }

        Audio.bgm.start();
        Audio.bgm.setLooping(true);
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Audio.volume == 1){
                    Audio.volume = 0;
                    Audio.setVolume();
                    sound.setImageResource(R.drawable.audiooff);
                }
                else if(Audio.volume == 0){
                    Audio.volume = 1;
                    Audio.setVolume();
                    sound.setImageResource(R.drawable.audioon);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_exitgame,null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog exit_dialog = builder.create();
        exit_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view.findViewById(R.id.exit_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        view.findViewById(R.id.exit_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit_dialog.dismiss();
            }
        });
        exit_dialog.show();
        exit_dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;


    }

    @Override
    protected void onPause() {

        SharedPreferences prefs = getSharedPreferences("Sound", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("volume", Audio.volume);
        editor.apply();

        if(Audio.bgm.isPlaying()){
            Audio.bgm.stop();
            try {
                Audio.bgm.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onPause();
    }

    public void gameOnedialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.diff,null);
        builder.setView(view);
        final AlertDialog diff_dialog = builder.create();
        diff_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageButton beg = (ImageButton)view.findViewById(R.id.beg);
        ImageButton pro = (ImageButton)view.findViewById(R.id.pro);
        beg.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.classic));
        pro.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.arcade));
        beg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gIntent = new Intent(MainActivity.this,GameActivity.class);
                gIntent.putExtra("PRO",false);
                startActivity(gIntent);
                diff_dialog.dismiss();
            }
        });

        pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gIntent = new Intent(MainActivity.this,GameActivity.class);
                gIntent.putExtra("PRO",true);
                startActivity(gIntent);
                diff_dialog.dismiss();
            }
        });
        diff_dialog.show();
    }
}
