package com.turnipconsultants.brongo_client.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.applozic.mobicomkit.api.MobiComKitConstants;
import com.applozic.mobicomkit.api.account.user.UserLogoutTask;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.payu.india.Payu.Payu;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.PageTransformerTest;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.adapters.ConnectedBrokersAdapter;
import com.turnipconsultants.brongo_client.fragments.ConnectedBrokersFragment;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.mvp.HomePresenter;
import com.turnipconsultants.brongo_client.mvp.HomePresenterImpl;
import com.turnipconsultants.brongo_client.mvp.HomeView;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.responseModels.QuestionsResponseModel;
import com.turnipconsultants.brongo_client.responseModels.SecondLandingResponse;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.turnipconsultants.brongo_client.others.Constants.AppConstants.PROPERTY.PROPERTY_TYPE;


/**
 * Created by mohit on 16-09-2017.
 */

public class HomeActivity extends AppCompatActivity implements HomeView, View.OnClickListener, ConnectedBrokersFragment.BrokerListener, NoInternetTryConnectListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SecondLandingAcivity";
    @BindView(R.id.homeLogo)
    ImageView homeLogo;

    @BindView(R.id.chatRL)
    RelativeLayout chatRL;

    @BindView(R.id.notiRL)
    RelativeLayout notiRL;

    @BindView(R.id.chat_num)
    TextView chatCountTV;

    @BindView(R.id.not_num)
    TextView notiCountTV;

    @BindView(R.id.second_view_pager)
    ViewPager viewPager;

    @BindView(R.id.foruiconrl)
    RelativeLayout HomeButtonsRL;

    @BindView(R.id.reside_menu_toggle_BTN)
    RelativeLayout resideMenuToggle;

    @BindView(R.id.newSearch)
    FloatingActionButton newSearch;

    private ResideMenu resideMenu;
    private ResideMenuItem itemHeader, itemAbout, itemPaymentSubscription, itemInvoice, itemRateUs, itemYourRating, itemPast, itemHelp, itemContact, itemLogout;
    private SharedPreferences pref;

    private ConnectedBrokersAdapter viewPagerAdapter;
    private Context context;
    private ArrayList<Object> viewPagerArrayList;
    private String isOnlyConnected = "OPEN";
    private String headerToken, headerDeviceId, headerPlatform, clientMobileNo;
    private boolean isSecondLand = false;

    private ArrayList<QuestionsResponseModel.DataEntity> questionsList;
    private HomePresenter homePresenter;
    private String[] SideMenuArray;
    private UserLogoutTask userLogoutTask;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private TASK selectedTask;


//    public final static int LOOPS = 1000;
//    public CarouselPagerAdapter adapter;
//    public static int FIRST_PAGE = 10;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private enum TASK {
        LOAD_BROKER, LOAD_QUESTION
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        ButterKnife.bind(this);
        Payu.setInstance(this);                                                         //PayuBIZ
        initializeViews();
        viewPager.setClipToPadding(false);
        viewPager.setPadding(AllUtils.DensityUtils.dpToPx(5), 0, AllUtils.DensityUtils.dpToPx(5), 0);
        viewPager.setPageTransformer(false, new PageTransformerTest());
        setUpMenu();
        chatRL.setOnClickListener(this);
        notiRL.setOnClickListener(this);
        initViewPager();
        SaveBrokerCount();
        homePresenter.fetchMicroMarkets();
        //new GlideImageSaveTask(context).execute("https://s3-ap-southeast-1.amazonaws.com/quickbrokerimages/profileImages/client/sachin.jpg");
    }

    private void initializeViews() {
        context = this;
        homePresenter = new HomePresenterImpl(this, this);
        pref = this.getSharedPreferences(AppConstants.PREF_NAME, MODE_PRIVATE);
        viewPagerArrayList = new ArrayList();
        clientMobileNo = pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "");
        isSecondLand = pref.getBoolean(AppConstants.PREFS.IS_SECOND, false);
        questionsList = new ArrayList<>();
        SideMenuArray = context.getResources().getStringArray(R.array.side_menu);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void setUpMenu() {

        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.blue_purple_bg_radial);
        resideMenu.attachToActivity(this);
        resideMenu.setShadowVisible(false);
        resideMenu.setScaleValue(0.6f);


        itemHeader = new ResideMenuItem(this, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""), pref.getString(AppConstants.PREFS.USER_FIRST_NAME, ""));

        itemAbout = new ResideMenuItem(this, R.drawable.aboutbrongo, SideMenuArray[0]);
        itemPaymentSubscription = new ResideMenuItem(this, R.drawable.payment, SideMenuArray[1]);
        itemPast = new ResideMenuItem(this, R.drawable.feedback, SideMenuArray[2]);
        itemHelp = new ResideMenuItem(this, R.drawable.layer, SideMenuArray[3]);
        itemContact = new ResideMenuItem(this, R.drawable.helpfaq, SideMenuArray[4]);
        itemRateUs = new ResideMenuItem(this, R.drawable.contactus, SideMenuArray[5]);
        itemLogout = new ResideMenuItem(this, R.drawable.logout, SideMenuArray[6]);
