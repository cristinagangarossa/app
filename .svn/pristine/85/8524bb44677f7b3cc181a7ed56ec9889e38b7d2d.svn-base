package com.neperiagroup.happysalus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neperiagroup.happysalus.R;

import java.util.List;

public class OtherSettingsAdapter extends BaseAdapter {

    private Context context;
    private List<String> listOtherSettings;

    public OtherSettingsAdapter(Context context, List<String> listOtherSettings){
        this.context = context;
        this.listOtherSettings = listOtherSettings;
    }

    @Override
    public int getCount() {
        return listOtherSettings.size();
    }

    @Override
    public Object getItem(int i) {
        return listOtherSettings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.other_settings_list, viewGroup, false);

            ImageView imageViewArrowNext = (ImageView) view.findViewById(R.id.imageViewArrowNext);
            TextView textViewLabelSettings = (TextView) view.findViewById(R.id.textViewLabelSettings);

            imageViewArrowNext.setImageResource(R.drawable.icon_arrow_next_dark);
            textViewLabelSettings.setText(listOtherSettings.get(i));
        }
        return view;
    }
}
