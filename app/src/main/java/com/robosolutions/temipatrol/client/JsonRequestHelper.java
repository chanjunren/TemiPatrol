package com.robosolutions.temipatrol.client;

import android.util.Base64;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonRequestHelper {
    private static final String TAG = "JsonRequestHelper";

    private static String encodeByteArrayToBase64String(byte[] image) {
        return Base64.encodeToString(image, Base64.DEFAULT);
    }

    public static JSONObject getJSONImageObj(byte[] image) throws JSONException{
        JSONObject imageObj = new JSONObject();
        imageObj.put("content", encodeByteArrayToBase64String(image));
        return imageObj;
    }

    // ========== MASK DETECTION ==========
    // KEYS
    private static final String MD_FEATURE_KEY = "features";
    private static final String MD_IMG_KEY = "image";
    private static final String MD_REQ_KEY = "requests";
    private static final String MD_MAX_RES_KEY = "maxResults";
    private static final String MD_TYPE_KEY = "type";
    // DEFAULT VALUES
    private static final int MD_MAX_RESULTS_VALUE = 20;
    private static final String MD_TYPE_VALUE = "FACEMASK_DETECTION";

    public static JSONObject generateJsonMessageForMaskDetection(JSONObject imageObj) {
        JSONObject reqBody = new JSONObject();
        try {
            JSONArray reqBodyArr = new JSONArray();

            JSONArray featuresArr = getFeaturesArrForMaskDetection();

            JSONObject singleRequestObj = new JSONObject();
            singleRequestObj.put(MD_FEATURE_KEY, featuresArr);
            singleRequestObj.put(MD_IMG_KEY, imageObj);

            reqBodyArr.put(singleRequestObj);

            reqBody.put(MD_REQ_KEY, reqBodyArr);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        return reqBody;
    }

    public static JSONArray getFeaturesArrForMaskDetection() throws JSONException{
        JSONArray featuresArr = new JSONArray();
        JSONObject featuresBody = new JSONObject();
        featuresBody.put(MD_MAX_RES_KEY, MD_MAX_RESULTS_VALUE);
        featuresBody.put(MD_TYPE_KEY, MD_TYPE_VALUE);
        featuresArr.put(featuresBody);
        return featuresArr;
    }


    // ========== HUMAN DETECTION ==========
    // KEYS
    private static final String HD_MIN_HEIGHT_KEY = "min_height";
    private static final String HD_MIN_WIDTH_KEY = "min_width";
    private static final String HD_MAX_RESULTS_KEY = "maxResults";
    private static final String HD_SCORE_TH_KEY = "score_th";
    private static final String HD_NMS_IOU_KEY = "nms_iou";
    private static final String HD_TYPE_KEY = "type";
    // VALUES
    private static final double HD_MIN_HEIGHT_VALUE = 0.03;
    private static final double HD_MIN_WIDTH_VALUE = 0.03;
    private static final int HD_MAX_RESULTS_VALUE = 20;
    private static final double HD_SCORE_TH_VALUE = 0.30;
    private static final double HD_NMS_IOU_VALUE = 0.40;
    private static final String HD_TYPE_VALUE = "HUMAN_DETECTION";

    public static JSONObject generateJsonMessageForHumanDetection(JSONObject imageObj) {
        JSONObject reqBody = new JSONObject();
        try {
            JSONArray reqBodyArr = new JSONArray();

            JSONArray featuresArr = getFeaturesArrForHumanDetection();

            JSONObject singleRequestObj = new JSONObject();
            singleRequestObj.put(MD_FEATURE_KEY, featuresArr);
            singleRequestObj.put(MD_IMG_KEY, imageObj);

            reqBodyArr.put(singleRequestObj);

            reqBody.put(MD_REQ_KEY, reqBodyArr);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        return reqBody;
    }

    public static JSONArray getFeaturesArrForHumanDetection() throws JSONException{
        JSONArray featuresArr = new JSONArray();
        JSONObject featuresBody = new JSONObject();

        featuresBody.put(HD_MIN_HEIGHT_KEY, HD_MIN_HEIGHT_VALUE);
        featuresBody.put(HD_MIN_WIDTH_KEY, HD_MIN_WIDTH_VALUE);
        featuresBody.put(HD_MAX_RESULTS_KEY, HD_MAX_RESULTS_VALUE);
        featuresBody.put(HD_SCORE_TH_KEY, HD_SCORE_TH_VALUE);
        featuresBody.put(HD_NMS_IOU_KEY, HD_NMS_IOU_VALUE);
        featuresBody.put(HD_TYPE_KEY, HD_TYPE_VALUE);

        featuresArr.put(featuresBody);
        return featuresArr;
    }


    // ========== HUMAN DISTANCE ==========
    //KEYS
//    PRIVATE STATIC FIN
    // VALUES
    private static final int H_DIST_FX_VALUE = 823;
    private static final int H_DIST_FY_VALUE = 825;
    private static final int H_DIST_CENTER_X_VALUE = 323;
    private static final String H_DIST_TYPE_VALUE = "HUMAN_DISTANCE";
    public static JSONObject generateJsonMessageForHumanDistance(JSONObject imageObj) {
        JSONObject reqBody = new JSONObject();
        try {
            JSONArray reqBodyArr = new JSONArray();

            JSONArray featuresArr = getFeaturesArrForHumanDistance();

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
        featuresBody.put("Fx", H_DIST_FX_VALUE);
        featuresBody.put("Fy", H_DIST_FY_VALUE);
        featuresBody.put("centerX", H_DIST_CENTER_X_VALUE);
        featuresBody.put("type", H_DIST_TYPE_VALUE);
        featuresArr.put(featuresBody);
        return featuresArr;
    }
}
