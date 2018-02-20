package com.turnipconsultants.brongo_client.activities;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.services.RegistrationIntentService;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.turnipconsultants.brongo_client.R.id.textview;

/**
 * Created by mohit on 20-09-2017.
 */

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 8000;
    //    private TextView title, subTitle;
    private SharedPreferences pref;
    private Context context = this;
    @BindView(R.id.splashGif)
    GifImageView newGif;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_splash);
//        title = (TextView) findViewById(textview);
//        subTitle = (TextView) findViewById(R.id.subtitle);
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, 0);
        ButterKnife.bind(this);
        initializeFirebase();
        GifDrawable gifDrawable = null;
        try {
            gifDrawable = new GifDrawable(getResources(), R.drawable.new_splash);
            gifDrawable.setLoopCount(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        newGif.setImageDrawable(gifDrawable);
//        title.setVisibility(View.GONE);
//        subTitle.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pref.getBoolean(AppConstants.PREFS.LOGGED_IN, false) && MobiComUserPreference.getInstance(SplashScreenActivity.this).isLoggedIn()) {
                    Intent intent = new Intent(SplashScreenActivity.this, RegistrationIntentService.class);
                    startService(intent);
                    Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    startActivity(i);
                   /* Intent intent = new Intent(context, FeedbackActivity.class);
                    intent.putExtra("name", "Pankaj Sharma");
                    intent.putExtra("rating", 3.0F);
                    intent.putExtra("pic", "https://i.ytimg.com/vi/2hkFgwYsxck/maxresdefault.jpg");
                    startActivity(intent);*/

                    /*Intent i = new Intent(SplashScreenActivity.this, GetConnectedHomeActivity.class);
                    startActivity(i);*/
                    /*Intent i = new Intent(SplashScreenActivity.this, TopBrokersListActivity.class);
                    startActivity(i);*/
                    /*Intent i = new Intent(SplashScreenActivity.this, BrokersMapActivity.class);
                    startActivity(i);*/
                    /*Intent i = new Intent(SplashScreenActivity.this, TopBrokersListActivity.class);
                    startActivity(i);*/

                    /*Intent intent = new Intent(context, PropertyRouterActivity.class);
                    intent.putExtra(AppConstants.PROPERTY.PROPERTY_TYPE, 2);
                    startActivity(intent);*/
                } else {
                    Intent i = new Intent(SplashScreenActivity.this, TermsAndConditionsActivity.class);
                    startActivity(i);
                }
                finish();
            }
        }, SPLASH_TIME_OUT);

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                title.setVisibility(View.VISIBLE);
                BounceInterpolator bounceInterpolator = new BounceInterpolator();
                ObjectAnimator anim = ObjectAnimator.ofFloat(title, "translationY", 0f, 40);
                anim.setInterpolator(bounceInterpolator);
                anim.setDuration(300).start();
            }
        }, 1500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                subTitle.setVisibility(View.VISIBLE);
                BounceInterpolator bounceInterpolator = new BounceInterpolator();
                ObjectAnimator anim = ObjectAnimator.ofFloat(subTitle, "translationY", 0f, 47);
                anim.setInterpolator(bounceInterpolator);
                anim.setDuration(200).start();
            }
        }, 1800);*/
    }

    private void initializeFirebase() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
    }
}

