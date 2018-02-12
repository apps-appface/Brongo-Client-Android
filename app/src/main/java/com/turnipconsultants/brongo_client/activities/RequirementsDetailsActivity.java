package com.turnipconsultants.brongo_client.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.adapters.RequirementsDetailsRecycAdapter;
import com.turnipconsultants.brongo_client.models.AcceptedBrokersInputModel;
import com.turnipconsultants.brongo_client.models.RequireDetailsModel;
import com.turnipconsultants.brongo_client.mvp.RequirementsPresenterImpl;
import com.turnipconsultants.brongo_client.mvp.RequirementsView;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.AllRequirementsResponse;
import com.turnipconsultants.brongo_client.responseModels.SecondLandingResponseModel;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohit on 29-12-2017.
 */

public class RequirementsDetailsActivity extends AppCompatActivity implements RequirementsView {

    private Context context = this;
    private static final String BUY_RENT_MODEL = "object";

    @BindView(R.id.reqRecycler)
    RecyclerView reqRecycler;

    @BindView(R.id.title)
    BrongoTextView title;

    @BindView(R.id.reset)
    BrongoTextView reset;

    @BindView(R.id.titleBadge)
    BrongoTextView titleBadge;


    private RequirementsDetailsRecycAdapter reqAdapter;
    private ArrayList<RequireDetailsModel> reqList;
    private Object object;
    private DecimalFormat df = new DecimalFormat("#.##");
    private RequirementsPresenterImpl requirementsPresenter;
    private String propertyId, postingType, propertyType, brokerNo, clientNo,subPropertyType;
    private SharedPreferences pref;
    private String headerToken, headerDeviceId, headerPlatform;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_details2);
        ButterKnife.bind(this);
        initValues();
        setValues();
    }

    private void initValues() {
        title.setText("REQUIREMENTS");
        reset.setVisibility(View.GONE);
        reqList = new ArrayList();
        reqAdapter = new RequirementsDetailsRecycAdapter(context, reqList);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        reqRecycler.setLayoutManager(manager);
        reqRecycler.setAdapter(reqAdapter);
        requirementsPresenter = new RequirementsPresenterImpl(this, context);
        pref = getSharedPreferences(AppConstants.PREF_NAME, 0);
    }

    private void setValues() {
        headerDeviceId = Utils.getDeviceId(context);
        headerPlatform = "android";
        headerToken = pref.getString("token", "");
        propertyId = getIntent().getStringExtra("propertyId");
        postingType = getIntent().getStringExtra("postingType");
        propertyType = getIntent().getStringExtra("propertyType");
        brokerNo = getIntent().getStringExtra("brokerNo");
        clientNo = getIntent().getStringExtra("clientNo");
        subPropertyType=getIntent().getStringExtra("subPropertyType");
        AcceptedBrokersInputModel model = new AcceptedBrokersInputModel();
        model.setPropertyId(propertyId);
        model.setPostingType(postingType);
        model.setPropertyType(propertyType);
        model.setClientMobileNo(clientNo);
        model.setBrokerMobileNo(brokerNo);
        model.setSubPropertyType(subPropertyType);
        requirementsPresenter.getAllRequirements(headerToken, headerPlatform, headerDeviceId, model);
    }

    public void endActivity(View v) {
        finish();
    }

    @Override
    public void onSuccessCall(AllRequirementsResponse.DataEntity dataEntity) {

        if (dataEntity != null) {
            if (dataEntity.getPostingType().equals("BUY")) {
                titleBadge.setBackground(getResources().getDrawable(R.drawable.lead_type_buy));
            } else if (dataEntity.getPostingType().equals("RENT")) {
                titleBadge.setBackground(getResources().getDrawable(R.drawable.lead_type_rent));
            } else if (dataEntity.getPostingType().equals("SELL")) {
                titleBadge.setBackground(getResources().getDrawable(R.drawable.lead_type_sell));
            } else if (dataEntity.getPostingType().equals("RENT_OUT")) {
                titleBadge.setBackground(getResources().getDrawable(R.drawable.lead_type_rentout));
            }
            titleBadge.setText(AllUtils.StringUtilsBrongo.toCamelCase(dataEntity.getPostingType()) + "/" + AllUtils.StringUtilsBrongo.toCamelCase(dataEntity.getPropertyType()));
            for (int i = 0; i < dataEntity.getContent().size(); i++) {
                if (dataEntity.getContent().get(i).getContent() != null && !dataEntity.getContent().get(i).getContent().isEmpty())
                    reqList.add(new RequireDetailsModel(dataEntity.getContent().get(i).getTitle(), dataEntity.getContent().get(i).getContent(), false));
            }

            if (dataEntity.getImages().size() > 0) {
                reqList.add(new RequireDetailsModel(dataEntity.getImages()));
            }
            reqAdapter.notifyDataSetChanged();


        }
    }

    @Override
    public void onFailures(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgress(boolean showLoader) {
        if (showLoader) {
            AllUtils.LoaderUtils.showLoader(context);
        } else {
            AllUtils.LoaderUtils.dismissLoader();
        }
    }
}
