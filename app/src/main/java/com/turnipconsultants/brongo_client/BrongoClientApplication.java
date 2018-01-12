package com.turnipconsultants.brongo_client;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDex;
import android.util.Base64;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.turnipconsultants.brongo_client.DBTables.DaoMaster;
import com.turnipconsultants.brongo_client.DBTables.DaoSession;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.FetchMicroMarketResponse;
import com.turnipconsultants.brongo_client.responseModels.ProfileResponseModel;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Pankaj on 07-12-2017.
 */

public class BrongoClientApplication extends Application {
    private static final String TAG = "BrongoClientApplication";
    private static RequestOptions requestOptions;
    private DaoSession daoSession;
    private Context context;
    private FetchMicroMarketResponse microMarketResponse;
    private boolean isSuccess;

    public FetchMicroMarketResponse getMicroMarketResponse() {
        return microMarketResponse;
    }

    public void setMicroMarketResponse(FetchMicroMarketResponse microMarketResponse) {
        this.microMarketResponse = microMarketResponse;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public static RequestOptions getRequestOption(boolean showCached) {
        if (requestOptions == null) {
            if (showCached) {
                requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.user);
                requestOptions.error(R.drawable.user);
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                requestOptions.timeout(120000);
            } else {
                requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.user);
                requestOptions.error(R.drawable.user);
                requestOptions.skipMemoryCache(true);
                requestOptions.timeout(120000);
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            }
        }
        return requestOptions;
    }

    public static RequestOptions getRequestOptionSized(int width, int height) {
        if (requestOptions == null) {
            requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.user);
            requestOptions.error(R.drawable.user);
            requestOptions.skipMemoryCache(true);
            requestOptions.timeout(120000);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.override(width, height);
        }
        return requestOptions;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        daoSession = new DaoMaster(new DaoMaster.DevOpenHelper(this, AppConstants.DB_NAME).getWritableDb()).newSession();
        //getHashForFacebook();
        MultiDex.install(this);
    }

    private void getHashForFacebook() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.turnipconsultants.brongo",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }


    //Testing
    private void FetchProfile() {
        try {
            final String headerDeviceId = Utils.getDeviceId(context);
            final String headerPlatform = "android";
            final SharedPreferences pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
            String headerToken = pref.getString("token", "");
            if (InternetConnection.isNetworkAvailable(context)) {
                RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
                Call<ProfileResponseModel> call = apiInstance.getClientProfile(headerToken, headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""));
                call.enqueue(new Callback<ProfileResponseModel>() {
                    @Override
                    public void onResponse(Call<ProfileResponseModel> call, Response<ProfileResponseModel> response) {
                        try {
                            if (response != null && response.isSuccessful()) {
                                ProfileResponseModel responseModel = response.body();
                                if (responseModel.getStatusCode() == 200) {
                                    List<ProfileResponseModel.DataEntity> data = responseModel.getData();
                                    ProfileResponseModel.DataEntity user = data.get(0);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString(AppConstants.PREFS.USER_FIRST_NAME, user.getFirstName());
                                    editor.putString(AppConstants.PREFS.USER_LAST_NAME, user.getLastName());
                                    editor.putFloat(AppConstants.PREFS.USER_RATING, user.getRating());
                                    editor.putString(AppConstants.PREFS.USER_EMAIL, user.getEmailId());
                                    editor.commit();

                                }
                            } else {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                    new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                    FetchProfile();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileResponseModel> call, Throwable t) {
                        Log.e(TAG, "onFailure: Application was unable to load profile");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
