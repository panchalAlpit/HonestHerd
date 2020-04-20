package com.mobilefirst.honestherd.HHActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;

import com.mobilefirst.honestherd.HHGlobal.HHSharedPrefrence;
import com.mobilefirst.honestherd.HHGlobal.Utils;
import com.mobilefirst.honestherd.MainActivity;
import com.mobilefirst.honestherd.R;

public class HHSplash_activity extends AppCompatActivity {

    AppCompatTextView txt_title_splashscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_h_splash_activity);

        txt_title_splashscreen = findViewById(R.id.txt_title_splashscreen);
        txt_title_splashscreen.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels;
        Log.e("onCreate:", "heightPixels:- "+displayMetrics.heightPixels+" widthPixels:- "+displayMetrics.widthPixels );

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (HHSharedPrefrence.getLogin(HHSplash_activity.this)){
                    Intent i = new Intent(HHSplash_activity.this, HHHealthStatusActivity.class);
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
