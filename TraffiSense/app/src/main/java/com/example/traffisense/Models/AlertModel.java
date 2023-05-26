package com.example.traffisense.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AlertModel {

    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "date")
    private String alertdate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ColumnInfo (name = "time")
    private String alerttime;
    @ColumnInfo (name = "fire")
    private String fire;
    public AlertModel()
    {}
    public AlertModel(int id, String alertdate, String alerttime, String fire, String traffic, String weopon, int isnotified) {
        this.id = id;
        this.alertdate = alertdate;
        this.alerttime = alerttime;
        this.fire = fire;
        this.traffic = traffic;
        this.weopon = weopon;
        this.isnotified = isnotified;
    }

    @ColumnInfo (name = "traffic")
    private String traffic;
    @ColumnInfo (name = "weapon")
    private String weopon;
    @ColumnInfo (name = "isNotified")
    private int isnotified;

    public int getIsnotified() {
        return isnotified;
    }

    public void setIsnotified(int isnotified) {
        this.isnotified = isnotified;
    }

    public String getAlerttime() {
        return alerttime;
    }

    public void setAlerttime(String alerttime) {
        this.alerttime = alerttime;
    }

    public String getFire() {
        return fire;
    }

    public void setFire(String fire) {
        this.fire = fire;
    }

    public String getTraffic() {
        return traffic;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }

    public String getWeopon() {
        return weopon;
    }

    public void setWeopon(String weopon) {
        this.weopon = weopon;
    }


    public AlertModel(String alertdate, String alerttime, String fire, String traffic, String weopon,int isNotified) {
        this.alertdate = alertdate;
        this.alerttime = alerttime;
        this.fire = fire;
        this.traffic = traffic;
        this.weopon = weopon;
        this.isnotified=isNotified;
    }

    public String getAlertdate() {
        return alertdate;
    }

    public void setAlertdate(String alertdate) {
        this.alertdate = alertdate;
    }

}
