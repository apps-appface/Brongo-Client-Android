package com.turnipconsultants.brongo_client.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.turnipconsultants.brongo_client.CustomWidgets.BrongoButton;
import com.turnipconsultants.brongo_client.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.github.barteksc.pdfviewer.util.FitPolicy;

/**
 * Created by mohit on 08-02-2018.
 */

public class TermsAndConditionsActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {

    private static final String TAG = TermsAndConditionsActivity.class.getSimpleName();
    public static final String FILE_NAME = "tc_users_final.pdf";
    Integer pageNumber = 0;
    private boolean isSignUpPage = false;

    @BindView(R.id.pdfView)
    PDFView pdfView;

    @BindView(R.id.acceptBtn)
    BrongoButton accept;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        ButterKnife.bind(this);
        isSignUpPage = getIntent().getBooleanExtra("isSignUpPage", false);
        displayFromAsset(FILE_NAME);
    }

    private void displayFromAsset(String assetFileName) {
        pdfView.setBackgroundColor(Color.DKGRAY);
        pdfView.fromAsset(FILE_NAME)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .spacing(5) // in dp
                .onPageError(this)
                .pageFitPolicy(FitPolicy.WIDTH)
                .load();
    }

    @OnClick(R.id.acceptBtn)
    public void acceptClick() {
        if (isSignUpPage) {
            finish();
        } else {
            Intent i = new Intent(this, SplashPagerActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageError(int page, Throwable t) {
        Toast.makeText(this, "Cannot load page", Toast.LENGTH_SHORT).show();
    }
}
