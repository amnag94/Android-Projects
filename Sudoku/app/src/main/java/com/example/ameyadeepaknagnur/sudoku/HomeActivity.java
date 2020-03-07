package com.example.ameyadeepaknagnur.sudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button play_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initializations
        play_btn = findViewById(R.id.play_btn);

        // Listeners
        play_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.play_btn)) {
            Intent game_intent = new Intent(this, PuzzleActivity.class);
            startActivity(game_intent);
        }
    }
}
