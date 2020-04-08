package com.example.honestherd.HHFregment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.honestherd.HHGlobal.HHSharedPrefrence;
import com.example.honestherd.HHGlobal.Utils;
import com.example.honestherd.MainActivity;
import com.example.honestherd.R;
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
import com.google.type.LatLng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static android.content.Context.LOCATION_SERVICE;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class HHFeeling_well_Fragment extends Fragment implements View.OnClickListener {

    private AppCompatTextView txt_how_youfeel, txt_feel_well, txt_feel_sick;
    LinearLayout linear_feel_well, linear_feel_sick;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    GeoPoint latLng;
    LocationManager mLocationManager;

    public HHFeeling_well_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_h_h_feeling_well_, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        init(view);
        fetchrecord();
        return view;
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
                            HHSharedPrefrence.saveHealthLogID(getContext(), document.getId());
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

    private void init(View view) {
        txt_how_youfeel = view.findViewById(R.id.txt_how_youfeel);
        txt_feel_well = view.findViewById(R.id.txt_feel_well);
        txt_feel_sick = view.findViewById(R.id.txt_feel_sick);
        linear_feel_well = view.findViewById(R.id.linear_feel_well);
        linear_feel_sick = view.findViewById(R.id.linear_feel_sick);

        txt_how_youfeel.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Utils.DIN_BOLD));
        txt_feel_well.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Utils.DIN_BOLD));
        txt_feel_sick.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Utils.DIN_BOLD));

        linear_feel_sick.setOnClickListener(this);
        linear_feel_well.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_feel_well: {
                if (HHSharedPrefrence.getsaveHealthLogID(getContext()).equals("")) {
                    addHealthLog("FEELINGWELL");
                } else {
                    updateHealthLog("FEELINGWELL");
                }
                addFragment();
                break;
            }
            case R.id.linear_feel_sick: {

                if (HHSharedPrefrence.getsaveHealthLogID(getContext()).equals("")) {
                    addHealthLog("FEELINGSICK");
                } else {
                    updateHealthLog("FEELINGSICK");
                }
                AddNextStepFragment();
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
        HHSharedPrefrence.setHealthStatus(getContext(),status);

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
        HHSharedPrefrence.setHealthStatus(getContext(),status);

        firebaseFirestore.collection(Utils.USER_HEALTHLOG).document(HHSharedPrefrence.getsaveHealthLogID(getContext())).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    public void AddNextStepFragment() {

        Fragment f = new HHNextStepFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        String backStateName = f.getClass().getName();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, f, Utils.FRAGMENT_NextStep);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addFragment() {
        //first time call this method
        Fragment f = new HHMap_fregment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        String backStateName = f.getClass().getName();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, f, Utils.FRAGMENT_MAP);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private GeoPoint getLastKnownLocation() {
        mLocationManager = (LocationManager) getContext().getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
