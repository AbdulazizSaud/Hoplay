package com.example.kay.hoplay.CommunityComponents;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.CommunityComponents.UserListActivities.CreateChat;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Models.CommunityChatModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class Community extends Fragment {

//    ListView communityListview ;


    private RecyclerView mRecyclerView;
    private ImageView bgChatImageView;
    private RecyclerView.LayoutManager mLayoutManager;
    private  FloatingActionButton newPrivateChatFloatingActionButton;

    protected ArrayList<CommunityChatModel> communityUserLists=new ArrayList<CommunityChatModel>();
    protected RecyclerView.Adapter mAdapter;

    protected App app;

    public Community() {
        // Required empty public constructor
        app = App.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false) ;
//        communityListview = (ListView)  view.findViewById(R.id.community_listview);

        setupRecyclerView(view);


        bgChatImageView = (ImageView) view.findViewById(R.id.splash);

        // Go to Friends List to start new private chat
        newPrivateChatFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.new_private_chat_floatingactionbutton);
        newPrivateChatFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),CreateChat.class);
                startActivity(i);
            }
        });

        //testList();



        OnStartActivity();

        return view;
    }


    private void setupRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);


        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }


    public void addToList(CommunityChatModel communityUserList){
        communityUserLists.add(communityUserList);
        mAdapter.notifyDataSetChanged();

        if(communityUserLists.size() > 0)
            bgChatImageView.setVisibility(View.INVISIBLE);
    }

    private CommonAdapter<CommunityChatModel> createAdapter(){
        return new CommonAdapter<CommunityChatModel>(communityUserLists,R.layout.new_user_message_instance) {
            @Override
            public ViewHolders OnCreateHolder(View v) {

                return new ViewHolders.CommunityHolder(v);
            }

            @Override
            public void OnBindHolder(ViewHolders holder, final CommunityChatModel model) {
                // - get element from your dataset at this position
                // - replace the contents of the view with that element
                ViewHolders.CommunityHolder communityHolder = (ViewHolders.CommunityHolder)holder;
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickHolders(model,v);
                    }
                });




                app.loadingImage(getContext(),holder, model.getUserPictureURL());

                holder.setTitle(model.getFullName());
                communityHolder.setCommunitySubtitle(model.getLastMsg());
                holder.setTime(model.getLastMsgDate());
            }
        };
    }




    protected void testList(String name) {
        // test
        String picUrl = "https://s13.postimg.org/puvr2r9tz/test_user_copy.jpg";
        String username = name;
        String lastMessage = "Test has joined your request click to replay";

        CommunityChatModel clu = new CommunityChatModel();
        clu.setUserPictureURL(picUrl);
        clu.setLastMsg(lastMessage);

        addToList(clu);

    }
//


    protected abstract void OnClickHolders(CommunityChatModel model, View v);
    protected abstract void OnStartActivity();

}
