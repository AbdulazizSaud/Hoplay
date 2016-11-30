package com.example.kay.hoplay.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.example.kay.hoplay.Activities.ChatActivity;
import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Common.ViewHolders;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.model.CommunityUserList;
import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityFragment extends Fragment {

//    ListView communityListview ;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<CommunityUserList> communityUserLists=new ArrayList<CommunityUserList>();

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


         String picUrl = "https://trello-avatars.s3.amazonaws.com/287b7527a973a31518a71b15aacb11eb/original.png";
         CommunityUserList test = new CommunityUserList("kdesu",picUrl);
         test.setLastMsg( "azoz, please DONT do a drama");
         communityUserLists.add(test);
        ///
        mAdapter = createAdapter();

        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        return view;
    }


    private CommonAdapter<CommunityUserList> createAdapter(){
        return new CommonAdapter<CommunityUserList>(communityUserLists,R.layout.new_user_message) {
            @Override
            public ViewHolders OnCreateHolder(View v) {

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(),ChatActivity.class);
                        v.getContext().startActivity(i);
                    }
                });

                return new ViewHolders.CommunityHolder(v);
            }

            @Override
            public void OnBindHolder(ViewHolders holder,CommunityUserList model) {
                // - get element from your dataset at this position
                // - replace the contents of the view with that element
                ImageLoader loader = App.getInstance().getImageLoader();
                ViewHolders.CommunityHolder communityHolder = (ViewHolders.CommunityHolder)holder;

                if(model.getUserPictureURL().length() > 0){
                    loader.get(model.getUserPictureURL(),
                            ImageLoader.getImageListener(
                                    holder.getPicture()
                                    ,R.drawable.profile_default_photo
                                    ,R.drawable.profile_default_photo));

                } else {
                    CircularImageView picture = holder.getPicture();
                    picture.setImageResource(R.drawable.profile_default_photo);
                    holder.setPicture(picture);
                }
                holder.setTitle(model.getFullName());
                communityHolder.setCommunitySubtitle(model.getLastMsg());
                holder.setTime(model.getLastMsgDate());
            }
        };
    }


}
