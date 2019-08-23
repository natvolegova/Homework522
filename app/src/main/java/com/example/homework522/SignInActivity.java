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

public class SignInActivity extends AppCompatActivity {
    private Button btnSignin;
    private Button btnSignup;
    private TextView txtSignup;
    private TextView txtSignin;
    private LinearLayout layoutSignin;
    private LinearLayout layoutSignup;
    private FileHelper fh;
    private AppConfig config;
    private Intent intent;
    private String filename;
    private Boolean curStorage;
    private EditText etLogin;
    private EditText etPass;
    private EditText etLoginUp;
    private EditText etPassUp;

    public static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 11; //пользовательская переменная, определяем права доступа на запись

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = new AppConfig(SignInActivity.this);
        config.createMainConfig();
        fh = new FileHelper(SignInActivity.this);
        intent = new Intent(SignInActivity.this, MainActivity.class);

        //проверяем, авторизован ли пользователь
        if (config.isLoginned()) {
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_sign_in);
            initView();

            filename = config.getFileSetting();
            curStorage = config.getStorage();

            //авторизация
            btnSignin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String login = etLogin.getText().toString();
                    String pass = etPass.getText().toString();
                    String curData = "";
                    if (!login.equals("") && !pass.equals("")) {
                        //получаем регистрационные данные из файла, проверяем где находится файл регистрации
                        if (curStorage) {
                            //внешнее хранилище
                            curData = fh.getExternalValue(filename);
                        } else {
                            //внутреннее хранилище
                            curData = fh.getInternalValue(filename);
                        }
                        //проверяем, есть ли файл с данными и пользователь ранее регистрировался
                        if (!curData.equals("")) {
                            String[] curValue = curData.split(";");
                            //проверяем введенные данные и сравниваем их с данными файла
                            if (login.equals(curValue[0]) && pass.equals(curValue[1])) {
                                startActivity(intent);
                                config.setLoginned(true);
                            } else {
                                Toast.makeText(SignInActivity.this, getResources().getString(R.string.msg_error_signin), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            //учетная запись не найдена
                            Toast.makeText(SignInActivity.this, getResources().getString(R.string.msg_error_noregister), Toast.LENGTH_LONG).show();
                        }
                        etLogin.setText("");
                        etPass.setText("");
                    } else {
                        Toast.makeText(SignInActivity.this, getResources().getString(R.string.msg_error_signup), Toast.LENGTH_LONG).show();
                    }
                }
            });
            //регистрация
            btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String loginUp = etLoginUp.getText().toString();
                    String passUp = etPassUp.getText().toString();
                    if (!loginUp.equals("") && !passUp.equals("")) {
                        if (curStorage) {
                            //внешнее хранилище
                            int permissionStatus = ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                                // fh.createFileRegExternal(filename);
                                fh.updateExternalValue(filename, loginUp, passUp);
                            } else {
                                ActivityCompat.requestPermissions(SignInActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_WRITE_STORAGE);
                            }
                        } else {
                            //внутреннее хранилище
                            fh.updateInternalValue(filename, loginUp, passUp);
                        }
                        config.setLoginned(true);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignInActivity.this, getResources().getString(R.string.msg_error_signup), Toast.LENGTH_LONG).show();
                    }
                    etLoginUp.setText("");
                    etPassUp.setText("");
                }
            });
            //ссылка для открытия layout регистрации
            txtSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutSignup.setVisibility(View.VISIBLE);
                    layoutSignin.setVisibility(View.GONE);
                }
            });
            //ссылка для открытия layout авторизации
            txtSignin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutSignin.setVisibility(View.VISIBLE);
                    layoutSignup.setVisibility(View.GONE);
                }
            });

        }
    }

    private void initView() {
        btnSignin = findViewById(R.id.btn_signin);
        btnSignup = findViewById(R.id.btn_signup);
        txtSignup = findViewById(R.id.txt_signup);
        txtSignin = findViewById(R.id.txt_signin);
        layoutSignin = findViewById(R.id.layout_signin);
        layoutSignup = findViewById(R.id.layout_signup);
        etLogin = findViewById(R.id.et_login);
        etPass = findViewById(R.id.et_pass);
        etLoginUp = findViewById(R.id.et_login_up);
        etPassUp = findViewById(R.id.et_pass_up);
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
