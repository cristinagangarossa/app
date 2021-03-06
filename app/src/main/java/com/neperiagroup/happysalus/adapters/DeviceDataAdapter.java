package com.neperiagroup.happysalus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neperiagroup.happysalus.DeviceFoundActivity;
import com.neperiagroup.happysalus.R;
import com.neperiagroup.happysalus.bean.DataDevice;
import com.neperiagroup.happysalus.bean.Device;

import java.util.List;

public class DeviceDataAdapter extends BaseAdapter {

    private Context context;
    private List<DataDevice> dataDevices;

    public DeviceDataAdapter(Context context, List<DataDevice> dataDevices) {
        this.context = context;
        this.dataDevices = dataDevices;
    }

    @Override
    public int getCount() {
        return dataDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return dataDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.device_data_list_item, viewGroup, false);
        }
        ImageView imageViewData = (ImageView) view.findViewById(R.id.imageViewData);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageArrowNextDataDevice);
        TextView textViewInfo = (TextView) view.findViewById(R.id.textViewInfo);
        TextView textViewSubInfo = (TextView) view.findViewById(R.id.textViewSubInfo);
        TextView textViewStatus = (TextView) view.findViewById(R.id.textViewStatus);

        imageView.setImageResource(R.drawable.icon_arrow_next_dark);
        imageViewData.setImageResource(dataDevices.get(i).getImageResourceData());
        textViewInfo.setText(dataDevices.get(i).getInfo());
        textViewSubInfo.setText(dataDevices.get(i).getSubInfo());
        textViewStatus.setText(dataDevices.get(i).getState());
        return view;
    }
}
