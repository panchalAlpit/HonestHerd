package com.example.honestherd.HHGlobal;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {

    public static final String publishKey = "7pXsJ2QfvGFLjzyJG-dNUm99YT9iiqd0UsXNV6qHb9wPr0ebWQDlYmjEYMPn8KNjC8x-DA-19Yjg2urO8w9DPw";
    public static final String Account_id = "X43jMR2erQh3C460rGVWrCLi2Tw";
    public static final String SecretKey = "eWiCn0_PQ5vPI8UTIk0vbOTOGONAwEIQaJ16qq6xz4diT2OerZk3jg";
    public static final String HyperTrack_URL = "https://v3.api.hypertrack.com/devices/";
    public static final String Api_history = "/history/";

    public static final String MARKERS = "markers";
    public static final String TYPE = "type";
    public static final String DATA = "data";
    public static final String METADATA = "metadata";
    public static final String RECORDED_AT = "recorded_at";
    public static final String ADDRESS = "address";
    public static final String TRIP_MARKER = "trip_marker";

    //Fragment name
    public static final String FRAGMENT_MAP = "map_fragment";

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

}
