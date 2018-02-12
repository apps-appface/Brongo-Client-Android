package com.turnipconsultants.brongo_client.architecture;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.turnipconsultants.brongo_client.models.BuyAProperty.BuyPropertyModel;
import com.turnipconsultants.brongo_client.responseModels.PropertyTransactionResponseModel;

/**
 * Created by mohit on 24-01-2018.
 */

public class BuyLandResViewModel extends ViewModel {
    private MutableLiveData<PropertyTransactionResponseModel> apiResponseData;
    private BuyLandResRepository repository=BuyLandResRepository.getInstance(getApiResponseData());

    public MutableLiveData<PropertyTransactionResponseModel> getApiResponseData() {
        if (apiResponseData == null) {
            apiResponseData = new MutableLiveData<>();
        }
        return apiResponseData;
    }

    public void getCallToServer(Context context, String accessToken, String platform, String deviceId, BuyPropertyModel model) {
        apiResponseData= repository.postBuyLandResidential(context, accessToken, platform, deviceId, model);
    }
}
