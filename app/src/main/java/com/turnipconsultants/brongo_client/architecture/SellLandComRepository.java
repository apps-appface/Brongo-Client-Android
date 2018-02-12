package com.turnipconsultants.brongo_client.architecture;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.turnipconsultants.brongo_client.fragments.SellYourProperty.SELL_Your_ResidentialFragment;
import com.turnipconsultants.brongo_client.models.BuyAProperty.BuyPropertyModel;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.PropertyTransactionResponseModel;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohit on 24-01-2018.
 */

public class SellLandComRepository {
    private static SellLandComRepository landRepository;
    private static MutableLiveData<PropertyTransactionResponseModel> data;
    private String propertyId = "";

    public static SellLandComRepository getInstance(MutableLiveData<PropertyTransactionResponseModel> dataValue) {
        if (landRepository == null) {
            landRepository = new SellLandComRepository();
        }
        data = dataValue;
        return landRepository;
    }


    public MutableLiveData<PropertyTransactionResponseModel> postSellLandCommercial(final Context context, final String accessToken, final String platform, final String deviceId, final String clientMob, final Map<String, RequestBody> map, final MultipartBody.Part img1, final MultipartBody.Part img2, final MultipartBody.Part img3) {
//        final MutableLiveData<PropertyTransactionResponseModel> data = new MutableLiveData<>();
        RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
        Call<PropertyTransactionResponseModel> call = apiInstance.getSellYourProperty(accessToken, platform, deviceId, map, img1, img2, img3);
        call.enqueue(new Callback<PropertyTransactionResponseModel>() {
            @Override
            public void onResponse(Call<PropertyTransactionResponseModel> call, Response<PropertyTransactionResponseModel> response) {
                if (response != null && response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                            new Utils().getTokenRefresh(context, new TokenInputModel(platform, deviceId, clientMob));
                            postSellLandCommercial(context, accessToken, platform, deviceId, clientMob, map, img1, img2, img3);
                        } else if (jObjError.getString("statusCode").equals("412")) {
                            PropertyTransactionResponseModel errorModel = new PropertyTransactionResponseModel();
                            errorModel.setStatusCode(412);
                            errorModel.setMessage(jObjError.getString("message"));
                            data.setValue(errorModel);
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
            public void onFailure(Call<PropertyTransactionResponseModel> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
