package com.hoplay.kay.hoplay.CoresAbstract.RequestAbstracts;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hoplay.kay.hoplay.Adapters.CommonAdapter;
import com.hoplay.kay.hoplay.Adapters.SpinnerAdapter;
import com.hoplay.kay.hoplay.Adapters.ViewHolders;
import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.Cores.RequestCore.RequestLobbyCore;
import com.hoplay.kay.hoplay.Models.RequestModel;
import com.hoplay.kay.hoplay.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public abstract class SearchResults extends AppCompatActivity {



    private MaterialBetterSpinner searchPrioritySpinner;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<RequestModel> requestModels;
    private ArrayList<String> searchPriorityList;
    private ArrayAdapter searchPriorityAdapter;


    protected HashMap<String,RequestModel> requestModelHashMap = new HashMap<>();
    protected App app;



    // Provider Accounht stuff
    private String pcGameProvider = "";
    private Dialog gameProviderDialog;
    private String selectedPlatform = "";

    protected HashMap<String,RequestModel> requestModelsHashMap =new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        // Set the screen orientation to the portrait mode :
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        app = App.getInstance();
        initControls();
        setupRecyclerView();

        OnStartActivity();


    }

    private void initControls() {
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

        searchPrioritySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Collections.sort(requestModels, new Comparator<RequestModel>() {
                            @Override
                            public int compare(RequestModel o1, RequestModel o2) {
                                if (o1.getTimeStamp() == o2.getTimeStamp())
                                    return 0;
                                else if (o1.getTimeStamp() > o2.getTimeStamp())
                                    return 1;
                                return -1;
                            }
                        });
                        mAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        Collections.sort(requestModels, new Comparator<RequestModel>() {
                            @Override
                            public int compare(RequestModel o1, RequestModel o2) {
                                if (o1.getPlayerNumber() == o2.getPlayerNumber())
                                    return 0;
                                else if (o1.getPlayerNumber() > o2.getPlayerNumber())
                                    return 1;
                                return -1;
                            }
                        });
                        mAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        });

    }


    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.search_results_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next hoplay)


        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    public void addResult(String platform, String requestTitle, String admin, String description, String region, int playerNumber, String matchType, String rank, long timeStamp) {

        RequestModel requestModel = new RequestModel(platform, requestTitle, admin, description, region, playerNumber, matchType, rank, timeStamp);
        requestModels.add(requestModel);
        mAdapter.notifyDataSetChanged();
    }


    public void addResult(RequestModel requestModel) {
        requestModelsHashMap.put(requestModel.getRequestId(),requestModel);
        requestModels.add(requestModel);
        mAdapter.notifyDataSetChanged();
    }


    protected void removeRequest(RequestModel requestModel)
    {
        requestModelsHashMap.remove(requestModel);
        requestModels.remove(requestModel);
        mAdapter.notifyDataSetChanged();
    }


    private CommonAdapter<RequestModel> createAdapter() {
        return new CommonAdapter<RequestModel>(requestModels, R.layout.request_model) {
            @Override
            public ViewHolders OnCreateHolder(View v) {

                return new ViewHolders.SearchResultsHolder(v);
            }

            @Override
            public void OnBindHolder(ViewHolders holder, final RequestModel model, int position) {

                app.loadingImage(getApplication(), holder, model.getRequestPicture());





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

                holder.setSubtitle(model.getDescription());
                holder.setTime(app.convertFromTimeStampToTimeAgo(model.getTimeStamp()));
                holder.setNumberOfPlayers(model.getPlayers().size() + "/" + String.valueOf(model.getPlayerNumber()));
                holder.setSubtitle2(model.getAdminName());

                // end-dots if description is more than 36 characters
                if (model.getDescription().length() >= 36) {
                    StringBuilder dotsDescription = new StringBuilder(model.getDescription());
                    dotsDescription.setCharAt(36, '.');
                    dotsDescription.setCharAt(37, '.');
                    dotsDescription.setCharAt(38, '.');
                    dotsDescription.setCharAt(39, '.');
                    dotsDescription.setCharAt(40, '.');
                    holder.setSubtitle(dotsDescription.toString());
                }

                Typeface playbold = Typeface.createFromAsset(getAssets(), "playbold.ttf");
                Typeface playregular = Typeface.createFromAsset(getAssets(), "playregular.ttf");
                // Set the font
                holder.getTitleView().setTypeface(playbold);
                holder.getSubtitle2().setTypeface(playregular);
                holder.getSubtitleView().setTypeface(playregular);



                // Set the request image border width
                holder.getPicture().setBorderWidth(8);

                // Styling the title and request image border
                if (model.getPlatform().equalsIgnoreCase("PC")) {
                    holder.getPicture().setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.pc_color));
                    holder.getTitleView().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.pc_color));
                } else if (model.getPlatform().equalsIgnoreCase("PS")) {
                    holder.getPicture().setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.ps_color));
                    holder.getTitleView().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ps_color));
                } else {
                    holder.getPicture().setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.xbox_color));
                    holder.getTitleView().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.xbox_color));
                }




                //Match type icon
                if (model.getMatchType().equalsIgnoreCase("Competitive"))
                    holder.getSubtitleImageview().setImageResource(R.drawable.ic_whatshot_competitive_18dp);
                else if (model.getMatchType().equalsIgnoreCase("Quick Match"))
                    holder.getSubtitleImageview().setImageResource(R.drawable.ic_whatshot_quick_match_18dp);
                else
                    holder.getSubtitleImageview().setImageResource(R.drawable.ic_whatshot_unfocused_24dp);



                // Game provider settings
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (app.getGameManager().getGameByName(app.getGameManager().getGameById(model.getGameId()).getGameName()) != null)
                            pcGameProvider =  app.getGameManager().getGameByName(app.getGameManager().getGameById(model.getGameId()).getGameName()).getPcGameProvider();

                        if (model.getPlatform().equalsIgnoreCase("PS") && !app.getUserInformation().getPSNAcc().equals("")){
                            OnClickHolders(model,v);
                        }else if(model.getPlatform().equalsIgnoreCase("XBOX") && !app.getUserInformation().getXboxLiveAcc().equals("")){
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
        Intent i = new Intent(getApplicationContext(), RequestLobbyCore.class);
        startActivity(i);
    }


    public void createGameProviderDialog(final RequestModel requestModel) {

        boolean userEnteredGameProviderAcc = false;
        pcGameProvider = "";
        gameProviderDialog = new Dialog(this);
        gameProviderDialog.setCancelable(false);
        gameProviderDialog.setContentView(R.layout.provider_account_pop_up);
        gameProviderDialog.show();


        gameProviderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView gameProviderMessage;
        Button saveInfoButton;
        String providerAccountType = "";   // Xbox ID , PSN , PC (STEAM , Battlenet..etc)
        final EditText gameProviderEdittext;



        gameProviderEdittext = (EditText) gameProviderDialog.findViewById(R.id.game_provider_account_edittext);
        gameProviderMessage = (TextView) gameProviderDialog.findViewById(R.id.popup_message_textview_provide_account);
        saveInfoButton = (Button) gameProviderDialog.findViewById(R.id.save_game_provider_button);

        // get the request platform
        selectedPlatform = requestModel.getPlatform().trim();

        if (selectedPlatform.equalsIgnoreCase("PS"))
            providerAccountType = String.format(getResources().getString(R.string.provider_account_message), "PSN");
        else if (selectedPlatform.equalsIgnoreCase("XBOX"))
            providerAccountType = String.format(getResources().getString(R.string.provider_account_message), "Xbox Live");
        else if (selectedPlatform.equalsIgnoreCase("PC")) {
            pcGameProvider = app.getGameManager().getGameById(requestModel.getGameId().trim()).getPcGameProvider();
            providerAccountType = String.format(getResources().getString(R.string.provider_account_message), pcGameProvider);
        }


        gameProviderMessage.setText(providerAccountType);

        Typeface sansation = Typeface.createFromAsset(getAssets(), "sansationbold.ttf");
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
                if (gameProviderEdittext.length() > 0) {
                    saveGameProviderAccount(pcGameProvider, gameProviderEdittext.getText().toString().trim(), selectedPlatform);
                    OnClickHolders(requestModel, v);
                    gameProviderDialog.dismiss();

                } else {
                    String noGameProviderMsg = "";
                    if (selectedPlatform.equalsIgnoreCase("PS"))
                        noGameProviderMsg = String.format(getResources().getString(R.string.new_request_dialog_no_game_provider_error), "PSN");
                    else if (selectedPlatform.equalsIgnoreCase("XBOX"))
                        noGameProviderMsg = String.format(getResources().getString(R.string.new_request_dialog_no_game_provider_error), "Xbox Live");
                    else if (selectedPlatform.equalsIgnoreCase("PC")) {
                        String pcGameProvider = app.getGameManager().getGameById(requestModel.getGameId()).getPcGameProvider();
                        noGameProviderMsg = String.format(getResources().getString(R.string.new_request_dialog_no_game_provider_error), pcGameProvider);
                    }
                    Toast.makeText(getApplicationContext(), noGameProviderMsg, Toast.LENGTH_LONG).show();
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






    protected abstract void saveGameProviderAccount(String gameProvider, String userGameProviderAcc, String platform);
    protected abstract void OnStartActivity();
    protected abstract void OnClickHolders(RequestModel model, View v);

}
