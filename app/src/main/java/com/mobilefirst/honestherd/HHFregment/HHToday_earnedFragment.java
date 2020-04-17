package com.mobilefirst.honestherd.HHFregment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobilefirst.honestherd.R;


public class HHToday_earnedFragment extends Fragment {

    public HHToday_earnedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_h_h_today_earned, container, false);
        return view;
    }
}
