package com.turnipconsultants.brongo_client.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.Listener.FragmentBackListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.turnipconsultants.brongo_client.others.Constants.AppConstants.FRAGMENT_TAGS.FEEDBACK_STAGE2_FRAGMENT;

public class FeedbackFragmentStage1 extends Fragment {
    private static final String TAG = "FeedbackFragmentStage1";
    private static final String PIC = "picValue";
    private static final String NAME = "param1";
    private static final String RATING = "param2";
    private static final String BROKERNUM="param3";

    private String nameValue;
    private float ratingValue;
    private String picValue;
    private String brokerNum;

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

    public FeedbackFragmentStage1() {
        // Required empty public constructor
    }


    public static FeedbackFragmentStage1 newInstance(String name, String pic, float rating,String brokerMobileNo) {
        FeedbackFragmentStage1 fragment = new FeedbackFragmentStage1();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        args.putString(PIC, pic);
        args.putFloat(RATING, rating);
        args.putString(BROKERNUM,brokerMobileNo);
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
            brokerNum=getArguments().getString(BROKERNUM);
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
        ratingRB.setRating(ratingValue);
        setListener();
        return v;
    }

    private void setListener() {
        ratingRB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    AllUtils.FragmentUtils.addFragment(fm, R.id.fragment_container, FeedbackFragmentStage2.newInstance(nameValue, picValue, ratingValue,brokerNum), FEEDBACK_STAGE2_FRAGMENT);
                }
                return false;
            }
        });
        /*ratingRB.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            }
        });*/
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

}
