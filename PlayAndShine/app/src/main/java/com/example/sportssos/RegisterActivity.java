package com.example.sportssos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.SharedElementCallback;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    int SELECTED, UNSELECTED;
    CardView card_type_coach, card_type_athlete;
    TextView txt_type_coach, txt_type_athlete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initializations
        card_type_athlete = findViewById(R.id.card_type_athlete);
        card_type_coach = findViewById(R.id.card_type_coach);
        txt_type_athlete = findViewById(R.id.text_type_athlete);
        txt_type_coach = findViewById(R.id.text_type_coach);
        SELECTED = getResources().getColor(R.color.Blue);
        UNSELECTED = getResources().getColor(R.color.Black);

        // Set Listeners
        card_type_athlete.setOnClickListener(this);
        card_type_coach.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == findViewById(R.id.card_type_athlete)) {
            txt_type_athlete.setTextColor(SELECTED);
            txt_type_coach.setTextColor(UNSELECTED);
        }
        else if (v == findViewById(R.id.card_type_coach)) {
            txt_type_athlete.setTextColor(UNSELECTED);
            txt_type_coach.setTextColor(SELECTED);
        }
        else {

        }

    }
}
