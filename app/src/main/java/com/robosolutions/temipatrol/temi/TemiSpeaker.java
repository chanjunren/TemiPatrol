package com.robosolutions.temipatrol.temi;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

import java.util.ArrayList;
import java.util.List;

public class TemiSpeaker {
    private final String TAG = "TemiSpeaker";
    private Robot temiRobot;
    private TtsRequest currentRequest;

    public TemiSpeaker(Robot temiRobot) {
        this.temiRobot = temiRobot;
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

    public void temiSpeak(String speechText) {
        TtsRequest ttsReqObj = TtsRequest.create(speechText, false);
        temiRobot.speak(ttsReqObj);
    }
}
