package com.neperiagroup.happysalus.bean;

public class BreatheFreq {

    private int avgBreatheDay;
    private int minBreatheDay;
    private int maxBreatheDay;

    public BreatheFreq() {
    }

    public BreatheFreq(int avgBreatheDay, int minBreatheDay, int maxBreatheDay) {
        this.avgBreatheDay = avgBreatheDay;
        this.minBreatheDay = minBreatheDay;
        this.maxBreatheDay = maxBreatheDay;
    }

    public int getAvgBreatheDay() {
        return avgBreatheDay;
    }

    public void setAvgBreatheDay(int avgBreatheDay) {
        this.avgBreatheDay = avgBreatheDay;
    }

    public int getMinBreatheDay() {
        return minBreatheDay;
    }

    public void setMinBreatheDay(int minBreatheDay) {
        this.minBreatheDay = minBreatheDay;
    }

    public int getMaxBreatheDay() {
        return maxBreatheDay;
    }

    public void setMaxBreatheDay(int maxBreatheDay) {
        this.maxBreatheDay = maxBreatheDay;
    }
}
