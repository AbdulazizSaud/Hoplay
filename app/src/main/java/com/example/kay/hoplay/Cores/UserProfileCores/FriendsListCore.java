package com.example.kay.hoplay.Cores.UserProfileCores;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.kay.hoplay.Cores.ParentCore.UserListCore;
import com.example.kay.hoplay.Models.FriendCommonModel;
import com.example.kay.hoplay.R;
import com.google.firebase.database.DatabaseReference;


public class FriendsListCore extends UserListCore {


    View view1, view2 ;

    @Override
    protected void OnClickHolders(FriendCommonModel model)
    {
        Intent i = new Intent(this,ViewFriendProfileCore.class);
        i.putExtra("user_key",model.getFriendKey());
        startActivity(i);
    }

    @Override
    protected void onStartActivity() {

//        searchEditText.setVisibility(EditText.GONE);
//        searchEditText = null;

        loadFriendList();
    }

    @Override
    protected void showFriendpopup(final FriendCommonModel friendCommonModel) {


        final Dialog friendLongClickDialog;
        friendLongClickDialog = new Dialog(FriendsListCore.this);
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
                deleteFriend(friendCommonModel.getFriendKey().trim());
                slideRightAnimate(getView1(),getView2());
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

    @Override
    protected void removeFriendAnimation(View viewToAnimate1, View viewToAnimate2, int position) {


            if (position > -1) {
                setViews(viewToAnimate1,viewToAnimate2);
            }


    }

    // Animation Deosn't work yet  !
    private void slideRightAnimate(View viewToAnimate1, View viewToAnimate2)
    {
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right);
            viewToAnimate1.startAnimation(animation);
            viewToAnimate2.startAnimation(animation);
            viewToAnimate1.setVisibility(View.GONE);
            viewToAnimate2.setVisibility(View.GONE);

    }


    private void setViews(View view1 , View view2)
    {
        this.view1 = view1;
        this.view2 = view2;
    }

    private View getView1()
    {
        return  view1;
    }

    private View getView2()
    {
        return view2;
    }


    private void deleteFriend(String friendKey)
    {
        // users_info_ -> user id  -> _friend_list_
        DatabaseReference userRequestRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_FRIENDS_LIST_ATTR);

        // access _friend_list_ -> friend key
        userRequestRef.child(friendKey).removeValue();

    }


}