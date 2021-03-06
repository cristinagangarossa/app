package com.neperiagroup.happysalus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.htsmart.wristband.WristbandApplication;
import com.htsmart.wristband.bean.SleepTotalData;
import com.htsmart.wristband.bean.SyncRawData;
import com.htsmart.wristband.bean.TodayTotalData;
import com.htsmart.wristband.bean.WristbandConfig;
import com.htsmart.wristband.bean.WristbandVersion;
import com.htsmart.wristband.performer.IDevicePerformer;
import com.htsmart.wristband.performer.SimplePerformerListener;
import com.neperiagroup.happysalus.Renderers.HorizontalBarChartRendererVerticalLines;
import com.neperiagroup.happysalus.adapters.WeekDataAdapter;
import com.neperiagroup.happysalus.bean.DataDevice;
import com.neperiagroup.happysalus.bean.GenericDayData;
import com.neperiagroup.happysalus.bean.OxygenDayData;
import com.neperiagroup.happysalus.bean.SleepSaveDataBean;
import com.neperiagroup.happysalus.bean.WeekData;
import com.neperiagroup.happysalus.services.FileMamoryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SleepStatsActivity extends AppCompatActivity{
    private static final String TAG = SleepStatsActivity.class.getName();
    private static final String DEVICE_DATA_KEY = TAG + ".device_data";
    private static final String DEVICE_NAME_KEY = TAG + ".device_name";

    private Toolbar toolbar;
    private TextView toolbarTitle;

    private TextView hTotText;
    private TextView mTotText;
    private TextView mLightText;
    private TextView hLightText;
    private TextView hDeepText;
    private TextView mDeepText;

    private String deviceName;
    private DataDevice deviceData;

    List<PieEntry> entries = new ArrayList<>();
    PieChart chart;
    private BarChart barChart;

    Button readButton;

    private IDevicePerformer mDevicePerformer = WristbandApplication.getDevicePerformer();
    private ListView weeksDataList;
    private FileMamoryService fileMamoryService;
    private SleepSaveDataBean todaySleep;
    private int gooalSleep=28800;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_stats);
        hTotText=findViewById(R.id.hTot);
        mTotText=findViewById(R.id.minTot);
        hLightText=findViewById(R.id.lightSleepH);
        mLightText=findViewById(R.id.lightSleepM);
        hDeepText=findViewById(R.id.heavySleapH);
        mDeepText=findViewById(R.id.heavySleapM);

        fileMamoryService=new FileMamoryService(getApplicationContext());
        weeksDataList=findViewById(R.id.listWeekData);
        try {
            todaySleep=fileMamoryService.getSleepDayData(new Date());
        } catch (IOException e) {
            todaySleep=new SleepSaveDataBean();
            e.printStackTrace();
        }

        WeekDataAdapter weekDataAdapter=new WeekDataAdapter(getApplicationContext(),getWeeks());
        weeksDataList.setAdapter(weekDataAdapter);
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
        mDevicePerformer.addPerformerListener(mPerformerListener);

        mDevicePerformer.cmd_requestWristbandConfig();
        mDevicePerformer.syncData();

        chart=findViewById(R.id.SleepChart);
        barChart=findViewById(R.id.BarChart);

        readButton=(Button) findViewById(R.id.read_button);
        readButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDevicePerformer.syncData();

            }
        });
        setTodayValues();
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
        Intent intent = new Intent(context, SleepStatsActivity.class);
        intent.putExtra(DEVICE_DATA_KEY, deviceData);
        intent.putExtra(DEVICE_NAME_KEY, deviceName);
        return intent;
    }

    private SimplePerformerListener mPerformerListener = new SimplePerformerListener() {

        @Override
        public void onSyncDataSleepTotalData(List<SleepTotalData> datas) {
            String dataToString="";
            Log.e(TAG, "------------------" );
            Log.e(TAG,"onSyncDataSleepTotalData" );
            Log.e(TAG, "------------------" );
            for(SleepTotalData data :datas){
                dataToString+="deep sleep "+String.valueOf(data.getDeepSleep());
                dataToString+=" "+(int)(data.getDeepSleep()/60/60);
                dataToString+=" "+(int)(data.getDeepSleep()/60%60);
                dataToString+=" light sleep"+String.valueOf(data.getLightSleep());
                dataToString+=" "+(int)(data.getLightSleep()/60/60);
                dataToString+=" "+(int)(data.getLightSleep()/60%60);
                Date d = new Date(data.getTimeStamp() );



                dataToString+=" date "+d.toString();


            }
            if(datas.size()<=0){
                dataToString="no data to show";

            }
            Log.e(TAG, "------------------" );
            Log.e(TAG, dataToString );
            Log.e(TAG, "------------------" );
        }

        @Override
        public void onSyncDataResult(List<SyncRawData> datas) {

        }

        @Override
        public void onSyncDataTodayTotalData(TodayTotalData data) {

        }



    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDevicePerformer.removePerformerListener(mPerformerListener);
    }

    public void paintPieChart(float val )
    {

        int[] colors = new int[] {Color.argb(255,255,255,255), getResources().getColor(R.color.primary_disabled)};
        entries.add(new PieEntry(val));

        //Log.d("percentuale ","val "+val);
        float other = 100-val;
        //Log.d("percentuale ","other "+other);
        entries.add(new PieEntry(other));

        PieDataSet set = new PieDataSet(entries,null);
        PieData data = new PieData(set);
        set.setColors(colors);
        data.setValueTextColor(Color.TRANSPARENT);


        int value=(int)data.getDataSet().getEntryForIndex(0).getValue();


        //chart.setTransparentCircleRadius(94);
        //chart.setTransparentCircleColor(getResources().getColor(R.color.light));
        //chart.getDescription().setEnabled(false);
        chart.setRotationEnabled(false);
        //chart.setCenterText(value+"\n%");
        //chart.setCenterTextSize(20);
        //chart.setCenterTextColor(Color.WHITE);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        //chart.setEntryLabelColor(Color.TRANSPARENT);
        chart.setDrawEntryLabels(false);
        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(50.0f);
        chart.setHoleColor(Color.TRANSPARENT);
        chart.setData(data);
        //chart.spin( 500,0,360 - angleTo, Easing.EasingOption.EaseInOutQuad ); // rotazione del grafico
        chart.animateY(1000);
        chart.notifyDataSetChanged(); // let the chart know it's data changed

        //PieChartRenderer custom= new PieChartRenderer(chart, chart.getAnimator(),chart.getViewPortHandler());
        //chart.setRenderer(custom);
        chart.invalidate(); // refresh

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

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<WeekData> getWeeks(){
        //TODO get from storage


        List<WeekData> weeks=new ArrayList<WeekData>();
        FileMamoryService fileMamoryService=new FileMamoryService(getApplicationContext());
        try {
            //TODO storage method
            weeks=fileMamoryService.getAllWeekSleepData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weeks;


    }

    private void setTodayValues(){
        int totValue=todaySleep.getHardSleep()+todaySleep.getLightSleep();
        int totH=(int)(totValue/60/60);
        int totM=(int)(totValue/60%60);
        int lightH=(int)(todaySleep.getLightSleep()/60/60);
        int lightM=(int)(todaySleep.getLightSleep()/60%60);
        int deepH=(int)(todaySleep.getHardSleep()/60/60);
        int deepM=(int)(todaySleep.getHardSleep()/60%60);
        hTotText.setText(""+totH);
        mTotText.setText(""+totM);
        mLightText.setText(""+lightM);
        hLightText.setText(""+lightH);
        mDeepText.setText(""+deepM);
        hDeepText.setText(""+deepH);


        gethorizontalBarChart(totValue, gooalSleep);
        float valToPieChart=0;
        if(totValue!=0){
            valToPieChart=(100*todaySleep.getHardSleep())/totValue;
        }

        paintPieChart(valToPieChart );

    }


}
