package com.turnipconsultants.brongo_client.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.models.AcceptedBrokersInputModel;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.Circle;
import com.turnipconsultants.brongo_client.others.CircleAngleAnimation;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by mohit on 19-09-2017.
 */

public class BrokersMapActivity extends AppCompatActivity implements OnMapReadyCallback, NoInternetTryConnectListener {
    private static final String TAG = "BrokersMapActivity";
    private MyCountDownTimer myCountDownTimer;
    private TextView countDownTV, togo, place, cancel;
    private GoogleMap mMap;
    private Circle circle;
    private SupportMapFragment mapFragment;
    private final int COUNT_DOWN_TIME = 60000;
    private Context context;
    private SharedPreferences pref;
    private String headerToken, headerDeviceId, headerPlatform;
    private boolean isRunning = false;
    private String selectedPopularStr = "Koramangala";
    private CircleImageView broker1Img, broker2Img;
    private List<String> coordinates;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brokers_map);
        initializeViews();
        mapFragment.getMapAsync(this);

        CircleAngleAnimation animation = new CircleAngleAnimation(circle, 360);
        animation.setDuration(COUNT_DOWN_TIME);
        circle.startAnimation(animation);
        circle.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        myCountDownTimer = new MyCountDownTimer(COUNT_DOWN_TIME, 1000);
        myCountDownTimer.start();

        /*ObjectAnimator animation2 = ObjectAnimator.ofFloat(brokerImage, "rotationY", 0.0f, 360f);
        animation2.setDuration(2000);
        animation2.setInterpolator(new AccelerateDecelerateInterpolator());
        animation2.start();*/
        pref.edit().putInt("count", 0).commit();
        togo.setText("Searching");
        String temp = getIntent().getStringExtra("place");
        selectedPopularStr = temp == null ? "Koramangala" : temp;
        place.setText(selectedPopularStr + ",Bangalore");

        if (getIntent().getStringArrayListExtra("coordinates") != null) {
            coordinates = getIntent().getStringArrayListExtra("coordinates");
        } else {
            coordinates = new ArrayList<>();
            coordinates.add("12.8399");
            coordinates.add("77.6770");
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRunning) {
                    if (checkPermissionAllowed())
                        getCancelDeal();
                } else {
                    finish();
                }
            }
        });
    }

    private void initializeViews() {
        context = this;
        countDownTV = (TextView) findViewById(R.id.conutdowntv);
        circle = (Circle) findViewById(R.id.circle);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        togo = (TextView) findViewById(R.id.togo);
        place = (TextView) findViewById(R.id.locationTitle);
        cancel = (TextView) findViewById(R.id.cancel);
        broker1Img = (CircleImageView) findViewById(R.id.broker1);
        broker2Img = (CircleImageView) findViewById(R.id.broker2);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        CameraPosition cameraPosition;
        LatLng latLng = new LatLng(Double.parseDouble(coordinates.get(0)),Double.parseDouble(coordinates.get(1)));

        /*switch (selectedPopularStr) {
            *//*case "Koramangala":
                latLng = new LatLng(AppConstants.LAT_LONG.KORAMANGLA[0], AppConstants.LAT_LONG.KORAMANGLA[1]);
                break;
            case "HSR Layout":
                latLng = new LatLng(AppConstants.LAT_LONG.HSR_LAYOUT[0], AppConstants.LAT_LONG.HSR_LAYOUT[1]);
                break;
            case "Marathahalli":
                latLng = new LatLng(AppConstants.LAT_LONG.MARATHALLI[0], AppConstants.LAT_LONG.MARATHALLI[1]);
                break;
            case "Bommanahalli":
                latLng = new LatLng(AppConstants.LAT_LONG.BOMMANHALLI[0], AppConstants.LAT_LONG.BOMMANHALLI[1]);
                break;
            case "Electronic City":
                latLng = new LatLng(AppConstants.LAT_LONG.ELECTRONIC_CITY[0], AppConstants.LAT_LONG.ELECTRONIC_CITY[1]);
                break;*//*
            case "Marathahalli":
                latLng = new LatLng(AppConstants.LAT_LONG.MARATHAHALLI[0], AppConstants.LAT_LONG.MARATHAHALLI[1]);
                break;
            case "Hennur":
                latLng = new LatLng(AppConstants.LAT_LONG.HENNUR[0], AppConstants.LAT_LONG.HENNUR[1]);
                break;
            case "Whitefield":
                latLng = new LatLng(AppConstants.LAT_LONG.WHITEFIELD[0], AppConstants.LAT_LONG.WHITEFIELD[1]);
                break;
            case "Thanisandra":
                latLng = new LatLng(AppConstants.LAT_LONG.THANISANDRA[0], AppConstants.LAT_LONG.THANISANDRA[1]);
                break;
            case "Sarjapur Outer Ring Rd":
                latLng = new LatLng(AppConstants.LAT_LONG.SARJAPUR[0], AppConstants.LAT_LONG.SARJAPUR[1]);
                break;
            default:
                latLng = new LatLng(12.8399, 77.6770);
                break;
        }*/

        cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        for (int i = 0; i < 30; i++) {
            mMap.addMarker(new MarkerOptions().position(AllUtils.MapUtils.getRandomLocation(latLng, 5000)).title("").icon(BitmapDescriptorFactory.fromResource(R.drawable.broker_on_map_icon)));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mMessageReceiver, new IntentFilter(AppConstants.SPECIFIC_PUSH.ACCEPTED_BROKERS));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMessageReceiver);
    }

    private void getCancelDeal() {
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        if (InternetConnection.isNetworkAvailable(context)) {

            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<GeneralApiResponseModel> call = apiInstance.getCancleDealSearch(headerToken, headerPlatform, headerDeviceId, getStringValues());
            call.enqueue(new Callback<GeneralApiResponseModel>() {
                @Override
                public void onResponse(Call<GeneralApiResponseModel> call, Response<GeneralApiResponseModel> response) {
                    AllUtils.LoaderUtils.dismissLoader();
                    if (response != null && response.isSuccessful()) {
                        GeneralApiResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {
                            myCountDownTimer.cancel();
//                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                            showThankYouDialog(responseModel.getMessage());
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                getCancelDeal();
                            } else {
                                Toast.makeText(context, "Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                            Log.e("error", response.errorBody().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GeneralApiResponseModel> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    AllUtils.LoaderUtils.dismissLoader();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, this);
        }
    }

    private AcceptedBrokersInputModel getStringValues() {
        AcceptedBrokersInputModel model = new AcceptedBrokersInputModel();
        model.setClientMobileNo(pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""));
        model.setPostingType(pref.getString("postingType", ""));
        model.setPropertyId(pref.getString("propertyId", ""));
        return model;
    }

    private void showNoBrokerFound() {
        final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
        dialogBlock.setContentView(R.layout.no_result_found);
        dialogBlock.setCanceledOnTouchOutside(false);
        dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final TextView newSearch = dialogBlock.findViewById(R.id.newSearch);
        final Button dialogButtonOk = dialogBlock.findViewById(R.id.continuebtn);
        ImageView dialogButtonCancel = dialogBlock.findViewById(R.id.cancel);
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBlock.dismiss();
                finish();
            }
        });
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBlock.dismiss();
                finish();
            }
        });
        newSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBlock.dismiss();
                Intent intent = new Intent(context, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        dialogBlock.show();
    }

    private void showThankYouDialog(String messageStr) {
        final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
        dialogBlock.setContentView(R.layout.thankyou_dialog);
        dialogBlock.setCanceledOnTouchOutside(false);
        dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TextView message = dialogBlock.findViewById(R.id.thankyoutv);
        message.setText(messageStr);
        Button back = dialogBlock.findViewById(R.id.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(context, HomeActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent2);
                finish();
            }
        });

        ImageView cancel = dialogBlock.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBlock.dismiss();
            }
        });

        dialogBlock.show();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int count = Integer.parseInt(intent.getStringExtra("count"));
            Log.d(TAG, "New Broker Accepted, Total Count is " + count);
            String imageUrl = "";
            switch (count) {
                case 1:
                    pref.edit().putInt("count", 1).commit();
                    imageUrl = intent.getStringExtra("imageUrl");
                    togo.setText("1 broker found, 2 more to go");

                    Glide.with(context)
                            .load(imageUrl)
                            .apply(BrongoClientApplication.getRequestOptionSized(85, 85))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    broker1Img.setVisibility(View.VISIBLE);
                                    broker1Img.animate().rotationY(360f).setDuration(2000);
                                    return false;
                                }
                            })
                            .into(broker1Img);

                    break;
                case 2:
                    pref.edit().putInt("count", 2).commit();
                    imageUrl = intent.getStringExtra("imageUrl");
                    togo.setText("2 brokers found, 1 more to go");
                    Glide.with(context)
                            .load(imageUrl)
                            .apply(BrongoClientApplication.getRequestOptionSized(85, 85))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    broker2Img.setVisibility(View.VISIBLE);
                                    broker2Img.animate().rotationY(360f).setDuration(2000);
                                    return false;
                                }
                            })
                            .into(broker2Img);

                    break;
                case 3:
                    myCountDownTimer.cancel();
                    pref.edit().putInt("count", 3).commit();
                    Intent intent2 = new Intent(context, TopBrokersListActivity.class);
                    intent.putExtra("place", getIntent().getStringExtra("place"));
                    startActivity(intent2);
                    finish();
                    break;
            }
        }
    };

    @Override
    public void onTryReconnect() {
        getCancelDeal();
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            isRunning = true;
            long seconds = millisUntilFinished / 1000;
            countDownTV.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
        }

        @Override
        public void onFinish() {
            isRunning = false;
            countDownTV.setText("00:00");
            if (pref.getInt("count", 0) == 0 && !isFinishing()) {
                showNoBrokerFound();
            } else if (pref.getInt("count", 0) < 3) {
                Intent intent = new Intent(context, TopBrokersListActivity.class);
                intent.putExtra("place", getIntent().getStringExtra("place"));
                startActivity(intent);
                finish();
            }

        }
    }

    private boolean checkPermissionAllowed() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
            int callCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED && callCheck != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE}, 1);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {

                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myCountDownTimer.cancel();
    }
}
