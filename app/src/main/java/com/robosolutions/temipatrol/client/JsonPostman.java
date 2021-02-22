package com.robosolutions.temipatrol.client;

import android.app.Activity;
import android.util.Log;

import org.apache.http.HttpConnection;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonPostman {
    private static final String TAG = "JsonPostman";
    private static final String AWS_HOST = "http://54.255.249.46";
    private static final int MASK_DETECTION_PORT = 5000;
    private Activity mainActivity;

    private static final String POST_ENDPOINT = AWS_HOST + ":" + MASK_DETECTION_PORT + "/api";

    public JsonPostman(Activity activity) {
        this.mainActivity = activity;
    }

    public void postRequest(JSONObject requestJson) {
        try {
            URL url = new URL(POST_ENDPOINT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            // To be able to write content to the connection output stream
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            byte[] input = requestJson.toString().getBytes("utf-8");
            os.write(input, 0, input.length);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine);
            }
            Log.i(TAG, " === RESPONSE ===\n" + response.toString());


        } catch (Exception e) {
            Log.e(TAG, "postRequest exception: " + e.toString());
        }
    }
}
