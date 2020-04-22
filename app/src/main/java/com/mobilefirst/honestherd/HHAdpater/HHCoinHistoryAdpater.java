package com.mobilefirst.honestherd.HHAdpater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilefirst.honestherd.HHActivity.HHCoinHistoryActivity;
import com.mobilefirst.honestherd.HHGlobal.OnItemClickListener;
import com.mobilefirst.honestherd.HHGlobal.Utils;
import com.mobilefirst.honestherd.HHModel.HHDateModel;
import com.mobilefirst.honestherd.MainActivity;
import com.mobilefirst.honestherd.R;

import java.util.ArrayList;

public class HHCoinHistoryAdpater extends RecyclerView.Adapter<HHCoinHistoryAdpater.MyHoldar> {
    Context context;
    ArrayList<HHDateModel> arrayListOfCoinDates;
    OnItemClickListener onItemClickListener;
    public HHCoinHistoryAdpater(Context context, ArrayList<HHDateModel> arrayListOfCoinDates, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.arrayListOfCoinDates = arrayListOfCoinDates;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public HHCoinHistoryAdpater.MyHoldar onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_coin_history, parent, false);
        return new HHCoinHistoryAdpater.MyHoldar(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HHCoinHistoryAdpater.MyHoldar holder, final int position) {
        holder.txt_date_row_coin.setText(Utils.getFormateDate("MMM dd",arrayListOfCoinDates.get(position).getSdate()));
        holder.txt_coin_row.setText(arrayListOfCoinDates.get(position).getCoins());
        if (arrayListOfCoinDates.get(position).isActive()){
            holder.txt_date_row_coin.setVisibility(View.VISIBLE);
        }else {
            holder.txt_date_row_coin.setVisibility(View.GONE);
        }

        holder.constraint_coinshistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClickListener(position,arrayListOfCoinDates);


            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListOfCoinDates.size();
    }

    public class MyHoldar extends RecyclerView.ViewHolder {
        AppCompatTextView txt_date_row_coin,txt_coin_row;
        ConstraintLayout constraint_coinshistory;
        public MyHoldar(@NonNull View itemView) {
            super(itemView);
            txt_date_row_coin = itemView.findViewById(R.id.txt_date_row_coin);
            txt_coin_row = itemView.findViewById(R.id.txt_coin_row);
            constraint_coinshistory = itemView.findViewById(R.id.constraint_coinshistory);
        }
    }
}
