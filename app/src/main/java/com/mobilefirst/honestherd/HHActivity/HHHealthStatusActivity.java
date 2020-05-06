package com.mobilefirst.honestherd.HHActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mobilefirst.honestherd.HHGlobal.HHSharedPrefrence;
import com.mobilefirst.honestherd.HHGlobal.Utils;
import com.mobilefirst.honestherd.MainActivity;
import com.mobilefirst.honestherd.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class HHHealthStatusActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout linear_feel_well_healthstatus,linear_feel_sick_healthstatus;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    GeoPoint latLng;
    LocationManager mLocationManager;
    AppCompatTextView txt_how_you_feel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_h_health_status);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        init();
        fetchrecord();
    }

    private void init() {
        linear_feel_well_healthstatus = findViewById(R.id.linear_feel_well_healthstatus);
        linear_feel_sick_healthstatus =findViewById(R.id.linear_feel_sick_healthstatus);
        txt_how_you_feel = findViewById(R.id.txt_how_you_feel);

        txt_how_you_feel.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));

        linear_feel_sick_healthstatus.setOnClickListener(this);
        linear_feel_well_healthstatus.setOnClickListener(this);
    }

    private void fetchrecord() {
        firebaseFirestore.collection(Utils.USER_HEALTHLOG).whereEqualTo(Utils.FIREBASE_USERID, firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() != 0) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("TAGID", document.getId() + " => " + document.getId());
                            Log.e("TAG", "onSuccess: ");
                            HHSharedPrefrence.saveHealthLogID(HHHealthStatusActivity.this, document.getId());
                        }
                    } else {
                        Log.e("TAG", "onFailed: ");
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ");
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_feel_well_healthstatus: {
                if (HHSharedPrefrence.getsaveHealthLogID(HHHealthStatusActivity.this).equals("")) {
                    addHealthLog("FEELINGWELL");
                } else {
                    updateHealthLog("FEELINGWELL");
                }

                Intent intent = new Intent(HHHealthStatusActivity.this, MainActivity.class);
                intent.putExtra("screen",Utils.FRAGMENT_MAP);
                startActivity(intent);
                break;
            }
            case R.id.linear_feel_sick_healthstatus:{

                /*if (HHSharedPrefrence.getsaveHealthLogID(HHHealthStatusActivity.this).equals("")) {
                    addHealthLog("FEELINGSICK");
                } else {
                    updateHealthLog("FEELINGSICK");
                }*/
                Intent intent = new Intent(HHHealthStatusActivity.this,IMSickActivity.class);
                startActivity(intent);

//                Intent intent = new Intent(HHHealthStatusActivity.this, MainActivity.class);
//                intent.putExtra("screen",Utils.FRAGMENT_NextStep);
//                startActivity(intent);
                break;
            }
        }
    }


    void addHealthLog(String status) {
        // Create a new user with a first and last name
        TimeZone tz = TimeZone.getDefault();
        System.out.println("TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT) + " Timezon id :: " + tz.getID());

        Map<String, Object> user = new HashMap<>();
        user.put(Utils.FIREBASE_USERID, firebaseUser.getUid());
        user.put(Utils.FORDATE, Utils.getDateFromate("yyyy-MM-dd"));
        user.put(Utils.HEALTHSTATUS, status);
        user.put(Utils.TIMESTAMP, FieldValue.serverTimestamp());
        user.put(Utils.USERS_TIMEZONE, tz.getID());
        user.put(Utils.lastLocation, getLastKnownLocation());
        HHSharedPrefrence.setHealthStatus(HHHealthStatusActivity.this,status);

        // Add a new document with a generated ID
        firebaseFirestore.collection(Utils.USER_HEALTHLOG)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
    }

    void updateHealthLog(String status) {

        Map<String, Object> user = new HashMap<>();
        user.put(Utils.FORDATE, Utils.getDateFromate("yyyy-MM-dd"));
        user.put(Utils.HEALTHSTATUS, status);
        user.put(Utils.TIMESTAMP, FieldValue.serverTimestamp());
        user.put(Utils.lastLocation, getLastKnownLocation());
        HHSharedPrefrence.setHealthStatus(HHHealthStatusActivity.this,status);

        firebaseFirestore.collection(Utils.USER_HEALTHLOG).document(HHSharedPrefrence.getsaveHealthLogID(HHHealthStatusActivity.this)).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    private GeoPoint getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(HHHealthStatusActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HHHealthStatusActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
                latLng = new GeoPoint(l.getLatitude(), l.getLongitude());
            }
        }
        return latLng;
    }
}
