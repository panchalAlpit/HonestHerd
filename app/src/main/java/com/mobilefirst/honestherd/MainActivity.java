package com.mobilefirst.honestherd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobilefirst.honestherd.HHActivity.HHCoinHistoryActivity;
import com.mobilefirst.honestherd.HHActivity.HHHealthStatusActivity;
import com.mobilefirst.honestherd.HHActivity.HHTerms_activity;
import com.mobilefirst.honestherd.HHActivity.IMSickActivity;
import com.mobilefirst.honestherd.HHFregment.HHFeeling_well_Fragment;
import com.mobilefirst.honestherd.HHFregment.HHMap_fregment;
import com.mobilefirst.honestherd.HHFregment.HHNextStepFragment;
import com.mobilefirst.honestherd.HHFregment.HHTripHistory;
import com.mobilefirst.honestherd.HHGlobal.HHSharedPrefrence;
import com.mobilefirst.honestherd.HHGlobal.Utils;
import com.mobilefirst.honestherd.HHWebService.HHDeleteApi;
import com.mobilefirst.honestherd.HHWebService.OnUpdateListener;
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
import com.hypertrack.sdk.HyperTrack;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_ACCESS_FINE_LOCATION = 10;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    Map<String, Object> param = new HashMap<>();
    AppCompatTextView txt_unique_user_id;
    AppCompatImageView img_drawer_menu, txt_cancel_drawer;
    public static LinearLayout linear_dateselect;
    public static HyperTrack hyperTrack;
    LinearLayout linear_menu_history,linear_totalcoin;
   // private AppCompatTextView txt_viewmap, txt_datapolicy, txt_my_coins,txt_share_world,txt_assessment_tool,txt_export_my_path,txt_dont_feel_well,txt_follow_twitter;
   // AppCompatTextView txt_clipboard,txt_diagnosis_project,txt_xml_export,txt_sub_near_test_center,txt_privacy_seriously,txt_news_version,txt_sub_my_coins,txt_betheherd,txt_total_coins,txt_delete_account,txt_incentive_partner,txt_privacypolicy_main;
//    AppCompatTextView txt_delete_account,txt_emergency_info,txt_logout;
    public static AppCompatTextView txt_date_map_fregment;
    public static AppCompatTextView txt_month_map_fregment;
    AppCompatTextView txt_totalcoin;
    DrawerLayout drawer;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    String screenName = "";
    AppCompatImageView img_globalmap,img_delete_account,img_help;

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

