package com.turnipconsultants.brongo_client.fragments.SellYourProperty;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.applozic.mobicommons.file.FileUtils;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;
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
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.activities.BrokersMapActivity;
import com.turnipconsultants.brongo_client.activities.PaymentSubscriptionActivity;
import com.turnipconsultants.brongo_client.architecture.SellLandComViewModel;
import com.turnipconsultants.brongo_client.architecture.SellLandResViewModel;
import com.turnipconsultants.brongo_client.fragments.BaseFragment;
import com.turnipconsultants.brongo_client.fragments.BuyAProperty.BUY_Land_CommercialFragment;
import com.turnipconsultants.brongo_client.models.GooglePlacesModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.AllUtils.NumToWords;
import com.turnipconsultants.brongo_client.others.CommissionDialogFactory;
import com.turnipconsultants.brongo_client.others.CommonApiUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.ImageCaptureUtils;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.FetchMicroMarketResponse;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;
import com.turnipconsultants.brongo_client.responseModels.PropertyTransactionResponseModel;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by mohit on 23-01-2018.
 */

public class SELL_Your_Land_ResidentialFragment extends BaseFragment implements CommissionListenerFactory.SellCommissionListener, NoInternetTryConnectListener, CustomListener, AllUtils.RequestReachedListener {
    private static final String TAG = BUY_Land_CommercialFragment.class.getSimpleName();
    public static final int REQUEST_CAMERA_AND_WRITABLE_PERMISSIONS = 111;
    public static final int REQUEST_DEVICE_ID_PERMISSIONS = 112;
    private static final int REQUEST_CAMERA = 200;
    private static final int SELECT_FILE = 201;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @BindView(R.id.connect_BTN)
    public Button connectBtn;

    @BindView(R.id.live_locations_FL)
    TagFlowLayout liveLocationsFL;


    @BindView(R.id.other_req_ET)
    EditText commentsET;

    @BindView(R.id.housing_orientation_FL)
    TagFlowLayout housing_orientation_FL;

    @BindView(R.id.area_ET)
    EditText areaET;

    @BindView(R.id.expected_rent_ET)
    EditText expectedRentET;

    @BindView(R.id.select_picture_RL)
    RelativeLayout selectPictureRL;

    @BindView(R.id.img1)
    ImageView IV1;

    @BindView(R.id.img2)
    ImageView IV2;

    @BindView(R.id.img3)
    ImageView IV3;

    @BindView(R.id.expected_amount_TV3)
    BrongoTextView expected_amount_TV;

    @BindView(R.id.property_types_FL)
    TagFlowLayout propertytypesFL;

    @BindView(R.id.plot_types_FL)
    TagFlowLayout plottypesFL;

    @BindView(R.id.other_plot_ET)
    EditText otherplotET;

    @BindView(R.id.gated_ET)
    EditText gatedET;

    @BindView(R.id.plot_TV)
    BrongoTextView plotTV;

    @BindView(R.id.different_locations_TV)
    BrongoTextView diffLocation;

    @BindView(R.id.cancel1)
    ImageView cancel1;

    @BindView(R.id.cancel2)
    ImageView cancel2;

    @BindView(R.id.cancel3)
    ImageView cancel3;

    private String[] houseOrientationArray = new String[]{"East", "West", "North", "South", "North-East", "South-East", "North-West", "South-West", "Any"};
    private String[] propertyType = new String[]{"Independent Plot", "Gated community Plot"};
    private String[] plotType = new String[]{"BDA", "A khata", "B Khata", "Agricultural Land", "Industrial Land"};
    private Context context;
    private String liveLocStr = "",
            orientationStr = "", propTypeStr = "", plotTypeStr = "",
            otherReq = "", microMarketId = "", plotSizeSTr = "", budgetStr = "", commission = "", plotETStr = "", gatedETSTr = "";

    private TagAdapter<String> mAdapter;
    private FetchMicroMarketResponse microMarketResponse;
    private ArrayList<String> marketName, marketId;
    private ArrayList<Integer> brokersCountList;
    private ArrayList<List<String>> coordinatesList;
    private ArrayList<String> coordinates;
    private String headerToken, headerDeviceId, headerPlatform;
    private SharedPreferences pref;
    private ArrayAdapter unitAdapter;
    private String[] unitsArray = new String[]{"Sqft", "Acre"};
    private MultipartBody.Part img1, img2, img3;
    private HashMap<String, RequestBody> map;
    private Uri selectedImgUri, uriImg1, uriImg2, uriImg3;
    private List<String> listPermissionsNeeded;
    SellLandResViewModel viewModel;
    private OptionsPickerView RentOptions;

