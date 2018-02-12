package com.turnipconsultants.brongo_client.fragments;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.payu.magicretry.MainActivity;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.activities.HomeActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConnectionSuccessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConnectionSuccessFragment extends Fragment {
    private static final String BROKER_NAME = "param1";
    private static final String BROKER_MOBILE = "param2";
    private static final String BROKER_IMAGE = "param3";
    private static final String BROKER_LOCATION = "param4";

    private String brokerName, brokerMobile, brokerImage, brokerLocation;

    @BindView(R.id.okBTN)
    Button OkBTN;

    @BindView(R.id.broker_name_TV)
    TextView brokerNameTV;

//    @BindView(R.id.check_mark_GV)
//    GifView check_mark_GV;

    @BindView(R.id.newGif)
    GifImageView newGif;

    private Context context;
    private Unbinder unbinder;
    private int count = 0;

    public ConnectionSuccessFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param brokerName     Parameter 1.
     * @param brokerMobile   Parameter 2.
     * @param brokerImage    Parameter 2.
     * @param brokerLocation Parameter 2.
     * @return A new instance of fragment ConnectionSuccessFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConnectionSuccessFragment newInstance(String brokerName, String brokerMobile, String brokerImage, String brokerLocation) {
        ConnectionSuccessFragment fragment = new ConnectionSuccessFragment();
        Bundle args = new Bundle();
        args.putString(BROKER_NAME, brokerName);
        args.putString(BROKER_MOBILE, brokerMobile);
        args.putString(BROKER_IMAGE, brokerImage);
        args.putString(BROKER_LOCATION, brokerLocation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            brokerName = getArguments().getString(BROKER_NAME);
            brokerMobile = getArguments().getString(BROKER_MOBILE);
            brokerImage = getArguments().getString(BROKER_IMAGE);
            brokerLocation = getArguments().getString(BROKER_LOCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection_success, container, false);
        unbinder = ButterKnife.bind(this, view);
        brokerNameTV.setText(brokerName);
        context = getActivity();
        GifDrawable gifDrawable = null;
        try {
            gifDrawable = new GifDrawable(getResources(), R.drawable.check_mark);
            gifDrawable.setLoopCount(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        newGif.setImageDrawable(gifDrawable);

        MediaPlayer ring = MediaPlayer.create(context, R.raw.check_mark);
        ring.start();

        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MediaPlayer ring = MediaPlayer.create(context, R.raw.check_mark);
                ring.start();
            }
        }, 1300);*/

        return view;
    }

    @OnClick(R.id.okBTN)
    void OK() {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }
}
