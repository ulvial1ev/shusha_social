package com.byulvi.shushasocial.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byulvi.shushasocial.R;
import com.byulvi.shushasocial.webview;

import java.util.ArrayList;

public class testadapter extends RecyclerView.Adapter<testadapter.Viewholder> {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<testclass> list;

    public testadapter(Context context, ArrayList<testclass> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_1,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.grade.setText(list.get(position).getGrade());
        holder.index.setText(list.get(position).getType());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = position;
                Intent i = new Intent(context, webview.class);
                i.putExtra("indextop",list.get(position).getIndex());
                i.putExtra("testpos",itemPosition);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class Viewholder  extends RecyclerView.ViewHolder{
        TextView grade,index;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            grade = itemView.findViewById(R.id.tvgrade1);
            index = itemView.findViewById(R.id.tvusageadapter1);


        }

    }


}
