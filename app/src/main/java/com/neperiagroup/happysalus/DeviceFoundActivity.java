package com.neperiagroup.happysalus;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.htsmart.wristband.scanner.IDeviceScanner;

import java.util.ArrayList;
import java.util.List;

import cn.imengya.bluetoothle.scanner.ScanDeviceWrapper;
import cn.imengya.bluetoothle.scanner.ScannerListener;

public class DeviceFoundActivity extends AppCompatActivity {

    /**
     * Scanner
     */
    private IDeviceScanner mDeviceScanner = MyApplication.getDeviceScanner();

    /**
     * Views and Datas
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DeviceListAdapter mAdapter;
    private List<ScanDeviceWrapper> mDevices;
    private Button mButtonScan;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_found);

        //stats toolbar setup.
        toolbar = findViewById(R.id.statsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = findViewById(R.id.stats_toolbar_title);
        toolbarTitle.setText("Scan Devices");

        initView();

        //Add ScannerListener in onCreate. And you should Remove ScannerListener int onDestroy.
        mDeviceScanner.addScannerListener(mScannerListener);

        mButtonScan = findViewById(R.id.buttonScan);
        mButtonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean scanning = mDeviceScanner.isScanning();
                if (scanning) {
                    stopScanning();
                } else {
                    mButtonScan.setText("Stop Scanning");
                    startScanning();
                }
            }
        });


    }

    public static Intent getIstanceIntent(Context context){
        Intent intent = new Intent(context, DeviceFoundActivity.class);
        return intent;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.settings:
                startActivity(SettingsActivity.getIstanceIntent(getApplicationContext()));
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * Judge whether the device is  already exists in the list.
     *
     * @param wrapDevice device
     * @return True indicates in the list. False not.
     */
    private boolean existDevice(ScanDeviceWrapper wrapDevice) {
        if (mDevices.size() <= 0) return false;
        for (ScanDeviceWrapper device : mDevices) {
            if (device.getDevice().getAddress().equals(wrapDevice.getDevice().getAddress())) {
                return true;
            }
        }
        return false;
    }

    private ScannerListener mScannerListener = new ScannerListener() {

        /**
         * A device is found
         * @param scanDeviceWrapper The device be found
         */
        @Override
        public void onScan(ScanDeviceWrapper scanDeviceWrapper) {
            if (!existDevice(scanDeviceWrapper)) {
                mDevices.add(scanDeviceWrapper);
                mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * Scan has stopped
         */
        @Override
        public void onStop() {
            stopScanning();
        }
    };

    private void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startScanning();
            }
        });

        ListView listView = (ListView) findViewById(R.id.list_view);

        mDevices = new ArrayList<>(10);
        mAdapter = new DeviceListAdapter();
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BluetoothDevice device = mDevices.get(i).getDevice();
                startActivity(ConnectActivity.getIstanceIntent(getApplicationContext(), device));
                //Intent intent;
                if (BuildConfig.RXJAVA) {
                    //intent = new Intent(MainActivity.this, RxConnectActivity.class);
                } else {
                    //intent = new Intent(MainActivity.this, ConnectActivity.class);
                }
                //intent.putExtra(ConnectActivity.EXTRA_DEVICE, device);
                //startActivity(intent);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Remove ScannerListener
        mDeviceScanner.removeScannerListener(mScannerListener);
    }

    /**
     * Start scan
     */
    private void startScanning() {
        mDevices.clear();
        mAdapter.notifyDataSetChanged();

        mDeviceScanner.start();
        mSwipeRefreshLayout.setRefreshing(true);
        invalidateOptionsMenu();
    }

    /**
     * Stop scan
     */
    private void stopScanning() {
        mDeviceScanner.stop();
        mSwipeRefreshLayout.setRefreshing(false);
        invalidateOptionsMenu();
    }

    private class DeviceListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mDevices.get(i).getDevice();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(DeviceFoundActivity.this).inflate(R.layout.device_list_item, viewGroup, false);
                holder = new ViewHolder();
                holder.address_device = (TextView) view.findViewById(R.id.address_device);
                holder.name_device = (TextView) view.findViewById(R.id.name_device);
                holder.rssi_device = (TextView) view.findViewById(R.id.rssi_device);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            ScanDeviceWrapper device = mDevices.get(i);
            holder.address_device.setText("MAC : " + device.getDevice().getAddress());
            holder.name_device.setText("NAME : " + device.getDevice().getName());
            holder.rssi_device.setText("RSSI : " + String.valueOf(device.getRssi()));
            return view;
        }

        class ViewHolder {
            TextView address_device;
            TextView name_device;
            TextView rssi_device;
        }
    }
}
