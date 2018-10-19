package com.neperiagroup.happysalus.bean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HrDayData implements GenericDayData {


    SimpleDateFormat sdf;
    Date date;
    String data;
    String min;
    String max;

    public HrDayData() {

    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    @Override
    public String getDay(){
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
}
