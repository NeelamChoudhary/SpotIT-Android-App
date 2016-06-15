package com.example.tanyasanjay.giveandtake;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class splashScreen extends AppCompatActivity {

    public static int spalsh_Screen_Delay = 3000;
    EditText input;
    int MY_PERMISSIONS_REQUEST_CAMERA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //neelam
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.CAMERA},
//                MY_PERMISSIONS_REQUEST_CAMERA);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splashScreen.this, Login.class);
                startActivity(intent);

                finish();
            }
        }, spalsh_Screen_Delay);
    }
}
