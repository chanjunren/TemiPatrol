package com.robosolutions.temipatrol.temi;

import android.util.Log;

import com.robosolutions.temipatrol.model.TemiRoute;
import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

import java.util.ArrayList;

public class TemiController {
    private final String TAG = "TemiController";
    private GlobalViewModel viewModel;
    private Robot temiRobot;

    public TemiController() {
        temiRobot = Robot.getInstance();
    }

    public ArrayList<String> getLocationsFromTemi() {
        ArrayList<String> temp = new ArrayList<>();
        temp.add("location1");
        temp.add("location2");
        temp.add("location3");
        temp.add("location4");

        ArrayList<String> temiLocations = (ArrayList<String>) temiRobot.getLocations();

        return temiLocations != null ? temiLocations: temp;
    }

    public void patrolRoute(TemiRoute route) {
        TemiNavigator temiNavigator = new TemiNavigator(temiRobot);
        temiNavigator.patrolRoute(route);
    }

}
