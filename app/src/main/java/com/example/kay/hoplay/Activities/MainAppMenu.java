package com.example.kay.hoplay.Activities;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kay.hoplay.Adapters.MenuPagerAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabSelectedListener;
import com.roughike.bottombar.OnTabSelectedListener;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


public class MainAppMenu extends AppCompatActivity{


    private static MainAppMenu instance;

    public static void setInstance(MainAppMenu instance) {
        MainAppMenu.instance = instance;
    }

    private static BottomBar bottomBar;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

  private  ViewPager mainAppMenu ;
  private  boolean changeBar = true ;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);



        // Set the screen orientation to the portrait mode :
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // Get the current display info :
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        if(display.getWidth() > display.getHeight())
        {
            // Landscape mode
            Log.d("Oreintation :","Landscape  mode");
        }
        else
        {
            // Portrait mode
            Log.d("Oreintation : ", "Portrait mode");
        }



        mainAppMenu = (ViewPager) findViewById(R.id.view_pager);

        mainAppMenu.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                    if(changeBar)
                        bottomBar.selectTabAtPosition(position , true);

                    changeBar = true;
                }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        MenuPagerAdapter menuPagerAdapter = new MenuPagerAdapter(getSupportFragmentManager());

        mainAppMenu.setAdapter(menuPagerAdapter);




        bottomBar = BottomBar.attach(this, savedInstanceState);

        bottomBar.setItemsFromMenu(R.menu.icons_menu, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {

                changeBar = false ;
                switch (itemId) {
                    case R.id.requests_item:
                        mainAppMenu.setCurrentItem(0 , true);
                        break;
                    case R.id.community_item:
                        mainAppMenu.setCurrentItem(1 , true);
                        break ;
                    case R.id.new_rquest:
                        mainAppMenu.setCurrentItem(2 , true);
                        break;
                    case R.id.profile_item:
                        mainAppMenu.setCurrentItem(3 , true);
                        break;
                }
            }
        });



        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is sign out
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }
        };


       /*  bottomBar.setFragmentItems(getSupportFragmentManager(), R.id.view_pager,
                new BottomBarFragment(NavigatorFragment.newInstance("Content for requests."), R.drawable.ic_public_black_24dp, "requests"),
                new BottomBarFragment(NavigatorFragment.newInstance("Content for settings."), R.drawable.ic_menu_black_24dp, "settings"),
                new BottomBarFragment(NavigatorFragment.newInstance("Content for community."), R.drawable.ic_people_black_24dp, "community"),
                new BottomBarFragment(NavigatorFragment.newInstance("Content for profile."), R.drawable.ic_face_black_24dp, "profile")
        );
       */

        // Setting colors for different tabs when there's more than three of them.
        bottomBar.mapColorForTab(0, "#880E4F");
        bottomBar.mapColorForTab(1, "#880E4F");
        bottomBar.mapColorForTab(2, "#880E4F");
        bottomBar.mapColorForTab(3, "#880E4F");



        bottomBar.setOnItemSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onItemSelected(int position) {

                }

        });



        // Make a Badge for the first tab, with red background color and a value of "4".
        // Notification number it was 4 i put it 0w
        // the first argument is about which tap 1 represent the second one which is the settings .
        // try to change the  third argument to understand  it . change it to 4 ! or any number .
        BottomBarBadge unreadMessages = bottomBar.makeBadgeForTabAt(1, "#FF0000", 4);

        // Control the badge's visibility
        unreadMessages.show();
        //unreadMessages.hide();

        // Change the displayed count for this badge.
       // unreadMessages.setCount(4);

        // Change the show / hide animation duration.
        unreadMessages.setAnimationDuration(200);

        // If you want the badge be shown always after unselecting the tab that contains it.
        //unreadMessages.setAutoShowAfterUnSelection(true);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.


    }




    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onDestroy() {super.onDestroy();}

    @Override
    protected void onStop() {
        super.onStop();
        App.getInstance().removemAuthListener(authStateListener);

    }

    @Override
    protected void onStart(){
        super.onStart();
        App.getInstance().setmAuthListener(authStateListener);
    }

    public static BottomBar getBottomBar(){
        return bottomBar;
    }




}
