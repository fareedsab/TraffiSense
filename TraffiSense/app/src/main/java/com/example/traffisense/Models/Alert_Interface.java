package com.example.traffisense.Models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface Alert_Interface {
    @Insert
    void insert(AlertModel model);
    @Query("INSERT INTO AlertModel (id,date,time,fire,traffic,weapon)" +
            " VALUES (:id,:date,:time,:fire,:traffic,:weapon)")
    void add(int id,String date,String time,String fire,String traffic,String weapon);
    @Query("SELECT * FROM AlertModel ORDER BY id DESC")
    List<AlertModel> getallalert();
    @Query("SELECT * FROM AlertModel WHERE date = :date ORDER BY id DESC")
    LiveData<List<AlertModel>> findalltasksbydate(String date);
    @Query("SELECT * FROM AlertModel WHERE date = :date AND (traffic!='0' OR fire != '0' OR weapon != '0') ORDER BY id DESC")
    LiveData<List<AlertModel>> findalltasksbydatenonempty(String date);
    @Query("SELECT * FROM AlertModel WHERE date = :date ORDER BY id DESC ")
    List<AlertModel> findtodaytask(String date);
    @Query("SELECT * FROM AlertModel WHERE date = :date AND (traffic!='0' OR fire != '0' OR weapon != '0') ORDER BY id DESC")
    List<AlertModel> findtodaytasknonempty(String date);
    @Query("SELECT EXISTS(SELECT * FROM AlertModel WHERE date= :date And time=:time)")
    Boolean is_exist(String date,String time);

    @Query("SELECT * FROM AlertModel WHERE date = :date And time = :time ORDER BY id DESC")
    AlertModel getAlertBytimeDate(String date,String time);

    @Query("SELECT * FROM AlertModel WHERE date BETWEEN  :startDate AND :endDate ORDER BY id DESC")
    List<AlertModel> getDataByDate(String startDate,String endDate);
    @Query("SELECT * FROM AlertModel WHERE date BETWEEN  :startDate AND :endDate ORDER BY id DESC")
    List<AlertModel> getFilteredDataByDate(String startDate,String endDate);
    @Query("UPDATE alertmodel SET isNotified = :status WHERE id = :task_id " )
    void updatenotify(int task_id,int status);
    @Query("UPDATE AlertModel SET fire= :fire , weapon= :weapon,traffic = :traffic, isNotified = :isnotified WHERE id = :task_id")
    void update(String fire,String weapon,String traffic,int isnotified,int task_id);
    @Query("DELETE FROM AlertModel")
    void deleteall();



}
