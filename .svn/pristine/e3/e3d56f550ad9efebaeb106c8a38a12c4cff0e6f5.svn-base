package com.neperiagroup.happysalus.bean;

import com.htsmart.wristband.bean.IWristbandUser;

import java.util.Date;
import java.util.List;

/**
 * Created by Kilnn on 16-10-17.
 */
public class User implements IWristbandUser {

    private int id;
    private int height;
    private int weight;
    private int age;
    private String birthday;
    private Date WristbandBirthday;
    private boolean wearLeft;
    private boolean sex;// true for man ,false for woman

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setWristbandBirthday(Date wristbandBirthday) {
        WristbandBirthday = wristbandBirthday;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public boolean getSex(){ return  this.sex; }

    public boolean isWearLeft() {
        return wearLeft;
    }

    public void setWearLeft(boolean wearLeft) {
        this.wearLeft = wearLeft;
    }

    @Override
    public String wristbandUserId() {
        return String.valueOf(id);
    }

    @Override
    public int wristbandHeight() {
        return height;
    }

    @Override
    public int wristbandWeight() {
        return weight;
    }

    @Override
    public Date wristbandBirthday() {
        return WristbandBirthday;
    }

    @Override
    public boolean wristbandSex() {
        return sex;
    }

    @Override
    public boolean wristbandWearLeft() {
        return wearLeft;
    }

    @Override
    public int wristbandDiastolicPressure() {
        return 0;
    }

    @Override
    public int wristbandSystolicPressure() {
        return 0;
    }
}
