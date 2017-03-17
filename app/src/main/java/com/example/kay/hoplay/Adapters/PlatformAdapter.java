package com.example.kay.hoplay.Adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kay.hoplay.R;
import com.example.kay.hoplay.model.PlatformModel;
import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;

public class PlatformAdapter extends RecyclerView.Adapter<PlatformAdapter.ViewHolder> {
    private ArrayList<PlatformModel> data;


    // Provide a suitable constructor (depends on the kind of dataset)
    public PlatformAdapter(ArrayList<PlatformModel> platformModels) {
        data= platformModels;

    }


    // Create new views (invoked by the layout manager)
    @Override
    public PlatformAdapter.ViewHolder  onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_request_instance, parent, false);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // WHEN ITEM CLICKED :
            }
        });


        // set the view's size, margins, paddings and layout parameters

        PlatformAdapter.ViewHolder vh = new PlatformAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PlatformAdapter.ViewHolder holder, int position) {
        PlatformModel platformModel = data.get(position);



        switch (platformModel.getPlatform()){
            case PC:
                // color for pc text view
            break;

            case PS:
                // color for PS text view

            break;

            case XBOX:
                // color for XBOX text view

                break;

        }


        // GamePicture





        holder.gameName.setText(platformModel.getGameName());
        holder.gameDescription.setText( platformModel.getDescription());
        holder.numberOfPlayers.setText(platformModel.getNumberOfPlayers());
        holder.requester.setText(platformModel.getRequester());


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        CircularImageView gamePicture;
        TextView gameName;
        TextView gameDescription;
        TextView numberOfPlayers;
        TextView requester;

        public ViewHolder(View v) {
            super(v);

            gamePicture = (CircularImageView) v.findViewById(R.id.request_game_photo_circularimageview);
            Typeface sansation = Typeface.createFromAsset(gameName.getContext().getAssets() ,"sansationbold.ttf");

            gameName =  (TextView) v.findViewById(R.id.request_game_name_textview);
            gameName.setTypeface(sansation);

            gameDescription =  (TextView) v.findViewById(R.id.request_description_textview);
            gameDescription.setTypeface(sansation);

            numberOfPlayers =  (TextView) v.findViewById(R.id.request_players_number_textview);
            numberOfPlayers.setTypeface(sansation);

            requester =  (TextView) v.findViewById(R.id.request_by_user_textview);
            requester.setTypeface(sansation);


        }
    }
}
