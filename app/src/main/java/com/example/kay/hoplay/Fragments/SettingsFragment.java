package com.example.kay.hoplay.Fragments;



import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Services.SettingsScreen;


public class SettingsFragment extends  Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.settingsPF);
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        return v;
    }



}
