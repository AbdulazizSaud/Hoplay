package com.example.kay.hoplay.Adapters;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.kay.hoplay.Activities.ChatActivity;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.model.CommunityUserList;
import com.example.kay.hoplay.model.RecentActivityList;
import com.pkmmte.view.CircularImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import emojicon.EmojiconTextView;

/**
 * Created by khaledAlhindi on 11/22/2016 AD.
 */

public class RecentActivitiesAdapter extends RecyclerView.Adapter<RecentActivitiesAdapter.ViewHolder> {
    private ArrayList<com.example.kay.hoplay.model.RecentActivityList> recentActivityLists;


    // Provide a suitable constructor (depends on the kind of dataset)
    public RecentActivitiesAdapter(ArrayList<RecentActivityList> recentActivityLists) {
        this.recentActivityLists = recentActivityLists;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecentActivitiesAdapter.ViewHolder  onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_user_activity, parent, false);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // WHEN ITEM CLICKED :
            }
        });
        // set the view's size, margins, paddings and layout parameters

        RecentActivitiesAdapter.ViewHolder vh = new RecentActivitiesAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecentActivitiesAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        ImageLoader loader = App.getInstance().getImageLoader();

        RecentActivityList recentActivityList = recentActivityLists.get(position);

        if(recentActivityList.getGamePhotoURL().length() > 0){
            loader.get(recentActivityList.getGamePhotoURL(),
                    ImageLoader.getImageListener(
                            holder.gamePhoto
                            ,R.drawable.profile_default_photo
                            ,R.drawable.profile_default_photo));

        } else
            holder.gamePhoto.setImageResource(R.drawable.profile_default_photo);

        holder.gameName.setText(recentActivityList.getGameName());
        holder.activityDescription.setText(recentActivityList.getActivityDescription());
        holder.activityDate.setText(recentActivityList.getActivityDate());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return recentActivityLists.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        TextView gameName;
        TextView activityDescription;
        CircularImageView gamePhoto;
        TextView activityDate ;

        public ViewHolder(View v) {
            super(v);

            gameName =  (TextView) v.findViewById(R.id.game_name_recent_activity_textview);
            Typeface sansation = Typeface.createFromAsset(gameName.getContext().getAssets() ,"sansationbold.ttf");
            gameName.setTypeface(sansation);

            activityDescription =  (TextView) v.findViewById(R.id.activity_description_textview);
            activityDescription.setTypeface(sansation);
            gamePhoto = (CircularImageView) v.findViewById(R.id.game_photo_recent_activity_circularimageview);
            activityDate = (TextView) v.findViewById(R.id.activity_date_textview);
            activityDate.setTypeface(sansation);

        }
    }
}
