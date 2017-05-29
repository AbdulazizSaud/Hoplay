package com.example.kay.hoplay.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.kay.hoplay.Cores.CommunityCore;
import com.example.kay.hoplay.Cores.RequestCore.SearchRequestCore;
import com.example.kay.hoplay.Cores.UserProfileCores.UserProfileCore;
import com.example.kay.hoplay.Fragments.NewRequestFragment;
import com.example.kay.hoplay.Fragments.NoGameFragment;
import com.example.kay.hoplay.Fragments.ParentRequestFragments;


public class MenuPagerAdapter extends FragmentStatePagerAdapter {


        private ParentRequestFragments parentRequestFragments;

        public MenuPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position)
            {
                case 0:
                    return new SearchRequestCore();
                case 1 :
                    return new CommunityCore();
                case 2 :
                    return parentRequestFragments;
                case 3 :
                    return new UserProfileCore();


            }


            return new CommunityCore();
        }

        public void setParentRequestFragments(ParentRequestFragments fragments)
        {
            this.parentRequestFragments = fragments;
        }


        @Override
        public int getCount()
        {
            return 4;
        }

}
