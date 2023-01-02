package com.byulvi.shushasocial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class showuserlayout extends AppCompatActivity {
    private Intent i;
    private TextView name,mail,school;
    private CircleImageView iv;
    private DatabaseReference mdatabase;
    private String geturi;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showuserlayout);
        init();
    }

    private void init() {
        i = getIntent();
        name = findViewById(R.id.tvnameshow);
        mail = findViewById(R.id.tvemailshow);
        iv = findViewById(R.id.ivtopiclogo);
        school = findViewById(R.id.tvschoolshow);

        name.setText("Ad : " +i.getStringExtra("name"));
        mail.setText("Email : " +i.getStringExtra("mail"));
        school.setText("Məktəb/Universitet : " +i.getStringExtra("school"));

        mdatabase = FirebaseDatabase.getInstance().getReference("PP");

        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.transperent));
        progressDialog.setContentView(R.layout.progressdialog);

        FirebaseStorage.getInstance().getReference().child(i.getStringExtra("uid") + " pp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).resize(300,300).centerCrop().into(iv);
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                iv.setImageDrawable(getResources().getDrawable(R.drawable.shushauser));
            }
        });








    }
}
