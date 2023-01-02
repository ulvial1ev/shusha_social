package com.byulvi.shushasocial;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Loginactivity extends AppCompatActivity {
    private EditText edlogin,edpwd;
    private ImageView ivlogo;
    private TextView w1,w2, t1, t2;
    private Button b1;
    private Animation a1,a2;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mAuth = FirebaseAuth.getInstance();
        a1= AnimationUtils.loadAnimation(this,R.anim.logoanim);
        a2 = AnimationUtils.loadAnimation(this,R.anim.t1);
        edlogin = findViewById(R.id.edlogin);
        w1 = findViewById(R.id.tvwelcome);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        b1 = findViewById(R.id.b1);
        ivlogo = findViewById(R.id.logo1);
        w2= findViewById(R.id.tvwelcomeuname);
        edpwd = findViewById(R.id.edpwd);


    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser = mAuth.getCurrentUser();
        if (currentuser != null){
            if (mAuth.getCurrentUser().isEmailVerified()){
            w1.setVisibility(View.VISIBLE);
            w1.startAnimation(a1);
            w2.setText(mAuth.getCurrentUser().getEmail());
            w2.setVisibility(View.VISIBLE);
            w2.startAnimation(a1);
            ivlogo.startAnimation(a1);
            t1.setVisibility(View.GONE);
            t2.setVisibility(View.GONE);
            b1.setVisibility(View.GONE);
            edpwd.setVisibility(View.GONE);
            edlogin.setVisibility(View.GONE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent i = new Intent(Loginactivity.this, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }

            }).start();}
            else{
                startActivity(new Intent(Loginactivity.this,Regactivity.class));
            }

        }
        else{
            w1.setVisibility(View.GONE);
            w2.setVisibility(View.GONE);
            t1.setVisibility(View.VISIBLE);
            t2.setVisibility(View.VISIBLE);
            b1.setVisibility(View.VISIBLE);
            edpwd.setVisibility(View.VISIBLE);
            edlogin.setVisibility(View.VISIBLE);
            t1.setAnimation(a2);
            edlogin.setAnimation(a2);
            edpwd.setAnimation(a2);
            b1.setAnimation(a2);
            ivlogo.setAnimation(a2);

        }
    }

    public void OnClickLogin(View view) {
        String login = edlogin.getText().toString();
        String pwd = edpwd.getText().toString();

        if (!TextUtils.isEmpty(login) && !TextUtils.isEmpty(pwd)){
            mAuth.signInWithEmailAndPassword(login,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        if (mAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(Loginactivity.this,MainActivity.class));
                        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                        Toast.makeText(Loginactivity.this, "Xoş Gəldiniz " + login + "!", Toast.LENGTH_SHORT).show();
                        finish();
                        }
                        else{
                            startActivity(new Intent(Loginactivity.this,Regactivity.class));
                            overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                        }
                    }
                    else{
                        Toast.makeText(Loginactivity.this, "Belə bir istifadəçi tapılmadı, adminə müraciət edin!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "Xanaları doldurun", Toast.LENGTH_SHORT).show();
        }
    }


    public void onClickregtext(View view) {
        Intent i = new Intent(Loginactivity.this,Regactivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);

    }

}
