package com.neperiagroup.happysalus.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PresDayData implements GenericDayData {

    Date date;
    String data;
    String presMax;
    String pressMin;
    SimpleDateFormat sdf;

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

    public String getPresMax() {
        return presMax;
    }

    public void setPresMax(String presMax) {
        this.presMax = presMax;
    }

    public String getPressMin() {
        return pressMin;
    }

    public void setPressMin(String pressMin) {
        this.pressMin = pressMin;
    }
}
