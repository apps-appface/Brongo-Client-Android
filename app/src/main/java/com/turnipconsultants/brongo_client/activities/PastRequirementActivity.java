package com.turnipconsultants.brongo_client.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.adapters.PastReqAdapter;
import com.turnipconsultants.brongo_client.fragments.ConnectedBrokersFragment;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.PastRequirementResponse;
import com.turnipconsultants.brongo_client.responseModels.SecondLandingResponseModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastRequirementActivity extends AppCompatActivity implements NoInternetTryConnectListener {

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    BrongoTextView title;

    @BindView(R.id.noDataBTV)
    BrongoTextView noDataBTV;

    @BindView(R.id.reset)
    BrongoTextView reset;

    @BindView(R.id.pastReqRV)
    RecyclerView pastReqRV;

    private ArrayList<PastRequirementResponse.Dropped> droppedArrayList;
    private PastReqAdapter pastReqAdapter;
    private Unbinder unbinder;
    private Context context;
    private String headerDeviceId, headerPlatform, headerToken;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_requirement);
        unbinder = ButterKnife.bind(this);
        initPage();
        LoadBrokers();

    }

    private void initPage() {
        context = this;
        prefs = getSharedPreferences(AppConstants.PREF_NAME, MODE_PRIVATE);
        title.setText("PAST REQUIREMENTS");
        reset.setVisibility(View.INVISIBLE);
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = prefs.getString("token", "");


        droppedArrayList = new ArrayList<>();
        pastReqAdapter = new PastReqAdapter(context,droppedArrayList);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        pastReqRV.setLayoutManager(manager);
        pastReqRV.setAdapter(pastReqAdapter);
    }


    private void LoadBrokers() {
        if (InternetConnection.isNetworkAvailable(context)) {
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<PastRequirementResponse> call = apiInstance.getPastRequirementData(headerToken, headerPlatform, headerDeviceId, prefs.getString(AppConstants.PREFS.USER_MOBILE_NO, ""), false);
            call.enqueue(new Callback<PastRequirementResponse>() {
                @Override
                public void onResponse(Call<PastRequirementResponse> call, Response<PastRequirementResponse> response) {

                    try {
                        AllUtils.LoaderUtils.dismissLoader();
                        if (response != null && response.isSuccessful()) {
                            PastRequirementResponse responseModel = response.body();
                            if (responseModel.getStatusCode() == 200) {
                                if (responseModel.getData().get(0).getDropped().size() == 0) {
                                    noDataBTV.setVisibility(View.VISIBLE);
                                    pastReqRV.setVisibility(View.GONE);
                                } else {
                                    noDataBTV.setVisibility(View.GONE);
                                    pastReqRV.setVisibility(View.VISIBLE);
                                    droppedArrayList.addAll(responseModel.getData().get(0).getDropped());
                                    pastReqAdapter.notifyDataSetChanged();
                                }

                            } else {
                                Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, prefs.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                LoadBrokers();
                            } else {
                                Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            Log.e("error", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void onFailure(Call<PastRequirementResponse> call, Throwable t) {
                    AllUtils.LoaderUtils.dismissLoader();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }


    public void endActivity(View v) {
        finish();
    }

    @Override
    public void onTryReconnect() {
        LoadBrokers();
    }
}