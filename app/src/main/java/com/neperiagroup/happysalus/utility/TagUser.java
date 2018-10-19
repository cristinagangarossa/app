package com.neperiagroup.happysalus.utility;

public class TagUser {

    private static final String TAG = TagUser.class.getName();

    private String KEY_USER_ID = TAG + ".id_user";
    private String KEY_LOGIN_FACEBOOK = TAG + ".login_facebook";
    private String KEY_LOGIN_EMAIL = TAG + ".login_email";
    private String KEY_USER_HEIGHT = TAG + ".height_user";
    private String KEY_USER_WEIGHT = TAG + ".weight_user";
    private String KEY_USER_AGE = TAG + ".age";
    private String KEY_USER_BIRTHDAY = TAG + ".birthday_user";
    private String KEY_USER_SEX = TAG + ".sex_user";
    private String KEY_USER_BIND = TAG + ".user_bind";

    public TagUser(){}

    public String getKEY_USER_ID() {
        return KEY_USER_ID;
    }

    public String getKEY_LOGIN_FACEBOOK() {
        return KEY_LOGIN_FACEBOOK;
    }

    public String getKEY_LOGIN_EMAIL() {
        return KEY_LOGIN_EMAIL;
    }

    public String getKEY_USER_HEIGHT() {
        return KEY_USER_HEIGHT;
    }

    public String getKEY_USER_WEIGHT() {
        return KEY_USER_WEIGHT;
    }

    public String getKEY_USER_AGE() {
        return KEY_USER_AGE;
    }

    public String getKEY_USER_BIRTHDAY() {
        return KEY_USER_BIRTHDAY;
    }

    public String getKEY_USER_SEX() {
        return KEY_USER_SEX;
    }

    public String getKEY_USER_BIND() {
        return KEY_USER_BIND;
    }
}
