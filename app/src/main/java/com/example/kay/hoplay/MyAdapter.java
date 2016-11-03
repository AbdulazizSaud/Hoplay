package com.example.kay.hoplay;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kay on 10/21/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<userList> userList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<userList> userLists) {
        this.userList = userLists;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView username_community =  (TextView) holder.view.findViewById(R.id.username_community);
        TextView user_text_community =  (TextView) holder.view.findViewById(R.id.user_text_community);
        final ImageView favorite_user =  (ImageView) holder.view.findViewById(R.id.favorite_user_imageView);

        username_community.setText(userList.get(position).getTitle());
        user_text_community.setText(userList.get(position).getDesc());
        favorite_user.setImageResource(userList.get(position).getImage());


        favorite_user.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                favorite_user.setImageResource(R.drawable.heart_red);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return userList.size();
    }
}