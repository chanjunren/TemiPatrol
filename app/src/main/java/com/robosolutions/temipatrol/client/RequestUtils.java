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

public class RequestUtils {
    private static final String TAG = "RequestUtils";

    public static void main(String[] args) {
        test();
    }
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

    public JSONObject generateJsonMessage(File file) {
        try {
//            JSONObject
            
            JSONArray reqBodyArr = new JSONArray();

            JSONArray featuresArr = new JSONArray();

            JSONObject featuresBody = new JSONObject();
            featuresBody.put("maxResults", 20);
            featuresBody.put("type", "FACEMASK_DETECTION");

            featuresArr.put(featuresBody);


            JSONObject reqBody = new JSONObject();
            reqBody.put("features", featuresArr);
            reqBody.put("image", "image_test");

            reqBodyArr.put(reqBody);


        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
    }
}
