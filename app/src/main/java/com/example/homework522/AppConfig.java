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
    public String APP_LANG ="lang";
    public String APP_STORAGE ="storage";
    public SharedPreferences mSettings;

    AppConfig(Activity activity){
        this.context = activity.getBaseContext();
    }
    public void createMainConfig(){
        //создали базовый файл с настройками
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = mSettings.edit();
        //получили базовые настройки языка пользователя и записали в настройки языка приложения
        String lang = mSettings.getString(APP_LANG, "");
        Toast.makeText(context,lang, Toast.LENGTH_SHORT).show();
        if(lang.equals("")){
            lang = context.getResources().getConfiguration().locale.getLanguage();
            editor.putString(APP_LANG, lang);
        }
        editor.putString(APP_STORAGE,"internal");
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
    //получаем текущие настройки языка
    public String getLang(){
        String value="";
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mSettings.contains(APP_LANG)) {
            value = mSettings.getString(APP_LANG, "");
        }
        return value;
    }
    //получаем текущие настройки хранилища
    public String getStorage(){
        String value="";
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mSettings.contains(APP_STORAGE)) {
            value = mSettings.getString(APP_STORAGE, "");
        }
        return value;
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
