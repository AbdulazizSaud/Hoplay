package com.example.kay.hoplay.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kay.hoplay.model.Information;
import com.example.kay.hoplay.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Kay on 10/20/2016.
 */

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.MyViewHolder>{

    private final LayoutInflater inflater;
    List<Information> data = Collections.emptyList();

    public InformationAdapter(Context context , List<Information> data)
    {
       inflater =  LayoutInflater.from(context);
        this.data = data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = inflater.inflate(R.layout.custom_row, parent , false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Information current = data.get(position);
        holder.title.setText(current.getTitle());
        holder.icon.setImageResource(current.getIconId());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title ;
        ImageView icon ;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.list_text);
            icon = (ImageView) itemView.findViewById(R.id.list_icon);
        }
    }
}
