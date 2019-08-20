package com.example.homework522;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;

public class AppConfig {
    private Context context;

    public static final String APP_PREFERENCES = "settings";

    public String FILE_AUTH = "file_settings";
    public String APP_LANG = "lang";
    public String APP_STORAGE = "storage";
    public String IS_LOGINNED = "loginned";

    public String FILE_NAME = "txt_reginfo.txt";

    public SharedPreferences mSettings;

    AppConfig(Activity activity){
        this.context = activity.getBaseContext();
    }

    public void createMainConfig(){
        //создали базовый файл с настройками
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = mSettings.edit();

        //получили базовые настройки языка пользователя и настройки хранения по умолчанию
        String lang = getLang();
        editor.putString(FILE_AUTH, FILE_NAME);
        editor.putString(APP_LANG, lang);
        editor.putBoolean(APP_STORAGE, getStorage());
        editor.putBoolean(IS_LOGINNED, isLoginned());
        editor.apply();

        Configuration config = context.getResources().getConfiguration();
        if (!config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }

    }
    //получаем базовые настройки приложения
    public void initBaseConfig(){
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String lang = mSettings.getString(APP_LANG, "");
      //  setLangRecreate(lang);
    }

    //получаем текущие настройки хранилища
    public String getFileSetting() {
        String value = "";
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(FILE_AUTH)) {
            value = mSettings.getString(FILE_AUTH, "");
        }
        return value;
    }
    //получаем текущие настройки языка
    public String getLang(){
        String value=context.getResources().getConfiguration().locale.getLanguage();
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mSettings.contains(APP_LANG)) {
            value = mSettings.getString(APP_LANG, "");
        }
        return value;
    }
    //получаем текущие настройки хранилища
    public boolean getStorage(){
        Boolean value=false;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mSettings.contains(APP_STORAGE)) {
            value = mSettings.getBoolean(APP_STORAGE, false);
        }
        return value;
    }
    //получаем текущие настройки хранилища
    public boolean isLoginned() {
        Boolean value = false;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(IS_LOGINNED)) {
            value = mSettings.getBoolean(IS_LOGINNED, false);
        }
        return value;
    }

    //устанавливаем хранилище в настройках
    public void setStorage(Boolean value){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_STORAGE, value);
        editor.apply();
    }
    //устанавливаем статус зарегистрированного
    public void setLoginned(Boolean value) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(IS_LOGINNED, value);
        editor.apply();
    }
    //устанавливаем язык в настройках
    public void setLang(String value){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_LANG, value);
        editor.apply();
    }
    //устанавливаем язык
    public void setLangRecreate(String value) {
        Configuration config = context.getResources().getConfiguration();
        Locale locale = new Locale(value);
        Locale.setDefault(locale);
        config.locale = locale;
        Toast.makeText(context,value, Toast.LENGTH_SHORT).show();
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

}
