package com.neperiagroup.happysalus.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.neperiagroup.happysalus.bean.BalanceDayData;
import com.neperiagroup.happysalus.bean.BreathDayData;
import com.neperiagroup.happysalus.bean.HrDayData;
import com.neperiagroup.happysalus.bean.OxygenDayData;
import com.neperiagroup.happysalus.bean.PresDayData;
import com.neperiagroup.happysalus.bean.SleepDayData;
import com.neperiagroup.happysalus.bean.StepDayData;
import com.neperiagroup.happysalus.bean.WeekData;
import com.neperiagroup.happysalus.R;

import java.util.List;

public class WeekDataAdapter extends BaseAdapter {

    private Context context;
    private List<WeekData> weeks;
    private int daysDone;

    public WeekDataAdapter(Context context, List<WeekData> weeks) {
        this.context = context;
        this.weeks = weeks;
        this.daysDone=0;
    }

    @Override
    public int getCount() {
        return weeks.size();
    }

    @Override
    public Object getItem(int i) {
        return weeks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.week_data_list_item, viewGroup, false);
            TextView week=(TextView) view.findViewById(R.id.week);
            week.setText(weeks.get(i).getWeek());

            TextView value=(TextView) view.findViewById(R.id.avarage_value);
            value.setText(weeks.get(i).getValue());
            if(weeks.get(i).getDays()!=null && i>=daysDone) {
                setDays(view, i, viewGroup);
                daysDone++;
            }
        }



        return view;
    }


    private void setDays(View view, int i,ViewGroup viewGroup){
        WeekData weekData=weeks.get(i);


        for(Object dayData:weekData.getDays()){
            if(dayData instanceof HrDayData){

                LinearLayout ll =(LinearLayout)view.findViewById(R.id.layout_days);

                TextView valueLabelBpmAvg=(TextView) view.findViewById(R.id.avarage_value);
                valueLabelBpmAvg.setText(weeks.get(i).getValue() + " BPM medio");

                HrDayData hrData=(HrDayData)dayData;
                String day=hrData.getDay();
                String value=hrData.getData();
                String min=hrData.getMin();
                String max=hrData.getMax();

                LayoutInflater li = LayoutInflater.from(context);
                View layout = li.inflate(R.layout.hr_day, ll, false);

                TextView dayText=layout.findViewById(R.id.day);
                dayText.setText(day);

                TextView valueText=layout.findViewById(R.id.value);
                valueText.setText(value);

                TextView minText=layout.findViewById(R.id.min);
                minText.setText(min);

                TextView maxText=layout.findViewById(R.id.max);
                maxText.setText(max);
                ll.addView(layout);

            }else if(dayData instanceof StepDayData){
                LinearLayout ll =(LinearLayout)view.findViewById(R.id.layout_days);

                StepDayData stepDayData=(StepDayData)dayData;
                String day=stepDayData.getDay();
                String value=stepDayData.getData();


                LayoutInflater li = LayoutInflater.from(context);
                View layout = li.inflate(R.layout.step_day, ll, false);

                TextView dayText=layout.findViewById(R.id.day);
                dayText.setText(day);

                TextView valueText=layout.findViewById(R.id.value);
                valueText.setText(value);

                ll.addView(layout);
            }else if(dayData instanceof PresDayData){
                LinearLayout ll =(LinearLayout)view.findViewById(R.id.layout_days);

                PresDayData presDayData=(PresDayData)dayData;
                String day=presDayData.getDay();
                String minPres=presDayData.getPressMin();
                String maxPres=presDayData.getPresMax();

                LayoutInflater li = LayoutInflater.from(context);
                View layout = li.inflate(R.layout.pres_day, ll, false);

                TextView dayText=layout.findViewById(R.id.day);
                dayText.setText(day);

                TextView minText=layout.findViewById(R.id.value);
                minText.setText(minPres);

                TextView maxText=layout.findViewById(R.id.max);
                maxText.setText(maxPres);

                ll.addView(layout);
            }else if(dayData instanceof OxygenDayData){
                LinearLayout ll =(LinearLayout)view.findViewById(R.id.layout_days);

                TextView valueLabelAVG=(TextView) view.findViewById(R.id.avarage_value);
                valueLabelAVG.setText(weeks.get(i).getValue() + " %");

                OxygenDayData oxygenDayData=(OxygenDayData)dayData;
                String day=oxygenDayData.getDay();
                String value=oxygenDayData.getData();


                LayoutInflater li = LayoutInflater.from(context);
                View layout = li.inflate(R.layout.oxygen_day, ll, false);

                TextView dayText=layout.findViewById(R.id.day);
                dayText.setText(day);

                TextView dataText=layout.findViewById(R.id.value);
                dataText.setText(value);
                ll.addView(layout);

            }else if(dayData instanceof BreathDayData){
                LinearLayout ll =(LinearLayout)view.findViewById(R.id.layout_days);

                BreathDayData breathDayData=(BreathDayData)dayData;
                String day=breathDayData.getDay();
                String value=breathDayData.getData();
                String min=breathDayData.getMin();
                String max=breathDayData.getMax();

                LayoutInflater li = LayoutInflater.from(context);
                View layout = li.inflate(R.layout.breath_day, ll, false);

                TextView dayText=layout.findViewById(R.id.day);
                dayText.setText(day);

                TextView dataText=layout.findViewById(R.id.value);
                dataText.setText(value);

                TextView minText=layout.findViewById(R.id.min);
                minText.setText(min);

                TextView maxText=layout.findViewById(R.id.max);
                maxText.setText(max);
                ll.addView(layout);
            }else if(dayData instanceof BalanceDayData){
                LinearLayout ll =(LinearLayout)view.findViewById(R.id.layout_days);

                BalanceDayData balanceDayData=(BalanceDayData)dayData;
                String day=balanceDayData.getDay();
                String value=balanceDayData.getData();

                LayoutInflater li = LayoutInflater.from(context);
                View layout = li.inflate(R.layout.balance_day, ll, false);

                TextView dayText=layout.findViewById(R.id.day);
                dayText.setText(day);

                TextView dataText=layout.findViewById(R.id.value);
                dataText.setText(value);
                ll.addView(layout);
            }else if(dayData instanceof SleepDayData){
                LinearLayout ll =(LinearLayout)view.findViewById(R.id.layout_days);

                SleepDayData sleepDayData=(SleepDayData)dayData;
                String day=sleepDayData.getDay();


                LayoutInflater li = LayoutInflater.from(context);
                View layout = li.inflate(R.layout.sleep_day, ll, false);

                TextView dayText=layout.findViewById(R.id.day);
                dayText.setText(day);

                int sleepLightH=(int)(sleepDayData.getLightSleep()/60/60);
                TextView lightSleepHText=layout.findViewById(R.id.lightSleepH);
                lightSleepHText.setText(""+sleepLightH);

                int sleepLightM=(int)(sleepDayData.getLightSleep()/60%60);
                TextView sleepLighMText=layout.findViewById(R.id.lightSleepM);
                sleepLighMText.setText(""+sleepLightM);

                int sleepHardH=(int)(sleepDayData.getHardSleep()/60/60);
                TextView sleepHardHText=layout.findViewById(R.id.hardSleepH);
                sleepHardHText.setText(""+sleepHardH);

                int sleepHardM=(int)(sleepDayData.getHardSleep()/60%60);
                TextView sleepHardMText=layout.findViewById(R.id.hardSleepM);
                sleepHardMText.setText(""+sleepHardM);

                ll.addView(layout);

            }
        }

    }
}
