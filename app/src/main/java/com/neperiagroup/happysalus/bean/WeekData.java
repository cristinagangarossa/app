package com.neperiagroup.happysalus.bean;

import java.util.List;

public class WeekData<T > {

    String week;
    String value;
    List<T> days;

    public WeekData() {
    }

    public WeekData(String week, String value) {
        this.week = week;
        this.value = value;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<T> getDays() {
        return days;
    }

    public void setDays(List<T> days) {
        this.days = days;
    }
}