//        itemInvoice = new ResideMenuItem(this, R.drawable.bot, SideMenuArray[2]);
//        itemYourRating = new ResideMenuItem(this, R.drawable.feedback, SideMenuArray[4]);


        itemAbout.setOnClickListener(this);
        itemPaymentSubscription.setOnClickListener(this);
//        itemInvoice.setOnClickListener(this);
//        itemYourRating.setOnClickListener(this);
        itemRateUs.setOnClickListener(this);
        itemPast.setOnClickListener(this);
        itemHelp.setOnClickListener(this);
        itemContact.setOnClickListener(this);
        itemLogout.setOnClickListener(this);

        resideMenu.addMenuItem(itemHeader, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemAbout, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemPaymentSubscription, ResideMenu.DIRECTION_LEFT);
//        resideMenu.addMenuItem(itemInvoice, ResideMenu.DIRECTION_LEFT);
//        resideMenu.addMenuItem(itemYourRating, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemPast, ResideMenu.DIRECTION_LEFT);
        //resideMenu.addMenuItem(itemSetting, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemHelp, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemContact, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemRateUs, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemLogout, ResideMenu.DIRECTION_LEFT);

        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        resideMenu.addIgnoredView(viewPager);

        resideMenuToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });


    }

    @OnClick(R.id.newSearch)
    public void NewSearch(FloatingActionButton button) {
        Intent intent = new Intent(context, HomeWithBackActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        if (view == itemHeader) {
            resideMenu.closeMenu();
            Intent intent = new Intent(this, MyProfileActivity.class);
            startActivity(intent);
        } else if (view == itemAbout) {
            resideMenu.closeMenu();
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
        } else if (view == itemPaymentSubscription) {
            resideMenu.closeMenu();
            Intent intent = new Intent(this, PaymentSubscriptionActivity.class);
            startActivity(intent);
        }
        else if (view == itemRateUs) {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
            }
        }
        else if (view == itemPast) {
            resideMenu.closeMenu();
            Intent intent = new Intent(this, PastRequirementActivity.class);
            startActivity(intent);
        } else if (view == itemHelp) {
            resideMenu.closeMenu();
            Intent intent = new Intent(this, HelpFAQActivity.class);
            startActivity(intent);
        } else if (view == itemContact) {
            resideMenu.closeMenu();
            Intent intent = new Intent(this, ContactUsActivity.class);
            startActivity(intent);
        } else if (view == itemLogout) {
            showLogoutDialog();
        } else if (view == chatRL) {
            Intent intent = new Intent(this, ConversationActivity.class);
            startActivity(intent);
        } else if (view == notiRL) {
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);
        }
    }

    private void showLogoutDialog() {
        final Dialog dialogBlock = new Dialog(context, R.style.DialogTheme);
        dialogBlock.setContentView(R.layout.popup_dialog_two_btn);
        dialogBlock.setCanceledOnTouchOutside(false);
        dialogBlock.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        TextView message = dialogBlock.findViewById(R.id.thankyoutv);
        message.setText("Do you really want to logout ?");
        Button yes = dialogBlock.findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBlock.dismiss();
                AllUtils.LoaderUtils.showLoader(context);
                pref.edit().putBoolean(AppConstants.PREFS.LOGGED_IN, false).commit();
                resideMenu.closeMenu();
                ClearUserInfo();
            }
        });

        Button no = dialogBlock.findViewById(R.id.no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBlock.dismiss();
            }
        });

        ImageView cancel = dialogBlock.findViewById(R.id.cancel);
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBlock.dismiss();
                    }
                });

        dialogBlock.show();
    }

    private void ClearUserInfo() {
        // FB Logout
        LoginManager.getInstance().logOut();
        // Google Logout
        signOut();
        pref.edit().clear().commit();
        logoutAppLozic();

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

    private void logoutAppLozic() {
        final UserLogoutTask.TaskListener userLogoutTaskListener = new UserLogoutTask.TaskListener() {
            @Override
            public void onSuccess(Context context) {
                Log.i(TAG, "onSuccess: APPLOZIC LOGOUT");
                userLogoutTask = null;

                AllUtils.LoaderUtils.dismissLoader();
                Intent intent = new Intent(context, LoginSignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Exception exception) {
                userLogoutTask = null;

                AllUtils.LoaderUtils.dismissLoader();
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

        userLogoutTask = new UserLogoutTask(userLogoutTaskListener, context);
        userLogoutTask.execute((Void) null);
    }

    public void openBuyProperty(View v) {
        Intent intent = new Intent(this, PropertyRouterActivity.class);
        intent.putExtra(PROPERTY_TYPE, AppConstants.PROPERTY.BUY_A_PROPERTY);
        startActivity(intent);
    }

    public void openRentProperty(View v) {
        Intent intent = new Intent(this, PropertyRouterActivity.class);
        intent.putExtra(PROPERTY_TYPE, AppConstants.PROPERTY.RENT_A_PROPERTY);
        startActivity(intent);
    }

    public void sellYourProperty(View v) {
        Intent intent = new Intent(this, PropertyRouterActivity.class);
        intent.putExtra(PROPERTY_TYPE, AppConstants.PROPERTY.SELL_YOUR_PROPERTY);
        startActivity(intent);
    }

    public void rentYourProperty(View v) {
        Intent intent = new Intent(this, PropertyRouterActivity.class);
        intent.putExtra(PROPERTY_TYPE, AppConstants.PROPERTY.RENT_YOUR_PROPERTY);
        startActivity(intent);
    }


    private void LoadBrokers2(final boolean isForLeadUpdate) {
        selectedTask = TASK.LOAD_BROKER;
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        Log.w("POSTMAN", " LoadBrokers: headerDeviceId: " + headerDeviceId + " headerPlatform: " + headerPlatform + " headerToken: " + headerToken);
        if (InternetConnection.isNetworkAvailable(context)) {
            if (!isForLeadUpdate)
                AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<SecondLandingResponse> call = apiInstance.getPostedLeadsData2(headerToken, headerPlatform, headerDeviceId, clientMobileNo, isOnlyConnected);
            call.enqueue(new Callback<SecondLandingResponse>() {
                @Override
                public void onResponse(Call<SecondLandingResponse> call, Response<SecondLandingResponse> response) {

                    try {
                        if (!isForLeadUpdate)
                            AllUtils.LoaderUtils.dismissLoader();
                        if (response != null && response.isSuccessful()) {
                            SecondLandingResponse responseModel = response.body();
                            if (responseModel.getStatusCode() == 200) {

                                List<SecondLandingResponse.DataEntity> dataEntityList = responseModel.getData();
                                if (isForLeadUpdate) {
                                    int index = viewPager.getCurrentItem();
                                    ConnectedBrokersFragment fragment = viewPagerAdapter.getFragment(index);

                                    for (int i = 0; i < dataEntityList.size(); i++) {
                                        fragment.object = dataEntityList.get(i);
                                        fragment.setDealGraph2();
                                    }
                                } else {
                                    viewPagerArrayList.clear();
                                    viewPagerArrayList.addAll(dataEntityList);
                                    viewPagerAdapter.notifyDataSetChanged();
                                    if (dataEntityList.size() > 0) {
                                        pref.edit().putBoolean(AppConstants.PREFS.IS_SECOND, true).commit();
                                        showBrokerList(true);
                                    } else {
                                        showBrokerList(false);
                                    }
                                    getBrongoQuestions();
                                }
                            } else {
                                Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                                showBrokerList(false);
                            }
                        } else {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                LoadBrokers2(false);
                            } else {
                                Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                                showBrokerList(false);
                            }
                            Log.e("error", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showBrokerList(false);
                    }
                }

                @Override
                public void onFailure(Call<SecondLandingResponse> call, Throwable t) {
                    if (!isForLeadUpdate)
                        AllUtils.LoaderUtils.dismissLoader();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    showBrokerList(false);
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, this);
        }
    }

    void showBrokerList(boolean NoDeals) {
        if (NoDeals) {
            HomeButtonsRL.setVisibility(View.INVISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            newSearch.setVisibility(View.VISIBLE);
        } else {
            HomeButtonsRL.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.INVISIBLE);
            newSearch.setVisibility(View.INVISIBLE);
        }
    }

    private void getBrongoQuestions() {
        selectedTask = TASK.LOAD_QUESTION;
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        if (InternetConnection.isNetworkAvailable(context)) {
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<QuestionsResponseModel> call = apiInstance.getBrongoQuestions(headerToken, headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""), "CHALLENGE_STATUS");
            call.enqueue(new Callback<QuestionsResponseModel>() {
                @Override
                public void onResponse(Call<QuestionsResponseModel> call, Response<QuestionsResponseModel> response) {
                    if (response != null && response.isSuccessful()) {
                        QuestionsResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {

                            questionsList.addAll(responseModel.getData());

                        } else {
                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));

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
                public void onFailure(Call<QuestionsResponseModel> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, this);
        }
    }

    private void initViewPager() {

        viewPagerAdapter = new ConnectedBrokersAdapter(getSupportFragmentManager(), viewPagerArrayList, AppConstants.VISIBILITY_PAGE.SECOND_TIME_ACTIVITY);
        viewPager.setAdapter(viewPagerAdapter);

        if (isSecondLand) {
            showBrokerList(true);
            if (checkPermissionAllowed()) {
                LoadBrokers2(false);
            }

        } else {
            showBrokerList(false);
        }
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
    public void setChatCount(int chatCount) {
        if (chatCount == 0)
            chatCountTV.setVisibility(View.GONE);
        else {
            chatCountTV.setVisibility(View.VISIBLE);
            chatCountTV.setText(chatCount + "");
        }
    }

    @Override
    public void setNotificationCount(int notiCount) {
        if (notiCount == 0)
            notiCountTV.setVisibility(View.GONE);
        else {
            notiCountTV.setVisibility(View.VISIBLE);
            notiCountTV.setText(notiCount + "");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onresume", "on");
        IntentFilter filter = new IntentFilter();
        filter.addAction(MobiComKitConstants.APPLOZIC_UNREAD_COUNT);
        filter.addAction(AppConstants.SPECIFIC_PUSH.LEADS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(chatReceiver, filter);
        homePresenter.getChatCount();
        homePresenter.getNotificationCount();
        try {
            String profilePicFilePath = pref.getString(AppConstants.PREFS.USER_PROFILE_PIC_FILE, "");
            if (!profilePicFilePath.isEmpty() && new File(profilePicFilePath).exists()) {
                CircleImageView profilePicCIV = resideMenu.getLeftMenuView().findViewById(R.id.profilePicCIV);
                if (profilePicCIV != null) {
                    File file = new File(profilePicFilePath);
                    Bitmap profilePic = BitmapFactory.decodeFile(file.getAbsolutePath());
                    profilePicCIV.setImageBitmap(profilePic);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(chatReceiver);
    }

    private BroadcastReceiver chatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.w(TAG, "Chat Push Received");
            if (MobiComKitConstants.APPLOZIC_UNREAD_COUNT.equals(intent.getAction())) {
                homePresenter.getChatCount();
                homePresenter.getNotificationCount();
            } else if (AppConstants.SPECIFIC_PUSH.LEADS_UPDATE.equals(intent.getAction())) {
                LoadBrokers2(true);
            }
        }
    };

    void SaveBrokerCount() {
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        Utils.saveBrokers(context, headerToken, headerPlatform, headerDeviceId);
    }

    @Override
    public void ReloadBrokers() {
        LoadBrokers2(false);
    }

    @Override
    public void onTryReconnect() {
        switch (selectedTask) {
            case LOAD_BROKER: {
                LoadBrokers2(false);
                break;
            }
            case LOAD_QUESTION: {
                getBrongoQuestions();
                break;
            }
        }
    }
}
