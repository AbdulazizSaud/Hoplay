package com.example.kay.hoplay.Cores.UserProfileCores;

import com.example.kay.hoplay.CoresAbstract.ProfileAbstracts.EditProfile;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Kay on 6/5/2017.
 */

public class EditProfileCore extends EditProfile implements FirebasePaths {
    @Override
    protected void OnStartActivtiy() {

//        // users_info_ -> user id
//        DatabaseReference userRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID());
//
//        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                if(dataSnapshot.hasChild(FIREBASE_PS_GAME_PROVIDER))
//                {
//                    // leave core for m
//                    gamesProvidersAdapter.add("PSN Account");
//                }
//                if (dataSnapshot.hasChild(FIREBASE_XBOX_GAME_PROVIDER))
//                {
//                    gamesProvidersAdapter.add("XBOX Account");
//                }
//                if (dataSnapshot.hasChild(FIREBASE_PC_GAME_PROVIDER))
//                {
//                    for (DataSnapshot shot apshot.getChildren())
//                    {
//                            //gamesProvidersValues.put(shot.getKey().toString(),shot.getValue().toString());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
}
