package com.turnipconsultants.brongo_client.fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
import com.turnipconsultants.brongo_client.Listener.FragmentBackListener;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.activities.HomeActivity;
import com.turnipconsultants.brongo_client.adapters.TopBrokersRecycAdapter;
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

import static com.turnipconsultants.brongo_client.others.Constants.AppConstants.FRAGMENT_TAGS.BROKER_REVIEW_FRAGMENT;
import static com.turnipconsultants.brongo_client.others.Constants.AppConstants.FRAGMENT_TAGS.CONNECTION_SUCCESS_FRAGMENT;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AcceptedBrokersListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcceptedBrokersListFragment extends Fragment implements TopBrokersRecycAdapter.RecyclerChildClickListener, NoInternetTryConnectListener {
    private static final String SELECTED_LOCATION = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String selectedLocation;
    private String mParam2;


    @BindView(R.id.brokerRecycler)
    RecyclerView recyclerViewBrokers;

    private SharedPreferences pref;
    private String headerToken, headerDeviceId, headerPlatform, brokerImage;

    @BindView(R.id.connect_brongo)
    TextView connectBrongo;

    @BindView(R.id.timer)
    TextView timer;

    @BindView(R.id.cancel)
    TextView cancel;

    @BindView(R.id.locationTitle)
    TextView place;


    private Context context;
    private TopBrokersRecycAdapter adapter;
    private ArrayList<AcceptedBrokersResponseModel.Data> arrayList;
    private boolean isRunning = false;
    private Unbinder unbinder;
    private FragmentManager fm;
    private FragmentBackListener backListener;
    private int selectedChildPosition;
    private TASK selectedTask;

    public void setTimerUpdate(boolean isTimerRunning, String time) {
        isRunning = isTimerRunning;
        timer.setText(time);
    }

    private enum TASK {
        ACCEPTED_LIST,
        GET_CONNECTED,
        CANCEL_DEAL
    }

    public AcceptedBrokersListFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AcceptedBrokersListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AcceptedBrokersListFragment newInstance(String param1, String param2) {
        AcceptedBrokersListFragment fragment = new AcceptedBrokersListFragment();
        Bundle args = new Bundle();
        args.putString(SELECTED_LOCATION, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedLocation = getArguments().getString(SELECTED_LOCATION);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accepted_brokers_list, container, false);
        try {
            unbinder = ButterKnife.bind(this, view);
            initializeViews();
            place.setText(selectedLocation + ",Bangalore");
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerViewBrokers.setLayoutManager(manager);
            recyclerViewBrokers.setAdapter(adapter);


            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isRunning) {
                        if (checkPermissionAllowed())
                            CancelDealButton();
                    } else {
                        backListener.onBack();
                    }
                }
            });

            if (checkPermissionAllowed())
                getAcceptedBrokersDetails();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;

    }

    private void initializeViews() {
        context = getActivity();
        arrayList = new ArrayList();
        adapter = new TopBrokersRecycAdapter(context, arrayList, this);
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        fm = getFragmentManager();
    }

    @Override
    public void onTryReconnect() {

        switch (selectedTask) {
            case ACCEPTED_LIST: {
                getAcceptedBrokersDetails();
                break;
            }
            case CANCEL_DEAL: {
                CancelDealButton();
                break;
            }
            case GET_CONNECTED: {
                getConnectedToBroker(selectedChildPosition);
                break;
            }
        }
    }

    private void getAcceptedBrokersDetails() {
        selectedTask = TASK.ACCEPTED_LIST;
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
//                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                            brokerImage = data.get(0).getProfileImage();
                            arrayList.addAll(data);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                getAcceptedBrokersDetails();
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
        selectedTask = TASK.GET_CONNECTED;
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
                    if (response != null && response.isSuccessful()) {
                        AllUtils.LoaderUtils.dismissLoader();
                        GeneralApiResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {
                            if (responseModel.getMessage().equals("Client And Broker Connection Is Established")) {
                                pref.edit().putBoolean(AppConstants.PREFS.IS_SECOND, true).commit();
                                OpenSuccessFullyFragment(position);
                            }

                        } else {
                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AllUtils.LoaderUtils.dismissLoader();
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
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, this);
        }
    }

    private void CancelDealButton() {
        selectedTask = TASK.CANCEL_DEAL;
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
                    try {
                        AllUtils.LoaderUtils.dismissLoader();
                        if (response != null && response.isSuccessful()) {
                            GeneralApiResponseModel responseModel = response.body();
                            if (responseModel.getStatusCode() == 200) {
                                //                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                                showThankYouDialog(responseModel.getMessage());
                            }
                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                    new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                    CancelDealButton();
                                } else {
                                    Toast.makeText(context, "Please Try Again", Toast.LENGTH_SHORT).show();
                                }
                                Log.e("error", response.errorBody().string());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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

    @OnClick(R.id.connect_brongo)
    public void openConnectBrongo(BrongoTextView v) {
        showConnectBrongo();
    }

    private void showConnectBrongo() {
        final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
        dialogBlock.setContentView(R.layout.connect_to_brongo_dialog);
        dialogBlock.setCanceledOnTouchOutside(false);
        dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        ImageView back = dialogBlock.findViewById(R.id.back);
        ImageView dialogButtonCancel = dialogBlock.findViewById(R.id.cancel);
        ImageView callIm = dialogBlock.findViewById(R.id.callIM);
        ImageView emailIm = dialogBlock.findViewById(R.id.emailIM);
        final TextView callTV = dialogBlock.findViewById(R.id.mobileNo);
        final TextView emailTV = dialogBlock.findViewById(R.id.emailid);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBlock.dismiss();
            }
        });
        callIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + callTV.getText().toString()));
                if (checkPermissionAllowed()) {
                    startActivity(intent);
                }

            }
        });

        emailIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailTV.getText().toString()});
                startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
            }
        });
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBlock.dismiss();
            }
        });

        dialogBlock.show();
    }

    private void OpenSuccessFullyFragment(final int position) {
        try {
            AcceptedBrokersResponseModel.Data entity = arrayList.get(position);
            ConnectionSuccessFragment fragment = ConnectionSuccessFragment.newInstance(entity.getFirstName(), entity.getMobileNo(), entity.getProfileImage(), selectedLocation);
            AllUtils.FragmentUtils.addFragment(fm, R.id.fragment_container, fragment, CONNECTION_SUCCESS_FRAGMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                getActivity().finish();
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

    @Override
    public void onParentClick(int position) {
        BrokerReviewFragment brokerReviewFragment = BrokerReviewFragment.newInstance("", "");
        AllUtils.FragmentUtils.addFragment(fm, R.id.fragment_container, brokerReviewFragment, BROKER_REVIEW_FRAGMENT);
        /*Intent intent = new Intent(context, BrokerReviewActivity.class);
        startActivity(intent);*/
    }

    @Override
    public void onChildClick(int position, String brokerMobile) {
        selectedChildPosition = position;
        if (checkPermissionAllowed())
            getConnectedToBroker(position);
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
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

}
