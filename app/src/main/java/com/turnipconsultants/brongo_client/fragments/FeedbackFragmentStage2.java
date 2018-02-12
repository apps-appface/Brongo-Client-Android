package com.turnipconsultants.brongo_client.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
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
import com.turnipconsultants.brongo_client.responseModels.FeedBackQueResponse;
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

import static android.app.Activity.RESULT_OK;

public class FeedbackFragmentStage2 extends Fragment {
    private static final String PIC = "pic";
    private static final String NAME = "param1";
    private static final String RATING = "param2";
    private static final String BROKERNUM = "param3";
    private static final String TAGS = "param4";
    private static final String OBJ = "param5";
    private static final String PROPERTYID = "param6";

    private String picValue;
    private String nameValue, brokerNum, propertyId;
    private float ratingValue;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.reset)
    TextView reset;

    @BindView(R.id.title2)
    BrongoTextView title2;

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

    @BindView(R.id.other_comm_ET)
    EditText commentsET;

    private Context context;
    private Unbinder unbinder;
    private String[] wellTypes = new String[]{"Professionalism", "Good local knowledge", "Always on time", "Showed relevant property", "Organised"};
    private String selectedWell = "";
    private SharedPreferences prefs;
    private String headerToken, headerDeviceId, headerPlatform;
    private List<String> selectedReviews;
    private ArrayList<String> feedTags;
    private ArrayList<FeedBackQueResponse.DataEntity> feedbackTags;

    private FragmentBackListener mListener;
    private TagAdapter<String> tagAdapter;

    public FeedbackFragmentStage2() {
        // Required empty public constructor
    }

    public static FeedbackFragmentStage2 newInstance(String propertyId, String name, String pic, float rating, String brokerNo, ArrayList<String> tagList, ArrayList<FeedBackQueResponse.DataEntity> objArray) {
        FeedbackFragmentStage2 fragment = new FeedbackFragmentStage2();
        Bundle args = new Bundle();
        args.putString(PIC, pic);
        args.putString(NAME, name);
        args.putFloat(RATING, rating);
        args.putString(BROKERNUM, brokerNo);
        args.putString(PROPERTYID, propertyId);
        args.putStringArrayList(TAGS, tagList);
        args.putSerializable(OBJ, objArray);
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
            propertyId = getArguments().getString(PROPERTYID);
            feedTags = getArguments().getStringArrayList(TAGS);
            feedbackTags = (ArrayList<FeedBackQueResponse.DataEntity>) getArguments().getSerializable(OBJ);
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
        wellFL.setAdapter(tagAdapter = new TagAdapter<String>(feedTags) {

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
                selectedReviews.remove(feedTags.get(position));
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                selectedWell = feedTags.get(position);
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
                if (rating < 1.0f)
                    ratingBar.setRating(1.0f);
                setTags();
            }
        });
    }

    private void setTags() {
        float ratingVal = ratingRB.getRating();
        ArrayList<String> defaultStr = new ArrayList<>();
        feedTags = (ArrayList<String>) (ratingVal < 1.5 ? feedbackTags.get(0).getComment() :
                (ratingVal > 1.5 && ratingVal <= 2.5) ? feedbackTags.get(1).getComment() :
                        (ratingVal > 2.5 && ratingVal <= 3.5) ? feedbackTags.get(2).getComment() :
                                (ratingVal > 3.5 && ratingVal <= 4.5) ? feedbackTags.get(3).getComment() :
                                        (ratingVal > 4.5 && ratingVal <= 5) ? feedbackTags.get(4).getComment()
                                                : defaultStr);
        String title = (ratingVal < 1.5 ? feedbackTags.get(0).getMeaning() :
                (ratingVal > 1.5 && ratingVal <= 2.5) ? feedbackTags.get(1).getMeaning() :
                        (ratingVal > 2.5 && ratingVal <= 3.5) ? feedbackTags.get(2).getMeaning() :
                                (ratingVal > 3.5 && ratingVal <= 4.5) ? feedbackTags.get(3).getMeaning() :
                                        (ratingVal > 4.5 && ratingVal <= 5) ? feedbackTags.get(4).getMeaning()
                                                : "How was your experience ?");
        title2.setText(title);
//        tagAdapter.notifyDataChanged();
        final LayoutInflater mInflater = LayoutInflater.from(context);
        wellFL.setAdapter(tagAdapter = new TagAdapter<String>(feedTags) {

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
                selectedReviews.remove(feedTags.get(position));
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                selectedWell = feedTags.get(position);
                selectedReviews.add(selectedWell);
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
                            Intent intent = new Intent();
                            intent.putExtra("statusCode", 200);
                            getActivity().setResult(RESULT_OK, intent);
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
        inputModel.setRating(ratingRB.getRating());
        inputModel.setReview(selectedReviews);
        inputModel.setPropertyId(propertyId);
        if (!commentsET.getText().toString().isEmpty()) {
            inputModel.setComment(commentsET.getText().toString());
        } else {
            inputModel.setComment("");
        }
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
