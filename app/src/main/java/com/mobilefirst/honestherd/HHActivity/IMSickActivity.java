package com.mobilefirst.honestherd.HHActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.mobilefirst.honestherd.HHGlobal.Utils;
import com.mobilefirst.honestherd.R;

public class IMSickActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatTextView txt_covid19,txt_influenza,txt_none_above,txt_my_doctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_m_sick);

        init();

    }

    private void init() {
        txt_covid19 = findViewById(R.id.txt_covid19);
        txt_influenza = findViewById(R.id.txt_influenza);
        txt_none_above = findViewById(R.id.txt_none_above);
        txt_my_doctor = findViewById(R.id.txt_my_doctor);

        txt_covid19.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_influenza.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_none_above.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_my_doctor.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));

        txt_covid19.setOnClickListener(this);
        txt_influenza.setOnClickListener(this);
        txt_none_above.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){

          case R.id.txt_covid19:{
              NavigaionClass();
              break;
          }
          case R.id.txt_none_above:{
              NavigaionClass();
              break;
          }
          case R.id.txt_influenza:{
              NavigaionClass();
              break;
          }
      }
    }

   private void NavigaionClass(){
        Intent intent = new Intent(IMSickActivity.this,HHStayHomeActivity.class);
        startActivity(intent);
        finish();
    }
}
