package com.example.homework522;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnSignin;
    private Button btnSignup;
    private TextView txtSignup;
    private TextView txtSignin;
    private LinearLayout layoutSignin;
    private LinearLayout layoutSignup;
    private FileHelper fh;
    private AppConfig config;

    public static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 11; //пользовательская переменная, определяем права доступа на запись

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = new AppConfig(SignInActivity.this);
        config.createMainConfig();
        fh = new FileHelper(SignInActivity.this);

        setContentView(R.layout.activity_sign_in);
        initView();
    }

    private void initView() {
        btnSignin = findViewById(R.id.btn_signin);
        btnSignup = findViewById(R.id.btn_signup);
        txtSignup = findViewById(R.id.txt_signup);
        txtSignin = findViewById(R.id.txt_signin);
        btnSignin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        txtSignup.setOnClickListener(this);
        txtSignin.setOnClickListener(this);
        layoutSignin = findViewById(R.id.layout_signin);
        layoutSignup = findViewById(R.id.layout_signup);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        String filename = config.getFileSetting();
        Boolean curStorage = config.getStorage();

        switch (view.getId()) {
            //авторизация
            case R.id.btn_signin:
                EditText etLogin = findViewById(R.id.et_login);
                EditText etPass = findViewById(R.id.et_pass);
                String login = etLogin.getText().toString();
                String pass = etPass.getText().toString();
                String[] curValue = new String[2];
                if (!login.equals("") && !pass.equals("")) {
                    //получаем регистрационные данные из файла, проверяем где находится файл регистрации
                    if (curStorage) {
                        //внешнее хранилище
                        curValue = fh.getExternalValue(filename).split(";");
                    } else {
                        //внутреннее хранилище
                        curValue = fh.getInternalValue(filename).split(";");
                    }
                    //проверяем введенные данные и сравниваем их с данными файла
                    if (login.equals(curValue[0]) && pass.equals(curValue[1])) {
                        startActivity(intent);
                        config.setLoginned(true);
                    } else {
                        Toast.makeText(SignInActivity.this, getResources().getString(R.string.msg_error_signin), Toast.LENGTH_LONG).show();
                    }
                    etLogin.setText("");
                    etPass.setText("");
                } else {
                    Toast.makeText(SignInActivity.this, getResources().getString(R.string.msg_error_signup), Toast.LENGTH_LONG).show();
                }
                break;
            //регистрация
            case R.id.btn_signup:
                EditText etLoginUp = findViewById(R.id.et_login_up);
                EditText etPassUp = findViewById(R.id.et_pass_up);
                String loginUp = etLoginUp.getText().toString();
                String passUp = etPassUp.getText().toString();
                if (!loginUp.equals("") && !passUp.equals("")) {
                    if (curStorage) {
                        //внешнее хранилище
                        int permissionStatus = ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                            fh.createFileRegExternal(filename);
                        } else {
                            ActivityCompat.requestPermissions(SignInActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_WRITE_STORAGE);
                        }
                        fh.updateExternalValue(filename, loginUp, passUp);
                    } else {
                        //внутреннее хранилище
                        fh.createFileRegInternal(filename);
                        fh.updateInternalValue(filename, loginUp, passUp);
                    }
                    config.setLoginned(true);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignInActivity.this, getResources().getString(R.string.msg_error_signup), Toast.LENGTH_LONG).show();
                }
                etLoginUp.setText("");
                etPassUp.setText("");
                break;
            //переключаем экраны для показа формы авторизации или регистрации
            case R.id.txt_signup:
                layoutSignup.setVisibility(View.VISIBLE);
                layoutSignin.setVisibility(View.GONE);
                break;
            case R.id.txt_signin:
                layoutSignin.setVisibility(View.VISIBLE);
                layoutSignup.setVisibility(View.GONE);
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        String actualFile = config.getFileSetting();
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_WRITE_STORAGE: //пользовательская переменная, доступ на запись
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fh.createFileRegExternal(actualFile);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.msg_request_denied), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
