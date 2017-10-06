package com.hoplay.kay.hoplay.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.hoplay.kay.hoplay.Cores.AuthenticationCore.LoginCore;
import com.hoplay.kay.hoplay.R;

/**
 * Created by khaledAlhindi on 10/4/2017 AD.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        Intent i = new Intent(this, LoginCore.class);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_ONE_SHOT);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Hoplay");
        builder.setContentText(remoteMessage.getNotification().getBody());
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_stat_hoplaylogo);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());


    }
}
