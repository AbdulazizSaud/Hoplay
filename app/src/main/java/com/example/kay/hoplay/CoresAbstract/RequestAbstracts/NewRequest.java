package com.example.kay.hoplay.CoresAbstract.RequestAbstracts;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.example.kay.hoplay.Adapters.SpinnerAdapter;
import com.example.kay.hoplay.R;


import java.util.Arrays;
import java.util.List;

import static android.R.attr.scaleHeight;
import static android.R.attr.scaleWidth;

/**
 * Created by Kay on 2/12/2017.
 */

public abstract class NewRequest extends AppCompatActivity {


    /***************************************/

    // NOTE : Deal with spinners  here as Edittexts because it's not created natively by android studio it's a Dependency/Library.


    private TextView makeRequestMessage;
    private Button makeRequestButton;
    private Button saveRequestButton;
    private MaterialBetterSpinner countrySpinner;
    private MaterialBetterSpinner numberOfPlayersSpinner;
    private MaterialBetterSpinner playersRanksSpinner;
    private MaterialBetterSpinner matchTypeSpinner ;
    private RadioButton pcRadiobutton;
    private RadioButton psRadiobutton;
    private RadioButton xboxRadiobutton;
    private AutoCompleteTextView gamesAutoCompleteTextView;
    private EditText requestDescritopnEdittext;
    private ImageView goBackToMainAppMenuImageview;
    private CheckedTextView dropDownSpinnerItem;
    private TextView spinnerItem;
    private  String[] gamesArray;
    private  int layoutItemId;
    private EditText descriptionEdittext;
    private List<String> gamesList;
    private   ArrayAdapter<String> gamesAdapter;

