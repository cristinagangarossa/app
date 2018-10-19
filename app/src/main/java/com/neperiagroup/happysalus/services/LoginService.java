package com.neperiagroup.happysalus.services;

import android.content.Context;
import android.util.Log;

import com.neperiagroup.happysalus.bean.User;
import com.neperiagroup.happysalus.utility.TagUser;
import com.neperiagroup.happysalus.utility.StoreInMemory;

public class LoginService {

    private StoreInMemory storeInMemory;
    private User user;
    private TagUser tagUser;

    public LoginService(Context context){
        storeInMemory = new StoreInMemory(context);
        user = new User();
        tagUser = new TagUser();
    }

    public void login(int idClient){

        switch (idClient){
            case 1://Login tramite facebook
                storeInMemory.setValueIntToStore(tagUser.getKEY_LOGIN_FACEBOOK(), 1);
                break;
            case 2://Login tramite email
                storeInMemory.setValueIntToStore(tagUser.getKEY_LOGIN_EMAIL(), 1);
                break;
        }
    }

    public void logout(){
        storeInMemory.setValueIntToStore(tagUser.getKEY_LOGIN_FACEBOOK(), 0);
        storeInMemory.setValueIntToStore(tagUser.getKEY_LOGIN_EMAIL(), 0);
    }

    public boolean isLogged(){
        if(storeInMemory.getValueIntFromStore(tagUser.getKEY_LOGIN_FACEBOOK())!=0 || storeInMemory.getValueIntFromStore(tagUser.getKEY_LOGIN_EMAIL())!=0){
            return true;
        }else return false;
    }

    public void signup(User user){

        storeInMemory.setUser(user);
    }

    public boolean isSignup(){
        if(storeInMemory.getUser(1).getId()!=0){
            return true;
        }else return false;
    }

    public User getUser(){
        User user = new User();
        user = storeInMemory.getUser(1);
        return user;
    }

}
