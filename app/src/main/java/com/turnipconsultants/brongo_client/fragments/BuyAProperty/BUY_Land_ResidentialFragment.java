package com.turnipconsultants.brongo_client.fragments.BuyAProperty;

import android.Manifest;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
import com.turnipconsultants.brongo_client.Listener.CommissionListenerFactory;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.ProvinceBean;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.activities.BrokersMapActivity;
import com.turnipconsultants.brongo_client.architecture.BuyLandResViewModel;
import com.turnipconsultants.brongo_client.architecture.BuyLandViewModel;
import com.turnipconsultants.brongo_client.fragments.BaseFragment;
import com.turnipconsultants.brongo_client.models.BuyAProperty.BuyPropertyModel;
import com.turnipconsultants.brongo_client.models.GooglePlacesModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.CommissionDialogFactory;
import com.turnipconsultants.brongo_client.others.CommonApiUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.FetchMicroMarketResponse;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;
import com.turnipconsultants.brongo_client.responseModels.PropertyTransactionResponseModel;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by mohit on 16-01-2018.
 */

public class BUY_Land_ResidentialFragment extends BaseFragment implements CustomListener, NoInternetTryConnectListener, CommissionListenerFactory.BuyCommissionListener {
    private static final String TAG = BUY_Land_ResidentialFragment.class.getSimpleName();

    private static final double MIN_BUDGET = 0D;
    private static final double MAX_BUDGET = 50000000D;
    private static final double DIFF_BUDGET = 500000D;

    private static final float MIN_SQFT = 0F;
    private static final float MAX_SQFT = 20000F;
    private static final float DIFF_SQFT = 500F;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @BindView(R.id.connect_BTN)
    public Button connectBtn;

    @BindView(R.id.live_locations_FL)
    TagFlowLayout liveLocationsFL;

    @BindView(R.id.req_size_SB)
    CrystalRangeSeekbar requiredSizeSB;

    @BindView(R.id.budget_SB)
    CrystalRangeSeekbar budgetSB;

    @BindView(R.id.req_size_min_ET)
    EditText reqSizeMinET;

    @BindView(R.id.req_size_max_ET)
    EditText reqSizeMaxET;

    @BindView(R.id.budget_min_TV)
    TextView budgetMinET;

    @BindView(R.id.budget_max_TV)
    TextView budgetMaxET;

    @BindView(R.id.other_req_ET)
    EditText commentsET;

    @BindView(R.id.property_types_FL)
    TagFlowLayout propertytypesFL;

    @BindView(R.id.plot_types_FL)
    TagFlowLayout plottypesFL;

    @BindView(R.id.other_plot_ET)
    EditText otherplotET;

    @BindView(R.id.gated_ET)
    EditText gatedET;

    @BindView(R.id.housing_orientation_FL)
    TagFlowLayout housing_orientation_FL;

    @BindView(R.id.plot_TV)
    BrongoTextView plotTV;

    @BindView(R.id.selectBudgetTV)
    BrongoTextView selectBudget;

    @BindView(R.id.different_locations_TV)
    BrongoTextView diffLocation;

