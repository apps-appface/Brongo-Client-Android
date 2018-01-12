package com.turnipconsultants.brongo_client.fragments.BuyAProperty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.fragments.BaseFragment;

/**
 * Created by mohit on 19-09-2017.
 */

public class BUY_A_LandFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.buy_a_land_fragment,container,false);
        return v;
    }

    @Override
    public void OnReset() {

    }
}
