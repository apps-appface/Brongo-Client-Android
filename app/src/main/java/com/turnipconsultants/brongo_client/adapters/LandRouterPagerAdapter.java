package com.turnipconsultants.brongo_client.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.turnipconsultants.brongo_client.fragments.BaseFragment;
import com.turnipconsultants.brongo_client.fragments.BuyAProperty.BUY_A_CommercialFragment;
import com.turnipconsultants.brongo_client.fragments.BuyAProperty.BUY_A_LandFragment;
import com.turnipconsultants.brongo_client.fragments.BuyAProperty.BUY_A_ResidentialFragment;
import com.turnipconsultants.brongo_client.fragments.BuyAProperty.BUY_Land_CommercialFragment;
import com.turnipconsultants.brongo_client.fragments.BuyAProperty.BUY_Land_ResidentialFragment;
import com.turnipconsultants.brongo_client.fragments.RentAProperty.RENT_A_CommercialFragment;
import com.turnipconsultants.brongo_client.fragments.RentAProperty.RENT_A_LandFragment;
import com.turnipconsultants.brongo_client.fragments.RentAProperty.RENT_A_ResidentialFragment;
import com.turnipconsultants.brongo_client.fragments.RentYourProperty.RENT_Your_CommercialFragment;
import com.turnipconsultants.brongo_client.fragments.RentYourProperty.RENT_Your_LandFragment;
import com.turnipconsultants.brongo_client.fragments.RentYourProperty.RENT_Your_ResidentialFragment;
import com.turnipconsultants.brongo_client.fragments.SellYourProperty.SELL_Your_CommercialFragment;
import com.turnipconsultants.brongo_client.fragments.SellYourProperty.SELL_Your_LandFragment;
import com.turnipconsultants.brongo_client.fragments.SellYourProperty.SELL_Your_Land_CommercialFragment;
import com.turnipconsultants.brongo_client.fragments.SellYourProperty.SELL_Your_Land_ResidentialFragment;
import com.turnipconsultants.brongo_client.fragments.SellYourProperty.SELL_Your_ResidentialFragment;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;

import java.util.HashMap;
import java.util.Map;


public class LandRouterPagerAdapter extends FragmentPagerAdapter {
    public Map<Integer,BaseFragment> referenceMap;
    int propertyType;
    int mNumOfTabs;

    public LandRouterPagerAdapter(FragmentManager fm, int propertyType, int NumOfTabs) {
        super(fm);
        this.propertyType = propertyType;
        this.mNumOfTabs = NumOfTabs;
        this.referenceMap = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment = null;
        switch (propertyType) {
            case AppConstants.PROPERTY.BUY_A_PROPERTY:
                fragment = BuyALandProperty(position);
                break;
            case AppConstants.PROPERTY.SELL_YOUR_PROPERTY:
                fragment = SellYourProperty(position);
                break;
        }
        referenceMap.put(position, fragment);
        return fragment;
    }

    private BaseFragment BuyALandProperty(int position) {
        switch (position) {
            case 0:
                BUY_Land_CommercialFragment tab1 = new BUY_Land_CommercialFragment();
                return tab1;
            case 1:
                BUY_Land_ResidentialFragment tab2 = new BUY_Land_ResidentialFragment();
                return tab2;
            default:
                return null;
        }
    }

    private BaseFragment SellYourProperty(int position) {
        switch (position) {
            case 0:
                SELL_Your_Land_CommercialFragment tab1 = new SELL_Your_Land_CommercialFragment();
                return tab1;
            case 1:
                SELL_Your_Land_ResidentialFragment tab2 = new SELL_Your_Land_ResidentialFragment();
                return tab2;
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    /*@Override
    public int getItemPosition(Object object) {
        Log.i("pageradapter","POSITION_NONE"+POSITION_NONE);
        return POSITION_NONE;
    }*/
    public void destroyItem(View container, int position, Object object) {
        super.destroyItem(container, position, object);
        referenceMap.remove(position);
    }


    public BaseFragment getFragment(int position) {
        return referenceMap.get(position);
    }
}
