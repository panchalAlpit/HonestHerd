package com.mobilefirst.honestherd.HHFregment;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mobilefirst.honestherd.HHGlobal.Utils;
import com.mobilefirst.honestherd.MainActivity;
import com.mobilefirst.honestherd.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HHNextStepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HHNextStepFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    private AppCompatTextView txt_self_assessment,txt_self_assessment_subtitle,txt_my_help_center,txt_my_help_center_subtitle,txt_export_my_trace,txt_export_my_trace_subtitle;
    private LinearLayout linear_cancle_nextstep,linear_self_assessment,linear_my_help_center,linear_export_my_trace;
    public HHNextStepFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HHNextStepFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HHNextStepFragment newInstance(String param1, String param2) {
        HHNextStepFragment fragment = new HHNextStepFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_h_h_next_step, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

        linear_cancle_nextstep = view.findViewById(R.id.linear_cancle_nextstep);
        linear_self_assessment = view.findViewById(R.id.linear_self_assessment);
        linear_my_help_center = view.findViewById(R.id.linear_my_help_center);
        linear_export_my_trace = view.findViewById(R.id.linear_export_my_trace);

        txt_self_assessment = view.findViewById(R.id.txt_self_assessment);
        txt_my_help_center = view.findViewById(R.id.txt_my_help_center);
        txt_export_my_trace = view.findViewById(R.id.txt_export_my_trace);

        txt_self_assessment_subtitle = view.findViewById(R.id.txt_self_assessment_subtitle);
        txt_my_help_center_subtitle = view.findViewById(R.id.txt_my_help_center_subtitle);
        txt_export_my_trace_subtitle = view.findViewById(R.id.txt_export_my_trace_subtitle);

        txt_self_assessment.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Utils.DIN_BOLD));
        txt_my_help_center.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Utils.DIN_BOLD));
        txt_export_my_trace.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Utils.DIN_BOLD));

        txt_self_assessment_subtitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Utils.DIN_MEDIUM));
        txt_my_help_center_subtitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Utils.DIN_MEDIUM));
        txt_export_my_trace_subtitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Utils.DIN_MEDIUM));
        linear_cancle_nextstep.setOnClickListener(this);
        linear_self_assessment.setOnClickListener(this);
        linear_my_help_center.setOnClickListener(this);
        linear_export_my_trace.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_cancle_nextstep:{
                ((MainActivity)getActivity()).onBackPressed();
                break;
            }
            case R.id.linear_self_assessment:{
                OpenWebView();
                break;
            }
            case R.id.linear_my_help_center:{
                OpenWebView();
                break;
            }
            case R.id.linear_export_my_trace:{

                break;
            }
        }
    }

    private void OpenWebView(){
        Intent viewIntent =
                new Intent("android.intent.action.VIEW",
                        Uri.parse("https://honestherd.com/"));
        startActivity(viewIntent);
    }
}
