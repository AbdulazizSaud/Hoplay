package com.example.kay.hoplay.Cores.RequestCore;

import com.example.kay.hoplay.CoresAbstract.RequestAbstracts.EditRequest;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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


}
