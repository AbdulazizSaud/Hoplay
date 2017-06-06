package com.example.kay.hoplay.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Cores.RequestCore.NewRequestCore;
import com.example.kay.hoplay.Cores.UserProfileCores.AddGameCore;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.R;

import java.util.ArrayList;


public abstract class NewRequestFragment extends ParentRequestFragments {


    protected App app;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    Button newRequestButton;
    TextView savedRequestsMessage;
    TextView addGameTextView;

    FloatingActionButton addGameFloationActionButton;
    protected ArrayList<RequestModel> arrayList = new ArrayList<RequestModel>();


    public NewRequestFragment() {
        super();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        app = App.getInstance();


        View view = inflater.inflate(R.layout.activity_make_request_fragment, container, false);
        newRequestButton = (Button) view.findViewById(R.id.new_request_button);
        addGameFloationActionButton = (FloatingActionButton) view.findViewById(R.id.add_new_game_floatinactionbutton);

        addGameFloationActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), AddGameCore.class);
                startActivity(i);
            }
        });
        savedRequestsMessage = (TextView) view.findViewById(R.id.saved_activity_message_textview);
        addGameTextView = (TextView) view.findViewById(R.id.add_game_textview_make_request_fragment);
        newRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplicationContext(), NewRequestCore.class);
                startActivity(i);
            }
        });


        Typeface sansation = Typeface.createFromAsset(getActivity().getAssets(), "sansationbold.ttf");
        newRequestButton.setTypeface(sansation);
        savedRequestsMessage.setTypeface(sansation);
        addGameTextView.setTypeface(sansation);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.saved_requests_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);


        OnStartActivity();
        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }


    private CommonAdapter<RequestModel> createAdapter() {
        return new CommonAdapter<RequestModel>(arrayList, R.layout.saved_request_instance) {
            @Override
            public ViewHolders OnCreateHolder(View v) {
                return new ViewHolders.SavedRequestHolder(v);
            }

            @Override
            public void OnBindHolder(ViewHolders holder, final RequestModel model, int position) {
                //ImageLoader loader = App.getInstance().getImageLoader();


                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickHolders(model, v);
                    }
                });


                App.getInstance().loadingImage(getContext(), holder, model.getRequestPicture());

                // loadingImage(holder, model, loader);
                holder.setTitle(model.getRequestTitle());
                holder.setSubtitle(model.getDescription());
                holder.setNumberOfPlayers(String.valueOf(model.getPlayerNumber()) + " Players");


            }
        };
    }



    protected void addToSaveRequest(RequestModel model , String savedReqIndex)
    {
        model.setSavedRequestIndex(Integer.parseInt(savedReqIndex));
        arrayList.add(model);
        mAdapter.notifyDataSetChanged();
    }


    // Not used
    protected void removeFromSaveRequest(RequestModel model)
    {

        boolean b = arrayList.remove(model);
        app.getSavedRequests().remove(model);
        mAdapter.notifyDataSetChanged();
    }



    protected abstract void OnClickHolders(RequestModel model, View v);

    protected abstract void OnStartActivity();
}
