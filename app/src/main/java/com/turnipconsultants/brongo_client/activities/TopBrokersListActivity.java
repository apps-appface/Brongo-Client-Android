package com.turnipconsultants.brongo_client.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.turnipconsultants.brongo_client.Listener.FragmentBackListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.fragments.AcceptedBrokersListFragment;
import com.turnipconsultants.brongo_client.fragments.ConnectionSuccessFragment;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.turnipconsultants.brongo_client.others.Constants.AppConstants.FRAGMENT_TAGS.ACCEPTED_BROKER_LIST;

/**
 * Created by mohit on 22-09-2017.
 */

public class TopBrokersListActivity extends AppCompatActivity implements FragmentBackListener {

    private final int COUNT_DOWN_TIME = 300000;

    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    private Context context;
    private FragmentManager fm;
    private Unbinder unbinder;
    private AcceptedBrokersListFragment brokersListFragment;
    private MyCountDownTimer myCountDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_brokers_list);
        try {
            unbinder = ButterKnife.bind(this);
            context = this;
            fm = getSupportFragmentManager();
            brokersListFragment = AcceptedBrokersListFragment.newInstance(getIntent().getStringExtra("place"), "");
            AllUtils.FragmentUtils.addFragment(fm, fragmentContainer.getId(), brokersListFragment, ACCEPTED_BROKER_LIST);

            myCountDownTimer = new MyCountDownTimer(COUNT_DOWN_TIME, 1000);

            myCountDownTimer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (fm.getBackStackEntryCount() > 1) {

            FragmentManager.BackStackEntry backStackEntry = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1);
            Fragment fragment = fm.findFragmentByTag(backStackEntry.getName());

            if (fragment != null && fragment instanceof ConnectionSuccessFragment) {
                ConnectionSuccessFragment myFragment = (ConnectionSuccessFragment) fragment;
                if (myFragment != null && myFragment.isVisible()) {
                    //No Operation
                    return;
                }
            }


            fm.popBackStack();
        } else {
            ClearAllBackEntries();
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();

        if (myCountDownTimer != null)
            myCountDownTimer.cancel();
    }

    void ClearAllBackEntries() {
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long seconds = millisUntilFinished / 1000;
            AcceptedBrokersListFragment test = (AcceptedBrokersListFragment) getSupportFragmentManager().findFragmentByTag(ACCEPTED_BROKER_LIST);
            if (test != null && test.isVisible()) {
                brokersListFragment.setTimerUpdate(true, String.format("%02d m %02d sec", seconds / 60, seconds % 60));
            }
        }

        @Override
        public void onFinish() {
            AcceptedBrokersListFragment test = (AcceptedBrokersListFragment) getSupportFragmentManager().findFragmentByTag(ACCEPTED_BROKER_LIST);
            if (test != null && test.isVisible()) {
                brokersListFragment.setTimerUpdate(false, "00:00");
            }
            if (myCountDownTimer != null)
                myCountDownTimer.cancel();
            showTimeOutDialog();
        }
    }

    private void showTimeOutDialog() {
        final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
        dialogBlock.setContentView(R.layout.time_out_dialog);
        dialogBlock.setCanceledOnTouchOutside(false);
        dialogBlock.setCancelable(false);
        dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Button okbtn = dialogBlock.findViewById(R.id.continuebtn);
        LinearLayout newSearch = dialogBlock.findViewById(R.id.newsearcll);
        newSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBlock.dismiss();
                finish();
            }
        });

        dialogBlock.show();
    }

}
