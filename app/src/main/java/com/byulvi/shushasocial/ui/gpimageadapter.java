package com.byulvi.shushasocial.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.byulvi.shushasocial.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class gpimageadapter extends ArrayAdapter<gpimage> {
    public LayoutInflater inflater;
    public Context context;
    public List<gpimage> listItem = new ArrayList<>();
    private Boolean check = false;
    private String key;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();

    public gpimageadapter(@NonNull Context context, int resource, List<gpimage> listItem, LayoutInflater inflater) {
        super(context, resource,listItem);
        this.context = context;
        this.listItem = listItem;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Viewholder viewholder;
        gpimage listitemmain = listItem.get(position);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.gpimage_list_item,null,false);
            viewholder = new Viewholder();
            viewholder.tvname = convertView.findViewById(R.id.tvgpname);
            viewholder.ivphoto = convertView.findViewById(R.id.ivphotogp);
            viewholder.stamp = convertView.findViewById(R.id.tvstamp);
            viewholder.ivpp = convertView.findViewById(R.id.ivppgp);
            viewholder.ivdelete = convertView.findViewById(R.id.ivdelete);
        }
        else {
            viewholder = (Viewholder)convertView.getTag();
        }

        FirebaseStorage.getInstance().getReference().child(listitemmain.id + " pp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).resize(100,100).centerCrop().into(viewholder.ivpp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                viewholder.ivpp.setImageDrawable(context.getResources().getDrawable(R.drawable.shushauser));
            }
        });

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String gpname = pref.getString("gpname","null");


        viewholder.tvname.setText(listitemmain.publisher);
        viewholder.stamp.setText(listitemmain.timestamp);
        if (listitemmain.id.equals(mauth.getCurrentUser().getUid())){
            viewholder.ivdelete.setVisibility(View.VISIBLE);
        }
        else{
            viewholder.ivdelete.setVisibility(View.GONE);
        }
        viewholder.ivdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG",listitemmain.postid);
                Log.d("TAG",gpname);
                Query query = FirebaseDatabase.getInstance().getReference().child("gp" + gpname).orderByChild("postid").equalTo(listitemmain.postid);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            FirebaseStorage.getInstance().getReferenceFromUrl(listitemmain.image_id).delete();
                            FirebaseDatabase.getInstance().getReference("gp" + gpname).child(ds.getKey()).removeValue();
                            Toast.makeText(context, "Post silindi!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });

        Picasso.get().load(listitemmain.image_id).resize(500,500).centerCrop().into(viewholder.ivphoto);

        convertView.setTag(viewholder);
        return convertView;
    }

    public class Viewholder{
        public TextView tvname,stamp;
        public ImageView ivphoto,ivdelete;
        public ImageView ivpp;
    }
}

