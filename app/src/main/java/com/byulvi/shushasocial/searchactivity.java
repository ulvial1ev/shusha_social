package com.byulvi.shushasocial;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.byulvi.shushasocial.ui.User;
import com.byulvi.shushasocial.ui.searchadapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class searchactivity extends AppCompatActivity {
    private ListView listview;
    private EditText edsearch;
    private DatabaseReference mdatabase;

    private List<User> listdata;
    private User listitem;
    private List<User> listtemp;
    private searchadapter adapter;
    private Animation anim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlayout);
        listview = findViewById(R.id.listofusers);
        anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.t1);
        listview.setAnimation(anim);
        mdatabase = FirebaseDatabase.getInstance().getReference();
        edsearch = findViewById(R.id.edsearch);
        listview = findViewById(R.id.listofusers);
        listdata =new ArrayList<>();
        listtemp =new ArrayList<>();
        adapter = new searchadapter(getApplicationContext(),R.layout.searchadapter_listview,listdata,getLayoutInflater());
        databaseread();
        setonclickitem();
        edsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchusers(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setonclickitem() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = listtemp.get(position);
                Intent i = new Intent(searchactivity.this,showuserlayout.class);
                i.putExtra("mail",user.mail);
                i.putExtra("name",user.name);
                i.putExtra("school",user.school);
                i.putExtra("uid",user.uid);
                startActivity(i);
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            }
        });
    }

    private void searchusers(String s){                                                 //at work
        Query query = mdatabase.child("Users").orderByChild("name").startAt(s).endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listdata.clear();
                listtemp.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    listitem = new User();
                    listitem.setMail(user.mail);
                    listitem.setName(user.name);
                    listitem.setUid(user.uid);
                    listitem.setSchool(user.school);
                    listview.setAdapter(adapter);
                    listdata.add(listitem);
                    listtemp.add(user);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void databaseread(){
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if list is not clear, wiping it
                if (edsearch.getText().toString().equals("")){
                if(listdata.size() > 0 ){listdata.clear();}
                if(listtemp.size() > 0 )listdata.clear();
                //while size of elements in database do:
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //equals database values to user var
                    User user = ds.getValue(User.class);
                    listitem = new User();
                    listitem.setMail(user.mail);
                    listitem.setName(user.name);
                    listitem.setUid(user.uid);
                    listitem.setSchool(user.school);
                    listdata.add(listitem);
                    //fills listtemp to setonclickitem to use
                    listview.setAdapter(adapter);
                    listtemp.add(user);
                }
                adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mdatabase.child("Users").addValueEventListener(listener);
    }
}
