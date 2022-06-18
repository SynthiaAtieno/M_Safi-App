package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.myapplication.databinding.ActivitySplashScreenBinding;

public class Splash_Screen extends AppCompatActivity {
    ActivitySplashScreenBinding splash;
    ImageView largeImage, logo, textImage;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splash=ActivitySplashScreenBinding.inflate(getLayoutInflater());
        View view = splash.getRoot();
        setContentView(view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lottieAnimationView=findViewById(R.id.lottie);
                largeImage = findViewById(R.id.large_image);

                logo = findViewById(R.id.logo);
                textImage = findViewById(R.id.text_image);

                largeImage.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);
                logo.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
                textImage.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
                lottieAnimationView.animate().translationY(1400).setDuration(4000).setStartDelay(4000);
                startActivity(new Intent(Splash_Screen.this, Welcome_Activity.class));
                finish();
                overridePendingTransition(R.anim.push_down_out, R.anim.push_down_in);
            }
        }, 3000);



    }
}