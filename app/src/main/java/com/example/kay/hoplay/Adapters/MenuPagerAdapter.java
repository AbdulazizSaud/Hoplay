package com.example.kay.hoplay.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.kay.hoplay.Cores.CommunityCore;
import com.example.kay.hoplay.Cores.RequestCore.MakeRequestFragmentCore;
import com.example.kay.hoplay.Cores.RequestCore.SearchRequestCore;
import com.example.kay.hoplay.Cores.UserProfileCores.UserProfileCore;

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
                    return new SearchRequestCore();
                case 1 :
                    return new CommunityCore();
                case 2 :
                    return new MakeRequestFragmentCore();
                case 3 :
                    return new UserProfileCore();


            }


            return new CommunityCore();
        }

        @Override
        public int getCount()
        {
            return 4;
        }

}
