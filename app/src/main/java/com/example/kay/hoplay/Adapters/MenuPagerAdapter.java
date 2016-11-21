package com.example.kay.hoplay.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.kay.hoplay.Fragments.CommunityFragment;
import com.example.kay.hoplay.Fragments.MakeRequestFragment;
import com.example.kay.hoplay.RequestsRequires.RequestsFragment;
import com.example.kay.hoplay.Fragments.SettingsFragment;
import com.example.kay.hoplay.Fragments.UserFragment;

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
                    return new UserFragment();
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
