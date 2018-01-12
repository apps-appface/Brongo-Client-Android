package com.turnipconsultants.brongo_client.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.turnipconsultants.brongo_client.CustomWidgets.BrongoTextView;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.models.RequireDetailsModel;

import java.util.ArrayList;

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
        inflater=LayoutInflater.from(context);
    }

    @Override
    public ReqViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.requirements_child, parent,false);
        ReqViewHolder holder = new ReqViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReqViewHolder holder, int position) {
        holder.title.setText(reqList.get(position).getTitle());
        if (reqList.get(position).isTagFlow()) {
            holder.valueNoBg.setVisibility(View.GONE);
            holder.valueBg.setVisibility(View.VISIBLE);
            holder.valueBg.setText(reqList.get(position).getValue());
        } else {
            holder.valueBg.setVisibility(View.GONE);
            holder.valueNoBg.setVisibility(View.VISIBLE);
            holder.valueNoBg.setText(reqList.get(position).getValue());
        }
    }

    @Override
    public int getItemCount() {
        return reqList.size();
    }

    public class ReqViewHolder extends ViewHolder {
        BrongoTextView title, valueBg, valueNoBg;

        public ReqViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            valueBg = itemView.findViewById(R.id.valuewithBg);
            valueNoBg = itemView.findViewById(R.id.valuewithoutBG);
        }
    }
}
