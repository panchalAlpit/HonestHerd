package com.example.honestherd.HHGlobal;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Utils {

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

    //Fragment name
    public static final String FRAGMENT_MAP = "map_fragment";
    public static final String FRAGMENT_feeling = "FEELING_fragment";

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

}
