package com.example.honestherd.HHGlobal;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.firebase.firestore.GeoPoint;

import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static android.content.Context.LOCATION_SERVICE;

public class Utils {

    public static final Uri WEBURL = Uri.parse("https://honestherd.com/");
    public static final Uri PRIVACY_POLICY = Uri.parse("https://honestherd.com/privacy-policy.php");
    public static final String publishKey = "7pXsJ2QfvGFLjzyJG-dNUm99YT9iiqd0UsXNV6qHb9wPr0ebWQDlYmjEYMPn8KNjC8x-DA-19Yjg2urO8w9DPw";
    public static final String Account_id = "X43jMR2erQh3C460rGVWrCLi2Tw";
    public static final String SecretKey = "eWiCn0_PQ5vPI8UTIk0vbOTOGONAwEIQaJ16qq6xz4diT2OerZk3jg";
    public static final String HyperTrack_URL = "https://v3.api.hypertrack.com/devices/";
    public static final String Api_history = "/history/";

    public static final String MARKERS = "markers";
    public static final String TYPE = "type";
    public static final String DATA = "data";
    public static final String START = "start";
    public static final String METADATA = "metadata";
    public static final String RECORDED_AT = "recorded_at";
    public static final String ADDRESS = "address";
    public static final String TRIP_MARKER = "trip_marker";
    public static final String DEVICE_STATUS = "device_status";

    public static final String FIREBASE_USERID = "firebaseUserID";
    public static final String USERPOINTS = "userPoints";
    public static final String USERPOINTSLOGS = "userPointsLogs";
    public static final String USER_HEALTHLOG = "userHealthLog";
    public static final String AWARDEDFORDATE = "awardedForDate";
    public static final String POINTSCHANGE = "pointsChange";
    public static final String FORDATE = "forDate";
    public static final String HEALTHSTATUS = "healthStatus";
    public static final String TIMESTAMP = "timestamp";
    public static final String USERS_TIMEZONE = "usersTimezone";
    public static final String lastLocation = "lastLocation";

    public static final String HYPER_TRACK_DEVICEID = "hyperTrackDeviceID";
    public static final String PHONENUMBER = "phoneNumber";
    public static final String TIMEZONE = "timezone";

    //Fragment name
    public static final String FRAGMENT_MAP = "map_fragment";
    public static final String FRAGMENT_feeling = "FEELING_fragment";
    public static final String FRAGMENT_NextStep = "NextStep_fragment";

    //FontName
    public static final String DIN_BOLD = "din_bold.ttf";
    public static final String DIN_MEDIUM = "din_medium.ttf";

    public static String uTCToLocal( String datesToConvert,String formate) {

        String dateToReturn = datesToConvert;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gmt = null;
        SimpleDateFormat sdfOutPutToSend = new SimpleDateFormat(formate);
        sdfOutPutToSend.setTimeZone(TimeZone.getDefault());

        try {

            gmt = sdf.parse(datesToConvert);
            dateToReturn = sdfOutPutToSend.format(gmt);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e("TAG", "uTCToLocal: "+dateToReturn );
        return dateToReturn; }

        public static  String getDateFromate(String formate){
        return new SimpleDateFormat(formate).format(new Date());
        }

        public static String getFormateDate(String formate, String sdate){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            String newDateStr = " ";
            try {
                date = format.parse(sdate);
                SimpleDateFormat postFormater = new SimpleDateFormat(formate);
                 newDateStr = postFormater.format(date).toUpperCase();
                Log.e("TAG", "getFormateDate: "+newDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return newDateStr;
        }

        public static  int NumberOFDays(String startdate, String currentDate){

            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
            int number = 0;
            try {
                Date date1 = myFormat.parse(startdate);
                Date date2 = myFormat.parse(currentDate);
                long diff = date2.getTime() - date1.getTime();
                Log.e ("cccccc","Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                number = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return number;
        }


    public static GeoPoint currentlatLong(Context context, LocationManager mLocationManager) {

        mLocationManager = (LocationManager) context.getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        GeoPoint latLng = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
