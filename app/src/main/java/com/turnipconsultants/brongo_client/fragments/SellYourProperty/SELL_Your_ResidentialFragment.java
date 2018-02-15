package com.turnipconsultants.brongo_client.fragments.SellYourProperty;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
import com.turnipconsultants.brongo_client.Listener.CommissionListenerFactory;
import com.turnipconsultants.brongo_client.Listener.NoInternetTryConnectListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.activities.BrokersMapActivity;
import com.turnipconsultants.brongo_client.activities.PaymentSubscriptionActivity;
import com.turnipconsultants.brongo_client.fragments.BaseFragment;
import com.turnipconsultants.brongo_client.models.GooglePlacesModel;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.AllUtils.NumToWords;
import com.turnipconsultants.brongo_client.others.CommonApiUtils;
import com.turnipconsultants.brongo_client.responseModels.BrokersCountModel;
import com.turnipconsultants.brongo_client.responseModels.FetchMicroMarketResponse;
import com.turnipconsultants.brongo_client.responseModels.GeneralApiResponseModel;
import com.turnipconsultants.brongo_client.responseModels.PropertyTransactionResponseModel;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.CommissionDialogFactory;
import com.turnipconsultants.brongo_client.others.ImageCaptureUtils;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.turnipconsultants.brongo_client.others.Constants.AppConstants.POPULAR_LOCATIONS.BANGALORE;

/**
 * Created by mohit on 18-09-2017.
 */

public class SELL_Your_ResidentialFragment extends BaseFragment implements CommissionListenerFactory.SellCommissionListener, NoInternetTryConnectListener, CustomListener, AllUtils.RequestReachedListener {
    private static final String TAG = "SELL_Your_ResidentialFr";
    public static final int REQUEST_CAMERA_AND_WRITABLE_PERMISSIONS = 111;
    public static final int REQUEST_DEVICE_ID_PERMISSIONS = 112;
    private static final int REQUEST_CAMERA = 200;
    private static final int SELECT_FILE = 201;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @BindString(R.string.rupee)
    String rupeeSymbol;

    @BindView(R.id.popular_locations_FL)
    TagFlowLayout popularLocationsFL;

    @BindView(R.id.property_types_FL)
    TagFlowLayout propertyTypesFL;

    @BindView(R.id.property_status_FL)
    TagFlowLayout propertyStatusFL;

    @BindView(R.id.bedroom_FL)
    TagFlowLayout bedroomFL;

    @BindView(R.id.furnishing_FL)
    TagFlowLayout furnishedFL;

    @BindView(R.id.floor_FL)
    TagFlowLayout floorFL;

    @BindView(R.id.house_orientation_FL)
    TagFlowLayout houseOrientationFL;

    @BindView(R.id.name_of_the_project_ET)
    EditText projectNameET;

    @BindView(R.id.other_req_ET)
    EditText commentsET;

    @BindView(R.id.expected_rent_TV)
    TextView expectedRentTV;

    @BindView(R.id.budget_word_TV)
    TextView budgetWordTV;

    @BindView(R.id.select_picture_RL)
    RelativeLayout selectPictureRL;

    @BindView(R.id.connect_BTN)
    Button connectBrokersBTN;

    @BindView(R.id.img1)
    ImageView propertyPic1;

    @BindView(R.id.img2)
    ImageView propertyPic2;

    @BindView(R.id.img3)
    ImageView propertyPic3;

    @BindView(R.id.different_locations_TV)
    BrongoTextView diffLocation;

    @BindView(R.id.cancel1)
    ImageView cancel1;

    @BindView(R.id.cancel2)
    ImageView cancel2;

    @BindView(R.id.cancel3)
    ImageView cancel3;

