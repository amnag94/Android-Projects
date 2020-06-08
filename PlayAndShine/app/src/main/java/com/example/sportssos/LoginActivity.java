package com.example.sportssos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button login_btn, register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initializations
        login_btn = findViewById(R.id.login_btn);
        register_btn = findViewById(R.id.register_btn);

        // Set Listeners
        login_btn.setOnClickListener(this);
        register_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent next_activity;

        if(v == findViewById(R.id.login_btn)) {
            next_activity = new Intent(this, DashboardActivity.class);
        }
        else if(v == findViewById(R.id.register_btn)) {
            next_activity = new Intent(this, RegisterActivity.class);
        }
        else {
            next_activity = null;
        }

        if(next_activity != null) {
            startActivity(next_activity);
        }
    }
}
