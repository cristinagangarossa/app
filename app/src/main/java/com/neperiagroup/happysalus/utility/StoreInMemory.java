package com.neperiagroup.happysalus.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.neperiagroup.happysalus.bean.Device;
import com.neperiagroup.happysalus.bean.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoreInMemory {

    private Context context;
    private TagUser tagUser;

    public StoreInMemory(Context context) {

        this.context = context;
        tagUser = new TagUser();
    }

    public void setValueBooleanToStore(String key, boolean value){
        String nameClassKey = context.getClass().getName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(nameClassKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getValueBooleanFromStore(String key){
        String nameClassKey = context.getClass().getName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(nameClassKey, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public void setValueIntToStore(String key, int value){
        String nameClassKey = context.getClass().getName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(nameClassKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void setValueStringToStore(String key,String value){
        String nameClassKey = context.getClass().getName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(nameClassKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public String getValueStringFromStore(String key){
        String nameClassKey = context.getClass().getName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(nameClassKey, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public int getValueIntFromStore(String key){
        String nameClassKey = context.getClass().getName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(nameClassKey, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    public void setNameBtcDeviceUserBindToStore(int idUser, String nameBtcDevice){
        String nameClassKey = context.getClass().getName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(nameClassKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!checkNameBtcDeviceUserBindFromStore(idUser, nameBtcDevice)){//Se il dispositivo non è stato mai associato
            int sizeList = sharedPreferences.getInt(tagUser.getKEY_USER_ID() + "." + idUser + "_sizeListDevices", 0);
            sizeList++; //Incrementa la lunghezza dell'array
            editor.putInt(tagUser.getKEY_USER_ID() + "." + idUser + "_sizeListDevices", sizeList);//Memorizza la lunghezza dell'array
            sizeList--;//Lo decremento per far partire l'indice da zero
            editor.putString(tagUser.getKEY_USER_ID() + "." + idUser + "_nameBtcDevice_" + sizeList, nameBtcDevice);//Memorizza il nome del dispositivo bluetooth
            editor.commit();
        }//Fine if
    }

    public boolean checkNameBtcDeviceUserBindFromStore(int idUser, String nameBtcDevice){
        String nameClassKey = context.getClass().getName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(nameClassKey, Context.MODE_PRIVATE);
        int sizeList = sharedPreferences.getInt(tagUser.getKEY_USER_ID() + "." + idUser + "_sizeListDevices", 0);
        for(int i=0; i<sizeList; i++){
            String nameDevice = sharedPreferences.getString(tagUser.getKEY_USER_ID() + "." + idUser + "_nameBtcDevice_" + i, null);
            if(nameDevice!=null){
                if(nameDevice.equalsIgnoreCase(nameBtcDevice)){
                    return true; //Device già associato in precedenza
                }
            }
        }
        return false; //Dispositivo non trovato
    }

    public List<String> getListNameBtcDeviceUserBindFromStore(int idUser){
        String nameClassKey = context.getClass().getName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(nameClassKey, Context.MODE_PRIVATE);
        int sizeList = sharedPreferences.getInt(tagUser.getKEY_USER_ID() + "." + idUser + "_sizeListDevices", 0);
        List<String> nameBtcDevices = new ArrayList<String>();
        for(int i=0; i<sizeList; i++){
            nameBtcDevices.add(sharedPreferences.getString(tagUser.getKEY_USER_ID() + "." + idUser + "_nameBtcDevice_" + i, null));
        }
        return nameBtcDevices;
    }

    public void setUser(User user){
        String nameClassKey = context.getClass().getName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(nameClassKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(tagUser.getKEY_USER_ID()+"."+user.getId(), user.getId());
        editor.putInt(tagUser.getKEY_USER_HEIGHT()+"."+user.getId(), user.getHeight());
        editor.putInt(tagUser.getKEY_USER_WEIGHT()+"."+user.getId(), user.getWeight());
        editor.putInt(tagUser.getKEY_USER_AGE()+"."+user.getId(), user.getAge());
        editor.putString(tagUser.getKEY_USER_BIRTHDAY()+"."+user.getId(), user.getBirthday());
        editor.putBoolean(tagUser.getKEY_USER_SEX()+"."+user.getId(), user.getSex());
        editor.commit();
    }

    public User getUser(int idUser){
        String nameClassKey = context.getClass().getName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(nameClassKey, Context.MODE_PRIVATE);
        User user = new User();
        user.setId(sharedPreferences.getInt(tagUser.getKEY_USER_ID()+"."+idUser, 0));
        user.setHeight(sharedPreferences.getInt(tagUser.getKEY_USER_HEIGHT()+"."+idUser, 0));
        user.setWeight(sharedPreferences.getInt(tagUser.getKEY_USER_WEIGHT()+"."+idUser, 0));
        user.setAge(sharedPreferences.getInt(tagUser.getKEY_USER_AGE()+"."+idUser, 0));
        user.setBirthday(sharedPreferences.getString(tagUser.getKEY_USER_BIRTHDAY()+"."+idUser, ""));
        user.setSex(sharedPreferences.getBoolean(tagUser.getKEY_USER_SEX()+"."+idUser, false));
        return user;
    }

    public boolean checkUser(int idUser){
        String KEY_USER_ID = tagUser.getKEY_USER_ID() + "." + idUser;
        String nameClassKey = context.getClass().getName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(nameClassKey, Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt(KEY_USER_ID, 0);
        if(id!=0){
            return true;
        }
        return false;
    }


}
