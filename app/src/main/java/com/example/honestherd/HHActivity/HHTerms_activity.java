package com.example.honestherd.HHActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.example.honestherd.HHGlobal.Utils;
import com.example.honestherd.MainActivity;
import com.example.honestherd.R;

public class HHTerms_activity extends AppCompatActivity {

    AppCompatTextView txt_agree,txt_termof_use,txt_contnet_terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_h_terms_activity);
        txt_agree = findViewById(R.id.txt_agree);
        txt_termof_use = findViewById(R.id.txt_termof_use);
        txt_contnet_terms = findViewById(R.id.txt_contnet_terms);

        txt_agree.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_termof_use.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_contnet_terms.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));

        txt_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HHTerms_activity.this, HHLogin_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
