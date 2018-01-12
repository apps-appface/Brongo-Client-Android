package com.turnipconsultants.brongo_client.fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.turnipconsultants.brongo_client.Listener.FragmentBackListener;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.adapters.BrokerReviewPagerAdapter;
import com.turnipconsultants.brongo_client.models.AcceptedBrokersInputModel;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.responseModels.AcceptedBrokersResponseModel;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;
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
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.turnipconsultants.brongo_client.others.Constants.AppConstants.FRAGMENT_TAGS.CONNECTION_SUCCESS_FRAGMENT;

public class BrokerReviewFragment extends Fragment implements BrokerReviewPagerAdapter.ViewPagerOnItemClick, NoInternetTryConnectListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.bottom_TV)
    TextView back_TV;

    private FragmentManager fm;
    private BrokerReviewPagerAdapter brokerReviewPagerAdapter;
    private ArrayList<AcceptedBrokersResponseModel.Data> arrayList;
    private String headerToken, headerDeviceId, headerPlatform;
    private SharedPreferences pref;
    private Context context;
    private Unbinder unbinder;
    private FragmentBackListener backListener;
    private int selectedPosition;
    private TASK selectedTask;

    private enum TASK {
        GET_BROKER_REVIEW,
        CONNECT_WITH_BROKER
    }

    public BrokerReviewFragment() {
        // Required empty public constructor
    }

    public static BrokerReviewFragment newInstance(String param1, String param2) {
        BrokerReviewFragment fragment = new BrokerReviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_broker_review, container, false);
        unbinder = ButterKnife.bind(this, v);
        initializeViews();
        brokerReviewPagerAdapter = new BrokerReviewPagerAdapter(context, viewPager, arrayList, this);
        viewPager.setAdapter(brokerReviewPagerAdapter);
        viewPager.setClipToPadding(false);

        viewPager.setPageMargin(AllUtils.DensityUtils.dpToPx(5));
        viewPager.setOnPageChangeListener(brokerReviewPagerAdapter);
        viewPager.setCurrentItem(1, false);
        viewPager.setPadding(AllUtils.DensityUtils.dpToPx(25)
                , 0, AllUtils.DensityUtils.dpToPx(25)
                , 0);
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                page.setAlpha(normalizedposition + 0.5f);
                page.setScaleY(normalizedposition / 2 + 0.5f);

            }
        });


        if (checkPermissionAllowed())
            getBrokerDetailsReviews();
        return v;
    }

    private void initializeViews() {
        context = getActivity();
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        arrayList = new ArrayList<>();
        fm = getFragmentManager();
    }

    @OnClick(R.id.bottom_TV)
    void Back(TextView back_TV) {
        backListener.onBack();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentBackListener) {
            backListener = (FragmentBackListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        backListener = null;
    }

    private boolean checkPermissionAllowed() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    private void getBrokerDetailsReviews() {
        selectedTask = TASK.GET_BROKER_REVIEW;
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        if (InternetConnection.isNetworkAvailable(context)) {
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<AcceptedBrokersResponseModel> call = apiInstance.getAcceptedBrokerDetails(headerToken, headerPlatform, headerDeviceId, getStringValues());
            call.enqueue(new Callback<AcceptedBrokersResponseModel>() {
                @Override
                public void onResponse(Call<AcceptedBrokersResponseModel> call, Response<AcceptedBrokersResponseModel> response) {
                    AllUtils.LoaderUtils.dismissLoader();
                    if (response != null && response.isSuccessful()) {
                        AcceptedBrokersResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {
                            List<AcceptedBrokersResponseModel.Data> data = responseModel.getData();
                            arrayList.add(0, data.get(data.size() - 1));
                            arrayList.addAll(data);
                            arrayList.add(data.get(0));
                            brokerReviewPagerAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                getBrokerDetailsReviews();
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
                public void onFailure(Call<AcceptedBrokersResponseModel> call, Throwable t) {
                    AllUtils.LoaderUtils.dismissLoader();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
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

    private void getConnectedToBroker(final int position) {
        selectedTask = TASK.CONNECT_WITH_BROKER;
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        if (InternetConnection.isNetworkAvailable(context)) {
            AcceptedBrokersInputModel model = getStringValues();
            model.setBrokerMobileNo(arrayList.get(position).getMobileNo());
            model.setUserId("");
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<GeneralApiResponseModel> call = apiInstance.getConnectedBroker(headerToken, headerPlatform, headerDeviceId, model);
            call.enqueue(new Callback<GeneralApiResponseModel>() {
                @Override
                public void onResponse(Call<GeneralApiResponseModel> call, Response<GeneralApiResponseModel> response) {
                    AllUtils.LoaderUtils.dismissLoader();
                    if (response != null && response.isSuccessful()) {
                        GeneralApiResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {
                            if (responseModel.getMessage().equals("Client And Broker Connection Is Established")) {
                                pref.edit().putBoolean(AppConstants.PREFS.IS_SECOND, true).commit();
                                AcceptedBrokersResponseModel.Data dataEntity = arrayList.get(position);
                                ConnectionSuccessFragment fragment = ConnectionSuccessFragment.newInstance(dataEntity.getFirstName(), dataEntity.getMobileNo(), dataEntity.getProfileImage(), "");
                                AllUtils.FragmentUtils.addFragment(fm, R.id.fragment_container, fragment, CONNECTION_SUCCESS_FRAGMENT);
/*
                                Intent intent = new Intent(context, ConnectedBrokerActivity.class);
                                intent.putExtra("brokerMobileNo", arrayList.get(position).getMobileNo());
                                intent.putExtra("brokerImage", arrayList.get(position).getProfileImage());
                                startActivity(intent);
                                getActivity().finish();*/
                            }

                        } else {
                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                getConnectedToBroker(position);
                            } else {
                                Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            Log.e("error", response.errorBody().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GeneralApiResponseModel> call, Throwable t) {
                    AllUtils.LoaderUtils.dismissLoader();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                }
                break;

            default:
                break;
        }
    }

    @Override
    public void itemClick(int position, String mobileNo) {
        selectedPosition = position;
        getConnectedToBroker(position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    public void onTryReconnect() {
        switch (selectedTask) {
            case GET_BROKER_REVIEW: {
                getBrokerDetailsReviews();
                break;
            }
            case CONNECT_WITH_BROKER: {
                getConnectedToBroker(selectedPosition);
                break;
            }
        }
    }
}
