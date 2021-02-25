package com.robosolutions.temipatrol.client;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class JsonPostman {
    private static final String TAG = "JsonPostman";
    private static final String AWS_HOST = "http://54.151.241.216";
    private static final int MASK_DETECTION_PORT = 5000;
    private static final int HUMAN_DISTANCE_PORT = 5002;

    private static final String MASK_DETECTION_POST_ENDPOINT = AWS_HOST + ":"
            + MASK_DETECTION_PORT + "/api";
    private static final String HUMAN_DISTANCE_POST_ENDPOINT = AWS_HOST + ":"
            + HUMAN_DISTANCE_PORT + "/api";

    // Values for parsing
    private static final String MASK_VALUE = "name";
    private static final String WEARING_MASK = "Face Mask";
    private static final String NOT_WEARING_MASK = "No Face Mask";
    private static final String MASK_RESPONSE_ARRAY_NAME = "FACEMASK_DETECTION";
    private static final String MASK_BOUNDING_POLY = "boundingPoly";

    private static final String DISTANCE_RESPONSE_ARRAY_NAME = "HUMAN_DISTANCE";
    private static final String HUMAN_NUM = "human_num";
    private static final int HUMAN_LIMIT = 8;


    private URL maskDetectionUrl, humanDistanceUrl;
    private HttpURLConnection maskDetectionConnection, humanDistanceConnection;


    public JsonPostman() {
        try {
            setupHumanDistanceConnection();
        } catch (Exception e) {
            Log.e(TAG, "JsonPostman init exception: " + e.toString());

        }
    }

    private void setupMaskDetectionConnection() throws MalformedURLException,
            ProtocolException, IOException {
        maskDetectionUrl = new URL(MASK_DETECTION_POST_ENDPOINT);
        maskDetectionConnection = (HttpURLConnection) maskDetectionUrl.openConnection();
        maskDetectionConnection.setRequestMethod("POST");
        maskDetectionConnection.setRequestProperty("Content-Type", "application/json; utf-8");
        maskDetectionConnection.setRequestProperty("Accept", "application/json");
        // To be able to write content to the connection output stream
        maskDetectionConnection.setDoOutput(true);
    }

    private void setupHumanDistanceConnection() throws MalformedURLException,
            ProtocolException, IOException {
        humanDistanceUrl = new URL(HUMAN_DISTANCE_POST_ENDPOINT);
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
            return true;
        }
    }

    public boolean isWearingMask(String jsonResponse) throws JSONException {
        JSONObject response = new JSONObject(jsonResponse);
        if (response.has(MASK_RESPONSE_ARRAY_NAME)) {
            JSONArray arr = response.getJSONArray(MASK_RESPONSE_ARRAY_NAME);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject arrObject = (JSONObject) arr.get(i);
                Log.i(TAG, "Person: " + arrObject.toString());
                if (arrObject.has(MASK_BOUNDING_POLY)) {
                    JSONObject result = new JSONObject(arrObject.get(MASK_BOUNDING_POLY).toString());

                    if(result.has(MASK_VALUE)) {
                        Log.i(TAG, "RESULT VALUE: " + result.get(MASK_VALUE));
                        if (result.get(MASK_VALUE).equals(NOT_WEARING_MASK)) {
                            return false;
                        }
                    }

                }
            }
        }
        return true;
    }

    // True if wearing mask, false otherwise
    public boolean postHumanDistanceRequestAndGetResult(JSONObject requestJson) {
        try {
            Log.i(TAG, "=== SENT ===\n" + requestJson.toString());

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
            Log.i(TAG, " === RESPONSE ===\n" + response.toString());
            return isClusterDetected(response.toString());
        } catch (Exception e) {
            Log.e(TAG, "postRequest exception: " + e.toString());
            return true;
        }
    }

    public boolean isClusterDetected(String jsonResponse) throws JSONException {
        JSONObject response = new JSONObject(jsonResponse);
        JSONArray arr = response.getJSONArray(DISTANCE_RESPONSE_ARRAY_NAME);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject cluster = (JSONObject) arr.get(i);
            if ((int) cluster.get(HUMAN_NUM) > HUMAN_LIMIT) {
                return true;
            }
        }
        return false;
    }
}
