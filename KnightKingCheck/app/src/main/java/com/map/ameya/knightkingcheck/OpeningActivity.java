package com.map.ameya.knightkingcheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.ImageButton;

public class OpeningActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    ImageButton play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        play = (ImageButton) findViewById(R.id.play);

        ViewPropertyAnimator vp = play.animate();
        vp.translationZBy(getResources().getDimension(R.dimen.translate_dist));

        play.setBackground(getResources().getDrawable(R.drawable.black_play_button));

        play.setOnClickListener(this);
        play.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.play)) {
            Intent startGame = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(startGame);
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v == findViewById(R.id.play)) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                play.setBackground(getResources().getDrawable(R.drawable.border_black_button));
            } else {
                play.setBackground(getResources().getDrawable(R.drawable.black_play_button));
            }
        }
        return false;
    }

}
