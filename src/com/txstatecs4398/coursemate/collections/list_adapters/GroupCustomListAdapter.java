package com.txstatecs4398.coursemate.collections.list_adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.txstatecs4398.coursemate.R;
import java.util.ArrayList;

public class GroupCustomListAdapter extends BaseAdapter {

    private final Activity activity;
    private LayoutInflater inflater;
    private final ArrayList<String> list;
    private final ArrayList<String> listDate;
    private final ArrayList<String> listPeople;

    public GroupCustomListAdapter(Activity activity, ArrayList<String> groupList, ArrayList<String> groupDate, ArrayList<String> groupNames) {
        this.activity = activity;
        this.list = groupList;
        this.listDate = groupDate;
        this.listPeople = groupNames;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public String getItem(int i) {
        // TODO Auto-generated method stub
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        // TODO Auto-generated method stub
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group_list_row, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.groupName);
        TextView groupmates = (TextView) convertView.findViewById(R.id.groupMembers);
        TextView date = (TextView) convertView.findViewById(R.id.semester);

        title.setText(list.get(position));
        groupmates.setText(listPeople.get(position));
        date.setText(listDate.get(position));

        return convertView;
    }
}
