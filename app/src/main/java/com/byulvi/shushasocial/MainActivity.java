package com.byulvi.shushasocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Fragment fragment;
    private ImageView ivsettings,ivsearch;
    private Button btn_crpst;
    private Animation animation;
    private FirebaseAuth mauth;
    private TextView tvshusha;
    private DatabaseReference onlinecheckdatabase;

    @Override
    protected void onPause() {
        super.onPause();
        onlinecheckdatabase.child(mauth.getCurrentUser().getUid()).removeValue();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onlinecheckdatabase.child(mauth.getCurrentUser().getUid()).setValue(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        fragment = new home_postwimage();
        getSupportFragmentManager().beginTransaction().replace(R.id.Framelayout,fragment).commit();
        BottomNavigationView bottomnav =  findViewById(R.id.bottom_nav_view);
        bottomnav.setItemHorizontalTranslationEnabled(false);
        bottomnav.setOnNavigationItemSelectedListener(navlistener);
        onlinecheckdatabase = FirebaseDatabase.getInstance().getReference("State");
        tvshusha = findViewById(R.id.tvshushatext);
        mauth = FirebaseAuth.getInstance();
        ivsearch = findViewById(R.id.imageView6);
        ivsettings = findViewById(R.id.ivsettings);
        ivsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, profile_settings_activity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_down);

            }
        });
        ivsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,searchactivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });


    }


    private BottomNavigationView.OnNavigationItemSelectedListener navlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedfragment = null;

                    switch (item.getItemId()){
                        case R.id.menu_home:
                            if (selectedfragment != new home_postwimage()){
                                selectedfragment = new home_postwimage();
                            }
                            findViewById(R.id.constraintLayout).setVisibility(View.VISIBLE);
                            break;

                        case R.id.menu_cedvel:
                            if (selectedfragment != new home_post()){
                                selectedfragment = new home_post();
                            }
                            findViewById(R.id.constraintLayout).setVisibility(View.VISIBLE);
                            break;
                        case R.id.menu_study:
                            if (selectedfragment != new home_shushastudy()){
                                selectedfragment = new home_shushastudy();
                            }
                            findViewById(R.id.constraintLayout).setVisibility(View.INVISIBLE);
                            break;
                        case R.id.menu_groups:
                            Intent i = new Intent(MainActivity.this,groupregactivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                            break;
                        case R.id.menu_add:
                            Intent i1 = new Intent(MainActivity.this,addpostactivity.class);
                            startActivity(i1);
                            overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);
                            break;

                    }
                    if (selectedfragment != null) {
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_out_right, R.anim.slide_in_left).replace(R.id.Framelayout, selectedfragment).commit();
                    }
                    else{

                    }
                    return true;
                }
            };

    public void onClickcreatepost(View view) {
        Intent i = new Intent(MainActivity.this,addpostactivity.class);
        startActivity(i);

    }

    public void onclickprof(View view) {


    }

    public void onClicksearch(View view) {

    }
}