//        HyperTrack.enableDebugLogging();
        hyperTrack = HyperTrack.getInstance(MainActivity.this, "7pXsJ2QfvGFLjzyJG-dNUm99YT9iiqd0UsXNV6qHb9wPr0ebWQDlYmjEYMPn8KNjC8x-DA-19Yjg2urO8w9DPw");
        hyperTrack.start();
        hyperTrack.requestPermissionsIfNecessary();


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (firebaseUser.getPhoneNumber() != null)
        {
            hyperTrack.setDeviceName(firebaseUser.getUid());
        }

        screenName = getIntent().getStringExtra("screen");
        initMathod();
        if (HHSharedPrefrence.getAddData(MainActivity.this)) {
            addUserDetails();
        }
        Log.e("TAG", "onCreate: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//        Log.e("ssss", "onCreate: " + firebaseUser.getPhoneNumber());
        Log.e("hyperTrack", "onCreate:ID " + hyperTrack.getDeviceID());
        Log.e("FirebaseID", "onCreate:ID " + firebaseUser.getUid());

        FetchTotalCoins();
    }


    private void initMathod() {
//        addFragment();
        /*txt_privacypolicy_main = findViewById(R.id.txt_privacypolicy_main);
        txt_share_world  =findViewById(R.id.txt_share_world);
        txt_assessment_tool = findViewById(R.id.txt_assessment_tool);
        txt_export_my_path = findViewById(R.id.txt_export_my_path);
        txt_dont_feel_well = findViewById(R.id.txt_dont_feel_well);
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
        txt_delete_account = findViewById(R.id.txt_delete_account);
        txt_my_coins = findViewById(R.id.txt_my_coins);
        txt_datapolicy = findViewById(R.id.txt_datapolicy);
        txt_incentive_partner = findViewById(R.id.txt_incentive_partner);
        txt_viewmap = findViewById(R.id.txt_viewmap);*/

        txt_totalcoin = findViewById(R.id.txt_totalcoin);
        linear_totalcoin = findViewById(R.id.linear_totalcoin);

        img_globalmap = findViewById(R.id.img_globalmap);
        img_delete_account = findViewById(R.id.img_delete_account);
        img_help = findViewById(R.id.img_help);
        txt_unique_user_id = findViewById(R.id.txt_unique_user_id);

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

        linear_totalcoin.setOnClickListener(this);
        img_globalmap.setOnClickListener(this);
        img_delete_account.setOnClickListener(this);
        img_help.setOnClickListener(this);
//        txt_viewmap.setOnClickListener(this);
//        txt_share_world.setOnClickListener(this);
//        txt_assessment_tool.setOnClickListener(this);
//        txt_export_my_path.setOnClickListener(this);
//        txt_dont_feel_well.setOnClickListener(this);
//        txt_follow_twitter.setOnClickListener(this);

//        txt_delete_account.setOnClickListener(this);
//        txt_incentive_partner.setOnClickListener(this);
//        txt_privacypolicy_main.setOnClickListener(this);


//        txt_logout.setOnClickListener(this);
//        txt_emergency_info.setOnClickListener(this);
//        txt_delete_account.setOnClickListener(this);

//        txt_total_coins.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));

//        txt_share_world.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
//        txt_assessment_tool.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
//        txt_export_my_path.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
//        txt_dont_feel_well.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
//        txt_follow_twitter.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));

//        txt_betheherd.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
//        txt_clipboard.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
//        txt_diagnosis_project.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
//        txt_xml_export.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
//        txt_sub_near_test_center.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
//        txt_privacy_seriously.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
//        txt_news_version.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));
//        txt_sub_my_coins.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));

//        txt_my_coins.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
//        txt_viewmap.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
//        txt_datapolicy.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));

        txt_unique_user_id.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_date_map_fregment.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_month_map_fregment.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_MEDIUM));

       // addFragementFeeling_well();
        if (screenName.equals(Utils.FRAGMENT_MAP)){
            Log.e("FRAGMENT_MAP", "initMathod: " );
            addFragment();
        }else if (screenName.equals(Utils.FRAGMENT_NextStep)){
            Log.e("FRAGMENT_NextStep", "initMathod: " );
            AddNextStepFragment();
        }else if (screenName.equals(Utils.FRAGMENT_TIRPHISTORY)){
            Log.e("FRAGMENT_TIRPHISTORY", "initMathod: " );
         //   addHistoryFragment();
        }

        setCurrentDate();

        txt_unique_user_id.setText(firebaseUser.getUid());
    }

    private void setCurrentDate() {
        txt_date_map_fregment.setText(Utils.getDateFromate("dd"));
        txt_month_map_fregment.setText(Utils.getDateFromate("MMM yyyy"));
    }


    public void AddNextStepFragment() {
        Fragment f = new HHNextStepFragment();
        FragmentManager manager = getSupportFragmentManager();
        String backStateName = f.getClass().getName();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, f, Utils.FRAGMENT_NextStep);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addHistoryFragment(String date) {
//        linear_dateselect.setVisibility(View.VISIBLE);

        Bundle data = new Bundle();//create bundle instance
        data.putString("selectDate", date);

        Fragment f = new HHTripHistory();
        f.setArguments(data);
        FragmentManager manager = getSupportFragmentManager();
        String backStateName = f.getClass().getName();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.frame_layout, f, Utils.FRAGMENT_TIRPHISTORY);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addFragment() {
        //first time call this method

//        linear_dateselect.setVisibility(View.INVISIBLE);
        Fragment f = new HHMap_fregment();
        FragmentManager manager = getSupportFragmentManager();
        String backStateName = f.getClass().getName();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, f, Utils.FRAGMENT_MAP);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addFragementFeeling_well() {
//        linear_dateselect.setVisibility(View.INVISIBLE);
        Fragment f = new HHFeeling_well_Fragment();
        FragmentManager manager = getSupportFragmentManager();
        String backStateName = f.getClass().getName();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, f, Utils.FRAGMENT_feeling);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
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
            case R.id.img_globalmap: {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
                Log.e("TAG", "onClick: " + f.getTag());
                if (!f.getTag().equals(Utils.FRAGMENT_MAP)) {
                    addFragment();
                }
                CloseDrawer();
                break;
            }
            case R.id.img_help:{
                Intent intent = new Intent(MainActivity.this, IMSickActivity.class);
                startActivity(intent);
                CloseDrawer();
//                OpenBrowser(Utils.WEBURL);
                break;
            }
            case R.id.linear_totalcoin:{
                Intent intent = new Intent(MainActivity.this, HHCoinHistoryActivity.class);
                startActivityForResult(intent,2);
                break;
            }
            /*
            case R.id.txt_assessment_tool:{
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
                Log.e("TAG", "onClick: " + f.getTag());
                if (!f.getTag().equals(Utils.FRAGMENT_MAP)) {
                    addFragment();
                }
                CloseDrawer();
//                OpenBrowser();
                break;
            }*/
            /*case R.id.txt_dont_feel_well:{
//                OpenBrowser();
                addFragementFeeling_well();
                CloseDrawer();
                break;
            }
            case R.id.txt_datapolicy:{
                OpenBrowser(Utils.WEBURL);
            }
            case R.id.txt_follow_twitter:{
                OpenTwitter();
              *//*  FirebaseAuth.getInstance().signOut();
                CloseDrawer();
                HHSharedPrefrence.SetLogin(MainActivity.this, false);
                HHSharedPrefrence.ClearSession(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, HHTerms_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//mak
                startActivity(intent);*//*
                break;
            }*/
            case R.id.img_delete_account:{
                LogOutAlertBox();
//                DeleteAccount_data();
                break;
            }

            /*case R.id.txt_incentive_partner:{
                OpenBrowser(Utils.WEBURL);
                break;
            }

            case R.id.txt_privacypolicy_main:{
                OpenBrowser(Utils.PRIVACY_POLICY);
                break;
            }*/
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
                                txt_totalcoin.setText(document.get("totalPoints").toString());
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    void OpenBrowser(Uri url){
        Intent intent = new Intent(Intent.ACTION_VIEW, url);
        startActivity(intent);
        CloseDrawer();
    }

    void OpenTwitter(){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=HonestHerd")));
        }catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/HonestHerd")));
        }
        CloseDrawer();
    }

    void CloseDrawer() {
        drawer.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        Log.e("TAG", "onBackPressed: "+getSupportFragmentManager().findFragmentById(R.id.frame_layout).getTag() );
        if (getSupportFragmentManager().findFragmentById(R.id.frame_layout).getTag().equals(Utils.FRAGMENT_MAP) || getSupportFragmentManager().findFragmentById(R.id.frame_layout).getTag().equals(Utils.FRAGMENT_NextStep) ) {
            finish();
        }
        else {
            super.onBackPressed();
            if (getSupportFragmentManager().findFragmentById(R.id.frame_layout) instanceof HHFeeling_well_Fragment) {
//                linear_dateselect.setVisibility(View.INVISIBLE);
//                setCurrentDate();
            }/*else if (getSupportFragmentManager().findFragmentById(R.id.frame_layout) instanceof HHTripHistory){
//                linear_dateselect.setVisibility(View.VISIBLE);

            }*/
        }
    }

    void addUserDetails() {
        // Create a new user with a first and last name
        TimeZone tz = TimeZone.getDefault();
        System.out.println("TimeZone   "+tz.getDisplayName(false, TimeZone.SHORT)+" Timezon id :: " +tz.getID());

        Map<String, Object> user = new HashMap<>();
        user.put(Utils.FIREBASE_USERID, firebaseUser.getUid());
        user.put(Utils.HYPER_TRACK_DEVICEID, hyperTrack.getDeviceID());
        user.put(Utils.PHONENUMBER, firebaseUser.getPhoneNumber());
        user.put(Utils.TIMEZONE, tz.getID());

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

        new HHDeleteApi(MainActivity.this, Utils.HyperTrack_URL + hyperTrack.getDeviceID(), new OnUpdateListener() {
            @Override
            public void onUpdateComplete(JSONObject jsonObject, boolean isSuccess) {
                Log.e("TAG", "onResponse: 8" + isSuccess );
                if (!isSuccess) {
                    CloseDrawer();
                    FirebaseAuth.getInstance().signOut();
                    CloseDrawer();
                    HHSharedPrefrence.SetLogin(MainActivity.this, false);
                    HHSharedPrefrence.ClearSession(MainActivity.this);
                    Intent intent = new Intent(MainActivity.this, HHTerms_activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//mak
                    startActivity(intent);

                }else {
                    Log.e("TAG", "onResponse: 9" );
                }
            }
        }).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 2  && resultCode  == RESULT_OK) {
                String requiredValue = data.getStringExtra("screen");
                if (requiredValue.equals(Utils.FRAGMENT_TIRPHISTORY)){
                    addHistoryFragment(data.getStringExtra("date"));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
//            Toast.makeText(Activity.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }

   void LogOutAlertBox(){

       AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

       alertDialogBuilder.setTitle("Delete Account");
       alertDialogBuilder.setMessage("Do you want to delete your Account?");
       alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.O)
           @Override
           public void onClick(DialogInterface dialog, int which) {
//               Toast.makeText(MainActivity.this,"Yes",Toast.LENGTH_SHORT).show();
               DeleteAccount_data();
               dialog.dismiss();
           }
       });

       alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
//               Toast.makeText(MainActivity.this,"NO",Toast.LENGTH_SHORT).show();
               dialog.dismiss();
           }
       });

       AlertDialog alertDialog = alertDialogBuilder.create();

       alertDialog.show();

   }
}
//https://demonuts.com/android-google-map-in-fragment/