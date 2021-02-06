package com.ongbengchia.temipatrol.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "routeTable")
public class TemiRoute {
    @PrimaryKey(autoGenerate = true)
    private int routeIdx;
    @ColumnInfo(name="routeTitle")
    private String routeTitle;
    private ArrayList<String> destinations;

    public TemiRoute(String routeTitle, ArrayList<String> destinations) {
        this.routeTitle = routeTitle;
        this.destinations = destinations;
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
