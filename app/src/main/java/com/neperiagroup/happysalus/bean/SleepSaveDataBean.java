package com.neperiagroup.happysalus.bean;

import java.util.Date;

public class SleepSaveDataBean {
    int lightSleep;
    int hardSleep;
    Date date;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
