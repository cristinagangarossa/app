package com.neperiagroup.happysalus.bean;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import cn.imengya.bluetoothle.scanner.ScanDeviceWrapper;

public class ScanDevices implements Parcelable {

    private BluetoothDevice a;
    private int b;
    private byte[] c;

    public ScanDevices(ScanDeviceWrapper scanDeviceWrapper){
        this.a = scanDeviceWrapper.getDevice();
        this.b = scanDeviceWrapper.getRssi();
        this.c = scanDeviceWrapper.getScanRecord();
    }

    protected ScanDevices(Parcel in) {
        a = in.readParcelable(BluetoothDevice.class.getClassLoader());
        b = in.readInt();
        c = in.createByteArray();
    }

    public static final Creator<ScanDevices> CREATOR = new Creator<ScanDevices>() {
        @Override
        public ScanDevices createFromParcel(Parcel in) {
            return new ScanDevices(in);
        }

        @Override
        public ScanDevices[] newArray(int size) {
            return new ScanDevices[size];
        }
    };

    public BluetoothDevice getDevice() {
        return this.a;
    }

    public void setDevice(BluetoothDevice device) {
        this.a = device;
    }

    public int getRssi() {
        return this.b;
    }

    public void setRssi(int rssi) {
        this.b = rssi;
    }

    public byte[] getScanRecord() {
        return this.c;
    }

    public void setScanRecord(byte[] scanRecord) {
        this.c = scanRecord;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(a, i);
        parcel.writeInt(b);
        parcel.writeByteArray(c);
    }
}
