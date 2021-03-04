package com.robosolutions.temipatrol.client;

import android.util.Base64;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonRequestUtils {
    private static final String TAG = "JsonRequestUtils";

    // Feature values for humandistance JSON Request message
    private static final int MD_FX_VALUE = 823;
    private static final int MD_FY_VALUE = 825;
    private static final int MD_CENTER_X_VALUE = 323;
    private static final String MD_HUMAN_DIST_TYPE = "HUMAN_DISTANCE";

    private static String encodeByteArrayToBase64String(byte[] image) {
        return Base64.encodeToString(image, Base64.DEFAULT);
    }

    public static JSONObject generateJsonMessageForMaskDetection(byte[] image) {
        JSONObject reqBody = new JSONObject();
        try {
            JSONArray reqBodyArr = new JSONArray();

            JSONArray featuresArr = getFeaturesArrForMaskDetection();
            JSONObject imageObj = getJSONImageObj(image);

            JSONObject singleRequestObj = new JSONObject();
            singleRequestObj.put("features", featuresArr);
            singleRequestObj.put("image", imageObj);

            reqBodyArr.put(singleRequestObj);

            reqBody.put("requests", reqBodyArr);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        return reqBody;
    }

    public static JSONObject getJSONImageObj(byte[] image) throws JSONException{
        JSONObject imageObj = new JSONObject();
        imageObj.put("content", encodeByteArrayToBase64String(image));
        return imageObj;
    }

    public static JSONArray getFeaturesArrForMaskDetection() throws JSONException{
        JSONArray featuresArr = new JSONArray();
        JSONObject featuresBody = new JSONObject();
        featuresBody.put("maxResults", 20);
        featuresBody.put("type", "FACEMASK_DETECTION");
        featuresArr.put(featuresBody);
        return featuresArr;
    }

    public static JSONObject generateJsonMessageForHumanDistance(byte[] image) {
        JSONObject reqBody = new JSONObject();
        try {
            JSONArray reqBodyArr = new JSONArray();

            JSONArray featuresArr = getFeaturesArrForHumanDistance();
            JSONObject imageObj = getJSONImageObj(image);

            JSONObject singleRequestObj = new JSONObject();
            singleRequestObj.put("features", featuresArr);
            singleRequestObj.put("image", imageObj);

            reqBodyArr.put(singleRequestObj);

            reqBody.put("requests", reqBodyArr);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        return reqBody;
    }

    public static JSONArray getFeaturesArrForHumanDistance() throws JSONException{
        JSONArray featuresArr = new JSONArray();
        JSONObject featuresBody = new JSONObject();
        featuresBody.put("Fx", MD_FX_VALUE);
        featuresBody.put("Fy", MD_FY_VALUE);
        featuresBody.put("centerX", MD_CENTER_X_VALUE);
        featuresBody.put("type", MD_HUMAN_DIST_TYPE);
        featuresArr.put(featuresBody);
        return featuresArr;
    }
}
