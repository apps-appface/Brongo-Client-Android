package com.special.ResideMenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.activities.MyProfileActivity;
import com.turnipconsultants.brongo_client.activities.RatingActivity;
import com.turnipconsultants.brongo_client.models.TokenInputModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.responseModels.ProfileResponseModel;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.InternetConnection;
import com.turnipconsultants.brongo_client.others.RetrofitAPIs;
import com.turnipconsultants.brongo_client.others.RetrofitBuilders;
import com.turnipconsultants.brongo_client.others.Utils;

import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * User: special
 * Date: 13-12-10
 * Time: 下午11:05
 * Mail: specialcyci@gmail.com
 */
public class ResideMenuItem extends RelativeLayout {
    private static final String TAG = "ResideMenuItem";
    /**
     * menu item  icon
     */
    private ImageView iv_icon;
    /**
     * menu item  title
     */
    private TextView tv_title;

    private CircleImageView profileImage;
    private TextView clientName;
    private TextView rating;
    private TextView request;
    private RatingBar ratingBar;

    public ResideMenuItem(Context context, String mobile, String name) {
        super(context);
//        initViews(context);
        initViewsHead(context, mobile, name);
    }

    public ResideMenuItem(Context context, int icon, int title) {
        super(context);
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);
    }

    public ResideMenuItem(Context context, int icon, String title) {
        super(context);
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.residemenu_item, this);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    private void initViewsHead(final Context context, String mobileNo, String name) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.residemenu_header, this);
        profileImage = findViewById(R.id.profilePicCIV);
        clientName = findViewById(R.id.userName);
        rating = findViewById(R.id.rating);
        ratingBar = findViewById(R.id.ratingBar);

        request = findViewById(R.id.request);
        request.setText(mobileNo);
        clientName.setText(name);
        profileImage.setImageResource(R.drawable.user);

        ratingBar.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Intent intent = new Intent(context, RatingActivity.class);
                    context.startActivity(intent);
                }
                return true;
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyProfileActivity.class);
                context.startActivity(intent);
            }
        });
        getClientProfile(context);
    }

    public void setIcon(int icon) {
        iv_icon.setImageResource(icon);
    }

    /**
     * set the title with resource
     * ;
     *
     * @param title
     */
    public void setTitle(int title) {
        tv_title.setText(title);
    }

    /**
     * set the title with string;
     *
     * @param title
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }


    private void getClientProfile(final Context context) {
        try {
            final String headerDeviceId = Utils.getDeviceId(context);
            final String headerPlatform = "android";
            final SharedPreferences pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
            String headerToken = pref.getString("token", "");
            String firstName = pref.getString(AppConstants.PREFS.USER_FIRST_NAME, "");
            String lastName = pref.getString(AppConstants.PREFS.USER_LAST_NAME, "");
            clientName.setText(firstName + " " + lastName);
            if (InternetConnection.isNetworkAvailable(context)) {
                RetrofitAPIs apiInstance = RetrofitBuilders.getInstance().getAPIService(RetrofitBuilders.getBaseUrl());
                Call<ProfileResponseModel> call = apiInstance.getClientProfile(headerToken, headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, ""));
                call.enqueue(new Callback<ProfileResponseModel>() {
                    @Override
                    public void onResponse(Call<ProfileResponseModel> call, Response<ProfileResponseModel> response) {
                        try {
                            if (response != null && response.isSuccessful()) {
                                ProfileResponseModel responseModel = response.body();
                                if (responseModel.getStatusCode() == 200) {
                                    List<ProfileResponseModel.DataEntity> data = responseModel.getData();
                                    Glide.with(context)
                                            .load(data.get(0).getProfileImage())
                                            .apply(BrongoClientApplication.getRequestOption(false))
                                            .into(profileImage);
                                    String fName = AllUtils.StringUtilsBrongo.toCamelCase(data.get(0).getFirstName());
                                    String lName = AllUtils.StringUtilsBrongo.toCamelCase(data.get(0).getLastName());
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString(AppConstants.PREFS.USER_FIRST_NAME, fName);
                                    editor.putString(AppConstants.PREFS.USER_LAST_NAME, lName);
                                    editor.putFloat(AppConstants.PREFS.USER_RATING, data.get(0).getRating());
                                    editor.putString(AppConstants.PREFS.USER_EMAIL, data.get(0).getEmailId());
                                    editor.commit();
                                    clientName.setText(fName + " " + lName);
                                    request.setText(data.get(0).getActiveRequests() + " Active Request");
                                    ratingBar.setRating(data.get(0).getRating());
                                    rating.setText(data.get(0).getRating() + "");
                                }
                            } else {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                if ((jObjError.getString("message").equals("Invalid Access Token"))) {
                                    new Utils().getTokenRefresh(context, new TokenInputModel(headerPlatform, headerDeviceId, pref.getString(AppConstants.PREFS.USER_MOBILE_NO, "")));
                                    getClientProfile(context);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileResponseModel> call, Throwable t) {
                        Log.e(TAG, "onFailure: ");
                    }
                });
            } else {
                Log.e(TAG, "No Internet to load profile image ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
