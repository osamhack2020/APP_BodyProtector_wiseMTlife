package com.osam.bodyprotector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Vector;

public class ExerciseRoutineSelectActivity extends AppCompatActivity {

    private static class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<RoutineData> RoutineData = new ArrayList<RoutineData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }


        private class ViewHolder {
            public ImageView ExerImg;
            public TextView ExerciseName;
            public TextView ExerciseCount;
            public TextView ExerciseDifficulty;
        }

        public void addItem(Exercise exercise, int countperset, int set){
            RoutineData addInfo = null;
            addInfo = new RoutineData(exercise, countperset, set);

            RoutineData.add(addInfo);
        }


        @Override
        public int getCount() {
            return RoutineData.size();
        }

        @Override
        public Object getItem(int position) {
            return RoutineData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("ResourceType")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.exerciseroutineitem, null);

                holder.ExerImg = convertView.findViewById(R.id.routine_img);
                holder.ExerciseName = convertView.findViewById(R.id.routine_title);
                holder.ExerciseCount = convertView.findViewById(R.id.routine_count);
                holder.ExerciseDifficulty = convertView.findViewById(R.id.routine_difficulty);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            final RoutineData mData = RoutineData.get(position);

            switch (mData.exercise.Difficulty){
                case 0:
                    holder.ExerciseDifficulty.setText("난이도 하");
                    break;
                case 1:
                    holder.ExerciseDifficulty.setText("난이도 중");
                    break;
                case 2:
                    holder.ExerciseDifficulty.setText("난이도 상");
                    break;
            }
            switch(mData.exercise.Part){
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

            holder.ExerciseCount.setText("세트당 " + Integer.toString(mData.countperset) + "회씩 총 " + Integer.toString(mData.set) + "세트");
            holder.ExerciseName.setText(mData.exercise.ExerciseName);
            return convertView;
        }
    }

    private ViewPager viewpager;
    private ListView exerciselist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_routine_select);

        Gson gson = new Gson();
        Intent intent = getIntent();
        ExerJoinList joinlist = gson.fromJson(intent.getExtras().getString("json"),ExerJoinList.class);

        viewpager = (ViewPager)findViewById(R.id.daily_routine);
        exerciselist = (ListView)findViewById(R.id.ExerciseList);
        Vector<View> pages = new Vector<View>();
        ListView list = new ListView(getBaseContext());
        ListViewAdapter adapter = new ListViewAdapter(getBaseContext());
        list.setAdapter(adapter);
        ListViewAdapter Eadapter = new ListViewAdapter(getBaseContext());
        exerciselist.setAdapter(Eadapter);
        for(Exercise E : joinlist.list){
            ExerciseData data = new ExerciseData();
            Eadapter.addItem(E,30,3);
        }
        Eadapter.notifyDataSetChanged();

        ExerciseActivity.CustomPagerAdapter pagerAdapter = new ExerciseActivity.CustomPagerAdapter(this, pages);

    }


}