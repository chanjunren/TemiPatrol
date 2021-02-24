package com.robosolutions.temipatrol.temi;

import android.util.Log;

import com.robosolutions.temipatrol.model.TemiRoute;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TemiNavigator implements OnGoToLocationStatusChangedListener {
    private final String TAG = "TemiNavigator";
    private Robot robot;
    private boolean hasArrived;
    private TemiRoute currentPatrollingRoute;
    private int nextDestinationIndex;

    public TemiNavigator(Robot robot) {
        this.robot = robot;
        hasArrived = false;
        robot.addOnGoToLocationStatusChangedListener(this);
    }

    public synchronized void patrolRoute(TemiRoute route) {
        this.currentPatrollingRoute = route;
        try {
            ArrayList<String> destinations = route.getDestinations();
            for (int i = 0; i < destinations.size(); i++) {
                Log.i(TAG, "Going to " + destinations.get(i) + " ...");
                nextDestinationIndex = i;
                Log.i(TAG, "Updated nextDestinationIndex to " + i);
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

    public void pausePatrol() {
        robot.stopMovement();
        currentPatrollingRoute = getRemainingRoute();
    }

    public void resumePatrol() {
        patrolRoute(currentPatrollingRoute);
    }

    public TemiRoute getRemainingRoute() {
        ArrayList<String> remainingRoute = currentPatrollingRoute.getDestinations();
        for (int i = 0; i < nextDestinationIndex; i++) {
            remainingRoute.remove(i);
        }
        Log.i(TAG, "Remaining locations: " + remainingRoute.toString());
        return new TemiRoute(currentPatrollingRoute.getRouteTitle(),
                remainingRoute);
    }

    private synchronized void continueToNextDestination() {
        this.hasArrived = true;
        this.notify();
    }

    @Override
    public void onGoToLocationStatusChanged(@NotNull String destination, @NotNull String status,
                                            int descriptionId, @NotNull String navStatus) {
        if (status.equals(OnGoToLocationStatusChangedListener.COMPLETE)) {
            Log.i(TAG, "Destination reached!");
            continueToNextDestination();
        }
    }
}
