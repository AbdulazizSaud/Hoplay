package com.example.kay.hoplay.Cores.RequestCore;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kay.hoplay.Cores.UserProfileCores.AddGameCore;
import com.example.kay.hoplay.Fragments.NewRequestFragment;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;


public class NewRequestFragmentCore extends NewRequestFragment implements FirebasePaths {

    private Dialog savedRequestPopupDialog;

    @Override
    protected void OnClickHolders(RequestModel model, View v) {
        showSavedRequestDialog(model , v);

    }



    private void showSavedRequestDialog(final RequestModel requestModel , View view)
    {


        savedRequestPopupDialog = new Dialog(this.getContext());
        savedRequestPopupDialog.setContentView(R.layout.saved_request_on_click_pop_up);
        savedRequestPopupDialog.show();


        savedRequestPopupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Button requestButton , updateButton , deleteButton;

        requestButton = ( Button) savedRequestPopupDialog.findViewById(R.id.request_saved_request_button);
        updateButton = ( Button) savedRequestPopupDialog.findViewById(R.id.update_saved_request_button);
        deleteButton = ( Button) savedRequestPopupDialog.findViewById(R.id.delete_saved_request_button);

        Typeface sansation = Typeface.createFromAsset(getResources().getAssets() ,"sansationbold.ttf");
        requestButton.setTypeface(sansation);
        updateButton.setTypeface(sansation);
        deleteButton.setTypeface(sansation);



        // New request
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        // Delete Game
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSavedRequest(requestModel, String.valueOf(requestModel.getSavedRequestIndex()).trim());
                savedRequestPopupDialog.dismiss();
            }
        });


        // Update request
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(),EditRequestCore.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("savedReq",requestModel);
                i.putExtras(bundle);
                startActivity(i);
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = savedRequestPopupDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }


    private void deleteSavedRequest(RequestModel model,String savedReqIndex)
    {
        // users_info_ -> user id  -> _games_ -> _saved_requests_ -> Requests
        DatabaseReference savedReqRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_GAMES_REFERENCES).child(FIREBASE_SAVED_REQS_ATTR).child(FIREBASE_SAVED_REQUESTS_REQUESTS_ATTR);


        // go to saved request index -> 0 , 1 , 2 ...
        savedReqRef.child(savedReqIndex.trim()).removeValue();

        // no remove it from the app
        removeFromSaveRequest(model);

    }






    @Override
    protected void OnStartActivity() {
        app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_SAVED_REQS_PATH).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() == null)
                    return;

                Log.e("indexxx",dataSnapshot.toString());
                addToSaveRequest(dataSnapshot.getValue(RequestModel.class) , dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                addToSaveRequest(dataSnapshot.getValue(RequestModel.class) , dataSnapshot.getKey());

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
