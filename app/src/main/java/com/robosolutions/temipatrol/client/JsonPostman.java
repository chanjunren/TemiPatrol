package com.robosolutions.temipatrol.client;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.robosolutions.temipatrol.client.JsonParser.isClusterDetected;
import static com.robosolutions.temipatrol.client.JsonParser.isHuman;
import static com.robosolutions.temipatrol.client.JsonParser.isWearingMask;

public class JsonPostman {
    private static final String TAG = "JsonPostman";

    private String AWS_HOST;
    private static final int MASK_DETECTION_PORT = 5000;
    private static final int HUMAN_DETECTION_PORT = 5001;
    private static final int HUMAN_DISTANCE_PORT = 5002;

    private String MASK_DETECTION_POST_ENDPOINT;
    private String HUMAN_DETECTION_POST_ENDPOINT;
    private String HUMAN_DISTANCE_POST_ENDPOINT;

    private HttpURLConnection maskDetectionConnection, humanDetectionConnection, humanDistanceConnection;


    public JsonPostman(String serverIp) {
        try {
            // SETUP IP ADDRESS
            this.AWS_HOST = serverIp;
            MASK_DETECTION_POST_ENDPOINT = "http://" + AWS_HOST + ":" + MASK_DETECTION_PORT + "/api";
            HUMAN_DETECTION_POST_ENDPOINT = "http://" + AWS_HOST + ":" + HUMAN_DETECTION_PORT + "/api";
            HUMAN_DISTANCE_POST_ENDPOINT = "http://" + AWS_HOST + ":" + HUMAN_DISTANCE_PORT + "/api";
        } catch (Exception e) {
            Log.e(TAG, "JsonPostman init exception: " + e.toString());

        }
    }

    private void setupMaskDetectionConnection() throws IOException {
        URL maskDetectionUrl = new URL(MASK_DETECTION_POST_ENDPOINT);
        maskDetectionConnection = (HttpURLConnection) maskDetectionUrl.openConnection();
        maskDetectionConnection.setRequestMethod("POST");
        maskDetectionConnection.setRequestProperty("Content-Type", "application/json; utf-8");
        maskDetectionConnection.setRequestProperty("Accept", "application/json");
        // To be able to write content to the connection output stream
        maskDetectionConnection.setDoOutput(true);
    }

    private void setupHumanDetectionConnection() throws IOException {
        URL humanDetectionUrl = new URL(HUMAN_DETECTION_POST_ENDPOINT);
        humanDetectionConnection = (HttpURLConnection) humanDetectionUrl.openConnection();
        humanDetectionConnection.setRequestMethod("POST");
        humanDetectionConnection.setRequestProperty("Content-Type", "application/json; utf-8");
        humanDetectionConnection.setRequestProperty("Accept", "application/json");
        // To be able to write content to the connection output stream
        humanDetectionConnection.setDoOutput(true);
    }

    private void setupHumanDistanceConnection() throws IOException {
        URL humanDistanceUrl = new URL(HUMAN_DISTANCE_POST_ENDPOINT);
        humanDistanceConnection = (HttpURLConnection) humanDistanceUrl.openConnection();
        humanDistanceConnection.setRequestMethod("POST");
        humanDistanceConnection.setRequestProperty("Content-Type", "application/json; utf-8");
        humanDistanceConnection.setRequestProperty("Accept", "application/json");
        // To be able to write content to the connection output stream
        humanDistanceConnection.setDoOutput(true);
    }

    // True if wearing mask, false otherwise
    public boolean postMaskDetectionRequestAndGetResult(JSONObject requestJson) {
        try {
            setupMaskDetectionConnection();
            Log.i(TAG, "=== SENT ===\n" + requestJson.toString());
            OutputStream os = maskDetectionConnection.getOutputStream();
            Log.i(TAG, "Exception is thrown before me!");
            byte[] input = requestJson.toString().getBytes("utf-8");
            os.write(input, 0, input.length);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(maskDetectionConnection.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine);
            }
            Log.i(TAG, " === RESPONSE ===\n" + response.toString());
            return isWearingMask(response.toString());
        } catch (Exception e) {
            Log.e(TAG, "postRequest exception: " + e.toString());
            debugExceptionFromResult(maskDetectionConnection);
            return true;
        }
    }

    // True if wearing mask, false otherwise
    public boolean postHumanDistanceRequestAndGetResult(JSONObject requestJson) {
        try {
            Log.i(TAG, "=== SENT ===\n" + requestJson.toString());
            setupHumanDistanceConnection();
            OutputStream os = humanDistanceConnection.getOutputStream();
            byte[] input = requestJson.toString().getBytes("utf-8");
            os.write(input, 0, input.length);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(humanDistanceConnection.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine);
            }
            Log.i(TAG, " === HUMAN DISTANCE RESPONSE ===\n" + response.toString());
            return isClusterDetected(response.toString());
        } catch (Exception e) {
            Log.e(TAG, "postHumanDistanceRequest exception: " + e.toString());
            debugExceptionFromResult(humanDistanceConnection);
            return false;
        }
    }

    // True if wearing mask, false otherwise
    public boolean postHumanDetectionRequestAndGetResult(JSONObject requestJson) {
        try {
            Log.i(TAG, "=== SENT ===\n" + requestJson.toString());
            setupHumanDetectionConnection();
            OutputStream os = humanDetectionConnection.getOutputStream();
            byte[] input = requestJson.toString().getBytes("utf-8");
            os.write(input, 0, input.length);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(humanDetectionConnection.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine);
            }
            Log.i(TAG, " === HUMAN DETECTION RESPONSE ===\n" + response.toString());
            return isHuman(response.toString());
        } catch (Exception e) {
            Log.e(TAG, "postHumanDistanceRequest exception: " + e.toString());
            debugExceptionFromResult(humanDistanceConnection);
            return false;
        }
    }

    private void debugExceptionFromResult(HttpURLConnection connection) {
        try {
            int status = connection.getResponseCode();
            Log.e(TAG, "Status: " + status);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(humanDistanceConnection.getErrorStream(), "utf-8"));
            StringBuilder errorResponse = new StringBuilder();
            String errorResponseLine = null;
            while ((errorResponseLine = br.readLine()) != null) {
                errorResponse.append(errorResponseLine);
            }
            Log.i(TAG, " === ERROR RESPONSE ===\n" + errorResponse.toString());
        } catch (IOException err) {
            Log.e(TAG, "Debug IOException: " + err.toString());
        }
    }
}