    private String[] popularLocArray = BANGALORE;
    private String[] propTypesArray = new String[]{"Apartment/Flat", "House", "Villa", "PG/Hostel", "Independent house", "Any"};
    private String[] propertyStatusArray = new String[]{"Pre launch", "Under Construction", "Ready to move-in-Old", "Ready to move-in(new)"};
    private String[] bedroomArray = new String[]{"1 BHK", "2 BHK", "3 BHK", "4 BHK", "4 BHK+"};
    private String[] furnishingArray = new String[]{"Fully Furnished", "Semi Furnished", "Unfurnished", "Any"};
    private String[] floorArray = new String[]{"Ground floor","1st", "2nd", "3rd", "4th", "Any"};
    private String[] houseOrientationArray = new String[]{"East", "West", "North", "South", "North-East", "South-East", "North-West", "South-West", "Any"};

    private Context context;
    private String headerToken, headerDeviceId, headerPlatform;
    private SharedPreferences pref;
    private String popLocStr = "", propTypeStr = "", propStatusStr = "", bedroomStr = "", floorStr = "Any", microMarketId = "", furnishedStr = "Any", houseOrientationStr = "Any", commission = "";
    private int selectedCount = 0;

    private Uri selectedImgUri, uriImg1, uriImg2, uriImg3;
    Activity activity;
    private List<String> listPermissionsNeeded;


    private String brokerCountJSON;
    private Gson gson;
    List<BrokersCountModel.Data> arrayList;
    private Unbinder unbinder;

    private OptionsPickerView RentOptions;

    private ArrayList<String> options1Items = new ArrayList<>();
    private ArrayList<String> options2Items = new ArrayList<>();
    private ArrayList<String> options3Items = new ArrayList<>();
    private NumToWords numToWords;
    private double expectedRentAmount;
    private DecimalFormat df = new DecimalFormat("#.##");

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
            v = inflater.inflate(R.layout.sell_your_residential_fragment, container, false);
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
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        gson = new Gson();
        connectBrokersBTN.setText("CONNECT TO THE BEST LOCAL BROKERS");
        brokerCountJSON = pref.getString(AppConstants.PREFS.BROKER_COUNT, "");
        Type type = new TypeToken<List<BrokersCountModel.Data>>() {
        }.getType();
        arrayList = gson.fromJson(brokerCountJSON, type);
        numToWords = new NumToWords();
        expectedRentAmount = 0;

