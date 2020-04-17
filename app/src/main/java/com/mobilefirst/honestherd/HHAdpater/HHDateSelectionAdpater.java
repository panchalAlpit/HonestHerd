package com.mobilefirst.honestherd.HHAdpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilefirst.honestherd.HHGlobal.OnItemClickListener;
import com.mobilefirst.honestherd.HHGlobal.Utils;
import com.mobilefirst.honestherd.HHModel.HHDateModel;
import com.mobilefirst.honestherd.R;

import java.util.ArrayList;

public class HHDateSelectionAdpater extends RecyclerView.Adapter<HHDateSelectionAdpater.MyHolder> {

    Context context;
    ArrayList<HHDateModel> listOfDates;
     OnItemClickListener onItemClickListener;
    public HHDateSelectionAdpater(Context context, ArrayList<HHDateModel> listOfDates, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.listOfDates = listOfDates;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_date_selection, parent, false);
        return new HHDateSelectionAdpater.MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
//    holder.txt_date_selction_row.setText(listOfDates.get(position).getDay());
    holder.txt_date_selction_row.setText(Utils.getFormateDate("dd",listOfDates.get(position).getSdate()));
    holder.txt_month_year_selction_row.setText(Utils.getFormateDate("MMM yyyy",listOfDates.get(position).getSdate()));
    holder.txt_everyday_coins.setText(listOfDates.get(position).getCoins());

    if (listOfDates.get(position).isActive()){
        holder.txt_date_selction_row.setTextColor(context.getResources().getColor(R.color.white));
        holder.txt_month_year_selction_row.setTextColor(context.getResources().getColor(R.color.white));
    }else {
        holder.txt_date_selction_row.setTextColor(context.getResources().getColor(R.color.unseleted));
        holder.txt_month_year_selction_row.setTextColor(context.getResources().getColor(R.color.unseleted));
    }

    holder.linear_row_date_selection.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onItemClickListener.OnItemClickListener(position,listOfDates);
        }
    });

    }

    @Override
    public int getItemCount() {
        return listOfDates.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        LinearLayout linear_row_date_selection;
        AppCompatTextView txt_date_selction_row,txt_month_year_selction_row,txt_everyday_coins;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            linear_row_date_selection = itemView.findViewById(R.id.linear_row_date_selection);
            txt_date_selction_row = itemView.findViewById(R.id.txt_date_selction_row);
            txt_month_year_selction_row = itemView.findViewById(R.id.txt_month_year_selction_row);
            txt_everyday_coins = itemView.findViewById(R.id.txt_everyday_coins);

        }
    }


}
