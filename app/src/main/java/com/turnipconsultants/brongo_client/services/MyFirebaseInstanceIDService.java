package com.turnipconsultants.brongo_client.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.api.account.register.RegisterUserClientService;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;

/**
 * Created by mohit on 22-08-2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    private SharedPreferences pref;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        init();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        pref.edit().putString(AppConstants.FIREBASE_TOKEN, refreshedToken).commit();
        Log.i(TAG, "onTokenRefresh: " + refreshedToken);

        Applozic.getInstance(this).setDeviceRegistrationId(refreshedToken);
        if (MobiComUserPreference.getInstance(this).isRegistered()) {
            try {
                RegistrationResponse registrationResponse = new RegisterUserClientService(this).updatePushNotificationId(refreshedToken);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sendTheTokenToServer();

    }

    private void init() {
        pref = getSharedPreferences(AppConstants.PREF_NAME, MODE_PRIVATE);
    }


    private void sendTheTokenToServer() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }

}
