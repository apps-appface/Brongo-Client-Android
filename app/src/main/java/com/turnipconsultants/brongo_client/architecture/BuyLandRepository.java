package com.turnipconsultants.brongo_client.architecture;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.applozic.mobicomkit.api.account.user.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.turnipconsultants.brongo_client.models.AcceptedBrokersInputModel;
import com.turnipconsultants.brongo_client.models.BuyAProperty.BuyPropertyModel;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.AllRequirementsResponse;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;
import com.turnipconsultants.brongo_client.responseModels.PropertyTransactionResponseModel;

import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mohit on 17-01-2018.
 */


public class BuyLandRepository {
    private static BuyLandRepository landRepository;
    private static MutableLiveData<PropertyTransactionResponseModel> data;
    private String propertyId = "";


    public static BuyLandRepository getInstance(MutableLiveData<PropertyTransactionResponseModel> dataValue) {
        if (landRepository == null) {
            landRepository = new BuyLandRepository();
        }
        data=dataValue;
        return landRepository;
    }

    @Inject
    public BuyLandRepository() {
    }

    public MutableLiveData<PropertyTransactionResponseModel> postBuyLandCommercial(final Context context, final String accessToken, final String platform, final String deviceId, final BuyPropertyModel model) {
//        final MutableLiveData<PropertyTransactionResponseModel> data = new MutableLiveData<>();
        RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
        Call<PropertyTransactionResponseModel> call = apiInstance.getBuyAProperty(accessToken, platform, deviceId, model);
        call.enqueue(new Callback<PropertyTransactionResponseModel>() {
            @Override
            public void onResponse(Call<PropertyTransactionResponseModel> call, Response<PropertyTransactionResponseModel> response) {
                if (response != null && response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                            new Utils().getTokenRefresh(context, new TokenInputModel(platform, deviceId, model.getClientMobileNo()));
                            postBuyLandCommercial(context, accessToken, platform, deviceId, model);
                        } else {

                        }
                        Log.e("error", response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PropertyTransactionResponseModel> call, Throwable t) {

            }
        });
        return data;
    }
}
