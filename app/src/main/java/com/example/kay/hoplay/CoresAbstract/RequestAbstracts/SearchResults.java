package com.example.kay.hoplay.CoresAbstract.RequestAbstracts;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.Adapters.SpinnerAdapter;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Models.RecentGameModel;
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

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                return new ViewHolders.SearchResultsHolder(v);
            }

            @Override
            public void OnBindHolder(ViewHolders holder, RequestModel model, int position) {



                app.loadingImage(getApplication(), holder, model.getRequestPicture());
                holder.setTitle(model.getRequestTitle());
                holder.setSubtitle(model.getDescription());
                holder.setTime(app.convertFromTimeStampToDate(String.valueOf(model.getTimeStamp())));
                holder.setNumberOfPlayers(String.valueOf(model.getPlayerNumber()));
                holder.setSubtitle2(model.getAdminName());

            }

        };
    }
    protected abstract void OnStartActivity();

}
