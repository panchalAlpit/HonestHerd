package com.example.honestherd.HHAdpater;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honestherd.HHGlobal.Utils;
import com.example.honestherd.HHModel.HHHistory_Model;
import com.example.honestherd.R;

import java.util.ArrayList;

public class HHHistory_adpater extends RecyclerView.Adapter<HHHistory_adpater.ViewHoldar> {

    ArrayList<HHHistory_Model> arrayList_historyModel;
    Context context;
    public HHHistory_adpater(Context context, ArrayList<HHHistory_Model> arrayList_historyModel) {
        this.arrayList_historyModel = arrayList_historyModel;
        this.context =context;
    }

    @NonNull
    @Override
    public HHHistory_adpater.ViewHoldar onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_history_list, parent, false);
        return new ViewHoldar(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HHHistory_adpater.ViewHoldar holder, int position) {

        holder.setIsRecyclable(false);
//        if (arrayList_historyModel.get(position).getType().equals(Utils.DEVICE_STATUS))
        {
          //  holder.txt_address.setTextColor(context.getResources().getColor(R.color.green));
        }
        holder.txt_address.setText(arrayList_historyModel.get(position).getAddress());
        holder.txt_date.setText(arrayList_historyModel.get(position).getSdate());
        holder.txt_time.setText(arrayList_historyModel.get(position).getS_time());

    }

    @Override
    public int getItemCount() {
        return arrayList_historyModel.size();
    }

    public class ViewHoldar extends RecyclerView.ViewHolder {
        AppCompatTextView txt_address,txt_date,txt_time;
        public ViewHoldar(@NonNull View itemView) {
            super(itemView);
            txt_address = itemView.findViewById(R.id.txt_address);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_date.setTypeface(Typeface.createFromAsset(context.getAssets(), Utils.DIN_BOLD));
            txt_time.setTypeface(Typeface.createFromAsset(context.getAssets(), Utils.DIN_BOLD));
            txt_address.setTypeface(Typeface.createFromAsset(context.getAssets(), Utils.DIN_MEDIUM));
        }
    }
}
