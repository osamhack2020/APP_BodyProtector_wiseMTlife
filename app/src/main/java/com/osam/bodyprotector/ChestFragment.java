package com.osam.bodyprotector;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ChestFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chest, container, false);
        TextView text_partname = (TextView)v.findViewById(R.id.text_partname);
        if(getArguments() != null){
            Bundle args = getArguments();
            String s = args.getString("partname");
            text_partname.setText(s);
        }
        return v;
    }
}