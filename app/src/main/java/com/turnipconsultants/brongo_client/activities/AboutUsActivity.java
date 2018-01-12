package com.turnipconsultants.brongo_client.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.turnipconsultants.brongo_client.R;

public class AboutUsActivity extends AppCompatActivity {
    private TextView toolBarTitleTV, resetTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        toolBarTitleTV = (TextView) findViewById(R.id.title);
        resetTV = (TextView) findViewById(R.id.reset);
        resetTV.setVisibility(View.INVISIBLE);
        toolBarTitleTV.setText("ABOUT BRONGO");

    }

    public void endActivity(View v) {
        finish();
    }

}
