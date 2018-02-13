package com.turnipconsultants.brongo_client.others;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.activities.HomeActivity;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;

import java.util.List;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Pankaj on 06-12-2017.
 */

public class CheckIfForeground {
    private static final String TAG = "CheckIfForeground";
    private Context context;
    private String message;
    private boolean foreground;

    public CheckIfForeground(Context context, String message) {
        this.context = context;
        this.message = message;
    }

    public void IsInForeground() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                Log.i("Foreground App", appProcess.processName);

                if (context.getPackageName().equalsIgnoreCase(appProcess.processName)) {
                    Log.i(TAG, "foreground true:" + appProcess.processName);
                    foreground = true;
                    // close_app();
                }
            }
        }

        if (foreground) {
            foreground = false;
            LeadBroadcastOnScreen();
            Log.i(TAG, "Application onScreen...push Received");

        }
        //if not foreground
        NotificationCompat.Builder builder = buildNotification();
        NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(getRandomNotificationID(), builder.build());
        foreground = false;
        Log.i(TAG, "Application offScreen...push Received"+message);


    }

    protected NotificationCompat.Builder buildNotification() {
        PendingIntent pIntent = PendingIntent.getActivity(
                context,
                0,
                getIntent(),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.brongo_logo_push)
                .setTicker(message)
                .setAutoCancel(true)
                .setContentIntent(pIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(getComplexNotificationView());
        } else {
            builder = builder.setContentTitle(getTitle())
                    .setContentText(getText())
                    .setSmallIcon(android.R.drawable.ic_menu_gallery);
        }
        return builder;
    }

    private int getRandomNotificationID() {
        return new Random().nextInt(100);
    }

    private Intent getIntent() {
        return new Intent(context, HomeActivity.class);
    }

    void LeadBroadcastOnScreen() {
        Intent intent = new Intent(AppConstants.SPECIFIC_PUSH.LEADS_UPDATE);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private RemoteViews getComplexNotificationView() {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews notificationView = new RemoteViews(
                context.getPackageName(),
                R.layout.notification
        );

        // Locate and set the Image into customnotificationtext.xml ImageViews
        notificationView.setImageViewResource(
                R.id.imagenotileft,
                R.mipmap.ic_launcher_round);

        // Locate and set the Text into customnotificationtext.xml TextViews
        notificationView.setTextViewText(R.id.title, getTitle());
        notificationView.setTextViewText(R.id.text, getText());

        return notificationView;
    }

    public CharSequence getTitle() {
        return "Brongo";
    }

    public CharSequence getText() {
        return message;
    }
}