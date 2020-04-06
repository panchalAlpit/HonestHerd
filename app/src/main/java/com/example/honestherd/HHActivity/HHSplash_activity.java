package com.example.honestherd.HHActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;

import com.example.honestherd.HHGlobal.HHSharedPrefrence;
import com.example.honestherd.HHGlobal.Utils;
import com.example.honestherd.MainActivity;
import com.example.honestherd.R;

public class HHSplash_activity extends AppCompatActivity {

    AppCompatTextView txt_title_splashscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_h_splash_activity);

        txt_title_splashscreen = findViewById(R.id.txt_title_splashscreen);
        txt_title_splashscreen.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));

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
