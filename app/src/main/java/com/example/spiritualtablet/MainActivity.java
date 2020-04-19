package com.example.spiritualtablet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Variables for anim
    Animation topAnim, bottomAnim;
    ImageView imageView;
    TextView appName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Define Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Views
        imageView = findViewById(R.id.imageView);
        appName = findViewById(R.id.textView);

        //Setting Animation
        imageView.setAnimation(topAnim);
        appName.setAnimation(bottomAnim);

        //Going To Next Screen
        int SPLASH_TIME_OUT = 4000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoggedIn.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View,String>(imageView, "home_image");
                pairs[1] = new Pair<View,String>(appName, "home_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);

                startActivity(intent,options.toBundle());
            }
        }, SPLASH_TIME_OUT);
    }
}
