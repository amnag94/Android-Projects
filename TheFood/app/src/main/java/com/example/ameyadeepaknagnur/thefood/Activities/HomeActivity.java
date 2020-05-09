package com.example.ameyadeepaknagnur.thefood.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import com.example.ameyadeepaknagnur.thefood.Helpers.Constants;
import com.example.ameyadeepaknagnur.thefood.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    CardView add_list, edit_list, lets_cook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initializations
        add_list = findViewById(R.id.card_container_add);
        edit_list = findViewById(R.id.card_container_edit);
        lets_cook = findViewById(R.id.card_container_cook);

        // Listeners
        add_list.setOnClickListener(this);
        edit_list.setOnClickListener(this);
        lets_cook.setOnClickListener(this);

        // Request Permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            // Requires API level 23
            requestPermissions(new String[] {Manifest.permission.INTERNET}, 1);
        }
    }

    @Override
    public void onClick(View view) {
        Intent click_intent = null;

        switch (view.getId()) {
            case R.id.card_container_add :
                click_intent = new Intent(this, AddListActivity.class);
                break;
            case R.id.card_container_edit :
                click_intent = new Intent(this, EditListActivity.class);
                break;
            case R.id.card_container_cook :
                click_intent = new Intent(this, LetsCookActivity.class);
                break;
            default :
                Log.d(Constants.DEFAULT_CLICK_TAG,Constants.DEFAULT_CLICK_MSG);
        }

        if (click_intent != null) {
            startActivity(click_intent);
        }
    }
}
