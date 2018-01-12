package com.turnipconsultants.brongo_client.others;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.turnipconsultants.brongo_client.ProvinceBean;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.models.BudgetRangeModel;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.responseModels.BrokersCountModel;
import com.turnipconsultants.brongo_client.responseModels.TokenResponseModel;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.turnipconsultants.brongo_client.others.Constants.AppConstants.PREFS.BROKER_COUNT;

/**
 * Created by mohit on 16-09-2017.
 */

public class Utils {
    private static final String TAG = "AllUtils";
    private String newToken;
    public static Dialog dialog;
    private SharedPreferences pref;
    public static ProgressBar progressDialog;
    private static AlertDialog alertDialog;

    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String deviceId = telephonyManager.getDeviceId();
        return deviceId;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public String getTokenRefresh(final Context context, TokenInputModel model) {
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        if (InternetConnection.isNetworkAvailable(context)) {
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<TokenResponseModel> call = apiInstance.getTokenGenerated(model);
            call.enqueue(new Callback<TokenResponseModel>() {
                @Override
                public void onResponse(Call<TokenResponseModel> call, Response<TokenResponseModel> response) {
                    if (response != null && response.isSuccessful()) {
                        TokenResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {
//                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                            List<TokenResponseModel.DataEntity> data = responseModel.getData();
                            newToken = data.get(0).getAccessToken();
                            Log.i("tokenis", newToken);
                            pref.edit().putString("token", newToken).commit();

                            Log.w("POSTMAN", " getTokenRefresh: newToken : " + newToken);

                        } else {
                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Please Try Again After Sometime", Toast.LENGTH_SHORT).show();
                        try {
                            Log.e("error", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<TokenResponseModel> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, null);
        }
        return newToken;
    }

    public static ArrayAdapter<String> getCarParkingAdapter(Activity activity, List<String> plantsList) {
        return new ArrayAdapter<String>(activity, R.layout.spinner_item, plantsList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
    }

    public static String Budget(DecimalFormat df, String amt) {
        try {
            Double amount = Double.parseDouble(amt);
            String description = "";
            if (amount > 9999999) {
                amount = amount / 10000000;
                description = "Cr";
            } else if (amount > 99999) {
                amount = amount / 100000;
                description = "L";
            } else if (amount > 999) {
                amount = amount / 1000;
                description = "K";
            }
            return df.format(amount) + "" + description;
        } catch (Exception e) {
            e.printStackTrace();
            return amt;
        }
    }

    public static void saveBrokers(Context context, String headerToken, String headerPlatform, String headerDeviceId) {

        if (InternetConnection.isNetworkAvailable(context)) {
            final SharedPreferences pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<BrokersCountModel> call = apiInstance.getBrokersCount(headerToken, headerPlatform, headerDeviceId);
            call.enqueue(new Callback<BrokersCountModel>() {
                @Override
                public void onResponse(Call<BrokersCountModel> call, Response<BrokersCountModel> response) {
                    if (response != null && response.body() != null && response.isSuccessful()) {
                        BrokersCountModel brokersCountModel = response.body();
                        List<BrokersCountModel.Data> data = brokersCountModel.getData();
                        pref.edit().putString(BROKER_COUNT, new Gson().toJson(data)).commit();
                        Log.i(TAG, "onResponse: Broker Count Saved");
                    } else {
                        Log.e(TAG, "saveBrokers: Failed");
                    }
                }

                @Override
                public void onFailure(Call<BrokersCountModel> call, Throwable t) {
                    Log.e(TAG, "saveBrokers: Failed");
                }
            });
        }
    }

    public static ArrayList<ProvinceBean> getBudgetNames(String[] buyArray) {

        ArrayList<ProvinceBean> options1Items = new ArrayList<>();
        ArrayList<String> optionList = new ArrayList<>(Arrays.asList(buyArray));
        /*for (int i = 0; i < optionList.size(); i++) {
            options1Items.add(new ProvinceBean(i, optionList.get(i), optionList.get(i), optionList.get(i)));
        }*/
        return options1Items;
    }

    public static ArrayList<ProvinceBean> getBudgetNames2(Double from, Double to, Double diff) {
        ArrayList<ProvinceBean> options1Items = new ArrayList<>();
        BudgetRangeModel budgetRangeModel = AllUtils.CalculatorUtils.getRangeSetFor(from, to, diff);
        ArrayList<Double> optionDoubleList = budgetRangeModel.getDoubleArrayList();
        ArrayList<String> optionList = budgetRangeModel.getStringArrayList();
        for (int i = 0; i < optionList.size(); i++) {
            options1Items.add(new ProvinceBean(i, optionList.get(i), optionList.get(i), optionDoubleList.get(i)));
        }
        return options1Items;
    }

    public static ArrayList<Long> getBudgetValues() {
        ArrayList<Long> arrayList = new ArrayList<>();
        arrayList.add(0L);
        arrayList.add(500000L);
        arrayList.add(1000000L);
        arrayList.add(1500000L);
        arrayList.add(2000000L);
        arrayList.add(2500000L);
        arrayList.add(3000000L);
        arrayList.add(3500000L);
        arrayList.add(4000000L);
        arrayList.add(4500000L);
        arrayList.add(5000000L);
        arrayList.add(5500000L);
        arrayList.add(6000000L);
        arrayList.add(6500000L);
        arrayList.add(7000000L);
        arrayList.add(7500000L);
        arrayList.add(8000000L);
        arrayList.add(8500000L);
        arrayList.add(9000000L);
        arrayList.add(9500000L);
        arrayList.add(10000000L);
        arrayList.add(11000000L);
        arrayList.add(12000000L);
        arrayList.add(13000000L);
        arrayList.add(14000000L);
        arrayList.add(15000000L);
        arrayList.add(16000000L);
        arrayList.add(17000000L);
        arrayList.add(18000000L);
        arrayList.add(19000000L);
        arrayList.add(20000000L);
        arrayList.add(21000000L);
        arrayList.add(22000000L);
        arrayList.add(23000000L);
        arrayList.add(24000000L);
        arrayList.add(25000000L);
        arrayList.add(27500000L);
        arrayList.add(30000000L);
        arrayList.add(32500000L);
        arrayList.add(35000000L);
        arrayList.add(37500000L);
        arrayList.add(40000000L);
        arrayList.add(42500000L);
        arrayList.add(45000000L);
        arrayList.add(47500000L);
        arrayList.add(50000000L);
        arrayList.add(50000000L);
        return arrayList;
    }
}

