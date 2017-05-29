package com.example.kay.hoplay.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.Cores.RequestCore.NewRequestCore;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.Models.PlayerModel;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Models.SavedRequestModel;
import com.example.kay.hoplay.Cores.UserProfileCores.AddGameCore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParentRequestFragments extends Fragment {

    protected App app ;
    protected boolean hasReq;





    public ParentRequestFragments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        app = App.getInstance();

        View view = getView();

        return view;
    }







    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("shit","It destroyed");
    }



}
