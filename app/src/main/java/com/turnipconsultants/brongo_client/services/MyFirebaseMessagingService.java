package com.turnipconsultants.brongo_client.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.applozic.mobicomkit.api.notification.MobiComPushReceiver;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.CheckIfForeground;

import java.util.Set;

/**
 * Created by mohit on 22-08-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingServ";
    private Context context = this;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try {
            Log.i(TAG, "onMessageReceived: ");
            if (remoteMessage.getData().size() > 0) {
                if (MobiComPushReceiver.isMobiComPushNotification(remoteMessage.getData())) {
                    Log.i(TAG, "Applozic notificationz processing...");
                    MobiComPushReceiver.processMessageAsync(this, remoteMessage.getData());
                    return;
                }
            }

            Set<String> keys = remoteMessage.getData().keySet();
            if (keys != null && keys.size() > 0 && keys.contains(AppConstants.SPECIFIC_PUSH.NOTIFICATION_TYPE)) {
                switch (remoteMessage.getData().get(AppConstants.SPECIFIC_PUSH.NOTIFICATION_TYPE)) {
                    case AppConstants.SPECIFIC_PUSH.ACCEPTED_BROKERS:
                        Intent intent = null;
                        intent = new Intent(AppConstants.SPECIFIC_PUSH.ACCEPTED_BROKERS);
                        intent.putExtra("imageUrl", remoteMessage.getData().get("image"));
                        intent.putExtra("count", remoteMessage.getData().get("count"));
                        context.sendBroadcast(intent);
                        break;
                    case AppConstants.SPECIFIC_PUSH.LEADS_UPDATE:
                    case AppConstants.SPECIFIC_PUSH.NEW_BROKER:
                    case AppConstants.SPECIFIC_PUSH.DROP_DEAL:
                    case AppConstants.SPECIFIC_PUSH.ASSIGN_NEW_BROKER:
                    case AppConstants.SPECIFIC_PUSH.SCHEDULE_MEETING:
                        new CheckIfForeground(context, remoteMessage.getData().get("message")).IsInForeground();
                        Log.i(TAG, "NOitiType" + remoteMessage.getData().get("NotiType"));
                        break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
