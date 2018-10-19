package com.neperiagroup.happysalus.bean;

import java.util.Date;

public class BloodPressureData {
    int unit = -1;
    int systolic = -1;
    int diastolic = -1;
    int meanArterial = -1;
    Date timestamp = null;
    int heartRate = -1;
    int userId = -1;

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getSystolic() {
        return systolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    public int getMeanArterial() {
        return meanArterial;
    }

    public void setMeanArterial(int meanArterial) {
        this.meanArterial = meanArterial;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "BloodPressureData{" + "unit=" + unit + ", systolic=" + systolic + ", diastolic=" + diastolic + ", meanArterial=" + meanArterial + ", timestamp=" + timestamp + ", heartRate=" + heartRate + ", userId=" + userId + '}';
    }
}
