package com.example.kay.hoplay.CoresAbstract;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.Cores.ChatCore.CreateChatCore;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Models.CommunityChatModel;

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
                Intent i = new Intent(getContext(),CreateChatCore.class);
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


    public void removeFromList(String key){

        for(CommunityChatModel communityChatModel : communityUserLists) {
            if(communityChatModel.getChatKey().equals(key)) {
                communityUserLists.remove(communityChatModel);
                mAdapter.notifyDataSetChanged();
                break;
            }
        }

        if(communityUserLists.size() < 0)
            bgChatImageView.setVisibility(View.INVISIBLE);
    }


    private CommonAdapter<CommunityChatModel> createAdapter(){
        return new CommonAdapter<CommunityChatModel>(communityUserLists,R.layout.new_user_chat_instance) {
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

                holder.setTitle(model.getChatName());
                communityHolder.setCommunitySubtitle(model.getLastMsg());
                communityHolder.setCounter(String.valueOf(model.getChatCounter()));
                holder.setTime(model.getLastMsgDate());
            }
        };
    }





    protected abstract void OnClickHolders(CommunityChatModel model, View v);
    protected abstract void OnStartActivity();

}
