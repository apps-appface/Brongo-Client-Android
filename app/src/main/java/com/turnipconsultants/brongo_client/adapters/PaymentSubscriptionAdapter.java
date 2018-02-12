package com.turnipconsultants.brongo_client.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.models.SubscriptionModel;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.turnipconsultants.brongo_client.others.Constants.AppConstants.PAYMENT_ROW_TYPE.MIX_TYPE;
import static com.turnipconsultants.brongo_client.others.Constants.AppConstants.PAYMENT_ROW_TYPE.SINGLE_TYPE;

/**
 * Created by Pankaj on 29-11-2017.
 */

public class PaymentSubscriptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SubscriptionModel> arrayList;
    private Context context;
    private BuyClickListener listener;

    public class Type1Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.line1)
        TextView line1;
        @BindView(R.id.line2)
        TextView line2;
        @BindView(R.id.buy_now)
        Button buyNow;

        public Type1Holder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class Type2Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.line1)
        TextView line1;
        @BindView(R.id.line21)
        TextView line21;
        @BindView(R.id.line22)
        TextView line22;
        @BindView(R.id.line31)
        TextView line31;
        @BindView(R.id.line32)
        TextView line32;
        @BindView(R.id.buy_now)
        Button buyNow;

        public Type2Holder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public PaymentSubscriptionAdapter(Context context, List<SubscriptionModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        listener = (BuyClickListener) context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case SINGLE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_subscription_row_type1, parent, false);
                return new Type1Holder(view);
            case MIX_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_subscription_row_type2, parent, false);
                return new Type2Holder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SubscriptionModel subscriptionModel = arrayList.get(position);
        switch (arrayList.get(position).getType()) {
            case SINGLE_TYPE:
                PaymentSubscriptionAdapter.Type1Holder type1Holder = (Type1Holder) holder;
                type1Holder.line1.setText(subscriptionModel.getLine1());
                type1Holder.line2.setText(subscriptionModel.getLine2());
                type1Holder.buyNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        AllUtils.ToastUtils.showToast(context, "You Clicked " + subscriptionModel.getLine2());
                        listener.onBuyClick(position);
                    }
                });

                break;
            case MIX_TYPE:
                PaymentSubscriptionAdapter.Type2Holder type2Holder = (Type2Holder) holder;
                type2Holder.line1.setText(subscriptionModel.getLine1());
                setStrike(type2Holder.line21);
                type2Holder.line21.setText(subscriptionModel.getLine21());
                type2Holder.line22.setText(subscriptionModel.getLine22());
                type2Holder.line31.setText(subscriptionModel.getLine31());
                type2Holder.line32.setText(subscriptionModel.getLine32());
                type2Holder.buyNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        AllUtils.ToastUtils.showToast(context, "You Clicked " + subscriptionModel.getLine22());
                        listener.onBuyClick(position);
                    }
                });
                break;
        }
    }

    void setStrike(TextView tv) {
        tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {

        switch (arrayList.get(position).getType()) {
            case 1:
                return SINGLE_TYPE;
            case 2:
                return MIX_TYPE;
            default:
                return -1;
        }
    }

    public interface BuyClickListener {
        void onBuyClick(int position);
    }

}