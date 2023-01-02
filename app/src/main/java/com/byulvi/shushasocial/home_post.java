 package com.byulvi.shushasocial;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.byulvi.shushasocial.ui.Post;
import com.byulvi.shushasocial.ui.postadapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class home_post extends Fragment {
    private ListView listview;
    private postadapter adapter;
    //without image
    private List<Post> listdata;
    private Post listitem;
    private List<Post> listtemp;
    private Animation anim;



    private DatabaseReference mdatabase;
    private StorageReference storage;
    private String POST_KEY = "Post";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_post,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        //initialization
        init();
        //utils
        databaseread();
        setonclickitem();
    }





    private void init(){

        listview = getView().findViewById(R.id.listviewmuz);
        listdata =new ArrayList<>();
        listview.setAnimation(anim);
        listtemp =new ArrayList<>();
        adapter = new postadapter(getActivity(),R.layout.withoutpost_list_adapter,listdata,getLayoutInflater());
        mdatabase = FirebaseDatabase.getInstance().getReference("Post");


    }



    private void setonclickitem(){
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = listtemp.get(position);
                Intent i = new Intent(getActivity(),readeractivity.class);
                i.putExtra("title",post.title);
                i.putExtra("ppuri",post.uri);
                i.putExtra("publisher",post.publisher);
                i.putExtra("idofp",post.id);
                i.putExtra("text",post.post);
                i.putExtra("uiduser",post.uiduser);
                startActivity(i);

            }
        });
    }

    private void databaseread(){
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if list is not clear, wiping it
                FirebaseAuth mauth;
                mauth = FirebaseAuth.getInstance();
                FirebaseDatabase.getInstance().getReference("State").child(mauth.getCurrentUser().getUid()).setValue(true);
                if(listdata.size() > 0 )listdata.clear();
                if(listtemp.size() > 0 )listtemp.clear();
                //while size of elements in database do:
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //equals database values to user var
                    Post post = ds.getValue(Post.class);
                    listitem = new Post();
                    listitem.setTitle(post.title);
                    listitem.setUri(post.uri);
                    listitem.setUiduser(post.uiduser);
                    listitem.setPublisher(post.publisher);
                    listitem.setTimestamp(post.timestamp);
                    listitem.setId(post.id);
                    listdata.add(listitem);
                    //fills listtemp to setonclickitem to use
                    listview.setAdapter(adapter);
                    listtemp.add(post);
                }
                adapter.notifyDataSetChanged();
                listview.setSelection((int) dataSnapshot.getChildrenCount() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mdatabase.addValueEventListener(listener);

    }

}
