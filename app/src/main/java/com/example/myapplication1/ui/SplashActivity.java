package com.example.myapplication1.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication1.MainActivity;
import com.example.myapplication1.Repository;
import com.example.myapplication1.ThreadPool;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏融入背景
/*        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/

        ThreadPool.getExecutor().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //这里可以执行耗时操作
                Repository.setValue();

                //马上启动主界面
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        });
    }
}