    private Context context;
    private String liveLocStr = "", plotETStr = "", commission = "", gatedETSTr = "", unitStr = "", orientationStr = "", propTypeStr = "", plotTypeStr = "", otherReq = "", microMarketId = "";
    private TagAdapter<String> mAdapter;
    private FetchMicroMarketResponse microMarketResponse;
    private ArrayList<String> marketName, marketId;
    private ArrayList<Integer> brokersCountList;
    private ArrayList<List<String>> coordinatesList;
    private ArrayList<String> coordinates;
    private String[] houseOrientationArray = new String[]{"East", "West", "North", "South", "North-East", "South-East", "North-West", "South-West", "Any"};
    private String[] propertyType = new String[]{"Independent Plot", "Gated community Plot"};
    private String[] plotType = new String[]{"BDA", "A khata", "B Khata", "Agricultural Land", "Industrial Land"};
    private float reqSizeMin, reqSizeMax;
    private double budgetMin, budgetMax;
    private DecimalFormat df = new DecimalFormat("#.##");
    private String headerToken, headerDeviceId, headerPlatform;
    private SharedPreferences pref;
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<ProvinceBean>> options2Items = new ArrayList<>();
    private OptionsPickerView BudgetOptions;
    private BuyLandResViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(BuyLandResViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_buy_land_residential, container, false);
        ButterKnife.bind(this, v);
        initValues();
        viewModel.getApiResponseData().observeForever(modelObserver);
        OnReset();
        setFLAdapters();
        new BudgetTask().execute();
        return v;
    }

    private void initValues() {
        context = getContext();
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        marketId = new ArrayList<>();
        marketName = new ArrayList<>();
        brokersCountList = new ArrayList<>();
        coordinatesList = new ArrayList<>();
        getMicroMarketsData();
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
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

    private void setFLAdapters() {
        final LayoutInflater mInflater = LayoutInflater.from(context);
        liveLocationsFL.setAdapter(mAdapter = new TagAdapter<String>(marketName) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, liveLocationsFL, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                liveLocStr = "";
                DecideSubmitButtonColor();
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                liveLocStr = marketName.get(position);
                coordinates = (ArrayList<String>) coordinatesList.get(position);
                microMarketId = marketId.get(position);
                DecideSubmitButtonColor();
                try {
                    connectBtn.setText("CONNECT TO THE BEST " + brokersCountList.get(position) + " LOCAL BROKERS");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        housing_orientation_FL.setAdapter(new TagAdapter<String>(houseOrientationArray) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, housing_orientation_FL, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                orientationStr = "";
                DecideSubmitButtonColor();
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                orientationStr = houseOrientationArray[position];
                DecideSubmitButtonColor();
            }
        });

        propertytypesFL.setAdapter(new TagAdapter<String>(propertyType) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, propertytypesFL, false);
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
                propTypeStr = propertyType[position];
                if (propTypeStr.equals("Independent Plot")) {
                    plottypesFL.setVisibility(View.VISIBLE);
                    otherplotET.setVisibility(View.VISIBLE);
                    gatedET.setVisibility(View.GONE);
                    plotTV.setVisibility(View.VISIBLE);
                } else if (propTypeStr.equals("Gated community Plot")) {
                    plottypesFL.setVisibility(View.GONE);
                    otherplotET.setVisibility(View.GONE);
                    gatedET.setVisibility(View.VISIBLE);
                    plotTV.setVisibility(View.GONE);
                }
                DecideSubmitButtonColor();
            }
        });

        plottypesFL.setAdapter(new TagAdapter<String>(plotType) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, plottypesFL, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                plotTypeStr = "";
                DecideSubmitButtonColor();
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                plotTypeStr = plotType[position];
                DecideSubmitButtonColor();
            }
        });

        requiredSizeSB.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                reqSizeMinET.setText(minValue + " Sqft (min)");
                reqSizeMaxET.setText(maxValue + " Sqft (max)");
                reqSizeMin = minValue.floatValue();
                reqSizeMax = maxValue.floatValue();
            }
        });

        budgetSB.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                budgetMinET.setText("\u20B9 " + Utils.Budget(df, String.valueOf(minValue)));
                budgetMaxET.setText("\u20B9 " + Utils.Budget(df, String.valueOf(maxValue)));
                budgetMin = minValue.doubleValue();
                budgetMax = maxValue.doubleValue();

                if (budgetMin >= 0 && budgetMax < 10000000) {
                    budgetSB.setSteps(500000);
                } else if (budgetMin >= 10000000 && budgetMax < 30000000) {
                    budgetSB.setSteps(1000000);
                }else if (budgetMin >= 30000000 && budgetMax <= 50000000) {
                    budgetSB.setSteps(2500000);
                }
            }
        });
    }

    private void DecideSubmitButtonColor() {
        if (!liveLocStr.isEmpty() && (!plotTypeStr.isEmpty() || !plotETStr.isEmpty()) && !orientationStr.isEmpty()) {
            connectBtn.setBackgroundColor(getResources().getColor(R.color.appPurple));
        } else {
            connectBtn.setBackgroundColor(getResources().getColor(R.color.appButton));
        }
    }

    private void getUserValues() {
        otherReq = commentsET.getText().toString() != null ? commentsET.getText().toString() : "";
        plotETStr = otherplotET.getText().toString() != null ? otherplotET.getText().toString() : "";
        gatedETSTr = gatedET.getText().toString() != null ? gatedET.getText().toString() : "";
    }

    private BuyPropertyModel getModel() {
        BuyPropertyModel model = new BuyPropertyModel();
        model.setMicroMarketId(microMarketId);
        model.setLandType(propTypeStr);
        if (propTypeStr.equals("Independent Plot")) {
            if (!plotTypeStr.isEmpty() && plotETStr.isEmpty()) {
                model.setPlotType(plotTypeStr);
            } else if (!plotETStr.isEmpty()) {
                model.setPlotType(plotETStr);
            }
        } else if (propTypeStr.equals("Gated community Plot")) {
            model.setPlotType(gatedETSTr);
        }
        model.setPlotSizeRange1(reqSizeMin + "");
        model.setPlotSizeRange2(reqSizeMax + "");
        model.setBudgetRange1(budgetMin);
        model.setBudgetRange2(budgetMax);
        model.setOrientation(orientationStr);
        model.setComments(otherReq);
        model.setClientMobileNo(pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""));
        model.setPropertyType("LAND");
        model.setSubPropertyType("RESIDENTIAL_ZONE");
        model.setPostingType("BUY");
        model.setCommission(Float.parseFloat(commission));
        return model;
    }

    @OnClick(R.id.connect_BTN)
    public void getWebServiceCall(View view) {
        if (checkPermissionAllowed())
            if (!gatedETSTr.isEmpty() || (!plotTypeStr.isEmpty() || !plotETStr.isEmpty())) {
                CommissionDialogFactory.BuyCommissionDialog(context, this);
            } else {
                Toast.makeText(context, "Please select all details", Toast.LENGTH_SHORT).show();
            }
    }


    final Observer<PropertyTransactionResponseModel> modelObserver = new Observer<PropertyTransactionResponseModel>() {
        @Override
        public void onChanged(@Nullable final PropertyTransactionResponseModel newValue) {
            if (newValue != null) {
                AllUtils.LoaderUtils.dismissLoader();
                if (newValue.getMessage().equals("Posted Successfully")) {
                    List<PropertyTransactionResponseModel.DataEntity> dataEntities = newValue.getData();
                    pref.edit().putString("postingType", dataEntities.get(0).getPostingType()).commit();
                    pref.edit().putString("propertyId", dataEntities.get(0).getPropertyId()).commit();
                    Intent intent = new Intent(getActivity(), BrokersMapActivity.class);
                    intent.putExtra("place", liveLocStr);
                    intent.putStringArrayListExtra("coordinates", coordinates);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, newValue.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                AllUtils.LoaderUtils.dismissLoader();
                Toast.makeText(context, "Please Try Again", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.getApiResponseData().removeObserver(modelObserver);
        getApiResponseModel().removeObserver(modelObserver2);

    }

    private void setRequiredSizeReset() {
        reqSizeMin = MIN_SQFT;
        reqSizeMax = MAX_SQFT;
        requiredSizeSB.setMinValue(reqSizeMin);
        requiredSizeSB.setMaxValue(reqSizeMax);
        requiredSizeSB.setSteps(DIFF_SQFT);
        requiredSizeSB.setMinStartValue(reqSizeMin);
        requiredSizeSB.setMaxStartValue(reqSizeMax).apply();
        reqSizeMinET.setText(String.valueOf(reqSizeMin) + " Sqft");
        reqSizeMaxET.setText(String.valueOf(reqSizeMax) + " Sqft");
    }

    private void SetBudgetReset() {
        budgetMin = MIN_BUDGET;
        budgetMax = MAX_BUDGET;
        budgetSB.setMinValue((float) budgetMin);
        budgetSB.setMaxValue((float) budgetMax);
        budgetSB.setSteps((float) DIFF_BUDGET);
        budgetSB.setMinStartValue((float) budgetMin);
        budgetSB.setMaxStartValue((float) budgetMax).apply();
        budgetMinET.setText("\u20B9 " + Utils.Budget(df, String.valueOf(budgetMin)));
        budgetMaxET.setText("\u20B9 " + Utils.Budget(df, String.valueOf(budgetMax)));
    }

    @OnClick({R.id.selectBudgetTV})
    public void ShowBudgetRange(View view) {
        if (BudgetOptions != null)
            BudgetOptions.show(view);
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

    @Override
    public void SetCommission(String commissionValue) {
        commission = commissionValue;
    }

    @Override
    public void Accept() {
        AllUtils.LoaderUtils.showLoader(context);
        getUserValues();
        viewModel.getCallToServer(context, headerToken, headerPlatform, headerDeviceId, getModel());
    }

    @Override
    public void onTryReconnect() {
        AllUtils.LoaderUtils.showLoader(context);
        getUserValues();
        viewModel.getCallToServer(context, headerToken, headerPlatform, headerDeviceId, getModel());
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
                    if (!liveLocStr.isEmpty() && !orientationStr.isEmpty()) {
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
                        .setLayoutRes(R.layout.budget_selection_layout, BUY_Land_ResidentialFragment.this);

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

    @Override
    public void OnReset() {
        setRequiredSizeReset();
        SetBudgetReset();
        commentsET.setText("");
        connectBtn.setText("CONNECT TO THE BEST LOCAL BROKERS");
    }

    @OnClick(R.id.different_locations_TV)
    public void showGooglePlaces() {
        try {
            getApiResponseModel().observeForever(modelObserver2);
            AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(Place.TYPE_COUNTRY)
                    .setCountry("IN")
                    .build();

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(autocompleteFilter)
                    .build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                String[] plaecsStr = place.getAddress().toString().split(",");
                GooglePlacesModel model = new GooglePlacesModel();
                model.setAddress(place.getAddress().toString());
                model.setMobileNo(pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""));
                model.setName(place.getName().toString());
                model.setCity(plaecsStr[0]);
                model.setState(plaecsStr[1]);
                headerDeviceId = Utils.getDeviceId(context);
                headerPlatform = "android";
                headerToken = pref.getString("token", "");
                AllUtils.LoaderUtils.showLoader(context);
                CommonApiUtils.getFeedBackTags(context, headerDeviceId, headerPlatform, headerToken, model, getApiResponseModel());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    private MutableLiveData<GeneralApiResponseModel> apiResponseModel;

    public MutableLiveData<GeneralApiResponseModel> getApiResponseModel() {
        if (apiResponseModel == null) {
            apiResponseModel = new MutableLiveData<>();
        }
        return apiResponseModel;
    }

    final Observer<GeneralApiResponseModel> modelObserver2 = new Observer<GeneralApiResponseModel>() {
        @Override
        public void onChanged(@Nullable final GeneralApiResponseModel newValue) {
            if (newValue != null) {
                AllUtils.LoaderUtils.dismissLoader();
                if (newValue.getStatusCode() == 200) {
//                    Toast.makeText(context, newValue.getMessage(), Toast.LENGTH_LONG).show();
                    CommissionDialogFactory.showThankYouDialog(context);
                } else {
                    Toast.makeText(context, newValue.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                AllUtils.LoaderUtils.dismissLoader();
                Toast.makeText(context, "Please Try Again", Toast.LENGTH_LONG).show();
            }
        }
    };

}
