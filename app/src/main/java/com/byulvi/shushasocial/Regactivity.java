package com.byulvi.shushasocial;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.byulvi.shushasocial.ui.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Regactivity extends AppCompatActivity {
    private EditText edreg, edpwd, edschool, edname;
    private FirebaseAuth mauth;
    private Button regbtn, checkbtn, logoutbtn;
    private TextView tvreg, tvinfo;
    private StorageReference mstorage;
    private DatabaseReference dbreg;


    private Boolean ban = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_layout);
        edreg = findViewById(R.id.edreg);
        dbreg = FirebaseDatabase.getInstance().getReference("Users");
        mstorage = FirebaseStorage.getInstance().getReference();
        edpwd = findViewById(R.id.edpwdreg);
        logoutbtn = findViewById(R.id.logoutbtn);
        checkbtn = findViewById(R.id.checkbtn);
        tvreg = findViewById(R.id.tvreg);
        tvinfo = findViewById(R.id.tvinfo);
        edname = findViewById(R.id.edname);
        regbtn = findViewById(R.id.regbtn);
        edschool = findViewById(R.id.edschool);
        mauth = FirebaseAuth.getInstance();
        if (mauth.getCurrentUser() != null) {
            if (mauth.getCurrentUser().isEmailVerified()) {
                startActivity(new Intent(Regactivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            } else {
                verify();
            }
        } else {
            reg();
        }


    }

    private void verify() {
        edreg.setVisibility(View.GONE);
        edpwd.setVisibility(View.GONE);
        edschool.setVisibility(View.GONE);
        regbtn.setVisibility(View.GONE);
        checkbtn.setVisibility(View.VISIBLE);
        edname.setVisibility(View.GONE);
        tvinfo.setVisibility(View.VISIBLE);
        logoutbtn.setVisibility(View.VISIBLE);
        tvreg.setTextSize(22);
        tvreg.setText("Təsdiq məktubu göndərildi");

    }

    private void reg() {
        edreg.setVisibility(View.VISIBLE);
        edpwd.setVisibility(View.VISIBLE);
        edschool.setVisibility(View.VISIBLE);
        edname.setVisibility(View.VISIBLE);
        regbtn.setVisibility(View.VISIBLE);
        checkbtn.setVisibility(View.GONE);
        tvinfo.setVisibility(View.GONE);
        logoutbtn.setVisibility(View.GONE);
        tvreg.setTextSize(40);
        tvreg.setText("Hesab yaradın!");
    }

    public void onClickexitlogout(View view) {
        mauth.signOut();
        startActivity(new Intent(Regactivity.this, Loginactivity.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void onclickcheck(View view) {
        startActivity(new Intent(Regactivity.this, Loginactivity.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void OnClickreg(View view) {



        if (!TextUtils.isEmpty(edreg.getText().toString()) && !TextUtils.isEmpty(edpwd.getText().toString()) && !TextUtils.isEmpty(edschool.getText().toString()) && !TextUtils.isEmpty(edname.getText().toString())) {
            if (edpwd.length() < 6) {
                edpwd.setError("Şifrəniz 6 və daha çox simvoldan ibarət olmalıdır");
            }
            else {
                    regbtn.setVisibility(View.GONE);
                    mauth.createUserWithEmailAndPassword(edreg.getText().toString(), edpwd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            User user = new User(mauth.getCurrentUser().getUid(), edname.getText().toString(), mauth.getCurrentUser().getEmail(), edschool.getText().toString());
                            dbreg.push().setValue(user);

                            Toast.makeText(Regactivity.this, "Hesab yaradıldı", Toast.LENGTH_SHORT).show();


                            mauth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Regactivity.this, "Email təsdiqləmə göndərildi", Toast.LENGTH_SHORT).show();
                                    verify();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Regactivity.this, "Email təsdiqləmə göndərilmədi, birazdan təkrarlayın", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            edname.setError( "Belə istifadəçi artıq mövcuddur");
                        }
                    });
                }
        }


            else {
            Toast.makeText(this, "Xanaları doldurun", Toast.LENGTH_SHORT).show();
        }
    }

}