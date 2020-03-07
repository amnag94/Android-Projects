package com.example.ameya.eyecatching;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.ActionBarActivity;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;

public class HowToPlay extends ActionBarActivity implements OnClickListener{

    Button n,s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);
        n = (Button) findViewById(R.id.next);
        s = (Button) findViewById(R.id.skip);
        n.setOnClickListener(this);
        s.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        //db.execSQL("Insert into abc values(1 , 2)");
        if(v == findViewById(R.id.next))
        {
            Intent x = new Intent(this, HowToPlay2.class);
            startActivity(x);
        }
        else
        {
            Intent x = new Intent(this, MainActivity.class);
            startActivity(x);
            //ct.onFinish();
        }
    }


}
