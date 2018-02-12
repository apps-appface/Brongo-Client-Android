package com.turnipconsultants.brongo_client.fragments.BuyAProperty;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.adapters.LandRouterPagerAdapter;
import com.turnipconsultants.brongo_client.adapters.MainRouterPagerAdapter;
import com.turnipconsultants.brongo_client.fragments.BaseFragment;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by mohit on 19-09-2017.
 */

public class BUY_A_LandFragment extends BaseFragment implements HasSupportFragmentInjector{

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private LandRouterPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.buy_a_land_fragment, container, false);
        ButterKnife.bind(this, v);
        setValues();
        return v;
    }

    private void setValues() {
        tabLayout.addTab(tabLayout.newTab().setText("Commercial Zone"));
        tabLayout.addTab(tabLayout.newTab().setText("Residential Zone"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new LandRouterPagerAdapter(getChildFragmentManager(), AppConstants.PROPERTY.BUY_A_PROPERTY, tabLayout.getTabCount());
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

    @Override
    public void OnReset() {
        try {
            BaseFragment fragment = adapter.getFragment(viewPager.getCurrentItem());
            fragment.OnReset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return childFragmentInjector;
    }
}
