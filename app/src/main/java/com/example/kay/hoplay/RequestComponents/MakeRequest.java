package com.example.kay.hoplay.RequestComponents;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Models.SavedRequestModel;
import com.example.kay.hoplay.UserProfileComponents.AddGame;
import com.example.kay.hoplay.UserProfileComponents.AddGameCore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class MakeRequest extends Fragment {

    private  Button newRequestButton;
    private TextView savedRequestsMessage ;
    private TextView addGameTextView;
    private Dialog  noGameDialog;
    private FloatingActionButton addGameFloationActionButton;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // specify an adapter (see also next example)
   private ArrayList<SavedRequestModel> savedRequestModels =new ArrayList<SavedRequestModel>();



    public MakeRequest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
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
//                Intent i = new Intent(getContext(),NewRequestCore.class);
//                startActivity(i);
                createNoGameDialog();
            }
        });



        Typeface sansation = Typeface.createFromAsset(getActivity().getAssets() ,"sansationbold.ttf");
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




        savedRequestModels.add(saveRequest("","Rocket League","http://www.mobygames.com/images/covers/l/307552-rocket-league-playstation-4-front-cover.jpg","Doubles ranked ?",2));
        savedRequestModels.add(saveRequest("","Rocket League","http://www.mobygames.com/images/covers/l/307552-rocket-league-playstation-4-front-cover.jpg","Doubles ranked ?",2));
        savedRequestModels.add(saveRequest("","Rocket League","http://www.mobygames.com/images/covers/l/307552-rocket-league-playstation-4-front-cover.jpg","Doubles ranked ?",2));
        savedRequestModels.add(saveRequest("","Rocket League","http://www.mobygames.com/images/covers/l/307552-rocket-league-playstation-4-front-cover.jpg","Doubles ranked ?",2));

        savedRequestModels.add(saveRequest("","Rocket League","http://www.mobygames.com/images/covers/l/307552-rocket-league-playstation-4-front-cover.jpg","Doubles ranked ?",2));



        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);



        return view;

    }



    public SavedRequestModel saveRequest(String gameID, String gameName , String gamePhoto , String requestDescription , int numberOfPlayers)
    {
        return  new SavedRequestModel(gameID,gameName,numberOfPlayers,gamePhoto,requestDescription,"");

    }

    private CommonAdapter<SavedRequestModel> createAdapter(){
        return new CommonAdapter<SavedRequestModel>(savedRequestModels,R.layout.saved_request_instance) {
            @Override
            public ViewHolders OnCreateHolder(View v) {
                return new ViewHolders.SavedRequestHolder(v);
            }

            @Override
            public void OnBindHolder(ViewHolders holder, final SavedRequestModel model)
            {
                //ImageLoader loader = App.getInstance().getImageLoader();


                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickHolders(model,v);
                    }
                });



                App.getInstance().loadingImage(holder,model.getGamePhotoUrl());

               // loadingImage(holder, model, loader);
                holder.setTitle(model.getGameName());
                holder.setSubtitle(model.getActivityDescription());
                holder.setNumberOfPlayers(String.valueOf(model.getNumberOfPlayers()) + " Players");


            }
        };
    }


    protected abstract void OnClickHolders(SavedRequestModel model, View v);

    public void createNoGameDialog()
    {
        noGameDialog = new Dialog(getContext());
        noGameDialog.setContentView(R.layout.activity_no_game_pop_up);
        noGameDialog.show();

        noGameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView noGameMessage;
        Button addGameButton;

        noGameMessage = (TextView) noGameDialog.findViewById(R.id.popup_message_textview);
        addGameButton = ( Button) noGameDialog.findViewById(R.id.add_game_button_no_game_popup);

        Typeface sansation = Typeface.createFromAsset(getResources().getAssets() ,"sansationbold.ttf");
        addGameButton.setTypeface(sansation);

        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
        noGameMessage.setTypeface(playbold);


        addGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), AddGameCore.class);
                startActivity(i);
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = noGameDialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

}
