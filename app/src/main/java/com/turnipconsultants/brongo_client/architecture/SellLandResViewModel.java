package com.turnipconsultants.brongo_client.architecture;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.turnipconsultants.brongo_client.responseModels.PropertyTransactionResponseModel;

import java.util.Map;

import lombok.Data;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by mohit on 17-01-2018.
 */

@Data
public class SellLandResViewModel extends ViewModel {
    private MutableLiveData<PropertyTransactionResponseModel> apiResponseData;
    private SellLandResRepository repository = SellLandResRepository.getInstance(getApiResponseData());

    public void makeSeverCall(Context context, String accessToken, String platform, String deviceId, String clientMobile, Map<String, RequestBody> partMap, MultipartBody.Part propertyPicture1, MultipartBody.Part propertyPicture2, MultipartBody.Part propertyPicture3) {
        apiResponseData = repository.postSellLandREsidential(context, accessToken, platform, deviceId, clientMobile, partMap, propertyPicture1, propertyPicture2, propertyPicture3);
    }

    public MutableLiveData<PropertyTransactionResponseModel> getApiResponseData() {
        if (apiResponseData == null) {
            apiResponseData = new MutableLiveData<>();
        }
        return apiResponseData;
    }
}
