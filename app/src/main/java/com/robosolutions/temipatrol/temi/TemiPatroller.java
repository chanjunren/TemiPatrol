package com.robosolutions.temipatrol.temi;

import android.os.Handler;
import android.util.Log;

import com.robosolutions.temipatrol.model.TemiRoute;
import com.robosolutions.temipatrol.views.PatrolFragment;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TemiPatroller implements OnGoToLocationStatusChangedListener, Robot.TtsListener {
    private final String TAG = "TemiPatroller";

    private static TemiPatroller INSTANCE;
    private static final int MIN_BATT = 90;

    private Robot temiRobot;
    private ArrayList<String> currentPatrollingRoute;
    private volatile int currentIndex;
    private PatrolFragment patrolFragment;
    private boolean isStationaryPatrol, abortedDueToSpeech, abortedDueToTap, isContinuosPatrol;

    private TemiPatroller(PatrolFragment patrolFragment, Robot temiRobot) {
        this.temiRobot = temiRobot;
        this.currentIndex = -1;
        this.patrolFragment = patrolFragment;
    }

    public static TemiPatroller getInstance(PatrolFragment patrolFragment, Robot temiRobot,
                                            boolean isStationaryPatrol) {
        if (INSTANCE == null) {
            INSTANCE = new TemiPatroller(patrolFragment, temiRobot);
            return INSTANCE;
        }
        INSTANCE.setPatrolFragment(patrolFragment);
        INSTANCE.isStationaryPatrol = isStationaryPatrol;
        return INSTANCE;
    }

    public void patrolRoute(TemiRoute route) {
        Log.i(TAG, "Battery: " + temiRobot.getBatteryData().getBatteryPercentage());
        if (route.getPatrolCount() == Integer.MAX_VALUE) {
            isContinuosPatrol = true;
            this.currentPatrollingRoute = route.getDestinations();
        } else {
            this.currentPatrollingRoute = new ArrayList<>();
            for (int i = 0; i < route.getPatrolCount(); i++) {
                this.currentPatrollingRoute.addAll(route.getDestinations());
            }
        }

        Log.i(TAG, "Patrolling: " + this.currentPatrollingRoute.toString());
        this.currentIndex = 0;
        goToNextDestination();
    }

    private void goToNextDestination() {
        Log.i(TAG, "Current index: " + this.currentIndex);
        if (this.currentIndex >= currentPatrollingRoute.size()) {
            if (isContinuosPatrol) {
                this.currentIndex = 0;
            } else {
                // Reached final destination
                patrolFragment.navigateToHomePage();
                return;
            }
        }
        if (temiRobot.getBatteryData().getBatteryPercentage() < MIN_BATT) {
            Log.i(TAG, "Low battery, returning to home...");
            TtsRequest goHomeMsg =
                    TtsRequest.create("My battery is low, I'm going home now. Good Bye", false);
            temiRobot.speak(goHomeMsg);
            patrolFragment.navigateToHomePage();
            temiRobot.goTo("home base");
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

    public void setPatrolFragment(PatrolFragment patrolFragment) {
        this.patrolFragment = patrolFragment;
    }

    public Robot getTemiRobot() {
        return temiRobot;
    }

    @Override
    public void onGoToLocationStatusChanged(@NotNull String destination, @NotNull String status,
                                            int descriptionId, @NotNull String navStatus) {
        if (status.equals(OnGoToLocationStatusChangedListener.COMPLETE)) {
            Log.i(TAG, "Destination reached!");
            this.currentIndex++;
            goToNextDestination();
        } else if (status.equals(OnGoToLocationStatusChangedListener.ABORT)
                && abortedDueToSpeech) { // When location is aborted due to speech
            Log.i(TAG, "LocationStatusChangedListener");
            abortedDueToSpeech = false;
        } else if (status.equals(OnGoToLocationStatusChangedListener.ABORT)) { // When goTo is aborted due to someone touching the screen
            Handler handler = new Handler();
            handler.postDelayed(this::resumePatrol, 3000);
        }

    }

    @Override
    public void onTtsStatusChanged(@NotNull TtsRequest ttsRequest) {
        if (ttsRequest.getStatus() == TtsRequest.Status.COMPLETED) {
            Log.i(TAG, "SPEECH COMPLETED");
            if (!isStationaryPatrol) {
                resumePatrol();
            }
        } else if (ttsRequest.getStatus() == TtsRequest.Status.STARTED) {
            abortedDueToSpeech = true;
        }
    }
}
