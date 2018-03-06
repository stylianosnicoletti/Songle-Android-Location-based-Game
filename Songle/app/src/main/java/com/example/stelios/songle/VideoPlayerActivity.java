package com.example.stelios.songle;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;



public class VideoPlayerActivity extends Activity implements MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener,View.OnTouchListener {

    // FOR VIDEO VIEW
    private VideoView mVV;

    // FOR MEDIA CONTROLLER
    MediaController mediaController = null;

    @Override
    public void onCreate(Bundle b) {

        super.onCreate(b);

        // Set content view of activity_help.xml
        setContentView(R.layout.activity_help);

        int fileRes = 0;
        Bundle e = getIntent().getExtras();
        if (e != null) {
            fileRes = e.getInt("fileRes");
        }

        // Set Views and Listeners
        mVV = (VideoView) findViewById(R.id.myvideoview);
        mVV.setOnCompletionListener(this);
        mVV.setOnPreparedListener(this);
        mVV.setOnTouchListener(this);

        if (!playFileRes(fileRes)) return;


        // Set Media Controller
        mediaController = new MediaController(mVV.getContext());
        mediaController.setMediaPlayer(mVV);
        mediaController.setAnchorView(mVV);
        mVV.setMediaController(mediaController);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int fileRes = 0;
        Bundle e = getIntent().getExtras();
        if (e != null) {
            fileRes = e.getInt("fileRes");
        }
        playFileRes(fileRes);
    }

    private boolean playFileRes(int fileRes) {
        if (fileRes == 0) {
            stopPlaying();
            return false;
        } else {
            mVV.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + fileRes));
            return true;
        }
    }

    // For stopping video playback
    public void stopPlaying() {
        mVV.stopPlayback();
        this.finish();
    }

    // When Video ends
    @Override
    public void onCompletion(MediaPlayer mp) {

        // Finish Activity
        finish();
    }

    // On Video View Touch
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // Display Media Controller for 5 seconds
        mediaController.show(5000);
        return true;
    }

    // On prepared
    @Override
    public void onPrepared(MediaPlayer mp) {

        // Start Video
        mVV.start();

        // Display Media Controller for 5 seconds
        mediaController.show(5000);
    }
}

