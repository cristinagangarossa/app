package com.neperiagroup.happysalus;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.htsmart.wristband.WristbandApplication;
import com.htsmart.wristband.bean.WristbandConfig;
import com.htsmart.wristband.bean.WristbandVersion;
import com.htsmart.wristband.performer.IDevicePerformer;
import com.htsmart.wristband.performer.SimplePerformerListener;
import com.neperiagroup.happysalus.adapters.WeekDataAdapter;
import com.neperiagroup.happysalus.bean.BLECommunicationObject;
import com.neperiagroup.happysalus.bean.BloodPressureData;
import com.neperiagroup.happysalus.bean.DataDevice;
import com.neperiagroup.happysalus.bean.ScaleData;
import com.neperiagroup.happysalus.bean.User;
import com.neperiagroup.happysalus.bean.WeekData;
import com.neperiagroup.happysalus.services.FileMamoryService;
import com.neperiagroup.happysalus.utility.StoreInMemory;
import com.neperiagroup.happysalus.utility.TagUser;
import com.neperiagroup.happysalus.utility.Utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SphygmomanometerActivity extends AppCompatActivity {

    private static final String TAG = PressureStatsActivity.class.getName();

    static UUID BLOOD_PRESSURE_SERVICE_UUID = Utils.convertFromInteger(0x7889);
    static UUID APP_TO_BLOOD_PRESSURE_TRANSFER_CHAR_WRITE_UUID = Utils.convertFromInteger(0x8A81);
    static UUID BLOOD_PRESSURE_TO_APP_TRANSFER_CHAR_INDICATE_UUID = Utils.convertFromInteger(0x8A82);
    static UUID BLOOD_PRESSURE_MEASUREMENT_CHAR_INDICATE_UUID = Utils.convertFromInteger(0x8A91);
    static UUID BLOOD_PRESSURE_MEASUREMENT_CHAR_NOTIFY_UUID = Utils.convertFromInteger(0x8A92);

    static UUID CLIENT_CHARACTERISTIC_CONFIG_UUID = Utils.convertFromInteger(0x2902);

    final static String BLOOD_PRESSURE_MODEL = "805A0";

    String deviceToDiscoverName = null;

    private Queue<BLECommunicationObject> communicationActionQueue = new LinkedList<BLECommunicationObject>();

    private final static int REQUEST_ENABLE_BT = 1;

    public static boolean characteristicsSent = false;
    public static boolean mustStorePassword = false;
    public static boolean processTerminated = false;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 456;

    BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice myDevice;
    private BluetoothGatt gatt;
    private BluetoothGattCallback gattCallback = null;
    private String passwordBloodPressure = null;
    private String accountId = "4B09DF78";

    private StoreInMemory storeInMemory;
    private TagUser tagUser;
    private User user;

    private static final String DEVICE_DATA_KEY = TAG + ".device_data";
    private static final String DEVICE_NAME_KEY = TAG + ".device_name";

    private Toolbar toolbar;
    private TextView toolbarTitle;

    private String deviceName;
    private DataDevice deviceData;

    private TextView minHg;
    private TextView maxHg;

    private ListView weeksDataList;
    private IDevicePerformer mDevicePerformer = WristbandApplication.getDevicePerformer();

    List<PieEntry> entriesMax = new ArrayList<>();
    List<PieEntry> entriesMin = new ArrayList<>();

    PieChart chartMax;
    PieChart chartMin;

    int min;
    int max;
    Button readButton;
    private boolean canStartScan;
    private List<BloodPressureData> bloodPressureDataToday;
    private final String FILE_BLOOD_PRESSURE_DATA="pressureDataSphygmo";
    private FileMamoryService fileMamoryService;
    public static final String currentMonth="current_month";


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_press_stats);
        fileMamoryService=new FileMamoryService(getApplicationContext());

        //deserialize device object.
        deviceData = (DataDevice) getIntent().getSerializableExtra(DEVICE_DATA_KEY);
        deviceName = getIntent().getStringExtra(DEVICE_NAME_KEY);

        //stats toolbar setup.
        toolbar = findViewById(R.id.statsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = findViewById(R.id.stats_toolbar_title);
        toolbarTitle.setText(deviceName);

        //maxHg = findViewById(R.id.minValue);
        //minHg = findViewById(R.id.maxValue);

        //mDevicePerformer.addPerformerListener(mPerformerListener);
        //mDevicePerformer.cmd_requestWristbandConfig();
        //mDevicePerformer.openHealthyRealTimeData(IDevicePerformer.HEALTHY_TYPE_BLOOD_PRESSURE);

        weeksDataList=findViewById(R.id.listWeekData);

        WeekDataAdapter weekDataAdapter=new WeekDataAdapter(getApplicationContext(),getWeeks());
        weeksDataList.setAdapter(weekDataAdapter);

        chartMax = findViewById(R.id.systolicChart);
        chartMin = findViewById(R.id.dyastolic_chart);

        //todo get from storage
        readTodayData();
        if (bloodPressureDataToday != null && bloodPressureDataToday.size() > 0) {
            min=bloodPressureDataToday.get(bloodPressureDataToday.size()-1).getDiastolic();
            max=bloodPressureDataToday.get(bloodPressureDataToday.size()-1).getSystolic();
        }else{
            min=0;
            max=0;
        }

        paintPieChartMax(max);
        paintPieChartMin(min);

        storeInMemory = new StoreInMemory(getApplicationContext());
        tagUser = new TagUser();
        user = new User();
        user = storeInMemory.getUser(1);

        canStartScan = true;

        characteristicsSent = false;
        mustStorePassword = false;
        processTerminated = false;

        int viewSelected = getIntent().getIntExtra("viewSelected", 1);

        if(viewSelected == 0)
        {
            //SharedPreferences sharedPref = SphygmomanometerActivity.this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences sharedPref = getSharedPreferences("prefs", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove("password_blood_pressure");
            editor.apply();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                canStartScan = false;
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                Log.d(TAG, "onCreate() - ACCESS_COARSE_LOCATION permission requested");
            }
        }

        readButton=(Button) findViewById(R.id.read_button);
        readButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canStartScan)
                {
                    writeActionProgress("Bluetooth Activity started");

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(getApplicationContext(), "pronto per effettuare la misurazione della pressione", Toast.LENGTH_SHORT).show();

                        }
                    });


                    startDiscoveryProcess();
                }
            }
        });

    }

    public static Intent getInstanceIntent(Context context, int viewSelected){
        Intent intent = new Intent(context, SphygmomanometerActivity.class);
        intent.putExtra("viewSelected", viewSelected);
        return intent;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()){
//            case R.id.settings:
//                startActivity(SettingsActivity.getInstanceIntent(getApplicationContext()));
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    private SimplePerformerListener mPerformerListener = new SimplePerformerListener() {
        @Override
        public void onResponseWristbandConfig(WristbandConfig config) {
            WristbandVersion version = config.getWristbandVersion();
            if (version.isBloodPressureEnable()) {
                //minHg.setVisibility(View.VISIBLE);
                //maxHg.setVisibility(View.VISIBLE);
                //mOxyValue.setText(getString(R.string.oxygen_value, 0));
            }

        }

        @Override
        public void onOpenHealthyRealTimeData(int healthyType, boolean success) {
            if (success) {
                switch (healthyType) {
                    case IDevicePerformer.HEALTHY_TYPE_HEART_RATE:

                        break;
                }
            }
        }

        @Override
        public void onCloseHealthyRealTimeData(int healthyType) {
            switch (healthyType) {
                case IDevicePerformer.HEALTHY_TYPE_HEART_RATE:

                    break;

                case IDevicePerformer.HEALTHY_TYPE_OXYGEN:

                    break;

                case IDevicePerformer.HEALTHY_TYPE_BLOOD_PRESSURE:

                    break;

                case IDevicePerformer.HEALTHY_TYPE_RESPIRATORY_RATE:

                    break;
            }
        }

        @Override
        public void onResultHealthyRealTimeData(int heartRate, int oxygen, int diastolicPressure, int systolicPressure, int respiratoryRate) {
            if (diastolicPressure != 0) {
                //minHg.setText(String.valueOf(diastolicPressure));
                entriesMin.get(0).setY(diastolicPressure*0.8f);
                entriesMin.get(1).setY(100-(diastolicPressure*0.8f));
                chartMin.setCenterText(""+diastolicPressure);
                chartMin.notifyDataSetChanged();
                chartMin.invalidate();

            }
            if (systolicPressure != 0) {
                //maxHg.setText(String.valueOf(systolicPressure));
                entriesMax.get(0).setY(systolicPressure*0.5f);
                entriesMax.get(1).setY(100-(systolicPressure*0.5f));
                chartMax.setCenterText(""+systolicPressure);
                chartMax.notifyDataSetChanged();
                chartMax.invalidate();
            }

        }


    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<WeekData> getWeeks() {

        List<WeekData> weeks=new ArrayList<WeekData>();
        FileMamoryService fileMamoryService=new FileMamoryService(getApplicationContext());
        try {
            weeks=fileMamoryService.getAllWeekPressDataSphygmo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weeks;
    }

    public void paintPieChartMax(int val )
    {
        float proportion=0.5f;

        int[] colors = new int[] {Color.argb(255,255,255,255), Color.TRANSPARENT};
        entriesMax.add(new PieEntry(val*proportion));
        //Log.d("percentuale ","val "+val);
        float other = 100-(val*proportion);
        //Log.d("percentuale ","other "+other);
        entriesMax.add(new PieEntry(other));
        PieDataSet set = new PieDataSet(entriesMax,null);
        PieData data = new PieData(set);
        set.setColors(colors);
        data.setValueTextColor(Color.TRANSPARENT);
        //set.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //set.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //PieChartRenderer chartR = new PieChartRenderer(chart,chart.getAnimator(),chart.getViewPortHandler());
        //chart.setRenderer(chartR);
        //chart.invalidate();
        //temp
        int value=(int)data.getDataSet().getEntryForIndex(0).getValue();
        //Log.d("percentuale ","value "+value);
        chartMax.setTransparentCircleRadius(94);
        chartMax.setTransparentCircleColor(getResources().getColor(R.color.light));
        chartMax.getDescription().setEnabled(false);
        chartMax.setRotationEnabled(false);
        chartMax.setCenterText(val+"");
        chartMax.setCenterTextSize(20);
        chartMax.setCenterTextColor(Color.WHITE);
        chartMax.getLegend().setEnabled(false);
        chartMax.getDescription().setEnabled(false);
        chartMax.setEntryLabelColor(Color.TRANSPARENT);
        chartMax.setDrawEntryLabels(false);
        chartMax.setDrawHoleEnabled(true);
        chartMax.setHoleRadius(90.0f);
        chartMax.setHoleColor(Color.TRANSPARENT);
        chartMax.setData(data);
        //chart.spin( 500,0,360 - angleTo, Easing.EasingOption.EaseInOutQuad ); // rotazione del grafico
        chartMax.animateY(1000);
        chartMax.notifyDataSetChanged(); // let the chart know it's data changed
        //PieChartRenderer custom= new PieChartRenderer(chart, chart.getAnimator(),chart.getViewPortHandler());
        //chart.setRenderer(custom);
        chartMax.invalidate(); // refresh

    }

    public void paintPieChartMin(int val )
    {
        float proportion=0.8f;

        int[] colors = new int[] {Color.argb(255,255,255,255), Color.TRANSPARENT};
        entriesMin.add(new PieEntry(val*proportion));
        //Log.d("percentuale ","val "+val);
        float other = 100-(val*proportion);
        //Log.d("percentuale ","other "+other);
        entriesMin.add(new PieEntry(other));
        PieDataSet set = new PieDataSet(entriesMin,null);
        PieData data = new PieData(set);
        set.setColors(colors);
        data.setValueTextColor(Color.TRANSPARENT);
        //set.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //set.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //PieChartRenderer chartR = new PieChartRenderer(chart,chart.getAnimator(),chart.getViewPortHandler());
        //chart.setRenderer(chartR);
        //chart.invalidate();
        //temp
        int value=(int)data.getDataSet().getEntryForIndex(0).getValue();
        //Log.d("percentuale ","value "+value);
        chartMin.setTransparentCircleRadius(94);
        chartMin.setTransparentCircleColor(getResources().getColor(R.color.light));
        chartMin.getDescription().setEnabled(false);
        chartMin.setRotationEnabled(false);
        chartMin.setCenterText(val+"");
        chartMin.setCenterTextSize(20);
        chartMin.setCenterTextColor(Color.WHITE);
        chartMin.getLegend().setEnabled(false);
        chartMin.getDescription().setEnabled(false);
        chartMin.setEntryLabelColor(Color.TRANSPARENT);
        chartMin.setDrawEntryLabels(false);
        chartMin.setDrawHoleEnabled(true);
        chartMin.setHoleRadius(90.0f);
        chartMin.setHoleColor(Color.TRANSPARENT);
        chartMin.setData(data);
        //chart.spin( 500,0,360 - angleTo, Easing.EasingOption.EaseInOutQuad ); // rotazione del grafico
        chartMin.animateY(1000);
        chartMin.notifyDataSetChanged(); // let the chart know it's data changed
        //PieChartRenderer custom= new PieChartRenderer(chart, chart.getAnimator(),chart.getViewPortHandler());
        //chart.setRenderer(custom);
        chartMin.invalidate(); // refresh

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onDestroy()
    {

        if (gatt != null)
        {
            gatt.disconnect();
            gatt.close();
            gatt = null;
            myDevice = null;
        }
        super.onDestroy();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case PERMISSION_REQUEST_COARSE_LOCATION:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    this.startDiscoveryProcess();
                }
                else
                {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void startDiscoveryProcess()
    {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        //check if this app is already connected to the device
        if (!this.checkParing(BLOOD_PRESSURE_MODEL))
        {
            //if the device is not connected to the app:
            // set the device name to connect to
            // set the status of the protocol to start from
            storeInMemory.setNameBtcDeviceUserBindToStore(user.getId(), BLOOD_PRESSURE_MODEL);
            deviceToDiscoverName = "1" + BLOOD_PRESSURE_MODEL;
        }
        else
        {
            deviceToDiscoverName = "0" + BLOOD_PRESSURE_MODEL + accountId;
        }

        final BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback()
        {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord)
            {
                if (myDevice == null)
                {
                    if (device.getName() != null)
                    {
                        if (device.getName().startsWith(deviceToDiscoverName))
                        {
                            Log.d(TAG, "scanCallback() - discovered device name: " + device.getName());
                            myDevice = device;

                            if (myDevice != null)
                            {
                                Log.d(TAG, "onLeScan() - connect");
                                gatt = myDevice.connectGatt(SphygmomanometerActivity.this, true, gattCallback);
                                bluetoothAdapter.stopLeScan(this);

                                //TODO: read device info

                            }
                        }
                    }
                }
                else
                {
                    Log.d(TAG, "onLeScan() - already discovered");
                }

                if (gatt != null)
                {
                    gatt.connect();
                }
                else if (myDevice != null)
                {
                    gatt = myDevice.connectGatt(SphygmomanometerActivity.this, true, gattCallback);
                }
                else
                {

                }
            }
        };

        bluetoothAdapter.startLeScan(scanCallback);

        gattCallback = new BluetoothGattCallback()
        {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
            {
                if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED)
                {
                    /*
                     * Once successfully connected, we must next discover all the
                     * services on the device before we can read and write their
                     * characteristics.
                     */
                    gatt.discoverServices();
                }
                else if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_DISCONNECTED)
                {
                    /*
                     * If at any point we disconnect, send a message to clear the
                     * weather values out of the UI
                     */

                }
                else if (status == 19)
                {
                    /*
                     * If at any point we disconnect, send a message to clear the
                     * weather values out of the UI
                     */
                    gatt.connect();

                }
                else if (status != BluetoothGatt.GATT_SUCCESS)
                {
                    /*
                     * If there is a failure at any stage, simply disconnect
                     */
                    gatt.disconnect();
                }

                Log.d(TAG, "onConnectionStateChange() - status " + status + " --> newState " + newState);
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status)
            {
                if (!characteristicsSent)
                {
                    if (!checkParing(BLOOD_PRESSURE_MODEL))
                    {
                        characteristicsSent = true;
                    }
                    BLECommunicationObject object = null;

                    //enable indicate on BLOOD_PRESSURE_TO_APP_TRANSFER_CHAR_INDICATE_UUID
                    Log.d(TAG, "onServicesDiscovered() - queue enable blood pressure to app indication " + BLOOD_PRESSURE_TO_APP_TRANSFER_CHAR_INDICATE_UUID);
                    object = new BLECommunicationObject();
                    object.setType(BLECommunicationObject.TYPE_DESCRIPTOR);
                    object.setCharacteristic(BLOOD_PRESSURE_TO_APP_TRANSFER_CHAR_INDICATE_UUID);
                    object.setDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID);
                    object.setAction(BLECommunicationObject.ACTION_WRITE);
                    object.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                    object.setDescription("enable blood pressure to app indication");
                    object.setMustEnableNotify(true);
                    communicationActionQueue.add(object);

                    //enable indication on BLOOD_PRESSURE_MEASUREMENT_CHAR_INDICATE_UUID
                    Log.d(TAG, "onServicesDiscovered() - queue enable blood pressure measurement indication " + BLOOD_PRESSURE_MEASUREMENT_CHAR_INDICATE_UUID);
                    object = new BLECommunicationObject();
                    object.setType(BLECommunicationObject.TYPE_DESCRIPTOR);
                    object.setCharacteristic(BLOOD_PRESSURE_MEASUREMENT_CHAR_INDICATE_UUID);
                    object.setDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID);
                    object.setAction(BLECommunicationObject.ACTION_WRITE);
                    object.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                    object.setDescription("enable blood pressure measurement indication");
                    object.setMustEnableNotify(true);
                    communicationActionQueue.add(object);

                    //enable notify on BLOOD_PRESSURE_MEASUREMENT_CHAR_NOTIFY_UUID
                    Log.d(TAG, "onServicesDiscovered() - queue enable blood pressure notify " + BLOOD_PRESSURE_MEASUREMENT_CHAR_NOTIFY_UUID);
                    object = new BLECommunicationObject();
                    object.setType(BLECommunicationObject.TYPE_DESCRIPTOR);
                    object.setCharacteristic(BLOOD_PRESSURE_MEASUREMENT_CHAR_NOTIFY_UUID);
                    object.setDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID);
                    object.setAction(BLECommunicationObject.ACTION_WRITE);
                    object.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    object.setDescription("enable blood pressure measurement notify");
                    object.setMustEnableNotify(true);
                    communicationActionQueue.add(object);

                    //send first from queue
                    doNextAction();
                }
                else
                {
                }
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status)
            {
                Log.d(TAG, "onDescriptorRead() - " + descriptor.getUuid() + " on characteristic " + descriptor.getCharacteristic().getUuid() + " status " + status);

                doNextAction();
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status)
            {
                Log.d(TAG, "onDescriptorWrite() - " + descriptor.getUuid() + " on characteristic " + descriptor.getCharacteristic().getUuid() + " status " + status);

                doNextAction();
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
            {
                Log.d(TAG, "onCharacteristicChanged() - " + characteristic.getUuid());

                processData(characteristic);

                //doNextAction();
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
            {
                Log.d(TAG, "onCharacteristicWrite() - " + characteristic.getUuid() + " status " + status);

                doNextAction();
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
            {

                Log.d(TAG, "onCharacteristicRead() - " + characteristic.getUuid() + " status " + status);

                doNextAction();
            }
        };

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void doNextAction()
    {
        BluetoothGattService service = null;
        BluetoothGattCharacteristic characteristic = null;
        BluetoothGattDescriptor descriptor = null;
        boolean processed = false;

        //if there is something to write, do it!
        do
        {
            if (mustStorePassword)
            {
                this.writeActionProgress("Paring process terminated");
                Log.d(TAG, "doNextAction() - Paring process terminated, must store passwordBloodPressure");

                mustStorePassword = false;
                this.savePassword(passwordBloodPressure);
                processTerminated = true;
            }

            if (communicationActionQueue.size() > 0)
            {
                BLECommunicationObject object = communicationActionQueue.element();
                if (object != null)
                {
                    service = gatt.getService(BLOOD_PRESSURE_SERVICE_UUID);
                    if (service != null)
                    {
                        characteristic = service.getCharacteristic(object.getCharacteristic());

                        if (characteristic != null)
                        {
                            descriptor = characteristic.getDescriptor(object.getDescriptor());

                            if (descriptor != null || (object.getType() == BLECommunicationObject.TYPE_CHARACTERISTIC))
                            {

                                if (object.isMustEnableNotify())
                                {
                                    gatt.setCharacteristicNotification(characteristic, true);
                                }

                                if (object.getAction() == BLECommunicationObject.ACTION_WRITE)
                                {
                                    if (object.getType() == BLECommunicationObject.TYPE_DESCRIPTOR)
                                    {
                                        descriptor.setValue(object.getValue());
                                        boolean status = gatt.writeDescriptor(descriptor);
                                        Log.d(TAG, "doNextAction() - " + object.getDescription() + " " + descriptor.getUuid() + " - on characteristic " + characteristic.getUuid() + " writeDescriptor status " + status);
                                        this.writeActionProgress(object.getDescription() + " - " + status);
                                    }
                                    else
                                    {
                                        if ((characteristic != null) && (characteristic.getUuid().equals(APP_TO_BLOOD_PRESSURE_TRANSFER_CHAR_WRITE_UUID)) && (object.getValue() != null) && (object.getValue()[0] == (byte)0x22))
                                        {
                                            if (!checkParing(BLOOD_PRESSURE_MODEL))
                                            {
                                                mustStorePassword = true;
                                            }
                                        }

                                        characteristic.setValue(object.getValue());
                                        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                                        if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_WRITE) <= 0)
                                        {
                                            Log.d(TAG, "doNextAction() - does not have permission to send");
                                            this.writeActionProgress(object.getDescription() + " - error: missing prermissions to send data to the device");
                                        }
                                        else
                                        {
                                            boolean status = gatt.writeCharacteristic(characteristic);
                                            Log.d(TAG, "doNextAction() - " + object.getDescription() + " - writeCharacteristic " + characteristic.getUuid() + " status " + status);
                                            this.writeActionProgress(object.getDescription() + " - " + status);
                                        }
                                    }
                                }
                                else
                                {
                                    if (object.getType() == BLECommunicationObject.TYPE_DESCRIPTOR)
                                    {
                                        boolean status = gatt.readDescriptor(descriptor);
                                        Log.d(TAG, "doNextAction() - " + object.getDescription() + " " + descriptor.getUuid() + " - on characteristic " + characteristic.getUuid() + " readDescriptor status " + status);
                                        this.writeActionProgress(object.getDescription() + " - " + status);
                                    }
                                    else
                                    {
                                        boolean status = gatt.readCharacteristic(characteristic);
                                        Log.d(TAG, "doNextAction() - " + object.getDescription() + " - readCharacteristic " + characteristic.getUuid() + " status " + status);
                                        this.writeActionProgress(object.getDescription() + " - " + status);
                                    }
                                }
                                processed = true;
                            }
                            else
                            {
                                Log.d(TAG, "doNextAction() - descriptor " + object.getDescriptor() + " for characteristic " + object.getCharacteristic() + " is null");
                                this.writeActionProgress(object.getDescription() + " - error: descriptor " + object.getDescriptor() + " for characteristic " + object.getCharacteristic() + " is null");
                            }
                        }
                        else
                        {
                            Log.d(TAG, "doNextAction() - characteristic " + object.getCharacteristic() + " is null");
                            this.writeActionProgress(object.getDescription() + " - error: characteristic " + object.getCharacteristic() + " is null");
                        }
                    }
                    else
                    {
                        Log.d(TAG, "doNextAction() - service is null");
                        this.writeActionProgress(object.getDescription() + " - error: service is null");
                    }
                }
                else
                {
                    Log.d(TAG, "doNextAction() - object is null");
                    this.writeActionProgress("error: action object is null");
                }
                communicationActionQueue.remove();  //pop the item that we just finishing writing
            }
            else
            {
                Log.d(TAG, "doNextAction() - no more messages to process");

                processed = true;

                if(processTerminated)
                {
                    this.writeActionProgress("process completed");

                }
            }
        }
        while (!processed);
    }

    private void writeActionProgress(final String text)
    {
        /*
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

            }
        });
        */
    }

    private boolean checkParing(String deviceParingId)
    {
        boolean result = false;

        //check if an account was already connected to a specific device
        //SharedPreferences sharedPref = SphygmomanometerActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPref = getSharedPreferences("prefs", 0);

        String password = sharedPref.getString("password_blood_pressure", null);

        if (password != null)
        {
            SphygmomanometerActivity.this.passwordBloodPressure = password;
            result = true;
        }

        return result;
    }

    private void savePassword(String password)
    {
        //SharedPreferences sharedPref = SphygmomanometerActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPref = getSharedPreferences("prefs", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("password_blood_pressure", password);
        editor.commit();
    }

    public void finishButton(View v)
    {
        //we close this activiry and re return to the caller one
        setResult(Activity.RESULT_OK);

        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void processData(BluetoothGattCharacteristic characteristic)
    {
        //TRYING TO GET DATA FROM DEVICE LIKE PWD DURING PARING
        UUID thisUUID = characteristic.getUuid();

        Log.d(TAG, "processData() - characteristic uuid " + characteristic.getUuid());

        if (thisUUID.equals(BLOOD_PRESSURE_TO_APP_TRANSFER_CHAR_INDICATE_UUID))
        {
            String parsedValue = Utils.parse(characteristic);

            if (parsedValue != null)
            {
                String[] commandBytes = parsedValue.split("-");

                if (commandBytes != null)
                {
                    if (commandBytes.length >= 5)
                    {
                        Log.d(TAG, "processData() - message command --> " + commandBytes[0]);
                        if (commandBytes[0].compareTo("A0") == 0)
                        {
                            //command Password on Paring
                            passwordBloodPressure = commandBytes[4] + commandBytes[3] + commandBytes[2] + commandBytes[1];

                            if (passwordBloodPressure != null)
                            {
                                Log.d(TAG, "processData() - received passwordBloodPressure --> " + passwordBloodPressure);
                                writeActionProgress("received password " + passwordBloodPressure);
                                Log.d(TAG, "processData() - queue account id");
                                sendAccountId(APP_TO_BLOOD_PRESSURE_TRANSFER_CHAR_WRITE_UUID);
                            }

                            doNextAction();
                        }
                        else if (commandBytes[0].compareTo("A1") == 0)
                        {
                            //command Random Number on Paring
                            String randomNumber = commandBytes[4] + commandBytes[3] + commandBytes[2] + commandBytes[1];

                            Log.d(TAG, "processData() - received random number --> " + randomNumber);
                            writeActionProgress("received random number " + randomNumber);

                            if (randomNumber != null)
                            {
                                //send verification code
                                Log.d(TAG, "processData() - queue verification code");
                                sendVerificationCode(APP_TO_BLOOD_PRESSURE_TRANSFER_CHAR_WRITE_UUID, randomNumber);
                                Log.d(TAG, "processData() - queue time offset");
                                sendTimeOffset(APP_TO_BLOOD_PRESSURE_TRANSFER_CHAR_WRITE_UUID);

                                if (!this.checkParing(BLOOD_PRESSURE_MODEL))
                                {
                                    Log.d(TAG, "processData() - queue enable disconnection");
                                    sendEnableDisconnection(APP_TO_BLOOD_PRESSURE_TRANSFER_CHAR_WRITE_UUID);
                                }
                            }

                            doNextAction();
                        }
                        else
                        {
                            Log.d(TAG, "processData() - received an unknown command: " + commandBytes[0] + " [" + commandBytes + "]");
                        }
                    }
                    else
                    {
                        Log.d(TAG, "processData() - message length < 5 --> " + commandBytes.length);
                    }
                }
            }
        }
        else if (thisUUID.equals(BLOOD_PRESSURE_MEASUREMENT_CHAR_INDICATE_UUID))
        {
            String parsedValue = Utils.parse(characteristic);
            Log.d(TAG, "processData() - Measurement --> " + parsedValue);

            BloodPressureData data = convertMeasureToBloodPressureData(characteristic.getValue());

            writeActionProgress("Measurement --> value " + parsedValue);

            if(data != null)
            {
                writeData(data);
                writeActionProgress("Measurement --> Unit " + data.getUnit() + " (0=mmHg,1=kPa)");
                writeActionProgress("Measurement --> Systolic " + data.getSystolic() + " mmHg");
                writeActionProgress("Measurement --> Diastolic " + data.getDiastolic() + " mmHg");
                writeActionProgress("Measurement --> Mean Arterial " + data.getMeanArterial() + " mmHg");
                writeActionProgress("Measurement --> Heart Rate " + data.getHeartRate() + "beats/min");
                writeActionProgress("Measurement --> Timestamp " + data.getTimestamp());
                writeActionProgress("Measurement --> User Id " + data.getUserId());
            }
            else
            {
                writeActionProgress("Measurement --> Unparseable value! ");
            }

            //send enable disconnect
            sendEnableDisconnection(APP_TO_BLOOD_PRESSURE_TRANSFER_CHAR_WRITE_UUID);
            doNextAction();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void writeDataToBel(UUID characteristic, byte[] command, String description)
    {
        BLECommunicationObject object = new BLECommunicationObject();
        object.setType(BLECommunicationObject.TYPE_CHARACTERISTIC);
        object.setAction(BLECommunicationObject.ACTION_WRITE);
        object.setCharacteristic(characteristic);
        object.setValue(command);
        object.setDescription(description);
        object.setMustEnableNotify(true);
        communicationActionQueue.add(object);

        Log.d(TAG, "writeDataToBel() - queued [" + Utils.byteArrayToHexString(command) + "] to [" + characteristic + "]");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void sendAccountId(UUID characteristic)
    {
        byte[] command = {(byte) 0x21, (byte) 0x4B, (byte) 0x09, (byte) 0xDF, (byte) 0x78};

        this.writeDataToBel(characteristic, command, "account id");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void sendVerificationCode(UUID characteristic, String randomNumber)
    {
        byte[] command = new byte[5];
        command[0] = (byte) 0x20;

        String xorValue = Utils.xorHex(passwordBloodPressure, randomNumber);

        byte[] xorBytes = Utils.hexStringToByteArray(xorValue);
        if (xorBytes != null)
        {
            command[1] = xorBytes[3];
            command[2] = xorBytes[2];
            command[3] = xorBytes[1];
            command[4] = xorBytes[0];
        }

        this.writeDataToBel(characteristic, command, "verification code");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void sendTimeOffset(UUID characteristic)
    {
        byte[] command = new byte[5];

        long diffInMs = new Date().getTime() - Utils.getDate(2010, 1, 1, 0, 0, 0).getTime();

        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);

        String hexValue = Long.toHexString(diffInSec);

        hexValue = Utils.panString(hexValue, "0", 8);

        byte[] date = Utils.hexStringToByteArray(hexValue);

        command[0] = (byte) 0x02;
        command[1] = date[3];
        command[2] = date[2];
        command[3] = date[1];
        command[4] = date[0];

        this.writeDataToBel(characteristic, command, "time offset");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void sendEnableDisconnection(UUID characteristic)
    {
        byte[] command = new byte[5];
        command[0] = (byte) 0x22;
        command[1] = (byte) 0x00;
        command[2] = (byte) 0x00;
        command[3] = (byte) 0x00;
        command[4] = (byte) 0x00;

        this.writeDataToBel(characteristic, command, "enable disconnection");
    }

    private BloodPressureData convertMeasureToBloodPressureData(byte[] measure)
    {
        String inputHex = null;
        BloodPressureData data = null;
        byte[] tmpBytes = null;

        if(measure != null)
        {
            data = new BloodPressureData();

            //unit
            inputHex = "" + Utils.getBit((byte)measure[0], 0);
            data.setUnit(Integer.parseInt(inputHex, 16));
            //TODO: if is 1, must convert from mmHg to kPa

            //systolic blood pressure
            tmpBytes = new byte[2];
            tmpBytes[0] = measure[2];
            tmpBytes[1] = measure[1];
            inputHex = Utils.byteArrayToHexString(tmpBytes);
            data.setSystolic(Integer.parseInt(inputHex, 16));

            //diastolic blood pressure
            tmpBytes[0] = measure[4];
            tmpBytes[1] = measure[3];
            inputHex = Utils.byteArrayToHexString(tmpBytes);
            data.setDiastolic(Integer.parseInt(inputHex, 16));

            //mean arterial
            tmpBytes[0] = measure[6];
            tmpBytes[1] = measure[5];
            inputHex = Utils.byteArrayToHexString(tmpBytes);
            data.setMeanArterial(Integer.parseInt(inputHex, 16));

            //timestamp
            byte[] timeBytes = new byte[4];
            timeBytes[0] = measure[10];
            timeBytes[1] = measure[9];
            timeBytes[2] = measure[8];
            timeBytes[3] = measure[7];

            inputHex = Utils.byteArrayToHexString(timeBytes);

            long seconds = Long.parseLong(inputHex, 16);
            long dateInMs = Utils.getDate(2010, 1, 1, 0, 0, 0).getTime() + (seconds * 1000);
            Date timestamp = new Date();
            timestamp.setTime(dateInMs);
            data.setTimestamp(timestamp);

            //heart rate
            tmpBytes[0] = measure[12];
            tmpBytes[1] = measure[11];
            inputHex = Utils.byteArrayToHexString(tmpBytes);
            data.setHeartRate(Integer.parseInt(inputHex, 16));

            //user id
            inputHex = "" + (byte)measure[13];
            data.setUserId(Integer.parseInt(inputHex, 16));
        }

        return data;
    }

    private void writeData(final BloodPressureData data){
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {

                readTodayData();

                //minHg.setText(String.valueOf(diastolicPressure));
                entriesMin.get(0).setY(data.getDiastolic() * 0.8f);
                entriesMin.get(1).setY(100 - (data.getDiastolic() * 0.8f));
                chartMin.setCenterText("" + data.getDiastolic());
                chartMin.notifyDataSetChanged();
                chartMin.invalidate();

                entriesMax.get(0).setY(data.getSystolic() * 0.5f);
                entriesMax.get(1).setY(100 - (data.getSystolic() * 0.5f));
                chartMax.setCenterText("" + data.getSystolic());
                chartMax.notifyDataSetChanged();
                chartMax.invalidate();

                Gson gson = new Gson();
                String json = gson.toJson(data);
                storeInMemory.setValueStringToStore("BloodPressureData", json);

                bloodPressureDataToday.add(data);
                gson = new Gson();
                json = gson.toJson(bloodPressureDataToday);
                Log.i("---string---", json);
                Calendar today = Calendar.getInstance();
                String date = "" + today.get(Calendar.DAY_OF_MONTH) + today.get(Calendar.MONTH);
                try
                {
                    fileMamoryService.writeFile(FILE_BLOOD_PRESSURE_DATA + "_" + date + ".txt", json);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    Log.e("Errore", e.getMessage());
                }
            }
        });
    }

    private void readTodayData() {

        String json = null;
        Type listTypeDaySaveDataBean = new TypeToken<ArrayList<BloodPressureData>>() {
        }.getType();
        Calendar today = Calendar.getInstance();
        String date = "" + today.get(Calendar.DAY_OF_MONTH) + today.get(Calendar.MONTH);
        String path=getApplicationContext().getFilesDir()+"/"+currentMonth+"/"+FILE_BLOOD_PRESSURE_DATA+"_"+date+".txt";
        try {
            json = fileMamoryService.readFromFile(path);
            if (!json.equals("")) {
                bloodPressureDataToday = new Gson().fromJson(json, listTypeDaySaveDataBean);
            } else {
                bloodPressureDataToday = new ArrayList<BloodPressureData>();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
