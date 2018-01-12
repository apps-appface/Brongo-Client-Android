package com.turnipconsultants.brongo_client.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.adapters.BrokerReviewPagerAdapter;
import com.turnipconsultants.brongo_client.models.AcceptedBrokersInputModel;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.responseModels.AcceptedBrokersResponseModel;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohit on 22-09-2017.
 */

public class BrokerReviewActivity extends AppCompatActivity implements BrokerReviewPagerAdapter.ViewPagerOnItemClick, NoInternetTryConnectListener {

    private ViewPager viewPager;
    private Context context;
    private BrokerReviewPagerAdapter brokerReviewPagerAdapter;
    private ArrayList<AcceptedBrokersResponseModel.Data> arrayList;
    private String headerToken, headerDeviceId, headerPlatform, brokerImage;
    private SharedPreferences pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_review);
        initializeViews();


        /*brokerReviewPagerAdapter = new BrokerReviewPagerAdapter(context, viewPager, arrayList);
        viewPager.setAdapter(brokerReviewPagerAdapter);
        viewPager.setClipToPadding(false);

        viewPager.setPageMargin(20);
        viewPager.setOnPageChangeListener(brokerReviewPagerAdapter);
        viewPager.setCurrentItem(1, false);
        viewPager.setPadding(80, 0, 80, 0);*/

        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                page.setAlpha(normalizedposition + 0.5f);
                page.setScaleY(normalizedposition / 2 + 0.5f);

            }
        });


        if (checkPermissionAllowed())
            getBrokerDetailsReviews();
    }

    private void initializeViews() {
        context = this;
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        arrayList = new ArrayList<>();
    }

    private void getBrokerDetailsReviews() {
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        if (InternetConnection.isNetworkAvailable(context)) {
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<AcceptedBrokersResponseModel> call = apiInstance.getAcceptedBrokerDetails(headerToken, headerPlatform, headerDeviceId, getStringValues());
            call.enqueue(new Callback<AcceptedBrokersResponseModel>() {
                @Override
                public void onResponse(Call<AcceptedBrokersResponseModel> call, Response<AcceptedBrokersResponseModel> response) {
                    if (response != null && response.isSuccessful()) {
                        AllUtils.LoaderUtils.dismissLoader();
                        AcceptedBrokersResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {
                            List<AcceptedBrokersResponseModel.Data> data = responseModel.getData();
                            arrayList.add(0, data.get(data.size() - 1));
                            arrayList.addAll(data);
                            arrayList.add(data.get(0));
                            brokerReviewPagerAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AllUtils.LoaderUtils.dismissLoader();
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                getBrokerDetailsReviews();
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
                public void onFailure(Call<AcceptedBrokersResponseModel> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context,this);
        }
    }

    private AcceptedBrokersInputModel getStringValues() {
        AcceptedBrokersInputModel model = new AcceptedBrokersInputModel();
        model.setClientMobileNo(pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""));
        model.setPostingType(pref.getString("postingType", ""));
        model.setPropertyId(pref.getString("propertyId", ""));
        return model;
    }

    private void getConnectedToBroker(final String brokerMobile) {
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        if (InternetConnection.isNetworkAvailable(context)) {
            AcceptedBrokersInputModel model = getStringValues();
            model.setBrokerMobileNo(brokerMobile);
            model.setUserId("");
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<GeneralApiResponseModel> call = apiInstance.getConnectedBroker(headerToken, headerPlatform, headerDeviceId, model);
            call.enqueue(new Callback<GeneralApiResponseModel>() {
                @Override
                public void onResponse(Call<GeneralApiResponseModel> call, Response<GeneralApiResponseModel> response) {
                    AllUtils.LoaderUtils.dismissLoader();
                    if (response != null && response.isSuccessful()) {
                        GeneralApiResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {
                            if (responseModel.getMessage().equals("Client And Broker Connection Is Established")) {
                                pref.edit().putBoolean(AppConstants.PREFS.IS_SECOND, true).commit();
                                Intent intent = new Intent(context, ConnectedBrokerActivity.class);
                                intent.putExtra("brokerMobileNo", brokerMobile);
                                intent.putExtra("brokerImage", brokerImage);
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                getConnectedToBroker(brokerMobile);
                            } else {
                                Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            Log.e("error", response.errorBody().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GeneralApiResponseModel> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    AllUtils.LoaderUtils.dismissLoader();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context,this);
        }
    }

    private void showPrivacyPopUp(final String brokerMobile) {
        final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
        dialogBlock.setContentView(R.layout.privacy_pop_dialog);
        dialogBlock.setCanceledOnTouchOutside(false);
        dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        ImageView dialogButtonCancel = dialogBlock.findViewById(R.id.cancel);
        Button okbtn = dialogBlock.findViewById(R.id.okbtn);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBlock.dismiss();
            }
        });

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBlock.dismiss();
                if (checkPermissionAllowed())
                    getConnectedToBroker(brokerMobile);
            }
        });

        dialogBlock.show();
    }

    private boolean checkPermissionAllowed() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                }
                break;

            default:
                break;
        }
    }

    @Override
    public void itemClick(int pos, String mobileNo) {
        showPrivacyPopUp(mobileNo);
    }

    @Override
    public void onTryReconnect() {
        getBrokerDetailsReviews();
    }
}
