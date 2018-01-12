package com.turnipconsultants.brongo_client.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.models.DealStatusCustom.DealStatusCompleted;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TimeLineUtil {
    public static View getAllGreen(Context context, ArrayList<DealStatusCompleted> arrayList, boolean merge) {


        LinearLayout linearLayout = getNewLinearLayout(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        int total = arrayList.size();
        for (int i = 0; i < total; i++) {
            DealStatusCompleted dealStatusCompleted = arrayList.get(i);
            String formattedDate = dealStatusCompleted.getDate() != null ? dealStatusCompleted.getDate() : "";
            if (formattedDate != null && !formattedDate.isEmpty()) {
                try {
                    Date apiFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(formattedDate);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MM/yyyy hh:mm a");
                    formattedDate = sdf.format(apiFormat);
                    String array[] = formattedDate.split("\\s+");
                    formattedDate = array[0].toUpperCase() + " " + array[1] + " at " + array[2].toLowerCase() + " " + array[3].toLowerCase();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            View mainView = inflater.inflate(R.layout.deal_status_row, linearLayout, false);

            View vertical1 = mainView.findViewById(R.id.vertical_1);
            View vertical2 = mainView.findViewById(R.id.vertical_2);
            View dot = mainView.findViewById(R.id.dot);
            View horizontal = mainView.findViewById(R.id.horizontal_line);
            TextView status = mainView.findViewById(R.id.status_TV);
            TextView dateTV = mainView.findViewById(R.id.date_TV);

            status.setTextColor(context.getResources().getColor(R.color.deal_status_green));
            dateTV.setTextColor(context.getResources().getColor(R.color.dealStatusDate));
            vertical1.setBackgroundColor(context.getResources().getColor(R.color.deal_status_green));
            vertical2.setBackgroundColor(context.getResources().getColor(R.color.deal_status_green));
            horizontal.setBackgroundColor(context.getResources().getColor(R.color.deal_status_green));

            if (i == 0) {
                vertical1.setVisibility(View.INVISIBLE);
                if (i == total - 1) {
                    dot.setVisibility(View.VISIBLE);
                    vertical2.setVisibility(View.INVISIBLE);
                    ReduceWidthHorizontalTo(11, horizontal, context);
                    if (merge) {
                        vertical2.setVisibility(View.VISIBLE);
                        vertical2.setBackgroundColor(context.getResources().getColor(R.color.deal_status_red));
                    }
                } else {
                    dot.setVisibility(View.GONE);
                    /**
                     * To manage left gap margin of vertical lines.
                     */
                    GiveHorizontalLeftMarginTo(6, horizontal, context);
                    vertical2.setVisibility(View.VISIBLE);
                }
            } else if (i == total - 1) {
                dot.setVisibility(View.VISIBLE);
                vertical1.setVisibility(View.VISIBLE);
                vertical2.setVisibility(View.INVISIBLE);
                ReduceWidthHorizontalTo(11, horizontal, context);
                if (merge) {
                    vertical2.setVisibility(View.VISIBLE);
                    vertical2.setBackgroundColor(context.getResources().getColor(R.color.deal_status_red));
                }
            } else {

                dot.setVisibility(View.GONE);
                /**
                 * To manage left gap margin of vertical lines.
                 */
                GiveHorizontalLeftMarginTo(6, horizontal, context);

                vertical1.setVisibility(View.VISIBLE);
                vertical2.setVisibility(View.VISIBLE);
            }


            status.setText(dealStatusCompleted.getStatus());
            dateTV.setText(formattedDate);
            linearLayout.addView(mainView);
        }

        return linearLayout;
    }

    public static View getGreenOne(Context context, ArrayList<Object> arrayList) {
        ArrayList<DealStatusCompleted> dealStatusCompletedArrayList = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            Object obj = arrayList.get(i);
            if (obj instanceof DealStatusCompleted)
                dealStatusCompletedArrayList.add((DealStatusCompleted) arrayList.get(i));
        }
        return getAllGreen(context, dealStatusCompletedArrayList, false);
    }

    public static View getMergedOne(Context context, ArrayList<Object> arrayList) {
        ArrayList<DealStatusCompleted> dealStatusCompletedArrayList = new ArrayList<>();
        ArrayList<String> stringArrayList = new ArrayList<>();

        for (int i = 0; i < arrayList.size(); i++) {
            Object obj = arrayList.get(i);
            if (obj instanceof DealStatusCompleted)
                dealStatusCompletedArrayList.add((DealStatusCompleted) arrayList.get(i));
            if (obj instanceof String)
                stringArrayList.add((String) arrayList.get(i));
        }

        LinearLayout linearLayout = getNewLinearLayout(context);
        linearLayout.addView(getAllGreen(context, dealStatusCompletedArrayList, true));
        linearLayout.addView(getAllRed(context, stringArrayList, true));
        return linearLayout;
    }


    public static View getRedOne(Context context, ArrayList<Object> arrayList) {
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            Object obj = arrayList.get(i);
            if (obj instanceof String)
                stringArrayList.add((String) arrayList.get(i));
        }
        return getAllRed(context, stringArrayList, false);
    }

    public static View getAllRed(Context context, ArrayList<String> arrayList, boolean merge) {

        LinearLayout linearLayout = getNewLinearLayout(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        int total = arrayList.size();
        for (int i = 0; i < total; i++) {
            View mainView = inflater.inflate(R.layout.deal_status_row, linearLayout, false);


            View vertical1 = mainView.findViewById(R.id.vertical_1);
            View vertical2 = mainView.findViewById(R.id.vertical_2);
            View dot = mainView.findViewById(R.id.dot);
            View horizontal = mainView.findViewById(R.id.horizontal_line);
            TextView status = mainView.findViewById(R.id.status_TV);
            TextView date = mainView.findViewById(R.id.date_TV);
            status.setTextColor(context.getResources().getColor(R.color.deal_status_red));
            date.setVisibility(View.GONE);

            vertical1.setBackgroundColor(context.getResources().getColor(R.color.deal_status_red));
            vertical2.setBackgroundColor(context.getResources().getColor(R.color.deal_status_red));
            horizontal.setBackgroundColor(context.getResources().getColor(R.color.deal_status_red));

            dot.setVisibility(View.GONE);
            /**
             * To manage left gap margin of vertical lines.
             */
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) horizontal.getLayoutParams();
            params.leftMargin = (int) AllUtils.DensityUtils.dpToPx(6);
            horizontal.setLayoutParams(params);

            String statusStr = arrayList.get(i);
            if (i == 0) {
                vertical1.setVisibility(View.INVISIBLE);
                if (i == total - 1) {
                    vertical2.setVisibility(View.INVISIBLE);
                } else {
                    vertical2.setVisibility(View.VISIBLE);
                }

                if (merge)
                    vertical1.setVisibility(View.VISIBLE);

            } else if (i == total - 1) {
                vertical1.setVisibility(View.VISIBLE);
                vertical2.setVisibility(View.INVISIBLE);

            } else {
                vertical1.setVisibility(View.VISIBLE);
                vertical2.setVisibility(View.VISIBLE);
            }

            status.setText(statusStr);
            date.setText("");
            linearLayout.addView(mainView);
        }

        return linearLayout;
    }

    private static void GiveHorizontalLeftMarginTo(int i, View horizontal, Context context) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) horizontal.getLayoutParams();
        params.leftMargin = (int) AllUtils.DensityUtils.dpToPx(i);
        horizontal.setLayoutParams(params);
    }

    private static void ReduceWidthHorizontalTo(int i, View horizontal, Context context) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) horizontal.getLayoutParams();
        params.width = (int) AllUtils.DensityUtils.dpToPx(i);
        horizontal.setLayoutParams(params);
    }

    public static LinearLayout getNewLinearLayout(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return layout;
    }
}