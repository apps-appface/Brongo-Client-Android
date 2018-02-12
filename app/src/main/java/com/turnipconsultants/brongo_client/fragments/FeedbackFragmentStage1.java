package com.turnipconsultants.brongo_client.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.Listener.FragmentBackListener;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.FeedBackQueResponse;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.turnipconsultants.brongo_client.others.Constants.AppConstants.FRAGMENT_TAGS.FEEDBACK_STAGE2_FRAGMENT;

public class FeedbackFragmentStage1 extends Fragment {
    private static final String TAG = "FeedbackFragmentStage1";
    private static final String PIC = "picValue";
    private static final String NAME = "param1";
    private static final String RATING = "param2";
    private static final String BROKERNUM = "param3";
    private static final String PROPERTYID="param4";

    private String nameValue;
    private float ratingValue;
    private String picValue;
    private String brokerNum,propertyId;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.reset)
    TextView reset;

    @BindView(R.id.brokerName_TV)
    TextView BrokerNameTV;

    @BindView(R.id.ratingBar)
    RatingBar ratingRB;

    @BindView(R.id.profilePicCIV)
    CircleImageView profilePicCIV;

    private Context context;
    private Unbinder unbinder;
    private FragmentManager fm;

    private FragmentBackListener mListener;
    private SharedPreferences prefs;
    private String headerToken, headerDeviceId, headerPlatform;
    private ArrayList<FeedBackQueResponse.DataEntity> feedbackTags = new ArrayList<>();

    public FeedbackFragmentStage1() {
        // Required empty public constructor
    }


    public static FeedbackFragmentStage1 newInstance(String name, String pic, float rating, String brokerMobileNo,String propertyId) {
        FeedbackFragmentStage1 fragment = new FeedbackFragmentStage1();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        args.putString(PIC, pic);
        args.putFloat(RATING, rating);
        args.putString(BROKERNUM, brokerMobileNo);
        args.putString(PROPERTYID,propertyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nameValue = getArguments().getString(NAME);
            ratingValue = getArguments().getFloat(RATING);
            picValue = getArguments().getString(PIC);
            brokerNum = getArguments().getString(BROKERNUM);
            propertyId=getArguments().getString(PROPERTYID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feedback_fragment_stage1, container, false);
        context = getActivity();
        prefs = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        fm = getFragmentManager();
        unbinder = ButterKnife.bind(this, v);
        title.setText("FEEDBACK");
        reset.setVisibility(View.GONE);
        initPage();
        BrokerNameTV.setText(nameValue);
//        ratingRB.setRating(ratingValue);
        ratingRB.setRating(0f);
        setListener();
        getFeedBackTags();
        return v;
    }

    private void setListener() {
        /*ratingRB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setTags();
                }
                return false;
            }
        });*/
        ratingRB.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                try {
                    setTags();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void setTags() {
        float ratingVal = ratingRB.getRating();
        ArrayList<String> defaultStr = new ArrayList<>();
        List<String> tags = ratingVal < 1.5 ? feedbackTags.get(0).getComment() :
                (ratingVal > 1.5 && ratingVal <= 2.5) ? feedbackTags.get(1).getComment() :
                        (ratingVal > 2.5 && ratingVal <= 3.5) ? feedbackTags.get(2).getComment() :
                                (ratingVal > 3.5 && ratingVal <= 4.5) ? feedbackTags.get(3).getComment() :
                                        (ratingVal > 4.5 && ratingVal <= 5) ? feedbackTags.get(4).getComment()
                                                : defaultStr;
        AllUtils.FragmentUtils.addFragment(fm, R.id.fragment_container, FeedbackFragmentStage2.newInstance(propertyId,nameValue, picValue, ratingVal, brokerNum, (ArrayList<String>) tags,feedbackTags), FEEDBACK_STAGE2_FRAGMENT);

    }

    private void initPage() {
        Glide.with(context)
                .load(picValue)
                .apply(BrongoClientApplication.getRequestOption(false))
                .into(profilePicCIV);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentBackListener) {
            mListener = (FragmentBackListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentBackListener");
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

    private void getFeedBackTags() {
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = prefs.getString("token", "");

        if (InternetConnection.isNetworkAvailable(context)) {
            feedbackTags.clear();
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<FeedBackQueResponse> call = apiInstance.getFeedBackQue(headerToken, headerPlatform, headerDeviceId, prefs.getString(AppConstants.PREFS.USER_MOBILE_NO, ""));
            call.enqueue(new Callback<FeedBackQueResponse>() {
                @Override
                public void onResponse(Call<FeedBackQueResponse> call, Response<FeedBackQueResponse> response) {
                    AllUtils.LoaderUtils.dismissLoader();
                    if (response != null && response.isSuccessful()) {
                        FeedBackQueResponse responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {
                            feedbackTags.addAll(responseModel.getData());
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, prefs.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                getFeedBackTags();
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
                public void onFailure(Call<FeedBackQueResponse> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    AllUtils.LoaderUtils.dismissLoader();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, (NoInternetTryConnectListener) context);
        }
    }
}
