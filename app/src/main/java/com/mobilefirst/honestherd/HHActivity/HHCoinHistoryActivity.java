package com.mobilefirst.honestherd.HHActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mobilefirst.honestherd.HHAdpater.HHCoinHistoryAdpater;
import com.mobilefirst.honestherd.HHGlobal.HHSharedPrefrence;
import com.mobilefirst.honestherd.HHGlobal.OnItemClickListener;
import com.mobilefirst.honestherd.HHGlobal.Utils;
import com.mobilefirst.honestherd.HHModel.HHDateModel;
import com.mobilefirst.honestherd.MainActivity;
import com.mobilefirst.honestherd.R;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

public class HHCoinHistoryActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recycle_coinhistory;
    AppCompatImageView img_close_coin;
    AppCompatTextView txt_totalcoin;
    HHCoinHistoryAdpater historyAdpater;
    ArrayList<HHDateModel> arrayListOfCoinDates = new ArrayList<>();
    Date startDate;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    int totalcoin = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_h_coin_history);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        init();

    }

    private void init() {
        txt_totalcoin = findViewById(R.id.txt_totalcoin);
        img_close_coin = findViewById(R.id.img_close_coin);
        recycle_coinhistory = findViewById(R.id.recycle_coinhistory);
        recycle_coinhistory.setLayoutManager(new LinearLayoutManager(HHCoinHistoryActivity.this));
        historyAdpater = new HHCoinHistoryAdpater(HHCoinHistoryActivity.this, getListOfDates(), new OnItemClickListener() {
            @Override
            public void OnItemClickListener(int position, ArrayList<HHDateModel> models) {
                Intent intent = new Intent();
                intent.putExtra("screen",Utils.FRAGMENT_TIRPHISTORY);
                intent.putExtra("date",models.get(position).getSdate());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        recycle_coinhistory.setAdapter(historyAdpater);
        recycle_coinhistory.setNestedScrollingEnabled(false);
        UpdateCoin();

        img_close_coin.setOnClickListener(this);
    }

    public ArrayList<HHDateModel> getListOfDates() {

        arrayListOfCoinDates.clear();

        Calendar LoginCal = GregorianCalendar.getInstance();

        LoginCal.add(Calendar.DATE, -Utils.NumberOFDays(HHSharedPrefrence.getJoindate(HHCoinHistoryActivity.this),Utils.getDateFromate("yyyy-MM-dd")));
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
                arrayListOfCoinDates.add(dateModel);

            }

        }
        Collections.reverse(arrayListOfCoinDates);
        return arrayListOfCoinDates;
    }

    public void UpdateCoin(){
        for (int i =0;i<arrayListOfCoinDates.size();i++){
            if (arrayListOfCoinDates.get(i).isActive()){
                FetchTotalCoins(arrayListOfCoinDates.get(i).getSdate(),i);
            }
        }
        historyAdpater.notifyDataSetChanged();
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
                                arrayListOfCoinDates.get(index).setCoins(s);
                                totalcoin = totalcoin+Integer.parseInt(s);
                                txt_totalcoin.setText(totalcoin+"");
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                        historyAdpater.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_close_coin:{
                onBackPressed();
                break;
            }
        }
    }
}
