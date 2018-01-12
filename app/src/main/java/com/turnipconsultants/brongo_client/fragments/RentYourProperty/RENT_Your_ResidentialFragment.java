package com.turnipconsultants.brongo_client.fragments.RentYourProperty;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applozic.mobicommons.file.FileUtils;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.turnipconsultants.brongo_client.CustomWidgets.BrongoButton;
import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
import com.turnipconsultants.brongo_client.Listener.CommissionListenerFactory;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.activities.BrokersMapActivity;
import com.turnipconsultants.brongo_client.fragments.BaseFragment;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.AllUtils.NumToWords;
import com.turnipconsultants.brongo_client.others.CommissionDialogFactory;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.ImageCaptureUtils;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.BrokersCountModel;
import com.turnipconsultants.brongo_client.responseModels.PropertyTransactionResponseModel;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.turnipconsultants.brongo_client.others.Constants.AppConstants.POPULAR_LOCATIONS.BANGALORE;

/**
 * Created by mohit on 18-09-2017.
 */

public class RENT_Your_ResidentialFragment extends BaseFragment implements CommissionListenerFactory.RentOutCommissionListener, NoInternetTryConnectListener, CustomListener {
    private static final String TAG = "SELL_Your_ResidentialFr";
    public static final int REQUEST_CAMERA_AND_WRITABLE_PERMISSIONS = 111;
    public static final int REQUEST_DEVICE_ID_PERMISSIONS = 112;
    private static final int REQUEST_CAMERA = 200;
    private static final int SELECT_FILE = 201;

    @BindString(R.string.rupee)
    String rupeeSymbol;

    @BindView(R.id.connect_BTN)
    BrongoButton connectBrokersBTN;

    @BindView(R.id.popular_locations_FL)
    TagFlowLayout popularLocationsFL;

    @BindView(R.id.project_name_ET)
    EditText projectNameET;

    @BindView(R.id.property_types_FL)
    TagFlowLayout propertyTypesFL;

    @BindView(R.id.bedroom_TV)
    BrongoTextView bedroomTV;

    @BindView(R.id.bedroom_FL)
    TagFlowLayout bedroomFL;

    @BindView(R.id.expected_rent_amount_TV)
    BrongoTextView rentAmountTV;

    @BindView(R.id.rent_amount_words_TV)
    BrongoTextView rentAmountWordTV;

    @BindView(R.id.deposit_amount_TV)
    BrongoTextView depositAmountTV;

    @BindView(R.id.deposit_words_TV)
    BrongoTextView depositWordsTV;

    @BindView(R.id.housing_orientation_FL)
    TagFlowLayout houseOrientationFL;

    @BindView(R.id.tenant_type_FL)
    TagFlowLayout tenantTypeFL;

    @BindView(R.id.availability_status_FL)
    TagFlowLayout availableStatusFL;

    @BindView(R.id.furnishing_FL)
    TagFlowLayout furnishedFL;

    @BindView(R.id.select_picture_RL)
    RelativeLayout selectPictureRL;

    @BindView(R.id.img1)
    ImageView propertyPic1;

    @BindView(R.id.img2)
    ImageView propertyPic2;

    @BindView(R.id.img3)
    ImageView propertyPic3;

    @BindView(R.id.other_req_ET)
    EditText commentsET;

    Unbinder unbinder;

    private String[] popularLocArray = BANGALORE;
    private String[] propTypesArray = new String[]{"Apartment/Flat", "House", "Villa", "PG/Hostel", "Independent house"};
    private String[] bedroomArray = new String[]{"1 BHK", "2 BHK", "3 BHK", "4 BHK", "4 BHK+"};
    private String[] tenantTypeArray = new String[]{"Family", "Bachelor", "Any"};
    private String[] availabilityStatusArray = new String[]{"Within a Week", "Within 15 days", "Within a Month", "After a Month"};
    private String[] furnishingArray = new String[]{"Fully Furnished", "Semi Furnished", "Unfurnished", "Any"};
    private String[] houseOrientationArray = new String[]{"East", "West", "North", "South", "North-East", "South-East", "North-West", "South-West", "Any"};

    private Context context;
    private String headerToken, headerDeviceId, headerPlatform;
    private SharedPreferences pref;
    private String popLocStr = "", propTypeStr = "", bedroomStr = "", tenantTypeStr = "", availableStatusStr = "", furnishedStr = "", houseOrientationStr = "", commission = "";

    private Uri selectedImgUri, uriImg1, uriImg2, uriImg3;
    private Activity activity;
    private List<String> listPermissionsNeeded;

