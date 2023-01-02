package com.byulvi.shushasocial;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;

import com.byulvi.shushasocial.ui.Postwimage;
import com.byulvi.shushasocial.ui.postwimageadapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class home_postwimage extends Fragment {
    private ListView listviewwimage;
    private postwimageadapter adapter;
    //without image
    private List<Postwimage> listdata;
    private Postwimage listitem;
    private List<Postwimage> listtemp;
    


    private DatabaseReference mdatabase;
    private StorageReference storage;
    private String POSTWIMAGE_KEY = "Postwimage";
    private Animation anim;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_postwimage,container,false);

    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        databaseread();
        setonclickitem();

    }

    private void init(){
        listviewwimage = getView().findViewById(R.id.listviewwimage);
        storage = FirebaseStorage.getInstance().getReference("Imagepost");
        listviewwimage.setAnimation(anim);
        listdata =new ArrayList<>();
        listtemp =new ArrayList<>();
        adapter = new postwimageadapter(getActivity(),R.layout.wpost_list_adapter,listdata,getLayoutInflater());


        mdatabase = FirebaseDatabase.getInstance().getReference(POSTWIMAGE_KEY);

    }

    private void setonclickitem(){
        listviewwimage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Postwimage postwimage = listtemp.get(position);
                Intent i = new Intent(getActivity(),readeractivitywpost.class);
                i.putExtra("title",postwimage.title);
                i.putExtra("text",postwimage.post);
                i.putExtra("idofpwimage",postwimage.id);
                i.putExtra("publisher",postwimage.publisher);
                i.putExtra("ppuriwpost", postwimage.uri);
                i.putExtra("uiduserwpost",postwimage.uiduser);
                i.putExtra("uri",postwimage.image_id);
                startActivity(i);

            }
        });

    }



    private void databaseread(){
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseAuth mauth;
                mauth = FirebaseAuth.getInstance();
                FirebaseDatabase.getInstance().getReference("State").child(mauth.getCurrentUser().getUid()).setValue(true);
                if(listdata.size() > 0 ){listdata.clear();}
                if(listtemp.size() > 0 )listtemp.clear();
                //while size of elements in database do:
                for (DataSnapshot ds : snapshot.getChildren()){
                    //equals database values to user var
                    Postwimage postwimage = ds.getValue(Postwimage.class);
                    assert postwimage != null;
                    listitem = new Postwimage();
                    listitem.setTitle(postwimage.title);
                    listitem.setImage_id(postwimage.image_id);
                    listitem.setUri(postwimage.uri);
                    listitem.setTimestamp(postwimage.timestamp);
                    listitem.setId(postwimage.id);
                    listitem.setUiduser(postwimage.uiduser);
                    listitem.setPublisher(postwimage.publisher);
                    listdata.add(listitem);
                    //fills listtemp to setonclickitem to use
                    listviewwimage.setAdapter(adapter);
                    listtemp.add(postwimage);
                }
                adapter.notifyDataSetChanged();
                listviewwimage.setSelection((int) snapshot.getChildrenCount() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mdatabase.addValueEventListener(listener);
    }


}