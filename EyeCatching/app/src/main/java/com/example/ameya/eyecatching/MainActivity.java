package com.example.ameya.eyecatching;

        import android.app.Activity;
        import android.content.Intent;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.os.CountDownTimer;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
{

    Button  p,r;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        p = (Button) findViewById(R.id.play);
        r = (Button) findViewById(R.id.rules);
        p.setOnClickListener(this);
        r.setOnClickListener(this);
        //GameStart.ct.cancel();
        Score.timer = 9000;
        Score.count = 0;
    }

    @Override
    public void onBackPressed()
    {
        MainActivity.this.finish();
        //Intent j = new Intent(this, MainActivity.class);
        //startActivity(j);
    };

    public void onClick(View v)
    {
        //db.execSQL("Insert into abc values(1 , 2)");
        if(v == findViewById(R.id.play))
        {
            gameStart();
        }
        else
        {
            Intent x = new Intent(this, HowToPlay.class);
            startActivity(x);
            //ct.onFinish();
        }
    }

    private void gameStart() {
        Toast.makeText(getApplicationContext(), "Start", Toast.LENGTH_SHORT).show();
        Intent x = new Intent(this, GameStart.class);
        startActivity(x);
    }
}
