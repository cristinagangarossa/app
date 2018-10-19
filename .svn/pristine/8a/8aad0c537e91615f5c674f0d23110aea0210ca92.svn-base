package com.neperiagroup.happysalus.bean;

import java.util.Arrays;
import java.util.UUID;

public class BLECommunicationObject {
    public static final int TYPE_CHARACTERISTIC = 0;
    public static final int TYPE_DESCRIPTOR = 1;
    public static final int ACTION_READ = 1;
    public static final int ACTION_WRITE = 2;

    UUID descriptor = null;
    UUID characteristic = null;
    int type = -1;
    String description = null;
    int action = -1;
    byte[] value = null;
    boolean mustEnableNotify = false;

    public UUID getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(UUID descriptor) {
        this.descriptor = descriptor;
    }

    public UUID getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(UUID characteristic) {
        this.characteristic = characteristic;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public boolean isMustEnableNotify() {
        return mustEnableNotify;
    }

    public void setMustEnableNotify(boolean mustEnableNotify) {
        this.mustEnableNotify = mustEnableNotify;
    }

    @Override
    public String toString() {
        return "BLECommunicationObject{" +
                "descriptor=" + descriptor +
                ", characteristic=" + characteristic +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", action=" + action +
                ", value=" + Arrays.toString(value) +
                ", mustEnableNotify=" + mustEnableNotify +
                '}';
    }
}
