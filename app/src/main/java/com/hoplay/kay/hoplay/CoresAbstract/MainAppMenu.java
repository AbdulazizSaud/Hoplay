package com.hoplay.kay.hoplay.CoresAbstract;


import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.adapters.ToolbarBindingAdapter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hoplay.kay.hoplay.Adapters.MenuPagerAdapter;
import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.Cores.AuthenticationCore.LoginCore;
import com.hoplay.kay.hoplay.Cores.CommunityCore;
import com.hoplay.kay.hoplay.Cores.RequestCore.LobbyFragmentCore;
import com.hoplay.kay.hoplay.Cores.RequestCore.NewRequestFragmentCore;
import com.hoplay.kay.hoplay.CoresAbstract.AuthenticationAbstracts.Login;
import com.hoplay.kay.hoplay.Fragments.LobbyFragment;
import com.hoplay.kay.hoplay.Fragments.NoGameFragment;
import com.hoplay.kay.hoplay.Fragments.ParentRequestFragments;
import com.hoplay.kay.hoplay.R;

import com.hoplay.kay.hoplay.Services.CallbackHandlerCondition;
import com.hoplay.kay.hoplay.Services.ErrorHandler;
import com.hoplay.kay.hoplay.Services.HandlerCondition;
import com.google.firebase.auth.FirebaseUser;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;


/*


 */
public abstract class MainAppMenu extends AppCompatActivity  {

    private static MainAppMenu instance;
    protected ViewPager viewPagerMenu;
    private boolean changeBar = true;
    protected BottomBar bottomBar;
    protected App app;
    protected  MenuPagerAdapter menuPagerAdapter;

    protected boolean isDone=false;


    public static BottomBarTab unreadMessages;




    /***************************************/

    // Main methods
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        App.mainAppMenuActivityIsActive = true ;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Log.i("-->","it destroy");
    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        instance = this;
        ///Thread.setDefaultUncaughtExceptionHandler(new ErrorHandler(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        // Set the screen orientation to the portrait mode :
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //
        app = App.getInstance();
        // this method for init Main menu
        initControl(savedInstanceState);
        // this method for work only on create , basiclly init a mechinsims
        OnStartActivity();


    }

    /***************************************/

    protected void welcomeMessage(String username) {
        // success message
        String Msg = String.format(getResources().getString(R.string.main_app_menu_welcome_message), username);

        // results if it's successed
        Toast.makeText(getApplicationContext(), Msg,Toast.LENGTH_LONG).show();

    }


    private void initControl(final Bundle savedInstanceState) {
        viewPagerMenu = (ViewPager) findViewById(R.id.view_pager);

        // Prevent destroyin old fragments : it was 4
        viewPagerMenu.setOffscreenPageLimit(2);


        bottomBar = (BottomBar) findViewById(R.id.bottomBar);


        menuPagerAdapter = new MenuPagerAdapter(getSupportFragmentManager());

        CallbackHandlerCondition callback = new CallbackHandlerCondition() {
            @Override
            public boolean callBack() {

                if (isDone) activateMainAppMenu(savedInstanceState);
                return isDone;
            }
        };

        new HandlerCondition(callback,0);

    }
    private void activateMainAppMenu( Bundle savedInstanceState) {

        viewPagerMenu.setAdapter(menuPagerAdapter);
        setupBottmBar(savedInstanceState);
    }

    private void setupBottmBar(Bundle savedInstanceState)
    {

        bottomBar.selectTabAtPosition(3);
        unreadMessages = bottomBar.getTabAtPosition(1);
        unreadMessages.setBadgeBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.app_color));





        viewPagerMenu.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomBar.selectTabAtPosition(position, true);
                bottomBar.setInActiveTabColor(0x000033);
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });






        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                changeBar = false;
                switch (tabId) {
                    case R.id.tab_search_game:
                        viewPagerMenu.setCurrentItem(0, true);
                        break;
                    case R.id.tab_chat_community:
                        viewPagerMenu.setCurrentItem(1, true);
                        unreadMessages.removeBadge();
                        break;
                    case R.id.tab_make_request:
                        viewPagerMenu.setCurrentItem(2, true);
                        break;
                    case R.id.tab_user_profile:
                        viewPagerMenu.setCurrentItem(3, true);
                        break;
                }
            }

        });

    }



    public BottomBar getBottomBar() {
        return bottomBar;
    }
    public static MainAppMenu getInstance() {
        return instance;
    }
    protected void toLogin() {
        Intent i = new Intent(getApplicationContext(), LoginCore.class);
        startActivity(i);

    }


    public static void setChatCounterOnBottombar(int counter){
        // CHAT NOTIFICATION COUNTER

        unreadMessages.setBadgeCount(counter);

    }


    public void switchFragment(ParentRequestFragments fragments)
    {
        menuPagerAdapter.setParentRequestFragments(fragments);
        viewPagerMenu.setAdapter(menuPagerAdapter);
        viewPagerMenu.setCurrentItem(2);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String res = data.getStringExtra("result");

                if(res.equals("lobby"))
                {
                    setCurrentViewPagerMenu(2);
                } else if (res.equals("view profile"))
                {
                    String profileId = data.getStringExtra("profileId");

                }

            }
        }
    }


    public void setCurrentViewPagerMenu(int index)
    {
        viewPagerMenu.setCurrentItem(index);

    }

    @Override
    protected void onStop() {
        super.onStop();
        App.mainAppMenuActivityIsActive = false ;
    }

    protected abstract void OnStartActivity();


}

