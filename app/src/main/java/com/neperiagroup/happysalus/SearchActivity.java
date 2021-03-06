package com.neperiagroup.happysalus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.htsmart.wristband.scanner.IDeviceScanner;
import com.neperiagroup.happysalus.bean.Device;
import com.neperiagroup.happysalus.bean.ScanDevices;
import com.skyfishjy.library.RippleBackground;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.imengya.bluetoothle.scanner.ScanDeviceWrapper;
import cn.imengya.bluetoothle.scanner.ScannerListener;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getName();
    private static final String KEY_DEVICE = TAG + ".search_this_device";

    /**
     * Scanner
     */
    private IDeviceScanner mDeviceScanner = MyApplication.getDeviceScanner();

    /**
     * Datas
     */
    private List<ScanDeviceWrapper> mDevices;

    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        device = (Device) getIntent().getSerializableExtra(KEY_DEVICE);
        mDevices = new ArrayList<>(10);

        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.rippleContent);
        rippleBackground.startRippleAnimation();

        //Add ScannerListener in onCreate. And you should Remove ScannerListener int onDestroy.
        mDeviceScanner.addScannerListener(mScannerListener);

        startScanning();

        new Thread(new Run()).start();

    }

    public static Intent getInstanceIntent(Context context, Device device){
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(KEY_DEVICE, (Serializable) device);
        return intent;
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
        mDeviceScanner.start();
        invalidateOptionsMenu();
    }

    /**
     * Stop scan
     */
    private void stopScanning() {
        mDeviceScanner.stop();
        invalidateOptionsMenu();
    }

    public class Run implements Runnable{
        @Override
        public void run() {
            while (true){
                try {
                    Thread.sleep(3000L); //Durata dello scan
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean scanning = mDeviceScanner.isScanning();
                stopScanning();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        boolean scanning = mDeviceScanner.isScanning();
                            stopScanning();
                            boolean deviceFound = false;

                            List<ScanDevices> listDevices = new ArrayList<>();
                            for(ScanDeviceWrapper scanDeviceWrapper : mDevices){
                                if(scanDeviceWrapper.getDevice().getName()!=null) {
                                    if (scanDeviceWrapper.getDevice().getName().startsWith(device.getNameBtnDevice())) {
                                        startActivity(ConnectActivity.getIstanceIntent(getApplicationContext(), scanDeviceWrapper.getDevice()));
                                        finish();
                                        deviceFound = true;
                                    }
                                }
                            }
                            if(deviceFound==false){
                                startActivity(DashboardActivity.getInstanceIntent(getApplicationContext()));
                                Toast.makeText(getApplicationContext(), "Dispositivo non trovato.", Toast.LENGTH_SHORT).show();
                            }
                            return;//Stop thread
                    }
                });
                return;//Stop Run
            }
        }
    }
}
