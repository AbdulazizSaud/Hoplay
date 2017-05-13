package com.example.kay.hoplay.CoresAbstract.RequestAbstracts;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.Cores.RequestCore.NewRequestCore;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Models.SavedRequestModel;
import com.example.kay.hoplay.Cores.UserProfileCores.AddGameCore;

import java.util.ArrayList;

public abstract class MakeRequestFragment extends Fragment {

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

    protected   App app ;




    public MakeRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        app = App.getInstance();

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
                checkUserGamesNumber();
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
        savedRequestModels.add(saveRequest("","Overwatch","https://overwatch-a.akamaihd.net/img/logos/overwatch-share-3d5a268515283007bdf3452e877adac466d579f4b44abbd05aa0a98aba582eeaebc4541f1154e57ec5a43693345bebda953381a7b75b58adbd29d3f3eb439ad2.jpg","Need healing -_- ",6));
        savedRequestModels.add(saveRequest("","League of Legends","https://s-media-cache-ak0.pinimg.com/originals/30/0e/58/300e58c8416a68dcfcf1761501348243.jpg","I have the best team !",4));
        savedRequestModels.add(saveRequest("","World of Warcraft","https://s-media-cache-ak0.pinimg.com/236x/18/f2/c2/18f2c237688c6a4395e0f6a702743a7c.jpg","I'm bored",6));

        savedRequestModels.add(saveRequest("","Dying Light","https://upload.wikimedia.org/wikipedia/en/c/c0/Dying_Light_cover.jpg","Lets kill some zombies",3));



        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);



        onStartActivity();
        return view;

    }

    protected void canMakeRequest(boolean canMakeRequest) {
        if (canMakeRequest)
        {
            Intent i = new Intent(getContext(),NewRequestCore.class);
            startActivity(i);
        }
        else
        createNoGameDialog();
    }


    public SavedRequestModel saveRequest(String gameID, String gameName , String gamePhoto , String requestDescription , int numberOfPlayers)
    {
        return  new SavedRequestModel(gameID,gameName,numberOfPlayers,gamePhoto,"",requestDescription,"");

    }

    private CommonAdapter<SavedRequestModel> createAdapter(){
        return new CommonAdapter<SavedRequestModel>(savedRequestModels,R.layout.saved_request_instance) {
            @Override
            public ViewHolders OnCreateHolder(View v) {
                return new ViewHolders.SavedRequestHolder(v);
            }

            @Override
            public void OnBindHolder(ViewHolders holder, final SavedRequestModel model , int position)
            {
                //ImageLoader loader = App.getInstance().getImageLoader();


                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickHolders(model,v);
                    }
                });



                App.getInstance().loadingImage(getContext(),holder,model.getGamePhotoUrl());

               // loadingImage(holder, model, loader);
                holder.setTitle(model.getGameName());
                holder.setSubtitle(model.getActivityDescription());
                holder.setNumberOfPlayers(String.valueOf(model.getMaxPlayers()) + " Players");


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



    protected  abstract  void onStartActivity();
    protected  abstract void checkUserGamesNumber();
}
