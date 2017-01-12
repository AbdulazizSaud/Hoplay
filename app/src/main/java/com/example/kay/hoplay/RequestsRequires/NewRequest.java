package com.example.kay.hoplay.RequestsRequires;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kay.hoplay.Adapters.SpinnerAdapter;
import com.example.kay.hoplay.R;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

public class NewRequest extends AppCompatActivity {

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

    private CheckedTextView dropDownSpinnerItem;

    private TextView spinnerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout((int) (dm.widthPixels * .8), (int) (dm.heightPixels * .8));



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);


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


        requestDescritopnEdittext = (EditText) findViewById(R.id.request_descritption_edittext);
        requestDescritopnEdittext.requestFocus();

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


}
