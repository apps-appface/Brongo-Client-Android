package com.turnipconsultants.brongo_client.mvp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.applozic.mobicomkit.api.conversation.database.MessageDatabaseService;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.responseModels.FetchMicroMarketResponse;
import com.turnipconsultants.brongo_client.responseModels.ProfileResponseModel;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Pankaj on 17-11-2017.
 */

public class HomePresenterImpl implements HomePresenter {
    private HomeView homeView;
    private Context context;

    RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
    private SharedPreferences pref;
    String headerDeviceId, headerPlatform, headerToken;

    public HomePresenterImpl(HomeView homeView, Activity context) {
        this.homeView = homeView;
        this.context = context;
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
    }


    @Override
    public void getChatCount() {

        try {
            int oldChatCount = pref.getInt(AppConstants.PREFS.CHAT_COUNT, 0);
            homeView.setChatCount(oldChatCount);

            int totalUnreadCount = new MessageDatabaseService(context).getTotalUnreadCount();
            homeView.setChatCount(totalUnreadCount);
            pref.edit().putInt(AppConstants.PREFS.CHAT_COUNT, totalUnreadCount).commit();
        } catch (Exception e) {
            e.printStackTrace();
            homeView.setChatCount(0);
        }
    }

    @Override
    public void getNotificationCount() {

        try {
            int chatCount = pref.getInt(AppConstants.PREFS.NOTI_COUNT, 0);
            homeView.setNotificationCount(chatCount);

            Call<ProfileResponseModel> call = apiInstance.getClientProfile(headerToken, headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""));
            call.enqueue(new Callback<ProfileResponseModel>() {
                @Override
                public void onResponse(Call<ProfileResponseModel> call, Response<ProfileResponseModel> response) {

                    try {
                        if (response != null && response.isSuccessful()) {
                            ProfileResponseModel dataEntity = response.body();
                            homeView.setNotificationCount(dataEntity.getData().get(0).getNotificationBadge());
                        } else {
                            homeView.setNotificationCount(0);
                        }
                    } catch (Exception e) {
                        homeView.setNotificationCount(0);
                    }
                }

                @Override
                public void onFailure(Call<ProfileResponseModel> call, Throwable t) {
                    homeView.setNotificationCount(0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            homeView.setNotificationCount(0);
        }
    }

    @Override
    public void fetchMicroMarkets() {
        final BrongoClientApplication app = (BrongoClientApplication) context.getApplicationContext();
        try {
            Call<FetchMicroMarketResponse> call = apiInstance.getMicroMarketDetails(headerToken, headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""));
            call.enqueue(new Callback<FetchMicroMarketResponse>() {
                @Override
                public void onResponse(Call<FetchMicroMarketResponse> call, Response<FetchMicroMarketResponse> response) {
                    try {
                        if (response != null && response.isSuccessful()) {
                            FetchMicroMarketResponse dataEntity = response.body();
                            app.setMicroMarketResponse(dataEntity);
                            app.setSuccess(true);
                        } else {
                            app.setSuccess(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        app.setSuccess(false);
                    }
                }

                @Override
                public void onFailure(Call<FetchMicroMarketResponse> call, Throwable t) {
                    app.setSuccess(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            app.setSuccess(false);

        }
    }
}
