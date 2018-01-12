package com.turnipconsultants.brongo_client.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.turnipconsultants.brongo_client.BrongoClientApplication;
import com.turnipconsultants.brongo_client.Listener.OnLoadMoreListener;
import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.responseModels.NotificationResponseModel;
import com.turnipconsultants.brongo_client.others.Constants.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Pankaj on 27-11-2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "NotificationAdapter";
    private Context context;
    private List<NotificationResponseModel.DataEntity> notiList;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem,
            totalItemCount;
    private NotificationItemClickListener itemClickListener;
    private int readColor, unReadColor;

    private final Runnable loadMoreRunnable = new Runnable() {
        @Override
        public void run() {
            mOnLoadMoreListener.onLoadMore();
        }
    };

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return notiList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.message)
        public TextView message;

        @BindView(R.id.time)
        public TextView time;

        @BindView(R.id.icon)
        public ImageView icon;

        @BindView(R.id.profileImage)
        public CircleImageView profileImg;

        @BindView(R.id.notification_container)
        public LinearLayout notiItem;

        @BindView(R.id.divider)
        public View divider;

        public NotificationViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class LoaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.loader)
        ProgressBar loader;

        public LoaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NotificationAdapter(Context context, ArrayList<NotificationResponseModel.DataEntity> notiList, final RecyclerView notificationRV, NotificationItemClickListener itemClickListener) {
        this.context = context;
        this.notiList = notiList;
        this.itemClickListener = itemClickListener;
        this.readColor = context.getResources().getColor(R.color.noti_read);
        this.unReadColor = context.getResources().getColor(R.color.noti_unread);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) notificationRV.getLayoutManager();
        notificationRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                Log.i(TAG, "onScrolled: " + totalItemCount + "\t" + lastVisibleItem);
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        Log.i(TAG, "onScrolled: CALLED");
                        notificationRV.post(loadMoreRunnable);
                    }
                    isLoading = true;
                }
            }
        });


    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row, parent, false);
            return new NotificationViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.notification_loading, parent, false);
            return new LoaderViewHolder(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {

        try {
            if (viewholder instanceof NotificationViewHolder) {
                NotificationViewHolder holder = (NotificationViewHolder) viewholder;
                final NotificationResponseModel.DataEntity notification = notiList.get(position);

                switch (notification.getAlertType()) {
                    case AppConstants.NOTIFICATION_TYPE.BUILDER:
                        break;
                    case AppConstants.NOTIFICATION_TYPE.APP:
                        break;
                    case AppConstants.NOTIFICATION_TYPE.OFFERS:
                        break;
                    case AppConstants.NOTIFICATION_TYPE.GENERAL:
                        break;
                    case AppConstants.NOTIFICATION_TYPE.STOP_TIMER:
                        break;
                    case AppConstants.NOTIFICATION_TYPE.CLIENT_ACCEPT:
                        holder.icon.setBackgroundResource(R.drawable.notification_chat_icon);
                        break;
                    case AppConstants.NOTIFICATION_TYPE.BROKER_ACCEPT:
                        break;
                    case AppConstants.NOTIFICATION_TYPE.LEADS_UPDATE:
                        holder.icon.setBackgroundResource(R.drawable.notification_meeting_schedule_icon);
                        break;
                    case AppConstants.NOTIFICATION_TYPE.PAY_U_PAYMENT:
                        break;
                    case AppConstants.NOTIFICATION_TYPE.CALL_BACK:
                        break;
                    default:
                        holder.icon.setBackgroundResource(R.drawable.notification_meeting_schedule_icon);
                }


                String message = notification.getBrokerName() + ", " + notification.getMessage();

                SpannableStringBuilder ssBuilder = new SpannableStringBuilder(message);

                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                StyleSpan normalSpan = new StyleSpan(Typeface.NORMAL);

                ssBuilder.setSpan(
                        boldSpan,
                        0,
                        message.indexOf(", " + notification.getMessage()),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                ssBuilder.setSpan(
                        normalSpan,
                        message.indexOf(", " + notification.getMessage()),
                        message.length() - 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                holder.message.setText(ssBuilder);
                holder.time.setText(notification.getDays());

                Glide.with(context)
                        .load(notification.getBrokerProfile())
                        .apply(BrongoClientApplication.getRequestOption(true))
                        .into(holder.profileImg);

                holder.notiItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.OnItemClick(position, notification);
                    }
                });

                if (notification.isRead()) {
                    holder.notiItem.setBackgroundColor(readColor);
                    holder.divider.setVisibility(View.INVISIBLE);
                } else {
                    holder.notiItem.setBackgroundColor(unReadColor);
                    holder.divider.setVisibility(View.VISIBLE);
                }
            } else if (viewholder instanceof LoaderViewHolder) {
                LoaderViewHolder loadingViewHolder = (LoaderViewHolder) viewholder;
                loadingViewHolder.loader.setIndeterminate(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return notiList == null ? 0 : notiList.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public interface NotificationItemClickListener {
        void OnItemClick(int position, NotificationResponseModel.DataEntity notification);
    }
}