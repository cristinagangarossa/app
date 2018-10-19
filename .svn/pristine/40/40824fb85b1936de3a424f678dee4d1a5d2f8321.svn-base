package com.neperiagroup.happysalus;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neperiagroup.happysalus.Renderers.HorizontalBarChartRenderer;
import com.neperiagroup.happysalus.adapters.WeekDataAdapter;
import com.neperiagroup.happysalus.bean.BLECommunicationObject;
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

public class ScaleStatsActivity extends AppCompatActivity{

    private static final String TAG = ScaleStatsActivity.class.getName();
    static UUID SCALE_SERVICE_UUID = Utils.convertFromInteger(0x7802);
    static UUID APP_TO_SCALE_TRANSFER_CHAR_WRITE_UUID = Utils.convertFromInteger(0x8A81);
    static UUID SCALE_TO_APP_TRANSFER_CHAR_INDICATE_UUID = Utils.convertFromInteger(0x8A82);
    static UUID SCALE_WEIGHT_MEASUREMENT_CHAR_INDICATE_UUID = Utils.convertFromInteger(0x8A21);
    static UUID SCALE_BODY_COMPOSITION_CHAR_INDICATE_UUID = Utils.convertFromInteger(0x8A22);
    static UUID SCALE_WEIGHT_MEASUREMENT_CHAR_NOTIFY_UUID = Utils.convertFromInteger(0x8A23);

    static UUID CLIENT_CHARACTERISTIC_CONFIG_UUID = Utils.convertFromInteger(0x2902);

    final static String SCALE_MODEL = "203B";

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
    private String passwordScale = null;
    private String accountId = "4B09DF78";

    private StoreInMemory storeInMemory;
    private TagUser tagUser;
    private User user;
    private Button buttonRead;
    private boolean canStartScan;

    private static final String DEVICE_DATA_KEY = TAG + ".device_data";
    private static final String DEVICE_NAME_KEY = TAG + ".device_name";
    private final String FILE_SCALE="scale";
    public static final String currentMonth="current_month";

    private Toolbar toolbar;
    private TextView toolbarTitle;

    private String deviceName;
    private DataDevice deviceData;

    private TableLayout table;

    private ListView weeksDataList;

    private List<ScaleData> scaleToday;
    private FileMamoryService fileMamoryService;
    private List<ScaleData> todayData;


    List<PieEntry> fmEntries = new ArrayList<>();
    ArrayList<Integer> colors = new ArrayList<Integer>();
    PieChart fmChart;

    List<PieEntry> lbmEntries = new ArrayList<>();
    PieChart lbmChart;

    List<PieEntry> waterEntries = new ArrayList<>();
    PieChart waterChart;
    TextView weightValue;

    BarChart chart;

    BarChart bmiChart;

