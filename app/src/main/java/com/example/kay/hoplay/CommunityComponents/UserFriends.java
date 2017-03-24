package com.example.kay.hoplay.CommunityComponents;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Models.FriendCommonModel;

import java.util.ArrayList;

/**
 * Created by Kay on 2/19/2017.
 */

public abstract class UserFriends extends AppCompatActivity {


    protected App app;
    protected RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextInputEditText searchEditText;

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
        setContentView(R.layout.new_chat_activity);

        app = App.getInstance();
        mRecyclerView = (RecyclerView) findViewById(R.id.rec_new_chat);
        searchEditText = (TextInputEditText)findViewById(R.id.search_new_friend_bar_textinputedittext);


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString().trim();

                if(isTextVaild(value))
                {
                    updateAdapter(true);
                    searchForUser(value);
                }

            }
        });

        setupRecyclerView();
        //testList();
        loadFriendList();
    }




    private void setupRecyclerView() {

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

    protected void addToList(FriendCommonModel friendCommonModel){
        friendList.add(friendCommonModel);
         updateAdapter(false);
    }

    protected void updateAdapter(boolean clearList)
    {
         if(clearList)
         friendList.clear();

        mAdapter.notifyDataSetChanged();

    }

    private boolean isTextVaild(String value) {
        return !value.equals("") && !value.equals("\\s+") && null != value;
    }

    protected abstract void OnClickHolders(FriendCommonModel model);

    protected abstract void loadFriendList();
    protected abstract void searchForUser(String value);
}
