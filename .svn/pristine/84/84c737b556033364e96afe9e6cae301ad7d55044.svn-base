package com.neperiagroup.happysalus;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.htsmart.wristband.WristbandApplication;
import com.htsmart.wristband.bean.WristbandConfig;
import com.htsmart.wristband.bean.WristbandVersion;
import com.htsmart.wristband.performer.IDevicePerformer;

import com.htsmart.wristband.performer.SimplePerformerListener;
import com.neperiagroup.happysalus.adapters.WeekDataAdapter;
import com.neperiagroup.happysalus.bean.DataDevice;
import com.neperiagroup.happysalus.bean.GenericDayData;
import com.neperiagroup.happysalus.bean.HrDayData;
import com.neperiagroup.happysalus.bean.OxygenDayData;
import com.neperiagroup.happysalus.bean.SaveDataBean;
import com.neperiagroup.happysalus.bean.StepDayData;
import com.neperiagroup.happysalus.bean.WeekData;
import com.neperiagroup.happysalus.services.FileMamoryService;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.Math.round;

public class OxygenStatsActivity extends AppCompatActivity{
    private static final String TAG = OxygenStatsActivity.class.getName();
    private static final String DEVICE_DATA_KEY = TAG + ".device_data";
    private static final String DEVICE_NAME_KEY = TAG + ".device_name";

    List<PieEntry> entries = new ArrayList<>();
    ArrayList<Integer> colors = new ArrayList<Integer>();
    PieChart chart;
    Map<Integer,Float> OxygenWeek;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    private String deviceName;

    private TextView mOxyValue;


    private IDevicePerformer mDevicePerformer = WristbandApplication.getDevicePerformer();

    private float lastValue;
    Button readButton;

    HorizontalScrollView weekScroll;
    HorizontalScrollView monthScroll;

    Button lun;
    Button mart;
    Button mer;
    Button gio;
    Button ven;
    Button sab;
    Button dom;

