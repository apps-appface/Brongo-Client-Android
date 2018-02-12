package com.turnipconsultants.brongo_client.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.turnipconsultants.brongo_client.Listener.FragmentBackListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.fragments.ConnectionSuccessFragment;
import com.turnipconsultants.brongo_client.fragments.FeedbackFragmentStage1;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.turnipconsultants.brongo_client.others.Constants.AppConstants.FRAGMENT_TAGS.FEEDBACK_STAGE1_FRAGMENT;

/**
 * Created by Pankaj on 10-11-2017.
 */

public class FeedbackActivity extends AppCompatActivity implements FragmentBackListener {
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    private FragmentManager fm;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        unbinder = ButterKnife.bind(this);

        try {
            fm = getSupportFragmentManager();
            String name = getIntent().getStringExtra("name");
            float rating = getIntent().getFloatExtra("rating", 0F);
            String pic = getIntent().getStringExtra("pic");
            String brokerMobileNo=getIntent().getStringExtra("brokerMobileNo");
            String propertyId=getIntent().getStringExtra("propertyId");
            FeedbackFragmentStage1 brokersListFragment = FeedbackFragmentStage1.newInstance(name, pic, rating,brokerMobileNo,propertyId);
            AllUtils.FragmentUtils.addFragment(fm, fragmentContainer.getId(), brokersListFragment, FEEDBACK_STAGE1_FRAGMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void endActivity(View v) {
        onBack();
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
    }

    void ClearAllBackEntries() {
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

}
