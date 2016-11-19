package com.example.kay.hoplay;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;

import emojicon.EmojiconTextView;

//
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<userList> userList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

         TextView chatOpponentFullname;
         EmojiconTextView chatLastMessage;
         TextView chatLastMessageAgo;
         TextView chatNewMessagesCount;
         CircularImageView chatOpponent;

        public ViewHolder(View v) {
            super(v);
            chatOpponentFullname =  (TextView) v.findViewById(R.id.chatOpponentFullname);
            chatLastMessageAgo =  (TextView) v.findViewById(R.id.chatLastMessageAgo);
            chatNewMessagesCount =  (TextView) v.findViewById(R.id.chatNewMessagesCount);

            chatOpponent = (CircularImageView) v.findViewById(R.id.chatOpponent);
            chatLastMessage = (EmojiconTextView) v.findViewById(R.id.chatLastMessage);

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
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),ChatActivity.class);
                v.getContext().startActivity(i);
            }
        });
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        userList us = userList.get(position);
        holder.chatOpponentFullname.setText(us.fullName);
        holder.chatLastMessage.setText(us.lastMsg);
        holder.chatLastMessageAgo.setText(us.lastMsgDate);



        holder.chatOpponent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return userList.size();
    }
}