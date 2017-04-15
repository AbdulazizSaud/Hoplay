package com.example.kay.hoplay.CoresAbstract.ProfileAbstracts;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Models.FriendCommonModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Kay on 2/19/2017.
 */

public abstract class UserList extends AppCompatActivity {


    protected App app;
    protected RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    protected EditText searchEditText;
    protected RelativeLayout FriendsLayout;

    private ArrayList<FriendCommonModel> usersList = new ArrayList<FriendCommonModel>();
    private ArrayList<FriendCommonModel> friendsList = new ArrayList<FriendCommonModel>();

    private CommonAdapter<FriendCommonModel> userdListAdapter = new CommonAdapter<FriendCommonModel>(usersList, R.layout.friend_instance_model) {
        @Override
        public ViewHolders OnCreateHolder(View v) {
            return new ViewHolders.FriendListHolder(v);
        }

        @Override
        public void OnBindHolder(ViewHolders holder, final FriendCommonModel model , int position) {



            holder.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickHolders(model);
                }
            });

            holder.setTitle(model.getFriendUsername());
            app.loadingImage(getApplication(),holder, model.getUserPictureURL());

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list_layout);

        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
        app = App.getInstance();
        mRecyclerView = (RecyclerView) findViewById(R.id.rec_new_chat);
        searchEditText = (EditText)findViewById(R.id.search_new_friend_bar_edittext);
        searchEditText.setTypeface(playbold);
        FriendsLayout = (RelativeLayout) findViewById(R.id.activity_new_chat);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                // search icon changing animation
                searchEditText.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.ic_search_focused_32dp, 0);
                if(s.length() == 0)
                {
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.ic_search_unfocused_32dp, 0);
                }


                String value = s.toString().trim();

                if(isTextVaild(value))
                {
                    updateAdapter(true);
                    searchForUser(value);
                } else
                {
                    reloadFriendList();
                }

            }
        });

        setupRecyclerView();
        //testList();
        onStartActivity();
    }




    private void setupRecyclerView() {

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);


        mAdapter = userdListAdapter;
        mRecyclerView.setAdapter(mAdapter);

       // mLayoutManager = new GridLayoutManager(getApplicationContext(),3);

        mLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }


    
    protected void addToUserList(FriendCommonModel friendCommonModel){
        usersList.add(friendCommonModel);
        updateAdapter(false);
    }

    protected void addToFriendUserList(FriendCommonModel friendCommonModel){
        friendsList.add(friendCommonModel);
    }

    protected void updateAdapter(boolean clearList)
    {
         if(clearList)
             usersList.clear();

        mAdapter.notifyDataSetChanged();

    }

    private boolean isTextVaild(String value) {
        return !value.equals("") && !value.equals("\\s+") && null != value;
    }

    private void reloadFriendList() {
        usersList.clear();

        for(FriendCommonModel commonModel : friendsList)
        {
            addToUserList(commonModel);
        }
    }



    protected void setSearchEditTextVisibility(int v)
    {
        searchEditText.setVisibility(v);
    }

    protected abstract void OnClickHolders(FriendCommonModel model);

    protected abstract void onStartActivity();
    protected abstract void loadFriendList();
    protected abstract void searchForUser(String value);
}