    private String brokerCountJSON;
    private Gson gson;
    private List<BrokersCountModel.Data> arrayList;

    private OptionsPickerView pickerOptions;
    private ArrayList<String> options1Items = new ArrayList<>();
    private ArrayList<String> options2Items = new ArrayList<>();
    private ArrayList<String> options3Items = new ArrayList<>();
    private NumToWords numToWords;
    private double expectedRentAmount, expectedDepositAmount;
    private PICKER_TASK selectedPickerTask;

    private enum PICKER_TASK {
        RENT, DEPOSIT
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = null;
        try {
            v = inflater.inflate(R.layout.rent_your_residential_fragment, container, false);
            unbinder = ButterKnife.bind(this, v);
            InitPage();
            SetValues();
            OnReset();
            new BudgetTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }


    private void InitPage() {
        context = getActivity();
        activity = getActivity();
        numToWords = new NumToWords();
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        gson = new Gson();
        brokerCountJSON = pref.getString(AppConstants.PREFS.BROKER_COUNT, "");
        Type type = new TypeToken<List<BrokersCountModel.Data>>() {
        }.getType();
        arrayList = gson.fromJson(brokerCountJSON, type);
    }

    private void SetValues() {
        final LayoutInflater mInflater = LayoutInflater.from(getActivity());
        popularLocationsFL.setAdapter(new TagAdapter<String>(popularLocArray) {

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
                popLocStr = popularLocArray[position];
                DecideSubmitButtonColor();

                try {
                    for (BrokersCountModel.Data d : arrayList) {
                        if (d.getMicroMarketName().equalsIgnoreCase(popLocStr)) {
                            connectBrokersBTN.setText("CONNECT TO THE BEST " + d.getCount() + " LOCAL BROKERS");
                            break;
                        }
                    }
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
        availableStatusFL.setAdapter(new TagAdapter<String>(availabilityStatusArray) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, availableStatusFL, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                availableStatusStr = "";
                DecideSubmitButtonColor();
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                availableStatusStr = availabilityStatusArray[position];
                DecideSubmitButtonColor();
            }
        });

        furnishedFL.setAdapter(new TagAdapter<String>(furnishingArray) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, furnishedFL, false);
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

        houseOrientationFL.setAdapter(new TagAdapter<String>(houseOrientationArray) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, houseOrientationFL, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                houseOrientationStr = "";
                DecideSubmitButtonColor();
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                houseOrientationStr = houseOrientationArray[position];
                DecideSubmitButtonColor();
            }
        });
    }

