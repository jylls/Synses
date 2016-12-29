package com.example.jl10015.opencvtutorial;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class MyIntentService extends Service {

    private static final String TAG = "com.example";
    MediaPlayer mediaPlayer;
    //public int mcurrentPos ;
    public static int ma;

    @Override
    public void onCreate() {
        mediaPlayer = MediaPlayer.create(this, R.raw.am);// raw/s.mp3

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //final MediaPlayer mediaPlayer;
        //mediaPlayer = MediaPlayer.create(this, R.raw.am);

        final int currentId = startId;

        Log.v(TAG,"bla" + ma);
        mediaPlayer.seekTo(ma);
        Runnable r = new Runnable() {

            public void run() {
                mediaPlayer.start();


            }

    };

    Thread t = new Thread(r);
    t.start();
    return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent arg0) {

        return null;
    }





    @Override
    public void onDestroy() {
        if (mediaPlayer.isPlaying()) {

            mediaPlayer.pause();
            ma = mediaPlayer.getCurrentPosition();
            //Log.v(TAG,"bla" + a);


        }
            // service doesn't play from seek
            mediaPlayer.release();


    }



}








