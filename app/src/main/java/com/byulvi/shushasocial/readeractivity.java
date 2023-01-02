package com.byulvi.shushasocial;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.byulvi.shushasocial.ui.Comm;
import com.byulvi.shushasocial.ui.Post;
import com.byulvi.shushasocial.ui.User;
import com.byulvi.shushasocial.ui.commadapter;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class readeractivity extends AppCompatActivity {
    private TextView tvusername,tvtitle,tvtext;
    private String COMM_KEY = "Comms", postid;
    private ImageView ivpp;
    private FirebaseAuth mauth;
    private DatabaseReference mdatabase;
    private SharedPreferences spref;
    private EditText edcomm;
    private ArrayAdapter<Comm> adapter;
    private Comm listitem;
    private List<Comm> listdata;
    private String check;
    private Boolean isadmin = false,online = false;
    private ListView listview;
    private DatabaseReference onlinecheckdatabase;
    private String publisher;
    private CircleImageView ivonline;
    private Intent i;

    public void tag(String tag) {
        edcomm.append("@"+ tag);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onlinecheckdatabase.child(mauth.getCurrentUser().getUid()).removeValue();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        onlinecheckdatabase.child(mauth.getCurrentUser().getUid()).removeValue();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onlinecheckdatabase.child(mauth.getCurrentUser().getUid()).setValue(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reader_layout);
        init();
        checkmail();
        checkadmin();
        databaseread();
        onlinecheckdatabase.child(mauth.getCurrentUser().getUid()).setValue(true);


    }



    private void init()
    {
        i= getIntent();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("idofp",i.getStringExtra("idofp"));
        edit.commit();
        spref = PreferenceManager.getDefaultSharedPreferences(this);
        mauth = FirebaseAuth.getInstance();
        tvusername = findViewById(R.id.tvUsernamereader);
        listview = findViewById(R.id.listviewcomment);
        ivonline = findViewById(R.id.online);
        ivpp = findViewById(R.id.ivppreader);
        listdata = new ArrayList<>();
        adapter = new commadapter(getApplicationContext(),R.layout.commadapter_listview,listdata,getLayoutInflater());
        edcomm = findViewById(R.id.edmuz);
        onlinecheckdatabase = FirebaseDatabase.getInstance().getReference("State");
        tvtitle = findViewById(R.id.tvTitlereader);
        mdatabase = FirebaseDatabase.getInstance().getReference(COMM_KEY);
        tvtext = findViewById(R.id.tvTextreader);
        tvtext.setText(i.getStringExtra("text"));
        tvtitle.setText(i.getStringExtra("title"));
        FirebaseStorage.getInstance().getReference().child(i.getStringExtra("uiduser") + " pp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).resize(100,100).centerCrop().into(ivpp);
            }
        });
        tvusername.setText(i.getStringExtra("publisher"));



        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("uid").equalTo(mauth.getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    String publ = user.name;
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("publcomm",publ);
                    edit.commit();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void checkadmin() {
        Query query1 = FirebaseDatabase.getInstance().getReference("Users").orderByChild("uid").equalTo(mauth.getCurrentUser().getUid());
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    if (user.mail.equals("Admin")){
                        isadmin = true;
                    }
                    else{
                        isadmin = false;

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkmail(){
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("name").equalTo(tvusername.getText().toString());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    check = user.mail;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void Onclicksendcomm(View view) {

        if (!TextUtils.isEmpty(edcomm.getText().toString())){
            SharedPreferences prefget = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            publisher = prefget.getString("publcomm","null");
            String contain = edcomm.getText().toString();
            String id = mauth.getCurrentUser().getUid();
            String commid = System.currentTimeMillis() + "comm";
            Comm comm = new Comm(id,contain,publisher,commid);
            mdatabase.child(i.getStringExtra("idofp")).push().setValue(comm);
            edcomm.setText("");
            Toast.makeText(this, "Şərhiniz uğurla yerləşdirildi", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Şərh yazın...", Toast.LENGTH_SHORT).show();
        }
    }
    private void databaseread(){
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listdata.size() > 0 ){listdata.clear();}
                for (DataSnapshot ds : snapshot.child(i.getStringExtra("idofp")).getChildren()){
                    //equals database values to user var
                    Comm comm = ds.getValue(Comm.class);
                    assert comm != null;
                    listitem = new Comm();
                    listitem.setPublisher(comm.publisher);
                    listitem.setComm(comm.comm);
                    listitem.setCommid(comm.commid);
                    listdata.add(listitem);

                    //fills listtemp to setonclickitem to use
                    listview.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
                listview.setSelection((int) snapshot.getChildrenCount() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mdatabase.addValueEventListener(listener);

        onlinecheckdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(i.getStringExtra("uiduser")).exists()){
                    online = true;
                    ivonline.setVisibility(View.VISIBLE);

                }
                else{
                    online = false;
                    ivonline.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("name").equalTo(tvusername.getText().toString());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    User user = ds.getValue(User.class);


                    FirebaseStorage.getInstance().getReference().child(user.uid + " pp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).resize(100,100).centerCrop().into(ivpp);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ivpp.setImageDrawable(getResources().getDrawable(R.drawable.shushauser));
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void onclickdelete(View view) {
        if (mauth.getCurrentUser().getEmail().equals(check) || isadmin ){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Post");
            DatabaseReference refcom = FirebaseDatabase.getInstance().getReference("Comms");
            DatabaseReference reflikes = FirebaseDatabase.getInstance().getReference("Likes");
            Query query = ref.orderByChild("id").equalTo(i.getStringExtra("idofp"));
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Post post = ds.getValue(Post.class);
                        Log.d("TAG",check);
                        if (isadmin && !mauth.getCurrentUser().getEmail().equals(check)){
                            AlertDialog.Builder builder = new AlertDialog.Builder(readeractivity.this);
                            builder.setTitle("!DIQQƏT!");
                            builder.setMessage("Post sizin deyil, lakin siz adminsiniz. Postu silmək istəyirsiniz? Əminsiniz?");
                            builder.setPositiveButton("Bəli", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    refcom.child(post.id).removeValue();
                                    reflikes.child(post.id).removeValue();
                                    mdatabase.child(i.getStringExtra("idofp")).removeValue();
                                    FirebaseDatabase.getInstance().getReference().child("Likes").child(i.getStringExtra("idofp")).removeValue();
                                    ref.child(ds.getKey()).removeValue();
                                    Toast.makeText(readeractivity.this, "Post silindi", Toast.LENGTH_SHORT).show();
                                    finish();
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

                        else{

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(readeractivity.this);
                            builder1.setTitle("Post");
                            builder1.setMessage("Postu silmək istəyirsiniz? Əminsiniz?");
                            builder1.setPositiveButton("Bəli", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    refcom.child(post.id).removeValue();
                                    reflikes.child(post.id).removeValue();
                                    ref.child(ds.getKey()).removeValue();
                                    Toast.makeText(readeractivity.this, "Post silindi", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                            builder1.setNegativeButton("Xeyr", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog dialog1 = builder1.create();
                            dialog1.show();

                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        else{
            Toast.makeText(this, "Digərinin postunu silə bilməzsiniz", Toast.LENGTH_SHORT).show();
        }

    }
    private void openprofile(){
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("name").equalTo(tvusername.getText().toString());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    Intent i = new Intent(readeractivity.this, showuserlayout.class);
                    i.putExtra("name",user.name);
                    i.putExtra("school",user.school);
                    i.putExtra("mail",user.mail);
                    i.putExtra("uid",user.uid);
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onclickopen(View view) {
        openprofile();
    }
    public void onclickopen1(View view) {
        openprofile();
    }
}
