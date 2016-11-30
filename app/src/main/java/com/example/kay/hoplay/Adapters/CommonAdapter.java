package com.example.kay.hoplay.Adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.kay.hoplay.Common.ViewHolders;
import com.example.kay.hoplay.R;
import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;

import emojicon.EmojiconTextView;


public abstract class  CommonAdapter<T> extends RecyclerView.Adapter<ViewHolders> {
    private ArrayList<T> data;
    private int inflateLayout;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CommonAdapter(ArrayList<T> data,int inflateLayout) {
        this.data = data;
        this.inflateLayout = inflateLayout;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(inflateLayout, parent, false);

        return OnCreateHolder(v);
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolders holder, int position) {
        T commonModel = data.get(position);
        OnBindHolder(holder,commonModel);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

    public abstract ViewHolders OnCreateHolder(View v);
    public abstract void OnBindHolder(ViewHolders holder,T model);




}