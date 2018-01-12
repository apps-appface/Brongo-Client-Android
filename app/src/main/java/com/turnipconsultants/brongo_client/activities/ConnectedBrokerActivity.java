package com.turnipconsultants.brongo_client.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.bumptech.glide.Glide;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.models.AcceptedBrokersInputModel;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.responseModels.BrokerRatingResponseModel;
import com.turnipconsultants.brongo_client.responseModels.KnowlarityApiResponseModel;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohit on 06-10-2017.
 */

public class ConnectedBrokerActivity extends AppCompatActivity implements View.OnClickListener, NoInternetTryConnectListener {
    private Context context;
    private SharedPreferences pref;
    private String brokerMobileNo = "", brokerFirstName, brokerImageStr = "";
    private String headerToken, headerDeviceId, headerPlatform;
    private CircleImageView brokerImage, bChat, scheduleMeeting;
    private ImageView bCall, requestCall;
    private TextView brokerName, rating, userReviews, closedDeals, openDeals, viewAllReviews, place;
    private RatingBar ratingStars;
    private RelativeLayout backToHome;
    private TASK selectedTask;

    private enum TASK {
        GET_CONNECTED, GET_CALL,
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_broker);
        initializeViews();
        brokerMobileNo = getIntent().getStringExtra("brokerMobileNo");
        brokerImageStr = getIntent().getStringExtra("brokerImage");
        place.setText(getIntent().getStringExtra("place") + ",Bangalore");
        bChat.setOnClickListener(this);
        bCall.setOnClickListener(this);
        backToHome.setOnClickListener(this);
        if (!brokerMobileNo.isEmpty()) {
            if (checkPermissionAllowed()) {
                getConnectedBrokerRating();
            }
        }
    }

    private void initializeViews() {
        context = this;
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        brokerImage = (CircleImageView) findViewById(R.id.brokerImage);
        bChat = (CircleImageView) findViewById(R.id.chat);
        bCall = (ImageView) findViewById(R.id.call);
        requestCall = (ImageView) findViewById(R.id.call2);
        scheduleMeeting = (CircleImageView) findViewById(R.id.schedule);
        brokerName = (TextView) findViewById(R.id.brokerName);
        rating = (TextView) findViewById(R.id.rating);
        ratingStars = (RatingBar) findViewById(R.id.ratingBar);
        userReviews = (TextView) findViewById(R.id.userdeals);
        closedDeals = (TextView) findViewById(R.id.closedDeals);
        openDeals = (TextView) findViewById(R.id.opendeals);
        viewAllReviews = (TextView) findViewById(R.id.viewall);
        backToHome = (RelativeLayout) findViewById(R.id.backRl);
        place = (TextView) findViewById(R.id.place);
    }

    private void getConnectedBrokerRating() {
        selectedTask = TASK.GET_CONNECTED;
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        if (InternetConnection.isNetworkAvailable(context)) {
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<BrokerRatingResponseModel> call = apiInstance.getBrokerRating(headerToken, headerPlatform, headerDeviceId, getStringValues());
            call.enqueue(new Callback<BrokerRatingResponseModel>() {
                @Override
                public void onResponse(Call<BrokerRatingResponseModel> call, Response<BrokerRatingResponseModel> response) {
                    AllUtils.LoaderUtils.dismissLoader();
                    if (response != null && response.isSuccessful()) {
                        BrokerRatingResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {
                            List<BrokerRatingResponseModel.DataEntity> data = responseModel.getData();
                            brokerFirstName = data.get(0).getBrokerName();
                            Glide.with(context)
                                    .load(brokerImageStr)
                                    .apply(BrongoClientApplication.getRequestOption(false))
                                    .into(brokerImage);
                            rating.setText(data.get(0).getRating() + "");
                            userReviews.setText(data.get(0).getReviewsCount() + "");
                            closedDeals.setText(data.get(0).getClosedLeads() + "");
                            openDeals.setText(data.get(0).getOpenDeals() + "");
                            brokerName.setText(brokerFirstName);

                        } else {
                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                getConnectedBrokerRating();
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
                public void onFailure(Call<BrokerRatingResponseModel> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    AllUtils.LoaderUtils.dismissLoader();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, this);
        }
    }

    private AcceptedBrokersInputModel getStringValues() {
        AcceptedBrokersInputModel model = new AcceptedBrokersInputModel();
        model.setClientMobileNo(pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""));
        model.setBrokerMobileNo(brokerMobileNo);
        model.setLimit(2);
        return model;
    }

    private void getKnowalarityCall() {
        selectedTask = TASK.GET_CALL;
        if (InternetConnection.isNetworkAvailable(context)) {
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<KnowlarityApiResponseModel> call = apiInstance.getKnowlarityCall("+91" + pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""), "+91" + brokerMobileNo);
            call.enqueue(new Callback<KnowlarityApiResponseModel>() {
                @Override
                public void onResponse(Call<KnowlarityApiResponseModel> call, Response<KnowlarityApiResponseModel> response) {
                    AllUtils.LoaderUtils.dismissLoader();
                    if (response != null && response.isSuccessful()) {
                        KnowlarityApiResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {

                        } else {
                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Please Try Again After Sometime", Toast.LENGTH_SHORT).show();
                        try {
                            Log.e("error", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<KnowlarityApiResponseModel> call, Throwable t) {
                    AllUtils.LoaderUtils.dismissLoader();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, this);
        }
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
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.chat:
                Intent intent = new Intent(this, ConversationActivity.class);
                intent.putExtra(ConversationUIService.USER_ID, brokerMobileNo);
                intent.putExtra(ConversationUIService.DISPLAY_NAME, brokerFirstName); //put it for displaying the title.
                intent.putExtra(ConversationUIService.TAKE_ORDER, true); //Skip chatRL list for showing on back press
                startActivity(intent);
                break;
            case R.id.call:
                getKnowalarityCall();
                break;
            case R.id.backRl:
                Intent intent2 = new Intent(context, HomeActivity.class);
//                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                finish();
                break;
        }
    }

    @Override
    public void onTryReconnect() {
        switch (selectedTask) {
            case GET_CONNECTED: {
                getConnectedBrokerRating();
                break;
            }
            case GET_CALL: {
                getKnowalarityCall();
                break;
            }

        }
    }
}
