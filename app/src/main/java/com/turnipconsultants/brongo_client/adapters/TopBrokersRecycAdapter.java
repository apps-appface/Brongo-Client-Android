package com.turnipconsultants.brongo_client.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.fragments.AcceptedBrokersListFragment;
import com.turnipconsultants.brongo_client.responseModels.AcceptedBrokersResponseModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mohit on 22-09-2017.
 */

public class TopBrokersRecycAdapter extends RecyclerView.Adapter<TopBrokersRecycAdapter.TopBrokerViewHolder> {
    private Context context;
    private ArrayList<AcceptedBrokersResponseModel.Data> arrayList;
    private LayoutInflater inflater;
    private RecyclerChildClickListener listener;

    public TopBrokersRecycAdapter(Context context, ArrayList arrayList, AcceptedBrokersListFragment brokersListFragment) {
        this.context = context;
        this.arrayList = arrayList;
        inflater = LayoutInflater.from(context);
        listener = brokersListFragment;
    }

    @Override
    public TopBrokerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.child_top_brokers, parent, false);
        TopBrokerViewHolder viewHolder = new TopBrokerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TopBrokerViewHolder holder, final int position) {
        final AcceptedBrokersResponseModel.Data data = arrayList.get(position);
        Glide.with(context)
                .load(data.getProfileImage())
                .apply(BrongoClientApplication.getRequestOption(false))
                .into(holder.brokerImage);
        holder.brokerName.setText(data.getFirstName() + " " +data.getLastName());
        holder.rating.setText(String.valueOf(data.getRating()));
        holder.ratingBar.setRating(data.getRating());
        holder.userReviews.setText(String.valueOf(data.getReviews().size()));

        //pending...
        holder.closedDeals.setText("0");
        holder.openDeals.setText("0");


        holder.getconnected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChildClick(position, data.getMobileNo());
            }
        });

        holder.mainRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onParentClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class TopBrokerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.brokerImage)
        CircleImageView brokerImage;

        @BindView(R.id.brokerName)
        TextView brokerName;

        @BindView(R.id.rank)
        ImageView rank;

        @BindView(R.id.rating)
        TextView rating;

        @BindView(R.id.ratingBar)
        RatingBar ratingBar;

        @BindView(R.id.opendeals)
        TextView openDeals;

        @BindView(R.id.userdeals)
        TextView userReviews;

        @BindView(R.id.closedDeals)
        TextView closedDeals;

        @BindView(R.id.getconnected)
        TextView getconnected;

        @BindView(R.id.mainRl)
        RelativeLayout mainRl;

        public TopBrokerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface RecyclerChildClickListener {
        void onParentClick(int position);

        void onChildClick(int position, String brokerMobile);
    }
}
