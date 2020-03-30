package com.example.honestherd.HHActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.honestherd.HHGlobal.HHSharedPrefrence;
import com.example.honestherd.MainActivity;
import com.example.honestherd.R;

public class HHSplash_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_h_splash_activity);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (HHSharedPrefrence.getLogin(HHSplash_activity.this)){
                    Intent i = new Intent(HHSplash_activity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(HHSplash_activity.this, HHTerms_activity.class);
                    startActivity(i);
                    finish();
                }
                // This method will be executed once the timer is over

            }
        }, 3000);
    }
}
