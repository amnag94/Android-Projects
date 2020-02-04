package com.example.ameyadeepaknagnur.bitcurrency;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONException;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;

public class OrderBookActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView table_bids, table_asks;
    RecyclerView.Adapter bids_table_adap, asks_table_adap;
    RecyclerView.LayoutManager bids_layout_mng, asks_layout_mng;

    ImageButton refresh, home;

    ArrayList<String> list_bid, list_ask;
    Bid[] bids;
    Ask[] asks;

    String read_json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_book);

        // Initializations
        table_bids = findViewById(R.id.table_bids);
        table_asks = findViewById(R.id.table_asks);
        refresh = findViewById(R.id.refresh);
        home = findViewById(R.id.home);

        list_bid = new ArrayList<>();
        list_ask = new ArrayList<>();

        // Get bids and asks for the order book
        getBids();

        getAsks();

        // Set listeners
        refresh.setOnClickListener(this);
        home.setOnClickListener(this);
    }

    public void setUpTableRecyclerView(Context applicationContext, String type, ArrayList<String> list_order, RecyclerView table_order, RecyclerView.LayoutManager layout_mng, RecyclerView.Adapter table_adap) {

        layout_mng = new GridLayoutManager(applicationContext, 1);

        table_order.setLayoutManager(layout_mng);

        table_adap = new RecyclerViewAdapter(applicationContext, list_order, type);

        table_order.setAdapter(table_adap);

    }


    public void getBids() {
        RetrieveBids retrieveBids = new RetrieveBids();
        retrieveBids.execute();
    }

    public void getAsks() {
        RetrieveAsks retrieveAsks = new RetrieveAsks();
        retrieveAsks.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.refresh)) {
            // Empty lists
            list_ask.clear();
            list_bid.clear();

            // Get bids and asks for the order book
            getBids();

            getAsks();

            table_asks.refreshDrawableState();
        }
        else {
            Intent home_activity = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(home_activity);
        }
    }

    @Override
    public void onBackPressed() {

    }

    public class RetrieveBids extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            // Connect to api using url and get response
            String api_string = "order_book/btcusd/";
            //Log.d("single news api", api_string);
            HttpURLConnection urlConn = null;
            String response = ApiInfo.connectToApi(api_string, urlConn);

            try {

                if (!response.equals(ApplicationVariable.FAILED)) {
                    JsonParser jsonParser = new JsonParser();
                    JsonParser.bid_no = 0;

                    // Avoid calling api for asks again
                    read_json = response;

                    Bid bid = jsonParser.getBid(response);

                    while (bid != null) {
                        list_bid.add("Value : " + bid.value + " Amount : " + bid.amount);
                        bid = jsonParser.getBid(response);
                    }

                } else {
                    return ApplicationVariable.FAILED;
                }
            } catch (JSONException exc) {

                Log.d("JSONException", exc.getMessage());
                return ApplicationVariable.FAILED;
            }

            return ApplicationVariable.SUCCESS;
        }

    }

    public class RetrieveAsks extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String response;

            if(read_json.equals("") || read_json == null) {
                // Connect to api using url and get response
                String api_string = "order_book/btcusd/";
                //Log.d("single news api", api_string);
                HttpURLConnection urlConn = null;
                response = ApiInfo.connectToApi(api_string, urlConn);
            }
            else {
                response = read_json;
            }

            try {

                if (!response.equals(ApplicationVariable.FAILED)) {

                    JsonParser jsonParser = new JsonParser();
                    JsonParser.ask_no = 0;

                    Ask ask = jsonParser.getAsk(response);

                    while (ask != null) {
                        list_ask.add("Value : " + ask.value + " Amount : " + ask.amount);
                        ask = jsonParser.getAsk(response);
                    }

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

                // Set up table for bids and asks with respective lists.
                setUpTableRecyclerView(getApplicationContext(), "Bid", list_bid, table_bids, bids_layout_mng, bids_table_adap);
                setUpTableRecyclerView(getApplicationContext(), "Ask", list_ask, table_asks, asks_layout_mng, asks_table_adap);

                //populate_order_book(R.layout.list_item);

            }
        }

    }

}
