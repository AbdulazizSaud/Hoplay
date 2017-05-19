package com.example.kay.hoplay.CoresAbstract.RequestAbstracts;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.kay.hoplay.Adapters.SpinnerAdapter;
import com.example.kay.hoplay.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

public class SearchResults extends AppCompatActivity {

    MaterialBetterSpinner searchPrioritySpinner;
    protected ArrayList<String> searchPriorityList;
    protected ArrayAdapter searchPriorityAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

    }

    private void initControls()
    {
        searchPrioritySpinner = (MaterialBetterSpinner) findViewById(R.id.search_priority_spinner);
        final Typeface playregular = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");
        searchPrioritySpinner.setTypeface(playregular);


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


}
