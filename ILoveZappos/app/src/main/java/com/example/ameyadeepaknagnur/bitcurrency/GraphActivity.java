package com.example.ameyadeepaknagnur.bitcurrency;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONException;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity implements View.OnClickListener {

    ListView list_transact;
    ArrayList<String> list_transactions;
    ArrayList<Entry> list_prices;
    LineChart currency_value;
    LineData currency_data;
    LineDataSet currency_dataset;
    Spinner currency_drop_down;
    ImageButton refresh, home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        // Initializations
        list_transact = findViewById(R.id.list_transaction);
        currency_drop_down = findViewById(R.id.currency_spinner);
        currency_value = findViewById(R.id.currency_value_chart);
        refresh = findViewById(R.id.refresh_graph);
        home = findViewById(R.id.home_graph);

        list_transactions = new ArrayList<>();
        list_prices = new ArrayList<>();

        // Populate drop down
        List<String> currencies = new ArrayList<>();
        currencies.add("BitCoin");
        currencies.add("XRP");

        populateSpinner(currencies);

        // API hit functions
        getTransactions();

        // Set listeners
        refresh.setOnClickListener(this);
        home.setOnClickListener(this);
    }

    private void populateSpinner(List<String> values) {

        // Create an adapter to populate spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.currency_dropdown_item, values);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        // Set spinner adapter
        currency_drop_down.setAdapter(spinnerAdapter);

    }

    public void getTransactions() {
        RetrieveTransactions retrieveTransactions = new RetrieveTransactions();
        retrieveTransactions.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.refresh_graph)) {
            startActivity(this.getIntent());

            /*currency_value.removeAllViews();
            currency_value.resetTracking();
            currency_value.resetViewPortOffsets();
            currency_value.resetZoom();

            // API hit functions
            getTransactions();*/
        }
        else {
            Intent home_activity = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(home_activity);
        }
    }

    @Override
    public void onBackPressed() {

    }

    public class RetrieveTransactions extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            // Connect to api using url and get response
            String api_string = "transactions/btcusd/";
            //Log.d("single news api", api_string);
            HttpURLConnection urlConn = null;
            String response = ApiInfo.connectToApi(api_string, urlConn);

            try {

                if (!response.equals(ApplicationVariable.FAILED)) {
                    JsonParser jsonParser = new JsonParser();
                    JsonParser.transact_no = 0;
                    Transaction transaction = jsonParser.getTransaction(response);
                    while (transaction != null) {
                        list_transactions.add(transaction.tid);
                        if (transaction.type == 1) {
                            list_prices.add(new Entry(Integer.parseInt(transaction.date), (float) transaction.price));
                        }

                        transaction = jsonParser.getTransaction(response);
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

                populate_transact_list(list_transact, list_transactions, R.layout.list_item);

                plot_line_graph(list_prices);
            }
        }

    }

    public void populate_transact_list(ListView listView, ArrayList<String> arrayList, int layout) {

        // Create adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getBaseContext(), layout);
        arrayAdapter.addAll(arrayList);

        // Hit list with adapter
        listView.setAdapter(arrayAdapter);
    }

    public void plot_line_graph(ArrayList<Entry> prices) {
        currency_dataset = new LineDataSet(prices, "Currency Value");

        // Set graph display parameters
        currency_dataset.setDrawIcons(false);
        currency_dataset.enableDashedLine(10f, 5f, 0f);
        currency_dataset.enableDashedHighlightLine(10f, 5f, 0f);
        currency_dataset.setColor(getResources().getColor(R.color.Gold));
        currency_dataset.setCircleColor(getResources().getColor(R.color.blue));
        currency_dataset.setLineWidth(1f);
        currency_dataset.setCircleRadius(3f);
        currency_dataset.setDrawCircleHole(true);
        currency_dataset.setValueTextSize(9f);
        currency_dataset.setValueTextColor(getResources().getColor(R.color.white));

        ArrayList<ILineDataSet> prices_list = new ArrayList<>();
        prices_list.add(currency_dataset);

        currency_data = new LineData(prices_list);

        currency_value.setData(currency_data);
        currency_value.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
        currency_value.getXAxis().setTextColor(getResources().getColor(R.color.white));
        currency_value.getLegend().setTextColor(getResources().getColor(R.color.white));
        currency_value.invalidate();
    }

}
