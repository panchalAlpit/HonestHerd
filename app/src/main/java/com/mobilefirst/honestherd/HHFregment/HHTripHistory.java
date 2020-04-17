package com.mobilefirst.honestherd.HHFregment;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mobilefirst.honestherd.HHAdpater.HHDateSelectionAdpater;
import com.mobilefirst.honestherd.HHAdpater.HHHistory_adpater;
import com.mobilefirst.honestherd.HHGlobal.HHSharedPrefrence;
import com.mobilefirst.honestherd.HHGlobal.OnItemClickListener;
import com.mobilefirst.honestherd.HHGlobal.Utils;
import com.mobilefirst.honestherd.HHModel.HHDateModel;
import com.mobilefirst.honestherd.HHModel.HHHistory_Model;
import com.mobilefirst.honestherd.HHWebService.HHApiCall;
import com.mobilefirst.honestherd.HHWebService.OnUpdateListener;
import com.mobilefirst.honestherd.MainActivity;
import com.mobilefirst.honestherd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class HHTripHistory extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private String deviceid = "";
    private ArrayList<HHHistory_Model> arrayList_historyModel = new ArrayList<>();

    public HHTripHistory( ) {
        // Required empty public constructor
//        this.deviceid = deviceID;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment HHTripHistory.
     */
    // TODO: Rename and change types and number of parameters

    private LinearLayout linear_close_history;
    private FrameLayout frame_triphistory;
    private RecyclerView recycle_custom_date;
    private RecyclerView recycle_history;
    private HHHistory_adpater history_adpater;
    private AppCompatTextView txt_empty;
    private AppCompatImageView btn_previous,btn_next;
    int date_index = 0,total_days = 0;
    private int mYear, mMonth, mDay;
    Calendar calendar = Calendar.getInstance();
    HHDateSelectionAdpater adpaterSelection;
    ArrayList<HHDateModel> listOfDates = new ArrayList<>();
    Date startDate;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hhtrip_history, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        init(view);

//        HyperTrack.getInstance(getContext(), "7pXsJ2QfvGFLjzyJG-dNUm99YT9iiqd0UsXNV6qHb9wPr0ebWQDlYmjEYMPn8KNjC8x-DA-19Yjg2urO8w9DPw");
        HistoryApiCall( Utils.getDateFromate("yyyy-MM-dd"));
        CustomCalendar();
        total_days = Utils.NumberOFDays(HHSharedPrefrence.getJoindate(getActivity()),Utils.getDateFromate("yyyy-MM-dd"));
        Log.e("Number", "onCreateView: "+Utils.NumberOFDays("2020-03-25",Utils.getDateFromate("yyyy-MM-dd")) );
        Log.e("TAG", "onClick:OnCreate btn_previous"+date_index+" ---- "+total_days );
        if (total_days == date_index){
            btn_previous.setVisibility(View.GONE);
        }
        return view;
    }

    void init(View view){
        recycle_history = view.findViewById(R.id.recycle_history);
        frame_triphistory = view.findViewById(R.id.frame_triphistory);
        recycle_custom_date = view.findViewById(R.id.recycle_custom_date);
        linear_close_history = view.findViewById(R.id.linear_close_history);

        txt_empty = view.findViewById(R.id.txt_empty);
        btn_previous = view.findViewById(R.id.btn_previous);
        btn_next = view.findViewById(R.id.btn_next);

        frame_triphistory.setVisibility(View.VISIBLE);
        recycle_custom_date.setVisibility(View.GONE);

        btn_previous.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        linear_close_history.setOnClickListener(this);

        ((MainActivity)getContext()).txt_date_map_fregment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DateTImePicker();
                frame_triphistory.setVisibility(View.GONE);
                recycle_custom_date.setVisibility(View.VISIBLE);

            }
        });
    }

    private void CustomCalendar() {
        adpaterSelection = new HHDateSelectionAdpater(getContext(), getListOfDates(), new OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void OnItemClickListener(int position, ArrayList<HHDateModel> models) {
                if (models.get(position).isActive()){
                    frame_triphistory.setVisibility(View.VISIBLE);
                    recycle_custom_date.setVisibility(View.GONE);
                    HistoryApiCall(models.get(position).getSdate());
                    ((MainActivity)getContext()).txt_date_map_fregment.setText(Utils.getFormateDate("dd",models.get(position).getSdate()));
                    ((MainActivity)getContext()).txt_month_map_fregment.setText(Utils.getFormateDate("MMM yyyy",models.get(position).getSdate()));
                }
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recycle_custom_date.setLayoutManager(gridLayoutManager);
        recycle_custom_date.setAdapter(adpaterSelection);
        UpdateCoin();
    }

    public void UpdateCoin(){
        for (int i =0;i<listOfDates.size();i++){
            if (listOfDates.get(i).isActive()){
                FetchTotalCoins(listOfDates.get(i).getSdate(),i);
            }
        }
        adpaterSelection.notifyDataSetChanged();
    }

    public ArrayList<HHDateModel> getListOfDates() {

        listOfDates.clear();

        Calendar LoginCal = GregorianCalendar.getInstance();

        LoginCal.add(Calendar.DATE, -Utils.NumberOFDays(HHSharedPrefrence.getJoindate(getActivity()),Utils.getDateFromate("yyyy-MM-dd")));
        Date Logindate = LoginCal.getTime();
        Log.e("TAG","LOGIN DATE == "+Logindate);

        //currentDate
        Calendar TodayCal = GregorianCalendar.getInstance();
        TodayCal.setTime(new Date());
        Date TodayDate = TodayCal.getTime();
        Log.e("TAG","CURRENT DATE == "+TodayDate);

        //find the diff in Days

        long diff = TodayDate.getTime() - Logindate.getTime();
        long hours = ((diff / 1000) / 60) / 60;
        long days = hours / 24;

        if( days > 14){

            int temp = (int) days - 14;
            Calendar updateCal = GregorianCalendar.getInstance();
            updateCal.setTime(Logindate);
            updateCal.add(Calendar.DAY_OF_YEAR, temp);
            Date updateDate = updateCal.getTime();
            startDate = updateDate;

        }else{
            startDate = Logindate;
        }

        for (int i = 0 ; i < 14; i++) {

            Boolean isActive;
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(startDate);
            cal.add(Calendar.DAY_OF_YEAR, i);
            Date date = cal.getTime();

            String day = String.valueOf(date.getDate());
            String month = new DateFormatSymbols().getMonths()[date.getMonth()];
            String year = String.valueOf(date.getYear() + 1900);
            isActive =  ( i<=days ) ? true : false;
            HHDateModel dateModel = new HHDateModel();
            dateModel.setDay(day);
            dateModel.setMonth(month);
            dateModel.setYear(year);
            dateModel.setActive(isActive);
            Log.e("CheckMonth", "getListOfDates: "+(date.getMonth()+1)+"  "+month );
            dateModel.setSdate(Utils.getFormateDate("yyyy-MM-dd",year+"-"+(date.getMonth()+1)+"-"+day));
            if (isActive){
//                dateModel.setCoins(FetchTotalCoins(Utils.getFormateDate("yyyy-MM-dd",year+"-"+date.getMonth()+"-"+day)));
            }
            listOfDates.add(dateModel);
        }
        return listOfDates;
    }

    public void FetchTotalCoins(final String fetchs, final int index){
        Log.e("TAG", "FetchTotalCoins: "+fetchs +"  "+firebaseUser.getUid());
        final String[] spoint = new String[0];
        firebaseFirestore.collection(Utils.USERPOINTSLOGS)
                .whereEqualTo(Utils.FIREBASE_USERID, firebaseUser.getUid())
                .whereEqualTo(Utils.AWARDEDFORDATE,fetchs)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("TAGID", document.getId() + " => " + document.getId());
                                 String s = document.get(Utils.POINTSCHANGE).toString();
                                Log.e("CheckCoins", "onComplete: "+s +" "+fetchs+" --- "+document.getId());
                                listOfDates.get(index).setCoins(s);
                            }

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                        adpaterSelection.notifyDataSetChanged();
                    }
                });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void HistoryApiCall(String date) {
        arrayList_historyModel = new ArrayList<>();

        String url = Utils.HyperTrack_URL+((MainActivity)getActivity()).hyperTrack.getDeviceID()+Utils.Api_history+date;
//        String url = "https://v3.api.hypertrack.com/devices/"+deviceid+"/history/2020-03-28";
        new HHApiCall(getActivity(), url, new OnUpdateListener() {
            @Override
            public void onUpdateComplete(JSONObject jsonObject, boolean isSuccess) {
                try {
                    JSONArray jsonArray_marker = jsonObject.getJSONArray(Utils.MARKERS);
                    for (int i = 0;i<jsonArray_marker.length();i++){

                            JSONObject object = jsonArray_marker.getJSONObject(i);
                        if (object.getString(Utils.TYPE).equals(Utils.TRIP_MARKER)){
                            HHHistory_Model history_model = new HHHistory_Model();
                            history_model.setType(object.getString(Utils.TYPE));

                            history_model.setSdate(Utils.uTCToLocal(object.getJSONObject(Utils.DATA).getString(Utils.RECORDED_AT),"dd-MMMM-yyyy"));
                            history_model.setS_time(Utils.uTCToLocal(object.getJSONObject(Utils.DATA).getString(Utils.RECORDED_AT),"hh:mm a"));

                            if (object.getJSONObject(Utils.DATA).getJSONObject(Utils.METADATA).has(Utils.ADDRESS)){
                                history_model.setAddress(object.getJSONObject(Utils.DATA).getJSONObject(Utils.METADATA).getString(Utils.ADDRESS));
                                arrayList_historyModel.add(history_model);
                            }
                        }else if (object.getString(Utils.TYPE).equals(Utils.DEVICE_STATUS)){
                            HHHistory_Model history_model = new HHHistory_Model();
                            history_model.setType(object.getString(Utils.TYPE));
                            history_model.setSdate(Utils.uTCToLocal(object.getJSONObject(Utils.DATA).getJSONObject(Utils.START).getString(Utils.RECORDED_AT),"dd-MMMM-yyyy"));
                            history_model.setS_time(Utils.uTCToLocal(object.getJSONObject(Utils.DATA).getJSONObject(Utils.START).getString(Utils.RECORDED_AT),"hh:mm a"));
                            if (object.getJSONObject(Utils.DATA).has(Utils.ADDRESS)){
                                history_model.setAddress(object.getJSONObject(Utils.DATA).getString(Utils.ADDRESS));
                                arrayList_historyModel.add(history_model);
                            }
                        }
                    }

                    history_adpater = new HHHistory_adpater(getActivity(),arrayList_historyModel);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recycle_history.setLayoutManager(layoutManager);
                    recycle_history.setAdapter(history_adpater);
                    history_adpater.notifyDataSetChanged();

                    if (history_adpater.getItemCount() ==0){
                        txt_empty.setVisibility(View.VISIBLE);
                        recycle_history.setVisibility(View.GONE);
                    }else {
                        txt_empty.setVisibility(View.GONE);
                        recycle_history.setVisibility(View.VISIBLE);
                    }

                    Log.e("test", "onUpdateComplete: "+jsonObject.toString() );
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_previous:{
                if (date_index<total_days){
                    date_index++;
                    btn_next.setVisibility(View.VISIBLE);
                    Log.e("TAG", "onClick:IF btn_previous"+date_index+" ---- "+total_days );
                    ChangedDate(date_index);
                    if (date_index == total_days){
                        btn_previous.setVisibility(View.GONE);
                    }

                }else {
                    Log.e("TAG", "onClick:else btn_previous"+date_index+" ---- "+total_days );
                }

                break;
            }
            case R.id.btn_next:{

                if ( date_index>0){
                    date_index--;
                    btn_previous.setVisibility(View.VISIBLE);
                    if (date_index == 0)
                    {
                        btn_next.setVisibility(View.GONE);
                    }
                    ChangedDate(date_index);
                }else {
                    Log.e("TAG", "onClick:else btn_next" );
                }
                break;
            }
            case R.id.linear_close_history:{
                ((MainActivity)getContext()).onBackPressed();
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void ChangedDate(int index){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -index);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String yesterdayAsString = dateFormat.format(calendar.getTime());

        SimpleDateFormat fordate = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat formonth = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
        Log.e("TAG", "ChangedDate: "+index+" "+yesterdayAsString );
        ((MainActivity)getContext()).txt_date_map_fregment.setText(fordate.format(calendar.getTime()));
        ((MainActivity)getContext()).txt_month_map_fregment.setText(formonth.format(calendar.getTime()));
        HistoryApiCall(yesterdayAsString);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void DateTImePicker() {
        try {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.DATE, -Utils.NumberOFDays(HHSharedPrefrence.getJoindate(getActivity()),Utils.getDateFromate("yyyy-MM-dd")));

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String sdate = String.valueOf((monthOfYear+1));
                            if ((monthOfYear+1)<10){
                                sdate = "0"+ (monthOfYear+1);
                            }
                            String dateIs = (year + "-" + sdate + "-" + dayOfMonth);
                            Log.e("TAG", "onDateSet: " + dateIs);
                            ((MainActivity)getContext()).txt_date_map_fregment.setText(Utils.getFormateDate("dd",dateIs));
                            ((MainActivity)getContext()).txt_month_map_fregment.setText(Utils.getFormateDate("MMM yyyy",dateIs));
                            HistoryApiCall(dateIs);
                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());

            datePickerDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
