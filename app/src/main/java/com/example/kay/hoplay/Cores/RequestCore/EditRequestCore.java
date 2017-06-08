package com.example.kay.hoplay.Cores.RequestCore;

import android.util.Log;

import com.example.kay.hoplay.CoresAbstract.RequestAbstracts.EditRequest;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.RequestModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Kay on 6/6/2017.
 */

public class EditRequestCore extends EditRequest implements FirebasePaths {


    protected void OnStartActivity() {


        // Load regions
        DatabaseReference regionsRef = app.getDatabaseRegions();
        regionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot != null) {
                    for (DataSnapshot region : dataSnapshot.getChildren()) {
                        regionList.add(region.getValue(String.class).trim());
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void updateReqFirebase(RequestModel requestModel) {

        // users_info_ -> user id  -> _games_ -> _saved_requests_ -> Requests
        DatabaseReference savedReqRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_SAVED_REQS_PATH);


        Log.e("uniqid1",requestModel.getSavedReqUniqueID());


        for (RequestModel savedReq : app.getSavedRequests())
        {
            Log.e("uniqidxxxxxx",savedReq.getSavedReqUniqueID());
            if (savedReq.getSavedReqUniqueID().equals(requestModel.getSavedReqUniqueID()))
            {

                Log.e("shimatta","shimatta");
                int index = app.getSavedRequests().indexOf(savedReq);
                app.getSavedRequests().set(index,requestModel);
            }
        }


        savedReqRef.setValue(app.getSavedRequests());

    }


}
