package com.robosolutions.temipatrol.temi;

import com.robosolutions.temipatrol.viewmodel.GlobalViewModel;
import com.robotemi.sdk.Robot;

import java.util.ArrayList;

public class TemiController {
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
        return temp;
//        return (ArrayList<String>) temiRobot.getLocations();
    }
}
