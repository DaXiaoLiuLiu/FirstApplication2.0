package com.example.myapplication1.network.home;
/*
 * @Author Lxf
 * @Date 2021/8/10 20:06
 * @Description 用于UI更新的工具类，属于历史遗留类，后续可能需要改进
 * @Since version-1.0
 */

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapplication1.R;

public class ValueHelper {
    public static final int UNSAFE = 1;
    public static final int SAFE = 0;
    public static final int TVALUE_1 = 2;//指示温度传感器,出现了危险状态
    public static final int TVALUE_2 = 3;//指示温度传感器
    public static final int SVALUE_1 = 4;//指示烟雾传感器，出现危险
    public static final int SVALUE_2 = 5;//指示烟雾传感器
    private final String colorSafe = "#009933";
    private final String colorDanger = "#990033";

    private ImageView imageView;
    private TextView textView, textView_t, textView_s;
    private  Activity valueActivity;

    public void setValueActivity(Activity activity){
        valueActivity = activity;
    }

    public Activity getValueActivity(){
        return valueActivity;
    }


    public  void StatusCall(int status) {
        imageView = getValueActivity().findViewById(R.id.imageView5);
        textView = getValueActivity().findViewById(R.id.textView);

        Boolean flags;
        //这里接收传感器数据和识别
        if (status == 0) flags = true;
        else flags = false;

        if (flags) {//flags == true即出现了不安全的情况
            imageView.setImageResource(R.drawable.danger);
            textView.setText("不安全状态");
            textView.setTextColor(Color.parseColor(colorDanger));
            //下面是利用通知发起警告
        } else {
            imageView.setImageResource(R.drawable.safe);
            textView.setText("安全状态");
            textView.setTextColor(Color.parseColor(colorSafe));

        }
    }

    public void ValueCall(int tmp, int smoke) {
        textView_t = getValueActivity().findViewById(R.id.textView_1);
        textView_s = getValueActivity().findViewById(R.id.textView_2);

        //s即烟雾报警器，0表示危险，1表示安全
        if (tmp >= 35 || tmp < 5) {//不安全状态
            textView_t.setText(String.valueOf(tmp) + "℃");
            textView_t.setTextColor(Color.parseColor(colorDanger));
        } else {
            textView_t.setText(String.valueOf(tmp) + "℃");
            textView_t.setTextColor(Color.parseColor(colorSafe));
        }

        if (smoke == 0) {//危险状态
            textView_s.setText("异常");
            textView_s.setTextColor(Color.parseColor(colorDanger));
        } else {
            textView_s.setText("安全");
            textView_s.setTextColor(Color.parseColor(colorSafe));
        }
    }

    //封装的UI更新方法
    public void UiChange(ValueResponse value){
        //获取数值
        int tmp,smoke,status;
        tmp = value.getTemperatureStatus();
        smoke = value.getSmokeStatus();
        status = value.getViewStatus();
        Log.d("ValueHelper","tmp is " + tmp);
        Log.d("ValueHelper","UIchange");

        StatusCall(status);
        ValueCall(tmp,smoke);

    }


}
