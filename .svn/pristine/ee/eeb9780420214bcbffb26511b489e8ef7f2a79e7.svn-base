package com.neperiagroup.happysalus;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.htsmart.wristband.WristbandApplication;
import com.htsmart.wristband.bean.TodayTotalData;
import com.htsmart.wristband.bean.WristbandConfig;
import com.htsmart.wristband.bean.WristbandVersion;
import com.htsmart.wristband.performer.IDevicePerformer;
import com.htsmart.wristband.performer.SimplePerformerListener;
import com.neperiagroup.happysalus.Renderers.HorizontalBarChartRenderer;
import com.neperiagroup.happysalus.Renderers.HorizontalBarChartRendererVerticalLines;
import com.neperiagroup.happysalus.Renderers.VericalBarChartRenderer;
import com.neperiagroup.happysalus.Renderers.VerticalBarChartRendererStar;
import com.neperiagroup.happysalus.adapters.WeekDataAdapter;
import com.neperiagroup.happysalus.bean.DataDevice;
import com.neperiagroup.happysalus.bean.GenericDayData;
import com.neperiagroup.happysalus.bean.OxygenDayData;
import com.neperiagroup.happysalus.bean.SaveDataBean;
import com.neperiagroup.happysalus.bean.StepDayData;
import com.neperiagroup.happysalus.bean.WeekData;
import com.neperiagroup.happysalus.services.FileMamoryService;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class StepsStatsActivity extends AppCompatActivity{
    private static final String TAG = StepsStatsActivity.class.getName();
    private static final String DEVICE_DATA_KEY = TAG + ".device_data";
    private static final String DEVICE_NAME_KEY = TAG + ".device_name";
    private final String FILE_STEP = "file_step";
    public static final String currentMonth="current_month";
    private final String FILE_CALORIE="file_calorie";
    private final String FILE_DISTANZE="file_distance";

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private TextView stepsText;
    private TextView calText;
    private TextView distanceText;
    private TextView stepsStaicText;

    private String deviceName;
    private DataDevice deviceData;

    private ListView weeksDataList;

    private String[] colors = new String[] {"#FFFFFF"};
    private List<BarEntry> entriesStepToday;
    private List<SaveDataBean> stepsToday;
    private List<SaveDataBean> caloriesToday;
    private List<SaveDataBean> distanceToday;

    private FileMamoryService fileMamoryService;

    private BarChart barChart;
    private BarChart stepTodayChart;
    private BarChart stepWeekChart;

    private int currentSteps=0;

    private List<Integer > stepValuesByHour;
    private Map<Integer,Integer> barWeek;
    private TextView dayDate;
    private String distanceTodayValue;
    private String calorieTodayValue;

    private IDevicePerformer mDevicePerformer = WristbandApplication.getDevicePerformer();
    Button readButton;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_stats);
        fileMamoryService=new FileMamoryService(getApplicationContext());
        stepsText=findViewById(R.id.steps);
        stepsStaicText=findViewById(R.id.StepsStaticString);
        calText=findViewById(R.id.calories);
        distanceText=findViewById(R.id.distance);
        dayDate=findViewById(R.id.dayDate);
        //deserialize device object.
        deviceData = (DataDevice) getIntent().getSerializableExtra(DEVICE_DATA_KEY);
        deviceName = getIntent().getStringExtra(DEVICE_NAME_KEY);
        Date today=new Date();
        try {
            stepsToday=fileMamoryService.getStepDayData(today);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            caloriesToday=fileMamoryService.getCalorieDayData(today);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            distanceToday=fileMamoryService.getDistanceDayData(today);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(stepsToday!=null && stepsToday.size()>0){
            currentSteps=Integer.valueOf(stepsToday.get(stepsToday.size()-1).getValue());
            stepsText.setText(stepsToday.get(stepsToday.size()-1).getValue());
        }else{
            stepsText.setText("0");
        }
        if(caloriesToday!=null && caloriesToday.size()>0){
            double caloriesD=Double.valueOf(caloriesToday.get(caloriesToday.size()-1).getValue())/1000;
            String cal=String.valueOf(caloriesD);
            if(cal.length()>5){
                cal=cal.substring(0,5);
                if(cal.endsWith(",")||cal.endsWith(".")){
                    cal=cal.substring(0,4);
                }
            }
            calorieTodayValue=cal;
            calText.setText(cal);
        }else{
            calText.setText("0");
        }
        if(distanceToday!=null && distanceToday.size()>0){

            double distanceD=Double.valueOf(distanceToday.get(distanceToday.size()-1).getValue())/1000;
            String dis=String.valueOf(distanceD);
            if(dis.length()>5){
                dis=dis.substring(0,5);
                if(dis.endsWith(",")||dis.endsWith(".")){
                    dis=dis.substring(0,4);
                }
            }
            distanceTodayValue=dis;
            distanceText.setText(dis);
        }else{
            distanceText.setText("0");
        }
        mDevicePerformer.addPerformerListener(mPerformerListener);
        readButton=(Button) findViewById(R.id.read_button);
        readButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Lettura in corso...", Toast.LENGTH_SHORT).show();

                mDevicePerformer.syncData();
            }
        });



        //stats toolbar setup.
        toolbar = findViewById(R.id.statsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = findViewById(R.id.stats_toolbar_title);
        toolbarTitle.setText(deviceName);
        weeksDataList=findViewById(R.id.listWeekData);

        barChart=findViewById(R.id.BarChart);
        gethorizontalBarChart(currentSteps, 7000);

        stepTodayChart=findViewById(R.id.stepChartToday);
        stepWeekChart=findViewById(R.id.stepChartWeek);
        //
        getWeekBarValue();
        drawStepToday();
        drawWeekChart();
        stepWeekChart.setVisibility(View.INVISIBLE);
        WeekDataAdapter weekDataAdapter=new WeekDataAdapter(getApplicationContext(),getWeeks());
        weeksDataList.setAdapter(weekDataAdapter);

        TabLayout tabLayout = findViewById(R.id.tabDays);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //do stuff here

                switch (tab.getPosition()){
                    case 0:
                        Toast.makeText(getApplicationContext(), "today", Toast.LENGTH_LONG).show();
                        stepTodayChart.setVisibility(View.VISIBLE);
                        stepWeekChart.setVisibility(View.INVISIBLE);
                        stepsStaicText.setVisibility(View.VISIBLE);
                        barChart.setVisibility(View.VISIBLE);
                        stepsText.setVisibility(View.VISIBLE);
                        calText.setText(calorieTodayValue);
                        distanceText.setText(distanceTodayValue);
                        dayDate.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "week", Toast.LENGTH_LONG).show();
                        stepTodayChart.setVisibility(View.INVISIBLE);
                        stepWeekChart.setVisibility(View.VISIBLE);
                        stepsStaicText.setVisibility(View.INVISIBLE);
                        barChart.setVisibility(View.INVISIBLE);
                        stepsText.setVisibility(View.INVISIBLE);
                        dayDate.setVisibility(View.VISIBLE);
                        //drawWeekChart();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "month", Toast.LENGTH_LONG).show();
                        stepTodayChart.setVisibility(View.INVISIBLE);
                        stepWeekChart.setVisibility(View.INVISIBLE);
                        stepsStaicText.setVisibility(View.INVISIBLE);
                        barChart.setVisibility(View.INVISIBLE);
                        stepsText.setVisibility(View.INVISIBLE);
                        dayDate.setVisibility(View.INVISIBLE);
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
//        switch(item.getItemId()){
//            case R.id.settings:
//                startActivity(SettingsActivity.getInstanceIntent(getApplicationContext()));
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getInstanceIntent(Context context, DataDevice deviceData, String deviceName){
        Intent intent = new Intent(context, StepsStatsActivity.class);
        intent.putExtra(DEVICE_DATA_KEY, deviceData);
        intent.putExtra(DEVICE_NAME_KEY, deviceName);
        return intent;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<WeekData> getWeeks() {

        List<WeekData> weeks=new ArrayList<>();
        try {
            weeks=fileMamoryService.getAllWeekStepData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weeks;

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
    public void gethorizontalBarChart(int val,int bound)
    {
        ArrayList<BarEntry> yVals =  new ArrayList<>();
        yVals.add(new BarEntry(1f,val)); //index,value)

        BarDataSet set1= new BarDataSet(yVals, "");
        set1.setHighLightColor(Color.WHITE);
        set1.setColor(Color.WHITE);
        set1.setDrawValues(false);
        BarData data = new BarData(set1);
        data.setBarWidth(1f);

        YAxis yl = barChart.getAxisLeft();
        yl.setAxisMinimum(0f);
        yl.setAxisMaximum(bound);
        yl.setTextColor(Color.TRANSPARENT);
        yl.setEnabled(false);

        YAxis yr = barChart.getAxisRight();
        yr.setAxisMinimum(0f);
        yr.setAxisMaximum(bound);
        yr.setEnabled(false);
        // int[] rootLocation = new int[2];
        barChart.setData(data);
        barChart.setScaleEnabled(false);
        //chart.getLocationOnScreen(rootLocation);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getXAxis().setEnabled(false);
        barChart.getXAxis().setCenterAxisLabels(true);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMinimum(1);
        barChart.setDrawBarShadow(true);
        barChart.getLegend().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();

        //Bitmap pointBitmap  = BitmapFactory.decodeResource(getResources(), drawable.circle_shape);

        Bitmap bit0= getBitmap(R.drawable.bar_shape);
        Bitmap bit10=getBitmap(R.drawable.bar_shape);
        Bitmap bit20= getBitmap(R.drawable.bar_shape);
        Bitmap bit30=getBitmap(R.drawable.bar_shape);
        Bitmap bit40= getBitmap(R.drawable.bar_shape);
        Bitmap bit50=getBitmap(R.drawable.circle_shape);
        Bitmap [] points ={bit0,bit10,bit20,bit30,bit40,bit50};

        XAxis xAxis=barChart.getXAxis();
        xAxis.setAxisMinimum(0);
        YAxis yAxis=barChart.getAxisLeft();
        yAxis.setAxisMinimum(0);
        HorizontalBarChartRendererVerticalLines barChartCustomRenderer = new HorizontalBarChartRendererVerticalLines(barChart,barChart.getAnimator(),barChart.getViewPortHandler(),points);
        barChart.setRenderer(barChartCustomRenderer);


        barChart.invalidate();
    }

    private int[] getColors() {
        int[] values = new int[colors.length];

        for (int i = 0; i < colors.length; i++) {
            values[i] = Color.parseColor(colors[i]);
        }

        return values;
    }

    private void drawStepToday(){
        // TODO mapping in x axis from number to string;
        final HashMap<Integer, String> numMap = new HashMap<>();
        // TODO parameter x axis & y
        Random rand = new Random();
        int axisX = 24;
        setEntriesToday();
        entriesStepToday = new ArrayList<>();

        for (int i=1; i<=axisX; i++) {
            float randomNumber = rand.nextFloat() * (130 - 50) +50;

            if (i==1) {
                numMap.put(i, "00:00"); // la mappatura avviene qui;
            }
            if (i>1 && i<24) {
                numMap.put(i, ""); // la mappatura avviene qui;
            }
            if (i==24){
                numMap.put(i, "24:00"); // la mappatura avviene qui;
            }
            entriesStepToday.add(new BarEntry(i,stepValuesByHour.get(i-1)));
            //entriesStepToday.add(new BarEntry(i, randomNumber)); // setta qui i valori reali;

        }


         BarDataSet set1;

        if (stepTodayChart.getData() != null && stepTodayChart.getData().getDataSetCount() > 0)
        {
            set1 = (BarDataSet) stepTodayChart.getData().getDataSetByIndex(0);
            set1.setValues(entriesStepToday);
            //set1.setHighLightAlpha(0); // To remove background color use setHighLightAlpha(0);

            set1.setHighlightEnabled(true);

            set1.setDrawValues(false);
            stepTodayChart.getData().notifyDataChanged();
            stepTodayChart.notifyDataSetChanged();
        }
        else
        {
            set1 = new BarDataSet(entriesStepToday, "");
            //set1.setHighLightAlpha(0); // To remove background color use setHighLightAlpha(0);

            set1.setDrawIcons(false);
            set1.setDrawValues(false);
            set1.setColors(getColors());
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();

            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setBarWidth(0.3f);

            stepTodayChart.setData(data);
        }



        // TODO disable zoom
        stepTodayChart.setScaleEnabled(false);

        // TODO disable description label
        stepTodayChart.getDescription().setEnabled(false);

        // TODO this animationX
        stepTodayChart.animateX(1000);

        // TODO Settings generics
        stepTodayChart.setFitBars(true);

        stepTodayChart.getAxisLeft().setDrawGridLines(false); // disattiva la griglia di sinistra.
        stepTodayChart.getXAxis().setDrawGridLines(false);
        stepTodayChart.getAxisRight().setDrawGridLines(false); // disattiva la griglia di destra.
        stepTodayChart.setDrawValueAboveBar(false); //imposta i valori sotto la punta max di ogni barra
        stepTodayChart.setDrawBarShadow(false); // setta l'ombra su tutta la barra..
        stepTodayChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        // TODO color parameter on the left
        stepTodayChart.getAxisLeft().setTextColor(Color.WHITE);
        stepTodayChart.getAxisRight().setDrawAxisLine(false);
        stepTodayChart.getAxisLeft().setDrawGridLines(false); // grid line left
        stepTodayChart.getAxisLeft().setAxisLineColor(Color.WHITE); // colore linea Y
        stepTodayChart.getAxisRight().setEnabled(false);
        stepTodayChart.getXAxis().setAxisLineColor(Color.WHITE);

        // TODO remove axis Y
        YAxis leftAxis = stepTodayChart.getAxisLeft(); // remove axis
        leftAxis.setAxisMinimum(0);
        leftAxis.setEnabled(false);


        //Todo this Mapping axis x
        final XAxis xAxis = stepTodayChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setLabelCount(24);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return numMap.get((int)value);
            }
        });

        Legend legend = stepTodayChart.getLegend();
        legend.setEnabled(false); // hide legend

        VericalBarChartRenderer customRenderer = new VericalBarChartRenderer(stepTodayChart, stepTodayChart.getAnimator(), stepTodayChart.getViewPortHandler());
        stepTodayChart.setRenderer(customRenderer);


        stepTodayChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(final Entry entry, Highlight highlight) {

                final float x=entry.getX();
                final float y=entry.getY();
                System.out.println("x: "+x);
                System.out.println("y: "+y);

                Highlight h1 = new Highlight(x,0,0);
                //set1.setHighLightColor(Color.YELLOW); // change color selection barchar -> change Color.TRASPARENT
                //set1.setHighLightAlpha(0);



                stepTodayChart.highlightValues(new Highlight[]{h1});
                highlight.getDataIndex();

            }

            @Override
            public void onNothingSelected() {

            }
        });



        stepTodayChart.invalidate();


    }

    private void setEntriesToday() {
        try {
            stepsToday=fileMamoryService.getStepDayData(new Date());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"lettura dati oggi completata");

        Map<Integer,List<SaveDataBean>> dataIntervals=new HashMap<Integer,List<SaveDataBean>>();
        for(int i =0;i<24;i++){
            List lista=new ArrayList<SaveDataBean>();
            dataIntervals.put(i,lista);
        }
        for(SaveDataBean data:stepsToday){
            Calendar c=Calendar.getInstance();
            c.setTime(data.getDate());
            int h=c.get(Calendar.HOUR_OF_DAY);
            dataIntervals.get(h).add(data);
        }
        Set<Integer> keys=dataIntervals.keySet();
        List<Integer> values=new ArrayList();
        int stepToNow=0;
        for(int i =0;i<24;i++){
            List<SaveDataBean> dataByH=dataIntervals.get(i);
            int valueH=0;
            for (SaveDataBean data:dataByH){
                if(Integer.valueOf(data.getValue())>valueH){
                    valueH=Integer.valueOf(data.getValue());
                }
            }

            values.add(valueH);
        }
        List<Integer> interval=new ArrayList<Integer>();
        for(int value:values){
            if(value==0){
                interval.add(value);
            }
            else if(stepToNow==0){
                stepToNow=value;
                interval.add(value);
            }else{
                interval.add(value-stepToNow);
                stepToNow+=value;
            }
        }
        stepValuesByHour=interval;


    }

    private void drawWeekChart(){
        List<BarEntry> entries = new ArrayList<>();
        int value;
        if(barWeek.containsKey(Calendar.MONDAY)){
            value=barWeek.get(Calendar.MONDAY);
        }else{
            value=0;
        }
        entries.add(new BarEntry(0f, value));
        if(barWeek.containsKey(Calendar.TUESDAY)){
            value=barWeek.get(Calendar.TUESDAY);
        }else{
            value=0;
        }
        entries.add(new BarEntry(1f, value));
        if(barWeek.containsKey(Calendar.WEDNESDAY)){
            value=barWeek.get(Calendar.WEDNESDAY);
        }else{
            value=0;
        }
        entries.add(new BarEntry(2f, value));
        if(barWeek.containsKey(Calendar.THURSDAY)){
            value=barWeek.get(Calendar.THURSDAY);
        }else{
            value=0;
        }
        entries.add(new BarEntry(3f, value));
        // gap of 2f
        if(barWeek.containsKey(Calendar.FRIDAY)){
            value=barWeek.get(Calendar.FRIDAY);
        }else{
            value=0;
        }
        entries.add(new BarEntry(4f, value));
        if(barWeek.containsKey(Calendar.SATURDAY)){
            value=barWeek.get(Calendar.SATURDAY);
        }else{
            value=0;
        }
        entries.add(new BarEntry(5f, value));
        if(barWeek.containsKey(Calendar.SUNDAY)){
            value=barWeek.get(Calendar.SUNDAY);
        }else{
            value=0;
        }
        entries.add(new BarEntry(6f, value));
        BarDataSet set = new BarDataSet(entries, "");
        set.setColor(Color.WHITE,1);
        set.setBarBorderColor(Color.WHITE);
        set.setBarBorderWidth(0.5f);

        set.setHighLightColor(Color.WHITE);
        set.setHighLightAlpha(255); //full opaque
        set.setDrawValues(false);
        BarData data = new BarData(set);

        // TODO mapping in x axis from number to string;
        final HashMap<Integer, String> numMap = new HashMap<>();
        numMap.put(0, "Lun");
        numMap.put(1, "Mar");
        numMap.put(2, "Mer");
        numMap.put(3, "Gio");
        numMap.put(4, "Ven");
        numMap.put(5, "Sab");
        numMap.put(6, "Dom");

        //Todo this

        XAxis xAxis = stepWeekChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return numMap.get((int)value);
            }
        });
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxis = stepWeekChart.getAxisLeft();
        yAxis.setAxisMinimum(0f); // start at zero

        data.setBarWidth(0.8f); // set custom bar width
        stepWeekChart.setData(data);
        stepWeekChart.setFitBars(true); // make the x-axis fit exactly all bars
        stepWeekChart.setScaleXEnabled(false);
        stepWeekChart.getAxisLeft().setDrawGridLines(false);
        stepWeekChart.getXAxis().setDrawGridLines(false);
        stepWeekChart.getAxisRight().setDrawGridLines(false);
        stepWeekChart.getAxisLeft().setDrawAxisLine(false);
        stepWeekChart.getAxisRight().setDrawAxisLine(false);
        stepWeekChart.getXAxis().setDrawAxisLine(false);
        stepWeekChart.getXAxis().setEnabled(true);
        stepWeekChart.getAxisLeft().setEnabled(false);
        stepWeekChart.getAxisRight().setEnabled(false);
        stepWeekChart.getDescription().setEnabled(false);
        stepWeekChart.setScaleEnabled(false);
        stepWeekChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float index=e.getX();
                index+=2;
                if(index==8){index=1;}
                Calendar c=Calendar.getInstance();
                int year=c.get(Calendar.YEAR);
                int week=c.get(Calendar.WEEK_OF_YEAR);
                c.setWeekDate(year,week,(int)index);
                SimpleDateFormat dt1 = new SimpleDateFormat("EEEE d MMMM");
                dayDate.setText(dt1.format(c.getTime()));
                List<SaveDataBean> caloriesDay=new ArrayList<>();
                List<SaveDataBean> distanceDay=new ArrayList<>();
                try {
                    caloriesDay=fileMamoryService.getCalorieDayData(c.getTime());
                } catch (IOException err) {
                    err.printStackTrace();
                }
                try {
                    distanceDay=fileMamoryService.getDistanceDayData(c.getTime());
                } catch (IOException err) {
                    err.printStackTrace();
                }
                if(caloriesDay!=null && caloriesDay.size()>0){
                    double caloriesD=Double.valueOf(caloriesDay.get(caloriesDay.size()-1).getValue())/1000;
                    String cal=String.valueOf(caloriesD);
                    if(cal.length()>5){
                        cal=cal.substring(0,5);
                        if(cal.endsWith(",")||cal.endsWith(".")){
                            cal=cal.substring(0,4);
                        }
                    }
                    calText.setText(cal);
                }else{
                    calText.setText("0");
                }
                if(distanceDay!=null && distanceDay.size()>0){

                    double distanceD=Double.valueOf(distanceDay.get(distanceDay.size()-1).getValue())/1000;
                    String dis=String.valueOf(distanceD);
                    if(dis.length()>5){
                        dis=dis.substring(0,5);
                        if(dis.endsWith(",")||dis.endsWith(".")){
                            dis=dis.substring(0,4);
                        }
                    }
                    distanceText.setText(dis);
                }else{
                    distanceText.setText("0");
                }


            }

            @Override
            public void onNothingSelected() {

            }
        });
        //stepWeekChart.setDrawValueAboveBar(true);



        Bitmap starBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.star_step);
        VerticalBarChartRendererStar customRenderer = new VerticalBarChartRendererStar(stepWeekChart, stepWeekChart.getAnimator(), stepWeekChart.getViewPortHandler(), starBitmap);

        stepWeekChart.setRenderer(customRenderer);

        stepTodayChart.invalidate(); // refresh


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getWeekBarValue(){

        List<WeekData> weeks=new ArrayList<>();
        try {
            weeks=fileMamoryService.getAllWeekStepData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<Integer,Integer> values=new HashMap<>();
        if(weeks!=null && weeks.size()>0){
            if(weeks.get(0).getWeek()=="Questa settimana"){
                List<StepDayData> days=weeks.get(0).getDays();
                for(StepDayData day : days){
                    Calendar c=Calendar.getInstance();
                    c.setTime(day.getDate());
                    int dayNumber=c.get(Calendar.DAY_OF_WEEK);
                    values.put(dayNumber,Integer.valueOf(day.getData()));
                }
            }
        }
        barWeek=values;
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
        public void onSyncDataTodayTotalData(TodayTotalData data) {
            Log.e(TAG, "------------------" );
            Log.e(TAG, "steps "+ String.valueOf(data.getSteps()) );
            Log.e(TAG, "------------------" );

            Calendar today = Calendar.getInstance();
            String date = "" + today.get(Calendar.DAY_OF_MONTH) + today.get(Calendar.MONTH);
            Type listTypeDaySaveDataBean = new TypeToken<ArrayList<SaveDataBean>>() {
            }.getType();

            try {
                String json = fileMamoryService.readFromFile(getApplicationContext().getFilesDir() + "/"+currentMonth+"/"+FILE_STEP + "_" + date + ".txt");
                if (!json.equals("")) {
                    stepsToday = new Gson().fromJson(json, listTypeDaySaveDataBean);
                } else {
                    stepsToday = new ArrayList<>();
                }
            } catch (IOException e) {
                e.printStackTrace();
                stepsToday=null;
            }
            SaveDataBean toSave=new SaveDataBean();
            toSave.setDate(new Date());
            toSave.setValue(""+data.getSteps());
            stepsToday.add(toSave);

            String json="";
            Gson gson=new Gson();
            if(stepsToday!=null){
                json = gson.toJson(stepsToday);
                Log.i("---string---stepsToday", json);
                try {
                    fileMamoryService.writeFile(FILE_STEP + "_" + date + ".txt", json);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Errore", e.getMessage());
                }
                stepsText.setText(stepsToday.get(stepsToday.size()-1).getValue());
            }

            try {
                json = fileMamoryService.readFromFile(getApplicationContext().getFilesDir() + "/"+currentMonth+"/"+FILE_CALORIE + "_" + date + ".txt");
                if (!json.equals("")) {
                    caloriesToday = new Gson().fromJson(json, listTypeDaySaveDataBean);
                } else {
                    caloriesToday = new ArrayList<>();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                json = fileMamoryService.readFromFile(getApplicationContext().getFilesDir() + "/"+currentMonth+"/"+FILE_DISTANZE + "_" + date + ".txt");
                if (!json.equals("")) {
                    distanceToday = new Gson().fromJson(json, listTypeDaySaveDataBean);
                } else {
                    distanceToday = new ArrayList<>();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            toSave.setValue(""+data.getCalories());
            caloriesToday.add(toSave);
            toSave.setValue(data.getDistance()+"");
            distanceToday.add(toSave);

            json=gson.toJson(caloriesToday);
            Log.i("---string---calorie", json);
            try{
                fileMamoryService.writeFile(FILE_CALORIE + "_" + date + ".txt", json);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Errore", e.getMessage());
            }
            json=gson.toJson(distanceToday);
            Log.i("---string---distance", json);
            try{
                fileMamoryService.writeFile(FILE_DISTANZE + "_" + date + ".txt", json);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Errore", e.getMessage());
            }

            if(caloriesToday!=null && caloriesToday.size()>0){
                double caloriesD=Double.valueOf(caloriesToday.get(caloriesToday.size()-1).getValue())/1000;
                String cal=String.valueOf(caloriesD);
                if(cal.length()>5){
                    cal=cal.substring(0,5);
                    if(cal.endsWith(",")||cal.endsWith(".")){
                        cal=cal.substring(0,4);
                    }
                }
                calorieTodayValue=cal;
                calText.setText(cal);
            }else{
                calText.setText("0");
            }
            if(distanceToday!=null && distanceToday.size()>0){

                double distanceD=Double.valueOf(distanceToday.get(distanceToday.size()-1).getValue())/1000;
                String dis=String.valueOf(distanceD);
                if(dis.length()>5){
                    dis=dis.substring(0,5);
                    if(dis.endsWith(",")||dis.endsWith(".")){
                        dis=dis.substring(0,4);
                    }
                }
                distanceTodayValue=dis;
                distanceText.setText(dis);
            }else{
                distanceText.setText("0");
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

        }

    };

}