package com.hoplay.kay.hoplay.Fragments;



import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hoplay.kay.hoplay.R;
import com.hoplay.kay.hoplay.Services.SettingsScreen;


public class SettingsFragment extends  Fragment {



    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_community, container, false);
        //Fragment fragment = fi
        return v;
    }



}
