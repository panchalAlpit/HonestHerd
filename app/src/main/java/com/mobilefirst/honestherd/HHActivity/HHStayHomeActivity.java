package com.mobilefirst.honestherd.HHActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mobilefirst.honestherd.HHGlobal.Utils;
import com.mobilefirst.honestherd.MainActivity;
import com.mobilefirst.honestherd.R;

public class HHStayHomeActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatImageView img_close_stayhome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_h_stay_home);

        Init();
    }

    private void Init() {
        img_close_stayhome = findViewById(R.id.img_close_stayhome);
        img_close_stayhome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_close_stayhome:{
                Intent intent = new Intent(HHStayHomeActivity.this, MainActivity.class);
                intent.putExtra("screen", Utils.FRAGMENT_MAP);
               // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            }
        }
    }
}
