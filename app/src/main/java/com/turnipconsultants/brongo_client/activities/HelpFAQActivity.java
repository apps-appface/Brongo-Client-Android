package com.turnipconsultants.brongo_client.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.turnipconsultants.brongo_client.Listener.HelpFAQFragmentItemClickListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.fragments.FAQFragment;
import com.turnipconsultants.brongo_client.fragments.HelpFAQFragment;
import com.turnipconsultants.brongo_client.fragments.HelpSupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelpFAQActivity extends AppCompatActivity implements HelpFAQFragmentItemClickListener {

    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    private Context context;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_faq);
        ButterKnife.bind(this);
        context = this;
        fm = getSupportFragmentManager();


        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(fragmentContainer.getId(), HelpFAQFragment.newInstance("", ""));
        ft.addToBackStack("HELP_FAQ");
        ft.commit();
    }


    @Override
    public void OnHelpFAQItemClick(String item) {
        FragmentTransaction ft = fm.beginTransaction();
        switch (item) {
            case "Help & Support":
                ft.replace(fragmentContainer.getId(), HelpSupportFragment.newInstance("", ""));
                ft.addToBackStack("HELP_SUPPORT");
                ft.commit();
                break;
            case "FAQ":
                ft.replace(fragmentContainer.getId(), FAQFragment.newInstance("", ""));
                ft.addToBackStack("FAQ");
                ft.commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        } else {
            finish();
        }
    }

    public void endActivity(View v) {
        onBackPressed();
    }
}
