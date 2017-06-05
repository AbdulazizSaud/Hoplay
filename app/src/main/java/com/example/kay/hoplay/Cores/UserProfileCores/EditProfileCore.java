package com.example.kay.hoplay.Cores.UserProfileCores;

import android.net.Uri;
import android.util.Log;

import com.example.kay.hoplay.CoresAbstract.ProfileAbstracts.EditProfile;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileCore extends EditProfile implements FirebasePaths {

    Uri taskURI;


    @Override
    protected void OnStartActivity() {

    }

    @Override
    protected void saveProfileInfo() {

        if(taskURI !=null) {
            app.getDatabaseUsersInfo().child(app.getUserInformation().getUID() + "/" + FIREBASE_PICTURE_URL_PATH).setValue(taskURI.toString());
        }

        if(!getBioText().equals(oldBio)) {
            app.getDatabaseUsersInfo().child(app.getUserInformation().getUID() + "/" + FIREBASE_BIO_PATH).setValue(getBioText());
        }

        if(currentPosition !=-1)
        {
            String providor = gamesProvidersAdapter.getItem(currentPosition);
            String newValue = getProvidorText();
            String oldValue = "none";

            if(providor.equals(PROVIDOR_PS))
            {
                oldValue= app.getUserInformation().getPSNAcc();
                if(!newValue.equals(oldValue)) {
                    app.getDatabaseUsersInfo().child(app.getUserInformation().getUID() + "/" + FIREBASE_PS_GAME_PROVIDER).setValue(newValue);
                    app.getUserInformation().setPSNAcc(newValue);
                }
            }else if(providor.equals(PROVIDOR_XBOX)){

                oldValue= app.getUserInformation().getXboxLiveAcc();
                if(!newValue.equals(oldValue)) {
                    app.getDatabaseUsersInfo().child(app.getUserInformation().getUID() + "/" + FIREBASE_XBOX_GAME_PROVIDER).setValue(newValue);
                    app.getUserInformation().setXboxLiveAcc(newValue);
                }
            }else if(providor.startsWith(PROVIDOR_PC))
            {
                String prov = providor.replace(PROVIDOR_PC,"");
                oldValue = app.getUserInformation().getPcGamesAcc().get(prov);
                if(!newValue.equals(oldValue)) {
                    app.getDatabaseUsersInfo().child(app.getUserInformation().getUID() + "/" + FIREBASE_PC_GAME_PROVIDER + "/" + prov).setValue(newValue);
                    app.getUserInformation().getPcGamesAcc().put(prov,newValue);
                }
            }


        }

    }

    @Override
    protected void uploadPicture(final CircleImageView circleImageView) {
        app.uploadPicture(circleImageView,app.getUserInformation().getUID()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                taskURI = taskSnapshot.getDownloadUrl();

                circleImageView.setClickable(true);
                setSaveButtonClickAble(true);

            }
        });
    }
}
