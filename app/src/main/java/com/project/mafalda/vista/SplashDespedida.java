package com.project.mafalda.vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import com.project.mafalda.R;

public class SplashDespedida extends AppCompatActivity {
    private final int duracion = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash_despedida);
        //MediaPlayer mp = MediaPlayer.create(getApplicationContext(),R.raw.gracias);
        //mp.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            };
        },duracion);

    }

}
