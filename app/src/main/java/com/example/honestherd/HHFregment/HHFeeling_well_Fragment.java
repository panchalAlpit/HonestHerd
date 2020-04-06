package com.example.honestherd.HHFregment;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.honestherd.HHGlobal.Utils;
import com.example.honestherd.R;


public class HHFeeling_well_Fragment extends Fragment implements View.OnClickListener {

   private AppCompatTextView txt_how_youfeel,txt_feel_well,txt_feel_sick;
   LinearLayout linear_feel_well,linear_feel_sick;

    public HHFeeling_well_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_h_h_feeling_well_, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        txt_how_youfeel = view.findViewById(R.id.txt_how_youfeel);
        txt_feel_well = view.findViewById(R.id.txt_feel_well);
        txt_feel_sick = view.findViewById(R.id.txt_feel_sick);
        linear_feel_well = view.findViewById(R.id.linear_feel_well);
        linear_feel_sick = view.findViewById(R.id.linear_feel_sick);

        txt_how_youfeel.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Utils.DIN_BOLD));
        txt_feel_well.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Utils.DIN_BOLD));
        txt_feel_sick.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Utils.DIN_BOLD));

        linear_feel_sick.setOnClickListener(this);
        linear_feel_well.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_feel_well:{
                addFragment();
                break;
            }
            case R.id.linear_feel_sick:{
                AddNextStepFragment();
            }
        }
    }

    public void AddNextStepFragment(){

        Fragment f = new HHNextStepFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        String backStateName = f.getClass().getName();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, f, Utils.FRAGMENT_NextStep);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addFragment() {
        //first time call this method
        Fragment f = new HHMap_fregment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        String backStateName = f.getClass().getName();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, f, Utils.FRAGMENT_MAP);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
