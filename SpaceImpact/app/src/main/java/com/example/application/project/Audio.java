package com.example.application.project;


import android.content.Context;
import android.media.MediaPlayer;

public class Audio {

    static MediaPlayer laser = new MediaPlayer();
    static MediaPlayer explosion = new MediaPlayer();
    static MediaPlayer bgm = new MediaPlayer();
    static int volume = 1;

    public static void strt(Context context){
        laser = MediaPlayer.create(context,R.raw.laser);
        explosion = MediaPlayer.create(context,R.raw.explode);
        bgm = MediaPlayer.create(context,R.raw.bgm);
    }

    public static void setVolume() {
        laser.setVolume(volume, volume);
        explosion.setVolume(volume, volume);
        bgm.setVolume(volume, volume);
    }

}
