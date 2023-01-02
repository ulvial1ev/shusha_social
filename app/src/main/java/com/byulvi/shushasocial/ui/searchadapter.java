package com.byulvi.shushasocial.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.byulvi.shushasocial.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class searchadapter  extends ArrayAdapter<User> {
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private List<User> ListItem = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    public searchadapter(@NonNull Context context, int resource,List<User> ListItem, LayoutInflater inflater) {
        super(context, resource, ListItem);
        this.ListItem = ListItem;
        this.inflater = inflater;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Viewholder viewholder;

        User listitemmain = ListItem.get(position);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.searchadapter_listview,null,false);
            viewholder = new Viewholder();
            viewholder.username = convertView.findViewById(R.id.tvnamesearch);
            viewholder.pp = convertView.findViewById(R.id.ivppsearch);
            viewholder.school = convertView.findViewById(R.id.tvschool);
            viewholder.mail = convertView.findViewById(R.id.tvmailsearch);
        }
        else {
            viewholder = (Viewholder) convertView.getTag();
        }
        //Picasso.get().load(listitemmain.uid).resize(60,60).centerCrop().into(viewholder.pp);
        viewholder.mail.setText(listitemmain.getMail());
        viewholder.school.setText(listitemmain.getSchool());
        viewholder.username.setText(listitemmain.getName());
        convertView.setTag(viewholder);
        return convertView;
    }


    public class Viewholder{
        TextView username,mail,school;
        ImageView pp;

    }
}
