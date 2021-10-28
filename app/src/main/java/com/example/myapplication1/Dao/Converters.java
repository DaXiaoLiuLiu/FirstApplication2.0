package com.example.myapplication1.Dao;
/*
 * @Author Lxf
 * @Date 2021/8/31 10:59
 * @Description 转换器，用于数据库的数据类型和程序数据类型的转换,
 * 同时还加入了Data的转换方法
 * @Since version-1.0
 */

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date LongToDate(Long value){//将数据库中的字段，转为Date类型
        if(value != null){
            return new Date(value);
        }
        else return null;
    }

    @TypeConverter
    public static Long DateToLong(Date date){
        if (date != null) return date.getTime();
        else return null;
    }

    //转换date的方法,将date转成人看得懂的格式
    public static String DateToString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s_date = simpleDateFormat.format(date);
        return s_date;
    }
}
