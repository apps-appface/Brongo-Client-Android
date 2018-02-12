package com.turnipconsultants.brongo_client.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.turnipconsultants.brongo_client.fragments.BaseFragment;
import com.turnipconsultants.brongo_client.fragments.BuyAProperty.BUY_Land_CommercialFragment;
import com.turnipconsultants.brongo_client.fragments.BuyAProperty.BUY_Land_ResidentialFragment;
import com.turnipconsultants.brongo_client.fragments.PastReqClosedFragment;
import com.turnipconsultants.brongo_client.fragments.PastReqDroppedFragment;
import com.turnipconsultants.brongo_client.fragments.SellYourProperty.SELL_Your_Land_CommercialFragment;
import com.turnipconsultants.brongo_client.fragments.SellYourProperty.SELL_Your_Land_ResidentialFragment;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;

import java.util.HashMap;
import java.util.Map;


public class PastRequirementsPagerAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;

    public PastRequirementsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment=new PastReqClosedFragment();
                break;
            case 1:
                fragment=new PastReqDroppedFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public void destroyItem(View container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
