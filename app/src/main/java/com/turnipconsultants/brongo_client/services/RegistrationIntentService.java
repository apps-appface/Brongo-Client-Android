package com.turnipconsultants.brongo_client.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.turnipconsultants.brongo_client.models.DeviceInfoInputModel;
import com.turnipconsultants.brongo_client.responseModels.DeviceInfo;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohit on 24-08-2017.
 */

public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService";
    private SharedPreferences pref;
    private PackageInfo pInfo;
    private String appVersion;
    private String os_version;
    private String mobile_no;
    private String oldToken = "";
    private String newToken = "";
    private Context context = this;
    private String defaultMacid = "";
    String refreshedToken, currentMacId = "";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            pref = getSharedPreferences(AppConstants.PREF_NAME, MODE_PRIVATE);
            refreshedToken = pref.getString(AppConstants.FIREBASE_TOKEN, FirebaseInstanceId.getInstance().getToken());
            Log.d(TAG, "Refreshed token: " + refreshedToken);
            if (InternetConnection.isNetworkAvailable(getApplicationContext())) {
                storeDeviceInfo(context);
            } else {
                Log.e(TAG, "onHandleIntent: NOT INTERNET CONNECTION therefore tokens will not work as expected");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void storeDeviceInfo(final Context context) {
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        appVersion = pInfo.versionName;
        mobile_no = pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "");
        String platform = "android";
        os_version = Build.VERSION.RELEASE;
        final String manufacturer = Build.MANUFACTURER;
        final String model_no = Build.MODEL;
        String model = manufacturer + " " + model_no;
        String device_id = Utils.getDeviceId(context);


       /* Map<String, String> data = new HashMap<String, String>();
        data.put("mobileNo", mobile_no);
        data.put("appVersion", appVersion);
        data.put("platform", platform);
        data.put("osVersion", os_version);
        data.put("modelName", model);
        data.put("deviceId", device_id);
        data.put("deviceToken", refreshedToken);

        data.put("userId", "");*/
//        data.put("deviceArn", device_arn);

        DeviceInfoInputModel inputmodel = new DeviceInfoInputModel();
        inputmodel.setMobileNo(mobile_no);
        inputmodel.setAppVersion(appVersion);
        inputmodel.setPlatform(platform);
        inputmodel.setOsVersion(os_version);
        inputmodel.setModelName(model);
        inputmodel.setDeviceId(device_id);
        inputmodel.setDeviceToken(refreshedToken);
        inputmodel.setUserId("");

        RetrofitAPIs apiService = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
        Call<DeviceInfo> call = apiService.sendDeviceInfo(inputmodel);
        call.enqueue(new Callback<DeviceInfo>() {
            @Override
            public void onResponse(Call<DeviceInfo> call, Response<DeviceInfo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DeviceInfo deviceInfo = response.body();

                    if (deviceInfo.getStatusCode() == 200) {
                        Log.i(TAG, "onResponse: " + response.body().getMessage());
                        showDeviceInfo();
                    }

                } else {
                    Log.e(TAG, "onResponse: DEVICE INFO API Got PROBLEM");
                    //Toast.makeText(context, "please try again register", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeviceInfo> call, Throwable t) {
                Log.e("Device ", "Info Error" + t.getMessage());
            }
        });

    }

    private void showDeviceInfo() {
        System.out.println("-------------------------------------------");
        Log.i("Device ", "REGISTRATION Details");
        Log.i("App Version No : ", appVersion);
        Log.i("OS Version No : ", os_version);
        Log.i("Platform : ", "android");
        Log.i("Mobile no : ", mobile_no);
        Log.i("Model No : ", Build.MANUFACTURER + " " + Build.MODEL);
        Log.i("Old Device Token  : ", oldToken);
        Log.i("New Device Token  : ", newToken);
//        Log.i("Macid: ",defaultMacid);
        System.out.println("-------------------------------------------");
    }


}
