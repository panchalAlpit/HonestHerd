package com.example.honestherd;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.example.honestherd.HHFregment.HHMap_fregment;
import com.example.honestherd.HHFregment.HHTripHistory;
import com.example.honestherd.HHGlobal.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hypertrack.sdk.HyperTrack;
import com.hypertrack.sdk.TrackingError;
import com.hypertrack.sdk.TrackingStateObserver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements   View.OnClickListener {

    public static final int REQUEST_ACCESS_FINE_LOCATION = 10;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    Map<String, Object> param = new HashMap<>();

    AppCompatImageView img_drawer_menu,txt_cancel_drawer;
    public static LinearLayout linear_dateselect;
    public static HyperTrack hyperTrack;
    LinearLayout linear_menu_history;
    private AppCompatTextView txt_viewmap;
    public static AppCompatTextView txt_date_map_fregment;
    public static AppCompatTextView txt_month_map_fregment;
    DrawerLayout drawer;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        hyperTrack = HyperTrack.getInstance(MainActivity.this, "7pXsJ2QfvGFLjzyJG-dNUm99YT9iiqd0UsXNV6qHb9wPr0ebWQDlYmjEYMPn8KNjC8x-DA-19Yjg2urO8w9DPw");
        hyperTrack.requestPermissionsIfNecessary();
        hyperTrack.start();
        hyperTrack.setDeviceName(firebaseUser.getPhoneNumber().toString());
        initMathod();

        Log.e("TAG", "onCreate: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        Log.e("ssss", "onCreate: " + firebaseUser.getPhoneNumber());
//        Log.e("ssss", "onCreate: " + hyperTrack.getDeviceID());
    }


    private void initMathod() {
        addFragment();
        txt_viewmap = findViewById(R.id.txt_viewmap);
        img_drawer_menu = findViewById(R.id.img_drawer_menu);
        txt_cancel_drawer = findViewById(R.id.txt_cancel_drawer);
        drawer = findViewById(R.id.drawer);
        linear_dateselect = findViewById(R.id.linear_dateselect);
        linear_dateselect.setOnClickListener(this);
        txt_date_map_fregment = findViewById(R.id.txt_date_map_fregment);
        txt_month_map_fregment = findViewById(R.id.txt_month_map_fregment);
        img_drawer_menu.setOnClickListener(this);
        txt_cancel_drawer.setOnClickListener(this);
        txt_viewmap.setOnClickListener(this);

        txt_date_map_fregment.setText(Utils.getDateFromate("dd"));
        txt_month_map_fregment.setText(Utils.getDateFromate("MMM yyyy"));
    }

   public void addHistoryFragment(){
       Fragment f = new HHTripHistory();
       FragmentManager manager = getSupportFragmentManager();
       String backStateName = f.getClass().getName();
       FragmentTransaction transaction = manager.beginTransaction();
       transaction.replace(R.id.frame_layout, f,"history");
       transaction.addToBackStack(null);
       transaction.commit();
   }
    public void addFragment() {
        //first time call this method
        Fragment f = new HHMap_fregment();
        FragmentManager manager = getSupportFragmentManager();
        String backStateName = f.getClass().getName();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, f,Utils.FRAGMENT_MAP);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("", "onResume");


//        hyperTrack.requestPermissionsIfNecessary();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
//        hyperTrack.removeTrackingListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_drawer_menu:{
            drawer.openDrawer(Gravity.RIGHT);
            break;
            }
            case R.id.txt_cancel_drawer:{
                drawer.closeDrawers();
                break;
            }
            case R.id.txt_viewmap:{
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
                Log.e("TAG", "onClick: "+f.getTag() );
                if (!f.getTag().equals(Utils.FRAGMENT_MAP)){
                    addFragment();
                }
                CloseDrawer();
//
                break;
            }
        }
    }

    void CloseDrawer(){
        drawer.closeDrawers();
    }


}


//https://demonuts.com/android-google-map-in-fragment/