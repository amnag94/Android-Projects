package com.example.ameya.eyecatching;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.ActionBarActivity;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;

public class HowToPlay2 extends ActionBarActivity implements OnClickListener{

    Button b,p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play2);
        b = (Button) findViewById(R.id.back);
        p = (Button) findViewById(R.id.play);
        b.setOnClickListener(this);
        p.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        //db.execSQL("Insert into abc values(1 , 2)");
        if(v == findViewById(R.id.back))
        {
            Intent x = new Intent(this, HowToPlay.class);
            startActivity(x);
        }
        else
        {
            Intent x = new Intent(this, GameStart.class);
            startActivity(x);
            //ct.onFinish();
        }
    }
}
