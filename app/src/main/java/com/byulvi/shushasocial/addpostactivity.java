package com.byulvi.shushasocial;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.byulvi.shushasocial.ui.Post;
import com.byulvi.shushasocial.ui.Postwimage;
import com.byulvi.shushasocial.ui.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class addpostactivity extends AppCompatActivity {
    private DatabaseReference myref, myrefpost;
    private String POST_KEY = "Post", POSTWIMAGE_KEY= "Postwimage";
    private SwitchCompat switchshekil;
    private EditText edtitle,edpost;
    private Boolean need_image = false, is_ed_empty_for_image;
    private FirebaseAuth mauth;
    private StorageReference mystorage;
    private DatabaseReference mdatabase;
    private LottieAnimationView animview;
    private Uri upload_uri;
    private TextView tvshekil;
    private ProgressDialog progressDialog;
    private ImageView displayimage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myref= FirebaseDatabase.getInstance().getReference(POST_KEY);
        myrefpost = FirebaseDatabase.getInstance().getReference(POSTWIMAGE_KEY);
        mystorage = FirebaseStorage.getInstance().getReference();
        setContentView(R.layout.addpostlayout);
        displayimage = findViewById(R.id.displayimage);
        switchshekil = findViewById(R.id.switchsgekil);
        tvshekil = findViewById(R.id.tvshekil);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        edtitle = findViewById(R.id.edTitle);
        animview = findViewById(R.id.animationView);
        animview.playAnimation();
        edpost = findViewById(R.id.edText);
        mauth = FirebaseAuth.getInstance();
        displayimage.setVisibility(View.GONE);
        Log.d("TAG","" + mauth.getCurrentUser().getUid());
        tvshekil.setVisibility(View.GONE);

        String curuserquery = mauth.getCurrentUser().getUid();
        Query query = mdatabase.orderByChild("uid").equalTo(curuserquery);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    SharedPreferences prefpubl = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor edit = prefpubl.edit();
                    edit.putString("publ",user.name);
                    edit.commit();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


      public void Switchonclick(View view) {
        if (switchshekil.isChecked()){
            need_image= true;
            displayimage.setVisibility(View.VISIBLE);
            tvshekil.setVisibility(View.VISIBLE);
        }
        else{
            need_image = false;
            displayimage.setImageResource(R.drawable.ic_launcher_background);
            displayimage.setVisibility(View.GONE);
            tvshekil.setVisibility(View.GONE);
        }
     }

    private void checkup(){
        if (!TextUtils.isEmpty(edpost.getText().toString()) && !TextUtils.isEmpty(edtitle.getText().toString())){
            is_ed_empty_for_image = true;
        }
        else{
            is_ed_empty_for_image= false;
        }


    }
    public void savepostwimage(){
        String idwimage = "Postwimage " + System.currentTimeMillis();
        String title = edtitle.getText().toString();
        String uid = mauth.getCurrentUser().getUid();
        SharedPreferences prefppwimage = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String publimage = prefppwimage.getString("publ","def");
        Date time = Calendar.getInstance().getTime();
        String format = new SimpleDateFormat("HH:mm, MMM d").format(time);
        String text = edpost.getText().toString();

        FirebaseStorage.getInstance().getReference().child(mauth.getCurrentUser().getUid() +" pp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(text)){
                    if (edtitle.length() <= 25){
                    Postwimage post= new Postwimage(idwimage,title,text,upload_uri.toString(),publimage,uri.toString(),uid,format);
                    myrefpost.push().setValue(post);
                    edpost.setText("");
                    edtitle.setText("");
                    displayimage.setImageResource(R.drawable.ic_launcher_background);
                    }
                    else{
                        progressDialog.dismiss();
                        edtitle.setError("Başlıq çox uzundur");
                    }
                }
                else{
                    progressDialog.dismiss();
                    alertdialog("","Xanaları doldurun!");
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(text)){
                    if (edtitle.length() <= 25){
                    String nullpppost = "null";
                    Postwimage post= new Postwimage(idwimage,title,text,upload_uri.toString(),publimage,nullpppost,uid,format);
                    myrefpost.push().setValue(post);
                    edpost.setText("");
                    edtitle.setText("");
                    displayimage.setImageResource(R.drawable.ic_launcher_background);
                    }
                    else{
                        progressDialog.dismiss();
                        edtitle.setError("Başlıq çox uzundur");
                    }
                }
                else{
                    progressDialog.dismiss();
                    alertdialog("","Xanaları doldurun!");
                }
            }
        });



    }

    public void savepost(){
        String id = "Post" + System.currentTimeMillis();
        String title = edtitle.getText().toString();
        String text = edpost.getText().toString();
        String uid = mauth.getCurrentUser().getUid();
        Date time = Calendar.getInstance().getTime();
        String format = new SimpleDateFormat("HH:mm, MMM d").format(time);
        SharedPreferences prefpp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String publ = prefpp.getString("publ","def");
        FirebaseStorage.getInstance().getReference().child(mauth.getCurrentUser().getUid() + " pp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(text)){
                    if (edtitle.length() <= 25){
                    Post post= new Post(id,title,text,publ,uri.toString(),uid,format);
                    myref.push().setValue(post);
                    edpost.setText("");
                    edtitle.setText("");
                    Toast.makeText(addpostactivity.this, "Post yaradıldı!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        edtitle.setError("Başlıq çox uzundur");
                    }
                }
                else{
                    alertdialog("","Xanaları doldurun!");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(text)){
                    if (edtitle.length() <= 25){
                    String nulluri = "null";
                    Post post= new Post(id,title,text,publ,nulluri,uid,format);
                    myref.push().setValue(post);
                    edpost.setText("");
                    edtitle.setText("");
                    Toast.makeText(addpostactivity.this, "Post yaradıldı!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        edtitle.setError("Başlıq çox uzundur");
                    }

                }
                else{
                    Toast.makeText(addpostactivity.this, "Xanaları doldurun!", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }



    public void OnClickCreatepost(View view) {

        if(need_image){
            uploadImage();
        }
        else{
            savepost();
        }

    }

    public void OnClickSelectImage(View view) {
        getimage();
    }
    public void uploadImage(){
        progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Post yaradılır...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        Drawable.ConstantState drawable = getResources().getDrawable(R.drawable.ic_launcher_background).getConstantState();
        if (!displayimage.getDrawable().getConstantState().equals(drawable)) {
            progressDialog.show();
            Bitmap bitmap = ((BitmapDrawable) displayimage.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bytearray = baos.toByteArray();
            StorageReference uploadstorage = mystorage.child(System.currentTimeMillis() + "Image");
            checkup();
            if (is_ed_empty_for_image) {
                UploadTask up = (UploadTask) uploadstorage.putBytes(bytearray).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        progressDialog.setProgress(50);


                    }
                });
                Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        return uploadstorage.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        upload_uri = task.getResult();
                        progressDialog.dismiss();
                        savepostwimage();
                        Toast.makeText(addpostactivity.this, "Postunuz uğurla yerləşdirildi", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(this, "Xanaları doldurun!", Toast.LENGTH_SHORT).show();
            }

        }
        else{
            alertdialog("","Şəkil seçin!");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && data !=null && data.getData() != null){
            if (resultCode == RESULT_OK){
                Log.d("Mylog","Image URI: "+ data.getData());
                displayimage.setImageURI(data.getData());
            }
        }
    }

    public void getimage() {
        Intent intentchooser = new Intent();
        intentchooser.setType("image/*");
        intentchooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentchooser,2);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
    private void alertdialog(String title,String desc){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(desc);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog auye = builder.create();
        auye.show();
    }
}
