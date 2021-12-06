package com.example.myapplication1.Dao;
/*
 * @Author Lxf
 * @Date 2021/8/30 23:19
 * @Description Room创建dataBase的较为标准示例
 * @Since version-1.0
 */

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {DLog.class},version = 1,exportSchema = false)
@TypeConverters({Converters.class})
public abstract class LogDataBase extends RoomDatabase {

    private static LogDataBase INSTANCE;
    private static final Object sLock = new Object();//同步锁
    public abstract LogDao logDao();


    public static LogDataBase getInstance(Context context){
        synchronized (sLock){
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        LogDataBase.class,"log_database").build();
            }
            return INSTANCE;
        }
    }
}
