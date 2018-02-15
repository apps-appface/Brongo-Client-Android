package com.turnipconsultants.brongo_client.fragments.RentAProperty;

import android.Manifest;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.CustomWidgets.BrongoButton;
import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
import com.turnipconsultants.brongo_client.Listener.CommissionListenerFactory;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.ProvinceBean;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.activities.BrokersMapActivity;
import com.turnipconsultants.brongo_client.fragments.BaseFragment;
import com.turnipconsultants.brongo_client.models.GooglePlacesModel;
import com.turnipconsultants.brongo_client.models.RentAProperty.RentAPropertyModel;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.CommissionDialogFactory;
import com.turnipconsultants.brongo_client.others.CommonApiUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.BrokersCountModel;
import com.turnipconsultants.brongo_client.responseModels.FetchMicroMarketResponse;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;
import com.turnipconsultants.brongo_client.responseModels.PropertyTransactionResponseModel;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.turnipconsultants.brongo_client.others.Constants.AppConstants.POPULAR_LOCATIONS.BANGALORE;

/**
 * Created by mohit on 18-09-2017.
 */

public class RENT_A_ResidentialFragment extends BaseFragment implements CommissionListenerFactory.RentCommissionListener, NoInternetTryConnectListener, CustomListener {
    private static final String TAG = "RENT_A_ResidentialFragm";
    private static final double MIN_BUDGET = 0D;
    private static final double MAX_BUDGET = 50000000D;
    private static final double DIFF_BUDGET = 500000D;

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;


    @BindView(R.id.connect_BTN)
    BrongoButton connectBrokersBTN;

    @BindView(R.id.popular_locations_FL)
    TagFlowLayout popularLocationsFL;

    @BindView(R.id.property_types_FL)
    TagFlowLayout propertyTypesFL;

    @BindView(R.id.bedroom_FL)
    TagFlowLayout bedroomFL;

    @BindView(R.id.budget_SB)
    CrystalRangeSeekbar budgetSB;

    @BindView(R.id.budget_min_TV)
    TextView budgetMinTV;

    @BindView(R.id.budget_max_TV)
    TextView budgetMaxTV;

    @BindView(R.id.selectBudgetTV)
    TextView selectBudgetTV;

    @BindView(R.id.tenant_type_FL)
    TagFlowLayout tenantTypeFL;

    @BindView(R.id.preferred_project_ET)
    EditText preferredProjectET;

    @BindView(R.id.avoid_project_ET)
    EditText avoidProjectET;

    @BindView(R.id.other_req_ET)
    EditText commentsET;

    @BindView(R.id.housing_orientation_FL)
    TagFlowLayout housingOrientationFL;

    @BindView(R.id.furnishing_FL)
    TagFlowLayout furnishingFL;

    @BindView(R.id.different_locations_TV)
    BrongoTextView diffLocation;

    Unbinder unbinder;

    private String[] popularLocArray = BANGALORE;
    private String[] propTypesArray = new String[]{"Apartment/Flat", "Row House", "Penthouse", "Villa", "PG/Hostel", "Independent house","Any"};
    private String[] bedroomArray = new String[]{"1 BHK", "2 BHK", "3 BHK", "4 BHK", "4 BHK+"};
    private String[] tenantTypeArray = new String[]{"Bachelor", "Family", "Any"};
    private String[] furnishingArray = new String[]{"Fully Furnished", "Semi Furnished", "Unfurnished", "Any"};
    private String[] housingOrientationArray = new String[]{"East", "West", "North", "South", "North-East", "South-East", "North-West", "South-West", "Any"};
    private Context context;
    private String headerToken, headerDeviceId, headerPlatform;
    private SharedPreferences pref;
    private String popLocStr = "", propTypeStr = "", bedroomStr = "", tenantTypeStr = "", furnishedStr = "", housingOrientationStr = "",microMarketId="";
    private double budgetMin, budgetMax;
    private DecimalFormat df = new DecimalFormat("#.##");
    private int commission;

    private String brokerCountJSON;
    private Gson gson;
    List<BrokersCountModel.Data> arrayList;

