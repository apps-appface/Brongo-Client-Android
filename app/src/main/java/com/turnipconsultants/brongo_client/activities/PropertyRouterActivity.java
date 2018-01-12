package com.turnipconsultants.brongo_client.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.adapters.MainRouterPagerAdapter;
import com.turnipconsultants.brongo_client.fragments.BaseFragment;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by mohit on 18-09-2017.
 */

public class PropertyRouterActivity extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    BrongoTextView toolBarTitle;
    @BindView(R.id.reset)
    BrongoTextView toolBarReset;
    @BindView(R.id.LinearLayout)
    RelativeLayout LinearLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private Context context;
    private MainRouterPagerAdapter adapter;
    private int propertyType;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyproperty);
        unbinder = ButterKnife.bind(this);
        initPage();

        AllUtils.KeyboardUtils.hideKeyBoard(this);
        switch (propertyType) {
            case AppConstants.PROPERTY.BUY_A_PROPERTY:
                toolBarTitle.setText("BUY A PROPERTY");
                tabLayout.addTab(tabLayout.newTab().setText("Residential"));
                tabLayout.addTab(tabLayout.newTab().setText("Commercial"));
                tabLayout.addTab(tabLayout.newTab().setText("Land"));
                break;
            case AppConstants.PROPERTY.RENT_A_PROPERTY:
                toolBarTitle.setText("RENT A PROPERTY");
                tabLayout.addTab(tabLayout.newTab().setText("Residential"));
                tabLayout.addTab(tabLayout.newTab().setText("Commercial"));
                tabLayout.addTab(tabLayout.newTab().setText("Land"));
                break;
            case AppConstants.PROPERTY.SELL_YOUR_PROPERTY:
                toolBarTitle.setText("SELL YOUR PROPERTY");
                tabLayout.addTab(tabLayout.newTab().setText("Residential"));
                tabLayout.addTab(tabLayout.newTab().setText("Commercial"));
                tabLayout.addTab(tabLayout.newTab().setText("Land"));
                break;
            case AppConstants.PROPERTY.RENT_YOUR_PROPERTY:
                toolBarTitle.setText("RENT YOUR PROPERTY");
                tabLayout.addTab(tabLayout.newTab().setText("Residential"));
                tabLayout.addTab(tabLayout.newTab().setText("Commercial"));
                tabLayout.addTab(tabLayout.newTab().setText("Land"));
                break;
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new MainRouterPagerAdapter(getSupportFragmentManager(), propertyType, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initPage() {
        context = this;
        propertyType = getIntent().getIntExtra(AppConstants.PROPERTY.PROPERTY_TYPE, AppConstants.PROPERTY.BUY_A_PROPERTY);
    }

    public void endActivity(View v) {
        finish();
    }

    @OnClick(R.id.reset)
    public void onReset() {

        try {
            BaseFragment fragment = adapter.getFragment(viewPager.getCurrentItem());
            fragment.OnReset();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }
}
