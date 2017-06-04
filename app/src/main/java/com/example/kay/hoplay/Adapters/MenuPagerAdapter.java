package com.example.kay.hoplay.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.view.ViewGroup;

import com.example.kay.hoplay.Cores.CommunityCore;
import com.example.kay.hoplay.Cores.RequestCore.SearchRequestCore;
import com.example.kay.hoplay.Cores.UserProfileCores.UserProfileCore;
import com.example.kay.hoplay.Fragments.NewRequestFragment;
import com.example.kay.hoplay.Fragments.NoGameFragment;
import com.example.kay.hoplay.Fragments.ParentRequestFragments;
import com.example.kay.hoplay.R;

import java.util.ArrayList;


public class MenuPagerAdapter extends FragmentStatePagerAdapter {



        private FragmentManager fm;
        private ParentRequestFragments parentRequestFragments;
        private ArrayList<Fragment> fragments;

        public MenuPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
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
            notifyDataSetChanged();
        }


    @Override
        public int getCount()
        {
            return 4;
        }




}
