package com.turnipconsultants.brongo_client.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.responseModels.AcceptedBrokersResponseModel;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mohit on 27-07-2016.
 */
public class BrokerReviewPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
    Context context;
    ViewPager pager;
    private int mCurrentPosition;
    private int mScrollState;
    ArrayList<AcceptedBrokersResponseModel.Data> arrayList;
    private BrokerReviewRecycAdapter reviewAdapter;
    private ArrayList reviewList;
    private RecyclerView reviewRecyc;
    ViewPagerOnItemClick viewPagerOnItemClick;

    public BrokerReviewPagerAdapter(Context context, ViewPager pager, ArrayList arrayList, ViewPagerOnItemClick listener) {
        this.context = context;
        this.pager = pager;
        this.arrayList = arrayList;
        reviewList = new ArrayList();
        reviewAdapter = new BrokerReviewRecycAdapter(context, reviewList);
        viewPagerOnItemClick = listener;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final AcceptedBrokersResponseModel.Data dataEntity = arrayList.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.broker_review_row, container, false);
        reviewRecyc = row.findViewById(R.id.reviewRecyc);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        reviewRecyc.setLayoutManager(manager);
        reviewRecyc.setAdapter(reviewAdapter);
        container.addView(row);

        CircleImageView brokerImage = row.findViewById(R.id.circImage);
        TextView brokerName = row.findViewById(R.id.brokerName);
        TextView rating = row.findViewById(R.id.rating);
        RatingBar ratingBar = row.findViewById(R.id.ratingBar);
        TextView userReviews = row.findViewById(R.id.userdeals);
        TextView closedDeals = row.findViewById(R.id.closedDeals);
        TextView openDeals = row.findViewById(R.id.opendeals);
        Button getConnected = row.findViewById(R.id.getconnected);

        Glide.with(context)
                .load(dataEntity.getProfileImage())
                .apply(BrongoClientApplication.getRequestOption(true))
                .into(brokerImage);
        brokerName.setText(dataEntity.getFirstName() + " " + dataEntity.getLastName());
        rating.setText(String.valueOf(dataEntity.getRating()));
        ratingBar.setRating(dataEntity.getRating());
        userReviews.setText(String.valueOf(dataEntity.getReviews().size()));

        //Pending...
        closedDeals.setText("0");
        openDeals.setText("0");


        getConnected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPagerOnItemClick.itemClick(position, dataEntity.getMobileNo());
            }
        });
        return row;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);

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

    public interface ViewPagerOnItemClick {
        void itemClick(int pos, String mobileNo);
    }
}
