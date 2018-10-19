package com.neperiagroup.happysalus.listeners;

import android.util.Log;

import com.htsmart.wristband.bean.EcgBean;
import com.htsmart.wristband.bean.SleepTotalData;
import com.htsmart.wristband.bean.SyncRawData;
import com.htsmart.wristband.bean.TodayTotalData;
import com.htsmart.wristband.performer.SimplePerformerListener;
import com.neperiagroup.happysalus.services.WritePeriodicalyService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WriterListener extends SimplePerformerListener {

    private static final String TAG = WriterListener.class.getName();
    List<Integer> oxygenValues;
    List<Integer> heartRateValues;
    List<Integer> diastolicPressureValues;
    List<Integer> systolicPressureValues;
    List<Integer> respiratoryRateValues;
    int deepSleep=0;
    int lightSleep=0;
    int calories=0;
    int distance=0;

    int steps;


    @Override
    public void onSyncDataSleepTotalData(List<SleepTotalData> datas) {
        String dataToString="";
        Log.e(TAG, "------------------" );
        Log.e(TAG,"onSyncDataSleepTotalData" );
        Log.e(TAG, "------------------" );
        for(SleepTotalData data :datas){
            dataToString+="deep sleep "+String.valueOf(data.getDeepSleep());
            dataToString+=" light sleep"+String.valueOf(data.getLightSleep());
            deepSleep=data.getDeepSleep();
            lightSleep=data.getLightSleep();
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
        Log.e(TAG, "------------------" );
        Log.e(TAG, "onSyncDataResult" );
        Log.e(TAG, "------------------" );
        int i =0;
        String row="";
        for(SyncRawData data: datas){
            row="";
            Log.i("row",""+i);
            row+="type "+String.valueOf(data.getType());
            row+=" value "+ String.valueOf(data.getValue());
            row+=" value2 "+String.valueOf(data.getValue2());
            Date d=new Date(data.getTimeStamp());
            row+="date " +d.toString();
            Log.i("row i ", row);
            i++;
        }
    }

    @Override
    public void onSyncDataTodayTotalData(TodayTotalData data) {
        Log.e(TAG, "------------------" );
        Log.e(TAG, "steps "+ String.valueOf(data.getSteps()) );
        Log.e(TAG, "------------------" );

        Log.e(TAG, "------------------" );
        Log.e(TAG, "calories "+ String.valueOf(data.getCalories()) );
        Log.e(TAG, "------------------" );

        Log.e(TAG, "------------------" );
        Log.e(TAG, "distance "+ String.valueOf(data.getDistance()) );
        Log.e(TAG, "------------------" );

        distance=data.getDistance();
        calories=data.getCalories();
        steps=data.getSteps();

    }

    @Override
    public void onResultHealthyRealTimeData(int heartRate, int oxygen, int diastolicPressure, int systolicPressure, int respiratoryRate) {
        super.onResultHealthyRealTimeData(heartRate, oxygen, diastolicPressure, systolicPressure, respiratoryRate);

        if(heartRate!=0){
            if(heartRateValues==null){
                heartRateValues=new ArrayList<Integer>();
            }
            heartRateValues.add(heartRate);
        }


        if(oxygen!=0){
            if(oxygenValues==null){
                oxygenValues=new ArrayList<Integer>();
            }
            oxygenValues.add(oxygen);
        }

        if(diastolicPressure!=0){
            if(diastolicPressureValues==null){
                diastolicPressureValues=new ArrayList<Integer>();
            }
            diastolicPressureValues.add(diastolicPressure);
        }

        if(systolicPressure!=0){
            if(systolicPressureValues==null){
                systolicPressureValues=new ArrayList<Integer>();
            }
            systolicPressureValues.add(systolicPressure);
        }

        if(respiratoryRate!=0){
            if(respiratoryRateValues==null){
                respiratoryRateValues=new ArrayList<Integer>();
            }
            respiratoryRateValues.add(respiratoryRate);
        }

    }
    @Override
    public void onSyncDataEcgResult(List<EcgBean> ecgBeanList) {
        Log.e(TAG, "------------------" );
        Log.e(TAG, "number of ecgBeans "+ String.valueOf(ecgBeanList.size()) );
        Log.e(TAG, "------------------" );

    }





    public float getOxygen(){
        float value=0;
        if(oxygenValues==null){
            return 0;
        }
        for(Integer i :oxygenValues){
            value+=i;
        }
        value=value/oxygenValues.size();
        return value;
    }

    public float getHeartRate(){
        if(heartRateValues==null){
            return 0;
        }
        float value=0;
        for(Integer i :heartRateValues){
            value+=i;
        }
        value=value/heartRateValues.size();
        return value;

    }

    public float getDiastolicPressure(){
        if(diastolicPressureValues==null){
            return 0;
        }
        float value=0;
        for(Integer i :diastolicPressureValues){
            value+=i;
        }
        value=value/diastolicPressureValues.size();
        return value;
    }

    public float getSystolicPressure(){
        float value=0;
        if(systolicPressureValues==null){
            return 0;
        }
        for(Integer i :systolicPressureValues){
            value+=i;
        }
        value=value/systolicPressureValues.size();
        return value;
    }

    public float getRespiratoryRate(){
        if(respiratoryRateValues==null){
            return 0;
        }
        float value=0;
        for(Integer i :respiratoryRateValues){
            value+=i;
        }
        value=value/respiratoryRateValues.size();
        return value;
    }

    public int getSteps(){
        return steps;
    }

    public void clearAll(){
        oxygenValues=null;
        heartRateValues=null;
        diastolicPressureValues=null;
        systolicPressureValues=null;
        respiratoryRateValues=null;
        deepSleep=0;
        lightSleep=0;
        distance=0;
        calories=0;

    }

    public int getDeepSleep() {
        return deepSleep;
    }

    public int getLightSleep() {
        return lightSleep;
    }

    public static String getTAG() {
        return TAG;
    }

    public int getCalories() {
        return calories;
    }

    public int getDistance() {
        return distance;
    }
}
