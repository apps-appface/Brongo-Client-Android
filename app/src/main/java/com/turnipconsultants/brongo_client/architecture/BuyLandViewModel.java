package com.turnipconsultants.brongo_client.architecture;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.turnipconsultants.brongo_client.models.BuyAProperty.BuyPropertyModel;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;
import com.turnipconsultants.brongo_client.responseModels.PropertyTransactionResponseModel;

import org.json.JSONObject;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import lombok.Data;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohit on 17-01-2018.
 */

@Data
public class BuyLandViewModel extends ViewModel {
    private MutableLiveData<PropertyTransactionResponseModel> apiResponseModel;
     private BuyLandRepository repository = BuyLandRepository.getInstance(getApiResponseModel());

//   BuyLandRepository repository;

    public BuyLandViewModel() {
    }

    public BuyLandViewModel(BuyLandRepository repository) {
        this.repository = repository;
        Log.e("BUYLANDVIEWMODEL","repo"+repository);
    }

    public void getCallToServer(Context context, String accessToken, String platform, String deviceId, BuyPropertyModel model) {
        apiResponseModel = repository.postBuyLandCommercial(context, accessToken, platform, deviceId, model);
    }

    public MutableLiveData<PropertyTransactionResponseModel> getApiResponseModel() {
        if (apiResponseModel == null) {
            apiResponseModel = new MutableLiveData<>();
        }
        return apiResponseModel;
    }

}
