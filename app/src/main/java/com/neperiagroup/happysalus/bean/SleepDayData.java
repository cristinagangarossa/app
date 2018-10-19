package com.neperiagroup.happysalus.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SleepDayData implements GenericDayData {

    String data;
    Date date;
    SimpleDateFormat sdf;
    int lightSleep;
    int hardSleep;

    @Override
    public String getDay() {
        sdf = new SimpleDateFormat("EEE", java.util.Locale.ENGLISH);
        return sdf.format(date);
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public void setData(String data) {
    this.data=data;
    }

    @Override
    public void setDate(Date date) {
        this.date=date;
    }

    @Override
    public Date getDate() {
        return date;
    }

    public int getLightSleep() {
        return lightSleep;
    }

    public void setLightSleep(int lightSleep) {
        this.lightSleep = lightSleep;
    }

    public int getHardSleep() {
        return hardSleep;
    }

    public void setHardSleep(int hardSleep) {
        this.hardSleep = hardSleep;
    }
}
