package com.turnipconsultants.brongo_client.architecture;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.turnipconsultants.brongo_client.models.BuyAProperty.BuyPropertyModel;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.PropertyTransactionResponseModel;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohit on 24-01-2018.
 */

public class BuyLandResRepository {

    private static BuyLandResRepository landRepository;
    private static MutableLiveData<PropertyTransactionResponseModel> data;


    public static BuyLandResRepository getInstance(MutableLiveData<PropertyTransactionResponseModel> dataValue) {
        if (landRepository == null) {
            landRepository = new BuyLandResRepository();
        }
        data=dataValue;
        return landRepository;
    }

    public MutableLiveData<PropertyTransactionResponseModel> postBuyLandResidential(final Context context, final String accessToken, final String platform, final String deviceId, final BuyPropertyModel model){
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
                            postBuyLandResidential(context, accessToken, platform, deviceId, model);
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
