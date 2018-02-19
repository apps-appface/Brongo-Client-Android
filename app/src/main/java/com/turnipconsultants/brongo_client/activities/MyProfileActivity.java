package com.turnipconsultants.brongo_client.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.applozic.mobicommons.file.FileUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;
import com.turnipconsultants.brongo_client.responseModels.ProfileResponseModel;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.ImageCaptureUtils;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohit on 11-10-2017.
 */

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener, NoInternetTryConnectListener {

    private static final String TAG = "MyProfileActivity";

    public static final int REQUEST_CAMERA_AND_WRITABLE_PERMISSIONS = 111;
    public static final int REQUEST_DEVICE_ID_PERMISSIONS = 112;
    private static final int REQUEST_CAMERA = 200;
    private static final int SELECT_FILE = 201;

    @BindView(R.id.title)
    TextView toolBarTitle;

    @BindView(R.id.reset)
    TextView toolBarReset;

    @BindView(R.id.brokerName)
    TextView clientName;

    @BindView(R.id.rating)
    TextView clientRating;

    @BindView(R.id.activereq)
    TextView activeRequests;

    @BindView(R.id.mobileno)
    TextView mobileET;

    @BindView(R.id.circImage)
    CircleImageView profileimage;

    @BindView(R.id.emailIdET)
    EditText emailET;

    @BindView(R.id.altMobET)
    EditText altMobileET;

    @BindView(R.id.switchbtn)
    Switch hideProfile;

    @BindView(R.id.edit)
    ImageView edit;

    @BindView(R.id.add)
    ImageView add;

    @BindView(R.id.camera)
    ImageView camera;

    @BindView(R.id.save)
    Button save;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    private static Context context;
    private String headerToken, headerDeviceId, headerPlatform, clientMobileNo;
    private SharedPreferences pref;
    private String emailStr, altNoStr, hidePicStr, firstNameStr, lastNameStr;
    private Uri fileUri;
    private MultipartBody.Part body;
    private RequestBody firstNameStrRb, clientMobileNoRb, lastNameStrRb, altNoStrRb, hidePicStrRb, emailStrRb, genderRb;
    private List<String> listPermissionsNeeded;
    private TASK selectedTask;

    private enum TASK {
        GET_PROFILE, UPDATE_PROFILE
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        initializeViews();

        setProfileValues();
        toolBarReset.setVisibility(View.GONE);
        toolBarTitle.setText("MY PROFILE");
        mobileET.setText(pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""));
        edit.setOnClickListener(this);
        add.setOnClickListener(this);
        camera.setOnClickListener(this);
        save.setOnClickListener(this);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        getClientProfile();

        hideProfile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                save.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

    private void setProfileValues() {
        try {
            String fName, lName, email, mobile, profilePic;
            fName = pref.getString(AppConstants.PREFS.USER_FIRST_NAME, "");
            lName = pref.getString(AppConstants.PREFS.USER_LAST_NAME, "");
            email = pref.getString(AppConstants.PREFS.USER_EMAIL, "");
            mobile = pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "");
            profilePic = pref.getString(AppConstants.PREFS.USER_PROFILE_PIC_FILE, "");
            clientName.setText(fName + " " + lName);
            emailET.setText(email);
            mobileET.setText(mobile);
            profileimage = (CircleImageView) findViewById(R.id.circImage);

            if (!profilePic.isEmpty() && new File(profilePic).exists()) {
                File file = new File(profilePic);

                progressBar.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(file)
                        .apply(BrongoClientApplication.getRequestOption(false))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(profileimage);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeViews() {
        context = this;
        pref = this.getSharedPreferences(AppConstants.PREF_NAME, 0);
        emailET.setEnabled(false);
        altMobileET.setEnabled(false);
        save.setVisibility(View.GONE);
    }

    private void getClientProfile() {
        selectedTask = TASK.GET_PROFILE;
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        clientName.setText(pref.getString(AppConstants.PREFS.USER_FIRST_NAME, "") + " " + pref.getString(AppConstants.PREFS.USER_LAST_NAME, ""));
        emailET.setText(pref.getString(AppConstants.PREFS.USER_EMAIL, ""));
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        if (InternetConnection.isNetworkAvailable(context)) {
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<ProfileResponseModel> call = apiInstance.getClientProfile(headerToken, headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""));
            call.enqueue(new Callback<ProfileResponseModel>() {
                @Override
                public void onResponse(Call<ProfileResponseModel> call, Response<ProfileResponseModel> response) {
                    AllUtils.LoaderUtils.dismissLoader();
                    if (response != null && response.isSuccessful()) {
                        ProfileResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {
                            List<ProfileResponseModel.DataEntity> data = responseModel.getData();
                            Glide.with(context)
                                    .load(data.get(0).getProfileImage())
                                    .apply(BrongoClientApplication.getRequestOption(false))
                                    .into(profileimage);
                            clientName.setText(AllUtils.StringUtilsBrongo.toCamelCase(data.get(0).getFirstName()) + " " + AllUtils.StringUtilsBrongo.toCamelCase(data.get(0).getLastName()));
                            emailET.setText(data.get(0).getEmailId());
                            String alternativeNumber = data.get(0).getAlternateMobileNo();

                            if (alternativeNumber == null || alternativeNumber.isEmpty()) {
                                altMobileET.setHint(String.valueOf("Add Number"));
                            } else {
                                altMobileET.setText(alternativeNumber);
                            }

                            if (data.get(0).getHideProfilePic().equals("1")) {
                                hideProfile.setChecked(true);
                            } else if (data.get(0).getHideProfilePic().equals("0")) {
                                hideProfile.setChecked(false);
                            }
                            clientRating.setText(data.get(0).getRating() + "");
                            activeRequests.setText(data.get(0).getActiveRequests() + "");
                            ratingBar.setRating(data.get(0).getRating());
                            scrollView.post(new Runnable() {
                                public void run() {
                                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                                }
                            });

                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                getClientProfile();
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
                public void onFailure(Call<ProfileResponseModel> call, Throwable t) {
                    AllUtils.LoaderUtils.dismissLoader();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, this);
        }
    }

    private void getStringValues() {
        clientMobileNo = pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "");
        emailStr = emailET.getText().toString();
        altNoStr = altMobileET.getText().toString();
        if (hideProfile.isChecked()) {
            hidePicStr = "1";
        } else {
            hidePicStr = "0";
        }

        firstNameStr = pref.getString(AppConstants.PREFS.USER_FIRST_NAME, "");
        lastNameStr = pref.getString(AppConstants.PREFS.USER_LAST_NAME, "");

        body = fileUri != null ? prepareFilePart("profileImage", fileUri) : null;

        clientMobileNoRb = RequestBody.create(MediaType.parse("multipart/form-data"), clientMobileNo);
        emailStrRb = RequestBody.create(MediaType.parse("multipart/form-data"), emailStr);
        altNoStrRb = RequestBody.create(MediaType.parse("multipart/form-data"), altNoStr);
        hidePicStrRb = RequestBody.create(MediaType.parse("multipart/form-data"), hidePicStr);
        firstNameStrRb = RequestBody.create(MediaType.parse("multipart/form-data"), firstNameStr);
        lastNameStrRb = RequestBody.create(MediaType.parse("multipart/form-data"), lastNameStr);
        genderRb = RequestBody.create(MediaType.parse("multipart/form-data"), "Male");
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = FileUtils.getFile(context, fileUri);

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(context.getContentResolver().getType(fileUri)),
                        file
                );

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private void updateUserProfile() {
        selectedTask = TASK.UPDATE_PROFILE;
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        getStringValues();
        if (InternetConnection.isNetworkAvailable(context)) {
            AllUtils.LoaderUtils.showLoader(context);
            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<GeneralApiResponseModel> call = apiInstance.updateClientProfile(headerToken, headerPlatform, headerDeviceId, firstNameStrRb, clientMobileNoRb, lastNameStrRb, emailStrRb, genderRb, altNoStrRb, hidePicStrRb, body);
            call.enqueue(new Callback<GeneralApiResponseModel>() {
                @Override
                public void onResponse(Call<GeneralApiResponseModel> call, Response<GeneralApiResponseModel> response) {
                    AllUtils.LoaderUtils.dismissLoader();
                    if (response != null && response.isSuccessful()) {
                        GeneralApiResponseModel responseModel = response.body();
                        if (responseModel.getStatusCode() == 200) {

                            emailET.setEnabled(false);
                            altMobileET.setEnabled(false);
                            emailET.clearFocus();
                            altMobileET.clearFocus();

                            File imgFile = FileUtils.getFile(context, fileUri);
                            if (imgFile != null && imgFile.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                profileimage.setImageBitmap(myBitmap);
                                pref.edit().putString(AppConstants.PREFS.USER_PROFILE_PIC_FILE, imgFile.getAbsolutePath()).commit();
                                save.setVisibility(View.GONE);
                            }
                            save.setVisibility(View.GONE);
                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                updateUserProfile();
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
                    AllUtils.LoaderUtils.dismissLoader();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            AllUtils.DialogUtils.NoInternetDialog(context, this);
        }
    }

    public void endActivity(View v) {
        finish();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.camera:
                save.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkCameraAndWritablePermission()) {
                        ImageCaptureUtils.selectImageAlert(context, this, REQUEST_CAMERA, SELECT_FILE);
                    } else {
                        requestCameraAndWritablePermission();
                    }
                } else {
                    ImageCaptureUtils.selectImageAlert(context, this, REQUEST_CAMERA, SELECT_FILE);
                }
                break;
            case R.id.save:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (isReadDeviceIdAllowed()) {
                        updateUserProfile();
                    } else {
                        requestDeviceIdPermission();
                    }
                } else {
                    updateUserProfile();
                }

                break;
            case R.id.edit:
                emailET.setEnabled(true);
                emailET.requestFocus();
                save.setVisibility(View.VISIBLE);
                break;
            case R.id.add:
                altMobileET.setEnabled(true);
                altMobileET.requestFocus();
                //altMobileET.setSelection(altMobileET.getText());
                save.setVisibility(View.VISIBLE);
                break;
        }
    }

    private boolean checkCameraAndWritablePermission() {
        int permissionCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int permissionWritableExternal = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        listPermissionsNeeded = new ArrayList<>();

        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionWritableExternal != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraAndWritablePermission() {
        requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_CAMERA_AND_WRITABLE_PERMISSIONS);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestDeviceIdPermission() {
        requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_DEVICE_ID_PERMISSIONS);
    }

    private boolean isReadDeviceIdAllowed() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_AND_WRITABLE_PERMISSIONS:
                if (grantResults.length > 0) {
                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WritablePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (CameraPermission && WritablePermission) {
                        ImageCaptureUtils.selectImageAlert(context, this, REQUEST_CAMERA, SELECT_FILE);
                    } else {
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case REQUEST_DEVICE_ID_PERMISSIONS:
                if (grantResults.length > 0) {
                    boolean DeviceIdPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (DeviceIdPermission) {
                        updateUserProfile();
                    } else {
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE)
                    onSelectFromGalleryResult(data);
                else if (requestCode == REQUEST_CAMERA)
                    onCaptureImageResult(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        try {
            fileUri = data.getData();

            Bitmap bm = null;
            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            profileimage.setImageBitmap(bm);
        } catch (Exception e) {
            throw e;
        }
    }

    private void onCaptureImageResult(Intent data) {
        try {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            profileimage.setImageBitmap(thumbnail);
            ImageCaptureUtils.CreateNewFileForPicture();
            fileUri = ImageCaptureUtils.getImageUri(context, thumbnail);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void onTryReconnect() {
        switch (selectedTask) {
            case GET_PROFILE: {
                getClientProfile();
                break;
            }
            case UPDATE_PROFILE: {
                updateUserProfile();
                break;
            }
        }
    }
}
