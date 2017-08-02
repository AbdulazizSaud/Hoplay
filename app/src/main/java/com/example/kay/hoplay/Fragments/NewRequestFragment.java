package com.example.kay.hoplay.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


    // No saved requests variables
    protected ImageView noSavedRequestImageView;
    protected TextView  noSavedRequestsTextView;


    // Counter of the saved request to keep updated with the items number in the recycler view adapter
    private int savedReqsCounter = 0 ;



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

        noSavedRequestImageView = (ImageView) view.findViewById(R.id.no_saved_request_imageview);
        noSavedRequestsTextView = (TextView)  view.findViewById(R.id.no_saved_request_message_textview);

        addGameFloationActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), AddGameCore.class);
                startActivity(i);
                getActivity().overridePendingTransition( R.anim.slide_in_up_layouts, R.anim.slide_out_up_layouts);
            }
        });
        savedRequestsMessage = (TextView) view.findViewById(R.id.saved_activity_message_textview);
        addGameTextView = (TextView) view.findViewById(R.id.add_game_textview_make_request_fragment);
        newRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check if the user has any game
                if(app.getGameManager().getUserGamesNumber()<1){
                    showNoGameDialog();
                }else{
                    Intent i = new Intent(getActivity().getApplicationContext(), NewRequestCore.class);
                    startActivity(i);
                    getActivity().overridePendingTransition( R.anim.slide_in_down_layouts, R.anim.slide_out_down_layouts);
                }

            }
        });


        Typeface sansation = Typeface.createFromAsset(getActivity().getAssets(), "sansationbold.ttf");
        Typeface playregular = Typeface.createFromAsset(getActivity().getAssets(), "playregular.ttf");
        newRequestButton.setTypeface(sansation);
        savedRequestsMessage.setTypeface(sansation);
        addGameTextView.setTypeface(sansation);

        noSavedRequestsTextView.setTypeface(playregular);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.saved_requests_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = createAdapter();
        mAdapter.notifyDataSetChanged();



        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        // Make the saved request title gone at first : because user has no games
        savedRequestsMessage.setVisibility(View.GONE);

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



                // Sync the saved reqs counter with the recyclerview adapter
                savedReqsCounter = mAdapter.getItemCount();


                // Show or hide no saved requests elements
                if (mAdapter.getItemCount()<1)
                    showNoSavedRequestElements();
                else
                    hideNoSavedRequestElements();



                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickHolders(model, v,position);
                    }
                });


                Typeface playbold = Typeface.createFromAsset(getActivity().getAssets(), "playbold.ttf");
                Typeface playregular = Typeface.createFromAsset(getActivity().getAssets(), "playregular.ttf");

                App.getInstance().loadingImage(getContext(), holder, model.getRequestPicture());



                // loadingImage(holder, model, loader);


                // Capitalize Request Title letters
                String requestTitle = model.getRequestTitle();
                String capitlizedReqtitle = requestTitle.substring(0,1).toUpperCase() +  requestTitle.substring(1);
                if (requestTitle.contains(" "))
                {
                    // Capitalize game title letters
                    String cpWord= "";
                    for (int  i = 0 ; i < capitlizedReqtitle.length(); i++)
                    {
                        if (capitlizedReqtitle.charAt(i) == 32 && capitlizedReqtitle.charAt(i+1) != 32)
                        {
                            cpWord= capitlizedReqtitle.substring(i+1,i+2).toUpperCase() + capitlizedReqtitle.substring(i+2);
                            capitlizedReqtitle = capitlizedReqtitle.replace(capitlizedReqtitle.charAt(i+1),cpWord.charAt(0));
                        }
                    }
                    holder.setTitle(capitlizedReqtitle);
                }else {
                    if (capitlizedReqtitle.equalsIgnoreCase("cs:go")){
                        holder.setTitle(capitlizedReqtitle.toUpperCase());
                    } else
                        holder.setTitle(capitlizedReqtitle);
                }


                holder.getTitleView().setTypeface(playbold);
                holder.getSubtitleView().setTypeface(playregular);



                holder.getPicture().setBorderWidth(6);
                // Changing title color depending on the platform
                if (model.getPlatform().equalsIgnoreCase("PC"))
                {holder.getTitleView().setTextColor(ContextCompat.getColor(getContext(), R.color.pc_color));
                holder.getPicture().setBorderColor(ContextCompat.getColor(getContext(), R.color.pc_color));}
                else if (model.getPlatform().equalsIgnoreCase("PS"))
                {holder.getTitleView().setTextColor(ContextCompat.getColor(getContext(), R.color.ps_color));
                    holder.getPicture().setBorderColor(ContextCompat.getColor(getContext(), R.color.ps_color));}
                else
                {holder.getTitleView().setTextColor(ContextCompat.getColor(getContext(), R.color.xbox_color));
                    holder.getPicture().setBorderColor(ContextCompat.getColor(getContext(), R.color.xbox_color));}

                holder.setSubtitle(model.getDescription());
                holder.setNumberOfPlayers(String.valueOf(model.getPlayerNumber()) + " Players");



            }
        };
    }



    protected void addToSaveRequest(RequestModel model , String savedReqIndex)
    {
        arrayList.add(model);
        mAdapter.notifyDataSetChanged();




        // Show or hide no saved requests elements
        if (mAdapter.getItemCount()<1)
            showNoSavedRequestElements();
        else
            hideNoSavedRequestElements();



    }


    // Not used
    protected void removeFromSaveRequest(RequestModel model)
    {

        arrayList.remove(model);

        app.setSavedRequests(arrayList);

        mAdapter.notifyDataSetChanged();


        //Decrease the counter
        savedReqsCounter--;

        // Show or hide no saved requests elements
        if (savedReqsCounter<1)
            showNoSavedRequestElements();
        else
            hideNoSavedRequestElements();



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

                // Check if the user still have the game
                if (app.getGameManager().checkIfHasGameById(requestModel.getGameId().trim())){
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
                // if not then show a message , to add that game  to game library
                else
                {
                    Toast.makeText(getContext(),getString(R.string.make_request_fragment_do_not_have_game),Toast.LENGTH_LONG).show();
                }

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
                getActivity().overridePendingTransition( R.anim.slide_in_down_layouts, R.anim.slide_out_down_layouts);
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




        // Show or hide no saved requests elements
        if (mAdapter.getItemCount()<1)
            showNoSavedRequestElements();
        else
            hideNoSavedRequestElements();




    }

    protected abstract void deleteSavedRequest(RequestModel model);
    protected abstract void OnClickHolders(RequestModel model, View v,int position);


    private void showNoGameDialog(){


      final Dialog   noGameDialog = new Dialog(getContext());
        noGameDialog.setContentView(R.layout.activity_no_game_pop_up);
        noGameDialog.show();


        noGameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView noGameText;
        Button addGameButton;




        noGameText = (TextView) noGameDialog.findViewById(R.id.popup_message_textview);
        addGameButton = (Button) noGameDialog.findViewById(R.id.add_game_button_no_game_popup);




        Typeface sansation = Typeface.createFromAsset(getResources().getAssets() ,"sansationbold.ttf");
        addGameButton.setTypeface(sansation);

        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
        final Typeface playReg = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");

        noGameText.setTypeface(playReg);

        addGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), AddGameCore.class);
                startActivity(i);
                getActivity().overridePendingTransition( R.anim.slide_in_down_layouts, R.anim.slide_out_down_layouts);
                noGameDialog.dismiss();
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



    protected void showNoSavedRequestElements(){

        Animation slideDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_view);

        savedRequestsMessage.setVisibility(View.GONE);

        noSavedRequestImageView.setVisibility(View.INVISIBLE);
        noSavedRequestImageView.startAnimation(slideDown);
        noSavedRequestImageView.setVisibility(View.VISIBLE);

        noSavedRequestsTextView.setVisibility(View.INVISIBLE);
        noSavedRequestsTextView.startAnimation(slideDown);
        noSavedRequestsTextView.setVisibility(View.VISIBLE);


    }

    protected void hideNoSavedRequestElements()
    {

        savedRequestsMessage.setVisibility(View.VISIBLE);
        noSavedRequestImageView.setVisibility(View.GONE);
        noSavedRequestsTextView.setVisibility(View.GONE);

    }



    protected abstract void OnStartActivity();
}
