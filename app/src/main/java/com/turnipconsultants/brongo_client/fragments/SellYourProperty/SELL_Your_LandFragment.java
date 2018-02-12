package com.turnipconsultants.brongo_client.fragments.SellYourProperty;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.adapters.LandRouterPagerAdapter;
import com.turnipconsultants.brongo_client.fragments.BaseFragment;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SELL_Your_LandFragment extends BaseFragment {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private LandRouterPagerAdapter adapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public static SELL_Your_LandFragment newInstance(String param1, String param2) {
        SELL_Your_LandFragment fragment = new SELL_Your_LandFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sell__your__land, container, false);
        ButterKnife.bind(this, v);
        setValues();
        return v;
    }

    private void setValues() {
        tabLayout.addTab(tabLayout.newTab().setText("Commercial Zone"));
        tabLayout.addTab(tabLayout.newTab().setText("Residential Zone"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new LandRouterPagerAdapter(getChildFragmentManager(), AppConstants.PROPERTY.SELL_YOUR_PROPERTY, tabLayout.getTabCount());
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


}
