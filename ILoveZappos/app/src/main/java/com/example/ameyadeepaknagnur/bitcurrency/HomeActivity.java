package com.example.ameyadeepaknagnur.bitcurrency;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button order_btn, graph_btn, price_track_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initializations
        order_btn = findViewById(R.id.order_btn);
        graph_btn = findViewById(R.id.graph_btn);
        price_track_btn = findViewById(R.id.price_track_btn);

        // Set listeners
        order_btn.setOnClickListener(this);
        graph_btn.setOnClickListener(this);
        price_track_btn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.order_btn)) {
            Intent order_book_activity = new Intent(this, OrderBookActivity.class);
            startActivity(order_book_activity);
        }
        else if(v == findViewById(R.id.graph_btn)) {
            Intent graph_activity = new Intent(getApplicationContext(), GraphActivity.class);
            startActivity(graph_activity);
        }
        else {
            Intent price_tracker_activity = new Intent(getApplicationContext(), PriceTickerActivity.class);
            startActivity(price_tracker_activity);
        }
    }
}
