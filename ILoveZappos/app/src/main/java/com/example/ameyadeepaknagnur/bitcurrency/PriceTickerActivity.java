package com.example.ameyadeepaknagnur.bitcurrency;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.net.HttpURLConnection;
import java.util.Timer;
import java.util.TimerTask;

public class PriceTickerActivity extends AppCompatActivity implements View.OnClickListener {

    Button track_btn;
    ImageButton home;
    EditText price_edit_txt;

    double current_price, saved_price;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String price_key = "priceKey";

    Timer timer;
    TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_ticker);

        // Initializations
        track_btn = findViewById(R.id.track_btn);
        price_edit_txt = findViewById(R.id.edit_text_price);
        home = findViewById(R.id.home_price);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        String price_pref = "";

        if(sharedPreferences.contains(price_key)) {
            saved_price = Double.parseDouble(sharedPreferences.getString(price_key, price_pref));
        }


        // Set listeners
        track_btn.setOnClickListener(this);
        home.setOnClickListener(this);

    }

    public void getCurrentPrice() {
        RetrieveCurrentPrice retrieveCurrentPrice = new RetrieveCurrentPrice();
        retrieveCurrentPrice.execute();
    }

    public class RetrieveCurrentPrice extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            // Connect to api using url and get response
            String api_string = "ticker_hour/btcusd/";
            //Log.d("single news api", api_string);
            HttpURLConnection urlConn = null;
            String response = ApiInfo.connectToApi(api_string, urlConn);

            try {

                if (!response.equals(ApplicationVariable.FAILED)) {
                    JsonParser jsonParser = new JsonParser();

                    current_price = jsonParser.getPrice(response);

                } else {
                    return ApplicationVariable.FAILED;
                }
            } catch (JSONException exc) {

                Log.d("JSONException", exc.getMessage());
                return ApplicationVariable.FAILED;
            }

            return ApplicationVariable.SUCCESS;
        }

        @Override
        protected void onPostExecute(String response) {
            if (!response.equals(ApplicationVariable.FAILED)) {

                // Send notification
                if (current_price < saved_price) {
                    //Toast.makeText(getApplicationContext(), "HIT", Toast.LENGTH_LONG).show();

                    sendNotification(getApplicationContext(), saved_price);
                }

                //track_btn.setText("HIT");

            }
        }

    }

    private void sendNotification(Context applicationContext, double saved_price) {

        NotificationManager NM;
        NM = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pending = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(),0);

        Notification notify = new Notification.Builder(applicationContext)
                .setContentTitle("Price went below " + String.valueOf(saved_price))
                .setContentText("Price Alert")
                .setSmallIcon(R.drawable.icon_bitcurrency)
                .setContentIntent(pending)
                .build();

        NM.notify(0, notify);

    }

    @Override
    public void onBackPressed() {

    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                getCurrentPrice();
            }
        };
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 0, 1000); //3600 * 1000
    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.track_btn)) {
            String entered = price_edit_txt.getText().toString();

            if (entered.length() == 0) {
                Toast.makeText(getApplicationContext(), "Enter something please!", Toast.LENGTH_SHORT).show();
            }

            else {
                saved_price = Double.parseDouble(entered);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(price_key, String.valueOf(current_price));
                editor.commit();

                Toast.makeText(getApplicationContext(), "Tracking ...", Toast.LENGTH_SHORT).show();

                startTimer();
            }

        }
        else {
            Intent home_activity = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(home_activity);
        }
    }
}
