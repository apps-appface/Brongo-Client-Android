package com.turnipconsultants.brongo_client.architecture;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Payu.PayuConstants;
import com.payu.payuui.Activity.PayUBaseActivity;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.fragments.ConnectedBrokersFragment;
import com.turnipconsultants.brongo_client.models.GooglePlacesModel;
import com.turnipconsultants.brongo_client.models.PaymentHashModel;
import com.turnipconsultants.brongo_client.models.PayuConfigModel;
import com.turnipconsultants.brongo_client.models.SellYourProperty.PaymentHashResponseModel;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;
import com.turnipconsultants.brongo_client.responseModels.PaymentSubscriptionResponse;
import com.turnipconsultants.brongo_client.responseModels.SecondLandingResponse;

import org.json.JSONObject;

import java.util.List;

import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohit on 06-02-2018.
 */

public class SubscriptionRepository {

    public static void getMyPlan(final Context context, final String headerDeviceId, final String headerPlatform, final String headerToken, final String mobileNo, final MutableLiveData<PaymentSubscriptionResponse> dataValue) {

        RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
        Call<PaymentSubscriptionResponse> call = apiInstance.getMyPlanSubscription(headerToken, headerPlatform, headerDeviceId, mobileNo);
        call.enqueue(new Callback<PaymentSubscriptionResponse>() {
            @Override
            public void onResponse(Call<PaymentSubscriptionResponse> call, Response<PaymentSubscriptionResponse> response) {
                if (response != null && response.isSuccessful()) {
                    PaymentSubscriptionResponse responseModel = response.body();
                    if (responseModel.getStatusCode() == 200) {
                        dataValue.setValue(responseModel);
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                            new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, mobileNo));
                            getMyPlan(context, headerDeviceId, headerPlatform, headerToken, mobileNo, dataValue);
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
            public void onFailure(Call<PaymentSubscriptionResponse> call, Throwable t) {
                dataValue.setValue(null);
                Log.e("SubRepo", t.getMessage());
            }
        });
    }


    public static void MakePayment(SharedPreferences pref, final String headerDeviceId, final String headerPlatform, final String headerToken, final String subscrId, final double amountTopay, final MutableLiveData<PayuConfigModel> liveData) {

        final String userMobileNo = pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "");
        final String firstName = pref.getString(AppConstants.PREFS.USER_FIRST_NAME, "");
        final String email = pref.getString(AppConstants.PREFS.USER_EMAIL, "");

        PaymentHashModel paymentHashModel = new PaymentHashModel();
        paymentHashModel.setAmount(String.valueOf(amountTopay));
        paymentHashModel.setFirstname(firstName);
        paymentHashModel.setProductInfo("Brongo_Client");
        paymentHashModel.setEmail(email);
        paymentHashModel.setMobileNo(userMobileNo);
        paymentHashModel.setPaymentId(subscrId);
        paymentHashModel.setBrokerMobileNo("");
        paymentHashModel.setPaymentMode("SUBSCRIPTION");
//        paymentHashModel.setIsDevelopment(1);
        paymentHashModel.setUserType("CLIENT");

        RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
        Call<PaymentHashResponseModel> call = apiInstance.getPaymentHash(headerToken, headerPlatform, headerDeviceId, paymentHashModel);
        call.enqueue(new Callback<PaymentHashResponseModel>() {
            @Override
            public void onResponse(Call<PaymentHashResponseModel> call, Response<PaymentHashResponseModel> response) {
                AllUtils.LoaderUtils.dismissLoader();
                if (response != null && response.body() != null && response.isSuccessful()) {
                    PaymentHashResponseModel hashResponseModel = response.body();
                    List<PaymentHashResponseModel.Data> data = hashResponseModel.getData();

                    PaymentParams paymentParams = new PaymentParams();
//                    paymentParams.setKey("gtKFFx");                  //DEVELOPMENT
                    paymentParams.setKey("FHOPnO");               //PRODUCTION
                    paymentParams.setTxnId(data.get(0).getTxnid());

                    paymentParams.setAmount(String.valueOf(amountTopay));
                    paymentParams.setProductInfo("Brongo_Client");
                    paymentParams.setFirstName(firstName);
                    //paymentParams.setVpa(data.get(0).getVapsHash());
                    paymentParams.setEmail(email);
                    paymentParams.setUdf1("");
                    paymentParams.setUdf2(subscrId);
                    paymentParams.setUdf3("SUBSCRIPTION");
                    paymentParams.setUdf4("CLIENT");
                    paymentParams.setUdf5("");
                    paymentParams.setPhone(userMobileNo);

                    paymentParams.setSurl(RetrofitBuilders.getBaseUrl() + "/paymentStatus");
                    paymentParams.setFurl(RetrofitBuilders.getBaseUrl() + "/paymentStatus");

                    PayuHashes payuHashes = new PayuHashes();
                    payuHashes.setPaymentHash(data.get(0).getSha512());
                    payuHashes.setVasForMobileSdkHash(data.get(0).getVapsHash());                       //
                    payuHashes.setPaymentRelatedDetailsForMobileSdkHash(data.get(0).getPaymentHash());              //
                    //payuHashes.setPaymentRelatedDetailsForMobileSdkHash(data.get(0).getPayment_related_details_for_mobile_sdk_hash());              //


                    paymentParams.setHash(payuHashes.getPaymentHash());
                    paymentParams.setUserCredentials(userMobileNo + ":Brongo_Client");

                    PayuConfig payuConfig = new PayuConfig();
//                    payuConfig.setEnvironment(PayuConstants.STAGING_ENV);
                      payuConfig.setEnvironment(PayuConstants.PRODUCTION_ENV);

                    PayuConfigModel payuConfigModel = new PayuConfigModel();
                    payuConfigModel.setPaymentParams(paymentParams);
                    payuConfigModel.setPayuConfig(payuConfig);
                    payuConfigModel.setPayuHashes(payuHashes);
                    liveData.setValue(payuConfigModel);

                } else {

                }
            }

            @Override
            public void onFailure(Call<PaymentHashResponseModel> call, Throwable t) {
                liveData.setValue(null);
            }
        });
    }
}
