package com.osam.bodyprotector;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class RoutineOptionFragment extends Fragment {

    private TextView txt_routineoption;
    private TextView txt_submessageV;
    private Button bt_yes;
    private Button bt_no;

    public interface CallbackListner{
        void onRecieveData(boolean bool, int cnt);
    }

    private CallbackListner CListner;

    public void onAttach(Context context) {
        super.onAttach(context);
        if(getActivity() != null && getActivity() instanceof CallbackListner){
            CListner = (CallbackListner) getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_routine_option, container, false);
        txt_routineoption = (TextView)v.findViewById(R.id.txt_RoutineOption);
        txt_submessageV = (TextView)v.findViewById(R.id.txt_submessage);

        bt_yes = (Button)v.findViewById(R.id.bt_RoutineYes);
        bt_no = (Button)v.findViewById(R.id.bt_RoutineNo);

        Bundle bundle = getArguments();
        String txt_message = bundle.getString("message",null);
        String txt_submessage = bundle.getString("submessage",null);
        final int count = bundle.getInt("count",0);

        txt_routineoption.setText(txt_message);
        txt_submessageV.setText(txt_submessage);
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CListner.onRecieveData(true, count + 1);
            }
        });
        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CListner.onRecieveData(false, count + 1);
            }
        });
        return v;
    }
}