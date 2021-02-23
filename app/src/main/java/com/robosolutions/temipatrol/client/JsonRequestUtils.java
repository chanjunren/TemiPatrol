package com.robosolutions.temipatrol.client;

import android.util.Base64;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonRequestUtils {
    private static final String TAG = "JsonRequestUtils";

    private static String encodeByteArrayToBase64String(byte[] image) {
        return Base64.encodeToString(image, Base64.DEFAULT);
    }

    public static JSONObject generateJsonMessage(byte[] image) {
        JSONObject reqBody = new JSONObject();
        try {
            JSONArray reqBodyArr = new JSONArray();

            JSONArray featuresArr = getFeaturesArr();
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

    public static JSONArray getFeaturesArr() throws JSONException{
        JSONArray featuresArr = new JSONArray();
        JSONObject featuresBody = new JSONObject();
        featuresBody.put("maxResults", 20);
        featuresBody.put("type", "FACEMASK_DETECTION");
        featuresArr.put(featuresBody);
        return featuresArr;
    }
}
