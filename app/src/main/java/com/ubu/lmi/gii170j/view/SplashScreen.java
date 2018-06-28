package com.ubu.lmi.gii170j.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.ubu.lmi.gii170j.R;

public class SplashScreen extends Activity {

    private static int splashInterval = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, Launcher.class);
                startActivity(i);

                this.finish();
            }

            private void finish(){
                // Required empty private method.
            }
        },splashInterval);

    }


}
