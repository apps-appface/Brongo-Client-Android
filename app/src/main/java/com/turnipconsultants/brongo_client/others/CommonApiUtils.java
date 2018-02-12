package com.turnipconsultants.brongo_client.others;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.models.GooglePlacesModel;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.responseModels.FeedBackQueResponse;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;
import com.turnipconsultants.brongo_client.responseModels.PropertyTransactionResponseModel;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Pankaj on 08-11-2017.
 */

public class CommonApiUtils {

    public static void getFeedBackTags(final Context context, final String headerDeviceId, final String headerPlatform, final String headerToken, final GooglePlacesModel model, final MutableLiveData<GeneralApiResponseModel> dataValue) {
        if (InternetConnection.isNetworkAvailable(context)) {
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<GeneralApiResponseModel> call = apiInstance.postMicroMarketGoogle(headerToken, headerPlatform, headerDeviceId, model);
            call.enqueue(new Callback<GeneralApiResponseModel>() {
                @Override
                public void onResponse(Call<GeneralApiResponseModel> call, Response<GeneralApiResponseModel> response) {
                    if (response != null && response.isSuccessful()) {
                        GeneralApiResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {
                            dataValue.setValue(responseModel);
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, model.getMobileNo()));
                                getFeedBackTags(context, headerDeviceId, headerPlatform, headerToken, model, dataValue);
                            } else {
                                Toast.makeText(context, "Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                            Log.e("error", response.errorBody().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GeneralApiResponseModel> call, Throwable t) {
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Log.e("error", "No internet");

        }
    }
}
