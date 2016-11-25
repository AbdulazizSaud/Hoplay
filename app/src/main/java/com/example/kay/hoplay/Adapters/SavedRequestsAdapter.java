package com.example.kay.hoplay.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.kay.hoplay.Activities.SignUpActivity;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;

import com.example.kay.hoplay.model.SavedRequestsList;
import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;

/**
 * Created by khaledAlhindi on 11/23/2016 AD.
 */

public class SavedRequestsAdapter extends RecyclerView.Adapter<SavedRequestsAdapter.ViewHolder> {
    private ArrayList<SavedRequestsList> savedRequestsLists;



    // Provide a suitable constructor (depends on the kind of dataset)
    public SavedRequestsAdapter(ArrayList<SavedRequestsList> savedRequestsLists) {
        this.savedRequestsLists = savedRequestsLists;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SavedRequestsAdapter.ViewHolder  onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {



        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_request, parent, false);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // WHEN ITEM CLICKED :
                // This case when user  click on a saved request
                // it will make new request in the Requests time line for the specified console.
            }
        });
        // set the view's size, margins, paddings and layout parameters

        SavedRequestsAdapter.ViewHolder vh = new SavedRequestsAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(SavedRequestsAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        Typeface sansation = Typeface.createFromAsset(holder.gameName.getContext().getAssets() ,"sansationbold.ttf");

        ImageLoader loader = App.getInstance().getImageLoader();

        SavedRequestsList savedRequestsList = savedRequestsLists.get(position);

        if(savedRequestsList.getGamePhotoURL().length() > 0){
            loader.get(savedRequestsList.getGamePhotoURL(),
                    ImageLoader.getImageListener(
                            holder.gamePhoto
                            ,R.drawable.profile_default_photo
                            ,R.drawable.profile_default_photo));

        } else
            holder.gamePhoto.setImageResource(R.drawable.profile_default_photo);

        holder.gameName.setText(savedRequestsList.getGameName());
        holder.requestDescription.setText(savedRequestsList.getRequestDescription());
        holder.numberOfPlayers.setText(savedRequestsList.getNumberOfPlayers());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return savedRequestsLists.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        TextView gameName;
        TextView requestDescription;
        CircularImageView gamePhoto;
        TextView numberOfPlayers ;

        public ViewHolder(View v) {
            super(v);
            gameName =  (TextView) v.findViewById(R.id.game_name_saved_request_textview);

            // In case you want to play with the fonts and fonts color :
            Typeface sansation = Typeface.createFromAsset(gameName.getContext().getAssets() ,"sansationbold.ttf");
            gameName.setTypeface(sansation);
            gameName.setTextColor(gameName.getResources().getColor(R.color.pccolor));


            requestDescription =  (TextView) v.findViewById(R.id.request_description_saved_request_textview);
            requestDescription.setTypeface(sansation);

            gamePhoto = (CircularImageView) v.findViewById(R.id.game_photo_saved_request_circularimageview);

            numberOfPlayers = (TextView) v.findViewById(R.id.number_of_players_saved_requests_textview);
            numberOfPlayers.setTypeface(sansation);

        }
    }
}
