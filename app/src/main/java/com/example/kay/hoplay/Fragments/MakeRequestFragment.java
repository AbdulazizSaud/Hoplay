package com.example.kay.hoplay.Fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kay.hoplay.Adapters.RecentActivitiesAdapter;
import com.example.kay.hoplay.Adapters.SavedRequestsAdapter;
import com.example.kay.hoplay.RequestsRequires.NewRequest;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.model.RecentActivityList;
import com.example.kay.hoplay.model.SavedRequestsList;

import java.util.ArrayList;

public class MakeRequestFragment extends Fragment {

    private  Button newRequestButton;
    private TextView savedRequestsMessage ;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public MakeRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_make_request_fragment, container, false);
        newRequestButton = (Button) view.findViewById(R.id.new_request_button);
        savedRequestsMessage = (TextView) view.findViewById(R.id.saved_activity_message_textview);
        newRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),NewRequest.class);
                startActivity(i);
            }
        });


        Typeface sansation = Typeface.createFromAsset(getActivity().getAssets() ,"sansationbold.ttf");
        newRequestButton.setTypeface(sansation);
        savedRequestsMessage.setTypeface(sansation);



        mRecyclerView = (RecyclerView) view.findViewById(R.id.saved_requests_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
         mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        ArrayList<SavedRequestsList> savedRequestsLists =new ArrayList<SavedRequestsList>();







        savedRequestsLists.add(saveRequest("Rocket League","http://www.mobygames.com/images/covers/l/307552-rocket-league-playstation-4-front-cover.jpg","Doubles ranked ?","2 Players"));
        savedRequestsLists.add(saveRequest("Overwatch","https://b.thumbs.redditmedia.com/ksN39DPM7HFaXTP_tBi-IuYYfWccRBjYykD6VFSePXE.jpg","-_- I need to get diamond","6 Players"));
        savedRequestsLists.add(saveRequest("Dying Light","http://shinigaming.com/wp-content/uploads/2015/01/dying-light-logo-high-resolution.jpg","Lets Kill some zombies * ^ *","3 Players"));
        savedRequestsLists.add(saveRequest("Tekken 6","http://vignette2.wikia.nocookie.net/tekken/images/0/04/T6-logo-trophy.png/revision/latest?cb=20140330054519&path-prefix=en","Quick Game ?","2 Players"));


        mAdapter = new SavedRequestsAdapter(savedRequestsLists);
        mRecyclerView.setAdapter(mAdapter);



        return view;

    }



    public SavedRequestsList saveRequest(String gameName , String gamePhoto , String requestDescription , String numberOfPlayers)
    {
        SavedRequestsList savedRequestsList = new SavedRequestsList();
        savedRequestsList.setGameName(gameName);
        savedRequestsList.setGamePhotoURL(gamePhoto);
        savedRequestsList.setRequestDescription(requestDescription);
        savedRequestsList.setNumberOfPlayers(numberOfPlayers);
        return savedRequestsList;
    }

}
