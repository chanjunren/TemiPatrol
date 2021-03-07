package com.robosolutions.temipatrol.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.robosolutions.temipatrol.db.TypeConverter;

import java.util.ArrayList;

@Entity(tableName = "routeTable")
public class TemiRoute {
    private static final String TAG = "TemiRoute";
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int routeIdx;
    @ColumnInfo(name="routeTitle")
    private String routeTitle;
    @ColumnInfo(name="patrolCount")
    private int patrolCount;
    @TypeConverters(TypeConverter.class)
    private ArrayList<String> destinations;

    public TemiRoute(String routeTitle, ArrayList<String> destinations, int patrolCount) {
        this.routeTitle = routeTitle;
        this.destinations = destinations;
        this.patrolCount = patrolCount;
    }

    public int getRouteIdx() {
        return routeIdx;
    }

    public void setRouteIdx(int routeIdx) {
        this.routeIdx = routeIdx;
    }

    public String getRouteTitle() {
        return routeTitle;
    }

    public void setRouteTitle(String routeTitle) {
        this.routeTitle = routeTitle;
    }

    public ArrayList<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(ArrayList<String> destinations) {
        this.destinations = destinations;
    }

    public int getPatrolCount() {
        return patrolCount;
    }

    public void setPatrolCount(int patrolCount) {
        this.patrolCount = patrolCount;
    }

    @Override
    public String toString() {
        return "TemiRoute{" +
                "routeIdx=" + routeIdx +
                ", routeTitle='" + routeTitle + '\'' +
                ", destinations=" + destinations.toString() +
                '}';
    }
}
