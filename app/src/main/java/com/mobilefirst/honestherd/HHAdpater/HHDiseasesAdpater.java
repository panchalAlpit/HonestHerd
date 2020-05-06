package com.mobilefirst.honestherd.HHAdpater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.mobilefirst.honestherd.HHActivity.HHStayHomeActivity;
import com.mobilefirst.honestherd.HHActivity.IMSickActivity;
import com.mobilefirst.honestherd.HHGlobal.OnItemClickListener;
import com.mobilefirst.honestherd.HHGlobal.OnItemClickListener_other;
import com.mobilefirst.honestherd.HHModel.HHDiseaseModel;
import com.mobilefirst.honestherd.R;

import java.util.ArrayList;

public class HHDiseasesAdpater extends RecyclerView.Adapter<HHDiseasesAdpater.MyViewHolder> {
    Context context;
    ArrayList<HHDiseaseModel> arrayListDiseaseModel;
     OnItemClickListener_other onItemClickListener;
    public HHDiseasesAdpater(Context context, ArrayList<HHDiseaseModel> arrayListDiseaseModel, OnItemClickListener_other onItemClickListener) {
    this.context = context;
    this.arrayListDiseaseModel = arrayListDiseaseModel;
    this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_diseases, parent, false);
        return new HHDiseasesAdpater.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.setIsRecyclable(true);
        if (arrayListDiseaseModel.get(position).getType().equals("sick")){
            holder.linear_pink_box.setVisibility(View.VISIBLE);
            holder.linear_green_box.setVisibility(View.GONE);
            holder.txt_pink_box.setText(arrayListDiseaseModel.get(position).getName());
        }else {
            holder.linear_pink_box.setVisibility(View.GONE);
            holder.linear_green_box.setVisibility(View.VISIBLE);
            holder.txt_green_box.setText(arrayListDiseaseModel.get(position).getName());
        }
        holder.linear_parent_diseases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClickListener(position,arrayListDiseaseModel);

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListDiseaseModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linear_pink_box,linear_green_box,linear_parent_diseases;
        AppCompatTextView txt_pink_box,txt_green_box;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linear_pink_box = itemView.findViewById(R.id.linear_pink_box);
            linear_green_box = itemView.findViewById(R.id.linear_green_box);
            txt_pink_box = itemView.findViewById(R.id.txt_pink_box);
            txt_green_box = itemView.findViewById(R.id.txt_green_box);
            linear_parent_diseases = itemView.findViewById(R.id.linear_parent_diseases);
        }
    }
}
