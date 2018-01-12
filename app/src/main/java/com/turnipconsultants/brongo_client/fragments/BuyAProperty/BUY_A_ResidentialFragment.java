package com.turnipconsultants.brongo_client.fragments.BuyAProperty;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.Listener.CommissionListenerFactory;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.ProvinceBean;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.activities.BrokersMapActivity;
import com.turnipconsultants.brongo_client.fragments.BaseFragment;
import com.turnipconsultants.brongo_client.models.BuyAProperty.BuyPropertyModel;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.mvp.HomePresenterImpl;
import com.turnipconsultants.brongo_client.mvp.MicroMarketsView;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.responseModels.BrokersCountModel;
import com.turnipconsultants.brongo_client.responseModels.FetchMicroMarketResponse;
import com.turnipconsultants.brongo_client.responseModels.PropertyTransactionResponseModel;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.CommissionDialogFactory;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.turnipconsultants.brongo_client.others.Constants.AppConstants.POPULAR_LOCATIONS.BANGALORE;

/**
 * Created by mohit on 18-09-2017.
 */

public class BUY_A_ResidentialFragment extends BaseFragment implements CommissionListenerFactory.BuyCommissionListener, NoInternetTryConnectListener, CustomListener {

    private static final String TAG = "BUY_A_ResidentialFragme";
    private static final double MIN_BUDGET = 2500000D;
    private static final double MAX_BUDGET = 250000000D;
    private static final double DIFF_BUDGET = 500000D;

    @BindView(R.id.popular_locations_FL)
    TagFlowLayout popularLocationsFL;

    @BindView(R.id.property_types_FL)
    TagFlowLayout propertyTypesFL;

    @BindView(R.id.property_status_FL)
    TagFlowLayout propertyStatusFL;

    @BindView(R.id.bedroom_FL)
    TagFlowLayout bedroomsFL;

    @BindView(R.id.budget_min_TV)
    TextView budgetMinTV;

    @BindView(R.id.budget_max_TV)
    TextView budgetMaxTV;

    @BindView(R.id.selectBudgetTV)
    TextView selectBudgetTV;

    @BindView(R.id.other_req_ET)
    EditText commentsET;

    @BindView(R.id.budget_SB)
    CrystalRangeSeekbar budgetSB;

    @BindView(R.id.connect_BTN)
    Button connectBrokersBTN;

    private TagAdapter<String> mAdapter;
    private String[] popularLoc = BANGALORE;
    private String[] propTypes = new String[]{"Apartment/Flat", "Row House", "Penthouse", "Villa", "PG/Hostel", "Independent house"};
    private String[] propStatus = new String[]{"Pre launch", "Under Construction", "Ready to move-in(new)", "Resale"};
    private String[] bedroom = new String[]{"1 BHK", "2 BHK", "3 BHK", "4 BHK", "4 BHK+"};
    private Context context;
    private String headerToken, headerDeviceId, headerPlatform;
    private SharedPreferences pref;
    private String popLocStr = "", propTypeStr = "", propStatusStr = "", bedroomStr = "", mobileStr, commission = "", microMarketId = "";
    private double budgetMin, budgetMax;
    private DecimalFormat df = new DecimalFormat("#.##");
    private String brokerCountJSON;
    private Gson gson;
    private List<BrokersCountModel.Data> arrayList;
    private Unbinder unbinder;
    private OptionsPickerView BudgetOptions;

    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<ProvinceBean>> options2Items = new ArrayList<>();

    //    private HomePresenterImpl homePresenter;
    private FetchMicroMarketResponse microMarketResponse;
    private ArrayList<String> marketName, marketId;
    private ArrayList<Integer> brokersCountList;
    private ArrayList<List<String>> coordinatesList;
    private ArrayList<String> coordinates;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = null;
        try {
            v = inflater.inflate(R.layout.buy_a_residential_fragment, container, false);
            unbinder = ButterKnife.bind(this, v);
            initPage();
            setValues();
            OnReset();
            setListener();
            new BudgetTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    private void initPage() {
        context = getActivity();
        gson = new Gson();
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        mobileStr = pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "");
        brokerCountJSON = pref.getString(AppConstants.PREFS.BROKER_COUNT, "");

        Type type = new TypeToken<List<BrokersCountModel.Data>>() {
        }.getType();
        arrayList = gson.fromJson(brokerCountJSON, type);

        marketId = new ArrayList<>();
        marketName = new ArrayList<>();
        brokersCountList = new ArrayList<>();
        coordinatesList = new ArrayList<>();
        getMicroMarketsData();
    }

    private void getMicroMarketsData() {
        BrongoClientApplication app = (BrongoClientApplication) context.getApplicationContext();
        microMarketResponse = app.getMicroMarketResponse();
        List<FetchMicroMarketResponse.DataEntity> dataEntityList = microMarketResponse.getData();
        for (int i = 0; i < dataEntityList.size(); i++) {
            if (dataEntityList.get(i).isTrending()) {
                marketName.add(dataEntityList.get(i).getName());
                marketId.add(dataEntityList.get(i).getMicroMarketId());
                brokersCountList.add(dataEntityList.get(i).getBrokersCount());
                coordinatesList.add(dataEntityList.get(i).getMarketLocation().getCoordinates());
            }
        }
    }

