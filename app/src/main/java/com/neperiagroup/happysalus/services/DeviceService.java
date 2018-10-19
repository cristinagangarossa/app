package com.neperiagroup.happysalus.services;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neperiagroup.happysalus.R;
import com.neperiagroup.happysalus.bean.BloodPressureData;
import com.neperiagroup.happysalus.bean.DataDevice;
import com.neperiagroup.happysalus.bean.Device;
import com.neperiagroup.happysalus.bean.PressSaveDataBean;
import com.neperiagroup.happysalus.bean.SaveDataBean;
import com.neperiagroup.happysalus.bean.ScaleData;
import com.neperiagroup.happysalus.bean.SleepSaveDataBean;
import com.neperiagroup.happysalus.utility.StoreInMemory;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DeviceService {

    private List<Device> devices;
    private Context context;
    private StoreInMemory storeInMemory;

    public DeviceService(Context context){
        this.context=context;
        this.storeInMemory=new StoreInMemory(context);
        initSampleDevices();

    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }





    private void initSampleDevices(){

        int imageDevice[] = {
                R.drawable.ip68,
                R.drawable.balance,
                R.drawable.sfig
        };

        String nameDevice[] = {
                "Fitness Tracker IP68",
                "Bilancia",
                "Sfigmomanometro"
        };

        String nameBtnDevice[] = {
                "HC92",
                "",
                ""
        };

        String dataType[] = {
                "heart",
                "oxygen",
                "pressure"
        };

        int imageDataDevice[] = {
                R.drawable.ic_heart_light,
                R.drawable.ic_steps_light,
                R.drawable.ic_sleep_light,
                R.drawable.ic_balance_light,
                R.drawable.ic_fire_light,
                R.drawable.ic_timer_light,
                R.drawable.ic_air_light,
                R.drawable.ic_oxygen_light,
                R.drawable.ic_sfig_light
        };

        devices = new ArrayList<Device>();
        List<DataDevice> dataDevices = null;
        Random rand = new Random();
        /*
        for(int x=0; x<3; x++){//Crea i device
            dataDevices = new ArrayList<DataDevice>();
            Random randDataType = new Random();
            for(int i=0; i<rand.nextInt(6)+1; i++){//Crea i dati dei dispositivi random
                DataDevice dataDevice = new DataDevice(imageDataDevice[i], "127 bpm", "Ultimo monitoraggio: 15:32", "Attività: intensa", dataType[randDataType.nextInt(3)]);
                dataDevices.add(dataDevice);
            }
            Device device = new Device(imageDevice[x], nameDevice[x],57, nameBtnDevice[x], dataDevices);
            devices.add(device);
        }
        */

        devices.add(getTracker());
        devices.add(getBalance());
        devices.add(getSfigmomanometro());
    }//Fine initSampleDevice



    private Device getTracker(){
        //TODO add control if there are data about previous misuration
        //TODO get battery level
        int batteryLevel=57;
        List<DataDevice> dataDevices =getDatadevices(1);
        Device device=new  Device(R.drawable.ip68,"Fitness Tracker IP68",batteryLevel,"HC92",dataDevices);
        return device;
    }

    private Device getBalance(){
        //TODO add control if there are data about previous misuration
        //TODO get battery level
        int batteryLevel=57;
        List<DataDevice> dataDevices =getDatadevices(2);
        Device device=new  Device(R.drawable.balance,"Balance",batteryLevel,"",dataDevices);
        return device;
    }
    private Device getSfigmomanometro(){
        //TODO add control if there are data about previous misuration
        //TODO get battery level
        int batteryLevel=57;
        List<DataDevice> dataDevices =getDatadevices(3);
        Device device=new  Device(R.drawable.sfig,"Sfigmomanometro",batteryLevel,"",dataDevices);
        return device;
    }

    private List<DataDevice> getDatadevices(int type){
        List<DataDevice> devices=new ArrayList<DataDevice>();
        if(type==1){//case traker
            //TODO get previous data
            String info;
            String state="";
            String subInfo="";
            Date date;
            Calendar calendar=Calendar.getInstance();
            Calendar today=Calendar.getInstance();
            String lastBpm=storeInMemory.getValueStringFromStore("lastHeartRate");
            Gson gson = new Gson();
            Type SaveDataType = new TypeToken<SaveDataBean>(){}.getType();
            SaveDataBean saveDataBean= gson.fromJson(lastBpm, SaveDataType);
            if(saveDataBean!=null){
                info=Math.round(Float.valueOf(saveDataBean.getValue()))+ " bpm";
                if(Float.valueOf(saveDataBean.getValue())<=85){
                    state="Attivita normale";
                }else if(Float.valueOf(saveDataBean.getValue())<=105){
                    state="Attivita moderata";
                }else if(Float.valueOf(saveDataBean.getValue())<=125){
                    state="Attivita intensa";
                }
                else if(Float.valueOf(saveDataBean.getValue())<=125){
                    state="Attivita vigorosa";
                }
                date=saveDataBean.getDate();
                calendar.setTime(date);
                if(calendar.get(Calendar.DAY_OF_YEAR)==today.get(Calendar.DAY_OF_YEAR)){
                    SimpleDateFormat format1 = new SimpleDateFormat("H:m");
                    subInfo="Ultimo monitoraggio "+format1.format(calendar.getTime());
                }else{
                    SimpleDateFormat format1 = new SimpleDateFormat("d/MM");
                    subInfo="Ultimo monitoraggio "+format1.format(calendar.getTime());
                }
            }else{
                info="";
                state="Non hai effettuato la prima lettura";
                subInfo="";
            }

            String dataType="heart";
            DataDevice heart=new DataDevice(R.drawable.ic_heart_light,info,subInfo,state,dataType);
            devices.add(heart);

            String lastOxygen=storeInMemory.getValueStringFromStore("lastOxygen");
            saveDataBean= gson.fromJson(lastOxygen, SaveDataType);

            if(saveDataBean!=null){
                info=Math.round(Float.valueOf(saveDataBean.getValue()))+ " %";
                if(Float.valueOf(saveDataBean.getValue())>95){
                    state="Ossigenezione nella norma";
                }else{
                    state="Ossigenazione bassa";
                }

                date=saveDataBean.getDate();
                calendar.setTime(date);
                if(calendar.get(Calendar.DAY_OF_YEAR)==today.get(Calendar.DAY_OF_YEAR)){
                    SimpleDateFormat format1 = new SimpleDateFormat("H:m");
                    subInfo="Ultimo monitoraggio "+format1.format(calendar.getTime());
                }else{
                    SimpleDateFormat format1 = new SimpleDateFormat("d/MM");
                    subInfo="Ultimo monitoraggio "+format1.format(calendar.getTime());
                }

            }else{
                info="";
                state="Non hai effettuato la prima lettura";
                subInfo="";
            }

            dataType="oxygen";
            DataDevice ox=new DataDevice(R.drawable.ic_oxygen_light,info,subInfo,state,dataType);
            devices.add(ox);


            String lastStep=storeInMemory.getValueStringFromStore("lastStep");
            saveDataBean= gson.fromJson(lastStep, SaveDataType);

            if(saveDataBean!=null){
                info=Math.round(Float.valueOf(saveDataBean.getValue()))+ "";
                state="";

                date=saveDataBean.getDate();
                calendar.setTime(date);
                if(calendar.get(Calendar.DAY_OF_YEAR)==today.get(Calendar.DAY_OF_YEAR)){
                    SimpleDateFormat format1 = new SimpleDateFormat("H:m");
                    subInfo="Ultimo monitoraggio "+format1.format(calendar.getTime());
                }else{
                    SimpleDateFormat format1 = new SimpleDateFormat("d/MM");
                    subInfo="Ultimo monitoraggio "+format1.format(calendar.getTime());
                }

            }else{
                info="";
                state="Non hai effettuato la prima lettura";
                subInfo="";
            }



            dataType="steps";
            DataDevice steps=new DataDevice(R.drawable.ic_steps_light,info,subInfo,state,dataType);
            devices.add(steps);

            Type sleepDataBeanType = new TypeToken<SleepSaveDataBean>(){}.getType();
            String lastSleep=storeInMemory.getValueStringFromStore("lastSleep");
            SleepSaveDataBean sleepDataBean= gson.fromJson(lastSleep, sleepDataBeanType);

            if(sleepDataBean!=null){
                int tot=sleepDataBean.getHardSleep()+sleepDataBean.getLightSleep();
                int h=(int)(tot/60/60);
                int min=(int)(tot/60%60);
                info=h+" ore "+ min +" min";
                state="";

                date=saveDataBean.getDate();
                calendar.setTime(date);
                if(calendar.get(Calendar.DAY_OF_YEAR)==today.get(Calendar.DAY_OF_YEAR)){
                    SimpleDateFormat format1 = new SimpleDateFormat("H:m");
                    subInfo="Ultimo monitoraggio "+format1.format(calendar.getTime());
                }else{
                    SimpleDateFormat format1 = new SimpleDateFormat("d/MM");
                    subInfo="Ultimo monitoraggio "+format1.format(calendar.getTime());
                }

            }else{
                info="";
                state="Non hai effettuato la prima lettura";
                subInfo="";
            }

            dataType="sleep";
            DataDevice sleep=new DataDevice(R.drawable.ic_sleep_light,info,subInfo,state,dataType);
            devices.add(sleep);


            PressSaveDataBean pressSaveDataBean;
            Type pressSaveDataBeanType = new TypeToken<PressSaveDataBean>(){}.getType();
            String lastPress=storeInMemory.getValueStringFromStore("lastPressure");
            pressSaveDataBean=gson.fromJson(lastPress,pressSaveDataBeanType);
            if(pressSaveDataBean!=null){
                info=pressSaveDataBean.getPressData().getMin()+" min "+pressSaveDataBean.getPressData().getMax()+" max";
                state="";

                date=pressSaveDataBean.getDate();
                calendar.setTime(date);
                if(calendar.get(Calendar.DAY_OF_YEAR)==today.get(Calendar.DAY_OF_YEAR)){
                    SimpleDateFormat format1 = new SimpleDateFormat("H:m");
                    subInfo="Ultimo monitoraggio "+format1.format(calendar.getTime());
                }else{
                    SimpleDateFormat format1 = new SimpleDateFormat("d/MM");
                    subInfo="Ultimo monitoraggio "+format1.format(calendar.getTime());
                }
            }else{
                info="";
                state="Non hai effettuato la prima lettura";
                subInfo="";
            }

            dataType="pressure";
            DataDevice pressure=new DataDevice(R.drawable.ic_sfig_light,info,subInfo,state,dataType);
            devices.add(pressure);

            String lastBR=storeInMemory.getValueStringFromStore("lastRespiratoryRate");
            saveDataBean= gson.fromJson(lastBR, SaveDataType);

            if(saveDataBean!=null){
                info=Math.round(Float.valueOf(saveDataBean.getValue()))+ "";
                state="";

                date=saveDataBean.getDate();
                calendar.setTime(date);
                if(calendar.get(Calendar.DAY_OF_YEAR)==today.get(Calendar.DAY_OF_YEAR)){
                    SimpleDateFormat format1 = new SimpleDateFormat("H:m");
                    subInfo="Ultimo monitoraggio "+format1.format(calendar.getTime());
                }else{
                    SimpleDateFormat format1 = new SimpleDateFormat("d/MM");
                    subInfo="Ultimo monitoraggio "+format1.format(calendar.getTime());
                }

            }else{
                info="";
                state="Non hai effettuato la prima lettura";
                subInfo="";
            }


            dataType="breathFrequency";
            DataDevice oxygen=new DataDevice(R.drawable.ic_air_light,info,subInfo,state,dataType);
            devices.add(oxygen);



        }else if(type==2){//case libra
            Gson gson=new Gson();
            ScaleData scaleData;
            Type scaleDataBeanType = new TypeToken<ScaleData>(){}.getType();
            String lastScale=storeInMemory.getValueStringFromStore("lastScaleData");
            scaleData=gson.fromJson(lastScale,scaleDataBeanType);
            String info;
            String subInfo;
            String state;
            if(scaleData!=null){
                info=scaleData.getWeight()+" kg";
                Date date;
                date=scaleData.getTimestamp();
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(date);
                Calendar today=Calendar.getInstance();
                if(calendar.get(Calendar.DAY_OF_YEAR)==today.get(Calendar.DAY_OF_YEAR)){
                    SimpleDateFormat format1 = new SimpleDateFormat("H:m");
                    subInfo="Ultimo monitoraggio "+format1.format(calendar.getTime());
                }else{
                    SimpleDateFormat format1 = new SimpleDateFormat("d/MM");
                    subInfo="Ultimo monitoraggio "+format1.format(calendar.getTime());
                }
                state="";
            }else{
                info="";
                subInfo="Non hai effettuato la prima lettura";
                state="";
            }


            String dataType="balance";
            DataDevice libra=new DataDevice(R.drawable.ic_balance_light,info,subInfo,state,dataType);
            devices.add(libra);

        }else if(type==3){
            Gson gson=new Gson();
            BloodPressureData bloodPressureData;
            Type pressSaveDataBeanType = new TypeToken<BloodPressureData>(){}.getType();
            String lastPress=storeInMemory.getValueStringFromStore("BloodPressureData");
            bloodPressureData=gson.fromJson(lastPress,pressSaveDataBeanType);

            String info;
            String subInfo;
            String state;
            if(bloodPressureData!=null){
                info=bloodPressureData.getDiastolic()+ " min "+bloodPressureData.getSystolic()+" max";
                Date date;
                date=bloodPressureData.getTimestamp();
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(date);
                Calendar today=Calendar.getInstance();
                if(calendar.get(Calendar.DAY_OF_YEAR)==today.get(Calendar.DAY_OF_YEAR)){
                    SimpleDateFormat format1 = new SimpleDateFormat("H:m");
                    subInfo="Ultimo monitoraggio "+format1.format(calendar.getTime());
                }else{
                    SimpleDateFormat format1 = new SimpleDateFormat("d/MM");
                    subInfo="Ultimo monitoraggio "+format1.format(calendar.getTime());
                }
                state="";
            }else{
                info="";
                subInfo="Non hai effettuato la prima lettura";
                state="";
            }

            String dataType="pressure";
            DataDevice pressure=new DataDevice(R.drawable.ic_sfig_light,info,subInfo,state,dataType);
            devices.add(pressure);
        }

        return devices;
    }
}
