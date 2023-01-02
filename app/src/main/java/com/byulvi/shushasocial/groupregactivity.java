package com.byulvi.shushasocial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.byulvi.shushasocial.ui.groupclass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class groupregactivity extends AppCompatActivity {
    private EditText edgp,edpwd;
    private TextView tvlog,tvchange;
    private Button btn;
    private DatabaseReference mdatabase;
    public SharedPreferences prefcheck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupreglayout);
        init();
        prefcheck = PreferenceManager.getDefaultSharedPreferences(groupregactivity.this);
        String check = prefcheck.getString("group","null");
        if (!check.equals("null")){
            Intent i = new Intent(groupregactivity.this,contentgroupactivity.class);
            startActivity(i);
        }
    }

    private void init() {
        edgp = findViewById(R.id.edgpname);
        tvchange = findViewById(R.id.tvchange);
        edpwd = findViewById(R.id.edgppwd);
        btn = findViewById(R.id.gpregbtn);
        mdatabase = FirebaseDatabase.getInstance().getReference("Groups");
        tvlog = findViewById(R.id.tvlog);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.t);
        tvlog.setAnimation(animation);


    }


    public void onclickloggroup(View view) {
        if (!TextUtils.isEmpty(edgp.getText().toString()) && !TextUtils.isEmpty(edpwd.getText().toString())){
            String name = edgp.getText().toString();
            String pwd = edpwd.getText().toString();
            Query query = mdatabase.orderByChild("gpname").equalTo(name);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String compare = null;
                    String gpname = null;
                    for (DataSnapshot ds: snapshot.getChildren()){
                        groupclass groupclass = ds.getValue(groupclass.class);
                        compare = groupclass.getPwd();
                        gpname = groupclass.getGpname();
                    }
                    if (compare != null){
                        if (pwd.equals(compare)){
                            Intent i = new Intent(groupregactivity.this,contentgroupactivity.class);
                            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(groupregactivity.this);
                            SharedPreferences.Editor edit= pref.edit();
                            edit.putString("group",gpname);
                            edit.commit();
                            startActivity(i);
                        }
                        else{
                            edpwd.setError("Şifrə yalnışdır");
                        }
                    }
                    else{
                        edgp.setError("Belə qrup yoxdur");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void onclickreggp(View view) {
        btn.setOnClickListener(null);
        Animation animin = AnimationUtils.loadAnimation(this,R.anim.fadein);
        Animation animout = AnimationUtils.loadAnimation(this,R.anim.fadeout);

        tvlog.startAnimation(animout);
        tvlog.setText("Qrup yaradın.");
        tvlog.startAnimation(animin);

        btn.startAnimation(animout);
        btn.setText("Yarat");
        btn.startAnimation(animin);

        tvchange.startAnimation(animout);
        tvchange.setText("Daxil olacağınız qrup var? Daxil olun.");
        tvchange.setOnClickListener(null);
        tvchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvlog.startAnimation(animout);
                tvlog.setText("Qrupa daxil olun.");
                tvlog.startAnimation(animin);

                btn.startAnimation(animout);
                btn.setText("Daxil ol");
                btn.startAnimation(animin);
                btn.setOnClickListener(groupregactivity.this::onclickloggroup);

                tvchange.startAnimation(animout);
                tvchange.setText("Qrupunuz yoxdur? Yeni qrup yaradın.");
                tvchange.setAnimation(animin);
                tvchange.setOnClickListener(null);
                tvchange.setOnClickListener(groupregactivity.this::onclickreggp);

            }
        });
        tvchange.startAnimation(animin);

        tvlog.setTextSize(30);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edgp.getText().toString()) && !TextUtils.isEmpty(edpwd.getText().toString())){
                    if (edgp.getText().toString().length() >=15){
                        edgp.setError("Qrupun adı uzundur");
                    }
                    else {
                        String name = edgp.getText().toString();
                        String pwd = edpwd.getText().toString();
                        groupclass gp = new groupclass(name, pwd);
                        mdatabase.push().setValue(gp);
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(groupregactivity.this);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString("group", name);
                        edit.commit();
                        Toast.makeText(groupregactivity.this, "Qrup yaradıldı!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(groupregactivity.this, contentgroupactivity.class);
                        startActivity(i);
                    }
                }
                else if(TextUtils.isEmpty(edgp.getText().toString())){
                    edgp.setError("Xananı doldurun");
                }
                else if(TextUtils.isEmpty(edpwd.getText().toString())){
                    edpwd.setError("Xananı doldurun");
                }
                else{
                    Toast.makeText(groupregactivity.this, "Xanaları doldurun", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
