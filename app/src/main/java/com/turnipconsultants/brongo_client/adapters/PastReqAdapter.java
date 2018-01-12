package com.turnipconsultants.brongo_client.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.turnipconsultants.brongo_client.CustomWidgets.BrongoButton;
import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;
import com.turnipconsultants.brongo_client.others.Utils;
import com.turnipconsultants.brongo_client.responseModels.PastRequirementResponse;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pankaj on 26-12-2017.
 */

public class PastReqAdapter extends RecyclerView.Adapter<PastReqAdapter.PastReqViewHolder> {

    private Context context;
    private List<PastRequirementResponse.Dropped> pastRequirementList;
    private String rs = "";
    private DecimalFormat df = new DecimalFormat("#.##");

    public class PastReqViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.nameBTV)
        BrongoTextView nameBTV;

        @BindView(R.id.postTypeBTV)
        BrongoTextView postTypeBTV;

        @BindView(R.id.commissionBTV)
        BrongoTextView commissionBTV;

        @BindView(R.id.property_detail_FL)
        TagFlowLayout propertyDetailFL;

        @BindView(R.id.restartSearchBTN)
        BrongoButton restartSearchBTN;

        public PastReqViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public PastReqAdapter(Context context, ArrayList<PastRequirementResponse.Dropped> pastRequirementList) {
        this.context = context;
        this.pastRequirementList = pastRequirementList;
        this.rs = context.getResources().getString(R.string.rupee);
    }

    @Override
    public PastReqViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.past_requirement_row, parent, false);

        return new PastReqViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PastReqViewHolder holder, final int position) {
        PastRequirementResponse.Dropped item = pastRequirementList.get(position);
        holder.nameBTV.setText(item.getBrokerName());
        holder.postTypeBTV.setText(item.getPostingType() + "/" + AllUtils.StringUtilsBrongo.toCamelCase(item.getPropertyType()));
        holder.commissionBTV.setText(rs + " 5600");
        ArrayList<String> flowList = new ArrayList<>();
        switch (item.getPostingType()) {
            case AppConstants.POSTING_TYPE.BUY_A_PROPERTY: {
                String microMarket = item.getMicroMarketName();
                String rentalType = item.getRentalType();
                String bedroomType = item.getRentalType();
                String propertyStatus = item.getPropertyStatus();
                int budget1 = item.getBudgetRange1();
                int budget2 = item.getBudgetRange2();
                if (!microMarket.isEmpty())
                    flowList.add(microMarket);
                if (!rentalType.isEmpty())
                    flowList.add(rentalType);
                if (!bedroomType.isEmpty())
                    flowList.add(bedroomType);
                if (!propertyStatus.isEmpty())
                    flowList.add(propertyStatus);
                flowList.add(Utils.Budget(df, budget1 + "") + "-" + Utils.Budget(df, budget2 + ""));

                break;
            }
            case AppConstants.POSTING_TYPE.RENT_A_PROPERTY: {
                String microMarket = item.getMicroMarketName();
                String rentalType = item.getRentalType();
                String bedroomType = item.getRentalType();
                String subPropertyType = item.getSubPropertyType();
                int budget1 = item.getBudgetRange1();
                int budget2 = item.getBudgetRange2();
                if (!microMarket.isEmpty())
                    flowList.add(microMarket);
                if (!rentalType.isEmpty())
                    flowList.add(rentalType);
                if (!bedroomType.isEmpty())
                    flowList.add(bedroomType);
                if (!subPropertyType.isEmpty())
                    flowList.add(subPropertyType);
                flowList.add(Utils.Budget(df, budget1 + "") + "-" + Utils.Budget(df, budget2 + ""));

                break;
            }
            case AppConstants.POSTING_TYPE.SELL_A_PROPERTY: {
                String microMarket = item.getMicroMarketName();
                String subPropType = item.getSubPropertyType();
                String bedroomType = item.getBedRoomType();
                int budget1 = item.getBudgetRange1();
                int budget2 = item.getBudgetRange2();
                if (!microMarket.isEmpty())
                    flowList.add(microMarket);
                if (!subPropType.isEmpty())
                    flowList.add(subPropType);
                if (!bedroomType.isEmpty())
                    flowList.add(bedroomType);
                flowList.add(Utils.Budget(df, budget1 + "") + "-" + Utils.Budget(df, budget2 + ""));

                break;
            }
            case AppConstants.POSTING_TYPE.RENT_YOUR_PROPERTY: {
                break;
            }
        }

        holder.propertyDetailFL.setAdapter(new TagAdapter<String>(flowList) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.second_tv,
                        holder.propertyDetailFL, false);
                tv.setText(s);
                return tv;
            }

        });
        holder.restartSearchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllUtils.ToastUtils.showToast(context, "" + position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pastRequirementList.size();
    }
}