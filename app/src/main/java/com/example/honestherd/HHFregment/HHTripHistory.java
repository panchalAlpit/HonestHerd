package com.example.honestherd.HHFregment;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.honestherd.HHAdpater.HHHistory_adpater;
import com.example.honestherd.HHGlobal.Utils;
import com.example.honestherd.HHModel.HHHistory_Model;
import com.example.honestherd.HHWebService.HHApiCall;
import com.example.honestherd.HHWebService.OnUpdateListener;
import com.example.honestherd.MainActivity;
import com.example.honestherd.R;
import com.hypertrack.sdk.HyperTrack;

import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    private RecyclerView recycle_history;
    private HHHistory_adpater history_adpater;
    private AppCompatTextView txt_empty;
    private AppCompatButton btn_previous,btn_next;
    int date_index = 0;
    private int mYear, mMonth, mDay;
    Calendar calendar = Calendar.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hhtrip_history, container, false);
        recycle_history = view.findViewById(R.id.recycle_history);
        txt_empty = view.findViewById(R.id.txt_empty);
        btn_previous = view.findViewById(R.id.btn_previous);
        btn_next = view.findViewById(R.id.btn_next);
        btn_previous.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        HyperTrack.getInstance(getContext(), "7pXsJ2QfvGFLjzyJG-dNUm99YT9iiqd0UsXNV6qHb9wPr0ebWQDlYmjEYMPn8KNjC8x-DA-19Yjg2urO8w9DPw");
        HistoryApiCall(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        ((MainActivity)getContext()).txt_date_map_fregment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTImePicker();
            }
        });
        return view;
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
                if (date_index<14){
                    date_index++;
                    btn_next.setVisibility(View.VISIBLE);
                    ChangedDate(date_index);
                }

                break;
            }
            case R.id.btn_next:{

                if ( date_index>0){
                    date_index--;
                    if (date_index == 0)
                    {
                        btn_next.setVisibility(View.GONE);
                    }
                    ChangedDate(date_index);
                }
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
            calendar.add(Calendar.DATE, -14);

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
