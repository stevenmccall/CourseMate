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

public class PersonCustomListAdapter extends BaseAdapter {

    private final Activity activity;
    private LayoutInflater inflater;
    private final ArrayList<String> list;
    private final PersonCustomListAdapter context = this;

    public PersonCustomListAdapter(Activity activity, ArrayList<String> groupList) {
        this.activity = activity;
        this.list = groupList;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.person_list_row, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.groupName);
        title.setText(list.get(position));

        return convertView;
    }
}