    private void setValues() {
        final LayoutInflater mInflater = LayoutInflater.from(context);
        popularLocationsFL.setAdapter(mAdapter = new TagAdapter<String>(marketName) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, popularLocationsFL, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                popLocStr = "";
                DecideSubmitButtonColor();
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
//                popLocStr = popularLoc[position];
                popLocStr = marketName.get(position);
                coordinates = (ArrayList<String>) coordinatesList.get(position);
                microMarketId = marketId.get(position);
                DecideSubmitButtonColor();
                try {
                    connectBrokersBTN.setText("CONNECT TO THE BEST " + brokersCountList.get(position) + " LOCAL BROKERS");
                    /*for (BrokersCountModel.Data d : arrayList) {
                        if (d.getMicroMarketName().equalsIgnoreCase(popLocStr)) {
                            connectBrokersBTN.setText("CONNECT TO THE BEST " + d.getCount() + " LOCAL BROKERS");
                            break;
                        }
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        propertyTypesFL.setAdapter(mAdapter = new TagAdapter<String>(propTypes) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, propertyTypesFL, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                propTypeStr = "";
                DecideSubmitButtonColor();
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                propTypeStr = propTypes[position];
                DecideSubmitButtonColor();
            }
        });

        propertyStatusFL.setAdapter(mAdapter = new TagAdapter<String>(propStatus) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, propertyStatusFL, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                propStatusStr = "";
                DecideSubmitButtonColor();
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                propStatusStr = propStatus[position];
                DecideSubmitButtonColor();
            }
        });

        bedroomsFL.setAdapter(mAdapter = new TagAdapter<String>(bedroom) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, bedroomsFL, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                bedroomStr = "";
                DecideSubmitButtonColor();
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                bedroomStr = bedroom[position];
                DecideSubmitButtonColor();
            }
        });

    }

    @OnClick({R.id.selectBudgetTV})
    public void ShowBudgetRange(View view) {
        if (BudgetOptions != null)
            BudgetOptions.show(view);
    }

    private void setListener() {
        budgetSB.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                budgetMinTV.setText("\u20B9 " + Utils.Budget(df, String.valueOf(minValue)));
                budgetMaxTV.setText("\u20B9 " + Utils.Budget(df, String.valueOf(maxValue)));
                budgetMin = minValue.doubleValue();
                budgetMax = maxValue.doubleValue();
            }
        });
    }

    @OnClick(R.id.connect_BTN)
    public void SearchBrokers(Button button) {
        if (checkPermissionAllowed())
            if (!popLocStr.isEmpty() && !propTypeStr.isEmpty() && !propStatusStr.isEmpty() && !bedroomStr.isEmpty()) {
                CommissionDialogFactory.BuyCommissionDialog(context, this);
            } else {
                Toast.makeText(context, "Please select all details", Toast.LENGTH_SHORT).show();
            }

    }

    private void BuyProperty() {
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        if (InternetConnection.isNetworkAvailable(context)) {
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<PropertyTransactionResponseModel> call = apiInstance.getBuyAProperty(headerToken, headerPlatform, headerDeviceId, getStringValues());
            call.enqueue(new Callback<PropertyTransactionResponseModel>() {
                @Override
                public void onResponse(Call<PropertyTransactionResponseModel> call, Response<PropertyTransactionResponseModel> response) {
                    AllUtils.LoaderUtils.dismissLoader();
                    if (response != null && response.isSuccessful()) {
                        PropertyTransactionResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {
                            List<PropertyTransactionResponseModel.DataEntity> data = responseModel.getData();
                            if (responseModel.getMessage().equals("Posted Successfully")) {
                                pref.edit().putString("postingType", data.get(0).getPostingType()).commit();
                                pref.edit().putString("propertyId", data.get(0).getPropertyId()).commit();
                                Intent intent = new Intent(context, BrokersMapActivity.class);
                                intent.putExtra("place", popLocStr);
                                intent.putStringArrayListExtra("coordinates", coordinates);
                                startActivity(intent);
                            } else {
                                Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                BuyProperty();
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
                public void onFailure(Call<PropertyTransactionResponseModel> call, Throwable t) {
                    AllUtils.LoaderUtils.dismissLoader();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, this);
        }
    }

    private BuyPropertyModel getStringValues() {

        BuyPropertyModel p = new BuyPropertyModel();
        p.setPostingType(AppConstants.POSTING_TYPE.BUY_A_PROPERTY);
        /*BuyPropertyModel.Area area1 = new BuyPropertyModel.Area();
        area1.setMicroMarketCity("Bangalore");
        area1.setMicroMarketState("Karnataka");
        area1.setMicroMarketName(popLocStr);
        p.setArea(area1);*/
        p.setMicroMarketId(microMarketId);
        p.setPropertyType("RESIDENTIAL");
        p.setSubPropertyType(propTypeStr);
        p.setPropertyStatus(propStatusStr);
        p.setBedRoomType(bedroomStr);
        p.setBudgetRange1(budgetMin);
        p.setBudgetRange2(budgetMax);
        p.setComments(commentsET.getText().toString());
        p.setClientMobileNo(mobileStr);
        p.setCommission(Float.parseFloat(commission));

        return p;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if (!popLocStr.isEmpty() && !propTypeStr.isEmpty() && !propStatusStr.isEmpty() && !bedroomStr.isEmpty()) {
                        CommissionDialogFactory.BuyCommissionDialog(context, this);
                    } else {
                        Toast.makeText(context, "Please select all details", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    private void DecideSubmitButtonColor() {
        if (!bedroomStr.isEmpty() && !propStatusStr.isEmpty() && !propTypeStr.isEmpty() && !popLocStr.isEmpty()) {
            connectBrokersBTN.setBackgroundColor(getResources().getColor(R.color.appPurple));
        } else {
            connectBrokersBTN.setBackgroundColor(getResources().getColor(R.color.appButton));
        }
    }

    @Override
    public void SetCommission(String commissionValue) {
        commission = commissionValue;
    }

    @Override
    public void Accept() {
        Log.i(TAG, "SetCommission: " + commission);
        BuyProperty();
    }

    @Override
    public void onTryReconnect() {
        BuyProperty();
    }

    @Override
    public void OnReset() {
        popLocStr = "";
        propStatusStr = "";
        propStatusStr = "";
        bedroomStr = "";
        popularLocationsFL.getAdapter().notifyDataChanged();
        propertyTypesFL.getAdapter().notifyDataChanged();
        propertyStatusFL.getAdapter().notifyDataChanged();
        bedroomsFL.getAdapter().notifyDataChanged();

        SetBudgetReset();
        commentsET.setText("");
        connectBrokersBTN.setText("CONNECT TO THE BEST LOCAL BROKERS");
        DecideSubmitButtonColor();
    }

    private void SetBudgetReset() {
        budgetMin = MIN_BUDGET;
        budgetMax = MAX_BUDGET;
        budgetMinTV.setText("\u20B9 " + Utils.Budget(df, String.valueOf(budgetMin)));
        budgetMaxTV.setText("\u20B9 " + Utils.Budget(df, String.valueOf(budgetMax)));
        budgetSB.setDataType(CrystalRangeSeekbar.DataType.DOUBLE);
        budgetSB.setGap((float) DIFF_BUDGET);
        budgetSB.setMinValue((float) budgetMin);
        budgetSB.setMaxValue((float) budgetMax);
        budgetSB.setMinStartValue((float) budgetMin);
        budgetSB.setMaxStartValue((float) budgetMax).apply();

    }

    @Override
    public void customLayout(View v) {
        Log.i(TAG, "customLayout: RECEIVED");
        v.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BudgetOptions.dismiss();
            }
        });
        v.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BudgetOptions.dismiss();
                BudgetOptions.returnData();
            }
        });
    }

    /*@Override
    public void setMicroMarkets(FetchMicroMarketResponse obj, boolean isSuccess) {
        if (isSuccess) {
            List<FetchMicroMarketResponse.DataEntity> dataEntityList = obj.getData();
            for (int i = 0; i < dataEntityList.size(); i++) {
                if (dataEntityList.get(i).isTrending()) {
                    marketName.add(dataEntityList.get(i).getName());
                    marketId.add(dataEntityList.get(i).getMicroMarketId());
                    brokersCountList.add(dataEntityList.get(i).getBrokersCount());
                }
            }
        }
    }*/


    private class BudgetTask extends AsyncTask {
        private OptionsPickerView.Builder builder;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute: ");
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                options1Items.clear();
                options2Items.clear();
                options1Items.addAll(Utils.getBudgetNames2(MIN_BUDGET, MAX_BUDGET, DIFF_BUDGET));

                for (int i = 0; i < options1Items.size(); i++) {
                    ArrayList<ProvinceBean> temp = new ArrayList<>();
                    for (int j = i; j < options1Items.size(); j++) {
                        temp.add(options1Items.get(j));
                    }
                    options2Items.add(temp);
                }
                builder = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        try {
                            Double min = options1Items.get(options1).getAmount(), max = options2Items.get(options1).get(options2).getAmount();
                            Log.i(TAG, "onOptionsSelect: MIN " + min + " MAX: " + max);
                            budgetSB.setMinStartValue(Float.valueOf(min + ""));
                            budgetSB.setMaxStartValue(Float.valueOf(max + "")).apply();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                        .setLayoutRes(R.layout.budget_selection_layout, BUY_A_ResidentialFragment.this);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            BudgetOptions = builder.build();
            BudgetOptions.setPicker(options1Items, options2Items);
            Log.i(TAG, "onPostExecute: ");
        }
    }
}
