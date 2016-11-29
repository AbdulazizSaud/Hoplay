package com.example.kay.hoplay.Adapters;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.android.volley.toolbox.ImageLoader;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Activities.ChatActivity;
import com.example.kay.hoplay.model.CommonModel;
import com.example.kay.hoplay.model.CommunityUserList;
import com.example.kay.hoplay.R;
import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;

import emojicon.EmojiconTextView;


public abstract class  CommonAdapter extends RecyclerView.Adapter<CommonAdapter.ViewHolder> {
    private ArrayList<CommonModel> data;
    private int inflateLayout;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CommonAdapter(ArrayList<CommonModel> data,int inflateLayout) {
        this.data = data;
        this.inflateLayout = inflateLayout;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CommonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(inflateLayout, parent, false);


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickRow();
            }
        });
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommonModel commonModel = data.get(position);

        //ImageLoader loader = App.getInstance().getImageLoader();
//        if(commonModel.getUrlOfPicture().length() > 0){
//            loader.get(commonModel.getUserPictureURL(),
//                    ImageLoader.getImageListener(
//                            holder.chatOpponentPicture
//                            ,R.drawable.profile_default_photo
//                            ,R.drawable.profile_default_photo));
//
//        } else
//            holder.chatOpponentPicture.setImageResource(R.drawable.profile_default_photo);




    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

    public abstract void OnClickRow();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View v) {
            super(v);

        }
    }


}