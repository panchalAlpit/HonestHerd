package com.example.honestherd;

import androidx.annotation.NonNull;
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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.example.honestherd.HHActivity.HHLogin_activity;
import com.example.honestherd.HHFregment.HHFeeling_well_Fragment;
import com.example.honestherd.HHFregment.HHMap_fregment;
import com.example.honestherd.HHFregment.HHTripHistory;
import com.example.honestherd.HHGlobal.HHSharedPrefrence;
import com.example.honestherd.HHGlobal.Utils;
import com.example.honestherd.HHWebService.HHApiCall;
import com.example.honestherd.HHWebService.OnUpdateListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hypertrack.sdk.CoreSDKProvider;
import com.hypertrack.sdk.HyperTrack;
import com.hypertrack.sdk.TrackingError;
import com.hypertrack.sdk.TrackingStateObserver;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_ACCESS_FINE_LOCATION = 10;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    Map<String, Object> param = new HashMap<>();
    AppCompatImageView img_drawer_menu, txt_cancel_drawer;
    public static LinearLayout linear_dateselect;
    public static HyperTrack hyperTrack;
    LinearLayout linear_menu_history;
    private AppCompatTextView txt_viewmap, txt_datapolicy, txt_my_coins,txt_share_world,txt_assessment_tool,txt_export_my_path,txt_near_test_center,txt_follow_twitter;
    AppCompatTextView txt_clipboard,txt_diagnosis_project,txt_xml_export,txt_sub_near_test_center,txt_privacy_seriously,txt_news_version,txt_sub_my_coins,txt_betheherd,txt_total_coins;
//    AppCompatTextView txt_delete_account,txt_emergency_info,txt_logout;
    public static AppCompatTextView txt_date_map_fregment;
    public static AppCompatTextView txt_month_map_fregment;
    DrawerLayout drawer;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;



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
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        HyperTrack.enableDebugLogging();
        hyperTrack = HyperTrack.getInstance(MainActivity.this, "7pXsJ2QfvGFLjzyJG-dNUm99YT9iiqd0UsXNV6qHb9wPr0ebWQDlYmjEYMPn8KNjC8x-DA-19Yjg2urO8w9DPw");
        hyperTrack.requestPermissionsIfNecessary();
        hyperTrack.start();

        if (firebaseUser.getPhoneNumber() != null) {
            hyperTrack.setDeviceName(firebaseUser.getUid());
        }
        initMathod();
        if (HHSharedPrefrence.getAddData(MainActivity.this)) {
            addUserDetails();
        }
        Log.e("TAG", "onCreate: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//        Log.e("ssss", "onCreate: " + firebaseUser.getPhoneNumber());
//        Log.e("ssss", "onCreate: " + hyperTrack.getDeviceID());

        FetchTotalCoins();
    }


    private void initMathod() {
//        addFragment();
        addFragementFeeling_well();
        txt_share_world  =findViewById(R.id.txt_share_world);
        txt_assessment_tool = findViewById(R.id.txt_assessment_tool);
        txt_export_my_path = findViewById(R.id.txt_export_my_path);
        txt_near_test_center = findViewById(R.id.txt_near_test_center);
        txt_follow_twitter = findViewById(R.id.txt_follow_twitter);
        txt_clipboard = findViewById(R.id.txt_clipboard);
        txt_diagnosis_project = findViewById(R.id.txt_diagnosis_project);
        txt_xml_export = findViewById(R.id.txt_xml_export);
        txt_sub_near_test_center = findViewById(R.id.txt_sub_near_test_center);
        txt_privacy_seriously = findViewById(R.id.txt_privacy_seriously);
        txt_news_version = findViewById(R.id.txt_news_version);
        txt_sub_my_coins = findViewById(R.id.txt_sub_my_coins);
        txt_betheherd= findViewById(R.id.txt_betheherd);
        txt_total_coins = findViewById(R.id.txt_total_coins);

        txt_my_coins = findViewById(R.id.txt_my_coins);
//        txt_delete_account = findViewById(R.id.txt_delete_account);
        txt_datapolicy = findViewById(R.id.txt_datapolicy);
//        txt_emergency_info = findViewById(R.id.txt_emergency_info);
        txt_viewmap = findViewById(R.id.txt_viewmap);
//        txt_logout = findViewById(R.id.txt_logout);
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
        txt_share_world.setOnClickListener(this);
        txt_assessment_tool.setOnClickListener(this);
        txt_export_my_path.setOnClickListener(this);
        txt_near_test_center.setOnClickListener(this);
        txt_follow_twitter.setOnClickListener(this);


//        txt_logout.setOnClickListener(this);
//        txt_emergency_info.setOnClickListener(this);
//        txt_delete_account.setOnClickListener(this);

        txt_total_coins.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_share_world.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_assessment_tool.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_export_my_path.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_near_test_center.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_follow_twitter.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_betheherd.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_clipboard.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
        txt_diagnosis_project.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
        txt_xml_export.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
        txt_sub_near_test_center.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
        txt_privacy_seriously.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
        txt_news_version.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
        txt_sub_my_coins.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));

        txt_my_coins.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
