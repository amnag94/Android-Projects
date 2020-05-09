package com.example.ameyadeepaknagnur.thefood.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ameyadeepaknagnur.thefood.Activities.HomeActivity;
import com.example.ameyadeepaknagnur.thefood.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent home_page = new Intent(this, HomeActivity.class);
        startActivity(home_page);
    }
}
