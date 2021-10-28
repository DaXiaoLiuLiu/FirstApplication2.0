package com.example.myapplication1.ui.notifications.User;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication1.MainActivity;
import com.example.myapplication1.R;
import com.example.myapplication1.ThreadPool;
import com.example.myapplication1.network.Info.LoginNetwork;
import com.example.myapplication1.network.Info.entity.User;
import com.example.myapplication1.network.Info.entity.UserResponse;
import com.tencent.mmkv.MMKV;

public class LoginActivity extends AppCompatActivity{
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private EditText accountEdit;
    private EditText passwordEdit;
    private TextView registerBtn;
    private TextView forgetBtn;
    private TextView contactBtn;
    private Button loginBtn;
    private ProgressBar progressBar;

    private CheckBox rememberPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MMKV.initialize(this);

        setContentView(R.layout.activity_login2);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();

        }

        accountEdit = (EditText) findViewById(R.id.et_mobile);
        passwordEdit = (EditText) findViewById(R.id.et_password);
        loginBtn = (Button) findViewById(R.id.btn_login);
        progressBar = (ProgressBar)findViewById(R.id.login_progress);
        registerBtn = (TextView)findViewById(R.id.regist);

        if(progressBar.getVisibility() == View.VISIBLE){
            progressBar.setVisibility(View.GONE);
        }

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(accountEdit.getText().toString(),
                        passwordEdit.getText().toString());
                if(progressBar.getVisibility() == View.GONE){
                    progressBar.setVisibility(View.VISIBLE);
                }

                ThreadPool.getExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        UserResponse responseResult = LoginNetwork.sign_in(user);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(responseResult == null){

                                    Toast.makeText(getApplicationContext(),"网络连接错误~！",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    if(responseResult.getStatus()){
                                        MMKV.defaultMMKV().encode("name",user.getUserName());
                                        MMKV.defaultMMKV().encode("password",user.getPassword());

                                        Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();

                                        try {//线程休眠，不然太快了
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        if(progressBar.getVisibility() == View.VISIBLE){
                                            progressBar.setVisibility(View.GONE);
                                        }
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        LoginActivity.this.finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),responseResult.getMessage() + ": 账号或密码错误",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if(progressBar.getVisibility() == View.VISIBLE){
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });

                    }
                });

            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });

    }
}