        marketId = new ArrayList<>();
        marketName = new ArrayList<>();
        brokersCountList = new ArrayList<>();
        coordinatesList = new ArrayList<>();
        getMicroMarketsData();
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
        propertyStatusFL.setAdapter(new TagAdapter<String>(propertyStatusArray) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, propertyStatusFL, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                propStatusStr = "";
                DecideSubmitButtonColor();
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                propStatusStr = propertyStatusArray[position];
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

        floorFL.setAdapter(new TagAdapter<String>(floorArray) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, floorFL, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                floorStr = "";
                DecideSubmitButtonColor();
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                floorStr = floorArray[position];
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    private void SellProperty() {
        if (expectedRentAmount == 0) {
            AllUtils.ToastUtils.showToast(context, "Please select expected rent");
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
            map.put("postingType", RequestBody.create(okhttp3.MultipartBody.FORM, AppConstants.POSTING_TYPE.SELL_A_PROPERTY));
            map.put("projectName", RequestBody.create(okhttp3.MultipartBody.FORM, projectNameET.getText().toString()));
            map.put("clientMobileNo", RequestBody.create(okhttp3.MultipartBody.FORM, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
            map.put("subPropertyType", RequestBody.create(okhttp3.MultipartBody.FORM, propTypeStr));
//            map.put("microMarketName", RequestBody.create(okhttp3.MultipartBody.FORM, popLocStr));
//            map.put("microMarketCity", RequestBody.create(okhttp3.MultipartBody.FORM, "Bangalore"));
//            map.put("microMarketState", RequestBody.create(okhttp3.MultipartBody.FORM, "Karnataka"));
            map.put("microMarketId", RequestBody.create(okhttp3.MultipartBody.FORM, microMarketId));

            map.put("comments", RequestBody.create(okhttp3.MultipartBody.FORM, commentsET.getText().toString()));
            map.put("bedRoomType", RequestBody.create(okhttp3.MultipartBody.FORM, bedroomStr));
//            map.put("approxSizeSquareFeet", RequestBody.create(okhttp3.MultipartBody.FORM, ""));
            map.put("furnishingStatus", RequestBody.create(okhttp3.MultipartBody.FORM, furnishedStr));
//            map.put("budget", RequestBody.create(okhttp3.MultipartBody.FORM, expectedRentAmount + ""));
            map.put("expectedPrice", RequestBody.create(okhttp3.MultipartBody.FORM, expectedRentAmount + ""));
            map.put("commission", RequestBody.create(okhttp3.MultipartBody.FORM, 2 + ""));
            map.put("availableFloor", RequestBody.create(okhttp3.MultipartBody.FORM, floorStr));
            map.put("orientation", RequestBody.create(okhttp3.MultipartBody.FORM, houseOrientationStr));
//            map.put("plotSize", RequestBody.create(okhttp3.MultipartBody.FORM, ""));
//            map.put("preferredFloors", RequestBody.create(okhttp3.MultipartBody.FORM, ""));
            map.put("propertyStatus", RequestBody.create(okhttp3.MultipartBody.FORM, propStatusStr));
            map.put("propertyType", RequestBody.create(okhttp3.MultipartBody.FORM, "RESIDENTIAL"));
//            map.put("requiredSquareFeet", RequestBody.create(okhttp3.MultipartBody.FORM, ""));
//            map.put("totalBuildUpAreaInSquareFT", RequestBody.create(okhttp3.MultipartBody.FORM, ""));

            RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
            Call<PropertyTransactionResponseModel> call = apiInstance.getSellYourProperty(headerToken, headerPlatform, headerDeviceId, map, img1, img2, img3);
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
                                SellProperty();
                            }  else if (jObjError.getString("statusCode").equals("412")) {
                                AllUtils.showMaxRequestReached(context,jObjError.getString("message"),SELL_Your_ResidentialFragment.this);
                            }else {
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

    @Override
    public void onRequestReached() {
        Intent intent = new Intent(context, PaymentSubscriptionActivity.class);
        startActivity(intent);
        getActivity().finish();
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

    /*private void setInImageView(Bitmap bitmap) {
        if (uriImg1 != null && uriImg2 == null && uriImg3 == null) {
            propertyPic1.setImageBitmap(bitmap);
            cancel1.setVisibility(View.VISIBLE);
        } else if (uriImg2 != null && uriImg1 != null && uriImg3 == null) {
            propertyPic2.setImageBitmap(bitmap);
            cancel2.setVisibility(View.VISIBLE);
        } else if (uriImg3 != null && uriImg1 != null && uriImg2 != null) {
            propertyPic3.setImageBitmap(bitmap);
            cancel3.setVisibility(View.VISIBLE);
        }
    }*/

    private void setInImageView(Bitmap bitmap, int num) {
        if (uriImg1 != null && num == 1) {
            propertyPic1.setImageBitmap(bitmap);
            cancel1.setVisibility(View.VISIBLE);
        } else if (uriImg2 != null && num == 2) {
            propertyPic2.setImageBitmap(bitmap);
            cancel2.setVisibility(View.VISIBLE);
        } else if (uriImg3 != null && num == 3) {
            propertyPic3.setImageBitmap(bitmap);
            cancel3.setVisibility(View.VISIBLE);
        }
    }


    @OnClick(R.id.cancel1)
    public void cancelimg1() {
        propertyPic1.setImageBitmap(null);
        cancel1.setVisibility(View.GONE);
        uriImg1 = null;
    }

    @OnClick(R.id.cancel2)
    public void cancelimg2() {
        propertyPic2.setImageBitmap(null);
        cancel2.setVisibility(View.GONE);
        uriImg2 = null;
    }

    @OnClick(R.id.cancel3)
    public void cancelimg3() {
        propertyPic3.setImageBitmap(null);
        cancel3.setVisibility(View.GONE);
        uriImg3 = null;
    }

    private void onCaptureImageResult(Intent data) {
        try {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//            propertyPic1.setImageBitmap(thumbnail);
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
                        CommissionDialogFactory.SellCommissionDialog(context, this);
                    } else {
                        Toast.makeText(activity, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }

    private void DecideSubmitButtonColor() {
        if (!popLocStr.isEmpty() && !propTypeStr.isEmpty() && !propTypeStr.isEmpty() && !propStatusStr.isEmpty() && !bedroomStr.isEmpty() && !furnishedStr.isEmpty() && !floorStr.isEmpty() && !houseOrientationStr.isEmpty()) {
            connectBrokersBTN.setBackgroundColor(getResources().getColor(R.color.appPurple));
        } else {
            connectBrokersBTN.setBackgroundColor(getResources().getColor(R.color.appButton));
        }
    }

    @OnClick(R.id.select_picture_RL)
    void SelectPicture() {
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

    @OnClick(R.id.expected_rent_TV)
    void onRentSelect() {
        RentOptions.show();
    }

    @OnClick(R.id.connect_BTN)
    void SubmitButton() {
        if (isReadDeviceIdAllowed()) {
            if (!popLocStr.isEmpty() && !propTypeStr.isEmpty() && !propTypeStr.isEmpty() && !propStatusStr.isEmpty() && !bedroomStr.isEmpty() && !furnishedStr.isEmpty() && !floorStr.isEmpty() && !houseOrientationStr.isEmpty()) {
                CommissionDialogFactory.SellCommissionDialog(context, this);
            } else {
                Toast.makeText(context, "Please select all details", Toast.LENGTH_SHORT).show();
            }
        } else {
            requestDeviceIdPermission();
        }
    }

    @Override
    public void Accept() {
        commission = "2";
        Log.i(TAG, "Accept: Commission: " + commission);
        SellProperty();
    }

    @Override
    public void onTryReconnect() {
        SellProperty();
    }

    @Override
    public void OnReset() {

        popLocStr = "";
        popularLocationsFL.getAdapter().notifyDataChanged();

        projectNameET.setText("");

        propTypeStr = "";
        propertyTypesFL.getAdapter().notifyDataChanged();

        propStatusStr = "";
        propertyStatusFL.getAdapter().notifyDataChanged();

        bedroomStr = "";
        bedroomFL.getAdapter().notifyDataChanged();

        expectedRentAmount = 0;
        expectedRentTV.setText("");
        budgetWordTV.setText("");


        furnishedStr = furnishingArray[3];
        furnishedFL.getAdapter().notifyDataChanged();
        furnishedFL.getAdapter().setSelectedList(3);

        floorStr = floorArray[4];
        floorFL.getAdapter().notifyDataChanged();
        floorFL.getAdapter().setSelectedList(4);

        houseOrientationStr = houseOrientationArray[8];
        houseOrientationFL.getAdapter().notifyDataChanged();
        houseOrientationFL.getAdapter().setSelectedList(8);

        uriImg1 = null;
        uriImg2 = null;
        uriImg3 = null;

        propertyPic1.setImageResource(0);
        propertyPic2.setImageResource(0);
        propertyPic3.setImageResource(0);

        commentsET.setText("");
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
                Log.i(TAG, "Expected amount" + expectedRentAmount);

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
                            budgetWordTV.setText(String.valueOf(numToWords.convert(Integer.parseInt(crore + lakh + thousand + "000"))));
                            expectedRentTV.setText(rupeeSymbol + " " + crore + "," + lakh + "," + thousand + "," + "000");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                        .setLayoutRes(R.layout.rent_container_layout, SELL_Your_ResidentialFragment.this);

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