    private ArrayList<String> options1Items = new ArrayList<>();
    private ArrayList<String> options2Items = new ArrayList<>();
    private ArrayList<String> options3Items = new ArrayList<>();
    private NumToWords numToWords;
    private double expectedRentAmount;
    private DecimalFormat df = new DecimalFormat("#.##");
    @BindString(R.string.rupee)
    String rupeeSymbol;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SellLandResViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sell_land_residential, container, false);
        ButterKnife.bind(this, v);
        initValues();
        setFLAdapters();
        new BudgetTask().execute();
        viewModel.getApiResponseData().observeForever(modelObserver);
        return v;
    }

    private void initValues() {
        context = getActivity();
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        marketId = new ArrayList<>();
        marketName = new ArrayList<>();
        brokersCountList = new ArrayList<>();
        coordinatesList = new ArrayList<>();
        numToWords = new NumToWords();
        expectedRentAmount = 0;
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
    }

    private void DecideSubmitButtonColor() {
        if (!liveLocStr.isEmpty() && !orientationStr.isEmpty()) {
            connectBtn.setBackgroundColor(getResources().getColor(R.color.appPurple));
        } else {
            connectBtn.setBackgroundColor(getResources().getColor(R.color.appButton));
        }
    }

    private void getUserValues() {
        otherReq = commentsET.getText().toString() != null ? commentsET.getText().toString() : "";
        plotSizeSTr = areaET.getText().toString() != null ? areaET.getText().toString() : "";
        plotETStr = otherplotET.getText().toString() != null ? otherplotET.getText().toString() : "";
        gatedETSTr = gatedET.getText().toString() != null ? gatedET.getText().toString() : "";
//        budgetStr = expectedRentET.getText().toString() != null ? expectedRentET.getText().toString() : "";
    }

    @OnClick(R.id.select_picture_RL)
    void OnSelectPictureClick() {
        if (checkCameraAndWritablePermission()) {
            if (uriImg1 != null && uriImg2 != null && uriImg3 != null) {
                AllUtils.ToastUtils.showToast(context, "Maximum 3 images are allowed.");
                return;
            }
            ImageCaptureUtils.selectImageAlert(context, this, REQUEST_CAMERA, SELECT_FILE);
        } else {
            requestCameraAndWritablePermission();
        }
    }

    @OnClick(R.id.connect_BTN)
    void OnSubmitClick() {
        if (isReadDeviceIdAllowed()) {
            if (!liveLocStr.isEmpty() && !orientationStr.isEmpty()) {
                CommissionDialogFactory.SellCommissionDialog(context, this);
            } else {
                Toast.makeText(context, "Please select all details", Toast.LENGTH_SHORT).show();
            }
        } else {
            requestDeviceIdPermission();
        }
    }

    @OnClick(R.id.expected_rent_ET)
    void onRentSelect() {
        RentOptions.show();
    }


    private void getMultipartDetails() {
        if (expectedRentAmount == 0) {
            AllUtils.ToastUtils.showToast(context, "Please select expected price");
            return;
        }

        img1 = uriImg1 != null ? prepareFilePart("propertyPicture1", uriImg1) : null;
        img2 = uriImg2 != null ? prepareFilePart("propertyPicture2", uriImg2) : null;
        img3 = uriImg3 != null ? prepareFilePart("propertyPicture3", uriImg3) : null;

        map = new HashMap<>();
        map.put("postingType", RequestBody.create(MultipartBody.FORM, AppConstants.POSTING_TYPE.SELL_A_PROPERTY));
        map.put("clientMobileNo", RequestBody.create(MultipartBody.FORM, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
        map.put("subPropertyType", RequestBody.create(MultipartBody.FORM, AppConstants.SUB_PROPERTY_TYPE.RESIDENTIAL));
        map.put("microMarketId", RequestBody.create(MultipartBody.FORM, microMarketId));
        map.put("comments", RequestBody.create(MultipartBody.FORM, otherReq));
        map.put("expectedPrice", RequestBody.create(MultipartBody.FORM, expectedRentAmount + ""));
        map.put("commission", RequestBody.create(MultipartBody.FORM, commission));
        map.put("propertyType", RequestBody.create(MultipartBody.FORM, "LAND"));
        map.put("plotSize", RequestBody.create(MultipartBody.FORM, plotSizeSTr));
        map.put("orientation", RequestBody.create(MultipartBody.FORM, orientationStr));
        map.put("landType", RequestBody.create(MultipartBody.FORM, propTypeStr));
        if (propTypeStr.equals("Independent Plot")) {
            if (!plotTypeStr.isEmpty() && plotETStr.isEmpty()) {
                map.put("plotType", RequestBody.create(MultipartBody.FORM, plotTypeStr));
            } else if (!plotETStr.isEmpty()) {
                map.put("plotType", RequestBody.create(MultipartBody.FORM, plotETStr));
            }
        } else if (propTypeStr.equals("Gated community Plot")) {
            map.put("plotType", RequestBody.create(MultipartBody.FORM, gatedETSTr));
        }
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
                    bm = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            addToList(selectedImgUri, bm);
        } catch (Exception e) {
            throw e;
        }
    }

    private void addToList(Uri file, Bitmap bitmap) {
        if (uriImg1 == null) {
            uriImg1 = file;
            setInImageView(bitmap, 1);
        } else if (uriImg2 == null) {
            uriImg2 = file;
            setInImageView(bitmap, 2);
        } else if (uriImg3 == null) {
            uriImg3 = file;
            setInImageView(bitmap, 3);
        }

    }

    private void setInImageView(Bitmap bitmap, int num) {
        if (uriImg1 != null && num == 1) {
            IV1.setImageBitmap(bitmap);
            cancel1.setVisibility(View.VISIBLE);
        } else if (uriImg2 != null && num == 2) {
            IV2.setImageBitmap(bitmap);
            cancel2.setVisibility(View.VISIBLE);
        } else if (uriImg3 != null && num == 3) {
            IV3.setImageBitmap(bitmap);
            cancel3.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.cancel1)
    public void cancelimg1() {
        IV1.setImageBitmap(null);
        cancel1.setVisibility(View.GONE);
        uriImg1 = null;
    }

    @OnClick(R.id.cancel2)
    public void cancelimg2() {
        IV2.setImageBitmap(null);
        cancel2.setVisibility(View.GONE);
        uriImg2 = null;
    }

    @OnClick(R.id.cancel3)
    public void cancelimg3() {
        IV3.setImageBitmap(null);
        cancel3.setVisibility(View.GONE);
        uriImg3 = null;
    }

    private void onCaptureImageResult(Intent data) {
        try {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//            IV1.setImageBitmap(thumbnail);
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
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case REQUEST_DEVICE_ID_PERMISSIONS:
                if (grantResults.length > 0) {
                    boolean DeviceIdPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (DeviceIdPermission) {
                        CommissionDialogFactory.SellCommissionDialog(context, this);
                    } else {
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
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
                RentOptions.dismiss();
            }
        });
        v.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RentOptions.dismiss();
                RentOptions.returnData();
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

                            expectedRentAmount = Double.parseDouble(crore + lakh + thousand + "000");
                            expected_amount_TV.setVisibility(View.VISIBLE);
                            expected_amount_TV.setText(String.valueOf(numToWords.convert(Integer.parseInt(crore + lakh + thousand + "000"))));
                            expectedRentET.setText(rupeeSymbol + " " + crore + "," + lakh + "," + thousand + "," + "000");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                        .setLayoutRes(R.layout.rent_container_layout, SELL_Your_Land_ResidentialFragment.this);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            RentOptions = builder.build();
            RentOptions.setNPicker(options1Items, options2Items, options3Items);
            Log.i(TAG, "onPostExecute: ");
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
                } else if(newValue.getStatusCode()==412){
                    AllUtils.showMaxRequestReached(context,newValue.getMessage(),SELL_Your_Land_ResidentialFragment.this);
                }
                else {
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

    @Override
    public void OnReset() {
        liveLocStr = "";
        liveLocationsFL.getAdapter().notifyDataChanged();
        orientationStr = "";
        housing_orientation_FL.getAdapter().notifyDataChanged();
    }

    @Override
    public void Accept() {
        commission = "20";
        getUserValues();
        getMultipartDetails();
        AllUtils.LoaderUtils.showLoader(context);
        viewModel.makeSeverCall(context, headerToken, headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""), map, img1, img2, img3);
    }

    @Override
    public void onTryReconnect() {
        viewModel.makeSeverCall(context, headerToken, headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""), map, img1, img2, img3);
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

    @Override
    public void onRequestReached() {
        Intent intent = new Intent(context, PaymentSubscriptionActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
