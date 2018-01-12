package com.turnipconsultants.brongo_client.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.adapters.PaymentSubscriptionAdapter;
import com.turnipconsultants.brongo_client.models.SubscriptionModel;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class PaymentSubscriptionActivity extends AppCompatActivity {

    @BindView(R.id.subscription_recycler_view)
    RecyclerView subscriptionRV;

    @BindView(R.id.reset)
    TextView toolBarReset;

    @BindView(R.id.title)
    TextView toolBarTitle;

    @BindView(R.id.userPicCIV)
    CircleImageView userPicCIV;

    @BindView(R.id.userNameBTV)
    TextView userNameBTV;

    @BindView(R.id.userRatingTV)
    TextView userRatingTV;

    @BindView(R.id.userRB)
    RatingBar userRB;

    private ArrayList<SubscriptionModel> arrayList;
    private Context context;
    private Unbinder unbinder;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_and_subscription);
        unbinder = ButterKnife.bind(this);
        initPage();
        getDummyList();

        toolBarReset.setVisibility(View.GONE);
        toolBarTitle.setText("PAYMENT & SUBSCRIPTION");
        subscriptionRV.setAdapter(new PaymentSubscriptionAdapter(context, arrayList));
    }

    private void initPage() {
        context = this;
        pref = this.getSharedPreferences(AppConstants.PREF_NAME, MODE_PRIVATE);
        LoadProfileImage();
    }

    private void LoadProfileImage() {
        try {
            String profilePicFilePath = pref.getString(AppConstants.PREFS.USER_PROFILE_PIC_FILE, "");
            if (!profilePicFilePath.isEmpty() && new File(profilePicFilePath).exists()) {
                if (userPicCIV != null) {
                    File file = new File(profilePicFilePath);
                    Bitmap profilePic = BitmapFactory.decodeFile(file.getAbsolutePath());
                    userPicCIV.setImageBitmap(profilePic);
                }
            }

            userNameBTV.setText(pref.getString(AppConstants.PREFS.USER_FIRST_NAME, "") + " " + pref.getString(AppConstants.PREFS.USER_LAST_NAME, ""));
            userRatingTV.setText(pref.getFloat(AppConstants.PREFS.USER_RATING, 0.0f) + "");
            userRB.setRating(pref.getFloat(AppConstants.PREFS.USER_RATING, 0.0f));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDummyList() {
        arrayList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            SubscriptionModel subscriptionModel = new SubscriptionModel();
            if (i < 2) {
                subscriptionModel.setLine1("For Posting " + i + " Requirement");
                subscriptionModel.setLine2(getString(R.string.rupee) + 999 + i);
                subscriptionModel.setType(AppConstants.PAYMENT_ROW_TYPE.SINGLE_TYPE);
            } else {
                subscriptionModel.setLine1("For Posting " + i + " Requirement");
                subscriptionModel.setLine21(getString(R.string.rupee) + 999 + i);
                subscriptionModel.setLine22(getString(R.string.rupee) + 999 + i);
                subscriptionModel.setLine31("Save: " + 14 + i + "%");
                subscriptionModel.setLine32("Price per requirement: " + getString(R.string.rupee) + 848 + i);
                subscriptionModel.setType(AppConstants.PAYMENT_ROW_TYPE.MIX_TYPE);
            }
            arrayList.add(subscriptionModel);
        }
    }

    public void endActivity(View v) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }
}