//        txt_logout.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
//        txt_delete_account.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_viewmap.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
//        txt_emergency_info.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_datapolicy.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_date_map_fregment.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_month_map_fregment.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
        setCurrentDate();
    }

    private void setCurrentDate() {
        txt_date_map_fregment.setText(Utils.getDateFromate("dd"));
        txt_month_map_fregment.setText(Utils.getDateFromate("MMM yyyy"));
    }

    public void addHistoryFragment() {
        Fragment f = new HHTripHistory();
        FragmentManager manager = getSupportFragmentManager();
        String backStateName = f.getClass().getName();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, f, "history");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addFragment() {
        //first time call this method
        Fragment f = new HHMap_fregment();
        FragmentManager manager = getSupportFragmentManager();
        String backStateName = f.getClass().getName();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, f, Utils.FRAGMENT_MAP);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addFragementFeeling_well() {
        Fragment f = new HHFeeling_well_Fragment();
        FragmentManager manager = getSupportFragmentManager();
        String backStateName = f.getClass().getName();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, f, Utils.FRAGMENT_feeling);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_drawer_menu: {
                drawer.openDrawer(Gravity.RIGHT);
                break;
            }
            case R.id.txt_cancel_drawer: {
                drawer.closeDrawers();
                break;
            }
            case R.id.txt_viewmap: {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
                Log.e("TAG", "onClick: " + f.getTag());
                if (!f.getTag().equals(Utils.FRAGMENT_MAP)) {
                    addFragment();
                }
                CloseDrawer();
                break;
            }
            case R.id.txt_share_world:{
                OpenBrowser();
                break;
            }
            case R.id.txt_assessment_tool:{
                OpenBrowser();
                break;
            }
            case R.id.txt_near_test_center:{
                OpenBrowser();
                break;
            }
            case R.id.txt_datapolicy:{
                OpenBrowser();
            }
            case R.id.txt_follow_twitter:{
//                OpenBrowser();
                FirebaseAuth.getInstance().signOut();
                CloseDrawer();
                HHSharedPrefrence.SetLogin(MainActivity.this, false);
                HHSharedPrefrence.ClearSession(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, HHLogin_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//mak
                startActivity(intent);
                break;
            }
            /*case R.id.txt_logout: {
                FirebaseAuth.getInstance().signOut();
                CloseDrawer();
                HHSharedPrefrence.SetLogin(MainActivity.this, false);
                HHSharedPrefrence.ClearSession(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, HHLogin_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//mak
                startActivity(intent);
                break;
            }
            case R.id.txt_emergency_info: {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
                Log.e("TAG", "onClick: " + f.getTag());
                if (!f.getTag().equals(Utils.FRAGMENT_feeling)) {
                    addFragementFeeling_well();
                }
                CloseDrawer();
                break;
            }
            case R.id.txt_delete_account: {

                break;
            }*/
        }
    }

    void FetchTotalCoins(){
        firebaseFirestore.collection(Utils.USERPOINTS)
                .whereEqualTo(Utils.FIREBASE_USERID, firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAGID", document.getId() + " => " + document.getId());
                                Log.d("TAGID", document.getId() + " => " + document.get("totalPoints")+" --- "+firebaseUser.getUid());
                                txt_total_coins.setText(document.get("totalPoints").toString());
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    void OpenBrowser(){
        Intent intent = new Intent(Intent.ACTION_VIEW, Utils.WEBURL);
        startActivity(intent);
        CloseDrawer();
    }

    void CloseDrawer() {
        drawer.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.frame_layout) instanceof HHFeeling_well_Fragment) {
            finish();
        } else {
            super.onBackPressed();
            if (getSupportFragmentManager().findFragmentById(R.id.frame_layout) instanceof HHFeeling_well_Fragment) {
                setCurrentDate();
            }

        }
    }

    void addUserDetails() {
        // Create a new user with a first and last name
        TimeZone tz = TimeZone.getDefault();
        System.out.println("TimeZone   "+tz.getDisplayName(false, TimeZone.SHORT)+" Timezon id :: " +tz.getID());

        Map<String, Object> user = new HashMap<>();
        user.put("firebaseUserID", firebaseUser.getUid());
        user.put("hyperTrackDeviceID", hyperTrack.getDeviceID());
        user.put("phoneNumber", firebaseUser.getPhoneNumber());
        user.put("timezone", tz.getID());

// Add a new document with a generated ID
        firebaseFirestore.collection("userInfo")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        HHSharedPrefrence.addUserDetails(MainActivity.this, false);
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void DeleteAccount_data() {

        new HHApiCall(MainActivity.this, Utils.HyperTrack_URL + hyperTrack.getDeviceID(), new OnUpdateListener() {
            @Override
            public void onUpdateComplete(JSONObject jsonObject, boolean isSuccess) {
                if (!isSuccess) {

                }
            }
        }).execute();
    }

}


//https://demonuts.com/android-google-map-in-fragment/