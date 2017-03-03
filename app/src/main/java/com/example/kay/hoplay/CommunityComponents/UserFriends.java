package com.example.kay.hoplay.CommunityComponents;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.model.CommunityUserList;
import com.example.kay.hoplay.model.FriendCommonModel;

import java.util.ArrayList;

/**
 * Created by Kay on 2/19/2017.
 */

public abstract class UserFriends extends AppCompatActivity {


    protected App app;
    protected RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private ArrayList<FriendCommonModel> friendList = new ArrayList<FriendCommonModel>();

    private CommonAdapter<FriendCommonModel> friendListAdapter = new CommonAdapter<FriendCommonModel>(friendList, R.layout.friend_instance_model) {
        @Override
        public ViewHolders OnCreateHolder(View v) {
            return new ViewHolders.FriendListHolder(v);
        }

        @Override
        public void OnBindHolder(ViewHolders holder, final FriendCommonModel model) {



            holder.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickHolders(model);
                }
            });

            holder.setTitle(model.getUsername());
            app.loadingImage(holder, model.getUserPictureURL());

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_friends);
        app = App.getInstance();
        mRecyclerView = (RecyclerView) findViewById(R.id.rec_friend_list);

        setupRecyclerView();
        //testList();
        loadFriendList();
    }



    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rec_friend_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);


        mAdapter = friendListAdapter;
        mRecyclerView.setAdapter(mAdapter);

       // mLayoutManager = new GridLayoutManager(getApplicationContext(),3);

        mLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }



    private void testList() {

        for(int i=0;i<3;i++) {
            // test
            String picUrl = "https://s13.postimg.org/puvr2r9tz/test_user_copy.jpg";
            String username = "Test";
            FriendCommonModel flu = new FriendCommonModel();
            flu.setKey("test"+i);
            flu.setUserPictureURL(picUrl);
            flu.setUsername(username + " ");
            addToList(flu);
        }
    }

    public void addToList(FriendCommonModel friendCommonModel){
        friendList.add(friendCommonModel);
        mAdapter.notifyDataSetChanged();
    }

    protected abstract void OnClickHolders(FriendCommonModel model);

    public abstract void loadFriendList();
}
