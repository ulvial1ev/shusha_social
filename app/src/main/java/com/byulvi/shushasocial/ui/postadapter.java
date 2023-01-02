package com.byulvi.shushasocial.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.byulvi.shushasocial.R;
import com.byulvi.shushasocial.readeractivity;
import com.byulvi.shushasocial.showuserlayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class postadapter extends ArrayAdapter<Post> {
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private List<Post> ListItem = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    private Boolean online =false;


    public postadapter(@NonNull Context context, int resource,List<Post> ListItem, LayoutInflater inflater) {
        super(context, resource, ListItem);
        this.ListItem = ListItem;
        this.inflater = inflater;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Viewholder viewholder;
        Post listitemmain = ListItem.get(position);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.withoutpost_list_adapter,null,false);
            viewholder = new Viewholder();

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("name").equalTo(listitemmain.publisher);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()){
                                User user = ds.getValue(User.class);
                                Intent i = new Intent(context, showuserlayout.class);
                                i.putExtra("name",user.name);
                                i.putExtra("school",user.school);
                                i.putExtra("mail",user.mail);
                                i.putExtra("uid",user.uid);
                                context.startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            };

            viewholder.title = convertView.findViewById(R.id.tvTitle);
            viewholder.mail = convertView.findViewById(R.id.tvUsername);
            viewholder.stamp = convertView.findViewById(R.id.tvts);
            viewholder.like = convertView.findViewById(R.id.ivlike);
            viewholder.pp = convertView.findViewById(R.id.ivtopiclogo);
            viewholder.pp.setOnClickListener(listener);
            viewholder.mail.setOnClickListener(listener);
            viewholder.online = convertView.findViewById(R.id.onlineadp);
            viewholder.count = convertView.findViewById(R.id.tvcount);
            viewholder.countcomm = convertView.findViewById(R.id.tvcommcount);
            viewholder.comm = convertView.findViewById(R.id.ivcommclick);
            viewholder.comm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context,readeractivity.class);
                    i.putExtra("title",listitemmain.title);
                    i.putExtra("ppuri",listitemmain.uri);
                    i.putExtra("publisher",listitemmain.publisher);
                    i.putExtra("idofp",listitemmain.id);
                    i.putExtra("uiduser",listitemmain.uiduser);
                    i.putExtra("text",listitemmain.post);
                    context.startActivity(i);
                }
            });




        }
        else {
            viewholder = (Viewholder) convertView.getTag();
        }
        islikes(listitemmain.id,viewholder.like);
        numlikes(viewholder.count,listitemmain.id);
        numcomms(viewholder.countcomm,listitemmain.id);


        viewholder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewholder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(listitemmain.getId()).child(mauth.getUid()).setValue(true);
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(listitemmain.getId()).child(mauth.getUid()).removeValue();
                }
            }
        });

        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.transperent));
        progressDialog.setContentView(R.layout.progressdialog);
        FirebaseStorage.getInstance().getReference().child(listitemmain.uiduser+ " pp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).resize(100,100).centerCrop().into(viewholder.pp);
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                viewholder.pp.setImageDrawable(context.getDrawable(R.drawable.shushauser));
                progressDialog.dismiss();
            }
        });

        DatabaseReference onlineref = FirebaseDatabase.getInstance().getReference("State");
        onlineref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(listitemmain.uiduser).exists()){
                    online =true;
                    viewholder.online.setVisibility(View.VISIBLE);
                }
                else{
                    online = false;
                    viewholder.online.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query query1 = FirebaseDatabase.getInstance().getReference("Users").orderByChild("name").equalTo(listitemmain.publisher);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    if (user.mail.equals("Admin")){
                        viewholder.mail.setBackground(context.getResources().getDrawable(R.drawable.edittextstyle));


                    }
                    else{
                        viewholder.mail.setBackground(context.getResources().getDrawable(R.drawable.transperent));

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewholder.mail.setText(listitemmain.publisher);
        viewholder.title.setText(listitemmain.getTitle());
        viewholder.stamp.setText(listitemmain.timestamp);


        convertView.setTag(viewholder);
        return convertView;
    }



    private void numcomms(TextView counter,String postid) {
        FirebaseDatabase.getInstance().getReference("Comms").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                counter.setText(snapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void islikes(String postid ,ImageView iv){
        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(mauth.getUid()).exists()){
                    iv.setImageResource(R.drawable.like_fill);
                    iv.setTag("liked");
                }
                else{
                    iv.setImageResource(R.drawable.like);
                    iv.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void numlikes(TextView likes,String postid){
        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount() +"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    private class Viewholder{
        TextView mail;
        TextView title,stamp;
        TextView count,countcomm;
        ImageView like,comm,online;
        ImageView pp;
    }
}
