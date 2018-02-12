package com.turnipconsultants.brongo_client.architecture;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.models.UnsubscribeModel;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;
import com.turnipconsultants.brongo_client.responseModels.PaymentSubscriptionResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohit on 12-02-2018.
 */

public class HelpRepository {
    public static void unsubscribe(final Context context, final String headerDeviceId, final String headerPlatform, final String headerToken, final String mobileNo, final UnsubscribeModel model, final MutableLiveData<GeneralApiResponseModel> dataValue) {

        RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
        Call<GeneralApiResponseModel> call = apiInstance.unsubscribeCall(headerToken, headerPlatform, headerDeviceId, model);
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
                            new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, mobileNo));
                            unsubscribe(context, headerDeviceId, headerPlatform, headerToken, mobileNo, model, dataValue);
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
                dataValue.setValue(null);
                Log.e("SubRepo", t.getMessage());
            }
        });
    }
}
