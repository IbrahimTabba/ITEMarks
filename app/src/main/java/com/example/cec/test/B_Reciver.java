package com.example.cec.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by CEC on 22-Aug-18.
 */

public class B_Reciver extends BroadcastReceiver {
    NotificationManager nm ;
    @Override
    public void onReceive(Context context, Intent intent) {
        String S = "";
        ArrayList<String> Arr = (ArrayList<String>) intent.getExtras().get("Names");
        Log.e("New Mawaaads Are" , " " );
        NotificationCompat.InboxStyle NB = new NotificationCompat.InboxStyle();
        for(int i = 0 ; i < Arr.size() ; i++)
        {
            NB.addLine(Arr.get(i));
            Log.e("Mada : " , Arr.get(i));
        }
        Intent notifyIntent = new Intent(context, chosing.class);
        // Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Create the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder nb = new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("صدرت علامات جديدة : ")
                .setSmallIcon(R.drawable.noicon)
                .setContentText(S)
                .setStyle(NB)
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_MAX);
        nb.setContentIntent(notifyPendingIntent);
        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(232,nb.build());
    }
}
