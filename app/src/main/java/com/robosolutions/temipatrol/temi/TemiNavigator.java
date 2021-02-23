package com.robosolutions.temipatrol.temi;

import android.util.Log;

import com.robosolutions.temipatrol.model.TemiRoute;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

import java.util.ArrayList;

public class TemiNavigator implements OnGoToLocationStatusChangedListener {
    private final String TAG = "TemiNavigator";
    private Robot robot;
    private boolean hasArrived;

    public TemiNavigator(Robot robot) {
        this.robot = robot;
        hasArrived = false;
        robot.addOnGoToLocationStatusChangedListener(this);
    }

    public synchronized void patrolRoute(TemiRoute route) {
        try {
            ArrayList<String> destinations = route.getDestinations();
            for (int i = 0; i < destinations.size(); i++) {
                Log.i(TAG, "Going to " + destinations.get(i) + " ...");
                robot.goTo(destinations.get(i));
                hasArrived = false;
                while(!hasArrived) {
                    Log.i(TAG, "Waiting...");
                    this.wait();
                }
                Log.i(TAG, "Continuing...");
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "Interrupted exception while patrolling: " + e.toString());
        } finally {
            Log.i(TAG, "Patrol completed");
        }
    }

    @Override
    public void onGoToLocationStatusChanged(String location, String status) {
        if (status.equals(OnGoToLocationStatusChangedListener.COMPLETE)) {
            continueToNextDestination();
        } else if (status.equals(OnGoToLocationStatusChangedListener.ABORT)) {
            Log.i(TAG, "Status abort");
        } else if (status.equals(OnGoToLocationStatusChangedListener.CALCULATING)) {
            Log.i(TAG, "Calculating...");
        } else {
            Log.i(TAG, "Might not need to implement if no issues | status: " + status);
        }
    }

    private synchronized void continueToNextDestination() {
        this.hasArrived = true;
        this.notify();
    }
}
