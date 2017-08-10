package com.example.kay.hoplay.CoresAbstract.ProfileAbstracts;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Cores.MainAppMenuCore;
import com.example.kay.hoplay.Cores.UserProfileCores.AddGameCore;
import com.example.kay.hoplay.Interfaces.Constants;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Services.SchemaHelper;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.icu.lang.UCharacter.SentenceBreak.SP;

public abstract class AddGame extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    protected EditText searchBar;
    private ArrayList<GameModel> gamesList = new ArrayList<GameModel>();
    protected RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar loadGamesProgressbar;


    View holderView;

    // No games variables
    protected ImageView noGameImageView;
    protected TextView noGameMessage;

    // Games counter : to keep updated  the
    private int gamesCounter = 0 ;

    protected App app;
    Timer timer = new Timer();



    // Tour Section
    // Came from varible for the tour
    private String cameFrom ="";
    private Button tourButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        // Set the screen orientation to the portrait mode :
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
        final Typeface playregular = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");

        app = App.getInstance();
        mRecyclerView = (RecyclerView) findViewById(R.id.rec_add_game);
        searchBar = (EditText) findViewById(R.id.search_games_edititext_add_game);
        loadGamesProgressbar = (ProgressBar) findViewById(R.id.load_games_progressbar_add_game);
        loadGamesProgressbar.setVisibility(View.GONE);
        searchBar.setTypeface(playbold);
        mAdapter = createAdapter();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);



        // No games section
        noGameImageView = (ImageView) findViewById(R.id.no_games_imageview);
        noGameMessage = (TextView) findViewById(R.id.no_games_message);
        noGameMessage.setTypeface(playregular);


        // Tour section
        tourButton = (Button) findViewById(R.id.tour_finish_button);


        // remove the keyboard when start activity
        mRecyclerView.requestFocus();


        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Just to push
                final String value = s.toString().toLowerCase().trim();
                showLoadingAnimation();

                // chinging search icon when user search for a game
                // search icon changing animation
                if (s.length() == 0) {
                    searchBar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search_unfocused_32dp, 0);
                }
                searchBar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search_focused_32dp, 0);


                if (isTextValidate(value)) {
                    searchProcess(value);

                } else {
                    reloadGameList();
                }
            }
        });
        OnStartActivity();



        // **********************************************************************************************************************


        // ** Tour Section  **

        // Detect which previous activity : for the tour
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                cameFrom= "userProfile";
            } else {
                cameFrom= extras.getString("CameFrom");
            }
        } else {
            cameFrom= (String) savedInstanceState.getSerializable("CameFrom");
        }


        // change design if it came from the login activity
        if (cameFrom.equalsIgnoreCase("login"))
        {

            showtourDialog();

            tourButton.setVisibility(View.VISIBLE);
            tourButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MainAppMenuCore.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition( R.anim.slide_in_right_layouts, R.anim.slide_out_right_layouts);
                }
            });

        } // Change design if it came from userProfile
        else
        {
            tourButton.setVisibility(View.GONE);
        }



        // **********************************************************************************************************************

    }


    private void reloadGameList() {}


    private void searchProcess(final String value) {

        timer.cancel();
        timer = new Timer();

        updateAdapter(value);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                searchForGame(value);
            }
        }, Constants.COOL_DOWN_TIMER_MIllI_SECOND);
    }


    protected void updateAdapter(String value) {

        ArrayList<GameModel> gameModelLinkedList = new ArrayList<>();

        // Capitlizing the first letter of a game
        String gameNameWithCapitalLetter = value.substring(0, 1).toUpperCase() + value.substring(1);


        for (GameModel game : gamesList) {
            if (!game.getGameName().startsWith(gameNameWithCapitalLetter)) {
                gameModelLinkedList.add(game);
            }
        }


        gamesList.removeAll(gameModelLinkedList);
        mAdapter.notifyDataSetChanged();

    }


    protected boolean checkIsInList(String name) {


        // Capitlizing the first letter of a game
        String gameNameWithCapitalLetter = name.substring(0, 1).toUpperCase() + name.substring(1);

        for (GameModel game : gamesList) {
            if (game.getGameName().startsWith(gameNameWithCapitalLetter))
                return true;
        }
        return false;
    }


    private boolean isTextValidate(String value) {
        return !value.equals("") && !value.equals("\\s+") && null != value;
    }


    protected void addGame(GameModel gameModel) {
        gamesList.add(gameModel);
        mAdapter.notifyDataSetChanged();
    }

    private CommonAdapter<GameModel> createAdapter() {

        return new CommonAdapter<GameModel>(gamesList, R.layout.game_instance) {


            private ViewHolders.UserGameHolder gameHolder;

            @Override
            public ViewHolders OnCreateHolder(View v) {

                return new ViewHolders.UserGameHolder(v);
            }

            @Override
            public void OnBindHolder(final ViewHolders holder, final GameModel model, final int position) {


                // Sync gamesCounter with the adapter items number
                gamesCounter = mAdapter.getItemCount();

                // show or hide the noFriendsSection
                if(mAdapter.getItemCount()<1)
                    showNoGamesSection();
                else
                    hideNoGamesSection();




                // - get element from your dataset at this position
                // - replace the contents of the view with that element
                gameHolder = (ViewHolders.UserGameHolder) holder;
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickHolders(model, v);
                    }
                });

                holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        if (app.getGameManager().checkIfHasGameByName(holder.getTitle().getText().toString().trim()))
                        {
                            showOnLongClickDialog(model);
                            removeGameAnimation(v, position);
                        }
                        return true;
                    }
                });


                final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
                app.loadingImage(getApplication(), holder, model.getGamePhotoUrl());

                gameHolder.getPicture().setBorderWidth(6);
                gameHolder.getPicture().setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color));

                // Capitalize game name letters
                String gameName = model.getGameName();
                String capitlizedGameName = gameName.substring(0,1).toUpperCase() +  gameName.substring(1);
                if (gameName.contains(" "))
                {
                    // Capitalize game title letters
                    String cpWord= "";
                    for (int  i = 0 ; i < capitlizedGameName.length(); i++)
                    {
                        if (capitlizedGameName.charAt(i) == 32 && capitlizedGameName.charAt(i+1) != 32)
                        {
                            cpWord= capitlizedGameName.substring(i+1,i+2).toUpperCase() + capitlizedGameName.substring(i+2);
                            capitlizedGameName = capitlizedGameName.replace(capitlizedGameName.charAt(i+1),cpWord.charAt(0));
                        }

                    }
                    gameHolder.setTitle(capitlizedGameName);
                }else {
                    if (capitlizedGameName.equalsIgnoreCase("cs:go")){
                        gameHolder.setTitle(capitlizedGameName.toUpperCase());
                    } else
                    gameHolder.setTitle(capitlizedGameName);
                }


                gameHolder.getTitleView().setTypeface(playbold);
                gameHolder.getSubtitleView().setTypeface(playbold);
                gameHolder.getPcTextView().setTypeface(playbold);
                gameHolder.getPsTextView().setTypeface(playbold);
                gameHolder.getXboxTextView().setTypeface(playbold);

                // Animate holders
                setAnimation(gameHolder.getTitleView(), gameHolder.getSubtitleView(), gameHolder.getPcTextView(), gameHolder.getPsTextView(), gameHolder.getXboxTextView(), gameHolder.getPicture(), position);


                String platforms = model.getGamePlatforms();


                setPlatformColor(platforms.contains("PC"), ViewHolders.UserGameHolder.Platform.PC, R.color.pc_color);
                setPlatformColor(platforms.contains("PS"), ViewHolders.UserGameHolder.Platform.PS, R.color.ps_color);
                setPlatformColor(platforms.contains("XBOX"), ViewHolders.UserGameHolder.Platform.XBOX, R.color.xbox_color);

            }


            public void setPlatformColor(boolean status, ViewHolders.UserGameHolder.Platform platform, int activeColor) {

                int currentColor = (status) ? activeColor : R.color.hint_color;
                TextView view = gameHolder.getPlatformTextView(platform);
                view.setTextColor(ContextCompat.getColor(getApplicationContext(), currentColor));

            }

            // animate holders
            public void setAnimation(View viewToAnimate1, View viewToAnimate2, View viewToAnimate3, View viewToAnimate4, View viewToAnimate5, View viewToAnimate6, int position) {
                // If the bound view wasn't previously displayed on screen, it's animated
                if (position > -1) {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
                    viewToAnimate1.startAnimation(animation);
                    viewToAnimate2.startAnimation(animation);
                    viewToAnimate3.startAnimation(animation);
                    viewToAnimate4.startAnimation(animation);
                    viewToAnimate5.startAnimation(animation);
                    viewToAnimate6.startAnimation(animation);

                }
            }


        };
    }


    protected void addedGameMessage(String gameName) {
        // success message
        // String Msg = String.format(getResources().getString(R.string.signup_successful_message), username);

        // results if it's successed
        Toast.makeText(getApplicationContext(), "Game (" + gameName + ") Added", Toast.LENGTH_LONG).show();
    }





    protected void showLoadingAnimation() {
        mRecyclerView.setVisibility(RecyclerView.INVISIBLE);
        loadGamesProgressbar.setVisibility(View.VISIBLE);
    }

    protected void hideLoadingAnimation() {
        mRecyclerView.setVisibility(RecyclerView.VISIBLE);
        loadGamesProgressbar.setVisibility(View.INVISIBLE);
    }






    protected abstract void searchForGame(String value);

    protected abstract void OnClickHolders(GameModel gameModel, View v);

    protected abstract void OnStartActivity();

    protected void showOnLongClickDialog(final GameModel gameModel) {

        final Dialog onLongClickGameDialog;
        onLongClickGameDialog = new Dialog(AddGame.this);
        onLongClickGameDialog.setContentView(R.layout.game_long_click_pop_up);
        onLongClickGameDialog.show();


        onLongClickGameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView verificationDeleteText;
        Button deleteYesButton , deleteNoButton;

        verificationDeleteText = (TextView) onLongClickGameDialog.findViewById(R.id.delete_verification_text);
        deleteYesButton = ( Button) onLongClickGameDialog.findViewById(R.id.delete_game_yes_button);
        deleteNoButton = ( Button) onLongClickGameDialog.findViewById(R.id.delete_game_no_button);

        Typeface sansation = Typeface.createFromAsset(getResources().getAssets() ,"sansationbold.ttf");
        Typeface playregular = Typeface.createFromAsset(getResources().getAssets() ,"playregular.ttf");

        deleteYesButton.setTypeface(sansation);
        deleteNoButton.setTypeface(sansation);
        verificationDeleteText.setTypeface(playregular);


        // Delete Game
        deleteYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGame(gameModel.getGameID(),gameModel.getGameName());
                slideRightAnimate(getHolderView());
                onLongClickGameDialog.dismiss();

                // Decrease the number of games
                gamesCounter--;

                // Show or hide the no games dialog
                if (gamesCounter<1)
                    showNoGamesSection();
                else
                    hideNoGamesSection();
            }
        });


        // Remove Dialog
        deleteNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLongClickGameDialog.dismiss();
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = onLongClickGameDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }



    protected void showtourDialog() {

        final Dialog tourDialog;
        tourDialog = new Dialog(AddGame.this);
        tourDialog.setContentView(R.layout.tour_dialog);
        tourDialog.show();

        tourDialog.setCancelable(false);

        tourDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView greetingTextview , tourMessageTextview;
        Button addGamesButton , skipButton;


        SchemaHelper schemaHelper = new SchemaHelper(this);



        Typeface sansation = Typeface.createFromAsset(getResources().getAssets() ,"sansationbold.ttf");
        Typeface playregular = Typeface.createFromAsset(getResources().getAssets() ,"playregular.ttf");

        greetingTextview = (TextView) tourDialog.findViewById(R.id.tour_dialog_greeting) ;
        greetingTextview.setTypeface(playregular);

        tourMessageTextview = (TextView) tourDialog.findViewById(R.id.tour_message_dialog);
        tourMessageTextview.setTypeface(playregular);

        addGamesButton = ( Button) tourDialog.findViewById(R.id.add_games_tour_button);
        skipButton = ( Button) tourDialog.findViewById(R.id.skip_button_dialog);

        String greetingWithUsername = String.format(getResources().getString(R.string.add_game_tour_greeting), schemaHelper.getUsernameByIndex(1));
        greetingTextview.setText(greetingWithUsername);

        addGamesButton.setTypeface(sansation);
        skipButton.setTypeface(sansation);


        // Show the add game activity
        addGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourDialog.dismiss();
            }
        });



        // Skip the add game activity and go to the main app menu
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainAppMenuCore.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition( R.anim.slide_in_right_layouts, R.anim.slide_out_right_layouts);
            }
        });




        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = tourDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }



    protected void removeGameAnimation(View holderView, int position) {

        if (position > -1) {
            setHolderView(holderView);
        }
    }


    public View getHolderView() {
        return holderView;
    }

    public void setHolderView(View holderView) {
        this.holderView = holderView;
    }

    // Animation Deosn't work yet  !
    private void slideRightAnimate(View viewToAnimate)
    {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right);


        viewToAnimate.startAnimation(animation);
        viewToAnimate.setVisibility(View.GONE);

    }

    protected void showNoGamesSection(){
        Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down_view);

        noGameImageView.setVisibility(View.INVISIBLE);
        noGameImageView.startAnimation(slideDown);
        noGameImageView.setVisibility(View.VISIBLE);

        noGameMessage.setVisibility(View.INVISIBLE);
        noGameMessage.startAnimation(slideDown);
        noGameMessage.setVisibility(View.VISIBLE);

    }
    protected void hideNoGamesSection(){
        noGameImageView.setVisibility(View.GONE);
        noGameMessage.setVisibility(View.GONE);
    }


    protected abstract void deleteGame(final String gameKey, String gameName);

}
