package com.robosolutions.temipatrol.temi;

import android.util.Log;

import com.robosolutions.temipatrol.model.TemiRoute;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

import java.util.ArrayList;
import java.util.List;

public class TemiController {
    private final String TAG = "TemiController";
    private TemiNavigator temiNavigator;
    private Robot temiRobot;

    public TemiController() {
        temiRobot = Robot.getInstance();
        temiNavigator = new TemiNavigator(temiRobot);
    }

    public ArrayList<String> getLocationsFromTemi() {
        // Place holder for testing
        ArrayList<String> temp = new ArrayList<>();
        temp.add("location1");
        temp.add("location2");
        temp.add("location3");
        temp.add("location4");

        List<String> temiLocations = temiRobot.getLocations();

        return !temiLocations.isEmpty() ? (ArrayList<String>) temiLocations: temp;
    }

    public void patrolRoute(TemiRoute route) {
        TemiNavigator temiNavigator = new TemiNavigator(temiRobot);
        temiNavigator.patrolRoute(route);
    }

    public void temiSpeak(String speechText) {
        TtsRequest ttsReqObj = TtsRequest.create(speechText, false);
        temiRobot.speak(ttsReqObj);
    }

    public void pausePatrol() {
        temiNavigator.pausePatrol();
    }
}
