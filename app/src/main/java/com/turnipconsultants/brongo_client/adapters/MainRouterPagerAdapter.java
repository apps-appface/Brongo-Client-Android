package com.turnipconsultants.brongo_client.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.turnipconsultants.brongo_client.fragments.BaseFragment;
import com.turnipconsultants.brongo_client.fragments.BuyAProperty.BUY_A_CommercialFragment;
import com.turnipconsultants.brongo_client.fragments.BuyAProperty.BUY_A_LandFragment;
import com.turnipconsultants.brongo_client.fragments.BuyAProperty.BUY_A_ResidentialFragment;
import com.turnipconsultants.brongo_client.fragments.RentAProperty.RENT_A_CommercialFragment;
import com.turnipconsultants.brongo_client.fragments.RentAProperty.RENT_A_LandFragment;
import com.turnipconsultants.brongo_client.fragments.RentAProperty.RENT_A_ResidentialFragment;
import com.turnipconsultants.brongo_client.fragments.RentYourProperty.RENT_Your_CommercialFragment;
import com.turnipconsultants.brongo_client.fragments.RentYourProperty.RENT_Your_LandFragment;
import com.turnipconsultants.brongo_client.fragments.RentYourProperty.RENT_Your_ResidentialFragment;
import com.turnipconsultants.brongo_client.fragments.SellYourProperty.SELL_Your_CommercialFragment;
import com.turnipconsultants.brongo_client.fragments.SellYourProperty.SELL_Your_LandFragment;
import com.turnipconsultants.brongo_client.fragments.SellYourProperty.SELL_Your_ResidentialFragment;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;

import java.util.HashMap;
import java.util.Map;


public class MainRouterPagerAdapter extends FragmentPagerAdapter {
    public Map<Integer,BaseFragment> referenceMap;
    int propertyType;
    int mNumOfTabs;

    public MainRouterPagerAdapter(FragmentManager fm, int propertyType, int NumOfTabs) {
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
                fragment = BuyAProperty(position);
                break;
            case AppConstants.PROPERTY.RENT_A_PROPERTY:
                fragment = RentAProperty(position);
                break;
            case AppConstants.PROPERTY.SELL_YOUR_PROPERTY:
                fragment = SellYourProperty(position);
                break;
            case AppConstants.PROPERTY.RENT_YOUR_PROPERTY:
                fragment = RentYourProperty(position);
                break;
        }
        referenceMap.put(position, fragment);
        return fragment;
    }

    private BaseFragment BuyAProperty(int position) {
        switch (position) {
            case 0:
                BUY_A_ResidentialFragment tab1 = new BUY_A_ResidentialFragment();
                return tab1;
            case 1:
                BUY_A_CommercialFragment tab2 = new BUY_A_CommercialFragment();
                return tab2;
            case 2:
                BUY_A_LandFragment tab3 = new BUY_A_LandFragment();
                return tab3;
            default:
                return null;
        }
    }

    private BaseFragment RentAProperty(int position) {
        switch (position) {
            case 0:
                RENT_A_ResidentialFragment tab1 = new RENT_A_ResidentialFragment();
                return tab1;
            case 1:
                RENT_A_CommercialFragment tab2 = new RENT_A_CommercialFragment();
                return tab2;
//            case 2:
//                RENT_A_LandFragment tab3 = new RENT_A_LandFragment();
//                return tab3;
            default:
                return null;
        }
    }

    private BaseFragment SellYourProperty(int position) {
        switch (position) {
            case 0:
                SELL_Your_ResidentialFragment tab1 = new SELL_Your_ResidentialFragment();
                return tab1;
            case 1:
                SELL_Your_CommercialFragment tab2 = new SELL_Your_CommercialFragment();
                return tab2;
            case 2:
                SELL_Your_LandFragment tab3 = new SELL_Your_LandFragment();
                return tab3;
            default:
                return null;
        }
    }

    private BaseFragment RentYourProperty(int position) {
        switch (position) {
            case 0:
                RENT_Your_ResidentialFragment tab1 = new RENT_Your_ResidentialFragment();
                return tab1;
            case 1:
                RENT_Your_CommercialFragment tab2 = new RENT_Your_CommercialFragment();
                return tab2;
//            case 2:
//                RENT_Your_LandFragment tab3 = new RENT_Your_LandFragment();
//                return tab3;
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
