package com.example.stelios.songle;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity{

    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        //Sleep for 2 seconds in welcome screen
        SystemClock.sleep(2000);

        // Take user to Login Screen
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
