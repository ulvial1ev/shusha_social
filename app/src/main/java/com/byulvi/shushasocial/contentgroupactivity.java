package com.byulvi.shushasocial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.byulvi.shushasocial.ui.Muzgpclass;
import com.byulvi.shushasocial.ui.User;
import com.byulvi.shushasocial.ui.muzgpadapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class contentgroupactivity extends AppCompatActivity{
    public EditText edcomm;
    public ListView listView;
    public TextView tvgp;
    public ArrayAdapter<Muzgpclass> adapter;
    public FirebaseAuth mauth;
    public Muzgpclass listitem;
    public List<Muzgpclass> listdata;
    public DatabaseReference mdatabase;
    public SharedPreferences pref;
    public String gpname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contentgroup);
        init();
        databaseread();
    }

    public void tag(String tag) {
        edcomm.append("@"+ tag);
    }

    private void init() {
        listView = findViewById(R.id.listviewmuzgp);
        edcomm = findViewById(R.id.edmuzgp);
        tvgp = findViewById(R.id.tvgroupname);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        gpname = pref.getString("group","null");
        if (gpname.equals("null")){
            finish();
        }
        tvgp.setText(gpname);
        listdata = new ArrayList<>();
        mauth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference();
        adapter = new muzgpadapter(this,R.layout.commadapter_listview,listdata,getLayoutInflater());

        String uid = mauth.getCurrentUser().getUid();
        Query query = mdatabase.child("Users").orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    Log.d("TAG",user.name);
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contentgroupactivity.this);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("publcomm",user.name);
                    edit.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void onclickgpsend(View view) {
        if (!TextUtils.isEmpty(edcomm.getText().toString())){
            SharedPreferences prefget = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String publisher = prefget.getString("publcomm","null");
            String contain = edcomm.getText().toString();
            String id = mauth.getCurrentUser().getUid();
            Muzgpclass muzgpclass = new Muzgpclass(gpname,id,publisher,contain);
            mdatabase.child("gp" + gpname).push().setValue(muzgpclass);
            databaseread();
            edcomm.setText("");
            Toast.makeText(this, "Şərhiniz uğurla yerləşdirildi", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Şərh yazın...", Toast.LENGTH_SHORT).show();
        }
    }

    private void databaseread() {
        Query query = mdatabase.child("gp" + gpname).orderByChild("gp").equalTo(gpname);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listdata.size() > 0 ){listdata.clear();}
                for (DataSnapshot ds : snapshot.getChildren()){
                    //equals database values to user var
                    Muzgpclass muzgpclass = ds.getValue(Muzgpclass.class);
                    assert muzgpclass != null;
                    listitem = new Muzgpclass();
                    listitem.setPublisher(muzgpclass.getPublisher());
                    listitem.setComm(muzgpclass.getComm());
                    listitem.setId(muzgpclass.getId());
                    listdata.add(listitem);
                    //fills listtemp to setonclickitem to use
                    listView.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
                listView.setSelection((int) snapshot.getChildrenCount() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onclicklogoutgp(View view) {
        SharedPreferences preflogout = PreferenceManager.getDefaultSharedPreferences(contentgroupactivity.this);
        SharedPreferences.Editor edit = preflogout.edit();
        edit.remove("group");
        edit.commit();
        finish();
    }

    public void onclickgpimage(View view) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("gpname",gpname);
        edit.commit();
        Intent i = new Intent(contentgroupactivity.this,gpimageactivity.class);
        i.putExtra("publisher",pref.getString("publcomm","null"));
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
