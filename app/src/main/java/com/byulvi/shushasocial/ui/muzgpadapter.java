package com.byulvi.shushasocial.ui;

import android.content.Context;
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
import com.byulvi.shushasocial.contentgroupactivity;
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

public class muzgpadapter extends ArrayAdapter<Muzgpclass> {
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private List<Muzgpclass> ListItem = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private boolean online;

    public muzgpadapter(@NonNull Context context, int resource, List<Muzgpclass> ListItem, LayoutInflater inflater) {
        super(context, resource, ListItem);
        this.context = context;
        this.ListItem = ListItem;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Viewholder viewholder;
        Muzgpclass listitemmain = ListItem.get(position);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.commadapter_listview,null,false);
            viewholder = new Viewholder();
            viewholder.text = convertView.findViewById(R.id.tvcommcontent);
            viewholder.username = convertView.findViewById(R.id.tvusernamecomm);
            viewholder.online = convertView.findViewById(R.id.onlinecomm);
            viewholder.username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((contentgroupactivity) context).tag(listitemmain.publisher);
                }

            });
            viewholder.ivpp = convertView.findViewById(R.id.ivcommpp);
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
        if (context instanceof contentgroupactivity){
        DatabaseReference onlineref = FirebaseDatabase.getInstance().getReference("State");
        onlineref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(listitemmain.id).exists()){
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
        }

        viewholder.username.setText(listitemmain.getPublisher());
        viewholder.text.setText(listitemmain.getComm());
        convertView.setTag(viewholder);
        return convertView;
    }

    public class Viewholder{
        TextView username;
        TextView text;
        ImageView ivpp,online;
    }
}
