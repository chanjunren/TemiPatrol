package com.robosolutions.temipatrol.client;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

public class JsonPostman {
    private static final String TAG = "JsonPostman";

    private static final String AWS_USER = "ec2-user";
    private static final String AWS_HOST = "54.255.249.46";
    private static final int AWS_PORT =  22;
    private static final int MASK_DETECTION_PORT = 5000;

    private static final String filePath = "/client/mykeypair.ppk";

    private Activity mainActivity;

    public JsonPostman(Activity activity) {
        this.mainActivity = activity;
    }

    public void postRequest(JSONObject requestJson) {
        try {
            // Central configuration point, factory for Session object configured with these settings
            JSch jsch = new JSch();
            String privateKey = getPrivateKeyPath();
            Log.i(TAG, "Path returned: " + privateKey);

            jsch.addIdentity(privateKey);
//
            Session session = jsch.getSession(AWS_USER, AWS_HOST, AWS_PORT);
            Log.i(TAG, "Session Created");

//            // disabling StrictHostKeyChecking may help to make connection but makes it insecure
//            // see http://stackoverflow.com/questions/30178936/jsch-sftp-security-with-session-setconfigstricthostkeychecking-no

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            Log.i(TAG, "session connected.....");
//
            Channel channel = session.openChannel("direct-tcpip");
            InputStream is = new ByteArrayInputStream(requestJson.toString().getBytes("UTF-8"));
            channel.setInputStream(is);
            channel.setOutputStream(System.out);
            channel.connect();
            System.out.println("shell channel connected....");


        } catch (Exception e) {
            Log.e(TAG, "Post Request Exception: " + e.toString());
        }
    }

    // Stores private key in cache and returns it
    private String getPrivateKeyPath() throws IOException{
        InputStream inputStream = mainActivity.getAssets().open("mykeypair.pem");
        File file = new File(mainActivity.getCacheDir(), "cacheFileAppeal.srl");

        try (OutputStream output = new FileOutputStream(file)) {
            byte[] buffer = new byte[4 * 1024]; // or other buffer size
            int read;

            while ((read = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }

            output.flush();
        } finally {
            inputStream.close();
        }
        return file.getPath();
    }
}
