package com.example.kay.hoplay.CoresAbstract.RequestAbstracts;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.Adapters.SpinnerAdapter;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Cores.RequestCore.RequestLobbyCore;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

public abstract class SearchResults extends AppCompatActivity {



    private MaterialBetterSpinner searchPrioritySpinner;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<RequestModel> requestModels;
    private ArrayList<String> searchPriorityList;
    private ArrayAdapter searchPriorityAdapter;

    protected App app;



    // Provider Accounht stuff
    private String pcGameProvider ="";
   private  Dialog  gameProviderDialog;
    private   String selectedPlatform = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        app = App.getInstance();
        initControls();
        setupRecyclerView();

        OnStartActivity();
    }

    private void initControls()
    {
        searchPrioritySpinner = (MaterialBetterSpinner) findViewById(R.id.search_priority_spinner);
        final Typeface playregular = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");
        searchPrioritySpinner.setTypeface(playregular);
        requestModels = new ArrayList<RequestModel>();


        // Spinner stuffs
        searchPriorityList = new ArrayList<>();
        searchPriorityList.add("Order By Time");
        searchPriorityList.add("Order By Player Number");

        searchPriorityAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
                searchPriorityList);

        searchPrioritySpinner.setAdapter(searchPriorityAdapter);
        // set Order by time as a default
        searchPrioritySpinner.setSelection(0);


    }


    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.search_results_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)


        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    public void addResult(String platform, String requestTitle, String admin, String description, String region, int playerNumber, String matchType, String rank, long timeStamp)
    {

        RequestModel requestModel = new RequestModel(platform, requestTitle, admin, description, region, playerNumber, matchType, rank, timeStamp);
        requestModels.add(requestModel);
        mAdapter.notifyDataSetChanged();
    }


    public void addResult(RequestModel requestModel)
    {
        requestModels.add(requestModel);
        mAdapter.notifyDataSetChanged();
    }

    private CommonAdapter<RequestModel> createAdapter(){
        return new CommonAdapter<RequestModel>(requestModels,R.layout.request_model) {
            @Override
            public ViewHolders OnCreateHolder(View v) {

                return new ViewHolders.SearchResultsHolder(v);
            }

            @Override
            public void OnBindHolder(ViewHolders holder, final RequestModel model, int position) {

                app.loadingImage(getApplication(), holder, model.getRequestPicture());
                holder.setTitle(model.getRequestTitle());
                holder.setSubtitle(model.getDescription());
                holder.setTime(app.convertFromTimeStampToDate(model.getTimeStamp()));
                holder.setNumberOfPlayers(model.getPlayers().size()+"/"+String.valueOf(model.getPlayerNumber()));
                holder.setSubtitle2(model.getAdminName());

                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (model.getPlatform().equalsIgnoreCase("PS") && !app.getUserInformation().getPSNAcc().equals("")){
                            OnClickHolders(model,v);
                        }else if(model.getPlatform().equalsIgnoreCase("XBOX") && !app.getUserInformation().getPSNAcc().equals("")){
                            OnClickHolders(model,v);
                        }else if(model.getPlatform().equalsIgnoreCase("PC") && app.getUserInformation().getPcGamesAcc().get(pcGameProvider) !=null){
                            OnClickHolders(model,v);
                        }else {
                            createGameProviderDialog(model);
                        }


                    }
                });
            }

        };
    }

    private void goToLobby() {
        Intent i = new Intent(getApplicationContext(),RequestLobbyCore.class);
        startActivity(i);
    }



    public void createGameProviderDialog(final RequestModel requestModel)
    {

       boolean userEnteredGameProviderAcc = false;
       pcGameProvider ="";
   gameProviderDialog = new Dialog(this);
        gameProviderDialog.setCancelable(false);
        gameProviderDialog.setContentView(R.layout.provider_account_pop_up);
        gameProviderDialog.show();


        gameProviderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView gameProviderMessage;
        Button saveInfoButton;
        String providerAccountType = "";   // Xbox ID , PSN , PC (STEAM , Battlenet..etc)
        final EditText gameProviderEdittext ;



        gameProviderEdittext = (EditText) gameProviderDialog.findViewById(R.id.game_provider_account_edittext);
        gameProviderMessage = (TextView) gameProviderDialog.findViewById(R.id.popup_message_textview_provide_account);
        saveInfoButton = (Button) gameProviderDialog.findViewById(R.id.save_game_provider_button);

            // get the request platform
            selectedPlatform = requestModel.getPlatform().trim();

        if (selectedPlatform.equalsIgnoreCase("PS"))
            providerAccountType = String.format(getResources().getString(R.string.provider_account_message), "PSN");
        else if (selectedPlatform.equalsIgnoreCase("XBOX"))
            providerAccountType = String.format(getResources().getString(R.string.provider_account_message), "Xbox Live");
        else if (selectedPlatform.equalsIgnoreCase("PC"))
        {
            pcGameProvider =  app.getGameManager().getGameById(requestModel.getGameId().trim()).getPcGameProvider();
            providerAccountType = String.format(getResources().getString(R.string.provider_account_message), pcGameProvider);
        }


        gameProviderMessage.setText(providerAccountType);

        Typeface sansation = Typeface.createFromAsset(getAssets() ,"sansationbold.ttf");
        saveInfoButton.setTypeface(sansation);

        final Typeface playbold = Typeface.createFromAsset(getAssets(), "playbold.ttf");
        final Typeface playReg = Typeface.createFromAsset(getAssets(), "playregular.ttf");
        gameProviderMessage.setTypeface(playbold);
        gameProviderMessage.setTypeface(playbold);
        gameProviderEdittext.setTypeface(playReg);




        // Changing edittext icon
        gameProviderEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                gameProviderEdittext.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mood_focused_24dp, 0);
                if (s.length() == 0) {
                    gameProviderEdittext.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mood_bad_not_focused_24dp, 0);
                }
            }
        });



        saveInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if the user entered the game provider id
                if (gameProviderEdittext.length() > 0)
                {
                    saveGameProviderAccount(pcGameProvider,gameProviderEdittext.getText().toString().trim(),selectedPlatform);
                    OnClickHolders(requestModel,v);
                    gameProviderDialog.dismiss();

                }
                else
                {
                    String noGameProviderMsg ="";
                    if (selectedPlatform.equalsIgnoreCase("PS"))
                        noGameProviderMsg = String.format(getResources().getString(R.string.new_request_dialog_no_game_provider_error), "PSN");
                    else if (selectedPlatform.equalsIgnoreCase("XBOX"))
                        noGameProviderMsg = String.format(getResources().getString(R.string.new_request_dialog_no_game_provider_error), "Xbox Live");
                    else if (selectedPlatform.equalsIgnoreCase("PC"))
                    {
                        String pcGameProvider =  app.getGameManager().getGameById(requestModel.getGameId()).getPcGameProvider();
                        noGameProviderMsg = String.format(getResources().getString(R.string.new_request_dialog_no_game_provider_error), pcGameProvider);
                    }
                    Toast.makeText(getApplicationContext(),noGameProviderMsg, Toast.LENGTH_LONG).show();
                }



            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = gameProviderDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }





    protected abstract void saveGameProviderAccount(String gameProvider,String userGameProviderAcc , String platform );
    protected abstract void OnStartActivity();
    protected abstract void OnClickHolders(RequestModel model, View v);

}