    private void RentOut() {

        if (expectedRentAmount == 0) {
            AllUtils.ToastUtils.showToast(context, "Please select expected rent");
            return;
        }
        if (expectedDepositAmount == 0) {
            AllUtils.ToastUtils.showToast(context, "Please select expected deposit");
            return;
        }

        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        Log.e("token", headerToken);
        if (InternetConnection.isNetworkAvailable(context)) {
            AllUtils.LoaderUtils.showLoader(context);

            MultipartBody.Part img1 = uriImg1 != null ? prepareFilePart("propertyPicture1", uriImg1) : null;
            MultipartBody.Part img2 = uriImg2 != null ? prepareFilePart("propertyPicture2", uriImg2) : null;
            MultipartBody.Part img3 = uriImg3 != null ? prepareFilePart("propertyPicture3", uriImg3) : null;

            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("clientMobileNo", RequestBody.create(MultipartBody.FORM, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
            map.put("projectName", RequestBody.create(MultipartBody.FORM, projectNameET.getText().toString()));
            map.put("propertyType", RequestBody.create(MultipartBody.FORM, "RESIDENTIAL"));
            map.put("postingType", RequestBody.create(MultipartBody.FORM, AppConstants.POSTING_TYPE.RENT_YOUR_PROPERTY));
            map.put("subPropertyType", RequestBody.create(MultipartBody.FORM, propTypeStr));
            map.put("bedRoomType", RequestBody.create(MultipartBody.FORM, bedroomStr));
//            map.put("approxSizeSquareFeet", RequestBody.create(MultipartBody.FORM, ""));  // don't know
            map.put("expectedRent", RequestBody.create(MultipartBody.FORM, expectedRentAmount + ""));
            map.put("expectedDeposit", RequestBody.create(MultipartBody.FORM, expectedDepositAmount + ""));
            map.put("furnishingStatus", RequestBody.create(MultipartBody.FORM, furnishedStr));
            map.put("microMarketName", RequestBody.create(MultipartBody.FORM, popLocStr));
            map.put("microMarketCity", RequestBody.create(MultipartBody.FORM, "Bangalore"));
            map.put("microMarketState", RequestBody.create(MultipartBody.FORM, "Karnataka"));
            map.put("orientation", RequestBody.create(MultipartBody.FORM, houseOrientationStr));
            map.put("tenantType", RequestBody.create(MultipartBody.FORM, tenantTypeStr));
            map.put("availabilityStatus", RequestBody.create(MultipartBody.FORM, availableStatusStr));
            map.put("comments", RequestBody.create(okhttp3.MultipartBody.FORM, commentsET.getText().toString()));
            map.put("commission", RequestBody.create(okhttp3.MultipartBody.FORM, 2 + ""));

            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<PropertyTransactionResponseModel> call = apiInstance.getRentYourProperty(headerToken, headerPlatform, headerDeviceId, map, img1, img2, img3);
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
                                Intent intent = new Intent(activity, BrokersMapActivity.class);
                                intent.putExtra("place", popLocStr);
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
                                RentOut();
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

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = FileUtils.getFile(context, fileUri);

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(activity.getContentResolver().getType(fileUri)),
                        file
                );

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private void DecideSubmitButtonColor() {
        if (!popLocStr.isEmpty() && !propTypeStr.isEmpty() && !propTypeStr.isEmpty() && !bedroomStr.isEmpty() && !tenantTypeStr.isEmpty() && !availableStatusStr.isEmpty() && !furnishedStr.isEmpty()) {
            connectBrokersBTN.setBackgroundColor(getResources().getColor(R.color.appPurple));
        } else {
            connectBrokersBTN.setBackgroundColor(getResources().getColor(R.color.appButton));
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
            selectedImgUri = data.getData();
            Bitmap bm = null;
            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(activity.getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            addToList(selectedImgUri, bm);
        } catch (Exception e) {
            throw e;
        }
    }

    private void addToList(Uri file, Bitmap bm) {
        if (uriImg1 == null)
            uriImg1 = file;
        else if (uriImg2 == null)
            uriImg2 = file;
        else if (uriImg3 == null)
            uriImg3 = file;

        setInImageView(bm);
    }

    private void setInImageView(Bitmap bitmap) {
        if (uriImg1 != null && uriImg2 == null && uriImg3 == null)
            propertyPic1.setImageBitmap(bitmap);
        else if (uriImg2 != null && uriImg1 != null && uriImg3 == null)
            propertyPic2.setImageBitmap(bitmap);
        else if (uriImg3 != null && uriImg1 != null && uriImg2 != null)
            propertyPic3.setImageBitmap(bitmap);
    }

    private void onCaptureImageResult(Intent data) {
        try {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ImageCaptureUtils.CreateNewFileForPicture();
            Uri tempUri = ImageCaptureUtils.getImageUri(context, thumbnail);
            addToList(tempUri, thumbnail);
        } catch (Exception e) {
            throw e;
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


    private boolean isReadDeviceIdAllowed() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }

    private void requestCameraAndWritablePermission() {
        requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_CAMERA_AND_WRITABLE_PERMISSIONS);
    }

    private void requestDeviceIdPermission() {
        requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_DEVICE_ID_PERMISSIONS);
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
                        Toast.makeText(activity, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case REQUEST_DEVICE_ID_PERMISSIONS:
                if (grantResults.length > 0) {
                    boolean DeviceIdPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (DeviceIdPermission) {
                        CommissionDialogFactory.RentOutCommissionDialog(context, this);
                    } else {
                        Toast.makeText(activity, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }

    @Override
    public void Accept() {
        RentOut();
    }

    @Override
    public void onTryReconnect() {
        RentOut();
    }

    @Override
    public void OnReset() {
        commentsET.setText("");
        connectBrokersBTN.setText("CONNECT TO THE BEST LOCAL BROKERS");

        popLocStr = "";
        popularLocationsFL.getAdapter().notifyDataChanged();

        projectNameET.setText("");

        propTypeStr = "";
        propertyTypesFL.getAdapter().notifyDataChanged();

        bedroomStr = "";
        bedroomFL.getAdapter().notifyDataChanged();

        expectedRentAmount = 0;
        rentAmountTV.setText("");
        rentAmountWordTV.setText("");

        expectedDepositAmount = 0;
        depositAmountTV.setText("");
        depositWordsTV.setText("");

        houseOrientationStr = houseOrientationArray[8];
        houseOrientationFL.getAdapter().notifyDataChanged();
        houseOrientationFL.getAdapter().setSelectedList(8);

        tenantTypeStr = tenantTypeArray[2];
        tenantTypeFL.getAdapter().notifyDataChanged();
        tenantTypeFL.getAdapter().setSelectedList(2);

        availableStatusStr = "";
        availableStatusFL.getAdapter().notifyDataChanged();

        furnishedStr = furnishingArray[3];
        furnishedFL.getAdapter().notifyDataChanged();
        furnishedFL.getAdapter().setSelectedList(3);

        uriImg1 = null;
        uriImg2 = null;
        uriImg3 = null;

        propertyPic1.setImageResource(0);
        propertyPic2.setImageResource(0);
        propertyPic3.setImageResource(0);

        commentsET.setText("");

        DecideSubmitButtonColor();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    @OnClick({R.id.connect_BTN, R.id.expected_rent_amount_TV, R.id.deposit_amount_TV, R.id.select_picture_RL})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.connect_BTN:
                if (isReadDeviceIdAllowed()) {
                    if (!popLocStr.isEmpty() && !propTypeStr.isEmpty() && !propTypeStr.isEmpty() && !bedroomStr.isEmpty() && !tenantTypeStr.isEmpty() && !availableStatusStr.isEmpty() && !furnishedStr.isEmpty()) {
                        CommissionDialogFactory.RentOutCommissionDialog(context, this);
                    } else {
                        Toast.makeText(context, "Please select all details", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    requestDeviceIdPermission();
                }
                break;
            case R.id.expected_rent_amount_TV:
                selectedPickerTask = PICKER_TASK.RENT;
                if (pickerOptions != null)
                    pickerOptions.show();
                break;
            case R.id.deposit_amount_TV:
                selectedPickerTask = PICKER_TASK.DEPOSIT;
                if (pickerOptions != null)
                    pickerOptions.show();
                break;
            case R.id.select_picture_RL:
                if (checkCameraAndWritablePermission()) {
                    if (uriImg1 != null && uriImg2 != null && uriImg3 != null) {
                        AllUtils.ToastUtils.showToast(context, "Maximum 3 images are allowed.");
                        return;
                    }
                    ImageCaptureUtils.selectImageAlert(context, this, REQUEST_CAMERA, SELECT_FILE);
                } else {
                    requestCameraAndWritablePermission();
                }
                break;
        }
    }

    @Override
    public void customLayout(View v) {
        Log.i(TAG, "customLayout: RECEIVED");
        v.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerOptions.dismiss();
            }
        });
        v.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerOptions.dismiss();
                pickerOptions.returnData();
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
                options3Items.clear();

                for (int i = 0; i < 100; i++) {
                    options1Items.add(i + "");
                    options2Items.add(i + "");
                    options3Items.add(i + "");
                }

                builder = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {

                        try {
                            String crore = options1Items.get(options1).toString(), lakh = options2Items.get(options2), thousand = options3Items.get(options3);
                            Log.i(TAG, "onOptionsSelect: CRORE " + crore + " LAKH: " + lakh + " THOUSAND: " + thousand);

                            crore = crore.length() == 1 ? "0" + crore : crore;
                            lakh = lakh.length() == 1 ? "0" + lakh : lakh;
                            thousand = thousand.length() == 1 ? "0" + thousand : thousand;

                            if (selectedPickerTask == PICKER_TASK.RENT) {
                                expectedRentAmount = Double.parseDouble(crore + lakh + thousand+"000");
                                rentAmountTV.setText(rupeeSymbol + " " + crore + "," + lakh + "," + thousand + "," + "000");
                                rentAmountWordTV.setText(String.valueOf(numToWords.convert(Integer.parseInt(crore + lakh + thousand + "000"))));
                            } else if (selectedPickerTask == PICKER_TASK.DEPOSIT) {
                                expectedDepositAmount = Double.parseDouble(crore + lakh + thousand+"000");
                                depositAmountTV.setText(rupeeSymbol + " " + crore + "," + lakh + "," + thousand + "," + "000");
                                depositWordsTV.setText(String.valueOf(numToWords.convert(Integer.parseInt(crore + lakh + thousand + "000"))));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                        .setLayoutRes(R.layout.rent_container_layout, RENT_Your_ResidentialFragment.this);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            pickerOptions = builder.build();
            pickerOptions.setNPicker(options1Items, options2Items, options3Items);
            Log.i(TAG, "onPostExecute: ");
        }
    }
}
