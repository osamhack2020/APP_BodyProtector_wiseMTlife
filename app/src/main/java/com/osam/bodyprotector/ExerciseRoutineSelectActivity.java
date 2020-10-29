package com.osam.bodyprotector;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Vector;

import github.chenupt.springindicator.viewpager.ScrollerViewPager;

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

        public void addItem(Exercise exercise, int countperset, int set, int weight){
            RoutineData addInfo = null;
            addInfo = new RoutineData(exercise, countperset, set, weight);

            RoutineData.add(addInfo);
        }

        public void changeItem(int position, RoutineData after){
            RoutineData.set(position, after);
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

            holder.ExerciseCount.setText(Integer.parseInt(String.valueOf(mData.weight)) + "kg로 세트당 " + Integer.toString(mData.countperset) + "회씩 총 " + Integer.toString(mData.set) + "세트");
            holder.ExerciseName.setText(mData.exercise.ExerciseName);

            if(mData.exercise.ExerciseName.equals("휴식")){
                holder.ExerciseCount.setText("근육 휴식 기간");
                holder.ExerciseDifficulty.setText("");
            }
            return convertView;
        }
    }

    private ScrollerViewPager viewpager;
    private ListView exerciselist;
    private TextView txt_date;
    private Button btOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_routine_select);

        final Gson gson = new Gson();
        Intent intent = getIntent();
        ExerJoinList joinlist = gson.fromJson(intent.getExtras().getString("json"),ExerJoinList.class);
        String json = intent.getExtras().getString("routine");
        ExerciseRoutine routine = gson.fromJson(json,ExerciseRoutine.class);


        btOK = findViewById(R.id.btOK);

        viewpager = (ScrollerViewPager)findViewById(R.id.daily_routine);
        exerciselist = (ListView)findViewById(R.id.ExerciseList);

        Vector<View> pages = new Vector<View>();
        final ListViewAdapter Eadapter = new ListViewAdapter(getBaseContext());

        exerciselist.setAdapter(Eadapter);

        CustomPagerAdapter pagerAdapter = new CustomPagerAdapter(getBaseContext(), pages);
        final ListViewAdapter[] adapters = new ListViewAdapter[7];
        for(int i = 0; i < 7; i++){
            ListView list = new ListView(getBaseContext());
            ListViewAdapter adapter = new ListViewAdapter(getBaseContext());
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            final int finalI = i;
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(!adapters[finalI].RoutineData.get(position).exercise.ExerciseName.equals("휴식")){
                        CustomDialog cd = new CustomDialog(ExerciseRoutineSelectActivity.this);
                        cd.callFunction(Eadapter, adapters[finalI],position);
                    }
                }
            });
            adapters[i] = adapter;
            pages.add(list);
        }

        for (Exercise E : joinlist.list) {
            Eadapter.addItem(E, 30, 3, 20);
        }
        exerciselist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomDialog_list cdl = new CustomDialog_list(ExerciseRoutineSelectActivity.this);
                cdl.callFunction(adapters, Eadapter, position);
            }
        });
        Eadapter.notifyDataSetChanged();

        for(int i = 0; i < 7; i++){
            if(routine.Daily.get(i).isEmpty()){
                Exercise e = new Exercise("휴식","전신", 0, false, false, false);
                routine.Daily.get(i).add(e);
            }
            for(Exercise E : routine.Daily.get(i)){
                RoutineData Rdata = new RoutineData(E, routine.count, routine.set, routine.weight);
                adapters[i].RoutineData.add(Rdata);
            }
            adapters[i].notifyDataSetChanged();
        }



        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseRoutine routine = new ExerciseRoutine();
                int i = 0;
                for(ListViewAdapter adapter : adapters){
                    for(RoutineData data : adapter.RoutineData){
                        routine.Daily.get(i).add(data.exercise);
                    }
                    i++;
                }
                ExerJoinList list = new ExerJoinList();
                for(RoutineData data : Eadapter.RoutineData){
                    list.list.add(data.exercise);
                }
                String json = gson.toJson(routine);
                String listjson = gson.toJson(list);
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ExerciseRoutineSelectActivity.this);
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("ExerciseRoutine", json);
                editor.putString("ExerciseList", listjson);
                editor.apply();
                finish();
                Toast.makeText(ExerciseRoutineSelectActivity.this, "성공적으로 루틴을 저장했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        viewpager.setAdapter(pagerAdapter);
        txt_date = findViewById(R.id.txt_date);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        txt_date.setText("월요일");
                        break;
                    case 1:
                        txt_date.setText("화요일");
                        break;
                    case 2:
                        txt_date.setText("수요일");
                        break;
                    case 3:
                        txt_date.setText("목요일");
                        break;
                    case 4:
                        txt_date.setText("금요일");
                        break;
                    case 5:
                        txt_date.setText("토요일");
                        break;
                    case 6:
                        txt_date.setText("일요일");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;
        private Vector<View> pages;

        public CustomPagerAdapter(Context context, Vector<View> pages) {
            this.mContext=context;
            this.pages=pages;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View page = pages.get(position);
            container.addView(page);
            return page;
        }

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private class CustomDialog {

        private Context context;

        public CustomDialog(Context context) {
            this.context = context;
        }

        public void callFunction(final ListViewAdapter exerciseListAdapter, final ListViewAdapter adapter, final int position) {
            final Dialog dlg = new Dialog(context);

            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dlg.setContentView(R.layout.custom_dialog);
            dlg.show();

            final Spinner spin_count = dlg.findViewById(R.id.spin_list_count);
            final Spinner spin_set = dlg.findViewById(R.id.spin_list_set);
            final Spinner spin_weight = dlg.findViewById(R.id.spin_list_weight);
            final Button okButton = (Button) dlg.findViewById(R.id.okButton_list);
            final Button cancelButton = (Button) dlg.findViewById(R.id.cancelButton_list);
            final Button deleteButton = (Button) dlg.findViewById(R.id.deleteButton);
            Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30};
            ArrayAdapter<Integer> count_adapter = new ArrayAdapter<Integer>(ExerciseRoutineSelectActivity.this, android.R.layout.simple_spinner_item, items);
            spin_count.setAdapter(count_adapter);
            spin_count.setSelection(19);
            Integer[] items_set = new Integer[]{1,2,3,4,5,6,7,8,9,10};
            ArrayAdapter<Integer> set_adapter = new ArrayAdapter<Integer>(ExerciseRoutineSelectActivity.this,android.R.layout.simple_spinner_item, items_set);
            spin_set.setAdapter(set_adapter);
            spin_set.setSelection(2);
            Integer[] items_weight = new Integer[]{5,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95,100,105,110,115,120};
            ArrayAdapter<Integer> weight_adapter = new ArrayAdapter<Integer>(ExerciseRoutineSelectActivity.this,android.R.layout.simple_spinner_item, items_weight);
            spin_weight.setAdapter(weight_adapter);
            spin_weight.setSelection(5);



            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int count = Integer.parseInt(spin_count.getSelectedItem().toString());
                    final int set = Integer.parseInt(spin_set.getSelectedItem().toString());
                    final int weight = Integer.parseInt(spin_weight.getSelectedItem().toString());

                    RoutineData data = adapter.RoutineData.get(position);

                    data.weight = weight;
                    data.set = set;
                    data.countperset = count;
                    adapter.changeItem(position,data);
                    Toast.makeText(context, "성공적으로 수정했습니다.", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    dlg.dismiss();
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "취소했습니다.", Toast.LENGTH_SHORT).show();
                    dlg.dismiss();
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exerciseListAdapter.RoutineData.add(adapter.RoutineData.get(position));
                    adapter.RoutineData.remove(position);
                    Toast.makeText(context, "성공적으로 삭제했습니다.", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    exerciseListAdapter.notifyDataSetChanged();
                    dlg.dismiss();
                }
            });
        }
    }
    private class CustomDialog_list {

        private Context context;

        public CustomDialog_list(Context context) {
            this.context = context;
        }

        public void callFunction(final ListViewAdapter[] adapters, final ListViewAdapter adapter, final int position) {
            final Dialog dlg = new Dialog(context);

            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dlg.setContentView(R.layout.custom_dialog_list);
            dlg.show();

            final Spinner spin_date = dlg.findViewById(R.id.spin_list_day);
            final Spinner spin_count = dlg.findViewById(R.id.spin_list_count);
            final Spinner spin_set = dlg.findViewById(R.id.spin_list_set);
            final Spinner spin_weight = dlg.findViewById(R.id.spin_list_weight);
            final Button okButton = (Button) dlg.findViewById(R.id.okButton_list);
            final Button cancelButton = (Button) dlg.findViewById(R.id.cancelButton_list);
            String[] items_date = new String[]{"월요일","화요일","수요일","목요일","금요일","토요일","일요일"};
            ArrayAdapter<String> date_adapter = new ArrayAdapter<String>(ExerciseRoutineSelectActivity.this, android.R.layout.simple_spinner_item, items_date);
            spin_date.setAdapter(date_adapter);
            Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30};
            ArrayAdapter<Integer> count_adapter = new ArrayAdapter<Integer>(ExerciseRoutineSelectActivity.this, android.R.layout.simple_spinner_item, items);
            spin_count.setAdapter(count_adapter);
            spin_count.setSelection(19);
            Integer[] items_set = new Integer[]{1,2,3,4,5,6,7,8,9,10};
            ArrayAdapter<Integer> set_adapter = new ArrayAdapter<Integer>(ExerciseRoutineSelectActivity.this,android.R.layout.simple_spinner_item, items_set);
            spin_set.setAdapter(set_adapter);
            spin_set.setSelection(2);
            Integer[] items_weight = new Integer[]{5,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95,100,105,110,115,120};
            ArrayAdapter<Integer> weight_adapter = new ArrayAdapter<Integer>(ExerciseRoutineSelectActivity.this,android.R.layout.simple_spinner_item, items_weight);
            spin_weight.setAdapter(weight_adapter);
            spin_weight.setSelection(5);



            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String date = spin_date.getSelectedItem().toString();
                    final int count = Integer.parseInt(spin_count.getSelectedItem().toString());
                    final int set = Integer.parseInt(spin_set.getSelectedItem().toString());
                    final int weight = Integer.parseInt(spin_weight.getSelectedItem().toString());

                    RoutineData data = adapter.RoutineData.get(position);
                    data.weight = weight;
                    data.set = set;
                    data.countperset = count;

                    adapter.RoutineData.remove(position);

                    switch(date){
                        case "월요일":
                            adapters[0].RoutineData.add(data);
                            break;
                        case "화요일":
                            adapters[1].RoutineData.add(data);
                            break;
                        case "수요일":
                            adapters[2].RoutineData.add(data);
                            break;
                        case "목요일":
                            adapters[3].RoutineData.add(data);
                            break;
                        case "금요일":
                            adapters[4].RoutineData.add(data);
                            break;
                        case "토요일":
                            adapters[5].RoutineData.add(data);
                            break;
                        case "일요일":
                            adapters[6].RoutineData.add(data);
                            break;
                    }
                    Toast.makeText(context, "성공적으로 수정했습니다.", Toast.LENGTH_SHORT).show();
                    for(int i = 0; i < 7; i++){
                        adapters[i].notifyDataSetChanged();
                    }
                    adapter.notifyDataSetChanged();
                    dlg.dismiss();
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "취소했습니다.", Toast.LENGTH_SHORT).show();
                    dlg.dismiss();
                }
            });
        }
    }
}