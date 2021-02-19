package com.robosolutions.temipatrol.client;

import android.util.Log;

import com.google.gson.JsonObject;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class JsonRequestUtils {
    private static final String TAG = "JsonRequestUtils";

    private static String encodeFileToBase64Binary(File file) {
        String encodedFile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedFile = new String(Base64.encodeBase64(bytes), "UTF-8");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "encodeFileToBase64Binary error: " + e.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "encodeFileToBase64Binary error: " + e.toString());
        }
        return encodedFile;
    }

    public static JSONObject generateJsonMessage(File file) {
        JSONObject reqBody = new JSONObject();
        try {
            JSONArray reqBodyArr = new JSONArray();

            JSONArray featuresArr = getFeaturesArr();
            JSONObject imageObj = getJSONImageObj(file);

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

    public static JSONObject getJSONImageObj(File file) throws JSONException{
        JSONObject imageObj = new JSONObject();
        imageObj.put("content", encodeFileToBase64Binary(file));
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
