package com.elsoudany.said.tripreminderapp.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "trips")

public class Trip implements Serializable {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "trip_name")
    public String tripName;
    @ColumnInfo(name = "start_point")
    public String startPoint;
    @ColumnInfo(name = "end_point")
    public String endPoint;
    @ColumnInfo(name = "date")
    public String date;
    @ColumnInfo(name = "time")
    public String time;
    public String userId;
    public String status;
    public Trip(){

    }
    @Ignore
    public Trip(String tripName, String startPoint, String endPoint, String date, String time, String userId,String status) {
        this.tripName = tripName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.date = date;
        this.time = time;
        this.userId = userId;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripName='" + tripName + '\'' +
                ", startPoint='" + startPoint + '\'' +
                ", endPoint='" + endPoint + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
