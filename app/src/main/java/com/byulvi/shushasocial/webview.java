package com.byulvi.shushasocial;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.nio.channels.AlreadyBoundException;

public class webview extends AppCompatActivity {
    private WebView webView;
    private Intent i;
    private WebSettings webSettings;
    private StorageReference ref;
    private String pdfuri;
    private int adcontrol;
    ProgressDialog progressDialog;
    String url = "";
    private String checkurl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        init();
        ref = FirebaseStorage.getInstance().getReference();

        SharedPreferences prefget = PreferenceManager.getDefaultSharedPreferences(this);
        adcontrol = prefget.getInt("adcontrol",0);
        checkurl= prefget.getString("url","null");

        i = getIntent();


        int position = i.getIntExtra("index",-1);
        int topicpos = i.getIntExtra("indextop",0);
        int testpos = i.getIntExtra("testpos",-1);


        switch (topicpos){
            case 0:
                switch (testpos){
                    case 0:
                        url = "https://drive.google.com/file/d/1MN-vDxs9x2ixBvAErCxZpKbb8IoR-4If/view?ths=true";
                        break;
                    case 1:
                        url = "https://drive.google.com/file/d/1oHd3RYZ11aVeWGs3KdD3vTBzSCp5N6MW/view?usp=sharing";
                        break;
                    case 2:
                        url = "https://drive.google.com/file/d/1ICn5t8nqK2EpV_cXACsPgtE6JwpMYEXw/view?usp=sharing";
                        break;
                    case 3:
                        url = "https://forms.office.com/Pages/ResponsePage.aspx?id=DQSIkWdsW0yxEjajBLZtrQAAAAAAAAAAAAO__TjlOV9UNUhNT0dQNUY5QVg4SDNORDdQWUk3VFFaQi4u";
                        break;
                    case 4:
                        url = "https://drive.google.com/file/d/1CvQiHy30r-oB3781KFddoTjO0MvDcFcp/view?usp=sharing";
                        break;

                }
                switch (position){ //Riyaz
                    case 0:
                        url = "https://e-derslik.edu.az/player/index3.php?book_id=187#books/187/units/unit-1/page1.xhtml";
                        break;
                    case 1:
                        url = "https://e-derslik.edu.az/player/index3.php?book_id=243#books/243/units/unit-1/page1.xhtml";
                        break;
                    case 2:
                        url = "https://e-derslik.edu.az/player/index3.php?book_id=291#books/291/units/unit-1/page1.xhtml";
                        break;
                    case 3:
                        url = "https://e-derslik.edu.az/player/index3.php?book_id=393#books/393/units/unit-1/page1.xhtml";
                        break;
                    case 4:
                        url = "https://drive.google.com/file/d/1vNc8zXBDAPxa7FjUA2tK8AZ0BKpv8n9k/view?usp=sharing";
                        break;
                    case 5:
                        url = "https://e-derslik.edu.az/player/index3.php?book_id=408#books/408/units/unit-1/page1.xhtml";
                        break;
                    case 6:
                        url = "https://e-derslik.edu.az/player/index3.php?book_id=410#books/410/units/unit-1/page1.xhtml";
                        break;
                    case 7:
                        url = "https://drive.google.com/file/d/1sx75rl9UpsWpBE0b9laQGlUdezRbJE8z/view?usp=sharing";
                        break;
                }
                 break;
            case 1:
                switch (testpos){
                    case 0:
                        url = "https://drive.google.com/file/d/1pEWSn0EOYJQVw_tzA09f7Usib9EDgYdx/view?usp=sharing";
                        break;
                    case 1:
                        url = "https://drive.google.com/file/d/1SxS2FrNqVo92KB06KPwlbaCpb6kxfU_I/view?usp=sharing";
                        break;
                    case 2:
                        url = "https://forms.office.com/Pages/ResponsePage.aspx?id=DQSIkWdsW0yxEjajBLZtrQAAAAAAAAAAAAO__TjlOV9UNUlTVDhQV1NBMENHRDBHWkZWRzVaUElDVS4u";
                        break;
                    case 3:
                        url = "https://drive.google.com/file/d/1Ak_eXcyZjEpylllBYL0IpTIgsS9AdFYY/view?usp=sharing";
                        break;

                }
                switch (position){ //Az
                    case 0:
                        url = "https://drive.google.com/file/d/1JCA6zcsgH3a9wEgh5GTOafo0YbRjT-XB/view?usp=sharing";
                        break;
                    case 1:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=257#books/257/units/unit-1/page1.xhtml";
                        break;
                    case 2:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=18#books/18/units/unit-1/page1.xhtml";
                        break;
                    case 3:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=366#books/366/units/unit-1/page1.xhtml";
                        break;
                    case 4:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=267#books/267/units/unit-1/page1.xhtml";
                        break;
                    case 5:
                        url = "https://drive.google.com/file/d/1ZhKGj88VTeOrXWLAoGrFJf1M4Kh31Z1w/view?usp=sharing";
                        break;
                    case 6:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=352#books/352/units/unit-1/page1.xhtml";
                        break;
                    }
                break;
            case 2:
                switch (testpos){
                    case 0:
                        url = "https://drive.google.com/file/d/1low0z-N42DDoScCp2_G_N16sbe-u0FOS/view?usp=sharing";
                        break;
                }
                switch (position){ //Edeb
                    case 0:
                        url = "https://drive.google.com/file/d/1ifSSruWiSaaT7GQ2TeGkuF0YziR3s4tE/view?usp=sharing";
                        break;
                    case 1:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=245#books/245/units/unit-1/page1.xhtml";
                        break;
                    case 2:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=292#books/292/units/unit-1/page1.xhtml";
                        break;
                    case 3:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=368#books/368/units/unit-1/page1.xhtml";
                        break;
                    case 4:
                        url = "https://drive.google.com/file/d/10a5HQ0eQLGolLxZ4zacKiZ_jG55eHOfz/view?usp=sharing";
                        break;
                    case 5:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=225#books/225/units/unit-1/page1.xhtml";
                        break;
                    case 6:
                        url = "https://drive.google.com/file/d/1YuBjdpc0Z8k5676dx3J5D5Tkqiofbv4W/view?usp=sharing";
                        break;
                }
                break;

            case 3:
                switch (testpos){
                    case 0:
                        url = "https://drive.google.com/file/d/1CKLicrjNGYPB_uMYDX1jYbSBh3ONIZ0q/view?usp=sharing";
                        break;
                    case 1:
                        url = "https://drive.google.com/file/d/1zZ4z6ow3gpUZJ8I_-4Ika6lIIItISuvF/view?usp=sharing";
                        break;
                    case 2:
                        url = "https://drive.google.com/file/d/1s4LoEE1c-wkqzQdFkUErkCxz4J1MEdql/view?usp=sharing";
                        break;
                    case 3:
                        url = "https://drive.google.com/file/d/1_a-Y6vhgmoeAYp1wx5mxDYIJN6v3ahkG/view?usp=sharing";
                        break;
                    case 4:
                        url = "https://drive.google.com/file/d/1kh1UaGgrgAGhk_XCEAmsXbvm0lbMUQyT/view?usp=sharing";
                        break;
                }
                switch (position){  //Fizika
                    case 0:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=237#books/237/units/unit-1/page1.xhtml";
                        break;
                    case 1:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=290#books/290/units/unit-1/page1.xhtml";
                        break;
                    case 2:
                        url = "https://drive.google.com/file/d/1I3XE3htsVeqej1HcFjtynrfjLxldDu1e/view?usp=sharing";
                        break;
                    case 3:
                        url ="https://drive.google.com/file/d/14QWgAyvoSOxTabi7utRhpCQ2CzSrex-Q/view?usp=sharing";
                        break;
                    case 4:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=218#books/218/units/unit-1/page1.xhtml";
                        break;
                     case 5:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=282#books/282/units/unit-1/page1.xhtml";
                        break;

                }
                break;
            case 4:
                switch (testpos){
                    case 0:
                        url = "https://drive.google.com/file/d/1USEUN9o5S9W09kGX4mINFcq0erAYck4Q/view?usp=sharing";
                        break;
                    case 1:
                        url = "https://drive.google.com/file/d/1ZWg6yi5mIER1inN9P4GMX2T1FUF4dgOv/view?usp=sharing";
                        break;

                }
                switch (position){ //Kimya
                    case 0:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=19#books/19/units/unit-1/page1.xhtml";
                        break;
                    case 1:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=395#books/395/units/unit-1/page1.xhtml";
                        break;
                    case 2:
                        url = "https://drive.google.com/file/d/1asIc84dLzjvNxTQUenKhGoPkLG78PteI/view?usp=sharing";
                        break;
                    case 3:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=226#books/226/units/unit-1/page1.xhtml";
                        break;
                    case 4:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=349#books/349/units/unit-1/page1.xhtml";
                        break;
                }
                break;
            case 5:
                switch (testpos){
                    case 0:
                        url = "https://drive.google.com/file/d/1Bs1Hy00OjyKjVqPSgKmitlm2ulCb-YH3/view?usp=sharing";
                        break;
                    case 1:
                        url = "https://drive.google.com/file/d/1CxkRVcUGoId8vqpZ42Q9-UIcPONBtFqP/view?usp=sharing";
                        break;
                    case 2:
                        url = "https://drive.google.com/file/d/1WjaWSK26AdItaEkZatEowp0CMpvIqBv2/view?usp=sharing";
                        break;
                }
                switch (position){ //Eng
                    case 0:
                       url = "https://drive.google.com/file/d/1NFQCkiCEYaMR5DbZSa2oMrN-UUcFto3U/view?usp=sharing";
                       break;
                    case 1:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=242#books/242/units/unit-1/page1.xhtml";
                        break;
                    case 2:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=242#books/242/units/unit-1/page1.xhtml";
                        break;
                    case 3:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=310#books/310/units/unit-1/page1.xhtml";
                        break;
                    case 4:
                       url = "https://drive.google.com/file/d/10PeZyZIKq6pTfxyPTi_sdUL8BodhI-R9/view?usp=sharing";
                       break;
                    case 5:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=224#books/224/units/unit-1/page1.xhtml";
                        break;
                    case 6:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=284#books/284/units/unit-1/page1.xhtml";
                        break;
                    case 7:
                        url="https://drive.google.com/file/d/1tnRuyuKoJFp17Vvt3qg_4rshpJ3VZn3T/view?usp=sharing";
                        break;
                }
                break;

            case 6:
                switch (testpos){
                    case 0:
                        url = "https://drive.google.com/file/d/1sWG-HuvTNRdrzHp3xP3UgSNLJ3js-JWs/view?usp=sharing";
                        break;
                    case 1:
                        url = "https://drive.google.com/file/d/14vEAb2JxVVM6yej_IHYGhq4NYyAkmZ9u/view?usp=sharing";
                        break;
                }
                switch (position){ //Cografiya
                    case 0:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=238#books/238/units/unit-1/page1.xhtml";
                        break;
                    case 1:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=288#books/288/units/unit-1/page1.xhtml";
                        break;
                    case 2:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=397#books/397/units/unit-1/page1.xhtml";
                        break;
                    case 3:
                       url = "https://drive.google.com/file/d/1fkSOKMXhKjwC6i3dtUlxiedaoYqUQxPm/view?usp=sharing";
                       break;
                    case 4:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=220#books/220/units/unit-1/page1.xhtml";
                        break;
                    case 5:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=280#books/280/units/unit-1/page1.xhtml";
                        break;
                }
                break;

            case 7:
                switch (testpos){
                    case 0:
                        url = "https://drive.google.com/file/d/1ojT939mwW13GkBfPQhvG3CowEv6OefCI/view?usp=sharing";
                        break;
                    case 1:
                        url = "https://drive.google.com/file/d/1okN4oHUJWFmxYDAqcKD1c4mSqDTb7lXj/view?usp=sharing";
                        break;
                }
                switch (position){ //Bio
                    case 0:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=235#books/235/units/unit-1/page1.xhtml";
                        break;
                    case 1:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=300#books/300/units/unit-1/page1.xhtml";
                        break;
                    case 2:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=383#books/383/units/unit-1/page1.xhtml";
                        break;
                    case 3:
                       url = "https://drive.google.com/file/d/1ZhKGj88VTeOrXWLAoGrFJf1M4Kh31Z1w/view?usp=sharing";
                       break;
                    case 4:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=221#books/221/units/unit-1/page1.xhtml";
                        break;
                    case 5:
                        url = "https://www.e-derslik.edu.az/player/index3.php?book_id=276#books/276/units/unit-1/page1.xhtml";
                        break;
                }
                break;


        }
        /*if (adcontrol <= 2){
            SharedPreferences pref  = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor edit = pref.edit();
            if (url != checkurl ){
                adcontrol++;
                edit.putInt("adcontrol",adcontrol);
                edit.commit();
            }
            webView.loadUrl(url);
            edit.putString("url",url);
            edit.commit();

        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Limiti keçdiniz!");
            builder.setMessage("Bu proqramı pulsuz saxlayan reklamdır, reklama baxdıqdan sonra davam edə bilərsiniz");
            builder.setPositiveButton("Oldu", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(webview.this,ad.class);
                    startActivity(i);
                }
            });
            builder.setNegativeButton("Lazım deyil", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   dialog.cancel();
                   finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }*/
        Log.d("TAG",adcontrol+"");
        webView.loadUrl(url);


    }

    private void init() {

        webView = findViewById(R.id.webview);
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.transperent));
        progressDialog.setContentView(R.layout.progressdialog);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:(function() { " +
                        "document.querySelector('[role=\"toolbar\"]').remove();})()");
                progressDialog.dismiss();

            }
        });
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        webView.zoomOut();


        //PERFOMANCE
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);
        //PERFOMANCE
    }




}
