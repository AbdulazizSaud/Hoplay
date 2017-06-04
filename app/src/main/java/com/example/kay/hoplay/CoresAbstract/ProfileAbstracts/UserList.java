package com.example.kay.hoplay.CoresAbstract.ProfileAbstracts;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Cores.UserProfileCores.FriendsListCore;
import com.example.kay.hoplay.CoresAbstract.MainAppMenu;
import com.example.kay.hoplay.Interfaces.Constants;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Models.FriendCommonModel;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public abstract class UserList extends AppCompatActivity {


    protected App app;
    protected RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    protected EditText searchEditText;
    protected RelativeLayout FriendsLayout;
    private ProgressBar loadUsersProgressBar;
    private     View holderView;


    protected boolean removeFriend;




    private ArrayList<FriendCommonModel> usersList = new ArrayList<FriendCommonModel>();
    private ArrayList<FriendCommonModel> friendsList = new ArrayList<FriendCommonModel>();

    private Timer timer = new Timer();



    private CommonAdapter<FriendCommonModel> userdListAdapter = new CommonAdapter<FriendCommonModel>(usersList, R.layout.friend_instance_model) {
        @Override
        public ViewHolders OnCreateHolder(View v) {
            return new ViewHolders.FriendListHolder(v);
        }

        @Override
        public void OnBindHolder(final ViewHolders holder, final FriendCommonModel model , final int position) {




            holder.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickHolders(model);
                }
            });

            holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    showFriendpopup(model,getApplication());
                    removeFriendButtonViewHolder(v,position);
                    return true;
                }
            });


            holder.setTitle(model.getFriendUsername());
            app.loadingImage(getApplication(),holder, model.getUserPictureURL());

            // animate holders
            setAnimation(holder.getTitleView(),holder.getPicture(),position);



        }

        // animate holders
        public void setAnimation(View viewToAnimate1, View viewToAnimate2 ,  int position) {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > -1) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
                viewToAnimate1.startAnimation(animation);
                viewToAnimate2.startAnimation(animation);
//
            }
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
        loadUsersProgressBar = (ProgressBar) findViewById(R.id.load_users_progressbar_user_list);
        setupRecyclerView();


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                // Just to push
                final String value = s.toString().toLowerCase().trim();
                showLoadingAnimation();

                // chinging search icon when user search for a game
                // search icon changing animation
                if (s.length() == 0) {
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search_unfocused_32dp, 0);
                }
                 searchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search_focused_32dp, 0);


                if (isTextValidate(value)) {
                    searchProcess(value);

                } else {
                }

            }
        });

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


    private void searchProcess(final String value) {

        timer.cancel();
        timer = new Timer();

        updateAdapter(value);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                searchForUser(value);
            }
        }, Constants.COOL_DOWN_TIMER_MIllI_SECOND);
    }




    
    protected void addToUserList(String userKey , String username,String picture,boolean withNotifyChanges){
        FriendCommonModel  friendCommonModel = new FriendCommonModel();
        friendCommonModel.setFriendKey(userKey);
        friendCommonModel.setFriendUsername(username);
        friendCommonModel.setUserPictureURL(picture);

        usersList.add(friendCommonModel);
        friendsList.add(friendCommonModel);

        if(withNotifyChanges)
        mAdapter.notifyDataSetChanged();
    }


    protected void updateAdapter(String value) {

        ArrayList<FriendCommonModel> friendsModelList = new ArrayList<>();

        // Capitlizing the first letter of a game


        for (FriendCommonModel friendCommonModel : usersList)
        {
            if (!friendCommonModel.getFriendUsername().startsWith(value))
            {
                friendsModelList.add(friendCommonModel);
            }
        }


        usersList.removeAll(friendsModelList);
        mAdapter.notifyDataSetChanged();

    }

    private boolean isTextValidate(String value) {
        return !value.equals("") && !value.equals("\\s+") && null != value || !value.isEmpty();
    }

    private void reloadFriendList() {
        usersList.clear();

        for(FriendCommonModel commonModel : friendsList)
        {
            addToUserList(commonModel.getFriendKey(),commonModel.getFriendUsername(),commonModel.getUserPictureURL(),false);
        }
        mAdapter.notifyDataSetChanged();
    }

    protected boolean checkIsInList(String name) {
        // Capitlizing the first letter of a game

        for (FriendCommonModel friendCommonModel : usersList) {
            if (friendCommonModel.getFriendUsername().startsWith(name))
                return true;
        }
        return false;
    }


    protected void showLoadingAnimation() {
        mRecyclerView.setVisibility(RecyclerView.INVISIBLE);
        loadUsersProgressBar.setVisibility(View.VISIBLE);
    }

    protected void hideLoadingAnimation() {
        mRecyclerView.setVisibility(RecyclerView.VISIBLE);
        loadUsersProgressBar.setVisibility(View.INVISIBLE);
    }


    // Animation Deosn't work yet  !
    protected void slideRightAnimate(View holderView)
    {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right);
        holderView.startAnimation(animation);
        holderView.setVisibility(View.GONE);

    }



    protected void showFriendpopup(final FriendCommonModel friendCommonModel,Context c) {


        final Dialog friendLongClickDialog;
        friendLongClickDialog = new Dialog(UserList.this);
        friendLongClickDialog.setContentView(R.layout.pop_up_on_long_click_friend);
        friendLongClickDialog.show();


        friendLongClickDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button chatButton , viewProfileButton , deleteFriendButtton ;

        chatButton = ( Button) friendLongClickDialog.findViewById(R.id.chat_friend_pop_up_button);
        viewProfileButton = ( Button) friendLongClickDialog.findViewById(R.id.view_profile_friend_pop_up_button);
        deleteFriendButtton = ( Button) friendLongClickDialog.findViewById(R.id.delete_friend_pop_up_button);

        Typeface sansation = Typeface.createFromAsset(getResources().getAssets() ,"sansationbold.ttf");
        chatButton.setTypeface(sansation);
        viewProfileButton.setTypeface(sansation);
        deleteFriendButtton.setTypeface(sansation);


        // Open chat activity
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        // view friend profile
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
//
//
//        // delete friend
        deleteFriendButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFriend(friendCommonModel.getFriendKey().trim());
                slideRightAnimate(getHolderView());
                friendLongClickDialog.dismiss();


            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = friendLongClickDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }

    protected void removeFriendButtonViewHolder(View holderView, int position) {
        if (position > -1) {
            setHolderView(holderView);
        }

    }


    public View getHolderView() {
        return holderView;
    }

    public void setHolderView(View holderView) {
        this.holderView = holderView;
    }




    protected abstract void OnClickHolders(FriendCommonModel model);

    protected abstract void onStartActivity();
    protected abstract void loadFriendList();
    protected abstract void searchForUser(String value);
    protected abstract void removeFriend(String friendKey);
}
