package com.turnipconsultants.brongo_client.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.turnipconsultants.brongo_client.Listener.HelpFAQFragmentItemClickListener;
import com.turnipconsultants.brongo_client.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohit on 22-09-2017.
 */

public class HelpFaqRecAdapter extends RecyclerView.Adapter<HelpFaqRecAdapter.BrokerReviewViewHolder> {
    private Context context;
    private ArrayList<String> arrayList;
    private LayoutInflater inflater;
    private HelpFAQFragmentItemClickListener mListener;

    public HelpFaqRecAdapter(Context context, ArrayList<String> arrayList, HelpFAQFragmentItemClickListener mListener) {
        this.context = context;
        this.arrayList = arrayList;
        inflater = LayoutInflater.from(context);
        this.mListener = mListener;
    }

    @Override
    public BrokerReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.help_faq_row, parent, false);
        BrokerReviewViewHolder viewHolder = new BrokerReviewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BrokerReviewViewHolder holder, final int position) {
        holder.item.setText(arrayList.get(position));
        holder.rowCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnHelpFAQItemClick(arrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class BrokerReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item)
        TextView item;
        @BindView(R.id.help_faq_row)
        ConstraintLayout rowCL;

        public BrokerReviewViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
