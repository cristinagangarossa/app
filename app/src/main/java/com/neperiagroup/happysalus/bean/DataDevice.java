package com.neperiagroup.happysalus.bean;

import android.widget.ImageView;

import java.io.Serializable;

public class DataDevice implements Serializable {

    private int imageResourceData;
    private String info;
    private String subInfo;
    private String state;
    private String dataType;

    public DataDevice(int imageResourceData, String info, String subInfo, String state, String dataType) {
        this.imageResourceData = imageResourceData;
        this.info = info;
        this.subInfo = subInfo;
        this.state = state;
        this.dataType = dataType;
    }

    public int getImageResourceData() {
        return imageResourceData;
    }

    public void setImageResourceData(int imageResourceData) {
        this.imageResourceData = imageResourceData;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getSubInfo() {
        return subInfo;
    }

    public void setSubInfo(String subInfo) {
        this.subInfo = subInfo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
