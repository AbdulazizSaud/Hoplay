package com.example.kay.hoplay.Activities.MainMenu;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kay.hoplay.Activities.ActivityInterface;
import com.example.kay.hoplay.Adapters.MenuPagerAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Authentication.LoginActivity;
import com.example.kay.hoplay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;


import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;


/*


 */
public abstract class MainAppMenu extends AppCompatActivity implements ActivityInterface {

    private static MainAppMenu instance;
    private ViewPager viewPagerMenu;
    private boolean changeBar = true;
    private  BottomBar bottomBar;
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
       BottomBarTab unreadMessages = bottomBar.getTabAtPosition(1);
        unreadMessages.setBadgeCount(4);
        // Control the badge's visibility
        //unreadMessages.hide();

        // Change the displayed count for this badge.
        // unreadMessages.setCount(4);

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


        viewPagerMenu.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (changeBar)
                    bottomBar.selectTabAtPosition(position,true);
                changeBar = true;
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        MenuPagerAdapter menuPagerAdapter = new MenuPagerAdapter(getSupportFragmentManager());
        viewPagerMenu.setAdapter(menuPagerAdapter);


        setupBottmBar(savedInstanceState);
    }

    private void setupBottmBar(Bundle savedInstanceState) {
//        bottomBar = BottomBar.attach(this, savedInstanceState);
//
//        bottomBar.setItemsFromMenu(R.menu.icons_menu, new OnMenuTabSelectedListener() {
//            @Override
//            public void onMenuItemSelected(int itemId) {
//
//                changeBar = false;
//                switch (itemId) {
//                    case R.id.requests_item:
//                        viewPagerMenu.setCurrentItem(0, true);
//                        break;
//                    case R.id.community_item:
//                        viewPagerMenu.setCurrentItem(1, true);
//                        break;
//                    case R.id.new_rquest:
//                        viewPagerMenu.setCurrentItem(2, true);
//                        break;
//                    case R.id.profile_item:
//                        viewPagerMenu.setCurrentItem(3, true);
//                        break;
//                }
//            }
//        });
//
//        // Setting colors for different tabs when there's more than three of them.
//        bottomBar.mapColorForTab(0, "#880E4F");
//        bottomBar.mapColorForTab(1, "#880E4F");
//        bottomBar.mapColorForTab(2, "#880E4F");
//        bottomBar.mapColorForTab(3, "#880E4F");
//
//
//        bottomBar.setOnItemSelectedListener(new OnTabSelectedListener() {
//            @Override
//            public void onItemSelected(int position) {
//
//            }
//
//        });
//
//
//        // Make a Badge for the first tab, with red background color and a value of "4".
//        // Notification number it was 4 i put it 0w
//        // the first argument is about which tap 1 represent the second one which is the settings .
//        // try to change the  third argument to understand  it . change it to 4 ! or any number .
//        BottomBarBadge unreadMessages = bottomBar.makeBadgeForTabAt(1, "#FF0000", 4);
//
//        // Control the badge's visibility
//        unreadMessages.show();
//        //unreadMessages.hide();
//
//        // Change the displayed count for this badge.
//        // unreadMessages.setCount(4);
//
//        // Change the show / hide animation duration.
//        unreadMessages.setAnimationDuration(200);
//
//        // If you want the badge be shown always after unselecting the tab that contains it.
//        //unreadMessages.setAutoShowAfterUnSelection(true);
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    public  BottomBar getBottomBar() {
        return bottomBar;
    }

    public static MainAppMenu getInstance()
    {
        return instance;
    }

    protected void toLogin() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);

    }

    public abstract void OnStartActivity();

}

