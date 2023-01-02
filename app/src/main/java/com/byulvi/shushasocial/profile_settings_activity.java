package com.byulvi.shushasocial;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle    ;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile_settings_activity extends AppCompatActivity {
    private FirebaseAuth mauth;

    private AdView ad;
    private CircleImageView displaypp;
    private TextView tvusernameprofile;
    private StorageReference mstorage;
    DatabaseReference dbdelete;
    private DatabaseReference mdatabase;
    private String PP_KEY = "PP";
    private String checkuri;
    private Uri uploaduri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        mdatabase = FirebaseDatabase.getInstance().getReference(PP_KEY);
        setContentView(R.layout.profile_settings_layout);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        mauth = FirebaseAuth.getInstance();

        ad = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        ad.loadAd(adRequest);

        tvusernameprofile = findViewById(R.id.tvusernameprofile);
        mstorage = FirebaseStorage.getInstance().getReference();
        tvusernameprofile.setText(mauth.getCurrentUser().getEmail());
        displaypp = findViewById(R.id.ivpp);
        mstorage.child(mauth.getCurrentUser().getUid() + " pp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).resize(300,300).centerCrop().into(displaypp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profile_settings_activity.this, "Profil şəkili yerləşdirməmisiniz.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickpp(View view) {
        getImage();

    }

    private void getImage() {
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
                displaypp.setImageURI(data.getData());

            }
        }


    }



    private void uploadimage(){
        //converts displayimage content to bitmap
        Bitmap bitmap = ((BitmapDrawable) displaypp.getDrawable()).getBitmap();
        //creates stream to use in byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        //creating byte array(we can only upload images in byte array)
        byte[] bytearray = baos.toByteArray();
        //Creates child for main storage(mstorage)
        StorageReference myref = mstorage.child(mauth.getCurrentUser().getUid()+ " pp");
        //Creates task to start "up" task and creates oncomplete listener
        //uploads
        UploadTask up = myref.putBytes(bytearray);
        Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                Toast.makeText(profile_settings_activity.this, "Success", Toast.LENGTH_SHORT).show();
                return myref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploaduri = task.getResult();
            }
        });
    }

    public void Onclickupdateinfo(View view) {
        uploadimage();

    }

    public void onClicklogout(View view) {
        startActivity(new Intent(profile_settings_activity.this,Loginactivity.class));
        mauth.signOut();
    }

    public void onclickcredits(View view) {
        startActivity(new Intent(profile_settings_activity.this,credits.class));
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
    }
}
