package com.mobilefirst.honestherd.HHActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mobilefirst.honestherd.HHAdpater.HHHistory_adpater;
import com.mobilefirst.honestherd.HHGlobal.HHSharedPrefrence;
import com.mobilefirst.honestherd.HHGlobal.Utils;
import com.mobilefirst.honestherd.HHModel.HHHistory_Model;
import com.mobilefirst.honestherd.HHWebService.HHApiCall;
import com.mobilefirst.honestherd.HHWebService.OnUpdateListener;
import com.mobilefirst.honestherd.MainActivity;
import com.mobilefirst.honestherd.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HHTripHistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<HHHistory_Model> arrayList_historyModel = new ArrayList<>();
    RecyclerView recycle_history;
    private HHHistory_adpater history_adpater;
    AppCompatTextView txt_empty,txt_select_date;
    private AppCompatImageView btn_previous,btn_next;
    int date_index = 0,total_days = 0;
    AppCompatImageView img_close_triphistory;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hhtrip_history);
        String selectdate = getIntent().getStringExtra("date");
        init();
        HistoryApiCall(selectdate);

        total_days = Utils.NumberOFDays(HHSharedPrefrence.getJoindate(HHTripHistoryActivity.this),Utils.getDateFromate("yyyy-MM-dd"));
        Log.e("Number", "onCreateView: "+Utils.NumberOFDays("2020-03-25",Utils.getDateFromate("yyyy-MM-dd")) );
        Log.e("TAG", "onClick:OnCreate btn_previous"+date_index+" ---- "+total_days );
        if (total_days == date_index){
            btn_previous.setVisibility(View.GONE);
        }
        txt_select_date.setText(Utils.getFormateDate("dd-MMMM-yyyy",selectdate));
    }

    private void init() {
        recycle_history = findViewById(R.id.recycle_history);
        txt_empty = findViewById(R.id.txt_empty);
        btn_previous = findViewById(R.id.btn_previous);
        btn_next = findViewById(R.id.btn_next);
        txt_select_date = findViewById(R.id.txt_select_date);
        img_close_triphistory = findViewById(R.id.img_close_triphistory);

        btn_previous.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        img_close_triphistory.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void HistoryApiCall(String date) {

        arrayList_historyModel = new ArrayList<>();
        String url = Utils.HyperTrack_URL+MainActivity.hyperTrack.getDeviceID()+Utils.Api_history+date;
//        String url = "https://v3.api.hypertrack.com/devices/"+deviceid+"/history/2020-03-28";
        new HHApiCall(HHTripHistoryActivity.this, url, new OnUpdateListener() {
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

                    history_adpater = new HHHistory_adpater(HHTripHistoryActivity.this,arrayList_historyModel);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HHTripHistoryActivity.this);
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
//        ((MainActivity)getContext()).txt_date_map_fregment.setText(fordate.format(calendar.getTime()));
//        ((MainActivity)getContext()).txt_month_map_fregment.setText(formonth.format(calendar.getTime()));
        txt_select_date.setText(Utils.getFormateDate("dd-MMMM-yyyy",yesterdayAsString));
        HistoryApiCall(yesterdayAsString);
    }
}
