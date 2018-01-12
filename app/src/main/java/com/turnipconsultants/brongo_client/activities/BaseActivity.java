package com.turnipconsultants.brongo_client.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.turnipconsultants.brongo_client.R;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;

/**
 * Created by Pankaj on 10-11-2017.
 */

public class BaseActivity extends AppCompatActivity {
    private Context context;
    public static final int PERMISSION_REQUEST = 9;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        context = this;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(BaseActivity.this, new String[]{READ_PHONE_STATE, CAMERA, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);

    }


}
