package com.turnipconsultants.brongo_client.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.bumptech.glide.Glide;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Payu.PayuConstants;
import com.payu.magicretry.MainActivity;
import com.payu.payuui.Activity.PayUBaseActivity;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.Listener.RetryPaymentListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.activities.FeedbackActivity;
import com.turnipconsultants.brongo_client.activities.RequirementsDetailsActivity;
import com.turnipconsultants.brongo_client.adapters.TimeLineUtil;
import com.turnipconsultants.brongo_client.models.AcceptedBrokersInputModel;
import com.turnipconsultants.brongo_client.models.DealStatusCustom.DealStatusCompleted;
import com.turnipconsultants.brongo_client.models.PaymentHashModel;
import com.turnipconsultants.brongo_client.models.SellYourProperty.PaymentHashResponseModel;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;
import com.turnipconsultants.brongo_client.responseModels.KnowlarityApiResponseModel;
import com.turnipconsultants.brongo_client.responseModels.QuestionsResponseModel;
import com.turnipconsultants.brongo_client.responseModels.SecondLandingResponse;
import com.turnipconsultants.brongo_client.responseModels.SecondLandingResponseModel;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConnectedBrokersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConnectedBrokersFragment extends Fragment implements RetryPaymentListener, NoInternetTryConnectListener {
    private static final String TAG = "ConnectedBrokersFragmen";

    private static final String BUY_RENT_MODEL = "object";
    private static final String WHICH_PAGE = "which_page";
    private int position;

    public Object object;
    private String whichPageToDisplay;
    private List<String> requireList;
    private TagAdapter<String> mAdapter;
    private BrokerListener brokerListener;
    private Unbinder unbinder;
    private SharedPreferences pref;
    private Context context;
    private EditText commentsET;
    private RadioButton radioBtn = null, radioBtn2 = null;
    private ArrayList<QuestionsResponseModel.DataEntity> questionsList;
    private String headerToken, headerDeviceId, headerPlatform, clientMobileNo;
    private TASK selectedTask;

    private enum TASK {
        GET_CALL,
        CALLBACK,
        GET_QUESTIONS,
        DROP_DEAL,
        CHALLENGE_STATUS,
        MAKE_PAYMENT,
    }

    @BindView(R.id.title)
    TextView badge;

    @BindView(R.id.circImage)
    CircleImageView brokerImage;

    @BindView(R.id.brokerName)
    TextView brokerName;

    @BindView(R.id.property_Id)
    BrongoTextView propertyId;

    @BindView(R.id.rating)
    TextView rating;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.user_reviews)
    TextView reviews;

    @BindView(R.id.requirement_flowlayout)
    TagFlowLayout tagFlowLayout;

    @BindView(R.id.challengeStatus)
    TextView challengeStatus;

    @BindView(R.id.locationTv)
    TextView projectLocation;

    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.callIM)
    ImageView call;

    @BindView(R.id.chatIV)
    ImageView chat;

    @BindView(R.id.call_back_IV)
    ImageView callBack;

    @BindView(R.id.schedule_button_BTN)
    LinearLayout scheduleMeetingBTN;

    @BindView(R.id.dealStatusLL)
    LinearLayout dealStatusList;

    @BindView(R.id.get_connected_LL)
    LinearLayout getConnectedLL;

    @BindView(R.id.second_time_landing_LL)
    LinearLayout secondTimeLandingLL;

    @BindView(R.id.deal_closedRL)
    LinearLayout dealClosedRL;

    @BindView(R.id.deal_closed_offlineRL)
    LinearLayout dealClosedOfflineRL;

    @BindView(R.id.dropDeal)
    LinearLayout dropDeal;

    @BindView(R.id.make_payment_BTN)
    Button paymentBTN;

    @BindView(R.id.detailstv)
    BrongoTextView tempDetails;

    @BindView(R.id.give_feedback_BTN)
    Button feedbackBTN;

    @BindView(R.id.payoffline)
    BrongoTextView payoffline;

    @BindView(R.id.noBtn)
    Button noBtn;

    @BindView(R.id.yesBtn)
    Button yesBtn;

    @BindView(R.id.message)
    BrongoTextView dealClosedMessage;

    @BindView(R.id.dealClosedCard)
    CardView dealClosedCard;

    @BindView(R.id.card_deal_status)
    CardView cardDealStatus;

    @BindView(R.id.showMap)
    BrongoTextView showMap;

    private DecimalFormat df = new DecimalFormat("#.##");

    public ConnectedBrokersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1    Parameter 1.
     * @param whichPage Parameter 2.
     * @return A new instance of fragment ConnectedBrokersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConnectedBrokersFragment newInstance(SecondLandingResponse.DataEntity param1, String whichPage) {
        ConnectedBrokersFragment fragment = new ConnectedBrokersFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUY_RENT_MODEL, param1);
        args.putString(WHICH_PAGE, whichPage);
        fragment.setArguments(args);
        return fragment;
    }

    private static final String POSITON = "position";
    private static final String SCALE = "scale";

    public static Fragment newInstance(MainActivity context, int pos, float scale) {
        Bundle b = new Bundle();
        b.putInt(POSITON, pos);
        b.putFloat(SCALE, scale);

        return Fragment.instantiate(context, ConnectedBrokersFragment.class.getName(), b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            object = getArguments().getParcelable(BUY_RENT_MODEL);
            whichPageToDisplay = getArguments().getString(WHICH_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_connected_brokers, container, false);
            unbinder = ButterKnife.bind(this, view);

            initPage();
            setValues2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void initPage() {
        context = getActivity();
        questionsList = new ArrayList<>();
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
    }

    private void setValues2() {

        if (object instanceof SecondLandingResponse.DataEntity) {

            SecondLandingResponse.DataEntity dataEntity = (SecondLandingResponse.DataEntity) object;
            switch (whichPageToDisplay) {
                case AppConstants.VISIBILITY_PAGE.GET_CONNECTED_ACTIVITY:
                    getConnectedLL.setVisibility(View.VISIBLE);
                    secondTimeLandingLL.setVisibility(View.GONE);
                    break;
                case AppConstants.VISIBILITY_PAGE.SECOND_TIME_ACTIVITY:
                    secondTimeLandingLL.setVisibility(View.VISIBLE);
                    getConnectedLL.setVisibility(View.VISIBLE);
                    if (dataEntity.getMeetAt().isEmpty()) {
                        projectLocation.setText("No Meetings Scheduled Yet");
                        date.setText("N/A");
                        time.setText("N/A");
                    } else {
                        projectLocation.setText(dataEntity.getMeetAt());
                        date.setText(dataEntity.getDateOfVisit());
                        time.setText(dataEntity.getTimeOfVisit());
                        if (dataEntity.getLatLong().size() != 0) {
                            showMap.setVisibility(View.VISIBLE);
                        } else {
                            showMap.setVisibility(View.GONE);
                        }
                    }
                    break;
            }

            if (dataEntity.getPostingType().equals("BUY")) {
                badge.setBackground(getResources().getDrawable(R.drawable.lead_type_buy));
            } else if (dataEntity.getPostingType().equals("RENT")) {
                badge.setBackground(getResources().getDrawable(R.drawable.lead_type_rent));
            } else if (dataEntity.getPostingType().equals("SELL")) {
                badge.setBackground(getResources().getDrawable(R.drawable.lead_type_sell));
            } else if (dataEntity.getPostingType().equals("RENT_OUT")) {
                badge.setBackground(getResources().getDrawable(R.drawable.lead_type_rentout));
            }
            badge.setText(AllUtils.StringUtilsBrongo.toCamelCase(dataEntity.getPostingType()) + "/" + AllUtils.StringUtilsBrongo.toCamelCase(dataEntity.getPropertyType()));

            Glide.with(context)
                    .load(dataEntity.getBrokerImage())
                    .apply(BrongoClientApplication.getRequestOption(true))
                    .into(brokerImage);
            brokerName.setText(dataEntity.getBrokerName());
            rating.setText(Float.parseFloat(dataEntity.getRating() + "") + "");
            ratingBar.setRating(Float.parseFloat(dataEntity.getRating() + ""));
            reviews.setText(dataEntity.getReviews() + "");

            requireList = new ArrayList<>();
            for (int i = 0; i < dataEntity.getProperty().size(); i++) {
                if (!dataEntity.getProperty().get(i).isEmpty())
                    requireList.add(dataEntity.getProperty().get(i));
            }
//            requireList = dataEntity.getProperty();

            final LayoutInflater mInflater = LayoutInflater.from(context);

            tagFlowLayout.setAdapter(mAdapter = new TagAdapter<String>(requireList) {

                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) mInflater.inflate(R.layout.second_tv,
                            tagFlowLayout, false);
                    tv.setText(s);
                    return tv;
                }

            });

            setDealGraph2();
            propertyId.setText("Deal No : " + dataEntity.getPropertyId());
            tempDetails.setPaintFlags(tempDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    public void setDealGraph2() {

        if (object instanceof SecondLandingResponse.DataEntity) {
            SecondLandingResponse.DataEntity dataEntity = (SecondLandingResponse.DataEntity) object;
            dealStatusList.removeAllViews();
            List<String> completedStatus = dataEntity.getCompletedStatus();
            List<String> pendingStatus = dataEntity.getRemainingStatus();
            ArrayList<Object> allList = new ArrayList<>();

            if (completedStatus.size() != 0) {                                                                              // Show Both Merged
                for (int i = 0; i < completedStatus.size(); i++) {
                    allList.add(new DealStatusCompleted(completedStatus.get(i), dataEntity.getStatusUpdatedTimes().get(i)));
                }
                challengeStatus.setVisibility(View.VISIBLE);
            } else {
                challengeStatus.setVisibility(View.GONE);
            }

            if (pendingStatus.size() > 1) {
                allList.add(new DealStatusCompleted(dataEntity.getRemainingStatus().get(0), dataEntity.getStatusUpdatedTimes().get(0)));
                for (int i = 1; i < pendingStatus.size(); i++) {
                    allList.add(dataEntity.getRemainingStatus().get(i));
                }
                dealStatusList.addView(TimeLineUtil.getMergedOne(context, allList));
            } else if (pendingStatus.size() == 1) {
                allList.add(new DealStatusCompleted(dataEntity.getRemainingStatus().get(0), dataEntity.getStatusUpdatedTimes().get(0)));
                dealStatusList.addView(TimeLineUtil.getGreenOne(context, allList));

            } else {
                for (int i = 0; i < completedStatus.size(); i++) {
                    allList.add(new DealStatusCompleted(completedStatus.get(i), dataEntity.getStatusUpdatedTimes().get(i)));
                }
                dealStatusList.addView(TimeLineUtil.getGreenOne(context, allList));
            }

            paymentBTN.setVisibility(View.GONE);
            dealClosedCard.setVisibility(View.GONE);
            cardDealStatus.setVisibility(View.VISIBLE);
            dealClosedRL.setVisibility(View.VISIBLE);

            if (pendingStatus.size() < 3) {
                dropDeal.setVisibility(View.GONE);
                if (!dataEntity.isClientRated()) {
                    feedbackBTN.setVisibility(View.VISIBLE);
                } else {
                    feedbackBTN.setVisibility(View.GONE);
                }

                if (dataEntity.getPostingType().equals("RENT") || dataEntity.getPostingType().equals("RENT_OUT")) {
                    if (!dataEntity.isPayed() || !dataEntity.isClientRated()) {
                        dealClosedCard.setVisibility(View.VISIBLE);
                        dealClosedMessage.setText("Your broker " + dataEntity.getBrokerName() + " has claimed that thedeal is closed.");
                        cardDealStatus.setVisibility(View.GONE);
                    }
                    if (!dataEntity.isPayed()) {
                        paymentBTN.setVisibility(View.VISIBLE);
                    }

                } else {
                    if (pendingStatus.size() < 2 && !dataEntity.isClientRated()) {
                        dealClosedCard.setVisibility(View.VISIBLE);
                        cardDealStatus.setVisibility(View.GONE);
                    }
                }

            } else {
                dropDeal.setVisibility(View.VISIBLE);
            }

            if (dataEntity.getPostingType().equals("BUY") || dataEntity.getPostingType().equals("SELL")) {
                payoffline.setVisibility(View.VISIBLE);
                paymentBTN.setVisibility(View.GONE);
            } else {
                payoffline.setVisibility(View.GONE);
            }

        }

    }

    @OnClick(R.id.showMap)
    public void openLocMap() {
        SecondLandingResponse.DataEntity obj = (SecondLandingResponse.DataEntity) object;
        if (obj.getLatLong().size() != 0) {
            Uri gmmIntentUri = Uri.parse("geo:" + obj.getLatLong().get(0) + "," + obj.getLatLong().get(1) + "?q=" + obj.getLatLong().get(0) + "," + obj.getLatLong().get(1));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    }

    @OnClick(R.id.detailstv)
    public void viewReqDetails() {
        SecondLandingResponse.DataEntity obj = (SecondLandingResponse.DataEntity) object;
        Intent intent = new Intent(context, RequirementsDetailsActivity.class);
        intent.putExtra("brokerNo", obj.getBrokerMobileNo());
        intent.putExtra("clientNo", pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""));
        intent.putExtra("postingType", obj.getPostingType());
        intent.putExtra("propertyId", obj.getPropertyId());
        intent.putExtra("propertyType", obj.getPropertyType());
        intent.putExtra("subPropertyType", obj.getSubPropertyType());
        startActivity(intent);
    }

    @OnClick(R.id.yesBtn)
    public void closedDealYes() {
        SecondLandingResponse.DataEntity dataEntity = (SecondLandingResponse.DataEntity) object;
        if (!dataEntity.isPayed()) {
            MakePayment();
        } else {
            GiveFeedBack();
        }
    }

    @OnClick(R.id.make_payment_BTN)
    public void MakePayment() {
        selectedTask = TASK.MAKE_PAYMENT;
        if (InternetConnection.isNetworkAvailable(context)) {
            final String baseUrl = "http://13.59.8.173:8080/QuickBroker/client";
            String headerDeviceId = Utils.getDeviceId(context);
            String headerPlatform = "android";
            String headerToken = pref.getString("token", "");
            final String userMobileNo = pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "");
            final String firstName = pref.getString(AppConstants.PREFS.USER_FIRST_NAME, "");
            final String email = pref.getString(AppConstants.PREFS.USER_EMAIL, "");

            String postingType = "", propertyId = "", brokerNo = "";

            if (object instanceof SecondLandingResponse.DataEntity) {
                SecondLandingResponse.DataEntity dataEntity = (SecondLandingResponse.DataEntity) object;
                postingType = dataEntity.getPostingType();
                propertyId = dataEntity.getPropertyId();
                brokerNo = dataEntity.getBrokerMobileNo();
            }

            PaymentHashModel paymentHashModel = new PaymentHashModel();
            paymentHashModel.setAmount("1");
            paymentHashModel.setFirstname(firstName);
            paymentHashModel.setProductInfo("Brongo_Client");
            paymentHashModel.setEmail(email);
            paymentHashModel.setMobileNo(userMobileNo);
            paymentHashModel.setPaymentId(propertyId);
            paymentHashModel.setBrokerMobileNo(brokerNo);
            paymentHashModel.setPaymentMode("DEAL");
            paymentHashModel.setIsDevelopment(1);
            paymentHashModel.setUserType("CLIENT");

            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<PaymentHashResponseModel> call = apiInstance.getPaymentHash(headerToken, headerPlatform, headerDeviceId, paymentHashModel);
            final String finalPostingType = postingType;
            final String finalBrokerNo = brokerNo;
            final String finalPropertyId = propertyId;
            call.enqueue(new Callback<PaymentHashResponseModel>() {
                @Override
                public void onResponse(Call<PaymentHashResponseModel> call, Response<PaymentHashResponseModel> response) {
                    AllUtils.LoaderUtils.dismissLoader();
                    if (response != null && response.body() != null && response.isSuccessful()) {
                        PaymentHashResponseModel hashResponseModel = response.body();
                        List<PaymentHashResponseModel.Data> data = hashResponseModel.getData();

                        PaymentParams paymentParams = new PaymentParams();
                        paymentParams.setKey("gtKFFx");                  //DEVELOPMENT
                        //paymentParams.setKey("FHOPnO");               //PRODUCTION
                        paymentParams.setTxnId(data.get(0).getTxnid());

                        paymentParams.setAmount("1");
                        paymentParams.setProductInfo("Brongo_Client");
                        paymentParams.setFirstName(firstName);
                        //paymentParams.setVpa(data.get(0).getVapsHash());
                        paymentParams.setEmail(email);
                        paymentParams.setUdf1(finalBrokerNo);
                        paymentParams.setUdf2(finalPropertyId);
                        paymentParams.setUdf3("DEAL");
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
//                        payuConfig.setEnvironment(PayuConstants.MOBILE_STAGING_ENV);
//                        payuConfig.setEnvironment(PayuConstants.PRODUCTION_ENV);
                        payuConfig.setEnvironment(PayuConstants.STAGING_ENV);

                        Intent intent = new Intent(getActivity(), PayUBaseActivity.class);
                        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
                        intent.putExtra(PayuConstants.PAYMENT_PARAMS, paymentParams);
                        intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);
                        startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<PaymentHashResponseModel> call, Throwable t) {
                    AllUtils.LoaderUtils.dismissLoader();
                    AllUtils.DialogUtils.PaymentErrorDialog(context, ConnectedBrokersFragment.this);
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, this);
        }

    }

    @OnClick(R.id.give_feedback_BTN)
    public void GiveFeedBack() {
        String name = "", pic = "", brokerMobileNo = "", propertyId = "";
        float rating = 0;

        if (object instanceof SecondLandingResponse.DataEntity) {
            SecondLandingResponse.DataEntity dataEntity = (SecondLandingResponse.DataEntity) object;
            name = dataEntity.getBrokerName();
            rating = (float) dataEntity.getRating();
            pic = dataEntity.getBrokerImage();
            brokerMobileNo = dataEntity.getBrokerMobileNo();
            propertyId = dataEntity.getPropertyId();
        }


        Intent intent = new Intent(context, FeedbackActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("rating", rating);
        intent.putExtra("pic", pic);
        intent.putExtra("brokerMobileNo", brokerMobileNo);
        intent.putExtra("propertyId", propertyId);
        startActivityForResult(intent, 110);
    }

    @OnClick(R.id.chatIV)
    public void Chat(ImageView button) {

        if (object instanceof SecondLandingResponse.DataEntity) {
            SecondLandingResponse.DataEntity dataEntity = (SecondLandingResponse.DataEntity) object;
            showChatPopDialog(dataEntity.getBrokerMobileNo(), dataEntity.getBrokerName());
        }
    }

    @OnClick(R.id.callIM)
    public void Call(ImageView call) {

        if (object instanceof SecondLandingResponse.DataEntity) {
            SecondLandingResponse.DataEntity dataEntity = (SecondLandingResponse.DataEntity) object;
            getKnowalarityCall(dataEntity.getBrokerMobileNo());
        }
    }

    @OnClick(R.id.call_back_IV)
    public void CallBack(ImageView callBack) {
        selectedTask = TASK.CALLBACK;
        AllUtils.LoaderUtils.showLoader(context);
        if (InternetConnection.isNetworkAvailable(context)) {
            headerDeviceId = Utils.getDeviceId(context);
            headerPlatform = "android";
            headerToken = pref.getString("token", "");
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            String from, to = "";
            from = pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "");
            if (object instanceof SecondLandingResponseModel.Data.BuyAndRent) {
                SecondLandingResponseModel.Data.BuyAndRent buyAndRent = (SecondLandingResponseModel.Data.BuyAndRent) object;
                to = buyAndRent.getBrokerMobileNo();
            } else if (object instanceof SecondLandingResponseModel.Data.SellAndRentOut) {
                SecondLandingResponseModel.Data.SellAndRentOut sellAndRentOut = (SecondLandingResponseModel.Data.SellAndRentOut) object;
                to = sellAndRentOut.getBrokerMobileNo();
            }
            Call<ResponseBody> call = apiInstance.CallBack(headerToken, headerPlatform, headerDeviceId, from, to);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        AllUtils.LoaderUtils.dismissLoader();
                        if (response != null && response.body() != null && response.isSuccessful()) {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            int statusCode = jsonObject.optInt("statusCode");
                            String message = jsonObject.optString("message");
                            if (statusCode == 200 && message.equals("Broker will call you shortly")) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                    new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                } else {
                                    Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                                Log.e("error", response.errorBody().string());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    AllUtils.LoaderUtils.dismissLoader();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, this);
        }

    }

    @OnClick(R.id.challengeStatus)
    public void ChallengeStatus(TextView tv) {
        ArrayList<String> challengeList = getArrayList(object);

        if (object instanceof SecondLandingResponse.DataEntity) {
            SecondLandingResponse.DataEntity dataEntity = (SecondLandingResponse.DataEntity) object;
            showChallengeStatusDialog(dataEntity.getPropertyId(), dataEntity.getBrokerMobileNo(), challengeList);
        }
    }

    private ArrayList<String> getArrayList(Object object) {
        ArrayList<String> challengeList = new ArrayList<>();

        if (object instanceof SecondLandingResponse.DataEntity) {
            SecondLandingResponse.DataEntity dataEntity = (SecondLandingResponse.DataEntity) object;
            for (int i = 0; i < dataEntity.getCompletedStatus().size() - 2; i++) {
                challengeList.add(dataEntity.getCompletedStatus().get(i));
            }
        }
        return challengeList;
    }

    @OnClick(R.id.dropDeal)
    public void DropTheDeal(LinearLayout button) {

        if (object instanceof SecondLandingResponse.DataEntity) {
            SecondLandingResponse.DataEntity dataEntity = (SecondLandingResponse.DataEntity) object;
            getBrongoQuestionsApi(dataEntity.getPropertyId(), dataEntity.getBrokerMobileNo(), dataEntity.getPostingType());
        }
    }

    private void showChatPopDialog(final String brokerNo, final String brokerName) {
        final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
        dialogBlock.setContentView(R.layout.chat_pop_dialog);
        dialogBlock.setCanceledOnTouchOutside(false);
        dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Button ok = dialogBlock.findViewById(R.id.ok_BTN);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBlock.dismiss();
                Intent intent = new Intent(context, ConversationActivity.class);
                intent.putExtra(ConversationUIService.USER_ID, brokerNo);
                intent.putExtra(ConversationUIService.DISPLAY_NAME, brokerName); //put it for displaying the title.
                intent.putExtra(ConversationUIService.TAKE_ORDER, true); //Skip chat list for showing on back press
                startActivity(intent);
            }
        });

        ImageView cancel = dialogBlock.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBlock.dismiss();
            }
        });

        dialogBlock.show();
    }

    private void DropTheDealDialog(final String propertyId, final String brokerNo, final String postingType) {
        final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
        dialogBlock.setContentView(R.layout.drop_lead_dialog);
        dialogBlock.setCanceledOnTouchOutside(false);
        dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        RadioGroup group = dialogBlock.findViewById(R.id.radiogroup);
        RadioGroup.LayoutParams params_rb = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params_rb.topMargin = AllUtils.DensityUtils.dpToPx(5);

        int textColor = Color.parseColor("#882f8e");
        for (int i = 0; i < questionsList.size(); i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(questionsList.get(i).getQuestion());
            radioButton.setId(questionsList.get(i).getQuestionId());
            radioButton.setPadding(AllUtils.DensityUtils.dpToPx(6), 0, 0, 0);
            radioButton.setTextColor(Color.GRAY);
            radioButton.setLayoutParams(params_rb);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                radioButton.setButtonTintList(ColorStateList.valueOf(textColor));
            }
            group.addView(radioButton);
        }

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioBtn2 = (RadioButton) dialogBlock.findViewById(checkedId);
            }
        });
        Button submit = dialogBlock.findViewById(R.id.SubmitBtn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioBtn2 != null) {
                    dialogBlock.dismiss();
                    dropLeadAPI(propertyId, postingType, brokerNo, radioBtn2.getText().toString());
                } else {
                    Toast.makeText(context, "Please select a reason", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView cancel = dialogBlock.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBlock.dismiss();
            }
        });
        dialogBlock.show();
    }


    private void getKnowalarityCall(String brokerMobileNo) {
        selectedTask = TASK.GET_CALL;
        if (InternetConnection.isNetworkAvailable(context)) {
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<KnowlarityApiResponseModel> call = apiInstance.getKnowlarityCall(pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""), brokerMobileNo);
            call.enqueue(new Callback<KnowlarityApiResponseModel>() {
                @Override
                public void onResponse(Call<KnowlarityApiResponseModel> call, Response<KnowlarityApiResponseModel> response) {
                    AllUtils.LoaderUtils.dismissLoader();
                    if (response != null && response.isSuccessful()) {
                        KnowlarityApiResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200 && responseModel.getMessage().equals("You Can Processed With Call")) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel: +919590224224"));
                            startActivity(callIntent);
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

    private void showChallengeStatusDialog(final String propertyId, final String brokerNo, ArrayList<String> challengeList) {
        final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
        dialogBlock.setContentView(R.layout.challenge_status_dialog);
        dialogBlock.setCanceledOnTouchOutside(false);
        dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        commentsET = dialogBlock.findViewById(R.id.comments);
        RadioGroup group = dialogBlock.findViewById(R.id.radiogroup);
        int textColor = Color.parseColor("#882f8e");

        for (int i = 0; i < challengeList.size(); i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(challengeList.get(i));
            radioButton.setId(i + 555);
            radioButton.setPadding(AllUtils.DensityUtils.dpToPx(6), 0, 0, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                radioButton.setButtonTintList(ColorStateList.valueOf(textColor));
            }
            group.addView(radioButton);
        }

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioBtn = (RadioButton) dialogBlock.findViewById(checkedId);
            }
        });
        Button challengeStatus = dialogBlock.findViewById(R.id.challengeBtn);
        challengeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioBtn != null) {
                    dialogBlock.dismiss();
                    challengeStatusApi(propertyId, commentsET.getText().toString(), radioBtn.getId(), brokerNo);
                } else {
                    Toast.makeText(context, "Please select atleast one", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView cancel = dialogBlock.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBlock.dismiss();
            }
        });

        dialogBlock.show();
    }

    private void showThankYouDialog() {
        final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
        dialogBlock.setContentView(R.layout.thankyou_dialog);
        dialogBlock.setCanceledOnTouchOutside(false);
        dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Button back = dialogBlock.findViewById(R.id.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBlock.dismiss();
                brokerListener.ReloadBrokers();
                String brokerName = "", brokerImage = "", brokerMobileNo = "", propertyId = "";
                float brokerRating = 0.0F;

                if (object instanceof SecondLandingResponse.DataEntity) {
                    SecondLandingResponse.DataEntity dataEntity = (SecondLandingResponse.DataEntity) object;
                    brokerName = dataEntity.getBrokerName();
                    brokerImage = dataEntity.getBrokerImage();
                    brokerRating = (float) dataEntity.getRating();
                    brokerMobileNo = dataEntity.getBrokerMobileNo();
                    propertyId = dataEntity.getPropertyId();
                }

                AllUtils.LoaderUtils.dismissLoader();
                Intent intent = new Intent(context, FeedbackActivity.class);
                intent.putExtra("name", brokerName);
                intent.putExtra("rating", brokerRating);
                intent.putExtra("pic", brokerImage);
                intent.putExtra("brokerMobileNo", brokerMobileNo);
                intent.putExtra("propertyId", propertyId);
                startActivityForResult(intent, 110);
            }
        });

        ImageView cancel = dialogBlock.findViewById(R.id.cancel);
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBlock.dismiss();
                    }
                });

        dialogBlock.show();
    }

    private void showConnectToNewBrokerDialog() {
        final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
        dialogBlock.setContentView(R.layout.popup_dialog_two_btn);
        dialogBlock.setCanceledOnTouchOutside(false);
        dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        TextView message = dialogBlock.findViewById(R.id.thankyoutv);
        message.setText("Would you like to work with new broker ?");
        Button yes = dialogBlock.findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBlock.dismiss();
                connectToNewBrok();
            }
        });

        Button no = dialogBlock.findViewById(R.id.no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBlock.dismiss();
                brokerListener.ReloadBrokers();
            }
        });

        ImageView cancel = dialogBlock.findViewById(R.id.cancel);
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBlock.dismiss();
                        brokerListener.ReloadBrokers();
                    }
                });

        dialogBlock.show();
    }

    private void connectToNewBrok() {
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        if (InternetConnection.isNetworkAvailable(context)) {
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<GeneralApiResponseModel> call = apiInstance.connectToNewBroker(headerToken, headerPlatform, headerDeviceId, getModel());
            call.enqueue(new Callback<GeneralApiResponseModel>() {
                @Override
                public void onResponse(Call<GeneralApiResponseModel> call, Response<GeneralApiResponseModel> response) {
                    AllUtils.LoaderUtils.dismissLoader();
                    if (response != null && response.isSuccessful()) {
                        GeneralApiResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {
                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                            brokerListener.ReloadBrokers();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                connectToNewBrok();
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
                public void onFailure(Call<GeneralApiResponseModel> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    AllUtils.LoaderUtils.dismissLoader();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, (NoInternetTryConnectListener) context);
        }
    }

    private AcceptedBrokersInputModel getModel() {
        SecondLandingResponse.DataEntity dataEntity = (SecondLandingResponse.DataEntity) object;
        AcceptedBrokersInputModel model = new AcceptedBrokersInputModel();
        model.setPropertyId(dataEntity.getPropertyId());
        model.setPostingType(dataEntity.getPostingType());
        model.setClientMobileNo(pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""));
        model.setBrokerMobileNo(dataEntity.getBrokerMobileNo());
        return model;
    }

    private void getBrongoQuestionsApi(final String propertyId, final String brokerNo, final String postingType) {
        selectedTask = TASK.GET_QUESTIONS;
        if (InternetConnection.isNetworkAvailable(context)) {
            headerDeviceId = Utils.getDeviceId(context);
            headerPlatform = "android";
            headerToken = pref.getString("token", "");
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<QuestionsResponseModel> call = apiInstance.getBrongoQuestions(headerToken, headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""), "DROP_DEAL");
            call.enqueue(new Callback<QuestionsResponseModel>() {
                @Override
                public void onResponse(Call<QuestionsResponseModel> call, Response<QuestionsResponseModel> response) {
                    try {
                        AllUtils.LoaderUtils.dismissLoader();
                        if (response != null && response.isSuccessful()) {
                            QuestionsResponseModel responseModel = response.body();
                            if (responseModel.getStatusCode() == 200) {
                                questionsList.clear();
                                questionsList.addAll(responseModel.getData());
                                DropTheDealDialog(propertyId, brokerNo, postingType);
                            } else {
                                Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                    new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));

                                } else {
                                    Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                                Log.e("error", response.errorBody().string());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<QuestionsResponseModel> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    AllUtils.LoaderUtils.dismissLoader();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, this);
        }
    }

    private void dropLeadAPI(String propertyId, String postingType, String brokerNo, String reason) {
        selectedTask = TASK.DROP_DEAL;
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        AcceptedBrokersInputModel model3 = new AcceptedBrokersInputModel();
        model3.setClientMobileNo(pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""));
        model3.setPropertyId(propertyId);
        model3.setPostingType(postingType);
        model3.setBrokerMobileNo(brokerNo);
        model3.setReason(reason);
        if (InternetConnection.isNetworkAvailable(context)) {
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<GeneralApiResponseModel> call = apiInstance.getLeadDrop(headerToken, headerPlatform, headerDeviceId, model3);
            call.enqueue(new Callback<GeneralApiResponseModel>() {
                @Override
                public void onResponse(Call<GeneralApiResponseModel> call, Response<GeneralApiResponseModel> response) {
                    AllUtils.LoaderUtils.dismissLoader();
                    if (response != null && response.isSuccessful()) {
                        GeneralApiResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {
                            showThankYouDialog();
                        } else {
                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));

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
                    AllUtils.LoaderUtils.dismissLoader();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, this);
        }
    }

    private void challengeStatusApi(String propertyId, String challengeCommentsText, int challengeQue, String brokerNo) {
        selectedTask = TASK.CHALLENGE_STATUS;
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        AcceptedBrokersInputModel model2 = new AcceptedBrokersInputModel();
        model2.setClientMobileNo(pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""));
        model2.setPropertyId(propertyId);
        model2.setChallengeComment(challengeCommentsText);
        //model2.setChallengeQue(challengeQue);
        model2.setBrokerMobileNo(brokerNo);
        if (InternetConnection.isNetworkAvailable(context)) {
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<GeneralApiResponseModel> call = apiInstance.challengeStatus(headerToken, headerPlatform, headerDeviceId, model2);
            call.enqueue(new Callback<GeneralApiResponseModel>() {
                @Override
                public void onResponse(Call<GeneralApiResponseModel> call, Response<GeneralApiResponseModel> response) {
                    AllUtils.LoaderUtils.dismissLoader();
                    if (response != null && response.isSuccessful()) {
                        GeneralApiResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {
                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));

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
                    AllUtils.LoaderUtils.dismissLoader();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        brokerListener = (BrokerListener) getActivity();
    }

    void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void RetryPayment() {
        paymentBTN.performClick();
    }

    @Override
    public void paymentSuccess() {
        brokerListener.ReloadBrokers();
    }

    @Override
    public void onTryReconnect() {

        switch (selectedTask) {
            case GET_CALL: {
                call.performClick();
                break;
            }
            case DROP_DEAL: {

                if (object instanceof SecondLandingResponse.DataEntity) {
                    SecondLandingResponse.DataEntity dataEntity = (SecondLandingResponse.DataEntity) object;
                    dropLeadAPI(dataEntity.getPropertyId(), dataEntity.getPostingType(), dataEntity.getBrokerMobileNo(), radioBtn2.getText().toString());
                }
                break;
            }
            case GET_QUESTIONS: {
                dropDeal.performClick();
                break;
            }
            case MAKE_PAYMENT: {
                paymentBTN.performClick();
                break;
            }
            case CHALLENGE_STATUS: {

                if (object instanceof SecondLandingResponse.DataEntity) {
                    SecondLandingResponse.DataEntity dataEntity = (SecondLandingResponse.DataEntity) object;
                    challengeStatusApi(dataEntity.getPropertyId(), commentsET.getText().toString(), radioBtn.getId(), dataEntity.getBrokerMobileNo());
                }
                break;
            }
        }
    }


    public interface BrokerListener {
        void ReloadBrokers();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        } else if (requestCode != 110) {
            AllUtils.DialogUtils.PaymentErrorDialog(context, this);
        }

        if (requestCode == 110 && resultCode == RESULT_OK) {
            if (data.getIntExtra("statusCode", 0) == 200) {
//                brokerListener.ReloadBrokers();
                showConnectToNewBrokerDialog();
            }
        }
    }
}
