package com.byulvi.shushasocial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.byulvi.shushasocial.ui.Comm;
import com.byulvi.shushasocial.ui.User;
import com.byulvi.shushasocial.ui.commadapter;
import com.byulvi.shushasocial.ui.gradeadapter;
import com.byulvi.shushasocial.ui.gradeclass;
import com.byulvi.shushasocial.ui.testadapter;
import com.byulvi.shushasocial.ui.testclass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class readtopicactivity extends AppCompatActivity {
    private RecyclerView recyclerView, rectest;
    private ArrayList<gradeclass> list;
    private ArrayList<testclass> list1;
    private Intent i;
    private TextView tv;
    private DatabaseReference mdatabase;
    private EditText edmuz;
    private ImageView iv;
    private ListView listView;

    private int[] logo = {R.drawable.math,R.drawable.aze,R.drawable.edeb,R.drawable.physics,R.drawable.chem,R.drawable.eng,R.drawable.cog,R.drawable.bio};

    private List<Comm> listdata;
    private Comm listitem;
    private commadapter commadapter;

    String topic;
    private FirebaseAuth mauth;
    gradeadapter adapter;
    testadapter testadapter;

    public void tag(String tag) {
        edmuz.append("@"+ tag);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readtopic_layout);
        init();
        databaseread();
    }

    private void init() {
        recyclerView = findViewById(R.id.rectopic);
        rectest = findViewById(R.id.rectest);
        mdatabase = FirebaseDatabase.getInstance().getReference("Comms");
        edmuz = findViewById(R.id.edmuz);
        tv = findViewById(R.id.tvtopic);
        listView = findViewById(R.id.listviewmuz);
        iv = findViewById(R.id.ivtopiclogo);
        listdata = new ArrayList<>();
        commadapter = new commadapter(this,R.layout.commadapter_listview,listdata,getLayoutInflater());
        mauth = FirebaseAuth.getInstance();

        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("uid").equalTo(mauth.getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(readtopicactivity.this);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("publcomm",user.name);
                    edit.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        String[] grade;
        String [] test = new String[0];
        String [] type = new String[0];
        String [] typeup = new String[0];
        i= getIntent();
        int index = i.getIntExtra("index",0);
        iv.setImageDrawable(getResources().getDrawable(logo[index]));
        switch (index){
            case 0:
                topic = "Riyaziyyat";
                grade = new String[]{"5-ci sinif", "6-c?? sinif", "7-ci sinif", "8-ci sinif", "9-cu sinif", "10-cu sinif", "11-ci sinif","G??v??n n????riyyat?? riyaziyyat"};
                typeup = new String[]{"D??rslik","D??rslik","D??rslik","D??rslik","D??rslik","D??rslik","D??rslik","Qayda kitab??"};

                test = new String[]{"Riyaziyyat 1-ci hiss??","Riyaziyyat 2-ci hiss??","Riyaziyyat 11-ci sinif","Riyaziyyat 11-ci sinif","Riyaziyyat 8-ci sinif"};
                type = new String[]{"Test Toplusu(DIM)","Test Toplusu(DIM)","Sinif Testi(DIM)","Test","Sinif testi(DIM)"};
                break;
            case 1:
                topic = "Az??rbaycan dili";
                grade = new String[]{"5-ci sinif", "6-c?? sinif", "7-ci sinif", "8-ci sinif", "9-cu sinif", "10-cu sinif", "11-ci sinif"};
                typeup = new String[]{"D??rslik","D??rslik","D??rslik","D??rslik","D??rslik","D??rslik","D??rslik"};

                test = new String[]{"Az. dili 8-ci sinif","Az. dili 9-cu sinif","Az. dili 11ci sinif","Az. dili","Az. dili 5-11 sinif"};
                type = new String[]{"Sinif testi(DIM)","Sinif testi(DIM)","Test","Test bank??(UNEC)","Test toplusu(V??qar H??seynov)"};
                break;
            case 2:
                topic = "??d??biyyat";
                grade = new String[]{"5-ci sinif", "6-c?? sinif", "7-ci sinif", "8-ci sinif", "9-cu sinif", "10-cu sinif", "11-ci sinif",};
                typeup = new String[]{"D??rslik","D??rslik","D??rslik","D??rslik","D??rslik","D??rslik","D??rslik"};

                test = new String[]{"??d??biyyat 5-9-cu sinif"};
                type = new String[]{"Test Toplusu(Ibn Sina)"};
                break;
            case 3:
                topic = "Fizika";
                grade = new String[]{"6-c?? sinif", "7-ci sinif", "8-ci sinif", "9-cu sinif", "10-cu sinif", "11-ci sinif",};
                typeup = new String[]{"D??rslik","D??rslik","D??rslik","D??rslik","D??rslik","D??rslik"};

                test = new String[]{"Fizika 9-cu sinif","Fizika 10-cu sinif","Fizika 10-cu sinif","Fizika 11-ci sinif"};
                type  = new String[]{"S??naq testi","Sinif testi(DIM)","S??naq testi","S??naq testi"};
                break;

            case 4:
                topic = "Kimya";
                grade = new String[]{"7-ci sinif", "8-ci sinif", "9-cu sinif", "10-cu sinif", "11-ci sinif",};
                typeup = new String[]{"D??rslik","D??rslik","D??rslik","D??rslik","D??rslik"};

                test = new String[]{"Kimya","Kimya(8-ci sinif)"};
                type = new String[]{"Test toplusu(Kaspi)","Sinif testi(DIM)"};
                break;
            case 5:
                topic = "Xarici dil";
                grade = new String[]{"5-ci sinif", "6-c?? sinif", "7-ci sinif", "8-ci sinif", "9-cu sinif", "10-cu sinif", "11-ci sinif","English 5-11 sinif"};
                typeup = new String[]{"D??rslik","D??rslik","D??rslik","D??rslik","D??rslik","D??rslik","D??rslik","Qayda kitab??"};

                test = new String[]{"Ingilis dili","Ingilis dili(1-ci hiss??)","Ingilis dili(2-ci hiss??)"};
                type = new String[]{"Test Toplusu(H??d??f)","Test Topludu(D??M)","Test Topludu(D??M)"};
                break;
            case 6:
                topic = "??o??rafiya";
                grade = new String[]{"6-c?? sinif", "7-ci sinif", "8-ci sinif", "9-cu sinif", "10-cu sinif", "11-ci sinif",};
                typeup = new String[]{"D??rslik","D??rslik","D??rslik","D??rslik","D??rslik","D??rslik"};

                test = new String[]{"Co??rafiya(8-ci sinif)","Co??rafiya"};
                type = new String[]{"Sinif testi(D??M)","Test toplusu(G??v??n)"};
                break;
            case 7:
                topic = "Biologiya";
                grade = new String[]{"6-c?? sinif", "7-ci sinif", "8-ci sinif", "9-cu sinif", "10-cu sinif", "11-ci sinif"};
                typeup = new String[]{"D??rslik","D??rslik","D??rslik","D??rslik","D??rslik","D??rslik"};

                test = new String[]{"9-cu sinif","11-ci sinif"};
                type = new String[]{"S??naq testi(TQDK)","S??naq testi(G??v??n)"};
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + index);
        }
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("idofp",topic);
        edit.commit();
        tv.setText(topic);
        

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(readtopicactivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        list = new ArrayList<>();
        for (int i =0; i< grade.length;i++){
                gradeclass gradeclass = new gradeclass(grade[i],index,typeup[i]);
                list.add(gradeclass);
        }

        //TEST rec
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(readtopicactivity.this,LinearLayoutManager.HORIZONTAL,false);
        rectest.setLayoutManager(linearLayoutManager1);
        rectest.setItemAnimator(new DefaultItemAnimator());

        list1 = new ArrayList<>();
        for (int i =0; i< test.length;i++){
            testclass testclass = new testclass(test[i],index,type[i]);
            list1.add(testclass);
        }








        adapter = new gradeadapter(readtopicactivity.this,list);

        recyclerView.setAdapter(adapter);

        testadapter = new testadapter(this,list1);
        rectest.setAdapter(testadapter);

        databaseread();




    }

    public void Onclicksendcomm(View view) {
        if (!TextUtils.isEmpty(edmuz.getText().toString())){
            String commcont = edmuz.getText().toString();
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            SharedPreferences prefget = PreferenceManager.getDefaultSharedPreferences(this);
            String publ = prefget.getString("publcomm",mauth.getCurrentUser().getEmail());
            String commid = System.currentTimeMillis() + "comm";
            Comm comm = new Comm(uid,commcont,publ,commid);
            mdatabase.child(topic).push().setValue(comm);
            databaseread();
            edmuz.setText("");
        }
        else{
            Toast.makeText(this, "????rh yaz??n...", Toast.LENGTH_SHORT).show();
        }
    }

    private void databaseread(){
        mdatabase.child(topic).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listdata.size() > 0 )listdata.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Comm comm = ds.getValue(Comm.class);
                    listitem = new Comm();
                    listitem.setComm(comm.comm);
                    listitem.setPublisher(comm.publisher);
                    listitem.setId(comm.id);
                    listitem.setCommid(comm.commid);
                    listdata.add(listitem);
                    listView.setAdapter(commadapter);


                }
                commadapter.notifyDataSetChanged();
                listView.setSelection((int) snapshot.getChildrenCount() - 1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
