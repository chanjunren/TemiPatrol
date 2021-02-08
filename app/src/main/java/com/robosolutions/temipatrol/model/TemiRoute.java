package com.robosolutions.temipatrol.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.robosolutions.temipatrol.db.TypeConverter;

import java.util.ArrayList;

@Entity(tableName = "routeTable")
public class TemiRoute {
    @PrimaryKey(autoGenerate = true)
    private int routeIdx;
    @ColumnInfo(name="routeTitle")
    private String routeTitle;
    @TypeConverters(TypeConverter.class)
    private ArrayList<String> destinations;

    public TemiRoute(String routeTitle, ArrayList<String> destinations) {
        this.routeTitle = routeTitle;
        this.destinations = destinations;
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
}
