package com.neperiagroup.happysalus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neperiagroup.happysalus.R;
import com.neperiagroup.happysalus.bean.Device;

import java.util.ArrayList;
import java.util.List;

public class MyDeviceAdapter extends BaseAdapter {

    private Context context;
    private List<Device> listMyDevice;

    public MyDeviceAdapter(Context context, List<Device> listDevice){
        this.context = context;
        listMyDevice = new ArrayList<Device>();

    }

    @Override
    public int getCount() {
        return listMyDevice.size();
    }

    @Override
    public Object getItem(int i) {
        return listMyDevice.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.settings_list_my_device, viewGroup, false);

            ImageView imageViewDevice = (ImageView) view.findViewById(R.id.imageViewDevice);
            ImageView imageViewArrowNext = (ImageView) view.findViewById(R.id.imageViewArrowNext);
            TextView textViewNameDevice = (TextView) view.findViewById(R.id.textViewNameDevice);
            TextView textViewPowerDevice = (TextView) view.findViewById(R.id.textViewPowerDevice);

            imageViewArrowNext.setImageResource(R.drawable.icon_arrow_next_dark);
            imageViewDevice.setImageResource(listMyDevice.get(i).getImageResourceDevice());
            textViewNameDevice.setText(listMyDevice.get(i).getModelDevice());
            textViewPowerDevice.setText("Batteria: " + listMyDevice.get(i).getBattery() + "%");
        }
        return view;
    }
}
