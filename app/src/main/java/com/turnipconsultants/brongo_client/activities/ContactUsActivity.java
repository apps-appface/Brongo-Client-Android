package com.turnipconsultants.brongo_client.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context = this;
    public static final int REQUEST_CALL_PERMISSIONS = 501;
    public static String FACEBOOK_URL = "https://www.facebook.com/brongo.in/";
    public static String FACEBOOK_PAGE_ID = "brongo.in";
    public static String YOUTUBE_CHANNEL = "https://www.youtube.com/channel/UC73dLKuzzn8CxFTaOvr-qlw?view_as=subscriber";
    public static String INSTAGRAM_URL = "https://www.instagram.com/brongo4545/";
    public static String INSTAGRAM_SCHEME = "https://www.instagram.com/-u/brongo4545/";
    public static String TWITTER_URL = "https://twitter.com/brongo_in";
    public static String TWITTER_USER_ID = "915894810900234245";
    public static String LINKEDIN_URL = "https://www.linkedin.com/company/13443214/";

    @BindView(R.id.title)
    TextView toolBarTitle;

    @BindView(R.id.reset)
    TextView toolBarReset;

    @BindView(R.id.phoneNumber)
    BrongoTextView phoneNumberTv;

    @BindView(R.id.emailId)
    BrongoTextView emailIdTv;

    @BindView(R.id.callIM1)
    ImageView callIM1;

    @BindView(R.id.callIM2)
    ImageView callIM2;

    @BindView(R.id.facebook)
    ImageView facebook;

    @BindView(R.id.google)
    ImageView google;

    @BindView(R.id.instagram)
    ImageView instagram;

    @BindView(R.id.twitter)
    ImageView twitter;

    @BindView(R.id.linkedin)
    ImageView linkedin;

    @BindView(R.id.botIm)
    ImageView botIm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);

        toolBarReset.setVisibility(View.GONE);
        toolBarTitle.setText("CONTACT US");

        callIM1.setOnClickListener(this);
        callIM2.setOnClickListener(this);
        facebook.setOnClickListener(this);
        google.setOnClickListener(this);
        instagram.setOnClickListener(this);
        twitter.setOnClickListener(this);
        linkedin.setOnClickListener(this);
        botIm.setOnClickListener(this);
    }


    public void endActivity(View v) {
        onBackPressed();
    }

    private boolean checkCallPermission() {
        int permissionCall = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
        if (permissionCall != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestDeviceIdPermission() {
        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSIONS);
    }

    @SuppressLint("MissingPermission")
    private void makeCall() {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumberTv.getText().toString(), null));
            startActivity(callIntent);
        } catch (Exception e) {
            Toast.makeText(context, "No Registered Network", Toast.LENGTH_LONG).show();
        }
    }

    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", emailIdTv.getText().toString(), null));
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }

    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.callIM1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkCallPermission()) {
                        makeCall();
                    } else {
                        requestDeviceIdPermission();
                    }
                } else {
                    makeCall();
                }
                break;
            case R.id.callIM2:
                sendEmail();
                break;
            case R.id.facebook:
                boolean FBInstalled = AllUtils.isPackageInstalled("com.facebook.katana", context);
                if (FBInstalled) {
                    Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                    String facebookUrl = getFacebookPageURL(this);
                    facebookIntent.setData(Uri.parse(facebookUrl));
                    startActivity(facebookIntent);
                } else {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URL));
                    startActivity(i);
                }
                break;
            case R.id.google:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(YOUTUBE_CHANNEL));
                startActivity(intent);
                break;
            case R.id.instagram:
                try {
                    if (AllUtils.isPackageInstalled("com.instagram.android", context)) {
                        Uri uri = Uri.parse(INSTAGRAM_URL);
                        Intent likeIng = new Intent(Intent.ACTION_VIEW);
                        likeIng.setData(uri);
                        likeIng.setPackage("com.instagram.android");
                        startActivity(likeIng);
                    } else {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(INSTAGRAM_URL)));
                        Toast.makeText(context, "else", Toast.LENGTH_SHORT).show();
                    }
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.twitter:
                Intent twitterintent;
                try {
                    if (AllUtils.isPackageInstalled("com.twitter.android", context)) {
                        twitterintent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=" + TWITTER_USER_ID));
                        twitterintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    } else {
                        twitterintent = new Intent(Intent.ACTION_VIEW, Uri.parse(TWITTER_URL));
                    }
                    startActivity(twitterintent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.linkedin:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(LINKEDIN_URL)));
                break;
            case R.id.botIm:
                Intent intentchat = new Intent(context, ConversationActivity.class);
                intentchat.putExtra(ConversationUIService.USER_ID, AppConstants.BOT_ID);
                startActivity(intentchat);
                break;
        }
    }
}
