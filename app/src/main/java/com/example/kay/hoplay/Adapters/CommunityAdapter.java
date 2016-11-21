package com.example.kay.hoplay.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.android.volley.toolbox.ImageLoader;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Activities.ChatActivity;
import com.example.kay.hoplay.model.CommunityUserList;
import com.example.kay.hoplay.R;
import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;

import emojicon.EmojiconTextView;


public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {
    private ArrayList<com.example.kay.hoplay.model.CommunityUserList> CommunityUserList;


    // Provide a suitable constructor (depends on the kind of dataset)
    public CommunityAdapter(ArrayList<CommunityUserList> CommunityUserLists) {
        this.CommunityUserList = CommunityUserLists;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CommunityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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

        ImageLoader loader = App.getInstance().getImageLoader();

        CommunityUserList us = CommunityUserList.get(position);

        if(us.getUserPictureURL().length() > 0){
            loader.get(us.getUserPictureURL(),
                    ImageLoader.getImageListener(
                            holder.chatOpponentPicture
                            ,R.drawable.profile_default_photo
                            ,R.drawable.profile_default_photo));

        } else
            holder.chatOpponentPicture.setImageResource(R.drawable.profile_default_photo);

        holder.chatOpponentFullname.setText(us.getFullName());
        holder.chatLastMessage.setText(us.getLastMsg());
        holder.chatLastMessageAgo.setText(us.getLastMsgDate());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return CommunityUserList.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        TextView chatOpponentFullname;
        EmojiconTextView chatLastMessage;
        TextView chatLastMessageAgo;
        TextView chatNewMessagesCount;
        CircularImageView chatOpponentPicture;

        public ViewHolder(View v) {
            super(v);
            chatOpponentFullname =  (TextView) v.findViewById(R.id.chatOpponentFullname);
            chatLastMessageAgo =  (TextView) v.findViewById(R.id.chatLastMessageAgo);
            chatNewMessagesCount =  (TextView) v.findViewById(R.id.chatNewMessagesCount);

            chatOpponentPicture = (CircularImageView) v.findViewById(R.id.chatOpponent);
            chatLastMessage = (EmojiconTextView) v.findViewById(R.id.chatLastMessage);

        }
    }


}