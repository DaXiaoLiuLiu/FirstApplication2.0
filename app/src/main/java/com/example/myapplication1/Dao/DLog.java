package com.example.myapplication1.Dao;
/*
 * @Author Lxf
 * @Date 2021/8/30 20:19
 * @Description 数据库操作实体类
 * @Since version-1.0
 */

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class DLog {
    @PrimaryKey//自动生出主键
    private Date time;
    private int temperature;
    private int smoke;
    private int view;

    public DLog(Date time, int temperature, int smoke, int view) {
        this.time = time;
        this.temperature = temperature;
        this.smoke = smoke;
        this.view = view;
    }

    public Date getTime() {
        return time;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getSmoke() {
        return smoke;
    }

    public int getView() {
        return view;
    }
}
