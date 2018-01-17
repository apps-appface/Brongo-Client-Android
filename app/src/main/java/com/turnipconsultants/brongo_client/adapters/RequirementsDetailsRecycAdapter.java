package com.turnipconsultants.brongo_client.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.models.RequireDetailsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohit on 30-12-2017.
 */

public class RequirementsDetailsRecycAdapter extends RecyclerView.Adapter<RequirementsDetailsRecycAdapter.ReqViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<RequireDetailsModel> reqList;

    public RequirementsDetailsRecycAdapter(Context context, ArrayList<RequireDetailsModel> reqList) {
        this.context = context;
        this.reqList = reqList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ReqViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.requirements_child, parent, false);
        ReqViewHolder holder = new ReqViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReqViewHolder holder, int position) {
        holder.title.setText(reqList.get(position).getTitle());
        if (reqList.get(position).isTagFlow()) {
            holder.valueNoBg.setVisibility(View.GONE);
            holder.valueBg.setVisibility(View.VISIBLE);
            holder.imL.setVisibility(View.GONE);
            holder.valueBg.setText(reqList.get(position).getValue());
        } else {
            holder.valueBg.setVisibility(View.GONE);
            holder.valueNoBg.setVisibility(View.VISIBLE);
            holder.imL.setVisibility(View.GONE);
            holder.valueNoBg.setText(reqList.get(position).getValue());
        }

        /*Temperory code. Need to change*/
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        try {
            if (reqList.get(position).getImageUrl().size() > 0) {
                holder.valueBg.setVisibility(View.GONE);
                holder.valueNoBg.setVisibility(View.GONE);
                holder.imL.setVisibility(View.VISIBLE);
                for (int i = 0; i < reqList.get(position).getImageUrl().size(); i++) {
                    Glide
                            .with(context)
                            .load(reqList.get(position).getImageUrl().get(0))
                            .apply(options)
                            .into(holder.pic1);
                    Glide
                            .with(context)
                            .load(reqList.get(position).getImageUrl().get(1))
                            .apply(options)
                            .into(holder.pic2);
                    Glide
                            .with(context)
                            .load(reqList.get(position).getImageUrl().get(2))
                            .apply(options)
                            .into(holder.pic3);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return reqList.size();
    }

    public class ReqViewHolder extends ViewHolder {
        BrongoTextView title, valueBg, valueNoBg;
        @BindView(R.id.pic1)
        ImageView pic1;
        @BindView(R.id.pic2)
        ImageView pic2;
        @BindView(R.id.pic3)
        ImageView pic3;
        @BindView(R.id.pic4)
        ImageView pic4;
        @BindView(R.id.templl)
        LinearLayout imL;

        public ReqViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            valueBg = itemView.findViewById(R.id.valuewithBg);
            valueNoBg = itemView.findViewById(R.id.valuewithoutBG);
            ButterKnife.bind(this,itemView);
        }
    }
}
