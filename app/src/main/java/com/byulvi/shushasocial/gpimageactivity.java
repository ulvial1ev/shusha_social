package com.byulvi.shushasocial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.byulvi.shushasocial.ui.gpimage;
import com.byulvi.shushasocial.ui.gpimageadapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class gpimageactivity extends AppCompatActivity {
    private ImageView ivphoto;
    private ListView listView;
    private StorageReference mstorage;
    private DatabaseReference mdatabse;
    private String gpname;
    private Intent i;
    private Uri uploaduri;

    private List<gpimage> listdata;
    private gpimage listitem;
    private gpimageadapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gpimagelayout);
        ivphoto = findViewById(R.id.ivimage);
        i = getIntent();
        listView = findViewById(R.id.listviewgpphoto);
        mstorage = FirebaseStorage.getInstance().getReference();
        mdatabse = FirebaseDatabase.getInstance().getReference();
        listdata = new ArrayList<>();
        adapter = new gpimageadapter(this,R.layout.gpimage_list_item,listdata,getLayoutInflater());
        SharedPreferences prefget = PreferenceManager.getDefaultSharedPreferences(this);
        gpname = prefget.getString("gpname","null");
        Log.d("TAG",gpname);
        databaseread();
    }

    public void onclicksend(View view) {
        uploadimage();
    }

    private void uploadimage() {
        ProgressDialog progressDialog;
        progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Şəkil yerləşdirilir...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        //converts displayimage content to bitmap
        Bitmap bitmap = ((BitmapDrawable) ivphoto.getDrawable()).getBitmap();
        //creates stream to use in byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        //creating byte array(we can only upload images in byte array)
        byte[] bytearray = baos.toByteArray();
        //Creates child for main storage(mstorage)
        StorageReference myref = mstorage.child(System.currentTimeMillis()+ " gpimage");
        //Creates task to start "up" task and creates oncomplete listener
        //uploads
        UploadTask up = myref.putBytes(bytearray);
        Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                progressDialog.setProgress(50);
                return myref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploaduri = task.getResult();
                progressDialog.dismiss();
                savepost();
            }
        });
    }

    private void savepost() {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String publisher = i.getStringExtra("publisher");
        String imageid= uploaduri.toString();
        Date time = Calendar.getInstance().getTime();
        String format = DateFormat.getDateTimeInstance().format(time);
        String postid = System.currentTimeMillis() +" gpimage";
        gpimage gpimage = new gpimage(id,publisher,imageid,format,postid);
        mdatabse.child("gp" + gpname).push().setValue(gpimage);
        Toast.makeText(this, "Şəkil yerləşdirildi.", Toast.LENGTH_SHORT).show();
    }

    public void onclickselect(View view) {
        Intent intentchooser = new Intent();
        intentchooser.setType("image/*");
        intentchooser.setAction(Intent.ACTION_GET_CONTENT);
        //Func to retrieve url of image
        startActivityForResult(intentchooser, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null && data.getData() != null) {
            if (resultCode == RESULT_OK) {

                //Retrieves url of image that we want to upload to define in firebase
                Log.d("Mylog", "Image URI: " + data.getData());
                ivphoto.setImageURI(data.getData());

            }
        }
    }

    private void databaseread(){
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listdata.size() > 0 ){listdata.clear();}
                //while size of elements in database do:
                for (DataSnapshot ds : snapshot.getChildren()){
                    //equals database values to user var
                    gpimage gpimage = ds.getValue(gpimage.class);
                    assert gpimage != null;
                    listitem = new gpimage();
                    listitem.setImage_id(gpimage.getImage_id());
                    listitem.setId(gpimage.getId());
                    listitem.setPublisher(gpimage.getPublisher());
                    listitem.setPostid(gpimage.getPostid());
                    listitem.setTimestamp(gpimage.getTimestamp());
                    listdata.add(listitem);
                    //fills listtemp to setonclickitem to use
                    listView.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        Query query = mdatabse.child("gp" + gpname).orderByChild("image_id").startAt("h");
        query.addValueEventListener(listener);
    }


}