    Button readButton;





    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_stats);

        fileMamoryService=new FileMamoryService(getApplicationContext());
        try {
            todayData=fileMamoryService.getScaleDayData(new Date());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //deserialize device object.
        deviceData = (DataDevice) getIntent().getSerializableExtra(DEVICE_DATA_KEY);
        deviceName = getIntent().getStringExtra(DEVICE_NAME_KEY);

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
            //SharedPreferences sharedPref = ScaleStatsActivity.this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences sharedPref = getSharedPreferences("prefs", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove("password_scale");
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
                    writeActionProgress("Inizio la misurazione del peso");
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(getApplicationContext(), "Inizio la misurazione del peso", Toast.LENGTH_SHORT).show();

                        }
                    });
                    startDiscoveryProcess();
                }
            }
        });

        //stats toolbar setup.
        toolbar = findViewById(R.id.statsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = findViewById(R.id.stats_toolbar_title);
        toolbarTitle.setText(deviceName);

        //table data setup.
        weeksDataList=findViewById(R.id.listWeekData);

        WeekDataAdapter weekDataAdapter=new WeekDataAdapter(getApplicationContext(),getWeeks());
        weeksDataList.setAdapter(weekDataAdapter);

        fmChart=findViewById(R.id.fmChart);
        lbmChart=findViewById(R.id.lbmChart);
        waterChart=findViewById(R.id.waterChart);

        paintFmChart(18f);
        paintLbmChart(5f);
        paintWaterChart(62.7f);


        bmiChart =(HorizontalBarChart)findViewById(R.id.BarChart);

        gethorizontalBarChart(12.0f);
        weightValue=findViewById(R.id.weightValue);
        if(todayData!=null && todayData.size()!=0){
            weightValue.setText(String.valueOf( todayData.get(todayData.size()-1).getWeight()));
        }else{
            weightValue.setText("0");
        }



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
//        switch(item.getItemId()){
//            case R.id.settings:
//                startActivity(SettingsActivity.getInstanceIntent(getApplicationContext()));
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getInstanceIntent(Context context, DataDevice deviceData, String deviceName){
        Intent intent = new Intent(context, ScaleStatsActivity.class);
        intent.putExtra(DEVICE_DATA_KEY, deviceData);
        intent.putExtra(DEVICE_NAME_KEY, deviceName);
        return intent;
    }

    @SuppressLint("ResourceAsColor")
    private void addTableHeader(TableLayout table){
        TableRow header = new TableRow(this);
        header.setBackgroundColor(R.color.gray_light);
        TextView thisWeek = new TextView(this);
        thisWeek.setText("Questa settimana");
        header.addView(thisWeek);
        table.addView(header);
    }

    @SuppressLint("ResourceAsColor")
    private void addTableRow(TableLayout table){
        TableRow row = new TableRow(this);
        row.setBackgroundColor(R.color.white);

        TextView day = new TextView(this);
        day.setText("Mer");
        row.addView(day);

        TextView value = new TextView(this);
        value.setText("65,2 kg");
        row.addView(value);

        table.addView(row);
    }

    private void setMockedTable(TableLayout table){
        table.setStretchAllColumns(true);
        table.setShrinkAllColumns(true);

        TableRow rowTitle = new TableRow(this);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TableRow rowDayLabels = new TableRow(this);
        TableRow rowHighs = new TableRow(this);
        TableRow rowLows = new TableRow(this);
        TableRow rowConditions = new TableRow(this);
        rowConditions.setGravity(Gravity.CENTER);

        TextView empty = new TextView(this);

        // title column/row
        TextView title = new TextView(this);
        title.setText("Java Weather Table");

        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.SERIF, Typeface.BOLD);

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 6;

        rowTitle.addView(title, params);

        // labels column
        TextView highsLabel = new TextView(this);
        highsLabel.setText("Day High");
        highsLabel.setTypeface(Typeface.DEFAULT_BOLD);

        TextView lowsLabel = new TextView(this);
        lowsLabel.setText("Day Low");
        lowsLabel.setTypeface(Typeface.DEFAULT_BOLD);

        TextView conditionsLabel = new TextView(this);
        conditionsLabel.setText("Conditions");
        conditionsLabel.setTypeface(Typeface.DEFAULT_BOLD);

        rowDayLabels.addView(empty);
        rowHighs.addView(highsLabel);
        rowLows.addView(lowsLabel);
        rowConditions.addView(conditionsLabel);

        // day 1 column
        TextView day1Label = new TextView(this);
        day1Label.setText("Feb 7");
        day1Label.setTypeface(Typeface.SERIF, Typeface.BOLD);

        TextView day1High = new TextView(this);
        day1High.setText("28°F");
        day1High.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView day1Low = new TextView(this);
        day1Low.setText("15°F");
        day1Low.setGravity(Gravity.CENTER_HORIZONTAL);

        rowDayLabels.addView(day1Label);
        rowHighs.addView(day1High);
        rowLows.addView(day1Low);

        // day2 column
        TextView day2Label = new TextView(this);
        day2Label.setText("Feb 8");
        day2Label.setTypeface(Typeface.SERIF, Typeface.BOLD);

        TextView day2High = new TextView(this);
        day2High.setText("26°F");
        day2High.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView day2Low = new TextView(this);
        day2Low.setText("14°F");
        day2Low.setGravity(Gravity.CENTER_HORIZONTAL);

        rowDayLabels.addView(day2Label);
        rowHighs.addView(day2High);
        rowLows.addView(day2Low);

        // day3 column
        TextView day3Label = new TextView(this);
        day3Label.setText("Feb 9");
        day3Label.setTypeface(Typeface.SERIF, Typeface.BOLD);

        TextView day3High = new TextView(this);
        day3High.setText("23°F");
        day3High.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView day3Low = new TextView(this);
        day3Low.setText("3°F");
        day3Low.setGravity(Gravity.CENTER_HORIZONTAL);

        rowDayLabels.addView(day3Label);
        rowHighs.addView(day3High);
        rowLows.addView(day3Low);

        // day4 column
        TextView day4Label = new TextView(this);
        day4Label.setText("Feb 10");
        day4Label.setTypeface(Typeface.SERIF, Typeface.BOLD);

        TextView day4High = new TextView(this);
        day4High.setText("17°F");
        day4High.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView day4Low = new TextView(this);
        day4Low.setText("5°F");
        day4Low.setGravity(Gravity.CENTER_HORIZONTAL);

        rowDayLabels.addView(day4Label);
        rowHighs.addView(day4High);
        rowLows.addView(day4Low);

        // day5 column
        TextView day5Label = new TextView(this);
        day5Label.setText("Feb 11");
        day5Label.setTypeface(Typeface.SERIF, Typeface.BOLD);

        TextView day5High = new TextView(this);
        day5High.setText("19°F");
        day5High.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView day5Low = new TextView(this);
        day5Low.setText("6°F");
        day5Low.setGravity(Gravity.CENTER_HORIZONTAL);

        rowDayLabels.addView(day5Label);
        rowHighs.addView(day5High);
        rowLows.addView(day5Low);

        table.addView(rowTitle);
        table.addView(rowDayLabels);
        table.addView(rowHighs);
        table.addView(rowLows);
        table.addView(rowConditions);
    }
    /*
    private List<WeekData> getWeeks() {
        //TODO get from storage
        List<WeekData> weeks = new ArrayList<WeekData>();
        WeekData current = new WeekData("Questa Settimana", "68.5 Media");
        List<GenericDayData> days1 = new ArrayList<GenericDayData>();
        BalanceDayData day1 = new BalanceDayData();
        Date date1 = new Date();
        day1.setDate(date1);
        day1.setData("25");
        day1.setData("68");

        BalanceDayData day2 = new BalanceDayData();
        day2.setDate(date1);
        day2.setData("69");


        days1.add(day1);
        days1.add(day2);
        current.setDays(days1);
        WeekData pre = new WeekData("24 SET - 30 SET", "68 Media");

        weeks.add(current);
        weeks.add(pre);

        return weeks;
    }
    */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<WeekData> getWeeks() {

        List<WeekData> weeks=new ArrayList<WeekData>();
        FileMamoryService fileMamoryService=new FileMamoryService(getApplicationContext());
        try {
            weeks=fileMamoryService.getAllWeekScaleData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weeks;
    }



    public void paintFmChart(float val )
    {

        int[] colors = new int[] {Color.argb(255,255,255,255), Color.TRANSPARENT};
        fmEntries.add(new PieEntry(val));

        //Log.d("percentuale ","val "+val);
        float other = 100-val;
        //Log.d("percentuale ","other "+other);
        fmEntries.add(new PieEntry(other));

        PieDataSet set = new PieDataSet(fmEntries,null);
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

        fmChart.setTransparentCircleRadius(94);
        fmChart.setTransparentCircleColor(getResources().getColor(R.color.light));
        fmChart.getDescription().setEnabled(false);
        fmChart.setRotationEnabled(false);
        fmChart.setCenterText(value+"\n%");
        fmChart.setCenterTextSize(20);
        fmChart.setCenterTextColor(Color.WHITE);
        fmChart.getLegend().setEnabled(false);
        fmChart.getDescription().setEnabled(false);
        fmChart.setEntryLabelColor(Color.TRANSPARENT);
        fmChart.setDrawEntryLabels(false);
        fmChart.setDrawHoleEnabled(true);
        fmChart.setHoleRadius(90.0f);
        fmChart.setHoleColor(Color.TRANSPARENT);
        fmChart.setData(data);
        //chart.spin( 500,0,360 - angleTo, Easing.EasingOption.EaseInOutQuad ); // rotazione del grafico
        fmChart.animateY(1000);
        fmChart.notifyDataSetChanged(); // let the chart know it's data changed

        //PieChartRenderer custom= new PieChartRenderer(chart, chart.getAnimator(),chart.getViewPortHandler());
        //chart.setRenderer(custom);
        fmChart.invalidate(); // refresh

    }

    public void paintLbmChart(float val )
    {

        int[] colors = new int[] {Color.argb(255,255,255,255), Color.TRANSPARENT};
        lbmEntries.add(new PieEntry(val));

        //Log.d("percentuale ","val "+val);
        float other = 100-val;
        //Log.d("percentuale ","other "+other);
        lbmEntries.add(new PieEntry(other));

        PieDataSet set = new PieDataSet(lbmEntries,null);
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

        lbmChart.setTransparentCircleRadius(94);
        lbmChart.setTransparentCircleColor(getResources().getColor(R.color.light));
        lbmChart.getDescription().setEnabled(false);
        lbmChart.setRotationEnabled(false);
        lbmChart.setCenterText(value+"\n%");
        lbmChart.setCenterTextSize(20);
        lbmChart.setCenterTextColor(Color.WHITE);
        lbmChart.getLegend().setEnabled(false);
        lbmChart.getDescription().setEnabled(false);
        lbmChart.setEntryLabelColor(Color.TRANSPARENT);
        lbmChart.setDrawEntryLabels(false);
        lbmChart.setDrawHoleEnabled(true);
        lbmChart.setHoleRadius(90.0f);
        lbmChart.setHoleColor(Color.TRANSPARENT);
        lbmChart.setData(data);
        //chart.spin( 500,0,360 - angleTo, Easing.EasingOption.EaseInOutQuad ); // rotazione del grafico
        lbmChart.animateY(1000);
        lbmChart.notifyDataSetChanged(); // let the chart know it's data changed

        //PieChartRenderer custom= new PieChartRenderer(chart, chart.getAnimator(),chart.getViewPortHandler());
        //chart.setRenderer(custom);
        lbmChart.invalidate(); // refresh

    }

    public void paintWaterChart(float val )
    {

        int[] colors = new int[] {Color.argb(255,255,255,255), Color.TRANSPARENT};
        waterEntries.add(new PieEntry(val));

        //Log.d("percentuale ","val "+val);
        float other = 100-val;
        //Log.d("percentuale ","other "+other);
        waterEntries.add(new PieEntry(other));

        PieDataSet set = new PieDataSet(waterEntries,null);
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

        waterChart.setTransparentCircleRadius(94);
        waterChart.setTransparentCircleColor(getResources().getColor(R.color.light));
        waterChart.getDescription().setEnabled(false);
        waterChart.setRotationEnabled(false);
        waterChart.setCenterText(value+"\n%");
        waterChart.setCenterTextSize(20);
        waterChart.setCenterTextColor(Color.WHITE);
        waterChart.getLegend().setEnabled(false);
        waterChart.getDescription().setEnabled(false);
        waterChart.setEntryLabelColor(Color.TRANSPARENT);
        waterChart.setDrawEntryLabels(false);
        waterChart.setDrawHoleEnabled(true);
        waterChart.setHoleRadius(90.0f);
        waterChart.setHoleColor(Color.TRANSPARENT);
        waterChart.setData(data);
        //chart.spin( 500,0,360 - angleTo, Easing.EasingOption.EaseInOutQuad ); // rotazione del grafico
        waterChart.animateY(1000);
        waterChart.notifyDataSetChanged(); // let the chart know it's data changed

        //PieChartRenderer custom= new PieChartRenderer(chart, chart.getAnimator(),chart.getViewPortHandler());
        //chart.setRenderer(custom);
        waterChart.invalidate(); // refresh

    }

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public void gethorizontalBarChart(float val)
    {
        ArrayList<BarEntry> yVals =  new ArrayList<>();
        yVals.add(new BarEntry(2f,val)); //index,value)

        BarDataSet set1= new BarDataSet(yVals, "");
        set1.setDrawValues(false);
        set1.setColor(getResources().getColor(R.color.white));

        BarData data = new BarData(set1);
        data.setBarWidth(0.1f);


        YAxis yl = bmiChart.getAxisLeft();
        yl.setAxisMinimum(0f);
        yl.setAxisMaximum(50f);
        yl.setTextColor(Color.TRANSPARENT);
        yl.setEnabled(false);

        YAxis yr = bmiChart.getAxisRight();
        yr.setAxisMinimum(0f);
        yr.setAxisMaximum(50f);

        // int[] rootLocation = new int[2];
        bmiChart.setData(data);
        bmiChart.setScaleEnabled(false);
        //chart.getLocationOnScreen(rootLocation);
        bmiChart.getDescription().setEnabled(false);
        bmiChart.setDrawGridBackground(false);
        bmiChart.getXAxis().setDrawGridLines(false);
        bmiChart.getAxisLeft().setDrawGridLines(false);
        bmiChart.getAxisRight().setDrawGridLines(false);
        bmiChart.getXAxis().setEnabled(false);
        bmiChart.getXAxis().setCenterAxisLabels(true);
        bmiChart.getXAxis().setAxisMinimum(0);
        bmiChart.getXAxis().setAxisMinimum(1);
        bmiChart.setDrawBarShadow(true);
        bmiChart.getLegend().setEnabled(false);
        bmiChart.animateY(1000);
        bmiChart.invalidate();

        //Bitmap pointBitmap  = BitmapFactory.decodeResource(getResources(), drawable.circle_shape);
        Bitmap bit0= getBitmap(R.drawable.circle_shape);
        Bitmap bit10=getBitmap(R.drawable.circle_shape);
        Bitmap bit20= getBitmap(R.drawable.circle_shape);
        Bitmap bit30=getBitmap(R.drawable.circle_shape);
        Bitmap bit40= getBitmap(R.drawable.circle_shape);
        Bitmap bit50=getBitmap(R.drawable.circle_shape);

        Bitmap [] points ={bit0,bit10,bit20,bit30,bit40,bit50};
        HorizontalBarChartRenderer barChartCustomRenderer = new HorizontalBarChartRenderer(bmiChart,bmiChart.getAnimator(),bmiChart.getViewPortHandler(),points);
        bmiChart.setDrawingCacheBackgroundColor(getResources().getColor(R.color.light));
        bmiChart.setRenderer(barChartCustomRenderer);
        bmiChart.invalidate();
    }

    private void readTodayData() {

        String json = null;
        Type listTypeDaySaveDataBean = new TypeToken<ArrayList<ScaleData>>() {
        }.getType();
        Calendar today = Calendar.getInstance();
        String date = "" + today.get(Calendar.DAY_OF_MONTH) + today.get(Calendar.MONTH);
        String path=getApplicationContext().getFilesDir()+"/"+currentMonth+"/"+FILE_SCALE+"_"+date+".txt";
        try {
            json = fileMamoryService.readFromFile(path);
            if (!json.equals("")) {
                scaleToday = new Gson().fromJson(json, listTypeDaySaveDataBean);
            } else {
                scaleToday = new ArrayList<>();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeData(final ScaleData scaleData){

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                weightValue.setText(String.valueOf(scaleData.getWeight()));
                readTodayData();
            }
        });


        Gson gson = new Gson();
        String json = gson.toJson(scaleData);
        storeInMemory.setValueStringToStore("lastScaleData",json);


        scaleToday.add(scaleData);

        json = gson.toJson(scaleToday);
        Log.i("---string---", json);
        Calendar today = Calendar.getInstance();
        String date = "" + today.get(Calendar.DAY_OF_MONTH) + today.get(Calendar.MONTH);
        try {
            fileMamoryService.writeFile(FILE_SCALE + "_" + date + ".txt", json);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Errore", e.getMessage());
        }

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
        if (!this.checkParing(SCALE_MODEL))
        {
            //if the device is not connected to the app:
            // set the device name to connect to
            // set the status of the protocol to start from
            storeInMemory.setNameBtcDeviceUserBindToStore(user.getId(), SCALE_MODEL);
            deviceToDiscoverName = "1" + SCALE_MODEL;
        }
        else
        {
            deviceToDiscoverName = "0" + SCALE_MODEL + " " + accountId;
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
                                gatt = myDevice.connectGatt(ScaleStatsActivity.this, true, gattCallback);
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
                    gatt = myDevice.connectGatt(ScaleStatsActivity.this, true, gattCallback);
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
                    if (!checkParing(SCALE_MODEL))
                    {
                        characteristicsSent = true;
                    }
                    BLECommunicationObject object = null;

                    //enable indicate on SCALE_TO_APP_TRANSFER_CHAR_INDICATE_UUID
                    Log.d(TAG, "onServicesDiscovered() - queue enable scale to app indication " + SCALE_TO_APP_TRANSFER_CHAR_INDICATE_UUID);
                    object = new BLECommunicationObject();
                    object.setType(BLECommunicationObject.TYPE_DESCRIPTOR);
                    object.setCharacteristic(SCALE_TO_APP_TRANSFER_CHAR_INDICATE_UUID);
                    object.setDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID);
                    object.setAction(BLECommunicationObject.ACTION_WRITE);
                    object.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                    object.setDescription("enable scale to app indication");
                    object.setMustEnableNotify(true);
                    communicationActionQueue.add(object);

                    //enable indicate on SCALE_WEIGHT_MEASUREMENT_CHAR_INDICATE_UUID
                    Log.d(TAG, "onServicesDiscovered() - queue enable scale weight measurement indication " + SCALE_WEIGHT_MEASUREMENT_CHAR_INDICATE_UUID);
                    object = new BLECommunicationObject();
                    object.setType(BLECommunicationObject.TYPE_DESCRIPTOR);
                    object.setCharacteristic(SCALE_WEIGHT_MEASUREMENT_CHAR_INDICATE_UUID);
                    object.setDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID);
                    object.setAction(BLECommunicationObject.ACTION_WRITE);
                    object.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                    object.setDescription("enable scale weight measurement indication");
                    object.setMustEnableNotify(true);
                    communicationActionQueue.add(object);

                    //enable indicate on SCALE_BODY_COMPOSITION_CHAR_INDICATE_UUID
                    Log.d(TAG, "onServicesDiscovered() - queue enable scale body composition indication " + SCALE_BODY_COMPOSITION_CHAR_INDICATE_UUID);
                    object = new BLECommunicationObject();
                    object.setType(BLECommunicationObject.TYPE_DESCRIPTOR);
                    object.setCharacteristic(SCALE_BODY_COMPOSITION_CHAR_INDICATE_UUID);
                    object.setDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID);
                    object.setAction(BLECommunicationObject.ACTION_WRITE);
                    object.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                    object.setDescription("enable scale body composition indication");
                    object.setMustEnableNotify(true);
                    communicationActionQueue.add(object);

                    //enable notify on SCALE_WEIGHT_MEASUREMENT_CHAR_NOTIFY_UUID
                    Log.d(TAG, "onServicesDiscovered() - queue enable scale weight measurement notify " + SCALE_WEIGHT_MEASUREMENT_CHAR_NOTIFY_UUID);
                    object = new BLECommunicationObject();
                    object.setType(BLECommunicationObject.TYPE_DESCRIPTOR);
                    object.setCharacteristic(SCALE_WEIGHT_MEASUREMENT_CHAR_NOTIFY_UUID);
                    object.setDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID);
                    object.setAction(BLECommunicationObject.ACTION_WRITE);
                    object.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    object.setDescription("enable scale weight measurement notify");
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
                Log.d(TAG, "doNextAction() - Paring process terminated, must store passwordScale");

                mustStorePassword = false;
                this.savePassword(passwordScale);
                processTerminated = true;
            }

            if (communicationActionQueue.size() > 0)
            {
                BLECommunicationObject object = communicationActionQueue.element();
                if (object != null)
                {
                    service = gatt.getService(SCALE_SERVICE_UUID);
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
                                        if ((characteristic != null) && (characteristic.getUuid().equals(APP_TO_SCALE_TRANSFER_CHAR_WRITE_UUID)) && (object.getValue() != null) && (object.getValue()[0] == (byte)0x22))
                                        {
                                            if (!checkParing(SCALE_MODEL))
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
        //SharedPreferences sharedPref = ScaleStatsActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPref = getSharedPreferences("prefs", 0);
        String password = sharedPref.getString("password_scale", null);

        if (password != null)
        {
            ScaleStatsActivity.this.passwordScale = password;
            result = true;
        }

        return result;
    }

    private void savePassword(String password)
    {
        //SharedPreferences sharedPref = ScaleStatsActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPref = getSharedPreferences("prefs", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("password_scale", password);
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

        if (thisUUID.equals(SCALE_TO_APP_TRANSFER_CHAR_INDICATE_UUID))
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
                            passwordScale = commandBytes[4] + commandBytes[3] + commandBytes[2] + commandBytes[1];

                            if (passwordScale != null)
                            {
                                Log.d(TAG, "processData() - received passwordScale --> " + passwordScale);
                                writeActionProgress("received password " + passwordScale);
                                Log.d(TAG, "processData() - queue account id");
                                sendAccountId(APP_TO_SCALE_TRANSFER_CHAR_WRITE_UUID);
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
                                sendVerificationCode(APP_TO_SCALE_TRANSFER_CHAR_WRITE_UUID, randomNumber);
                                Log.d(TAG, "processData() - queue time offset");
                                sendTimeOffset(APP_TO_SCALE_TRANSFER_CHAR_WRITE_UUID);

                                if (!this.checkParing(SCALE_MODEL))
                                {
                                    Log.d(TAG, "processData() - queue enable disconnection");
                                    sendEnableDisconnection(APP_TO_SCALE_TRANSFER_CHAR_WRITE_UUID);
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
        else if (thisUUID.equals(SCALE_WEIGHT_MEASUREMENT_CHAR_INDICATE_UUID))
        {
            String parsedValue = Utils.parse(characteristic);
            Log.d(TAG, "processData() - Measurement --> " + parsedValue);

            ScaleData data = convertMeasureToScaleData(characteristic.getValue());

            writeActionProgress("Measurement --> " + parsedValue);

            if(data != null)
            {
                writeData(data);
                writeActionProgress("Measurement --> Scale " + data.getScale() + " (00=Kg,01=LB,10=st)");
                writeActionProgress("Measurement --> Weight " + data.getWeight() + " Kg");
                writeActionProgress("Measurement --> Timestamp " + data.getTimestamp());
                writeActionProgress("Measurement --> Impedance " + data.getImpedance() + " ohm");
                writeActionProgress("Measurement --> User Id " + data.getUserId());
                writeActionProgress("Measurement --> Weight Status Stability " + data.getWeightStatusStability() + " (0=Unstable,1=Stable)");
                writeActionProgress("Measurement --> Weight Status Type " + data.getWeightStatusType() + " (000=Idle,001=Processing,010=Measuring with shoes,011=Measuring with bare feet,100=Measuring Complete,101=Error)");
            }
            else
            {
                writeActionProgress("Measurement --> Unparseable value! ");
            }

            //send enable disconnect
            sendEnableDisconnection(APP_TO_SCALE_TRANSFER_CHAR_WRITE_UUID);
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

        String xorValue = Utils.xorHex(passwordScale, randomNumber);

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

    private ScaleData convertMeasureToScaleData(byte[] measure)
    {
        String inputHex = null;
        ScaleData data = null;

        if(measure != null)
        {
            data = new ScaleData();

            //scale
            inputHex = "" + Utils.getBit((byte)measure[0], 5) + "" + Utils.getBit((byte)measure[0], 6);
            data.setScale(Integer.parseInt(inputHex, 16));
            //TODO: if is 1, must convert from Kg to LB, if is 10 from Kg to st

            //weight
            byte[] weightBytes = new byte[3];
            weightBytes[0] = measure[3];
            weightBytes[1] = measure[2];
            weightBytes[2] = measure[1];

            inputHex = Utils.byteArrayToHexString(weightBytes);

            int firstPart = Integer.parseInt(inputHex, 16);
            inputHex = "" + (byte)(measure[4] + 1);
            int secondPart = Integer.parseInt(inputHex, 16);
            data.setWeight((firstPart * 10 ^(-(~ secondPart))) / 1000.0);

            //timestamp
            byte[] timeBytes = new byte[4];
            timeBytes[0] = measure[8];
            timeBytes[1] = measure[7];
            timeBytes[2] = measure[6];
            timeBytes[3] = measure[5];

            inputHex = Utils.byteArrayToHexString(timeBytes);

            long seconds = Long.parseLong(inputHex, 16);
            long dateInMs = Utils.getDate(2010, 1, 1, 0, 0, 0).getTime() + (seconds * 1000);
            Date timestamp = new Date();
            timestamp.setTime(dateInMs);
            data.setTimestamp(timestamp);

            //impedance
            byte[] impedanceBytes = new byte[3];
            impedanceBytes[0] = measure[15];
            impedanceBytes[1] = measure[14];
            impedanceBytes[2] = measure[13];

            inputHex = Utils.byteArrayToHexString(impedanceBytes);
            firstPart = Integer.parseInt(inputHex, 16);

            inputHex = "" + (byte)(measure[16] + 1);
            secondPart = Integer.parseInt(inputHex, 16);

            data.setImpedance(firstPart * 10 ^(-(~ secondPart)));

            //user id
            inputHex = "" + (byte)measure[17];
            data.setUserId(Integer.parseInt(inputHex, 16));

            //weight status stability
            inputHex = "" + Utils.getBit((byte)measure[18], 0);
            data.setWeightStatusStability(Integer.parseInt(inputHex, 16));

            //weight status type
            inputHex = "" + Utils.getBit((byte)measure[18], 3) + "" + Utils.getBit((byte)measure[18], 2) + "" + Utils.getBit((byte)measure[18], 1);
            data.setWeightStatusType(inputHex);
        }

        return data;
    }
}
