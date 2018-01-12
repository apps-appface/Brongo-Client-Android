package com.turnipconsultants.brongo_client.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.Listener.FragmentBackListener;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.models.AcceptedBrokersInputModel;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackFragmentStage2 extends Fragment {
    private static final String PIC = "pic";
    private static final String NAME = "param1";
    private static final String RATING = "param2";
    private static final String BROKERNUM = "param3";

    private String picValue;
    private String nameValue, brokerNum;
    private float ratingValue;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.reset)
    TextView reset;

    @BindView(R.id.brokerName_TV)
    TextView BrokerNameTV;

    @BindView(R.id.ratingBar)
    RatingBar ratingRB;

    @BindView(R.id.well_FL)
    TagFlowLayout wellFL;


    @BindView(R.id.profilePicCIV)
    CircleImageView profilePicCIV;

    @BindView(R.id.submitBTN)
    RelativeLayout submitBTN;

    private Context context;
    private Unbinder unbinder;
    private String[] wellTypes = new String[]{"Professionalism", "Good local knowledge", "Always on time", "Showed relevant property", "Organised"};
    private String selectedWell = "";
    private SharedPreferences prefs;
    private String headerToken, headerDeviceId, headerPlatform;
    private List<String> selectedReviews;


    private FragmentBackListener mListener;

    public FeedbackFragmentStage2() {
        // Required empty public constructor
    }

    public static FeedbackFragmentStage2 newInstance(String name, String pic, float rating, String brokerNo) {
        FeedbackFragmentStage2 fragment = new FeedbackFragmentStage2();
        Bundle args = new Bundle();
        args.putString(PIC, pic);
        args.putString(NAME, name);
        args.putFloat(RATING, rating);
        args.putString(BROKERNUM, brokerNo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nameValue = getArguments().getString(NAME);
            picValue = getArguments().getString(PIC);
            ratingValue = getArguments().getFloat(RATING);
            brokerNum = getArguments().getString(BROKERNUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feedback_fragment_stage2, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, v);
        initPage();

        title.setText("FEEDBACK");
        reset.setVisibility(View.GONE);

        BrokerNameTV.setText(nameValue);
        ratingRB.setRating(ratingValue);


        final LayoutInflater mInflater = LayoutInflater.from(context);
        wellFL.setAdapter(new TagAdapter<String>(wellTypes) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, wellFL, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                selectedWell = "";
                selectedReviews.remove(wellTypes[position]);
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                selectedWell = wellTypes[position];
                selectedReviews.add(selectedWell);
            }
        });

        setListener();
        return v;
    }

    private void setListener() {
        ratingRB.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });
    }


    private void initPage() {
        prefs = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        Glide.with(context)
                .load(picValue)
                .apply(BrongoClientApplication.getRequestOption(false))
                .into(profilePicCIV);
        selectedReviews = new ArrayList<>();
    }

    @OnClick(R.id.submitBTN)
    void SubmitRating() {
//        AllUtils.ToastUtils.showToast(context, "Not Yet Implemented");
        submitFeedBack();
    }

    private void submitFeedBack() {
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = prefs.getString("token", "");

        if (InternetConnection.isNetworkAvailable(context)) {

            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<GeneralApiResponseModel> call = apiInstance.postBrokerFeedBack(headerToken, headerPlatform, headerDeviceId, getStringValues());
            call.enqueue(new Callback<GeneralApiResponseModel>() {
                @Override
                public void onResponse(Call<GeneralApiResponseModel> call, Response<GeneralApiResponseModel> response) {
                    AllUtils.LoaderUtils.dismissLoader();
                    if (response != null && response.isSuccessful()) {
                        GeneralApiResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {
                            AllUtils.ToastUtils.showToast(context, responseModel.getMessage());
                            getActivity().finish();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, prefs.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                submitFeedBack();
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

    private AcceptedBrokersInputModel getStringValues() {
        AcceptedBrokersInputModel inputModel = new AcceptedBrokersInputModel();
        inputModel.setClientMobileNo(prefs.getString(AppConstants.PREFS.USER_MOBILE_NO, ""));
        inputModel.setBrokerMobileNo(brokerNum);
        inputModel.setRating(ratingRB.getNumStars());
        inputModel.setReview(selectedReviews);
        inputModel.setComment("");
        return inputModel;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentBackListener) {
            mListener = (FragmentBackListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
