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
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.htsmart.wristband.WristbandApplication;
import com.htsmart.wristband.bean.WristbandConfig;
import com.htsmart.wristband.bean.WristbandVersion;
import com.htsmart.wristband.performer.IDevicePerformer;
import com.htsmart.wristband.performer.SimplePerformerListener;
import com.neperiagroup.happysalus.adapters.WeekDataAdapter;
import com.neperiagroup.happysalus.bean.BreathDayData;
import com.neperiagroup.happysalus.bean.BreatheFreq;
import com.neperiagroup.happysalus.bean.DataDevice;
import com.neperiagroup.happysalus.bean.GenericDayData;
import com.neperiagroup.happysalus.bean.PresDayData;
import com.neperiagroup.happysalus.bean.PressData;
import com.neperiagroup.happysalus.bean.SaveDataBean;
import com.neperiagroup.happysalus.bean.WeekData;
import com.neperiagroup.happysalus.services.FileMamoryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BreatheFreqStatsActivity extends AppCompatActivity{
    private static final String TAG = BreatheFreqStatsActivity.class.getName();
    private static final String DEVICE_DATA_KEY = TAG + ".device_data";
    private static final String DEVICE_NAME_KEY = TAG + ".device_name";

    private Toolbar toolbar;
    private TextView toolbarTitle;

    private String deviceName;
    private DataDevice deviceData;

    private TextView actValue;
    private TextView minValueText;
    private TextView maxValueText;
    private ListView weeksDataList;




    private int lastValue;
    private int minValue;
    private int maxValue;

    PieChart chartActual;
    PieChart chartMax;
    PieChart chartMin;
    List<PieEntry> entriesActual = new ArrayList<>();
    List<PieEntry> entriesMax = new ArrayList<>();
    List<PieEntry> entriesMin = new ArrayList<>();

    Map<Integer,BreatheFreq> breatheFreqMap;

    Button readButton;
    HorizontalScrollView monthScroll;
    HorizontalScrollView weekScroll;

    private FileMamoryService fileMamoryService;

    private IDevicePerformer mDevicePerformer = WristbandApplication.getDevicePerformer();

    private Button lun;
    private Button mart;
    private Button mer;
    private Button gio;
    private Button ven;
    private Button sab;
    private Button dom;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathe_freq);

        fileMamoryService = new FileMamoryService(getApplicationContext());

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
        getWeekBarValue();

        //actValue=findViewById(R.id.actValue);
        //minValueText=findViewById(R.id.minValue);
        //maxValueText=findViewById(R.id.maxValue);

        mDevicePerformer.addPerformerListener(mPerformerListener);
        mDevicePerformer.cmd_requestWristbandConfig();
        mDevicePerformer.openHealthyRealTimeData(IDevicePerformer.HEALTHY_TYPE_RESPIRATORY_RATE);

        weekScroll= findViewById(R.id.horizontalScrollWeekBr);
        weekScroll.setVisibility(View.INVISIBLE);
        monthScroll= findViewById(R.id.horizontalScrollMonthBr);
        monthScroll.setVisibility(View.INVISIBLE);

        lun = findViewById(R.id.Lun);
        lun.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(breatheFreqMap.containsKey(Calendar.MONDAY)){
                    BreatheFreq breatheFreq=breatheFreqMap.get(Calendar.MONDAY);
                    updateAllCharts(breatheFreq.getMinBreatheDay(),breatheFreq.getMaxBreatheDay(),breatheFreq.getAvgBreatheDay());
                }else{
                    updateAllCharts(0,0,0);
                }
            }
        });
        mart=findViewById(R.id.Mart);
        mart.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(breatheFreqMap.containsKey(Calendar.TUESDAY)){
                    BreatheFreq breatheFreq=breatheFreqMap.get(Calendar.TUESDAY);
                    updateAllCharts(breatheFreq.getMinBreatheDay(),breatheFreq.getMaxBreatheDay(),breatheFreq.getAvgBreatheDay());
                }else{
                    updateAllCharts(0,0,0);
                }
            }
        });
        mer = findViewById(R.id.Mer);
        mer.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(breatheFreqMap.containsKey(Calendar.WEDNESDAY)){
                    BreatheFreq breatheFreq=breatheFreqMap.get(Calendar.WEDNESDAY);
                    updateAllCharts(breatheFreq.getMinBreatheDay(),breatheFreq.getMaxBreatheDay(),breatheFreq.getAvgBreatheDay());
                }else{
                    updateAllCharts(0,0,0);
                }
            }
        });
        gio =findViewById(R.id.Gio);
        gio.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(breatheFreqMap.containsKey(Calendar.THURSDAY)){
                    BreatheFreq breatheFreq=breatheFreqMap.get(Calendar.THURSDAY);
                    updateAllCharts(breatheFreq.getMinBreatheDay(),breatheFreq.getMaxBreatheDay(),breatheFreq.getAvgBreatheDay());
                }else{
                    updateAllCharts(0,0,0);
                }
            }
        });
        ven =findViewById(R.id.Ven);
        ven.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(breatheFreqMap.containsKey(Calendar.FRIDAY)){
                    BreatheFreq breatheFreq=breatheFreqMap.get(Calendar.FRIDAY);
                    updateAllCharts(breatheFreq.getMinBreatheDay(),breatheFreq.getMaxBreatheDay(),breatheFreq.getAvgBreatheDay());
                }else{
                    updateAllCharts(0,0,0);
                }
            }
        });
        sab =findViewById(R.id.Sab);
        sab.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(breatheFreqMap.containsKey(Calendar.SATURDAY)){
                    BreatheFreq breatheFreq=breatheFreqMap.get(Calendar.SATURDAY);
                    updateAllCharts(breatheFreq.getMinBreatheDay(),breatheFreq.getMaxBreatheDay(),breatheFreq.getAvgBreatheDay());
                }else{
                    updateAllCharts(0,0,0);
                }            }
        });
        dom =findViewById(R.id.Dom);
        dom.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(breatheFreqMap.containsKey(Calendar.SUNDAY)){
                    BreatheFreq breatheFreq=breatheFreqMap.get(Calendar.SUNDAY);
                    updateAllCharts(breatheFreq.getMinBreatheDay(),breatheFreq.getMaxBreatheDay(),breatheFreq.getAvgBreatheDay());
                }else{
                    updateAllCharts(0,0,0);
                }            }
        });

        initLastValuesBreathe();

        chartActual = findViewById(R.id.currentChart);
        chartMax = findViewById(R.id.maxChart);
        chartMin = findViewById(R.id.minChart);
        paintPieChartActual(lastValue);
        paintPieChartMin(minValue);
        paintPieChartMax(maxValue);

        weeksDataList=findViewById(R.id.listWeekData);

        WeekDataAdapter weekDataAdapter=new WeekDataAdapter(getApplicationContext(),getWeeks());
        weeksDataList.setAdapter(weekDataAdapter);

        readButton=(Button) findViewById(R.id.read_button);
        readButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BreathFreqReadActivity.getIstanceIntent(getApplicationContext(), deviceName));
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabBreathe);
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
                        updateAllCharts(minValue,maxValue,lastValue);
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

    @SuppressLint("LongLogTag")
    private void initLastValuesBreathe(){
        List<SaveDataBean> listSaveDataBeanBreathe = new ArrayList<SaveDataBean>();
        try {
            listSaveDataBeanBreathe = fileMamoryService.getBreathDayData(Calendar.getInstance().getTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(listSaveDataBeanBreathe!=null && listSaveDataBeanBreathe.size()!=0){
            lastValue = (int) Float.parseFloat(listSaveDataBeanBreathe.get(listSaveDataBeanBreathe.size()-1).getValue());//Ultimo valore letto
            minValue=lastValue;
            maxValue=lastValue;
            for(int i=0; i<listSaveDataBeanBreathe.size(); i++){
                lastValue = (int) Float.parseFloat(listSaveDataBeanBreathe.get(i).getValue());
                Log.d("LAST VALUE RESPIRAZIONE----------->", String.valueOf(lastValue));
                if(lastValue<minValue){
                   minValue=lastValue;
                }

                if(lastValue>maxValue){
                    maxValue=lastValue;
                }
            }

        }else{
            minValue = 0;
            maxValue = 0;
            lastValue = 0;
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
        switch(item.getItemId()){
            case R.id.settings:
                startActivity(SettingsActivity.getIstanceIntent(getApplicationContext()));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent getInstanceIntent(Context context, DataDevice deviceData, String deviceName){
        Intent intent = new Intent(context, BreatheFreqStatsActivity.class);
        intent.putExtra(DEVICE_DATA_KEY, deviceData);
        intent.putExtra(DEVICE_NAME_KEY, deviceName);
        return intent;
    }

    private SimplePerformerListener mPerformerListener = new SimplePerformerListener() {
        @Override
        public void onResponseWristbandConfig(WristbandConfig config) {
            WristbandVersion version = config.getWristbandVersion();
            if (version.isBloodPressureEnable()) {

                //actValue.setVisibility(View.VISIBLE);

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
            if (respiratoryRate != 0) {
                // actValue.setText(String.valueOf(respiratoryRate));
                /*entriesActual.get(0).setY(respiratoryRate*2);
                entriesActual.get(1).setY(100-(respiratoryRate*2));
                chartActual.setCenterText(respiratoryRate+"");
                chartActual.notifyDataSetChanged();
                chartActual.invalidate(); // refresh

                if(respiratoryRate<minValue){
                    minValue=respiratoryRate;
                    //minValueText.setText(String.valueOf(respiratoryRate));
                    entriesMin.get(0).setY(respiratoryRate*2);
                    entriesMin.get(1).setY(100-(respiratoryRate*2));
                    chartMin.setCenterText(respiratoryRate+"");
                    chartMin.notifyDataSetChanged();
                    chartMin.invalidate(); // refresh
                }
                if(respiratoryRate>maxValue){
                    maxValue=respiratoryRate;
                    //maxValueText.setText(String.valueOf(respiratoryRate));
                    entriesMax.get(0).setY(respiratoryRate*2);
                    entriesMax.get(1).setY(100-(respiratoryRate*2));
                    chartMax.setCenterText(respiratoryRate+"");
                    chartMax.notifyDataSetChanged();
                    chartMax.invalidate(); // refresh
                }*/

            }


        }


    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDevicePerformer.removePerformerListener(mPerformerListener);
        mDevicePerformer.closeHealthyRealTimeData(IDevicePerformer.HEALTHY_TYPE_RESPIRATORY_RATE);
    }


    public void paintPieChartActual(int val )
        {
            int proportion=2;
            int[] colors = new int[] {Color.argb(255,255,255,255), Color.TRANSPARENT};
            entriesActual.add(new PieEntry(val*proportion));
            //Log.d("percentuale ","val "+val);
            float other = 100-(val*proportion);
            //Log.d("percentuale ","other "+other);
            entriesActual.add(new PieEntry(other));
            PieDataSet set = new PieDataSet(entriesActual,null);
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
        chartActual.setTransparentCircleRadius(94);
        chartActual.setTransparentCircleColor(getResources().getColor(R.color.light));
        chartActual.getDescription().setEnabled(false);
        chartActual.setRotationEnabled(false);
        chartActual.setCenterText(val+"");
        chartActual.setCenterTextSize(20);
        chartActual.setCenterTextColor(Color.WHITE);
        chartActual.getLegend().setEnabled(false);
        chartActual.getDescription().setEnabled(false);
        chartActual.setEntryLabelColor(Color.TRANSPARENT);
        chartActual.setDrawEntryLabels(false);
        chartActual.setDrawHoleEnabled(true);
        chartActual.setHoleRadius(90.0f);
        chartActual.setHoleColor(Color.TRANSPARENT);
        chartActual.setData(data);
        //chart.spin( 500,0,360 - angleTo, Easing.EasingOption.EaseInOutQuad ); // rotazione del grafico
        chartActual.animateY(1000);
        chartActual.notifyDataSetChanged(); // let the chart know it's data changed
        //PieChartRenderer custom= new PieChartRenderer(chart, chart.getAnimator(),chart.getViewPortHandler());
        //chart.setRenderer(custom);
        chartActual.invalidate(); // refresh

    }

    public void paintPieChartMin(int val )
    {
        int proportion=2;
        int[] colors = new int[] {Color.argb(255,255,255,255), Color.TRANSPARENT};
        entriesMin.add(new PieEntry(val*proportion));
        //Log.d("percentuale ","val "+val);
        float other = 100-(val*proportion);
        //Log.d("percentuale ","other "+other);
        entriesMin.add(new PieEntry(other));
        PieDataSet set = new PieDataSet(entriesActual,null);
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
        chartMin.setCenterText(""+val);
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

    public void paintPieChartMax(int val )
    {
        int proportion=2;
        int[] colors = new int[] {Color.argb(255,255,255,255), Color.TRANSPARENT};
        entriesMax.add(new PieEntry(val*proportion));
        //Log.d("percentuale ","val "+val);
        float other = 100-(val*proportion);
        //Log.d("percentuale ","other "+other);
        entriesMax.add(new PieEntry(other));
        PieDataSet set = new PieDataSet(entriesActual,null);
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
        chartMax.setCenterText(""+val);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<WeekData> getWeeks() {
        List<WeekData> weeks=new ArrayList<WeekData>();
        FileMamoryService fileMamoryService=new FileMamoryService(getApplicationContext());
        try {
            weeks=fileMamoryService.getAllWeekBrData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weeks;
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


    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();

        initLastValuesBreathe();

        entriesActual.get(0).setY(lastValue*2);
        entriesActual.get(1).setY(100-(lastValue*2));
        chartActual.setCenterText(lastValue+"");
        chartActual.notifyDataSetChanged();
        chartActual.invalidate(); // refresh

        entriesMin.get(0).setY(minValue*2);
        entriesMin.get(1).setY(100-(minValue*2));
        chartMin.setCenterText(minValue+"");
        chartMin.notifyDataSetChanged();
        chartMin.invalidate(); // refresh

        entriesMax.get(0).setY(maxValue*2);
        entriesMax.get(1).setY(100-(maxValue*2));
        chartMax.setCenterText(maxValue+"");
        chartMax.notifyDataSetChanged();
        chartMax.invalidate();

        WeekDataAdapter weekDataAdapter=new WeekDataAdapter(getApplicationContext(),getWeeks());
        weeksDataList.setAdapter(weekDataAdapter);
    }

    private void updateAllCharts(int min,int max,int avg){
        entriesActual.get(0).setY(avg*2);
        entriesActual.get(1).setY(100-(avg*2));
        chartActual.setCenterText(avg+"");
        chartActual.animateY(1000);
        chartActual.notifyDataSetChanged();
        chartActual.invalidate(); // refresh

        entriesMin.get(0).setY(min*2);
        entriesMin.get(1).setY(100-(min*2));
        chartMin.setCenterText(min+"");
        chartMin.animateY(1000);
        chartMin.notifyDataSetChanged();
        chartMin.invalidate(); // refresh

        entriesMax.get(0).setY(max*2);
        entriesMax.get(1).setY(100-(max*2));
        chartMax.setCenterText(max+"");
        chartMax.animateY(1000);
        chartMax.notifyDataSetChanged();
        chartMax.invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getWeekDayGraph(int day)
    {
        /*int value=0;
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
        paintPieChart(value);*/
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getWeekBarValue(){

        List<WeekData> weeks=new ArrayList<>();
        try {
            weeks=fileMamoryService.getAllWeekBrData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<Integer,BreatheFreq> values=new HashMap<>();
        if(weeks!=null && weeks.size()>0){
            if(weeks.get(0).getWeek()=="Questa settimana"){
                List<BreathDayData> days=weeks.get(0).getDays();
                for(BreathDayData day : days){
                    Calendar c=Calendar.getInstance();
                    c.setTime(day.getDate());
                    int dayNumber=c.get(Calendar.DAY_OF_WEEK);
                    BreatheFreq breatheFreq=new BreatheFreq();
                    breatheFreq.setAvgBreatheDay(Integer.valueOf(day.getData()));
                    breatheFreq.setMaxBreatheDay(Integer.valueOf(day.getMax()));
                    breatheFreq.setMinBreatheDay(Integer.valueOf(day.getMin()));

                    values.put(dayNumber,breatheFreq);
                }
            }
        }
        breatheFreqMap=values;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getWeekValue(){
        /*
        List<WeekData> weeks=new ArrayList<>();
        try {
            weeks=fileMamoryService.getAllWeekBrData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<Integer,BreatheFreq> values = new HashMap<>();
        if(weeks!=null && weeks.size()>0){
            if(weeks.get(0).getWeek()=="Questa settimana"){
                List<StepDayData> days = weeks.get(0).getDays();
                for(StepDayData day : days){
                    Calendar c=Calendar.getInstance();
                    c.setTime(day.getDate());
                    int dayNumber=c.get(Calendar.DAY_OF_WEEK);
                    values.put(dayNumber,Integer.valueOf(day.getData()));
                }
            }
        }
        barWeek=values;
        */
    }
}
