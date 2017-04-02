package com.example.kay.hoplay.Activities.MainMenu;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.kay.hoplay.Activities.ActivityInterface;
import com.example.kay.hoplay.Adapters.MenuPagerAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Authentication.LoginCore;
import com.example.kay.hoplay.R;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;


/*


 */
public abstract class MainAppMenu extends AppCompatActivity implements ActivityInterface {

    private static MainAppMenu instance;
    private ViewPager viewPagerMenu;
    private boolean changeBar = true;
    private BottomBar bottomBar;
    protected App app;

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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;

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

    private void initControl(Bundle savedInstanceState) {
        viewPagerMenu = (ViewPager) findViewById(R.id.view_pager);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);


        MenuPagerAdapter menuPagerAdapter = new MenuPagerAdapter(getSupportFragmentManager());
        viewPagerMenu.setAdapter(menuPagerAdapter);


        setupBottmBar(savedInstanceState);
    }

    private void setupBottmBar(Bundle savedInstanceState)
    {

        bottomBar.selectTabAtPosition(1);
        BottomBarTab unreadMessages = bottomBar.getTabAtPosition(1);
        unreadMessages.setBadgeCount(4);

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


    protected void welcomeMessage(String username)
    {
        // success message
        String Msg = String.format(getResources().getString(R.string.signup_successful_message), username);

        // results if it's successed
        Toast.makeText(getApplicationContext(), Msg,Toast.LENGTH_LONG).show();

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
    public abstract void OnStartActivity();

}

