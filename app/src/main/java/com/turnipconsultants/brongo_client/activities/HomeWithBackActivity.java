package com.turnipconsultants.brongo_client.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applozic.mobicomkit.api.MobiComKitConstants;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.mvp.HomePresenter;
import com.turnipconsultants.brongo_client.mvp.HomePresenterImpl;
import com.turnipconsultants.brongo_client.mvp.HomeView;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.turnipconsultants.brongo_client.others.Constants.AppConstants.PROPERTY.PROPERTY_TYPE;

/**
 * Created by mohit on 10-10-2017.
 */

public class HomeWithBackActivity extends AppCompatActivity implements HomeView {

    private static final String TAG = "HomeWithBackButtonActiv";
    @BindView(R.id.back)
    RelativeLayout back;

    @BindView(R.id.chatRL)
    RelativeLayout chatRL;

    @BindView(R.id.notiRL)
    RelativeLayout notiRL;

    @BindView(R.id.buyPropertyRL)
    RelativeLayout BuyPropertyRL;

    @BindView(R.id.rentPropertyRL)
    RelativeLayout RentPropertyRL;

    @BindView(R.id.sellPropertyRL)
    RelativeLayout SellPropertyRL;

    @BindView(R.id.rentOutPropertyRL)
    RelativeLayout RentOutPropertyRL;

    @BindView(R.id.chat_num)
    TextView chatCountTV;

    @BindView(R.id.not_num)
    TextView notiCountTV;
    private Unbinder unbinder;

    private Context context;
    private HomePresenter homePresenter;
    private String headerToken, headerDeviceId, headerPlatform;
    private SharedPreferences pref;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_home_with_back_button);
        unbinder = ButterKnife.bind(this);
        initPage();
        SaveBrokerCount();
    }

    private void initPage() {
        context = this;
        pref = this.getSharedPreferences(AppConstants.PREF_NAME, 0);
        homePresenter = new HomePresenterImpl(this, this);
    }


    @OnClick(R.id.buyPropertyRL)
    public void openBuyProperty() {
        Intent intent = new Intent(this, PropertyRouterActivity.class);
        intent.putExtra(PROPERTY_TYPE, AppConstants.PROPERTY.BUY_A_PROPERTY);
        startActivity(intent);
    }

    @OnClick(R.id.rentPropertyRL)
    public void openRentProperty() {
        Intent intent = new Intent(this, PropertyRouterActivity.class);
        intent.putExtra(PROPERTY_TYPE, AppConstants.PROPERTY.RENT_A_PROPERTY);
        startActivity(intent);
    }


    @OnClick(R.id.sellPropertyRL)
    public void sellYourProperty() {
        Intent intent = new Intent(this, PropertyRouterActivity.class);
        intent.putExtra(PROPERTY_TYPE, AppConstants.PROPERTY.SELL_YOUR_PROPERTY);
        startActivity(intent);
    }

    @OnClick(R.id.rentOutPropertyRL)
    public void rentYourProperty() {
        Intent intent = new Intent(this, PropertyRouterActivity.class);
        intent.putExtra(PROPERTY_TYPE, AppConstants.PROPERTY.RENT_YOUR_PROPERTY);
        startActivity(intent);
    }

    @OnClick(R.id.back)
    void submit() {
        finish();
    }

    @OnClick(R.id.chatRL)
    void OpenChat() {
        Intent intent = new Intent(this, ConversationActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.notiRL)
    void OpenNotification() {
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }

    @Override
    public void setChatCount(int chatCount) {
        if (chatCount == 0)
            chatCountTV.setVisibility(View.GONE);
        else {
            chatCountTV.setVisibility(View.VISIBLE);
            chatCountTV.setText(chatCount + "");
        }
    }

    @Override
    public void setNotificationCount(int notiCount) {
        if (notiCount == 0)
            notiCountTV.setVisibility(View.GONE);
        else {
            notiCountTV.setVisibility(View.VISIBLE);
            notiCountTV.setText(notiCount + "");
        }
    }

    void SaveBrokerCount() {
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        Utils.saveBrokers(context, headerToken, headerPlatform, headerDeviceId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(MobiComKitConstants.APPLOZIC_UNREAD_COUNT);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
        homePresenter.getChatCount();
        homePresenter.getNotificationCount();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        if (unbinder != null)
            unbinder.unbind();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.w(TAG, "Leads Update Push Received");
            if (MobiComKitConstants.APPLOZIC_UNREAD_COUNT.equals(intent.getAction())) {
                homePresenter.getChatCount();
                homePresenter.getNotificationCount();
            }
        }
    };


}
