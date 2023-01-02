package com.byulvi.shushasocial.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
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
import com.byulvi.shushasocial.readeractivity;
import com.byulvi.shushasocial.readeractivitywpost;
import com.byulvi.shushasocial.readtopicactivity;
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

public class commadapter extends ArrayAdapter<Comm> {
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private List<Comm> ListItem = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    public commadapter(@NonNull Context context, int resource, List<Comm> ListItem, LayoutInflater inflater) {
        super(context, resource, ListItem);
        this.context = context;
        this.ListItem = ListItem;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String publishercheck = pref.getString("publcomm","null");
        Viewholder viewholder;
        Comm listitemmain = ListItem.get(position);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.commadapter_listview,null,false);
            viewholder = new Viewholder();
            viewholder.text = convertView.findViewById(R.id.tvcommcontent);
            viewholder.username = convertView.findViewById(R.id.tvusernamecomm);
            viewholder.username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (v.getContext() instanceof readeractivity){
                        ((readeractivity)v.getContext()).tag(listitemmain.publisher);
                    }
                    else if (v.getContext() instanceof readeractivitywpost){
                        ((readeractivitywpost)v.getContext()).tag(listitemmain.publisher);
                    }
                    else if (v.getContext() instanceof readtopicactivity ){
                        ((readtopicactivity)v.getContext()).tag(listitemmain.publisher);
                    }
                }
            });
            viewholder.ivpp = convertView.findViewById(R.id.ivcommpp);
            viewholder.ivpp.setOnClickListener(new View.OnClickListener() {
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
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });

            viewholder.ivdelete = convertView.findViewById(R.id.ivdeletecomm);
            if (listitemmain.publisher.equals(publishercheck)){
                viewholder.ivdelete.setVisibility(View.VISIBLE);
            }
            else{
                viewholder.ivdelete.setVisibility(View.GONE);
            }
            viewholder.ivdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Post");
                    builder.setMessage("Şərhi silmək istəyirsiniz? Əminsiniz?");
                    builder.setPositiveButton("Bəli", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences pref22 = PreferenceManager.getDefaultSharedPreferences(context);
                            String idofp = pref22.getString("idofp","null");
                            Query query1 = FirebaseDatabase.getInstance().getReference("Comms").child(idofp).orderByChild("commid").equalTo(listitemmain.commid);
                            query1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot ds: snapshot.getChildren()){
                                        DatabaseReference refdelete = FirebaseDatabase.getInstance().getReference("Comms").child(idofp).child(ds.getKey());
                                        refdelete.removeValue();
                                        Toast.makeText(context, "Şərh silindi!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                    builder.setNegativeButton("Xeyr", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
        else {
            viewholder = (Viewholder) convertView.getTag();
        }

        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("name").equalTo(listitemmain.publisher);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    FirebaseStorage.getInstance().getReference().child(user.uid+ " pp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).resize(100,100).into(viewholder.ivpp);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            viewholder.ivpp.setImageDrawable(context.getDrawable(R.drawable.shushauser));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewholder.username.setText(listitemmain.getPublisher());
        viewholder.text.setText(listitemmain.getComm());
        convertView.setTag(viewholder);
        return convertView;
    }


    public class Viewholder{
        TextView username;
        TextView text;
        ImageView ivpp,ivdelete;
    }
}