    private OptionsPickerView BudgetOptions;

    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<ProvinceBean>> options2Items = new ArrayList<>();

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
            v = inflater.inflate(R.layout.rent_a_residential_fragment, container, false);
            unbinder = ButterKnife.bind(this, v);
            InitPage();
            SetValues();
            OnReset();
            SetListener();
            new BudgetTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    private void SetListener() {
        budgetSB.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                budgetMinTV.setText("\u20B9 " + Utils.Budget(df, String.valueOf(minValue)));
                budgetMaxTV.setText("\u20B9 " + Utils.Budget(df, String.valueOf(maxValue)));
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

    private void SetValues() {
        final LayoutInflater mInflater = LayoutInflater.from(getActivity());
        popularLocationsFL.setAdapter(new TagAdapter<String>(marketName) {

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
//                popLocStr = popularLocArray[position];
                popLocStr = marketName.get(position);
                coordinates = (ArrayList<String>) coordinatesList.get(position);
                microMarketId = marketId.get(position);
                DecideSubmitButtonColor();
                try {
                    connectBrokersBTN.setText("CONNECT TO THE BEST " + brokersCountList.get(position) + " LOCAL BROKERS");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        propertyTypesFL.setAdapter(new TagAdapter<String>(propTypesArray) {

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
                propTypeStr = propTypesArray[position];
                DecideSubmitButtonColor();
            }
        });

        bedroomFL.setAdapter(new TagAdapter<String>(bedroomArray) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, bedroomFL, false);
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
                bedroomStr = bedroomArray[position];
                DecideSubmitButtonColor();
            }
        });

        tenantTypeFL.setAdapter(new TagAdapter<String>(tenantTypeArray) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, tenantTypeFL, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                tenantTypeStr = "";
                DecideSubmitButtonColor();
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                tenantTypeStr = tenantTypeArray[position];
                DecideSubmitButtonColor();
            }
        });

        housingOrientationFL.setAdapter(new TagAdapter<String>(housingOrientationArray) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, housingOrientationFL, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                housingOrientationStr = "";
                DecideSubmitButtonColor();
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                housingOrientationStr = housingOrientationArray[position];
                DecideSubmitButtonColor();
            }
        });

        furnishingFL.setAdapter(new TagAdapter<String>(furnishingArray) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, furnishingFL, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                furnishedStr = "";
                DecideSubmitButtonColor();
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                furnishedStr = furnishingArray[position];
                DecideSubmitButtonColor();
            }
        });

    }

    private void InitPage() {
        context = getActivity();
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        gson = new Gson();
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

    @OnClick({R.id.selectBudgetTV})
    public void ShowBudgetRange(View view) {
        BudgetOptions.show(view);
    }

    private void RentProperty() {
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        Log.e("token", headerToken);
        if (InternetConnection.isNetworkAvailable(context)) {
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<PropertyTransactionResponseModel> call = apiInstance.getRentAProperty(headerToken, headerPlatform, headerDeviceId, getStringValues());
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
                                Intent intent = new Intent(getActivity(), BrokersMapActivity.class);
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
                                RentProperty();
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

    private RentAPropertyModel getStringValues() {

        RentAPropertyModel p = new RentAPropertyModel();
        p.setPropertyType("RESIDENTIAL");
        p.setPostingType(AppConstants.POSTING_TYPE.RENT_A_PROPERTY);
        p.setClientMobileNo(pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""));
        /*RentAPropertyModel.Area area = new RentAPropertyModel.Area();
        area.setMicroMarketCity("Bangalore");
        area.setMicroMarketState("Karnataka");
        area.setMicroMarketName(popLocStr);
        p.setArea(area);*/
        p.setMicroMarketId(microMarketId);
        p.setSubPropertyType(propTypeStr);
        p.setBedRoomType(bedroomStr);
//        p.setBudget(budgetMax + "");
        p.setMaritalStatus(tenantTypeStr);
        p.setPreferredProjects(preferredProjectET.getText().toString());
        p.setAvoidProjects(avoidProjectET.getText().toString());
        p.setComments(commentsET.getText().toString());
        p.setBudgetRange1(budgetMin);
        p.setBudgetRange2(budgetMax);
        p.setFurnishingStatus(furnishedStr);
        p.setOrientation(housingOrientationStr);
        p.setPropertyStatus("Ready-to-move-in");
        p.setCommission(commission);

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
                    if (!popLocStr.isEmpty() && !propTypeStr.isEmpty() && !bedroomStr.isEmpty() && !tenantTypeStr.isEmpty() && !furnishedStr.isEmpty() && !housingOrientationStr.isEmpty()) {
                        CommissionDialogFactory.RentCommissionDialog(context, this);
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

    private void DecideSubmitButtonColor() {
        if (!popLocStr.isEmpty() && !propTypeStr.isEmpty() && !bedroomStr.isEmpty() && !tenantTypeStr.isEmpty() && !furnishedStr.isEmpty() && !housingOrientationStr.isEmpty()) {
            connectBrokersBTN.setBackgroundColor(getResources().getColor(R.color.appPurple));
        } else {
            connectBrokersBTN.setBackgroundColor(getResources().getColor(R.color.appButton));
        }
    }

    @Override
    public void SetAdvanceRentMonths(String commissionValue) {
        commission = Integer.parseInt(commissionValue);
    }

    @Override
    public void Accept() {
        Log.i(TAG, "Accept: SetAdvanceRentMonths " + commission);
        RentProperty();
    }

    @Override
    public void onTryReconnect() {
        RentProperty();
    }

    @Override
    public void OnReset() {
        popLocStr = "";
        popularLocationsFL.getAdapter().notifyDataChanged();

        propTypeStr = "";
        propertyTypesFL.getAdapter().notifyDataChanged();

        bedroomStr = "";
        bedroomFL.getAdapter().notifyDataChanged();

        tenantTypeStr = "";
        tenantTypeFL.getAdapter().notifyDataChanged();

        preferredProjectET.setText("");
        avoidProjectET.setText("");

        housingOrientationStr = "";
        housingOrientationFL.getAdapter().notifyDataChanged();

        furnishedStr = "";
        furnishingFL.getAdapter().notifyDataChanged();

        commentsET.setText("");

        tenantTypeStr = tenantTypeArray[2];
        housingOrientationStr = housingOrientationArray[8];
        furnishedStr = furnishingArray[3];
        connectBrokersBTN.setText("CONNECT TO THE BEST LOCAL BROKERS");
        SetBudgetReset();

        /*By Default Selected Values*/
        tenantTypeFL.getAdapter().setSelectedList(2);
        housingOrientationFL.getAdapter().setSelectedList(8);
        furnishingFL.getAdapter().setSelectedList(3);
        tenantTypeStr = tenantTypeArray[2];
        housingOrientationStr = housingOrientationArray[8];
        furnishedStr = furnishingArray[3];

        DecideSubmitButtonColor();
    }

    private void SetBudgetReset() {
        budgetMin = MIN_BUDGET;
        budgetMax = MAX_BUDGET;
        budgetSB.setMinValue((float) budgetMin);
        budgetSB.setMaxValue((float) budgetMax);
        budgetSB.setSteps((float) DIFF_BUDGET);
        budgetSB.setMinStartValue((float) budgetMin);
        budgetSB.setMaxStartValue((float) budgetMax).apply();
        budgetMinTV.setText("\u20B9 " + Utils.Budget(df, String.valueOf(budgetMin)));
        budgetMaxTV.setText("\u20B9 " + Utils.Budget(df, String.valueOf(budgetMax)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.connect_BTN)
    public void onSubmitClick() {
        if (checkPermissionAllowed())
            if (!popLocStr.isEmpty() && !propTypeStr.isEmpty() && !bedroomStr.isEmpty() && !tenantTypeStr.isEmpty() && !furnishedStr.isEmpty()) {
                CommissionDialogFactory.RentCommissionDialog(context, this);
            } else {
                Toast.makeText(context, "Please select all details", Toast.LENGTH_SHORT).show();
            }
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
                        .setLayoutRes(R.layout.budget_selection_layout, RENT_A_ResidentialFragment.this);

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

    @OnClick(R.id.different_locations_TV)
    public void showGooglePlaces() {
        try {
            getApiResponseModel().observeForever(modelObserver);
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

    final Observer<GeneralApiResponseModel> modelObserver = new Observer<GeneralApiResponseModel>() {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        getApiResponseModel().removeObserver(modelObserver);
    }
}
