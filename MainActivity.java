package com.example.jl10015.opencvtutorial;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements  OnTouchListener, CvCameraViewListener2 {

    private CameraBridgeViewBase mOpenCvCameraView;
    private Mat mRgba;

    public Scalar mMeanRgba;


    //private MediaPlayer mediaPlayer;
    //BackgroundSound mBackgroundSound = new BackgroundSound();

    Intent intent;


    TextView mean;



    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(MainActivity.this);

                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        mean = (TextView) findViewById(R.id.mean);
        //MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.am);
        //startService();

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.opencv_tutorial_activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        intent= new Intent(this, MyIntentService.class);
        //startService(intent);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
       // mBackgroundSound.execute();

        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
            stopService(intent);

    }

    @Override
    public void onCameraViewStarted(int width, int height) {


        mRgba = new Mat();
        mMeanRgba = new Scalar(255);

    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba= inputFrame.rgba();
        return mRgba;

    }

    @Override
        public boolean onTouch (View v, MotionEvent event){


            Mat rgba = mRgba;

            Mat gray_image= new Mat();

            Imgproc.cvtColor(rgba,gray_image , Imgproc.COLOR_BGRA2GRAY);

            mMeanRgba = Core.sumElems(gray_image);

            int pointCount = rgba.width() * rgba.height();
            mMeanRgba.val[0] /= pointCount;

            mean.setText("Mean: " + String.valueOf((int) mMeanRgba.val[0]));

            if((int) mMeanRgba.val[0]>61 ){
            stopService(intent);

            }

            else if((int) mMeanRgba.val[0]<=61 && !isMyServiceRunning(MyIntentService.class)){
            startService(intent);
            }




            return false;
        }






    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }






    /*
    public void startService() {
        startService(new Intent(this, MyIntentService.class));
    }

    public void stopService() {
        stopService(new Intent(this, MyIntentService.class));

    }*/

    /*
    public class BackgroundSound extends AsyncTask<Void, Void, Void> {

        @Override
        public Void doInBackground(Void... params) {
            MediaPlayer player = MediaPlayer.create(MainActivity.this, R.raw.am);
            //player.setLooping(true); // Set looping
            player.setVolume(1.0f, 1.0f);
            player.start();

            if (isCancelled()){
                player.stop();
            }

            return null;
        }

    }*/



}