    private ListView weeksDataList;
    private FileMamoryService fileMamoryService;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oxygen_stats);

        fileMamoryService = new FileMamoryService(getApplicationContext());

        //deserialize device object.
        deviceName = getIntent().getStringExtra(DEVICE_NAME_KEY);

        //stats toolbar setup.
        toolbar = findViewById(R.id.statsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = findViewById(R.id.stats_toolbar_title);
        toolbarTitle.setText(deviceName);
        //mOxyValue=findViewById(R.id.oxValue);

        mDevicePerformer.addPerformerListener(mPerformerListener);
        mDevicePerformer.cmd_requestWristbandConfig();
        mDevicePerformer.openHealthyRealTimeData(IDevicePerformer.HEALTHY_TYPE_OXYGEN);

        readButton=(Button) findViewById(R.id.read_button);
        readButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(OxygenRead.getIstanceIntent(getApplicationContext(), deviceName));
            }
        });
        chart = findViewById(R.id.weightChart);

        weekScroll = findViewById(R.id.horizontalScrollWeekOx);
        weekScroll.setVisibility(View.INVISIBLE);
        monthScroll = findViewById(R.id.horizontalScrollMonthOx);
        monthScroll.setVisibility(View.INVISIBLE);

       lun = findViewById(R.id.LunOx);
        lun.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(OxygenWeek.containsKey(Calendar.MONDAY)){
                    chart.setCenterText(String.valueOf(OxygenWeek.get(Calendar.MONDAY))+"\n%");
                    changeVAlues(OxygenWeek.get(Calendar.MONDAY));
                }else{
                    chart.setCenterText(String.valueOf(0+"\n%"));
                    changeVAlues(0);
                }

            }
        });
       mart=findViewById(R.id.MartOx);
        mart.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(OxygenWeek.containsKey(Calendar.TUESDAY)){
                    chart.setCenterText(String.valueOf(OxygenWeek.get(Calendar.TUESDAY))+"\n%");
                    changeVAlues(OxygenWeek.get(Calendar.TUESDAY));
                }else{
                    chart.setCenterText(String.valueOf(0+"\n%"));
                    changeVAlues(0);
                }
            }
        });
       mer = findViewById(R.id.MerOx);
        mer.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(OxygenWeek.containsKey(Calendar.WEDNESDAY)){
                    chart.setCenterText(String.valueOf(OxygenWeek.get(Calendar.WEDNESDAY))+"\n%");
                    changeVAlues(OxygenWeek.get(Calendar.WEDNESDAY));
                }else{
                    chart.setCenterText(String.valueOf(0+"\n%"));
                    changeVAlues(0);
                }
            }
        });
       gio =findViewById(R.id.GioOx);
        gio.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(OxygenWeek.containsKey(Calendar.THURSDAY)){
                    chart.setCenterText(String.valueOf(OxygenWeek.get(Calendar.THURSDAY))+"\n%");
                    changeVAlues(OxygenWeek.get(Calendar.THURSDAY));
                }else{
                    chart.setCenterText(String.valueOf(0+"\n%"));
                    changeVAlues(0);
                }
            }
        });
       ven =findViewById(R.id.VenOx);
        ven.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(OxygenWeek.containsKey(Calendar.FRIDAY)){
                    chart.setCenterText(String.valueOf(OxygenWeek.get(Calendar.FRIDAY))+"\n%");
                    changeVAlues(OxygenWeek.get(Calendar.FRIDAY));
                }else{
                    chart.setCenterText(String.valueOf(0+"\n%"));
                    changeVAlues(0);
                }
            }
        });
       sab =findViewById(R.id.SabOx);
        sab.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(OxygenWeek.containsKey(Calendar.SATURDAY)){
                    chart.setCenterText(String.valueOf(OxygenWeek.get(Calendar.SATURDAY))+"\n%");
                    changeVAlues(OxygenWeek.get(Calendar.SATURDAY));
                }else{
                    chart.setCenterText(String.valueOf(0+"\n%"));
                    changeVAlues(0);
                }
            }
        });
       dom =findViewById(R.id.DomOx);
        dom.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(OxygenWeek.containsKey(Calendar.SUNDAY)){
                    chart.setCenterText(String.valueOf(OxygenWeek.get(Calendar.SUNDAY))+"\n%");
                    changeVAlues(OxygenWeek.get(Calendar.SUNDAY));
                }else{
                    chart.setCenterText(String.valueOf(0+"\n%"));
                    changeVAlues(0);
                }            }
        });

        List<SaveDataBean> listSaveDataBeanOxygen = new ArrayList<SaveDataBean>();
        try {
            listSaveDataBeanOxygen = fileMamoryService.getOxygenDayData(Calendar.getInstance().getTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(listSaveDataBeanOxygen!=null && listSaveDataBeanOxygen.size()!=0){
            lastValue= (int) Float.parseFloat(listSaveDataBeanOxygen.get(listSaveDataBeanOxygen.size()-1).getValue());
            lastValue=Math.round(lastValue);
        }else lastValue = 0;
        initLastValuesOxygen();

        paintPieChart(lastValue);


        weeksDataList=findViewById(R.id.listWeekData);

        WeekDataAdapter weekDataAdapter=new WeekDataAdapter(getApplicationContext(),getWeeks());
        weeksDataList.setAdapter(weekDataAdapter);
        getWeekBarValue();
        TabLayout tabLayout = findViewById(R.id.tabOxygen);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //do stuff here

                switch (tab.getPosition()){
                    case 0:
                        Toast.makeText(getApplicationContext(), "today", Toast.LENGTH_LONG).show();
                        changeToToday();
                        weekScroll.setVisibility(View.INVISIBLE);
                        monthScroll.setVisibility(View.INVISIBLE);
                        chart.setCenterText(String.valueOf(lastValue)+" %");
                        changeVAlues(lastValue);
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "week", Toast.LENGTH_LONG).show();
                        changeToWeek();
                        weekScroll.setVisibility(View.VISIBLE);
                        monthScroll.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "month", Toast.LENGTH_LONG).show();
                        changeToMonth();
                        weekScroll.setVisibility(View.INVISIBLE);
                        monthScroll.setVisibility(View.VISIBLE);
                        break;

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


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

    public static Intent getInstanceIntent(Context context, String deviceName){
        Intent intent = new Intent(context, OxygenStatsActivity.class);
        intent.putExtra(DEVICE_NAME_KEY, deviceName);
        return intent;
    }

    private SimplePerformerListener mPerformerListener = new SimplePerformerListener() {
        @Override
        public void onResponseWristbandConfig(WristbandConfig config) {
            WristbandVersion version = config.getWristbandVersion();
            if (version.isOxygenEnable()) {
                //mOxyValue.setVisibility(View.VISIBLE);
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
            //if (oxygen != 0 ) {
                //mOxyValue.setText(String.valueOf(oxygen)+ "%");
                //chart.setCenterText(String.valueOf(oxygen)+"\n%");
                //changeVAlues(oxygen);
            //}

        }

    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDevicePerformer.removePerformerListener(mPerformerListener);
        mDevicePerformer.closeHealthyRealTimeData(IDevicePerformer.HEALTHY_TYPE_OXYGEN);
    }



    public void paintPieChart(float val )
    {

        int[] colors = new int[] {Color.argb(255,255,255,255), Color.TRANSPARENT};
        entries.add(new PieEntry(val));

        //Log.d("percentuale ","val "+val);
        float other = 100-val;
        //Log.d("percentuale ","other "+other);
        entries.add(new PieEntry(other));

        PieDataSet set = new PieDataSet(entries,null);
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

        chart.setTransparentCircleRadius(94);
        chart.setTransparentCircleColor(getResources().getColor(R.color.light));
        chart.getDescription().setEnabled(false);
        chart.setRotationEnabled(false);
        chart.setCenterText(value+"\n%");
        chart.setCenterTextSize(20);
        chart.setCenterTextColor(Color.WHITE);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setEntryLabelColor(Color.TRANSPARENT);
        chart.setDrawEntryLabels(false);
        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(90.0f);
        chart.setHoleColor(Color.TRANSPARENT);
        chart.setData(data);
        //chart.spin( 500,0,360 - angleTo, Easing.EasingOption.EaseInOutQuad ); // rotazione del grafico
        chart.animateY(1000);
        chart.notifyDataSetChanged(); // let the chart know it's data changed

        //PieChartRenderer custom= new PieChartRenderer(chart, chart.getAnimator(),chart.getViewPortHandler());
        //chart.setRenderer(custom);
        chart.invalidate(); // refresh

    }

    private void changeVAlues(float val){
        entries.get(0).setY(val);
        entries.get(1).setY(100-val);
        chart.animateY(1000);
        chart.notifyDataSetChanged();
        chart.invalidate(); // refresh
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<WeekData> getWeeks(){
        //TODO get from storage


        List<WeekData> weeks=new ArrayList<WeekData>();
        FileMamoryService fileMamoryService=new FileMamoryService(getApplicationContext());
        try {
            weeks=fileMamoryService.getAllWeekOxygenData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weeks;


    }

    private void initLastValuesOxygen(){
        List<SaveDataBean> listSaveDataBeanOxygen = new ArrayList<SaveDataBean>();
        try {
            listSaveDataBeanOxygen = fileMamoryService.getOxygenDayData(Calendar.getInstance().getTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(listSaveDataBeanOxygen!=null && listSaveDataBeanOxygen.size()!=0){
            lastValue= (int) Float.parseFloat(listSaveDataBeanOxygen.get(listSaveDataBeanOxygen.size()-1).getValue());
        }else lastValue = 0;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();

        initLastValuesOxygen();

        WeekDataAdapter weekDataAdapter=new WeekDataAdapter(getApplicationContext(),getWeeks());
        weeksDataList.setAdapter(weekDataAdapter);

        if (lastValue != 0 ) {
            chart.setCenterText(String.valueOf(lastValue)+" %");
            changeVAlues(lastValue);
        }

    }

    private void changeToToday(){
        //TODO
    }
    private void changeToWeek(){
        //TODO
    }
    private void changeToMonth(){
        //TODO
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getWeekDayGraph(int day)
    {

        int value=0;
        Calendar c = Calendar.getInstance();

        String dayLongName = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        List<WeekData> listweekDataOxygen = getWeeks();
        if(listweekDataOxygen!=null && listweekDataOxygen.size()!=0){
          for(WeekData wd : listweekDataOxygen)
            {
                if(wd.getWeek().equals("Questa settimana"))
                {
                    for(Object days : wd.getDays())
                    {
                        OxygenDayData oxygenDayData=(OxygenDayData)days;

                        if(oxygenDayData.getDay().equals(dayLongName)) {
                            String valuePerc=oxygenDayData.getData(); //valore
                            valuePerc=valuePerc.replaceAll("%","");
                            value= Integer.parseInt(valuePerc);
                        }
                    }
                 }
            }
        }

        paintPieChart(value);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getWeekBarValue(){

        List<WeekData> weeks=new ArrayList<>();
        try {
            weeks=fileMamoryService.getAllWeekOxygenData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<Integer,Float> values=new HashMap<>();
        if(weeks!=null && weeks.size()>0){
            if(weeks.get(0).getWeek()=="Questa settimana"){
                List<OxygenDayData> days=weeks.get(0).getDays();
                for(OxygenDayData day : days){
                    Calendar c=Calendar.getInstance();
                    c.setTime(day.getDate());
                    int dayNumber=c.get(Calendar.DAY_OF_WEEK);
                    values.put(dayNumber,Float.valueOf(day.getData()));
                }
            }
        }
        OxygenWeek=values;
    }


}