    /***************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);
        initControl();
        fieldsListeners();








    }

    private void initControl() {
        final Typeface sansationbold = Typeface.createFromAsset(getResources().getAssets(), "sansationbold.ttf");
        final Typeface playregular = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");
        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
        makeRequestMessage = (TextView) findViewById(R.id.make_request_message_textview);
        makeRequestMessage.setTypeface(sansationbold);
        makeRequestButton = (Button) findViewById(R.id.request_button_new_request);
        makeRequestButton.setTypeface(sansationbold);
        countrySpinner = (MaterialBetterSpinner) findViewById(R.id.country_spinner_new_request);
        countrySpinner.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.text_color));
        numberOfPlayersSpinner = (MaterialBetterSpinner) findViewById(R.id.players_number_spinner_new_request);
        countrySpinner.setTypeface(playbold);
        matchTypeSpinner = (MaterialBetterSpinner) findViewById(R.id.match_type_spinner_new_request);
        matchTypeSpinner.setTypeface(playbold);
        matchTypeSpinner.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.text_color));
        numberOfPlayersSpinner.setTypeface(playbold);
        playersRanksSpinner = (MaterialBetterSpinner) findViewById(R.id.players_rank_spinner_new_request);
        playersRanksSpinner.setTypeface(playbold);
        saveRequestButton = (Button) findViewById(R.id.save_request_button_new_request);
        saveRequestButton.setTypeface(sansationbold);
        pcRadiobutton = (RadioButton) findViewById(R.id.pc_choice_radiobutton);
        pcRadiobutton.setTypeface(sansationbold);
        psRadiobutton = (RadioButton) findViewById(R.id.ps_choice_radiobutton);
        psRadiobutton.setTypeface(sansationbold);
        xboxRadiobutton = (RadioButton) findViewById(R.id.xbox_choice_radiobutton);
        xboxRadiobutton.setTypeface(sansationbold);
        gamesAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.games_autocompletetextview_new_request);
        gamesAutoCompleteTextView.setTypeface(playbold);

        descriptionEdittext = (EditText) findViewById(R.id.description_edittext_new_request);
        descriptionEdittext.setTypeface(playbold);

        gamesArray = getResources().getStringArray(R.array.games_list);
        layoutItemId = android.R.layout.simple_dropdown_item_1line;
        gamesArray = getResources().getStringArray(R.array.games_list);
        gamesList = Arrays.asList(gamesArray);
        gamesAdapter = new ArrayAdapter<>(this, layoutItemId, gamesList);
        gamesAutoCompleteTextView.setAdapter(gamesAdapter);
//        goBackToMainAppMenuImageview = (ImageView) findViewById(R.id.go_back_toMainAppMenu_imageView);


//        requestDescritopnEdittext = (EditText) findViewById(R.id.request_descritption_edittext);

        makeRequestMessage.requestFocus();


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(requestDescritopnEdittext, InputMethodManager.SHOW_IMPLICIT);


        // (this ,R.array.players_ranks,R.layout.spinnner_item);

        ArrayAdapter regionAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
                Arrays.asList(getResources().getStringArray(R.array.countries_array)));
//
//
        ArrayAdapter playersRanksAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
                Arrays.asList(getResources().getStringArray(R.array.players_ranks)));
//
        ArrayAdapter playersNumberAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
                Arrays.asList(getResources().getStringArray(R.array.players_number)));

        ArrayAdapter matchTypeAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
                Arrays.asList(getResources().getStringArray(R.array.match_types)));

//
//
//        ArrayAdapter gamesAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
//                Arrays.asList(getResources().getStringArray(R.array.games_array)));
//
//
//        gamesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        playersRanksAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        regionAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        playersNumberAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        matchTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
//
//
        numberOfPlayersSpinner.setAdapter(playersNumberAdapter);
            countrySpinner.setAdapter(regionAdapter);
        playersRanksSpinner.setAdapter(playersRanksAdapter);
        matchTypeSpinner.setAdapter(matchTypeAdapter);

    }


    // Change icon listener for autocomplete and spinners
    private void fieldsListeners()
    {
        gamesAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                gamesAutoCompleteTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_games_focused_24dp , 0, 0, 0);
                if(s.length() == 0)
                {
                    gamesAutoCompleteTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_games_unfocused_24dp , 0, 0, 0);
                }
                if (s.toString().equalsIgnoreCase("Overwatch"))
                {
                    matchTypeSpinner.setVisibility(View.VISIBLE);
                    slideInFromLeft(matchTypeSpinner);

                }
                else
                {
                    if (matchTypeSpinner.isShown())
                    slideOutToRight(matchTypeSpinner);

                    matchTypeSpinner.setVisibility(View.GONE);

                }




            }
        });
        countrySpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                countrySpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_focused_24dp , 0, 0, 0);
                if(s.length() ==0 )
                {
                    countrySpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_unfocused_24dp , 0, 0, 0);

                }



            }
        });

        numberOfPlayersSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                numberOfPlayersSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_group_focused_24dp , 0, 0, 0);
                if(s.length() == 0 )
                {
                    numberOfPlayersSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_group_unfocused_24dp , 0, 0, 0);

                }
                numberOfPlayersSpinner.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.text_color));


            }
        });

        playersRanksSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                playersRanksSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_grade_focused_24dp , 0, 0, 0);
                if(s.length() ==0)
                {
                    playersRanksSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_grade_unfocused_24dp , 0, 0, 0);
                }
                playersRanksSpinner.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.text_color));
            }
        });

        matchTypeSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equalsIgnoreCase("Competitive"))
                {
                    matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_competitive_24dp , 0, 0, 0);
                }
                if (s.toString().equalsIgnoreCase("Quick Match"))
                {
                    matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_quick_match_24dp , 0, 0, 0);
                }
                if (s.length() == 0 )
                {
                    matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_unfocused_24dp , 0, 0, 0);
                }
            }
        });


    }


    public void slideInFromLeft(View viewToAnimate) {
        // If the bound view wasn't previously displayed on screen, it's animated

            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
            animation.setDuration(200);
            viewToAnimate.startAnimation(animation);
    }

    public void slideOutToRight(View viewToAnimate)
    {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right);
        viewToAnimate.startAnimation(animation);
    }

    public void goBackToMainAppMenu(View v)
    {finish();}
}
