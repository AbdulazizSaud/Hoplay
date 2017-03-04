package com.example.kay.hoplay.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.kay.hoplay.CommunityComponents.CommunityActivity;
import com.example.kay.hoplay.RequestComponents.MakeRequest;
import com.example.kay.hoplay.RequestComponents.SearchRequest;
import com.example.kay.hoplay.UserProfileComponents.UserProfileFragment;

/**
 * Created by Kay on 6/15/2016.
 */
public class MenuPagerAdapter extends FragmentStatePagerAdapter {


        public MenuPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position)
            {
                case 0:
                    return new SearchRequest();
                case 1  :
                    return new CommunityActivity();
                case 2 :
                    return new MakeRequest();
                case 3 :
                    return new UserProfileFragment();


            }


            return null;
        }

        @Override
        public int getCount()
        {
            return 4;
        }

}
