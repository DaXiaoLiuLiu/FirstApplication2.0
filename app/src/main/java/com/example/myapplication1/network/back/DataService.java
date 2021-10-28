package com.example.myapplication1.network.back;
/*
 * @Author Lxf
 * @Date 2021/8/5 10:37
 * @Description 用于发出手机警报的后台 服务程序
 * @Since version-1.0
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.myapplication1.Dao.DLog;
import com.example.myapplication1.Dao.LogDao;
import com.example.myapplication1.Dao.LogDataBase;
import com.example.myapplication1.MainActivity;
import com.example.myapplication1.R;
import com.example.myapplication1.Repository;
import com.example.myapplication1.ThreadPool;
import com.example.myapplication1.network.home.ValueResponse;

import java.util.Date;

import static java.lang.Thread.sleep;

public class DataService extends Service {

    private Boolean ring  = true;//用于指示发出危险报警

    public DataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
         return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("DataService", "前台服务", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(this, "DataService")
                .setContentTitle("智能安全监护系统")
                .setContentText("正在运行中")
                .setSmallIcon(R.drawable.small_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.large_icon))
                .setContentIntent(pi)
                .build();
        startForeground(1, notification);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ThreadPool.getExecutor().execute(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        sleep(2000);//2秒钟发出一个网络请求,以更新数据
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(Repository.setValue()){//成功接收值的情况
                        Log.d("DataService","服务正在更新 ");
                        //通过仓库类，获取网络返回值

                        ValueResponse valueResponse = Repository.getValue();
                        //处理返回的参数值
                        if(valueResponse == null) {
                            Log.d("DataService","Repository.getValue() is null");
                            continue;
                        }

                        int tmp,smoke,status;
                        tmp = valueResponse.getTemperatureStatus();
                        smoke = valueResponse.getSmokeStatus();
                        status = valueResponse.getViewStatus();

                        if (status == 0 || smoke == 0 || tmp > 40 || tmp < 0) {
                            if (ring) {//危险的情况，手机需要发出震动，但只震动一次
                                VibratorHelper.Vibrate(DataService.this, 1000);
                                VibratorHelper.Warning(DataService.this);
                                ring = false;
                            }
                            Log.d("DataService","tmp is " + tmp);
                            Log.d("DataService","smoke is " + smoke);
                            Log.d("DataService","status is " + status);
                            //使用SQL-Lite记录危险信息.将危险信息加入到数据库中
                            LogDao logDao = LogDataBase.getInstance(getApplicationContext()).logDao();
                            Date now = new Date(System.currentTimeMillis());
                            DLog log = new DLog(now,tmp,smoke,status);
                            logDao.insert(log);

                        }
                        else {//正常的情况
                            ring = true;
                        }
                    }else {//值接收失败
                        Log.d("DataService","Repository result is empty : " );
                        //Toast.makeText(MyApplication.getContext(),"网络连接错误",Toast.LENGTH_LONG).show();
                    }

                }//接收并更新参数值

            }//循环
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}


