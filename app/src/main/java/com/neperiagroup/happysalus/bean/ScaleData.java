package com.neperiagroup.happysalus.bean;

import java.util.Date;

public class ScaleData {
    int scale = -1;
    double weight = -1;
    Date timestamp = null;
    double impedance = -1.0;
    int userId = -1;
    int weightStatusStability = -1;
    String weightStatusType = null;

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getImpedance() {
        return impedance;
    }

    public void setImpedance(double impedance) {
        this.impedance = impedance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWeightStatusStability() {
        return weightStatusStability;
    }

    public void setWeightStatusStability(int weightStatusStability) {
        this.weightStatusStability = weightStatusStability;
    }

    public String getWeightStatusType() {
        return weightStatusType;
    }

    public void setWeightStatusType(String weightStatusType) {
        this.weightStatusType = weightStatusType;
    }

    @Override
    public String toString() {
        return "ScaleData{" + "scale=" + scale + ", weight=" + weight + ", timestamp=" + timestamp + ", impedance=" + impedance + ", userId=" + userId + ", weightStatusStability=" + weightStatusStability + ", weightStatusType=" + weightStatusType + '}';
    }
}
