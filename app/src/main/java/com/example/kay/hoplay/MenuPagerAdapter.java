package com.example.kay.hoplay;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
                    return new RequestsFragment();
                case 1  :
                    return new CommunityFragment();
                case 2 :
                    return new MakeRequestFragment();
                case 3  :
                    return new  UserFragment();
                case 4 :
                    return new SettingsFragment();

            }


            return null;
        }

        @Override
        public int getCount()
        {
            return 5;
        }

}
