package com.turnipconsultants.brongo_client.activities;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.payu.india.Payu.PayuConstants;
import com.payu.payuui.Activity.PayUBaseActivity;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.Listener.RetryPaymentListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.adapters.PaymentSubscriptionAdapter;
import com.turnipconsultants.brongo_client.architecture.SubscriptionRepository;
import com.turnipconsultants.brongo_client.fragments.ConnectedBrokersFragment;
import com.turnipconsultants.brongo_client.models.PayuConfigModel;
import com.turnipconsultants.brongo_client.models.SubscriptionModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.CommissionDialogFactory;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;
import com.turnipconsultants.brongo_client.responseModels.PaymentSubscriptionResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class PaymentSubscriptionActivity extends AppCompatActivity implements RetryPaymentListener, PaymentSubscriptionAdapter.BuyClickListener {

    @BindView(R.id.subscription_recycler_view)
    RecyclerView subscriptionRV;

    @BindView(R.id.reset)
    TextView toolBarReset;

    @BindView(R.id.title)
    TextView toolBarTitle;

    @BindView(R.id.userPicCIV)
    CircleImageView userPicCIV;

    @BindView(R.id.userNameBTV)
    TextView userNameBTV;

    @BindView(R.id.userRatingTV)
    TextView userRatingTV;

    @BindView(R.id.userRB)
    RatingBar userRB;

    @BindView(R.id.line1)
    BrongoTextView line1;

    @BindView(R.id.condition1)
    BrongoTextView condition1;

    @BindView(R.id.condition2)
    BrongoTextView condition2;

    private ArrayList<SubscriptionModel> arrayList;
    private Context context;
    private Unbinder unbinder;
    private SharedPreferences pref;
    private String headerToken, headerDeviceId, headerPlatform, mobileNo;
    private PaymentSubscriptionAdapter adapter;
    private List<PaymentSubscriptionResponse.DataEntity.SubPlans> subPlansList;
    int buyPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_and_subscription);
        unbinder = ButterKnife.bind(this);
        initPage();
        getApiResponseModel().observeForever(modelObserver);
        getPayuModel().observeForever(payuObserver);
        toolBarReset.setVisibility(View.GONE);
        toolBarTitle.setText("PAYMENT & SUBSCRIPTION");

    }

    @Override
    protected void onStart() {
        super.onStart();
        callToServer();
    }

    private void initPage() {
        context = this;
        pref = this.getSharedPreferences(AppConstants.PREF_NAME, MODE_PRIVATE);
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        mobileNo = pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "");
        arrayList = new ArrayList<>();
        adapter = new PaymentSubscriptionAdapter(context, arrayList);
        subscriptionRV.setAdapter(adapter);
    }

    private void callToServer() {
        AllUtils.LoaderUtils.showLoader(context);
        if (InternetConnection.isNetworkAvailable(context)) {
            SubscriptionRepository.getMyPlan(context, headerDeviceId, headerPlatform, headerToken, mobileNo, getApiResponseModel());
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, (NoInternetTryConnectListener) context);
        }
    }

    private void loadProfileImage(PaymentSubscriptionResponse response) {
        try {
            String profilePicFilePath = pref.getString(AppConstants.PREFS.USER_PROFILE_PIC_FILE, "");
            if (!profilePicFilePath.isEmpty() && new File(profilePicFilePath).exists()) {
                if (userPicCIV != null) {
                    File file = new File(profilePicFilePath);
                    Bitmap profilePic = BitmapFactory.decodeFile(file.getAbsolutePath());
                    userPicCIV.setImageBitmap(profilePic);
                }
            }

            userNameBTV.setText(pref.getString(AppConstants.PREFS.USER_FIRST_NAME, "") + " " + pref.getString(AppConstants.PREFS.USER_LAST_NAME, ""));
            userRatingTV.setText(pref.getFloat(AppConstants.PREFS.USER_RATING, 0.0f) + "");
            userRB.setRating(pref.getFloat(AppConstants.PREFS.USER_RATING, 0.0f));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHeaderValues(PaymentSubscriptionResponse.DataEntity dataEntity) {
        try {
            Glide.with(context)
                    .load(dataEntity.getImage())
                    .apply(BrongoClientApplication.getRequestOption(true))
                    .into(userPicCIV);
            userNameBTV.setText(dataEntity.getName());
            userRatingTV.setText(dataEntity.getRating() + "");
            userRB.setRating(dataEntity.getRating());
            condition1.setText(" " + dataEntity.getConditions().get(0));
            condition2.setText(" " + dataEntity.getConditions().get(1));
            line1.setText("You have " + dataEntity.getRequestsLeft() + " free request left");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDummyList(PaymentSubscriptionResponse.DataEntity dataEntity) {
        arrayList.clear();
        List<PaymentSubscriptionResponse.DataEntity.SubPlans> subPlansList = dataEntity.getSubPlans();
        for (int i = 0; i < subPlansList.size(); i++) {
            SubscriptionModel subscriptionModel = new SubscriptionModel();
            if (subPlansList.get(i).getOffer() == 0) {
                subscriptionModel.setLine1(subPlansList.get(i).getCondition());
                subscriptionModel.setLine2(getString(R.string.rupee) + subPlansList.get(i).getAmount());
                subscriptionModel.setType(AppConstants.PAYMENT_ROW_TYPE.SINGLE_TYPE);
            } else {
                subscriptionModel.setLine1(subPlansList.get(i).getCondition());
                subscriptionModel.setLine21(getString(R.string.rupee) + subPlansList.get(i).getAmount());
                subscriptionModel.setLine22(getString(R.string.rupee) + subPlansList.get(i).getAmoutToPay());
                subscriptionModel.setLine31("Save: " + subPlansList.get(i).getOffer() + "%");
                subscriptionModel.setLine32("Price per requirement: " + getString(R.string.rupee) + subPlansList.get(i).getPriceForReq());
                subscriptionModel.setType(AppConstants.PAYMENT_ROW_TYPE.MIX_TYPE);
            }
            arrayList.add(subscriptionModel);
        }
        adapter.notifyDataSetChanged();
    }

    public void endActivity(View v) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
        getApiResponseModel().removeObserver(modelObserver);
        getPayuModel().removeObserver(payuObserver);
    }

    private MutableLiveData<PaymentSubscriptionResponse> apiResponseModel;

    public MutableLiveData<PaymentSubscriptionResponse> getApiResponseModel() {
        if (apiResponseModel == null) {
            apiResponseModel = new MutableLiveData<>();
        }
        return apiResponseModel;
    }

    final Observer<PaymentSubscriptionResponse> modelObserver = new Observer<PaymentSubscriptionResponse>() {
        @Override
        public void onChanged(@Nullable final PaymentSubscriptionResponse newValue) {
            if (newValue != null) {
                AllUtils.LoaderUtils.dismissLoader();
                if (newValue.getStatusCode() == 200) {
                    loadProfileImage(newValue);
                    setHeaderValues(newValue.getData().get(0));
                    getDummyList(newValue.getData().get(0));
                    subPlansList = newValue.getData().get(0).getSubPlans();
                } else {
                    Toast.makeText(context, newValue.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                AllUtils.LoaderUtils.dismissLoader();
                Toast.makeText(context, "Please Try Again", Toast.LENGTH_LONG).show();
            }
        }
    };

    private MutableLiveData<PayuConfigModel> payuModel;

    public MutableLiveData<PayuConfigModel> getPayuModel() {
        if (payuModel == null) {
            payuModel = new MutableLiveData<>();
        }
        return payuModel;
    }

    final Observer<PayuConfigModel> payuObserver = new Observer<PayuConfigModel>() {
        @Override
        public void onChanged(@Nullable final PayuConfigModel newValue) {
            if (newValue != null) {
                AllUtils.LoaderUtils.dismissLoader();
                Intent intent = new Intent(context, PayUBaseActivity.class);
                intent.putExtra(PayuConstants.PAYU_CONFIG, newValue.getPayuConfig());
                intent.putExtra(PayuConstants.PAYMENT_PARAMS, newValue.getPaymentParams());
                intent.putExtra(PayuConstants.PAYU_HASHES, newValue.getPayuHashes());
                startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);

            } else {
                AllUtils.LoaderUtils.dismissLoader();
                AllUtils.DialogUtils.PaymentErrorDialog(context, PaymentSubscriptionActivity.this);
            }
        }
    };

    @Override
    public void RetryPayment() {
        callMakePayment(buyPosition);
    }

    @Override
    public void paymentSuccess() {
        callToServer();
    }

    @Override
    public void onBuyClick(int position) {
        buyPosition = position;
        callMakePayment(position);
    }

    private void callMakePayment(int position) {
        AllUtils.LoaderUtils.showLoader(context);
        if (InternetConnection.isNetworkAvailable(context)) {
            SubscriptionRepository.MakePayment(pref, headerDeviceId, headerPlatform, headerToken, subPlansList.get(position).getSubId(),subPlansList.get(position).getAmoutToPay(), getPayuModel());
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, (NoInternetTryConnectListener) context);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            if (data != null) {

                /**
                 * Here, data.getStringExtra("payu_response") ---> Implicit response sent by PayU
                 * data.getStringExtra("result") ---> Response received from merchant's Surl/Furl
                 *
                 * PayU sends the same response to merchant server and in app. In response check the value of key "status"
                 * for identifying status of transaction. There are two possible status like, success or failure
                 * */
                try {
                    String result = new JSONObject(data.getStringExtra("payu_response")).optString("status");
                    if (result != null) {
                        switch (result) {
                            case "success":
                                AllUtils.DialogUtils.PaymentSuccessDialog(context, this);
                                break;
                            case "failure":
                                AllUtils.DialogUtils.PaymentFailedDialog(context, this);
                                break;
                        }
                    }
                } catch (JSONException e) {
                    AllUtils.DialogUtils.PaymentErrorDialog(context, this);
                    e.printStackTrace();
                }

            } else {
                AllUtils.DialogUtils.PaymentErrorDialog(context, this);
            }
        } else {
            AllUtils.DialogUtils.PaymentErrorDialog(context, this);
        }
    }
}
