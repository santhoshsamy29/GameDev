package com.example.application.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class HighScore extends AppCompatActivity {

    TextView highScore1,highScore2,highScore3;
    TextView aHighScore1,aHighScore2,aHighScore3;
    ImageButton backButton;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        highScore1 = (TextView) findViewById(R.id.highscore1);
        highScore2 = (TextView) findViewById(R.id.highscore2);
        highScore3 = (TextView) findViewById(R.id.highscore3);

        aHighScore1 = (TextView) findViewById(R.id.aHighscore1);
        aHighScore2 = (TextView) findViewById(R.id.aHighscore2);
        aHighScore3 = (TextView) findViewById(R.id.aHighscore3);

        sharedPreferences  = getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);
        highScore1.setText("1. "+sharedPreferences.getInt("score1",0));
        highScore2.setText("2. "+sharedPreferences.getInt("score2",0));
        highScore3.setText("3. "+sharedPreferences.getInt("score3",0));

        sharedPreferences  = getSharedPreferences("LEVEL_TWO_HIGH", Context.MODE_PRIVATE);
        aHighScore1.setText("1. "+sharedPreferences.getInt("aScore1",0));
        aHighScore2.setText("2. "+sharedPreferences.getInt("aScore2",0));
        aHighScore3.setText("3. "+sharedPreferences.getInt("aScore3",0));

        backButton = (ImageButton)findViewById(R.id.highscore_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
