package com.turnipconsultants.brongo_client.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.adapters.SplashPagerAdapter;
import com.turnipconsultants.brongo_client.fragments.SplashPagerFragment;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SplashPagerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.viewPagerCountDots)
    LinearLayout viewPagerCountDots;

    @BindView(R.id.skip)
    TextView skipTV;

    SplashPagerAdapter pageAdapter;
    Unbinder unbinder;
    Context context;
    private int dotsCount;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_pager);
        unbinder = ButterKnife.bind(this);
        context = this;
        List<Fragment> fragments = getFragments();
        pageAdapter = new SplashPagerAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);
        pager.addOnPageChangeListener(this);
        setUiPageViewController();
        pager.setCurrentItem(0);

    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(SplashPagerFragment.newInstance(R.drawable.splash_1));
        fList.add(SplashPagerFragment.newInstance(R.drawable.splash_2));
        fList.add(SplashPagerFragment.newInstance(R.drawable.splash_3));
        fList.add(SplashPagerFragment.newInstance(R.drawable.splash_4));
        return fList;
    }

    public void openSignUp() {
        Intent i = new Intent(context, LoginSignUpActivity.class);
        startActivity(i);
        finish();
    }

    private void setUiPageViewController() {

        dotsCount = pageAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            int margin = AllUtils.DensityUtils.dpToPx(4);
            params.setMargins(margin, 0, margin, 0);

            viewPagerCountDots.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }


    @OnClick(R.id.skip)
    void Skip(TextView skipTV) {
        openSignUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
