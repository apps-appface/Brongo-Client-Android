package com.turnipconsultants.brongo_client.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.responseModels.UserExistResponseModel;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohit on 16-09-2017.
 */

public class LoginActivity extends AppCompatActivity implements NoInternetTryConnectListener {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.toolbar)
    Toolbar toolBar;

    @BindView(R.id.title)
    TextView toolBarTitle;

    @BindView(R.id.reset)
    TextView toolBarReset;

    @BindView(R.id.mobileET)
    EditText mobileET;

    private Context context;
    private SharedPreferences pref;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
        initializeViews();
        setListener();
    }

    private void initializeViews() {
        context = this;
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        toolBarTitle.setText("LOGIN");
    }

    private void setListener() {

        toolBarReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileET.setText("+91 ");
            }
        });
        mobileET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mobileET.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 14) {
                    AllUtils.KeyboardUtils.hideKeyBoard(mobileET, context);
                }
            }
        });
    }

    private void LoginCheck() {
        if (InternetConnection.isNetworkAvailable(context)) {
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<UserExistResponseModel> call = apiInstance.getLoginOtp(mobileET.getText().toString().split(" ")[1], "normal");
            call.enqueue(new Callback<UserExistResponseModel>() {
                @Override
                public void onResponse(Call<UserExistResponseModel> call, Response<UserExistResponseModel> response) {
                    AllUtils.LoaderUtils.dismissLoader();
                    if (response != null && response.isSuccessful()) {
                        UserExistResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {
                            //Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                            List<UserExistResponseModel.DataEntity> data = responseModel.getData();
                            Log.e(TAG, "OTP: " + data.get(0).getOtp());
                            pref.edit().putString(AppConstants.PREFS.USER_MOBILE_NO, mobileET.getText().toString().split(" ")[1]).commit();
                            pref.edit().putString(AppConstants.PREFS.USER_FIRST_NAME, AllUtils.StringUtilsBrongo.toCamelCase(data.get(0).getFirstName())).commit();
                            Intent intent = new Intent(context, OtpActivity.class);
                            startActivity(intent);
                            finish();
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
                public void onFailure(Call<UserExistResponseModel> call, Throwable t) {
                    AllUtils.LoaderUtils.dismissLoader();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, this);
        }
    }

    public void openOTP(View v) {
        if (checkPermissionAllowed())
            if (!mobileET.getText().toString().isEmpty() && mobileET.getText().toString().length() == 14) {
                AllUtils.KeyboardUtils.hideKeyBoard(v, context);
                LoginCheck();
            } else {
                //Toast.makeText(context, "Please Enter a Valid Mobile Number", Toast.LENGTH_LONG).show();
                mobileET.setError("Please enter a valid mobile number");
            }
    }

    private boolean checkPermissionAllowed() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
            int readSms = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED || readSms != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS}, 1);
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
//                    Toast.makeText(context, "allowed", Toast.LENGTH_LONG).show();
                    if (!mobileET.getText().toString().isEmpty() && mobileET.getText().toString().length() == 14) {
                        LoginCheck();
                    }
                }
                break;

            default:
                break;
        }
    }

    public void endActivity(View v) {
        finish();
    }

    public void hideKeyboard(View v) {
        mobileET.clearFocus();
        AllUtils.KeyboardUtils.hideKeyBoard(v, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    public void onTryReconnect() {
        LoginCheck();
    }
}
