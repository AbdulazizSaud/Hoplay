package com.example.kay.hoplay.RequestComponents.RequestsRequires;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kay.hoplay.Adapters.SpinnerAdapter;
import com.example.kay.hoplay.R;

import java.util.Arrays;

/**
 * Created by Kay on 2/12/2017.
 */

public abstract class NewRequest extends AppCompatActivity {


    /***************************************/

    private TextView makeRequestMessage;
    private Button makeRequestButton;
    private Spinner regionSpinner;
    private Spinner numberOfPlayersSpinner;
    private Spinner playersRanksSpinner;
    private RadioButton pcRadiobutton;
    private RadioButton psRadiobutton;
    private RadioButton xboxRadiobutton;
    private EditText gamesEditText;
    private EditText requestDescritopnEdittext;
    private ImageView goBackToMainAppMenuImageview;
    private CheckedTextView dropDownSpinnerItem;
    private TextView spinnerItem;
    /***************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);
        initControl();


    }

    private void initControl() {
        final Typeface sansationbold = Typeface.createFromAsset(getResources().getAssets(), "sansationbold.ttf");
        makeRequestMessage = (TextView) findViewById(R.id.make_request_message_textview);
        makeRequestMessage.setTypeface(sansationbold);
        makeRequestButton = (Button) findViewById(R.id.make_request_button);
        makeRequestButton.setTypeface(sansationbold);
        regionSpinner = (Spinner) findViewById(R.id.region_spinner);
        numberOfPlayersSpinner = (Spinner) findViewById(R.id.number_of_players_spinner);
        playersRanksSpinner = (Spinner) findViewById(R.id.players_rank_spinner);
        pcRadiobutton = (RadioButton) findViewById(R.id.pc_choice_radiobutton);
        pcRadiobutton.setTypeface(sansationbold);
        psRadiobutton = (RadioButton) findViewById(R.id.ps_choice_radiobutton);
        psRadiobutton.setTypeface(sansationbold);
        xboxRadiobutton = (RadioButton) findViewById(R.id.xbox_choice_radiobutton);
        xboxRadiobutton.setTypeface(sansationbold);
        gamesEditText = (EditText) findViewById(R.id.games_edittext);
        gamesEditText.setTypeface(sansationbold);
        goBackToMainAppMenuImageview = (ImageView) findViewById(R.id.go_back_toMainAppMenu_imageView);


        requestDescritopnEdittext = (EditText) findViewById(R.id.request_descritption_edittext);

        makeRequestMessage.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(requestDescritopnEdittext, InputMethodManager.SHOW_IMPLICIT);


        // (this ,R.array.players_ranks,R.layout.spinnner_item);

        ArrayAdapter regionAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
                Arrays.asList(getResources().getStringArray(R.array.countries_array)));


        ArrayAdapter playersRanksAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
                Arrays.asList(getResources().getStringArray(R.array.players_ranks)));

        ArrayAdapter playersNumberAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
                Arrays.asList(getResources().getStringArray(R.array.players_number)));


        ArrayAdapter gamesAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
                Arrays.asList(getResources().getStringArray(R.array.games_array)));


        gamesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        playersRanksAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        regionAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        playersNumberAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);


        numberOfPlayersSpinner.setAdapter(playersNumberAdapter);
        regionSpinner.setAdapter(regionAdapter);
        playersRanksSpinner.setAdapter(playersRanksAdapter);
    }


    public void goBackToMainAppMenu(View v)
    {finish();}
}