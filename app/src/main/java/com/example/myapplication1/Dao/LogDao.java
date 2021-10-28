package com.example.myapplication1.Dao;
/*
 * @Author Lxf
 * @Date 2021/8/30 21:29
 * @Description Dao 用来查询数据
 * @Since version-1.0
 */

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface LogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     void insert(DLog DLog);//增加危险记录

    @Delete
    void delete(DLog DLog);//删除危险记录（少用）

    @Query("SELECT * FROM DLog")
    List<DLog> selectAll();

    @Query("SELECT * FROM DLog WHERE time = :date")
    List<DLog> selectByTime(Date date);// 按时间来找



}
