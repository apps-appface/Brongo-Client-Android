package com.turnipconsultants.brongo_client.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.turnipconsultants.brongo_client.R;
import java.util.ArrayList;

/**
 * Created by mohit on 22-09-2017.
 */

public class BrokerReviewRecycAdapter extends RecyclerView.Adapter<BrokerReviewRecycAdapter.BrokerReviewViewHolder> {
    private Context context;
    private ArrayList arrayList;
    private LayoutInflater inflater;

    public BrokerReviewRecycAdapter(Context context, ArrayList arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public BrokerReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.child_reviews_broker, parent, false);
        BrokerReviewViewHolder viewHolder = new BrokerReviewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BrokerReviewViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class BrokerReviewViewHolder extends RecyclerView.ViewHolder {


        public BrokerReviewViewHolder(View itemView) {
            super(itemView);
        }
    }

}
