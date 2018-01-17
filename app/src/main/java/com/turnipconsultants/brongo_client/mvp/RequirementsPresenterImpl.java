package com.turnipconsultants.brongo_client.mvp;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.models.AcceptedBrokersInputModel;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.AcceptedBrokersResponseModel;
import com.turnipconsultants.brongo_client.responseModels.AllRequirementsResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohit on 15-01-2018.
 */

public class RequirementsPresenterImpl implements RequirementsPresenter {
    private RequirementsView view;
    private Context context;

    public RequirementsPresenterImpl(RequirementsView view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void getAllRequirements(final String accessToken, final String platform, final String deviceId, final AcceptedBrokersInputModel model) {
        view.onProgress(true);
        RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
        Call<AllRequirementsResponse> call = apiInstance.getAllRequirementDetails(accessToken, platform, deviceId, model);
        call.enqueue(new Callback<AllRequirementsResponse>() {
            @Override
            public void onResponse(Call<AllRequirementsResponse> call, Response<AllRequirementsResponse> response) {
                view.onProgress(false);
                if (response != null && response.isSuccessful()) {
                    view.onSuccessCall(response.body().getData().get(0));
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                            new Utils().getTokenRefresh(context, new TokenInputModel(platform, deviceId, model.getClientMobileNo()));
                            getAllRequirements(accessToken, platform, deviceId, model);
                        } else {
                            view.onFailures("Please try again");
                        }
                        Log.e("error", response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AllRequirementsResponse> call, Throwable t) {
                view.onProgress(false);
                view.onFailures(t.getMessage());
            }
        });
    }
}
