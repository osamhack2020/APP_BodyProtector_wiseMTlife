package com.osam.bodyprotector;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.communication.IOnItemFocusChangedListener;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class ChartActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference().child("Percent").child(firebaseAuth.getUid());


    private static class PercentAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<PercentListItem> PercentList = new ArrayList<PercentListItem>();

        public PercentAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }


        private class ViewHolder {
            public ImageView ExerImg;
            public TextView ExerciseTitle;
            public TextView ExercisePercent;
        }

        public void addItem(String ExerciseName, String ExercisePart, int percent, Date date){
            PercentListItem addInfo;
            addInfo = new PercentListItem(ExerciseName, ExercisePart, date, percent);
            PercentList.add(addInfo);
        }

        @Override
        public int getCount() {
            return PercentList.size();
        }

        @Override
        public Object getItem(int position) {
            return PercentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint({"ResourceType", "SetTextI18n"})
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.percentitem, null);

                holder.ExerImg = convertView.findViewById(R.id.percent_img);
                holder.ExerciseTitle = convertView.findViewById(R.id.percent_title);
                holder.ExercisePercent = convertView.findViewById(R.id.percent_txtP);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            final PercentListItem mData = PercentList.get(position);

            switch(mData.ExercisePart){
                case "팔":
                    holder.ExerImg.setImageResource(R.drawable.arms);
                    break;
                case "등":
                    holder.ExerImg.setImageResource(R.drawable.back);
                    break;
                case "어깨":
                    holder.ExerImg.setImageResource(R.drawable.shoulder);
                    break;
                case "복부":
                    holder.ExerImg.setImageResource(R.drawable.abdomen);
                    break;
                case "대퇴":
                    holder.ExerImg.setImageResource(R.drawable.leg);
                    break;
                case "둔부":
                    holder.ExerImg.setImageResource(R.drawable.buttocks);
                    break;
                case "가슴":
                    holder.ExerImg.setImageResource(R.drawable.chest);
                    break;
            }

            holder.ExerciseTitle.setText(mData.ExerciseName);
            holder.ExercisePercent.setText("총 " + mData.percent + "% 완료");

            return convertView;
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        final PieChart chart1 =  findViewById(R.id.tab1_chart_1);
        final Spinner spinner_percentPart = findViewById(R.id.spin_percentPart);
        final String spinner = spinner_percentPart.getSelectedItem().toString();
        final ListView percentList = findViewById(R.id.list_percent);
        final PercentAdapter adapter = new PercentAdapter(getBaseContext());
        percentList.setAdapter(adapter);

        chart1.clearChart();


        final BarChart mBarChart = (BarChart) findViewById(R.id.barchart);

        final int[] score = new int[12];
        for(int i = 0; i < 12; i++){
            score[i] = 0;
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    PercentData data = fileSnapshot.getValue(PercentData.class);
                    int cnt = 0;
                    for(int i = 0; i < data.list.size(); i++){
                        if(data.part.get(data.list.get(i)).equals(spinner) || spinner.equals("전체")){
                            score[data.date.getMonth()] += data.percent.get(data.list.get(i));
                            cnt++;
                        }
                    }
                    score[data.date.getMonth()] /= cnt;
                }
                for(int i = 0; i < 12; i++){
                    String _month = Integer.toString(i + 1) +"월";
                    Random rnd = new Random();
                    chart1.addPieSlice(new PieModel(_month, score[i], Color.argb(255,rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))));
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("HI", "값을 읽는데 실패했습니다.", error.toException());
            }
        });

        final ScrollView scroll = findViewById(R.id.percentScroll);
        chart1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scroll.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        mBarChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scroll.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        percentList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scroll.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        spinner_percentPart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String spinner = spinner_percentPart.getSelectedItem().toString();
                        for(int i = 0; i < 12; i++){
                            score[i] = 0;
                        }
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            PercentData data = fileSnapshot.getValue(PercentData.class);
                            int cnt = 0;
                            for(int i = 0; i < data.list.size(); i++){
                                if(data.part.get(data.list.get(i)).equals(spinner) || spinner.equals("전체")){
                                    score[data.date.getMonth()] += data.percent.get(data.list.get(i));

                                }
                            }
                        }
                        chart1.clearChart();
                        for(int i = 0; i < 12; i++){
                            String _month = Integer.toString(i + 1) +"월";
                            Random rnd = new Random();
                            chart1.addPieSlice(new PieModel(_month, score[i], Color.argb(255,rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))));
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w("HI", "값을 읽는데 실패했습니다.", error.toException());
                    }
                });
                chart1.startAnimation();

                chart1.setOnItemFocusChangedListener(new IOnItemFocusChangedListener() {
                    @Override
                    public void onItemFocusChanged(int _Position) {
                        String msg = chart1.getData().get(_Position).getLegendLabel();
                        int month;
                        if(msg.length() == 2){
                            month = Integer.parseInt(msg.substring(0,1));
                        }
                        else{
                            month = Integer.parseInt(msg.substring(0,2));
                        }
                        myRef.orderByChild("date/month").equalTo(month - 1).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                mBarChart.clearChart();
                                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                                    PercentData data = fileSnapshot.getValue(PercentData.class);
                                    String spinner = spinner_percentPart.getSelectedItem().toString();
                                    adapter.PercentList.clear();
                                    for(int i = 0; i < data.list.size(); i++){
                                        if(spinner.equals(data.part.get(data.list.get(i))) || spinner.equals("전체")){
                                            adapter.addItem(data.list.get(i), data.part.get(data.list.get(i)), data.percent.get(data.list.get(i)),data.date);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();

                                    String _date = (data.date.getMonth() + 1) +"월 " + data.date.getDate() + "일";
                                    Random rnd = new Random();
                                    mBarChart.addBar(new BarModel(_date, data.percentSum, Color.argb(255,rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))));
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError error) {
                                Log.w("HI", "값을 읽는데 실패했습니다.", error.toException());
                            }
                        });
                        mBarChart.startAnimation();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        chart1.startAnimation();



        chart1.setOnItemFocusChangedListener(new IOnItemFocusChangedListener() {
            @Override
            public void onItemFocusChanged(int _Position) {
                String msg = chart1.getData().get(_Position).getLegendLabel();
                int month;
                if(msg.length() == 2){
                    month = Integer.parseInt(msg.substring(0,1));
                }
                else{
                    month = Integer.parseInt(msg.substring(0,2));
                }
                myRef.orderByChild("date/month").equalTo(month - 1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mBarChart.clearChart();
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            PercentData data = fileSnapshot.getValue(PercentData.class);
                            String spinner = spinner_percentPart.getSelectedItem().toString();
                            for(int i = 0; i < data.list.size(); i++){
                                if(spinner.equals(data.part.get(data.list.get(i))) || spinner.equals("전체")){
                                    adapter.addItem(data.list.get(i), data.part.get(data.list.get(i)), data.percent.get(data.list.get(i)),data.date);
                                }
                            }

                            String _date = (data.date.getMonth() + 1) +"월 " + data.date.getDate() + "일";
                            Random rnd = new Random();
                            mBarChart.addBar(new BarModel(_date, data.percentSum, Color.argb(255,rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))));
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w("HI", "값을 읽는데 실패했습니다.", error.toException());
                    }
                });
                mBarChart.startAnimation();
            }
        });

    }

}
