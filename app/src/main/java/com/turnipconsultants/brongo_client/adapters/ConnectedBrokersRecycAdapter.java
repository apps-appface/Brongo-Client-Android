package com.turnipconsultants.brongo_client.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.turnipconsultants.brongo_client.fragments.ConnectedBrokersFragment;
import com.turnipconsultants.brongo_client.responseModels.SecondLandingResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pankaj on 15-11-2017.
 */

public class ConnectedBrokersRecycAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener{
    public Map<Integer, ConnectedBrokersFragment> referenceMap;
    private List<Object> objectList;
    private String whichPage = "";
    private int mCurrentPosition;
    private int mScrollState;
    ViewPager pager;

    public ConnectedBrokersRecycAdapter(FragmentManager fm, List<Object> objectList, String whichPage,ViewPager pager) {
        super(fm);
        this.objectList = objectList;
        this.whichPage = whichPage;
        this.pager = pager;
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        handleScrollState(state);
        mScrollState = state;
    }

    private void handleScrollState(final int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            setNextItemIfNeeded();
        }
    }

    private void setNextItemIfNeeded() {
        if (isScrollStateSettling() || !isScrollStateSettling()) {
            handleSetNextItem();
        }
    }

    private boolean isScrollStateSettling() {
        return mScrollState == ViewPager.SCROLL_STATE_SETTLING;
    }

    private void handleSetNextItem() {
        final int lastPosition = pager.getAdapter().getCount() - 1;
        if (mCurrentPosition == 0) {
            pager.setCurrentItem(lastPosition - 1, false);
        } else if (mCurrentPosition == lastPosition) {
            pager.setCurrentItem(1, false);
        }
    }
}