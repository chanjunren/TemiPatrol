package com.robosolutions.temipatrol.temi;

import android.util.Log;

import com.robosolutions.temipatrol.model.TemiRoute;
import com.robosolutions.temipatrol.views.PatrolFragment;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TemiNavigator implements OnGoToLocationStatusChangedListener {
    private final String TAG = "TemiNavigator";
    private Robot temiRobot;
    private ArrayList<String> currentPatrollingRoute;
    private int currentIndex;
    private PatrolFragment patrolFragment;

    public TemiNavigator(Robot temiRobot, PatrolFragment patrolFragment) {
        this.temiRobot = temiRobot;
        temiRobot.addOnGoToLocationStatusChangedListener(this);
        this.currentIndex = -1;
        this.patrolFragment = patrolFragment;
    }

    public void patrolRoute(TemiRoute route) {
        this.currentPatrollingRoute = new ArrayList<>();
        for (int i = 0; i < route.getPatrolCount(); i++) {
            this.currentPatrollingRoute.addAll(route.getDestinations());
        }
        Log.i(TAG, "Patrolling: " + this.currentPatrollingRoute.toString());
        this.currentIndex = 0;
        goToNextDestination();
    }

    private void goToNextDestination() {
        if (currentIndex >= currentPatrollingRoute.size()) {
            // Reached final destination
            patrolFragment.navigateToHomePage();
            return;
        }
        temiRobot.goTo(currentPatrollingRoute.get(currentIndex));
    }

    public void pausePatrol() {
        temiRobot.stopMovement();
    }

    public void resumePatrol() {
        goToNextDestination();
    }

    @Override
    public void onGoToLocationStatusChanged(@NotNull String destination, @NotNull String status,
                                            int descriptionId, @NotNull String navStatus) {
        if (status.equals(OnGoToLocationStatusChangedListener.GOING)) {
            Log.i(TAG, "Tilting...");
            temiRobot.tiltAngle(15);
        }
        else if (status.equals(OnGoToLocationStatusChangedListener.COMPLETE)) {
            Log.i(TAG, "Destination reached!");
            currentIndex++;
            goToNextDestination();
        }
    }

    public Robot getTemiRobot() {
        return temiRobot;
    }
}
