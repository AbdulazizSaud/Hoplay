package com.example.kay.hoplay.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Cores.RequestCore.EditRequestCore;
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
    private Dialog savedRequestPopupDialog;




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
            public void OnBindHolder(ViewHolders holder, final RequestModel model, final int position) {
                //ImageLoader loader = App.getInstance().getImageLoader();


                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickHolders(model, v,position);
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
        arrayList.add(model);
        mAdapter.notifyDataSetChanged();
    }


    // Not used
    protected void removeFromSaveRequest(RequestModel model)
    {

        arrayList.remove(model);

        app.setSavedRequests(arrayList);

        mAdapter.notifyDataSetChanged();
    }




    protected void showSavedRequestDialog(final RequestModel requestModel , View view,final int index)
    {


        savedRequestPopupDialog = new Dialog(this.getContext());
        savedRequestPopupDialog.setContentView(R.layout.saved_request_on_click_pop_up);
        savedRequestPopupDialog.show();


        savedRequestPopupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Button requestButton , updateButton , deleteButton;

        requestButton = ( Button) savedRequestPopupDialog.findViewById(R.id.request_saved_request_button);
        updateButton = ( Button) savedRequestPopupDialog.findViewById(R.id.update_saved_request_button);
        deleteButton = ( Button) savedRequestPopupDialog.findViewById(R.id.delete_saved_request_button);

        Typeface sansation = Typeface.createFromAsset(getResources().getAssets() ,"sansationbold.ttf");
        requestButton.setTypeface(sansation);
        updateButton.setTypeface(sansation);
        deleteButton.setTypeface(sansation);



        // New request
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addRequestToFirebase(
                     requestModel.getPlatform(),
                        requestModel.getRequestTitle(),
                        requestModel.getMatchType(),
                        requestModel.getRegion(),
                        String.valueOf(requestModel.getPlayerNumber()),
                        requestModel.getRank(),
                        requestModel.getDescription());
                savedRequestPopupDialog.cancel();
            }
        });


        // Delete Game
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSavedRequest(requestModel);
                savedRequestPopupDialog.dismiss();
            }
        });


        // Update request
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(),EditRequestCore.class);

                Bundle bundle = new Bundle();
                bundle.putParcelable("savedReq",requestModel);
                i.putExtras(bundle);
                startActivity(i);
                savedRequestPopupDialog.dismiss();
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = savedRequestPopupDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }


    protected abstract void addRequestToFirebase(String platform, String gameName, String matchType, String region, String numberOfPlayers, String rank, String description);

        protected void updateSavedRequest(int index,RequestModel requestModel)
    {
        arrayList.set(index,requestModel);
        app.getSavedRequests().set(index,requestModel);
        mAdapter.notifyDataSetChanged();

    }

    protected abstract void deleteSavedRequest(RequestModel model);
    protected abstract void OnClickHolders(RequestModel model, View v,int position);

    protected abstract void OnStartActivity();
}
