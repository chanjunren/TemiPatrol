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
    private Robot robot;
    private TemiRoute currentPatrollingRoute;
    private int currentIndex;
    private PatrolFragment patrolFragment;

    public TemiNavigator(Robot robot, PatrolFragment patrolFragment) {
        this.robot = robot;
        robot.addOnGoToLocationStatusChangedListener(this);
        this.currentIndex = -1;
        this.patrolFragment = patrolFragment;
    }

    public void patrolRoute(TemiRoute route) {
        this.currentPatrollingRoute = route;
        this.currentIndex = 0;
        goToNextDestination();
    }

    private void goToNextDestination() {
        if (currentIndex >= currentPatrollingRoute.getDestinations().size()) {
            // Reached final destination
            patrolFragment.navigateToHomePage();
            return;
        }
        robot.goTo(currentPatrollingRoute.getDestinations().get(currentIndex));
    }

    public void pausePatrol() {
        robot.stopMovement();
    }

    public void resumePatrol() {
        goToNextDestination();
    }

    @Override
    public void onGoToLocationStatusChanged(@NotNull String destination, @NotNull String status,
                                            int descriptionId, @NotNull String navStatus) {
        String threadName = Thread.currentThread().getName();
        Log.i(TAG, "OnGoToLocationStatusChanged thread: " + threadName);
//        Log.i(TAG, "status updated! ");
        if (status.equals(OnGoToLocationStatusChangedListener.COMPLETE)) {
            Log.i(TAG, "Destination reached!");
            currentIndex++;
            goToNextDestination();
        }
    }
}
