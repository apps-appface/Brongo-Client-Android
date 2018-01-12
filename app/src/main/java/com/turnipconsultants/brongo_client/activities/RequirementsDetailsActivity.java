package com.turnipconsultants.brongo_client.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.adapters.RequirementsDetailsRecycAdapter;
import com.turnipconsultants.brongo_client.models.RequireDetailsModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.Utils;
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

public class RequirementsDetailsActivity extends AppCompatActivity {

    private Context context = this;
    private static final String BUY_RENT_MODEL = "object";

    @BindView(R.id.reqRecycler)
    RecyclerView reqRecycler;

    private RequirementsDetailsRecycAdapter reqAdapter;
    private ArrayList<RequireDetailsModel> reqList;
    private Object object;
    private DecimalFormat df = new DecimalFormat("#.##");

    @BindView(R.id.title)
    BrongoTextView title;

    @BindView(R.id.reset)
    BrongoTextView reset;

    @BindView(R.id.titleBadge)
    BrongoTextView titleBadge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_details2);
        ButterKnife.bind(this);
        initValues();
        object = getIntent().getExtras().getParcelable(BUY_RENT_MODEL);
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
    }

    private void setValues() {

        if (object instanceof SecondLandingResponseModel.Data.BuyAndRent) {

            SecondLandingResponseModel.Data.BuyAndRent buyAndRent = (SecondLandingResponseModel.Data.BuyAndRent) object;
            switch (buyAndRent.getPostingType()) {
                case AppConstants.POSTING_TYPE.BUY_A_PROPERTY:
                    if (buyAndRent.getPropertyType().equalsIgnoreCase("RESIDENTIAL")) {
                        titleBadge.setText(AllUtils.StringUtilsBrongo.toCamelCase(buyAndRent.getPostingType()) + "/" + AllUtils.StringUtilsBrongo.toCamelCase(buyAndRent.getPropertyType()));
                        reqList.add(new RequireDetailsModel("Location", buyAndRent.getMicroMarketName(), false));
                        reqList.add(new RequireDetailsModel("Property Type", buyAndRent.getSubPropertyType(), true));
                        reqList.add(new RequireDetailsModel("Property Status", buyAndRent.getPropertyStatus(), true));
                        reqList.add(new RequireDetailsModel("Bedroom", buyAndRent.getBedRoomType(), true));
                        reqList.add(new RequireDetailsModel("Budget", Utils.Budget(df, buyAndRent.getBudgetRange1() + "") + " - " + Utils.Budget(df, buyAndRent.getBudgetRange2() + ""), false));
                        reqList.add(new RequireDetailsModel("Other requirements", buyAndRent.getComments(), false));
                    } else if (buyAndRent.getPropertyType().equalsIgnoreCase("COMMERCIAL")) {
                        titleBadge.setText(AllUtils.StringUtilsBrongo.toCamelCase(buyAndRent.getPostingType()) + "/" + AllUtils.StringUtilsBrongo.toCamelCase(buyAndRent.getPropertyType()));
                        reqList.add(new RequireDetailsModel("Location", buyAndRent.getMicroMarketName(), false));
                        reqList.add(new RequireDetailsModel("Property Type", buyAndRent.getSubPropertyType(), true));
                        reqList.add(new RequireDetailsModel("Property Status", buyAndRent.getPropertyStatus(), true));
                        reqList.add(new RequireDetailsModel("Required Size", buyAndRent.getReSquareFeetRange1() + "Sqft -" + buyAndRent.getReSquareFeetRange2()+"Sqft", false));
                        reqList.add(new RequireDetailsModel("Floor", buyAndRent.getPreferredFloors(), true));
                        reqList.add(new RequireDetailsModel("Budget", Utils.Budget(df, buyAndRent.getBudgetRange1() + "") + " - " + Utils.Budget(df, buyAndRent.getBudgetRange2() + ""), false));
                        reqList.add(new RequireDetailsModel("Car Parking", buyAndRent.getCarParking(), true));
                        reqList.add(new RequireDetailsModel("Other requirements", buyAndRent.getComments(), false));
                    }
                    reqAdapter.notifyDataSetChanged();
                    break;
                case AppConstants.POSTING_TYPE.RENT_A_PROPERTY:
                    if (buyAndRent.getPropertyType().equalsIgnoreCase("RESIDENTIAL")) {
                        titleBadge.setText(AllUtils.StringUtilsBrongo.toCamelCase(buyAndRent.getPostingType()) + "/" + AllUtils.StringUtilsBrongo.toCamelCase(buyAndRent.getPropertyType()));
                        reqList.add(new RequireDetailsModel("Location", buyAndRent.getMicroMarketName(), false));
                        reqList.add(new RequireDetailsModel("Property Type", buyAndRent.getSubPropertyType(), true));
                        reqList.add(new RequireDetailsModel("Bedroom", buyAndRent.getBedRoomType(), true));
                        reqList.add(new RequireDetailsModel("Budget", Utils.Budget(df, buyAndRent.getBudgetRange1() + "") + " - " + Utils.Budget(df, buyAndRent.getBudgetRange2() + ""), false));
                        reqList.add(new RequireDetailsModel("Tenant Type", buyAndRent.getMaritalStatus(), true));
                        reqList.add(new RequireDetailsModel("Preferred Project", buyAndRent.getPreferredProjects(), false));
                        reqList.add(new RequireDetailsModel("Avoid Project", buyAndRent.getAvoidProjects(), false));
                        reqList.add(new RequireDetailsModel("House Orientation", buyAndRent.getOrientation(), true));
                        reqList.add(new RequireDetailsModel("Furnishing", buyAndRent.getFurnishingStatus(), true));
                        reqList.add(new RequireDetailsModel("Other requirements", buyAndRent.getComments(), false));
                    } else if (buyAndRent.getPropertyType().equalsIgnoreCase("COMMERCIAL")) {
                        titleBadge.setText(AllUtils.StringUtilsBrongo.toCamelCase(buyAndRent.getPostingType()) + "/" + AllUtils.StringUtilsBrongo.toCamelCase(buyAndRent.getPropertyType()));
                        reqList.add(new RequireDetailsModel("Location", buyAndRent.getMicroMarketName(), false));
                        reqList.add(new RequireDetailsModel("Property Type", buyAndRent.getSubPropertyType(), true));
                        reqList.add(new RequireDetailsModel("Required Size", buyAndRent.getReSquareFeetRange1() + "-" + buyAndRent.getReSquareFeetRange2(), false));
                        reqList.add(new RequireDetailsModel("Floor", buyAndRent.getPreferredFloors(), true));
                        reqList.add(new RequireDetailsModel("Budget", Utils.Budget(df, buyAndRent.getBudgetRange1() + "") + " - " + Utils.Budget(df, buyAndRent.getBudgetRange2() + ""), false));
                        reqList.add(new RequireDetailsModel("Preferred Project", buyAndRent.getPreferredProjects(), false));
                        reqList.add(new RequireDetailsModel("Avoid Project", buyAndRent.getAvoidProjects(), false));
                        reqList.add(new RequireDetailsModel("Other requirements", buyAndRent.getComments(), false));
                    }
                    reqAdapter.notifyDataSetChanged();
                    break;

            }


        } else if (object instanceof SecondLandingResponseModel.Data.SellAndRentOut) {

            SecondLandingResponseModel.Data.SellAndRentOut sellAndRentOut = (SecondLandingResponseModel.Data.SellAndRentOut) object;
            switch (sellAndRentOut.getPostingType()) {
                case AppConstants.POSTING_TYPE.SELL_A_PROPERTY:
                    if (sellAndRentOut.getPropertyType().equalsIgnoreCase("RESIDENTIAL")) {
                        titleBadge.setText(AllUtils.StringUtilsBrongo.toCamelCase(sellAndRentOut.getPostingType()) + "/" + AllUtils.StringUtilsBrongo.toCamelCase(sellAndRentOut.getPropertyType()));
                        reqList.add(new RequireDetailsModel("Location", sellAndRentOut.getMicroMarketName(), false));
                        reqList.add(new RequireDetailsModel("Name of the Project", sellAndRentOut.getProjectName(), false));
                        reqList.add(new RequireDetailsModel("Property Type", sellAndRentOut.getSubPropertyType(), true));
                        reqList.add(new RequireDetailsModel("Property Status", sellAndRentOut.getPropertyStatus(), true));
                        reqList.add(new RequireDetailsModel("Bedroom", sellAndRentOut.getBedRoomType(), true));
                        reqList.add(new RequireDetailsModel("Expected Price", Utils.Budget(df, sellAndRentOut.getBudget() + ""), false));
                        reqList.add(new RequireDetailsModel("Furnishing", sellAndRentOut.getFurnishingStatus(), true));
                        reqList.add(new RequireDetailsModel("Floor", sellAndRentOut.getAvailableFloors(), true));
                        reqList.add(new RequireDetailsModel("House Orientation", sellAndRentOut.getOrientation(), true));
                        reqList.add(new RequireDetailsModel("Other requirements", sellAndRentOut.getComments(), false));
                    } else if (sellAndRentOut.getPropertyType().equalsIgnoreCase("COMMERCIAL")) {
                        titleBadge.setText(AllUtils.StringUtilsBrongo.toCamelCase(sellAndRentOut.getPostingType()) + "/" + AllUtils.StringUtilsBrongo.toCamelCase(sellAndRentOut.getPropertyType()));
                        reqList.add(new RequireDetailsModel("Location", sellAndRentOut.getMicroMarketName(), false));
                        reqList.add(new RequireDetailsModel("Property Type", sellAndRentOut.getSubPropertyType(), true));
                        reqList.add(new RequireDetailsModel("Property Status", sellAndRentOut.getPropertyStatus(), true));
                        reqList.add(new RequireDetailsModel("Area (In Sqft)", sellAndRentOut.getTotalBuildUpAreaInSquareFT(), false));
                        reqList.add(new RequireDetailsModel("Expected Price", Utils.Budget(df, sellAndRentOut.getBudget()), false));
                        reqList.add(new RequireDetailsModel("Floor", sellAndRentOut.getFloors(), true));
                        reqList.add(new RequireDetailsModel("Current Status", sellAndRentOut.getPossessionStatus(), true));
                        reqList.add(new RequireDetailsModel("Possession By", sellAndRentOut.getPossessionBy(), false));
                        reqList.add(new RequireDetailsModel("Other requirements", sellAndRentOut.getComments(), false));

                    }
                    reqAdapter.notifyDataSetChanged();
                    break;
                case AppConstants.POSTING_TYPE.RENT_YOUR_PROPERTY:
                    if (sellAndRentOut.getPropertyType().equalsIgnoreCase("RESIDENTIAL")) {
                        titleBadge.setText(AllUtils.StringUtilsBrongo.toCamelCase(sellAndRentOut.getPostingType()) + "/" + AllUtils.StringUtilsBrongo.toCamelCase(sellAndRentOut.getPropertyType()));
                        reqList.add(new RequireDetailsModel("Location", sellAndRentOut.getMicroMarketName(), false));
                        reqList.add(new RequireDetailsModel("Name of the Project", sellAndRentOut.getProjectName(), false));
                        reqList.add(new RequireDetailsModel("Property Type", sellAndRentOut.getSubPropertyType(), true));
                        reqList.add(new RequireDetailsModel("Bedroom", sellAndRentOut.getBedRoomType(), true));
                        reqList.add(new RequireDetailsModel("Expected Rent", Utils.Budget(df, sellAndRentOut.getExpectedRent()), false));
                        reqList.add(new RequireDetailsModel("Expected Deposit", Utils.Budget(df, sellAndRentOut.getExpectedDeposit()), false));
                        reqList.add(new RequireDetailsModel("House Orientation", sellAndRentOut.getOrientation(), true));
                        reqList.add(new RequireDetailsModel("Tenant Type", sellAndRentOut.getTenantType(), true));
                        reqList.add(new RequireDetailsModel("Availability Status", sellAndRentOut.getAvailabilityStatus(), true));
                        reqList.add(new RequireDetailsModel("Furnishing", sellAndRentOut.getFurnishingStatus(), true));
                        reqList.add(new RequireDetailsModel("Other requirements", sellAndRentOut.getComments(), false));
                    } else if (sellAndRentOut.getPropertyType().equalsIgnoreCase("COMMERCIAL")) {
                        titleBadge.setText(AllUtils.StringUtilsBrongo.toCamelCase(sellAndRentOut.getPostingType()) + "/" + AllUtils.StringUtilsBrongo.toCamelCase(sellAndRentOut.getPropertyType()));
                        reqList.add(new RequireDetailsModel("Location", sellAndRentOut.getMicroMarketName(), false));
                        reqList.add(new RequireDetailsModel("Property Type", sellAndRentOut.getSubPropertyType(), true));
                        reqList.add(new RequireDetailsModel("Area (In Sqft)", sellAndRentOut.getAvailableSquareFeet(), false));
                        reqList.add(new RequireDetailsModel("Floor", sellAndRentOut.getAvailableFloors(), true));
                        reqList.add(new RequireDetailsModel("Expected Rent", Utils.Budget(df, sellAndRentOut.getExpectedRent()), false));
                        reqList.add(new RequireDetailsModel("Expected Deposit", Utils.Budget(df, sellAndRentOut.getExpectedDeposit()), false));
                        reqList.add(new RequireDetailsModel("Available Status", sellAndRentOut.getPossessionStatus(), true));
                        reqList.add(new RequireDetailsModel("Possession By", sellAndRentOut.getPossessionBy(), false));
                        reqList.add(new RequireDetailsModel("Other requirements", sellAndRentOut.getComments(), false));
                    }
                    reqAdapter.notifyDataSetChanged();
                    break;
            }

        }
    }

    public void endActivity(View v) {
        finish();
    }
}
