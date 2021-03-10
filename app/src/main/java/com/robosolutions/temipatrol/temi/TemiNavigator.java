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

    private static TemiNavigator INSTANCE;

    private Robot temiRobot;
    private ArrayList<String> currentPatrollingRoute;
    private volatile int currentIndex;
    private PatrolFragment patrolFragment;

    private TemiNavigator(PatrolFragment patrolFragment) {
        this.temiRobot = Robot.getInstance();
        temiRobot.addOnGoToLocationStatusChangedListener(this);
        this.currentIndex = -1;
        this.patrolFragment = patrolFragment;
    }

    public static TemiNavigator getInstance(PatrolFragment patrolFragment) {
        if (INSTANCE == null) {
            INSTANCE = new TemiNavigator(patrolFragment);
            return INSTANCE;
        }
        INSTANCE.setPatrolFragment(patrolFragment);
        return INSTANCE;
    }

    public void patrolRoute(TemiRoute route) {
        Log.i(TAG, "Instance ID: " + this.toString());
        this.currentPatrollingRoute = new ArrayList<>();
        for (int i = 0; i < route.getPatrolCount(); i++) {
            this.currentPatrollingRoute.addAll(route.getDestinations());
        }
        Log.i(TAG, "Patrolling: " + this.currentPatrollingRoute.toString());
        this.currentIndex = 0;
        goToNextDestination();
    }

    private void goToNextDestination() {
        Log.i(TAG, "Current index: " + this.currentIndex);
        if (this.currentIndex >= currentPatrollingRoute.size()) {
            // Reached final destination
            patrolFragment.navigateToHomePage();
            return;
        }
        temiRobot.goTo(currentPatrollingRoute.get(this.currentIndex));
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
        if (status.equals(OnGoToLocationStatusChangedListener.COMPLETE)) {
            Log.i(TAG, "Destination reached!");
            this.currentIndex++;
            goToNextDestination();
        }
    }

    public void setPatrolFragment(PatrolFragment patrolFragment) {
        this.patrolFragment = patrolFragment;
    }

    public Robot getTemiRobot() {
        return temiRobot;
    }
}
