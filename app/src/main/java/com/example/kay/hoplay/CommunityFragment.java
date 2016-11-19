package com.example.kay.hoplay;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityFragment extends Fragment {

//    ListView communityListview ;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public CommunityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false) ;
//        communityListview = (ListView)  view.findViewById(R.id.community_listview);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        ArrayList<CommunityUserList> communityUserLists=new ArrayList<CommunityUserList>();



        ///
        String picUrl = "http://brockmentalhealth.ca/wp-content/uploads/2015/10/Community-family-and-friends-page.jpg";
        CommunityUserList test = new CommunityUserList("azoz",picUrl);
        communityUserLists.add(test);
        ///
        mAdapter = new MyAdapter(communityUserLists);
        mRecyclerView.setAdapter(mAdapter);



        return view;
    }




}
