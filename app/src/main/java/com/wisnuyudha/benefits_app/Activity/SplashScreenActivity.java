package com.wisnuyudha.benefits_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.wisnuyudha.benefits_app.R;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView logo;
    Animation fadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo = findViewById(R.id.logo);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(fadeIn);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        finish();
    }
}