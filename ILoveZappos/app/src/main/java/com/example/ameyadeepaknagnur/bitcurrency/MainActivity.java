package com.example.ameyadeepaknagnur.bitcurrency;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Thread to make activity display as a splash screen.
        Thread splashTimer = new Thread(){
            public void run()
            {
                try
                {
                    sleep(2000);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent home_activity = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(home_activity);
                }
            }
        };
        splashTimer.start();

    }

    @Override
    public void onBackPressed() {

    }


}
