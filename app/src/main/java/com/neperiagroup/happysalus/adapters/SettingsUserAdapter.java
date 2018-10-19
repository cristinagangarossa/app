package com.neperiagroup.happysalus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neperiagroup.happysalus.R;
import com.neperiagroup.happysalus.bean.SettingsUser;

import java.util.List;

public class SettingsUserAdapter extends BaseAdapter {

    private Context context;
    private List<SettingsUser> settingsUsers;

    public SettingsUserAdapter(Context context, List<SettingsUser> settingsUsers){
        this.context = context;
        this.settingsUsers = settingsUsers;
    }

    @Override
    public int getCount() {
        return settingsUsers.size();
    }

    @Override
    public Object getItem(int i) {
        return settingsUsers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.info_user_list, viewGroup, false);

            ImageView imageViewArrowNext = (ImageView) view.findViewById(R.id.imageViewArrowNext);
            TextView textViewLabel = (TextView) view.findViewById(R.id.textViewLabel);
            TextView textViewInfoUser = (TextView) view.findViewById(R.id.textViewInfoUser);

            imageViewArrowNext.setImageResource(R.drawable.icon_arrow_next_dark);
            textViewLabel.setText(settingsUsers.get(i).getLabel());
            textViewInfoUser.setText(settingsUsers.get(i).getInfo());
        }
        return view;
    }
}
