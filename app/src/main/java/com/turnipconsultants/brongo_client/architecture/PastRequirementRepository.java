package com.turnipconsultants.brongo_client.architecture;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.models.UnsubscribeModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;
import com.turnipconsultants.brongo_client.responseModels.PastRequirementResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohit on 12-02-2018.
 */

public class PastRequirementRepository {

    public static void LoadBrokersPastReq(final Context context, final String headerDeviceId, final String headerPlatform, final String headerToken, final String mobileNo, final String dealType, final MutableLiveData<PastRequirementResponse> dataValue) {
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<PastRequirementResponse> call = apiInstance.getPastRequirementData(headerToken, headerPlatform, headerDeviceId, mobileNo, dealType);
            call.enqueue(new Callback<PastRequirementResponse>() {
                @Override
                public void onResponse(Call<PastRequirementResponse> call, Response<PastRequirementResponse> response) {
                    try {
                        if (response != null && response.isSuccessful()) {
                            PastRequirementResponse responseModel = response.body();
                            if (responseModel.getStatusCode() == 200) {
                                dataValue.setValue(responseModel);
                            }
                        } else {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, mobileNo));
                                LoadBrokersPastReq(context,headerDeviceId,headerPlatform,headerToken,mobileNo,dealType,dataValue);
                            }
                            Log.e("error", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PastRequirementResponse> call, Throwable t) {
                    dataValue.setValue(null);
                }
            });
    }
}
