package com.byulvi.shushasocial.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.byulvi.shushasocial.readeractivitywpost;
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
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class postwimageadapter extends ArrayAdapter<Postwimage> {
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private List<Postwimage> ListItem = new ArrayList<>();
    private SharedPreferences spref;
    private LayoutInflater inflater;
    private Context context;
    private boolean online = false;
    private static final int defaultimage =R.drawable.transperent;


    public postwimageadapter(@NonNull Context context, int resource, List<Postwimage> ListItem, LayoutInflater inflater) {
        super(context, resource, ListItem);
        this.context = context;
        this.inflater = inflater;
        this.ListItem = ListItem;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Viewholder viewholder;
        Postwimage listitemmain = ListItem.get(position);
        initimageloader();
        if (convertView == null){
            convertView = inflater.inflate(R.layout.wpost_list_adapter,null,false);
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

            viewholder.mail= convertView.findViewById(R.id.tvusernamewpost);
            viewholder.title = convertView.findViewById(R.id.tvtitlewithpost);
            viewholder.like = convertView.findViewById(R.id.ivlikepost);
            viewholder.pp = convertView.findViewById(R.id.ivppadapterwpost);
            viewholder.pp.setOnClickListener(listener);
            viewholder.mail.setOnClickListener(listener);
            viewholder.image = convertView.findViewById(R.id.displayinpostimage);
            viewholder.stamp = convertView.findViewById(R.id.tvtswpost);
            viewholder.online = convertView.findViewById(R.id.onlineadppost);
            viewholder.count = convertView.findViewById(R.id.ivcountpost);
            viewholder.comm = convertView.findViewById(R.id.ivcommpost);
            viewholder.countcomm = convertView.findViewById(R.id.tvcommcountpost);
            viewholder.comm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, readeractivitywpost.class);
                    i.putExtra("title",listitemmain.title);
                    i.putExtra("text",listitemmain.post);
                    i.putExtra("idofpwimage",listitemmain.id);
                    i.putExtra("publisher",listitemmain.publisher);
                    i.putExtra("uiduserwpost",listitemmain.uiduser);
                    i.putExtra("ppuriwpost", listitemmain.uri);
                    i.putExtra("uri",listitemmain.image_id);
                    context.startActivity(i);
                }
            });

        }
        else{
            viewholder = (Viewholder)convertView.getTag();
        }

        viewholder.stamp.setText(listitemmain.timestamp);

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
        FirebaseStorage.getInstance().getReference().child(listitemmain.uiduser+ " pp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).resize(100,100).centerCrop().into(viewholder.pp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                viewholder.pp.setImageDrawable(context.getDrawable(R.drawable.shushauser));
            }
        });

        viewholder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // Create global configuration and initialize ImageLoader with this config
        UniversalImageAdapter.setimage(listitemmain.image_id,viewholder.image);


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

        viewholder.mail.setText(listitemmain.getPublisher());
        viewholder.title.setText(listitemmain.getTitle());
        convertView.setTag(viewholder);
        return convertView;

    }

    private void numcomms(TextView countcomm, String id) {
        FirebaseDatabase.getInstance().getReference("Comms").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                countcomm.setText(snapshot.getChildrenCount() +"");

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

    public  void initimageloader(){
        UniversalImageAdapter universalImageAdapter = new UniversalImageAdapter(context);
        ImageLoader.getInstance().init(universalImageAdapter.getconfig());
    }


    public class Viewholder{
        TextView mail,stamp;
        TextView count,countcomm;
        TextView title;
        ImageView image,pp,online;
        ImageView like,comm;
    }
}
