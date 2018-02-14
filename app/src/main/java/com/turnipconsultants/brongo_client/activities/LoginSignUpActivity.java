package com.turnipconsultants.brongo_client.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.payu.magicretry.MainActivity;
import com.turnipconsultants.brongo_client.CustomWidgets.BrongoButton;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by mohit on 15-09-2017.
 */

public class LoginSignUpActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginSignUpActivity";
    private static final int RC_SIGN_IN = 234;

    @BindView(R.id.fb_login_button)
    LoginButton FBLoginBTN;

    @BindView(R.id.fb_login_IV)
    ImageView FBLoginIV;

    @BindView(R.id.google_login_IV)
    ImageView GoogleLoginIV;

    @BindView(R.id.instagram_login_IV)
    ImageView InstagramLoginIV;

    @BindView(R.id.linkedin_login_IV)
    ImageView LinkedInLoginIV;

    @BindView(R.id.twitter_login_IV)
    ImageView TwitterLoginIV;

    @BindView(R.id.signupBtn)
    BrongoButton SignUpBTN;

    @BindView(R.id.login)
    TextView login;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;

    private Unbinder unbinder;
    private CallbackManager callbackManager;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        unbinder = ButterKnife.bind(this);
        initPage();
    }

    private void initPage() {
        context = this;
        FBLoginBTN.setReadPermissions("public_profile");
        FBLoginBTN.setReadPermissions("email");
        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.requestId_token_google))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject me, GraphResponse response) {

                                        if (response.getError() != null) {
                                            AllUtils.ToastUtils.showToast(context, "Unable to get Login information");
                                        } else {

                                            String user_firstname = me.optString("first_name");
                                            String user_lastname = me.optString("last_name");
                                            String user_email = response.getJSONObject().optString("email");
                                            OpenSignUpPage(user_firstname, user_lastname, user_email,"");

                                        }
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "last_name,first_name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        AllUtils.ToastUtils.showToast(context, "Login cancelled");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        AllUtils.ToastUtils.showToast(context, "Login Error");
                    }
                });
    }

    private void OpenSignUpPage(String firstname, String lastname, String email,String mobile) {
        Bundle fbBundle = new Bundle();
        fbBundle.putString(AppConstants.FB_USER.FIRST_NAME, firstname);
        fbBundle.putString(AppConstants.FB_USER.LAST_NAME, lastname);
        fbBundle.putString(AppConstants.FB_USER.EMAIL, email);
        fbBundle.putString(AppConstants.FB_USER.MOBILE, mobile);
        openSignUp(fbBundle);
    }

    @OnClick({R.id.login, R.id.signupBtn, R.id.fb_login_IV, R.id.google_login_IV, R.id.instagram_login_IV, R.id.linkedin_login_IV, R.id.twitter_login_IV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                openLogin();
                break;
            case R.id.signupBtn:
                openSignUp(null);
                break;
            case R.id.fb_login_IV:
                FBLoginBTN.performClick();
                break;
            case R.id.google_login_IV:
                signIn();
                break;
            case R.id.instagram_login_IV:
                break;
            case R.id.linkedin_login_IV:
                break;
            case R.id.twitter_login_IV:
                break;
        }
    }

    public void openSignUp(Bundle fbBundle) {
        Intent intent = new Intent(this, SignUpActivity.class);
        if (fbBundle != null)
            intent.putExtras(fbBundle);
        startActivity(intent);
    }

    public void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                AllUtils.LoaderUtils.showLoader(context);
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(LoginSignUpActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebase AuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        AllUtils.LoaderUtils.dismissLoader();
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            OpenSignUpPage(user.getDisplayName(), "", user.getEmail(),user.getPhoneNumber());
                        } else {
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        AllUtils.ToastUtils.showToast(context,"Login Failed");
    }
}
