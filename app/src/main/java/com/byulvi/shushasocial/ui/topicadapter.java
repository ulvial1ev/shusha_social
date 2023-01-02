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

import java.util.ArrayList;
import java.util.List;

public class topicadapter extends ArrayAdapter<topicclass> {
    private LayoutInflater inflater;
    private Context context;
    private List<topicclass> listItem = new ArrayList<>();


    public topicadapter(@NonNull Context context, int resource, List<topicclass> listItem,LayoutInflater inflater) {
        super(context, resource,listItem);
        this.inflater = inflater;
        this.listItem = listItem;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Viewholder viewholder;
        topicclass listitemMain = listItem.get(position);

        if(convertView == null) {
            // CONVERTVIEW MAKES US TO COMMUNICATE WITH LAYOUT ELEMENTS
            convertView = inflater.inflate(R.layout.topic_list_view, null, false);
            viewholder = new Viewholder();
            //Initializing variables in Viewholder class
            viewholder.logo = convertView.findViewById(R.id.ivtopiclogo);
            viewholder.topic = convertView.findViewById(R.id.tvtopciname);
            viewholder.usage = convertView.findViewById(R.id.tvtopicusage);

        }
        else{
            viewholder= (Viewholder) convertView.getTag();
        }

        

        //Sets text and image to elements in list
        viewholder.topic.setText(listitemMain.getTopic());
        viewholder.usage.setText(listitemMain.getUsage());
        viewholder.logo.setImageDrawable(context.getResources().getDrawable(listitemMain.getLogo()));

        convertView.setTag(viewholder);


        return convertView;
    }


    private class Viewholder{
        TextView topic,usage;
        ImageView logo;

    }
}
