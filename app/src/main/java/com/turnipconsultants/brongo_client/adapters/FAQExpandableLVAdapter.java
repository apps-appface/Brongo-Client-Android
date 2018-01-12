package com.turnipconsultants.brongo_client.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.turnipconsultants.brongo_client.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Pankaj on 30-11-2017.
 */

public class FAQExpandableLVAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    private HashMap<String, List<String>> _listDataChild;

    public FAQExpandableLVAdapter(Context context, List<String> listDataHeader,
                                  HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.faq_group_row, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.group_text);
        lblListHeader.setText(headerTitle);

        ImageView indicator = (ImageView) convertView.findViewById(R.id.indicator);
        RelativeLayout groupContainer = (RelativeLayout) convertView.findViewById(R.id.container_group);

        if (isExpanded) {
            groupContainer.setBackgroundColor(_context.getResources().getColor(R.color.simple_back_color));
            indicator.setImageResource(R.drawable.accordian_open);
        } else {
            groupContainer.setBackgroundColor(Color.WHITE);
            indicator.setImageResource(R.drawable.accordion_close);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.faq_child_row, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.child_text);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
