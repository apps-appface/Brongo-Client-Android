package com.turnipconsultants.brongo_client.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.account.user.PushNotificationTask;
import com.applozic.mobicomkit.api.account.user.User;
import com.applozic.mobicomkit.api.account.user.UserLoginTask;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.models.OtpInputModel;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.responseModels.OtpResponseModel;
import com.turnipconsultants.brongo_client.responseModels.UserExistResponseModel;
import com.turnipconsultants.brongo_client.services.RegistrationIntentService;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.turnipconsultants.brongo_client.others.Constants.AppConstants.APPLOZIC_PASSWORD;

/**
 * Created by mohit on 16-09-2017.
 */

public class OtpActivity extends AppCompatActivity implements NoInternetTryConnectListener {
    private static final String TAG = "OtpActivity";
    private Context context;
    private TextView toolBarTitle, toolBarReset, timer, resendotpTV, mobileNoTV;
    private IncomingSms incomingSms;
    private IntentFilter internetFilter;
    private EditText ET1, ET2, ET3, ET4;
    private StringBuilder str;
    private RelativeLayout otpBtn;
    private String mobileStr, deviceId, platform;
    private int otpValue;
    private SharedPreferences pref;
    private MyCountDownTimer myCountDownTimer;
    private final int COUNT_DOWN_TIME = 60000;
    private UserLoginTask mAuthTask;
    private TASK selectedTask;

    private enum TASK {
        VERIFY_OTP, RESEND_OTP
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        //AllUtils.setFullScreen(this);
        initializeViews();
        toolBarTitle.setText("OTP VERIFICATION");

        internetFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        incomingSms = new IncomingSms();
        registerReceiver(incomingSms, internetFilter);

        ET1.addTextChangedListener(new GenericTextWatcher(ET1));
        ET2.addTextChangedListener(new GenericTextWatcher(ET2));
        ET3.addTextChangedListener(new GenericTextWatcher(ET3));
        ET4.addTextChangedListener(new GenericTextWatcher(ET4));

        toolBarReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ET1.setText("");
                ET2.setText("");
                ET3.setText("");
                ET4.setText("");
                ET1.requestFocus();
            }
        });

        resendotpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResendOTP();
            }
        });

        myCountDownTimer = new MyCountDownTimer(COUNT_DOWN_TIME, 1000);
        myCountDownTimer.start();
        ET1.requestFocus();
    }

    private void ResendOTP() {
        selectedTask = TASK.RESEND_OTP;
        if (InternetConnection.isNetworkAvailable(context)) {
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<UserExistResponseModel> call = apiInstance.getLoginOtp(mobileStr, "normal");
            call.enqueue(new Callback<UserExistResponseModel>() {
                @Override
                public void onResponse(Call<UserExistResponseModel> call, Response<UserExistResponseModel> response) {
                    AllUtils.LoaderUtils.dismissLoader();
                    if (response != null && response.isSuccessful()) {
                        UserExistResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {
                            resendotpTV.setVisibility(View.GONE);
                            myCountDownTimer.start();
                            ET1.setText("");
                            ET2.setText("");
                            ET3.setText("");
                            ET4.setText("");
                            ET4.clearFocus();
                            ET3.clearFocus();
                            ET2.clearFocus();
                            ET1.clearFocus();
                            ET1.requestFocus();
                            AllUtils.KeyboardUtils.ShowKeyboard(context, ET1);
                            Toast.makeText(context, "OTP Sent", Toast.LENGTH_SHORT).show();
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

    private void initializeViews() {
        context = this;
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, MODE_PRIVATE);
        toolBarTitle = (TextView) findViewById(R.id.title);
        ET1 = (EditText) findViewById(R.id.otp_ET1);
        ET2 = (EditText) findViewById(R.id.otp_ET2);
        ET3 = (EditText) findViewById(R.id.otp_ET3);
        ET4 = (EditText) findViewById(R.id.otp_ET4);
        otpBtn = (RelativeLayout) findViewById(R.id.tickRL);
        toolBarReset = (TextView) findViewById(R.id.reset);
        mobileNoTV = (TextView) findViewById(R.id.sentOtpTV);
        timer = (TextView) findViewById(R.id.timerTV);
        resendotpTV = (TextView) findViewById(R.id.resendotpTV);
        mobileStr = pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "");

        mobileNoTV.setText("We have sent you an OTP to number +91 " + mobileStr);
        resendotpTV.setVisibility(View.INVISIBLE);
    }

    public void openMain(View v) {
        str = new StringBuilder();
        str.append(ET1.getText().toString());
        str.append(ET2.getText().toString());
        str.append(ET3.getText().toString());
        str.append(ET4.getText().toString());
        if (checkPermissionAllowed())
            if (str.length() == 4) {
                getOTPVerified();
            } else {
                Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show();
            }
    }

    private void getOTPVerified() {
        selectedTask = TASK.VERIFY_OTP;
        if (InternetConnection.isNetworkAvailable(context)) {
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<OtpResponseModel> call = apiInstance.getOtpVerified(getStringValues());
            call.enqueue(new Callback<OtpResponseModel>() {
                @Override
                public void onResponse(Call<OtpResponseModel> call, Response<OtpResponseModel> response) {
                    try {
                        if (response != null && response.isSuccessful()) {
                            OtpResponseModel responseModel = response.body();
                            if (responseModel.getStatusCode() == 200) {
                                Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                                List<OtpResponseModel.DataEntity> data = responseModel.getData();

                                pref.edit().putBoolean(AppConstants.PREFS.IS_SECOND, data.get(0).isHavingLeads()).commit();
                                new Utils().getTokenRefresh(context, new TokenInputModel("android", Utils.getDeviceId(context), pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                pref.edit().putBoolean(AppConstants.PREFS.LOGGED_IN, true).commit();
                                LoginUsingApplozic();
                            } else {
                                Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (response != null && response.errorBody() != null) {
                            AllUtils.LoaderUtils.dismissLoader();
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            int statusCode = jsonObject.optInt("statusCode");
                            String message = jsonObject.optString("message");
                            if (statusCode == 417 && message.equals("Invalid OTP")) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            AllUtils.LoaderUtils.dismissLoader();
                            Toast.makeText(context, "Please Try Again After Sometime", Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<OtpResponseModel> call, Throwable t) {
                    AllUtils.LoaderUtils.dismissLoader();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, this);
        }
    }

    private OtpInputModel getStringValues() {
        String deviceID = Utils.getDeviceId(context);
        deviceId = deviceID;
        platform = "android";
        otpValue = Integer.parseInt(str.toString());
        OtpInputModel model = new OtpInputModel();
        model.setMobileNo(mobileStr);
        model.setDeviceId(deviceId);
        model.setPlatform(platform);
        model.setOtp(otpValue);
        return model;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(incomingSms, internetFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(incomingSms);
    }

    @Override
    public void onTryReconnect() {
        switch (selectedTask) {
            case RESEND_OTP: {
                ResendOTP();
                break;
            }
            case VERIFY_OTP: {
                getOTPVerified();
                break;
            }
        }
        getOTPVerified();
    }

    public class IncomingSms extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Log.e("bundle", "bundle" + bundle);
            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();
//                    String otp = message.substring(message.indexOf("is") + 3);
                        String[] otp = message.split(":");
                        Log.i("msReceiver", "senderNum: " + senderNum + "; message: " + message + "; otp" + otp[1]);
                        if (otp[1] != null && otp[1].length() == 4) {
                            ET1.setText(String.valueOf(otp[1].charAt(0)));
                            ET2.setText(String.valueOf(otp[1].charAt(1)));
                            ET3.setText(String.valueOf(otp[1].charAt(2)));
                            ET4.setText(String.valueOf(otp[1].charAt(3)));
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void LoginUsingApplozic() {
        UserLoginTask.TaskListener listener = new UserLoginTask.TaskListener() {

            @Override
            public void onSuccess(RegistrationResponse registrationResponse, final Context context) {
                mAuthTask = null;
                AllUtils.LoaderUtils.dismissLoader();

                Intent intent2 = new Intent(context, RegistrationIntentService.class);
                startService(intent2);

                if (MobiComUserPreference.getInstance(context).isRegistered()) {

                    PushNotificationTask pushNotificationTask = null;
                    PushNotificationTask.TaskListener listener = new PushNotificationTask.TaskListener() {
                        @Override
                        public void onSuccess(RegistrationResponse registrationResponse) {
                            Log.i(TAG, "onSuccess: APPLOZIC PUSH LOGIN SUCCESS");
                        }

                        @Override
                        public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                            Log.e("applozic", registrationResponse.getMessage());
                        }

                    };
                    pushNotificationTask = new PushNotificationTask(Applozic.getInstance(context).getDeviceRegistrationId(), listener, context);
                    pushNotificationTask.execute((Void) null);
                }

                Intent intent = new Intent(context, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                AllUtils.LoaderUtils.dismissLoader();
                mAuthTask = null;
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle(getString(R.string.text_alert));
                alertDialog.setMessage(exception.toString());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok_alert),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                if (!isFinishing()) {
                    alertDialog.show();
                }

            }
        };

        User user = new User();
        user.setUserId(pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""));
        user.setDisplayName(pref.getString(AppConstants.PREFS.USER_FIRST_NAME, ""));
        user.setEmail(pref.getString(AppConstants.PREFS.USER_EMAIL, "z"));
        user.setAuthenticationTypeId(User.AuthenticationType.APPLOZIC.getValue());
        user.setPassword(APPLOZIC_PASSWORD);
        user.setImageLink("");

        mAuthTask = new UserLoginTask(user, listener, this);
        mAuthTask.execute((Void) null);
    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.otp_ET1:
                    if (text.length() == 1) {
                        ET2.requestFocus();
                    }
                    break;
                case R.id.otp_ET2:
                    if (text.length() == 1) {
                        ET3.requestFocus();
                    }
                    break;
                case R.id.otp_ET3:
                    if (text.length() == 1) {
                        ET4.requestFocus();
                    }
                    break;
                case R.id.otp_ET4:
                    if (text.length() == 1)
                        AllUtils.KeyboardUtils.hideKeyBoard(view, context);
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int start, int before, int count) {
            if (count == 0) {
                switch (view.getId()) {

                    case R.id.otp_ET1:
                        ET1.requestFocus();
                        break;
                    case R.id.otp_ET2:
                        ET1.requestFocus();
                        break;
                    case R.id.otp_ET3:
                        ET2.requestFocus();
                        break;
                    case R.id.otp_ET4:
                        ET3.requestFocus();
                        break;
                }
            }
        }
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long seconds = millisUntilFinished / 1000;

            timer.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
        }

        @Override
        public void onFinish() {
            timer.setText("00:00");
            resendotpTV.setVisibility(View.VISIBLE);

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
                    if (str.length() == 4) {
                        getOTPVerified();
                    } else {
                        Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show();
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
}
