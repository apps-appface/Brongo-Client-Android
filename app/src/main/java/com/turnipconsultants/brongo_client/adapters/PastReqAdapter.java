package com.turnipconsultants.brongo_client.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.CustomWidgets.BrongoButton;
import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.responseModels.PastRequirementResponse;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Pankaj on 26-12-2017.
 */

public class PastReqAdapter extends RecyclerView.Adapter<PastReqAdapter.PastReqViewHolder> {

    private Context context;
    private List<PastRequirementResponse.DataEntity> pastRequirementList;
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

//        @BindView(R.id.restartSearchBTN)
//        BrongoButton restartSearchBTN;

        @BindView(R.id.circImage)
        CircleImageView circImage;

        @BindView(R.id.time)
        BrongoTextView date;

        @BindView(R.id.property_Id)
        BrongoTextView propertyId;

        public PastReqViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public PastReqAdapter(Context context, ArrayList<PastRequirementResponse.DataEntity> pastRequirementList) {
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
        PastRequirementResponse.DataEntity item = pastRequirementList.get(position);
        if (item.getStatus().equals("Deal Closed")) {
            holder.circImage.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(item.getBrokerImage())
                    .apply(BrongoClientApplication.getRequestOption(true))
                    .into(holder.circImage);
            holder.nameBTV.setText(item.getBrokerName().toUpperCase());
            if (item.getPostingType().equals("BUY")) {
                holder.postTypeBTV.setBackground(context.getResources().getDrawable(R.drawable.lead_type_buy));
            } else if (item.getPostingType().equals("RENT")) {
                holder.postTypeBTV.setBackground(context.getResources().getDrawable(R.drawable.lead_type_rent));
            } else if (item.getPostingType().equals("SELL")) {
                holder.postTypeBTV.setBackground(context.getResources().getDrawable(R.drawable.lead_type_sell));
            } else if (item.getPostingType().equals("RENT_OUT")) {
                holder.postTypeBTV.setBackground(context.getResources().getDrawable(R.drawable.lead_type_rentout));
            }
            holder.postTypeBTV.setText(item.getPostingType() + "/" + AllUtils.StringUtilsBrongo.toCamelCase(item.getPropertyType()));
            holder.commissionBTV.setText(item.getCommission() + "%");
            holder.date.setText(item.getAddedTime());

            ArrayList<String> flowList = new ArrayList<>();
            flowList.addAll(item.getProperty());

            holder.propertyDetailFL.setAdapter(new TagAdapter<String>(flowList) {

                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.second_tv,
                            holder.propertyDetailFL, false);
                    tv.setText(s);
                    return tv;
                }

            });
        } else {
            holder.circImage.setVisibility(View.GONE);
            holder.nameBTV.setText(item.getStatus().toUpperCase());
            if (item.getPostingType().equals("BUY")) {
                holder.postTypeBTV.setBackground(context.getResources().getDrawable(R.drawable.lead_type_buy));
            } else if (item.getPostingType().equals("RENT")) {
                holder.postTypeBTV.setBackground(context.getResources().getDrawable(R.drawable.lead_type_rent));
            } else if (item.getPostingType().equals("SELL")) {
                holder.postTypeBTV.setBackground(context.getResources().getDrawable(R.drawable.lead_type_sell));
            } else if (item.getPostingType().equals("RENT_OUT")) {
                holder.postTypeBTV.setBackground(context.getResources().getDrawable(R.drawable.lead_type_rentout));
            }
            holder.postTypeBTV.setText(item.getPostingType() + "/" + AllUtils.StringUtilsBrongo.toCamelCase(item.getPropertyType()));
            holder.commissionBTV.setText(item.getCommission() + "%");
            holder.date.setText(item.getAddedTime());

            ArrayList<String> flowList = new ArrayList<>();
            flowList.addAll(item.getProperty());

            holder.propertyDetailFL.setAdapter(new TagAdapter<String>(flowList) {

                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.second_tv,
                            holder.propertyDetailFL, false);
                    tv.setText(s);
                    return tv;
                }

            });
        }

    }

    @Override
    public int getItemCount() {
        return pastRequirementList.size();
    }
}