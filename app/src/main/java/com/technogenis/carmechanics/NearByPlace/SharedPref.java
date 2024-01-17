package com.technogenis.carmechanics.NearByPlace;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private static final String NAME_PREF="my_app_pref";

    private static SharedPref sharedPref;
    private final SharedPreferences sharedPreferences;

    private static final String KEY_IS_LOGIN="user_login";
    public static final String KEY_USER_NAME="user_name";
    public static final String KEY_USER_EMAIL="user_email";
    public static final String KEY_IS_FIRST_RUN="first_run";

    public static SharedPref getInstance(Context context) {
        if (sharedPref == null) {
            sharedPref = new SharedPref(context);
        }
        return sharedPref;
    }

    private SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences(NAME_PREF,Context.MODE_PRIVATE);
    }

    public void saveData(String key,String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public String getData(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    public void setIsLogin(boolean isLogin) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(KEY_IS_LOGIN, isLogin);
        prefsEditor.commit();
    }

    public boolean getIsLogin() {
        return sharedPreferences.getBoolean(KEY_IS_LOGIN, false );
    }




}
