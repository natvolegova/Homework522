package com.example.homework522;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtLang;
    private TextView txtLogout;
    private Switch swStorageLogin;
    int currentItem = 0;
    private AppConfig config;
    String curlang;
    Context context;
    public static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 11; //пользовательская переменная, определяем права доступа на запись


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
        //изменение настроек хранилища
        swStorageLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                config.setStorage(b);
                changeStorage(b);
            }
        });
        //выход из приложения
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, SignInActivity.class);
                config.setLoginned(false);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        context = getBaseContext();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.name_settings));
        txtLang = findViewById(R.id.txt_lang);
        txtLogout = findViewById(R.id.txt_logout);
        swStorageLogin = findViewById(R.id.sw_storage_login);

        curlang = config.getLang();
        String[] langSetting = getResources().getStringArray(R.array.lang);
        //устанавливаем текущее значение языка
        switch (curlang) {
            case "en":
                txtLang.setText(langSetting[0]);
                currentItem = 0;
                break;
            case "ru":
                txtLang.setText(langSetting[1]);
                currentItem = 1;
                break;
        }
        Boolean curStorage = config.getStorage();
        if (curStorage) {
            swStorageLogin.setChecked(true);
        }
    }

    //показываем диалоговое окно для изменения языка
    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.language_dialog, null);
        dialogBuilder.setView(dialogView);

        final Spinner spinner1 = dialogView.findViewById(R.id.spinner1);
        spinner1.setSelection(currentItem);
        dialogBuilder.setTitle(getResources().getString(R.string.btn_changename));
        dialogBuilder.setPositiveButton(R.string.change, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int langpos = spinner1.getSelectedItemPosition();
                switch (langpos) {
                    case 0: //Английский
                        config.setLang("en");
                        config.setLangRecreate("en");
                        return;
                    case 1: //Русский
                        config.setLang("ru");
                        config.setLangRecreate("ru");
                        return;
                    default: //по умолчанию
                        config.setLang("en");
                        config.setLangRecreate("en");
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

    //изменение настроек хранилища
    public void changeStorage(Boolean b) {
        FileHelper fh = new FileHelper(SettingsActivity.this);
        String filename = config.getFileSetting();
        String[] curValue = new String[2];
        if (b) {
            //перемещаем во внешнее хранилище
            curValue = fh.getInternalValue(filename).split(";");
            int permissionStatus = ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                fh.updateExternalValue(filename, curValue[0], curValue[1]);
                fh.deleteInternalFile(filename);
            } else {
                ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_WRITE_STORAGE);
            }
        } else {
            //перемещаем во внутреннее хранилище
            curValue = fh.getExternalValue(filename).split(";");
            fh.updateInternalValue(filename, curValue[0], curValue[1]);
            fh.deleteExternalFile(filename);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_WRITE_STORAGE: //пользовательская переменная, доступ на запись
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, getResources().getString(R.string.msg_request_denied), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
