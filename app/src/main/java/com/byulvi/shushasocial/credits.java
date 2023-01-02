package com.byulvi.shushasocial;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class credits  extends AppCompatActivity {
    private TextView t1,t2;
    private Animation anim;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);
        init();
    }

    private void init() {
        t1 = findViewById(R.id.tvanim1);
        t2= findViewById(R.id.tvanim2);
        anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.t);
        t1.startAnimation(anim);
        t2.startAnimation(anim);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
