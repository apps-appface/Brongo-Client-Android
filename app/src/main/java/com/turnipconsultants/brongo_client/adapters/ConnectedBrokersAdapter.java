package com.turnipconsultants.brongo_client.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.turnipconsultants.brongo_client.fragments.ConnectedBrokersFragment;
import com.turnipconsultants.brongo_client.responseModels.SecondLandingResponse;
import com.turnipconsultants.brongo_client.responseModels.SecondLandingResponseModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pankaj on 15-11-2017.
 */

public class ConnectedBrokersAdapter extends FragmentStatePagerAdapter {
    public Map<Integer, ConnectedBrokersFragment> referenceMap;
    private List<Object> objectList;
    private String whichPage = "";

    public ConnectedBrokersAdapter(FragmentManager fm, List<Object> objectList, String whichPage) {
        super(fm);
        this.objectList = objectList;
        this.whichPage = whichPage;
        this.referenceMap = new HashMap<>();
    }

    @Override
    public Fragment getItem(int pos) {
        ConnectedBrokersFragment fr = null;
        Object obj = objectList.get(pos);
        if (obj instanceof SecondLandingResponse.DataEntity) {
            fr = ConnectedBrokersFragment.newInstance((SecondLandingResponse.DataEntity) obj, whichPage);
            referenceMap.put(pos, fr);
        }
        return fr;

    }

    @Override
    public int getCount() {
        return objectList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void destroyItem(View container, int position, Object object) {
        super.destroyItem(container, position, object);
        referenceMap.remove(position);
    }

    public ConnectedBrokersFragment getFragment(int position) {
        return referenceMap.get(position);
    }

}