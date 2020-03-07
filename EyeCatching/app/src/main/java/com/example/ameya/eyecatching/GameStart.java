package com.example.ameya.eyecatching;

        import android.content.Intent;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.os.CountDownTimer;
        import android.support.v7.app.ActionBarActivity;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewPropertyAnimator;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

public class GameStart extends ActionBarActivity implements OnClickListener{

    Button [] c = new Button[15];
    int [] id = {R.id.button1,R.id.button2,R.id.button3,R.id.button4,R.id.button5,R.id.button6,R.id.button7,R.id.button8,R.id.button9,R.id.button10,R.id.button11,R.id.button12,R.id.button13,R.id.button14,R.id.button15};
    int q,cs,n;
    TextView tv,lu,l2;
    Toast t,x;
    static CountDownTimer ct;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        int r = (int)(((Math.random()*1000)%239)+1);
        int g = (int)((Math.random()*1000)%240);
        int b = (int)((Math.random()*1000)%240);
        Score.count++;
        if(Score.count < 5)
        {
            n = 9;
            setContentView(R.layout.activity_game_start);
        }
        else
        {
            n = 15;
            setContentView(R.layout.activity_main2);

            tv = (TextView) findViewById(R.id.timer);
            lu = (TextView) findViewById(R.id.levelup);
            l2 = (TextView) findViewById(R.id.level2);

            if(Score.count >= 5)
            {
                lu.setText("Level 1!");
            }
            if(Score.count >= 13)
            {
                lu.setText("Level 2!");
                l2.setText("Wrong = Time - 1\nCorrect = Time + 1");
            }

            ct = new CountDownTimer(Score.timer,1000) {

                public void onTick(long millisUntilFinished) {
                    tv.setText("Time : " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    gameOver();
                }
            };
            ct.start();
        }
        int i = 0,j = 0;
        cs = (int)((Math.random()*7)+2);
        if(Score.count < 5)
            q = (int)(Math.random()*cs);
        else
        {
            q = (int)(Math.random()*15);
            cs = 15;
        }
        while(i<n)
        {
            c[i] = (Button) findViewById(id[j]);
            if(i>cs)
                c[i].setVisibility(View.INVISIBLE);
            c[i].setHeight(10);
            c[i].setWidth(10);
            c[i].setOnClickListener(this);
            if(i!=q)
                c[i].setBackgroundColor(Color.rgb(r+15, g+15, b+15));
            j++;
            i++;
        }
        if(Score.count < 5 && Score.count > 0 )
        {
            c[q].setBackgroundColor(Color.rgb(r, g, b));
            Score.score += 1;
        }
        else
        {
            c[q].setBackgroundColor(Color.rgb(r+5, g+5, b+5));
            Score.score += 3;
        }
    }

    @Override
    public void onBackPressed()
    {
        if(Score.count >= 5)
        {
            Score.count = 0;
            Score.score = 0;
            ct.cancel();
            Score.timer = 9000;
        }
        Intent j = new Intent(this, MainActivity.class);
        startActivity(j);
    };

    public void onPaused()
    {
        ct.cancel();
        Score.timer = 9000;
    }

    public void onClick(View v)
    {
        if(v == findViewById(id[q]))
        {
            if(Score.count >= 5)
                ct.cancel();
            t = Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT);
            t.show();
            if(Score.count == 12)
                Score.timer = 15000;
            if(Score.count >= 13)
                Score.timer += 1000;
            Intent j = new Intent(this, GameStart.class);
            startActivity(j);
        }
        else
        {
            if(Score.count >= 13)
                Score.timer -= 1000;
            ViewPropertyAnimator vp = v.animate();
            vp.rotationXBy(90);
        }
    }

    private void gameOver() {
        Toast.makeText(getApplicationContext(), "Time Out", Toast.LENGTH_SHORT).show();
        Intent x = new Intent(this, GameOver.class);
        startActivity(x);
    }
}
