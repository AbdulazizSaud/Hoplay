package com.example.kay.hoplay.Cores.RequestCore;

import android.view.View;

import com.example.kay.hoplay.Fragments.NewRequestFragment;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.util.Request;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class NewRequestFragmentCore extends NewRequestFragment implements FirebasePaths {


    private DatabaseReference saveRequestRef;

    private ChildEventListener saveRequestEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.getValue() == null)
                return;

            addToSaveRequest(dataSnapshot.getValue(RequestModel.class), dataSnapshot.getKey());
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            // IF EXSIST = DO CHANGES ELSE DO ADD

            int index = Integer.parseInt(dataSnapshot.getKey());
            RequestModel requestModel = dataSnapshot.getValue(RequestModel.class);
            updateSavedRequest(index,requestModel);
           // addToSaveRequest(dataSnapshot.getValue(RequestModel.class), dataSnapshot.getKey());

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
    };


    @Override
    protected void OnClickHolders(RequestModel model, View v,int position) {

        showSavedRequestDialog(model, v,position);

    }


    @Override
    protected void addRequestToFirebase(String platform, String gameName, String matchType, String region, String numberOfPlayers, String rank, String description) {

        Request request = new Request(platform,gameName,matchType,region,numberOfPlayers,rank,description);
        app.switchMainAppMenuFragment(new LobbyFragmentCore(request.getRequestModelReference()));
    }

    @Override
    protected void deleteSavedRequest(RequestModel model) {

        removeFromSaveRequest(model);

        saveRequestRef.getParent().child("count").setValue(arrayList.size());

        saveRequestRef.setValue(arrayList, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError !=null)
                {
                }
            }
        });

        // Show or hide no saved reqs elements
        if (countSavedReqs()<1)
            showNoSavedRequestElements();
        else
            hideNoSavedRequestElements();


    }


    private long countSavedReqs()
    {

        final long[] count = {0};

        DatabaseReference userInfoRef =  app.getDatabaseUsersInfo().child(app.getUserInformation().getUID());
        userInfoRef.child(FIREBASE_GAMES_REFERENCES).child(FIREBASE_SAVED_REQS_ATTR).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count[0] =  dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return count[0];
    }

    @Override
    protected void OnStartActivity() {

        saveRequestRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_SAVED_REQS_PATH);
        saveRequestRef.addChildEventListener(saveRequestEventListener);
    }
}
