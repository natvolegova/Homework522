package com.example.homework522;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    public static final String FILE_REG = "txt_reginfo.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppConfig config = new AppConfig(SignInActivity.this);
        config.createMainConfig();
        config.initBaseConfig();
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
        fh = new FileHelper(SignInActivity.this, FILE_REG);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        switch (view.getId()) {
            //авторизация
            case R.id.btn_signin:
                EditText etLogin = findViewById(R.id.et_login);
                EditText etPass = findViewById(R.id.et_pass);
                String login = etLogin.getText().toString();
                String pass = etPass.getText().toString();
                if (!login.equals("") && !pass.equals("")) {
                    //получаем регистрационные данные из файла
                    String[] curvalue = fh.getValue().split(";");
                    if (login.equals(curvalue[0]) && pass.equals(curvalue[1])) {
                        startActivity(intent);
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
                String loginup = etLoginUp.getText().toString();
                String passup = etPassUp.getText().toString();
                if (!loginup.equals("") && !passup.equals("")) {
                    fh.updateValue(loginup, passup);
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
}
