package com.example.kay.hoplay.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kay.hoplay.App.App;

public class ParentRequestFragments extends Fragment {

    protected App app ;
    protected boolean hasReq;


    public ParentRequestFragments() {
        // Required empty public constructor
        app = App.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = getView();

        return view;
    }







    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("shit","It destroyed");
    }



}
