package com.example.homework522;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtLang;
    private Switch swStorageLogin;
    int currentItem = 0;
    private AppConfig config;
    String curlang;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = new AppConfig(SettingsActivity.this);
        setContentView(R.layout.activity_settings);

        initView();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //показ окна для изменения языка
        txtLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLangDialog();
            }
        });
    }

    private void initView() {
        context = getBaseContext();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.name_settings));
        txtLang = findViewById(R.id.txt_lang);
        swStorageLogin = findViewById(R.id.sw_storage_login);
        curlang = config.getLang();
        String[] langSetting= getResources().getStringArray(R.array.lang);;
      //  Toast.makeText(SettingsActivity.this, curlang, Toast.LENGTH_SHORT).show();
        //устанавливаем текущее значение языка
        switch (curlang){
            case "en":
                txtLang.setText(langSetting[0]);
                currentItem = 0;
                break;
            case "ru":
                txtLang.setText(langSetting[1]);
                currentItem = 1;
                break;
        }
    }
    //показываем диалоговое окно для изменения языка
    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.language_dialog, null);
        dialogBuilder.setView(dialogView);

        final Spinner spinner1 = (Spinner) dialogView.findViewById(R.id.spinner1);
        spinner1.setSelection(currentItem);

        dialogBuilder.setTitle(getResources().getString(R.string.btn_changename));
        dialogBuilder.setPositiveButton(R.string.change, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int langpos = spinner1.getSelectedItemPosition();
                switch(langpos) {
                    case 0: //English
                        config.setLang("en");
                        config.setLangRecreate("en");
                    //    recreate();
                        return;
                    case 1: //Russian
                        config.setLang("ru");
                        config.setLangRecreate("ru");
                  //      recreate();
                        return;
                    default: //By default set to english
                        config.setLang("en");
                        config.setLangRecreate("en");
                      //  recreate();
                        return;
                }



            }
        });
        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }



}
