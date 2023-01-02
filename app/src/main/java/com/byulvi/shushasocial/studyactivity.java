package com.byulvi.shushasocial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.byulvi.shushasocial.ui.topicadapter;
import com.byulvi.shushasocial.ui.topicclass;

import java.util.ArrayList;
import java.util.List;

public class studyactivity extends AppCompatActivity {
    private ListView listview;
    private List<topicclass> listdata ;
    private topicclass listItem;

    private String[] name,usage;
    private topicadapter adapter;

    private int[] logo = {R.drawable.math,R.drawable.aze,R.drawable.edeb,R.drawable.physics,R.drawable.chem,R.drawable.eng,R.drawable.cog,R.drawable.bio};



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studylayout);

        listview = findViewById(R.id.listtopics);
        name =getResources().getStringArray(R.array.topic_name);
        usage = getResources().getStringArray(R.array.usage);


        listdata = new ArrayList<>();

        for (int i = 0; i < name.length; i++){
            listItem = new topicclass();
            //Sets info from listClass.java(box)
            listItem.setTopic(name[i]);
            listItem.setUsage(usage[i]);
            listItem.setLogo(logo[i]);
            listdata.add(listItem);
        }

        adapter = new topicadapter(studyactivity.this,R.layout.topic_list_view,listdata,getLayoutInflater());

        listview.setAdapter(adapter);

        onclickitem();

    }

    private void onclickitem(){
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = position;
                Intent i = new Intent(studyactivity.this,readtopicactivity.class);
                i.putExtra("index",index);
                startActivity(i);
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);

            }
        });
    }

    public void onclickback(View view) {
        finish();
    }
}
