package com.turnipconsultants.brongo_client.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.adapters.LandRouterPagerAdapter;
import com.turnipconsultants.brongo_client.adapters.PastReqAdapter;
import com.turnipconsultants.brongo_client.adapters.PastRequirementsPagerAdapter;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.PastRequirementResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastRequirementActivity extends AppCompatActivity implements NoInternetTryConnectListener {

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    BrongoTextView title;

    @BindView(R.id.reset)
    BrongoTextView reset;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private Context context;
    private PastRequirementsPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_requirement);
        ButterKnife.bind(this);
        initPage();
        setValues();
    }

    private void initPage() {
        context = this;
        title.setText("PAST DEALS");
        reset.setVisibility(View.INVISIBLE);
    }

    private void setValues() {
        tabLayout.addTab(tabLayout.newTab().setText("Closed"));
        tabLayout.addTab(tabLayout.newTab().setText("Dropped / Not Connected"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new PastRequirementsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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
    protected void onDestroy() {
        super.onDestroy();
    }


    public void endActivity(View v) {
        finish();
    }

    @Override
    public void onTryReconnect() {

    }
}