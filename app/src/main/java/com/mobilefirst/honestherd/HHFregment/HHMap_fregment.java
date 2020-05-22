package com.mobilefirst.honestherd.HHFregment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobilefirst.honestherd.HHActivity.HHCoinHistoryActivity;
import com.mobilefirst.honestherd.HHActivity.HHTripHistoryActivity;
import com.mobilefirst.honestherd.HHGlobal.HHSharedPrefrence;
import com.mobilefirst.honestherd.HHGlobal.Utils;
import com.mobilefirst.honestherd.HHModel.HHUserGeopoint;
import com.mobilefirst.honestherd.MainActivity;
import com.mobilefirst.honestherd.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hypertrack.sdk.TrackingError;
import com.hypertrack.sdk.TrackingStateObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class HHMap_fregment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener,TrackingStateObserver.OnTrackingStateChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    public HHMap_fregment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HHMap_fregment.
     */
    // TODO: Rename and change types and number of parameters
    AppCompatTextView txt_my_path_trace;
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Location currentLocation;
    private GoogleMap mMap;
    private Marker marker;
    private MapView mapView;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
//    HyperTrack hyperTrack;
    private LinearLayout linear_menu_history;
    private Map<String, Object> order;
    private AppCompatTextView txt_place,txt_walk,txt_vehicle;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private ArrayList<HHUserGeopoint> geopointArrayList = new ArrayList<>();
    LocationManager locationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hhmap_fregment, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100000);
        mLocationRequest.setFastestInterval(100000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

//        hyperTrack = HyperTrack.getInstance(getActivity(), "7pXsJ2QfvGFLjzyJG-dNUm99YT9iiqd0UsXNV6qHb9wPr0ebWQDlYmjEYMPn8KNjC8x-DA-19Yjg2urO8w9DPw");
//        hyperTrack.requestPermissionsIfNecessary();
//        hyperTrack.start();
//        hyperTrack.setDeviceName("newRedmi10");
        ((MainActivity)getContext()).hyperTrack.addTrackingListener(this);


        init(view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
               // boolean success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
                if (getContext() !=null){
                    MapStyleOptions mapStyleOptions=MapStyleOptions.loadRawResourceStyle(getContext(),R.raw.map_style);
                    mMap.setMapStyle(mapStyleOptions);
                    mMap.getUiSettings().setZoomGesturesEnabled(false);
                    mMap.getUiSettings().setZoomControlsEnabled(false);
                }


            }
        });
        linear_menu_history.setOnClickListener(this);
        fetchLocation();
        FetchAllUserLatLong();
        return view;
    }

    void  init(View view){
        linear_menu_history = view.findViewById(R.id.linear_menu_history);
        mapView = (MapView) view.findViewById(R.id.map);
        txt_place = view.findViewById(R.id.txt_place);
        txt_walk = view.findViewById(R.id.txt_walk);
        txt_vehicle = view.findViewById(R.id.txt_vehicle);
        txt_my_path_trace = view.findViewById(R.id.txt_my_path_trace);

        txt_vehicle.setTypeface(Typeface.createFromAsset(getContext().getResources().getAssets(), Utils.DIN_BOLD));
        txt_walk.setTypeface(Typeface.createFromAsset(getContext().getResources().getAssets(), Utils.DIN_BOLD));
        txt_place.setTypeface(Typeface.createFromAsset(getContext().getResources().getAssets(), Utils.DIN_BOLD));
        txt_my_path_trace.setTypeface(Typeface.createFromAsset(getContext().getResources().getAssets(), Utils.DIN_BOLD));



        txt_walk.setOnClickListener(this);
        txt_place.setOnClickListener(this);
        txt_vehicle.setOnClickListener(this);
    }

    private void FetchAllUserLatLong() {
//        https://stackoverflow.com/questions/50673639/firestore-possible-geoquery-workaround
        // ~1 mile of lat and lon in degrees
        GeoPoint geoPoint = Utils.currentlatLong(getActivity(),locationManager);
        double lat = 0.0144927536231884;
        double lon = 0.0181818181818182;

        if (geoPoint !=null){
            double lowerLat = geoPoint.getLatitude() - (lat * 15);
            double lowerLon = geoPoint.getLongitude() - (lon * 15);
            GeoPoint lesserGeopoint = new GeoPoint(lowerLat, lowerLon);

            firebaseFirestore.collection(Utils.USER_HEALTHLOG).whereGreaterThan(Utils.lastLocation,lesserGeopoint).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() != 0) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAGIDsa", document.getId());
                                HHUserGeopoint hhUserGeopoint = new HHUserGeopoint();
                                hhUserGeopoint.setGeoPoint(document.getGeoPoint(Utils.lastLocation));

                                MarkerOptions markerOptions = new MarkerOptions();
                              //  markerOptions.title(document.getId());
                                if (document.get(Utils.HEALTHSTATUS).equals("FEELINGWELL")){
                                    markerOptions.icon(getBitmapDescriptor(getActivity().getResources().getDrawable(R.drawable.green_circle)));
                                }else {
                                    markerOptions.icon(getBitmapDescriptor(getActivity().getResources().getDrawable(R.drawable.orange_circle)));
                                }

                                markerOptions.position(new LatLng(document.getGeoPoint(Utils.lastLocation).getLatitude(),document.getGeoPoint(Utils.lastLocation).getLongitude()));
                                hhUserGeopoint.setMarkerOptions(markerOptions);
//                                if (!firebaseUser.getUid().equals(document.get(Utils.FIREBASE_USERID)))
                                {
                                    geopointArrayList.add(hhUserGeopoint);
                                }

                            }
                            for (int i = 0;i<geopointArrayList.size();i++){
                                mMap.addMarker(geopointArrayList.get(i).getMarkerOptions());
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

    }

    private BitmapDescriptor getBitmapDescriptor(Drawable drawable) {
        Drawable vectorDrawable = drawable;

        vectorDrawable.setBounds(0, 0, 24, 24);
        Bitmap bm = Bitmap.createBitmap(24, 24, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    private void fetchLocation() {

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    currentLocation = location;
//                    Toast.makeText(getContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude()+ "Test", Toast.LENGTH_SHORT).show();
//                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
//                    supportMapFragment.getMapAsync(MainActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(getBitmapDescriptor(getActivity().getResources().getDrawable(R.drawable.current_icon)));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        marker = googleMap.addMarker(markerOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                        Log.e("CheckPermission", "onRequestPermissionsResult:  Accept");
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                       // fetchLocation();
                    }
                } else {
                    Log.e("CheckPermission", "onRequestPermissionsResult:  Failed");
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            if (marker !=null){
                marker.remove();
            }

           // if (currentLocation.getLatitude() !=location.getLatitude() )
            {
                Log.e("Checjc", "currentLocation: "+ currentLocation.getLatitude()+"   "+currentLocation.getLongitude());
                Log.e("Checjc", "onLocationChanged: "+ location.getLatitude()+"   "+ location.getLongitude());
                currentLocation = location;
                getAddressFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(getBitmapDescriptor(getActivity().getResources().getDrawable(R.drawable.current_icon)));

                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                marker = mMap.addMarker(markerOptions);

                order = new HashMap<>();
                order.put("address", getAddressFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude()));
                order.put(Utils.HEALTHSTATUS,HHSharedPrefrence.getsetHealthStatus(getActivity()));
                onTrackingStart();
            }
        }
    }


    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    String getAddressFromLocation(double latitude, double longitude) {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        String new_address = "";
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            Log.e("TAG", "getAddressFromLocation: "+latitude+" -- "+longitude );
            Log.e("TAG", "getAddressFromLocation: "+address+addresses.get(0).getSubAdminArea()+""+city );
            new_address  = address;
            return new_address;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new_address;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_menu_history:{
               // ((MainActivity)getActivity()).addHistoryFragment(data.getStringExtra("date"));

                Intent intent = new Intent(getContext(), HHTripHistoryActivity.class);
//                intent.putExtra("screen",Utils.FRAGMENT_TIRPHISTORY);
                intent.putExtra("date",Utils.getDateFromate("yyyy-MM-dd"));
                startActivity(intent);
                break;
            }
            case R.id.txt_vehicle:{
//                setDefualtPropertise();
//                txt_vehicle.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(R.drawable.green_line),null,null,null);
//                txt_vehicle.setTextColor(getContext().getResources().getColor(R.color.white));
                break;
            }
            case R.id.txt_walk:{
//                setDefualtPropertise();
//                txt_walk.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(R.drawable.pink_line),null,null,null);
//                txt_walk.setTextColor(getContext().getResources().getColor(R.color.white));
                break;
            }
            case R.id.txt_place:{
//                setDefualtPropertise();
//                txt_place.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(R.drawable.yellow_line),null,null,null);
//                txt_place.setTextColor(getContext().getResources().getColor(R.color.white));
                break;
            }
        }
    }

    @Override
    public void onError(TrackingError trackingError) {

    }

    @Override
    public void onTrackingStart() {
        if (((MainActivity)getActivity()).hyperTrack.isRunning()){
//            Map<String,Object> d = new HashMap<>();
//            d.put("address", "Test");
            try {
                ((MainActivity)getActivity()).hyperTrack.addTripMarker(order);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onTrackingStop() {

    }
    // TODO: Rename method, update argument and hook method into UI event

    void setDefualtPropertise(){
        txt_place.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(R.drawable.yellow_dots),null,null,null);
        txt_walk.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(R.drawable.pink_dots),null,null,null);
        txt_vehicle.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(R.drawable.green_dots),null,null,null);

        txt_walk.setTextColor(getContext().getResources().getColor(R.color.unseleted));
        txt_place.setTextColor(getContext().getResources().getColor(R.color.unseleted));
        txt_vehicle.setTextColor(getContext().getResources().getColor(R.color.unseleted));
    }

}

