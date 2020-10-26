package com.project.mafalda.vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.project.mafalda.MainActivity;
import com.project.mafalda.R;

public class SplashBienvenida extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_bienvenida);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashBienvenida.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }
}
