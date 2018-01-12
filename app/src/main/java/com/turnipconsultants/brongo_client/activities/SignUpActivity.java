package com.turnipconsultants.brongo_client.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.account.user.PushNotificationTask;
import com.applozic.mobicomkit.api.account.user.User;
import com.applozic.mobicomkit.api.account.user.UserLoginTask;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.common.StringUtils;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.models.SignUpInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.responseModels.SignUpResponseModel;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by mohit on 15-09-2017.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, NoInternetTryConnectListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignUpActivity";
    private Context context;
    private Toolbar toolBar;
    private TextView toolBarTitle, toolBarReset, privacyPolicy;
    private EditText nameET, mobileET, emailET;
    private Button otpBtn;
    private String nameStr, emailStr, mobileStr, platform, loginType, deviceId;
    private SharedPreferences pref;
    private UserLoginTask mAuthTask;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;

    private String fbFName, fbLName, fbEmail, fbMobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initializeViews();
        toolBarTitle.setText("SIGN UP");
        AllUtils.KeyboardUtils.hideKeyBoard(this);

        otpBtn.setOnClickListener(this);
        privacyPolicy.setOnClickListener(this);
    }

    private void initializeViews() {
        context = this;
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBarTitle = (TextView) findViewById(R.id.title);
        toolBarReset = (TextView) findViewById(R.id.reset);
        privacyPolicy = (TextView) findViewById(R.id.policy2);
        nameET = (EditText) findViewById(R.id.nameET);
        mobileET = (EditText) findViewById(R.id.mobileET);
        emailET = (EditText) findViewById(R.id.emailIdET);
        otpBtn = (Button) findViewById(R.id.generateOtpBtn);
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fbFName = bundle.getString(AppConstants.FB_USER.FIRST_NAME, "");
        fbLName = bundle.getString(AppConstants.FB_USER.LAST_NAME, "");
        fbEmail = bundle.getString(AppConstants.FB_USER.EMAIL, "");
        fbMobile = bundle.getString(AppConstants.FB_USER.MOBILE, "");

        nameET.setText(fbFName + " " + fbLName);
        emailET.setText(fbEmail);
        mobileET.setText(fbMobile);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.generateOtpBtn:
                if (checkPermissionAllowed()) {
                    if (!nameET.getText().toString().isEmpty() && !mobileET.getText().toString().isEmpty() && mobileET.getText().toString().length() == 10 && Utils.isValidEmail(emailET.getText().toString())) {
                        getUserSignUp();
                    } else {
                        Toast.makeText(context, "Name, Mobile Number cannot be empty and Email should be valid", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.policy2:
                break;
            case R.id.reset:
                nameET.setText("");
                mobileET.setText("");
                emailET.setText("");
                break;
        }
    }

    private void getUserSignUp() {
        if (InternetConnection.isNetworkAvailable(context)) {
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<SignUpResponseModel> call = apiInstance.getUserSignUp(getStringValues());
            call.enqueue(new Callback<SignUpResponseModel>() {
                @Override
                public void onResponse(Call<SignUpResponseModel> call, Response<SignUpResponseModel> response) {
                    try {
                        AllUtils.LoaderUtils.dismissLoader();
                        if (response != null && response.isSuccessful()) {
                            SignUpResponseModel responseModel = response.body();
                            if (responseModel.getStatusCode() == 200 && responseModel.getMessage().equals("Client Created Successfully.OTP Sent To Your Mobile Number Please Check")) {
                                Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                                List<SignUpResponseModel.DataEntity> data = responseModel.getData();
                                Log.e(TAG, "onResponse: OTP " + data.get(0).getOtp());
                                SaveValuesToPrefs();
                                RegisterOnApplozic();
                            } else if (responseModel.getMessage().equals("Client Updated Successfully")) {
                                Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT).show();
                            }
                        } else {
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
                public void onFailure(Call<SignUpResponseModel> call, Throwable t) {
                    AllUtils.LoaderUtils.dismissLoader();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, this);
        }

    }

    private void SaveValuesToPrefs() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(AppConstants.PREFS.USER_MOBILE_NO, mobileStr);
        editor.putString(AppConstants.PREFS.USER_EMAIL, emailStr);

        if (nameStr.contains(" ")) {
            String[] name = nameStr.split(" ");
            editor.putString(AppConstants.PREFS.USER_FIRST_NAME, name[1]);
            editor.putString(AppConstants.PREFS.USER_LAST_NAME, name[2]);
        } else {
            editor.putString(AppConstants.PREFS.USER_FIRST_NAME, nameStr);
        }
        editor.commit();
    }

    private SignUpInputModel getStringValues() {
        SignUpInputModel signUpInputModel = new SignUpInputModel();
        nameStr = nameET.getText().toString();
        nameStr = nameStr.replaceFirst("^\\s*", "");
        emailStr = emailET.getText().toString();
        mobileStr = mobileET.getText().toString();

        platform = "android";
        if (nameStr.contains(" ")) {

            /*String[] nameSplit = nameStr.split(" ");
            signUpInputModel.setFirstName(nameSplit[0]);
            signUpInputModel.setLastName(nameSplit[1]);*/

            int start = nameStr.indexOf(' ');
            int end = nameStr.lastIndexOf(' ');

            String firstName = "";
            String middleName = "";
            String lastName = "";

            if (start >= 0) {
                firstName = nameStr.substring(0, start);
                if (end > start)
                    middleName = nameStr.substring(start + 1, end);
                lastName = nameStr.substring(end + 1, nameStr.length());
            }

            signUpInputModel.setFirstName(firstName);
            signUpInputModel.setLastName(middleName + " " + lastName);
        } else {
            signUpInputModel.setFirstName(nameStr);
        }
        loginType = "normal";
        deviceId = Utils.getDeviceId(context);
        signUpInputModel.setEmailId(emailStr);
        signUpInputModel.setMobileNo(mobileStr);
        signUpInputModel.setPlatform(platform);
        signUpInputModel.setLoginType(loginType);
        signUpInputModel.setDeviceId(deviceId);
        return signUpInputModel;
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
                    if (!nameET.getText().toString().isEmpty() && !mobileET.getText().toString().isEmpty() && mobileET.getText().toString().length() == 10 && Utils.isValidEmail(emailET.getText().toString())) {
                        getUserSignUp();
                    } else {
                        Toast.makeText(context, "Name, Mobile Number cannot be empty and Email should be valid", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onTryReconnect() {
        getUserSignUp();
    }

    @Override
    public void onBackPressed() {
        //Facebook Logout
        LoginManager.getInstance().logOut();

        //Google Logout
        signOut();
        super.onBackPressed();

    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Log.i(TAG, "onResult: Google Login Logged Out");
                    }
                });
    }

    private void RegisterOnApplozic() {
        AllUtils.LoaderUtils.showLoader(context);
        UserLoginTask.TaskListener listener = new UserLoginTask.TaskListener() {

            @Override
            public void onSuccess(RegistrationResponse registrationResponse, final Context context) {
                mAuthTask = null;
                AllUtils.LoaderUtils.dismissLoader();

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

                Intent intent = new Intent(context, OtpActivity.class);
                startActivity(intent);
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
        user.setPassword("Brongo_Client");
        user.setImageLink("");

        mAuthTask = new UserLoginTask(user, listener, this);
        mAuthTask.execute((Void) null);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
