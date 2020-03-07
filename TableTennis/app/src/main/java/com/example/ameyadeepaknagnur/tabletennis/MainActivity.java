package com.example.ameyadeepaknagnur.tabletennis;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton player1;
    ImageButton player2;
    Button start;
    ImageView ball;
    TextView score_p1, score_p2, player1_txt, player2_txt;
    int p1_score, p2_score;
    int p1_location [], p2_location [];
    double player1_x, player1_y;
    double player2_x, player2_y;
    int click_counter1, click_counter2;
    int movement_counter;
    int towardsPlayer;
    TimerTask timerTask;
    Timer timer;
    Thread scoreThread;
    boolean scoreUpdate;
    final Handler handler = new Handler();
    double window_height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI items
        player1 = findViewById(R.id.player_racket1);
        player2 = findViewById(R.id.player_racket2);
        ball = findViewById(R.id.ball);
        score_p1 = findViewById(R.id.score_p1);
        score_p2 = findViewById(R.id.score_p2);
        player1_txt = findViewById(R.id.player_1Text);
        player2_txt = findViewById(R.id.player_2Text);
        start = findViewById(R.id.start);

        start.setVisibility(View.VISIBLE);

        // Get coordinates of rackets
        //p1_location = new int[2];
        //p2_location = new int[2];
        //player1.getLocationOnScreen(p1_location);
        //player2.getLocationOnScreen(p2_location);

        // Get window coordinates
        Point maxHeight =  new Point();
        getWindowManager().getDefaultDisplay().getSize(maxHeight);
        window_height = maxHeight.y;

        // Height is start i.e. 0 for p1 and window height - racket_height for p2
        player1_x = player1.getX();//p1_location[0];
        player1_y = 0;//p1_location[1];
        player2_x = player2.getX();//p2_location[0];
        player2_y = window_height - 400;//getResources().getDimension(R.dimen.racket_height);//p2_location[1];

        Log.d("p1 x : ", String.valueOf(player1_x));
        Log.d("p1 y : ", String.valueOf(player1_y));
        Log.d("p2 x : ", String.valueOf(player2_x));
        Log.d("p2 y : ", String.valueOf(player2_y));

        // Set initial position
        //ball.setY((float) player1_y);

        // Score initialization
        p1_score = 0;
        p2_score = 0;
        score_p1.setText("Score : " + p1_score);
        score_p2.setText("Score : " + p2_score);

        // Set listeners
        player1.setOnClickListener(this);
        click_counter1 = 0;
        player2.setOnClickListener(this);
        click_counter2 = 0;
        start.setOnClickListener(this);
    }

    @Override
    public void onStop()
    {
        if(timerTask != null) {
            timerTask.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }
        if (scoreThread != null) {
            scoreThread.interrupt();
        }
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.player_racket1))
        {
            click_counter2 = 0;
            click_counter1++;
            if (ball.getY() <= player1_y) {
                movement_counter = 0;
                towardsPlayer = 1;
                player1.setClickable(false);
                player2.setClickable(true);
            }
            //ball.setTranslationY(10 * click_counter1);
        }
        else if(v == findViewById(R.id.player_racket2))
        {
            click_counter1 = 0;
            click_counter2++;
            if (ball.getY() >= player2_y) { //&& ball.getY() <= window_height) {
                movement_counter = 0;
                towardsPlayer = -1;
                player2.setClickable(false);
                player1.setClickable(true);
            }
            //ball.setTranslationY(-10 * click_counter2);
        }
        else if (v == findViewById(R.id.start)) {
            // Make start button disappear
            start.setVisibility(View.GONE);

            player1.setClickable(true);
            player2.setClickable(true);

            // Timer for movement
            movement_counter = 0;
            towardsPlayer = 1;

            timerTask = new TimerTask() {
                @Override
                public void run() {
                    ball.setTranslationY(towardsPlayer * movement_counter * 10);
                    Log.d("Ball y : ", String.valueOf(ball.getY()));
                    movement_counter++;
                    if(towardsPlayer == -1 && ball.getY() <= player1_y - 100.0) {
                        p2_score++;
                        if(scoreThread != null) {
                            runOnUiThread(scoreThread);
                        }
                        movement_counter = 0;
                    }
                    if(towardsPlayer == 1 && ball.getY() >= window_height) {
                        p1_score++;
                        if(scoreThread != null) {
                            runOnUiThread(scoreThread);
                        }
                        movement_counter = 0;
                    }
                }


            };

            //scoreUpdate = true;

            scoreThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //while(scoreUpdate) {
                    score_p1.setText("Score : " + p1_score);
                    score_p2.setText("Score : " + p2_score);

                    if (p1_score == 11 || p2_score == 11) {

                        // Score initialization
                        p1_score = 0;
                        p2_score = 0;
                        score_p1.setText("Score : " + p1_score);
                        score_p2.setText("Score : " + p2_score);

                        if (p1_score == 11) {
                            player1_txt.setText("Player 1 (Winner)");
                            player2_txt.setText("Player 2");
                        }
                        else if (p2_score == 11) {
                            player2_txt.setText("Player 2 (Winner)");
                            player1_txt.setText("Player 1");
                        }

                        //scoreUpdate = false;
                    }

                    // Reset for next round
                    if (timer != null) {
                        timer.cancel();
                        if (timerTask != null) {
                            timerTask.cancel();
                        }
                    }

                    // Bring start back for starting next
                    start.setVisibility(View.VISIBLE);
                    //}
                }
            });

            if (timerTask != null) {
                this.runOnUiThread(timerTask);
                timer = new Timer();
                timer.schedule(timerTask, 0, 10);
            }
        }
        /*scoreThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(scoreUpdate) {
                    score_p1.setText("Score : " + p1_score);
                    score_p2.setText("Score : " + p2_score);

                    if (p1_score == 7 || p2_score == 7) {
                        if (timer != null) {
                            timer.cancel();
                            if (timerTask != null) {
                                timerTask.cancel();
                            }
                        }
                        scoreUpdate = false;
                    }
                }
            }
        });


        if(scoreThread != null) {
            this.runOnUiThread(scoreThread);
        }*/
    }
}
