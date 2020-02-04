package com.example.ameyadeepaknagnur.bitcurrency;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ApiInfo {
    //public static String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjUxIiwiaWF0IjoxNTU3MjExNTQ0LCJleHAiOjE1NjU4NTE1NDR9.f_iEHMVg2RRM3J3WQr79rPu7DERsNH5NaAeSwR_qt48";
    public static String url_start = "https://www.bitstamp.net/api/v2/";
    //public static String language = "en";
    //public static String youtube_start = "https://www.youtube.com/watch?v=";

    public static String connectToApi(String api_string, HttpURLConnection urlConn) {
        String url_string = ApiInfo.url_start + api_string;
        URL url;
        String result = "";

        try {
            // Initialize url objects
            url = new URL(url_string);
            urlConn = (HttpURLConnection) url.openConnection();

            // Properties of connection
            urlConn.setRequestMethod("GET");
            //urlConn.setRequestProperty("Authorization", ApiInfo.token);
            urlConn.setDoInput(true);

            // Get Response from url
            int responseCode = urlConn.getResponseCode();
            result = urlConn.getResponseMessage();

            // Get data from api
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                String response = bufferedReader.readLine();
                Log.d("Received", response);

                return response;
            }

        } catch (Exception e) {
            return ApplicationVariable.FAILED;
        } finally {
            urlConn.disconnect();
        }

        return ApplicationVariable.FAILED;
    }

}
