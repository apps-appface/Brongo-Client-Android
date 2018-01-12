package com.turnipconsultants.brongo_client.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.DBTables.Notification;
import com.turnipconsultants.brongo_client.DBTables.NotificationDao;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.Listener.OnLoadMoreListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.adapters.NotificationAdapter;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.responseModels.NotificationResponseModel;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity implements NoInternetTryConnectListener, NotificationAdapter.NotificationItemClickListener {

    private static final String TAG = "NotificationActivity";

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.reset)
    TextView reset;

    @BindView(R.id.zero_notification)
    TextView ZeroNotification;

    @BindView(R.id.notification_recycler_view)
    RecyclerView notificationRV;

    private Context context;
    private SharedPreferences pref;
    private String headerDeviceId, headerPlatform, headerToken, mobileNo;
    private ArrayList<NotificationResponseModel.DataEntity> notificationList;
    private NotificationAdapter notificationAdapter;
    private int from;
    private NotificationDao notificationDao;
    private boolean lastItemReached = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initPage();
        LoadFromStart();
    }

    private void setListener() {
        notificationAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                addLoader();
                LoadNotifications();
            }
        });
    }

    private void initPage() {
        context = this;
        pref = getSharedPreferences(AppConstants.PREF_NAME, 0);
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        mobileNo = pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "");
        notificationList = new ArrayList<NotificationResponseModel.DataEntity>();
        notificationRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        notificationAdapter = new NotificationAdapter(context, notificationList, notificationRV, this);
        notificationRV.setAdapter(notificationAdapter);

        reset.setVisibility(View.GONE);
        title.setText("ALERTS");
        from = 1;
        notificationDao = ((BrongoClientApplication) getApplication()).getDaoSession().getNotificationDao();
    }


    private void LoadNotifications() {
        RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
        Call<NotificationResponseModel> call = apiInstance.getNotifications(headerToken, headerPlatform, headerDeviceId, mobileNo, String.valueOf(from), "20");
        call.enqueue(new Callback<NotificationResponseModel>() {
            @Override
            public void onResponse(Call<NotificationResponseModel> call, Response<NotificationResponseModel> response) {
                removeLoader();
                try {
                    if (response != null && response.body() != null && response.isSuccessful()) {
                        NotificationResponseModel notiList = response.body();

                        if (from == 0 && notiList.getData().size() <= 20) {
                            notificationList.clear();
                            /*Save to GREEN DAO*/
                            List<Notification> listNotification = notificationDao.loadAll();
                            listNotification.clear();
                            notificationDao.deleteAll();
                            List<NotificationResponseModel.DataEntity> dataEntityList = notiList.getData();
                            for (int i = 0; i < dataEntityList.size(); i++) {
                                NotificationResponseModel.DataEntity dataEntity1 = dataEntityList.get(i);
                                Notification notification = new Notification();
                                notification.setAlertType(dataEntity1.getAlertType());
                                notification.setBrokerName(dataEntity1.getBrokerName());
                                notification.setBrokerProfile(dataEntity1.getBrokerProfile());
                                notification.setDays(dataEntity1.getDays());
                                notification.setId(dataEntity1.getId());
                                notification.setMessage(dataEntity1.getMessage());
                                notification.setIsRead(dataEntity1.isRead());

                                listNotification.add(notification);
                            }
                            notificationDao.saveInTx(listNotification);
                            from++;
                        }
                        from = from + 20;
                        if (notiList.getData().size() == 0) {
                            lastItemReached = true;
                        } else {
                            notificationList.addAll(notiList.getData());
                        }
                        if (notificationList.size() == 0) {
                            ZeroNotification.setVisibility(View.VISIBLE);
                            notificationRV.setVisibility(View.INVISIBLE);
                        } else {
                            if (!lastItemReached) {
                                notificationAdapter.notifyDataSetChanged();
                                notificationAdapter.setLoaded();
                                setListener();
                            }
                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<NotificationResponseModel> call, Throwable t) {
                removeLoader();
                if (!InternetConnection.isNetworkAvailable(context)) {
                    AllUtils.DialogUtils.NoInternetDialog(context, NotificationActivity.this);
                } else {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            }
        });
    }


    void LoadFromStart() {
        from = 0;
        notificationList.clear();
        LoadFromDB();
    }

    private void LoadFromDB() {
        addLoader();
        List<Notification> listNotification = notificationDao.loadAll();
        ArrayList<NotificationResponseModel.DataEntity> list = new ArrayList<NotificationResponseModel.DataEntity>();

        for (int i = 0; i < listNotification.size(); i++) {
            Notification nf = listNotification.get(i);
            NotificationResponseModel.DataEntity responseModel = new NotificationResponseModel.DataEntity();
            responseModel.setRead(nf.getIsRead());
            responseModel.setAlertType(nf.getAlertType());
            responseModel.setBrokerName(nf.getBrokerName());
            responseModel.setBrokerProfile(nf.getBrokerProfile());
            responseModel.setDays(nf.getDays());
            responseModel.setId(nf.getId());
            responseModel.setMessage(nf.getMessage());
            list.add(responseModel);
        }

        removeLoader();
        notificationList.addAll(list);
        notificationAdapter.notifyDataSetChanged();

        if (InternetConnection.isNetworkAvailable(context)) {
            addLoader();
            LoadNotifications();
        }
    }

    @Override
    public void onTryReconnect() {
        LoadFromStart();
    }

    public void endActivity(View v) {
        finish();
    }

    private void addLoader() {
        if (!notificationList.contains(null)) {
            notificationList.add(null);
            notificationAdapter.notifyItemInserted(notificationList.size() - 1);
        }
    }

    private void removeLoader() {
        if (notificationList.contains(null)) {
            notificationList.remove(notificationList.size() - 1);
            notificationAdapter.notifyItemRemoved(notificationList.size());
        }
    }

    @Override
    public void OnItemClick(final int position, NotificationResponseModel.DataEntity notification) {
        AllUtils.LoaderUtils.showLoader(context);
        RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
        Call<ResponseBody> call = apiInstance.SetNotificationRead(headerToken, headerPlatform, headerDeviceId, mobileNo, notification.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AllUtils.LoaderUtils.dismissLoader();
                if (response != null && response.body() != null && response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int statusCode = jsonObject.optInt("statusCode");
                        String message = jsonObject.optString("message");
                        if (statusCode == 200 && message.equals("Updated Successfully")) {
                            notificationList.get(position).setRead(true);
                            notificationAdapter.notifyDataSetChanged();
                            Log.i(TAG, "onResponse: Notification set as read Successfully");

                        } else
                            Log.e(TAG, "onResponse: Notification set as read Failed");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AllUtils.LoaderUtils.dismissLoader();
                Log.e(TAG, "onResponse: Notification set as read Failed");
            }
        });

    }
}
