package com.byulvi.shushasocial;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;


public class home_shushastudy extends Fragment {

    private Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_shushastudy, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {

        ImageView iv = getView().findViewById(R.id.ivlogostudy);
        Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.t);
        iv.setAnimation(animation);


        btn = getView().findViewById(R.id.btngostudy);

        btn.setAnimation(animation);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),studyactivity.class);
                startActivity(i);
            }
        });

    }
}