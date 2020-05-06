package com.mobilefirst.honestherd.HHActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
import com.mobilefirst.honestherd.HHAdpater.HHDiseasesAdpater;
import com.mobilefirst.honestherd.HHGlobal.HHSharedPrefrence;
import com.mobilefirst.honestherd.HHGlobal.OnItemClickListener;
import com.mobilefirst.honestherd.HHGlobal.OnItemClickListener_other;
import com.mobilefirst.honestherd.HHGlobal.Utils;
import com.mobilefirst.honestherd.HHModel.HHDateModel;
import com.mobilefirst.honestherd.HHModel.HHDiseaseModel;
import com.mobilefirst.honestherd.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class IMSickActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatTextView txt_covid19,txt_influenza,txt_none_above,txt_my_doctor;
    RecyclerView recycle_diseases;
    HHDiseasesAdpater diseasesAdpater;
    ArrayList<HHDiseaseModel> arrayListDiseaseModel = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    GeoPoint latLng;
    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_m_sick);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fetchDiseasesList();
        init();
    }

    private void fetchDiseasesList() {
        try {
            JSONArray jsonArray = new JSONArray(Utils.getJsonFromAssets(IMSickActivity.this,"diseasesList.json"));
            for (int i = 0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                HHDiseaseModel diseaseModel = new HHDiseaseModel();
                diseaseModel.setId(jsonObject.getString("id"));
                diseaseModel.setName(jsonObject.getString("name"));
                diseaseModel.setType(jsonObject.getString("type"));
                arrayListDiseaseModel.add(diseaseModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        txt_covid19 = findViewById(R.id.txt_covid19);
        txt_influenza = findViewById(R.id.txt_influenza);
        txt_none_above = findViewById(R.id.txt_none_above);
        txt_my_doctor = findViewById(R.id.txt_my_doctor);
        recycle_diseases = findViewById(R.id.recycle_diseases);

        txt_covid19.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_influenza.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_none_above.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));
        txt_my_doctor.setTypeface(Typeface.createFromAsset(getAssets(), Utils.DIN_BOLD));

        txt_covid19.setOnClickListener(this);
        txt_influenza.setOnClickListener(this);
        txt_none_above.setOnClickListener(this);

        diseasesAdpater = new HHDiseasesAdpater(IMSickActivity.this,arrayListDiseaseModel, new OnItemClickListener_other() {
            @Override
            public void OnItemClickListener(int position, ArrayList<HHDiseaseModel> models) {
                if (models.get(position).getType().equals("sick")){
                    if (HHSharedPrefrence.getsaveHealthLogID(IMSickActivity.this).equals("")) {
                        addHealthLog("FEELINGSICK");
                    } else {
                        updateHealthLog("FEELINGSICK");
                    }
                }else {
                    if (HHSharedPrefrence.getsaveHealthLogID(IMSickActivity.this).equals("")) {
                        addHealthLog("FEELINGWELL");
                    } else {
                        updateHealthLog("FEELINGWELL");
                    }
                }

                Intent intent = new Intent(IMSickActivity.this, HHStayHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        recycle_diseases.setLayoutManager(new LinearLayoutManager(IMSickActivity.this));
        recycle_diseases.setAdapter(diseasesAdpater);
        recycle_diseases.setNestedScrollingEnabled(false);
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
        HHSharedPrefrence.setHealthStatus(IMSickActivity.this,status);

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
        HHSharedPrefrence.setHealthStatus(IMSickActivity.this,status);

        firebaseFirestore.collection(Utils.USER_HEALTHLOG).document(HHSharedPrefrence.getsaveHealthLogID(IMSickActivity.this)).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
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
            if (ActivityCompat.checkSelfPermission(IMSickActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(IMSickActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
