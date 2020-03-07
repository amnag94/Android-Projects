package com.example.ameya.eyecatching;

        import com.google.android.gms.ads.AdRequest;
        import com.google.android.gms.ads.InterstitialAd;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.support.v7.app.ActionBarActivity;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;


public class GameOver extends ActionBarActivity implements OnClickListener{


    private InterstitialAd mInterstitialAd;
    int hscore;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(this);
        getActionBar().setTitle("Eye Catching");
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");


        TextView t = (TextView) findViewById(R.id.score);
        t.setText(""+Score.score);
        b = (Button) findViewById(R.id.again);
        SharedPreferences sp = getSharedPreferences("high", Context.MODE_PRIVATE);
        hscore = sp.getInt("hscore", 0);
        if(hscore == 0){
            updatePreference(sp);
        }
        else
        {
            if(hscore < Score.score)
            {
                updatePreference(sp);
            }

        }

        TextView th = (TextView) findViewById(R.id.hscore);
        th.setText(""+hscore);
        b.setOnClickListener(this);
    }

    private void updatePreference(SharedPreferences sp) {
        hscore = Score.score;
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("hscore", Score.score);
        ed.commit();
    }

    @Override
    public void onBackPressed()
    {
        Score.count = 0;
        Score.score = 0;
        Score.timer = 9000;
        Intent j = new Intent(this, MainActivity.class);
        startActivity(j);
    };

    @Override
    public void onClick(View v) {
        showInterstitial();
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            startGame();
        }
    }

    private void startGame() {
        // Hide the retry button, load the ad, and start the timer.
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
        Score.count = 0;
        Score.score = 0;
        Score.timer = 9000;
        Intent j = new Intent(this, MainActivity.class);
        startActivity(j);
    }

}
