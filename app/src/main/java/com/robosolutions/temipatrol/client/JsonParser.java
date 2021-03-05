package com.robosolutions.temipatrol.client;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
    private static final String TAG = "JsonParser";
    // Values for parsing
    private static final String MASK_VALUE = "name";
    private static final String WEARING_MASK = "Face Mask";
    private static final String NOT_WEARING_MASK = "No Face Mask";
    private static final String MASK_RESPONSE_ARRAY_NAME = "FACEMASK_DETECTION";
    private static final String MASK_BOUNDING_POLY = "boundingPoly";

    private static final String DISTANCE_RESPONSE_ARRAY_NAME = "HUMAN_DISTANCE";
    private static final String HUMAN_NUM = "human_num";
    private static final int HUMAN_LIMIT = 8;
    public static boolean isClusterDetected(String jsonResponse) throws JSONException {
        JSONObject response = new JSONObject(jsonResponse);
        JSONArray arr = response.getJSONArray(DISTANCE_RESPONSE_ARRAY_NAME);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject cluster = (JSONObject) arr.get(i);
            Log.i(TAG, "Number of hoomans detected: " + cluster.get(HUMAN_NUM));
            if ((int) cluster.get(HUMAN_NUM) > HUMAN_LIMIT) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWearingMask(String jsonResponse) throws JSONException {
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
}
