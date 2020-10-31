package com.osam.bodyprotector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;


public class ExerciseActivity extends AppCompatActivity {

    private static class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ExerciseData> ExerData = new ArrayList<ExerciseData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }


        private class ViewHolder {
            public ImageView ExerImg;
            public ImageView check_babel;
            public ImageView check_dumbel;
            public ImageView check_outfit;
            public TextView ExerciseName;
            public TextView ExerciseDescription;
            public TextView ExerciseDifficulty;
        }

        public void addItem(String ExerciseName, String ExerciseDescription, Drawable ExerciseImg, int ExerciseDifficulty, int isDumbel, int isOutfit, int isBabel, String ExerPart){
            ExerciseData addInfo = null;
            addInfo = new ExerciseData();
            addInfo.ExerciseName = ExerciseName;
            addInfo.ExerPart = ExerPart;
            addInfo.ExerciseDescription = ExerciseDescription;
            addInfo.ExerciseDifficulty = ExerciseDifficulty;
            addInfo.ExerciseImg = ExerciseImg;
            addInfo.isBabel = isBabel;
            addInfo.isDumbel = isDumbel;
            addInfo.isOutfit = isOutfit;

            ExerData.add(addInfo);
        }


        @Override
        public int getCount() {
            return ExerData.size();
        }

        @Override
        public Object getItem(int position) {
            return ExerData.get(position);
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
                convertView = inflater.inflate(R.xml.exerciseitem, null);

                holder.ExerImg = convertView.findViewById(R.id.img_exercise);
                holder.check_babel = convertView.findViewById(R.id.check_babel);
                holder.check_dumbel = convertView.findViewById(R.id.check_dumbel);
                holder.check_outfit = convertView.findViewById(R.id.check_outfit);
                holder.ExerciseName = convertView.findViewById(R.id.txt_exercisename);
                holder.ExerciseDescription = convertView.findViewById(R.id.txt_exercisedescription);
                holder.ExerciseDifficulty = convertView.findViewById(R.id.txt_exerInfodifficulty);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            final ExerciseData mData = ExerData.get(position);

            switch (mData.ExerciseDifficulty){
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
            holder.ExerImg.setImageDrawable(mData.ExerciseImg);
            holder.ExerciseDescription.setText(mData.ExerciseDescription);
            if(mData.isDumbel == 1) {
                holder.check_dumbel.setImageResource(android.R.drawable.checkbox_on_background);
            }
            else{
                holder.check_dumbel.setImageResource(android.R.drawable.checkbox_off_background);
            }
            if(mData.isBabel == 1) {
                holder.check_babel.setImageResource(android.R.drawable.checkbox_on_background);
            }
            else{
                holder.check_babel.setImageResource(android.R.drawable.checkbox_off_background);
            }
            if(mData.isOutfit == 1) {
                holder.check_outfit.setImageResource(android.R.drawable.checkbox_on_background);
            }
            else{
                holder.check_outfit.setImageResource(android.R.drawable.checkbox_off_background);
            }
            holder.ExerciseName.setText(mData.ExerciseName);
            return convertView;
        }
    }




    String[] partname = {"어깨","등","팔","가슴","복부","대퇴","둔부"};

    @SuppressLint({"UseCompatLoadingForDrawables", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        final String[] name_arms = getResources().getStringArray(R.array.ExerciseName_Arms);
        final String[] description_arms = getResources().getStringArray(R.array.ExerciseDescription_Arms);
        final String[] detail_arms = getResources().getStringArray(R.array.ExerciseDetail_Arms);
        final int[] babel_arms = getResources().getIntArray(R.array.Exercisebabel_Arms);
        final int[] dumbel_arms = getResources().getIntArray(R.array.Exercisedumbel_Arms);
        final int[] outfit_arms = getResources().getIntArray(R.array.Exerciseoutfit_Arms);
        final int[] difficulty_arms = getResources().getIntArray(R.array.Exercisedifficulty_Arms);

        final String[] name_abdomen = getResources().getStringArray(R.array.ExerciseName_Abdomen);
        final String[] description_abdomen = getResources().getStringArray(R.array.ExerciseDescription_Abdomen);
        final String[] detail_abdomen = getResources().getStringArray(R.array.ExerciseDetail_Abdomen);
        final int[] babel_abdomen = getResources().getIntArray(R.array.Exercisebabel_Abdomen);
        final int[] dumbel_abdomen = getResources().getIntArray(R.array.Exercisedumbel_Abdomen);
        final int[] outfit_abdomen = getResources().getIntArray(R.array.Exerciseoutfit_Abdomen);
        final int[] difficulty_abdomen = getResources().getIntArray(R.array.Exercisedifficulty_Abdomen);

        final String[] name_back = getResources().getStringArray(R.array.ExerciseName_Back);
        final String[] description_back = getResources().getStringArray(R.array.ExerciseDescription_Back);
        final String[] detail_back = getResources().getStringArray(R.array.Exercisebabel_Back);
        final int[] babel_back = getResources().getIntArray(R.array.Exercisebabel_Back);
        final int[] dumbel_back = getResources().getIntArray(R.array.Exercisedumbel_Back);
        final int[] outfit_back = getResources().getIntArray(R.array.Exerciseoutfit_Back);
        final int[] difficulty_back = getResources().getIntArray(R.array.Exercisedifficulty_Back);

        final String[] name_buttocks = getResources().getStringArray(R.array.ExerciseName_Buttocks);
        final String[] description_buttocks = getResources().getStringArray(R.array.ExerciseDescription_Buttocks);
        final String[] detail_buttocks = getResources().getStringArray(R.array.Exercisebabel_Buttocks);
        final int[] babel_buttocks = getResources().getIntArray(R.array.Exercisebabel_Buttocks);
        final int[] dumbel_buttocks = getResources().getIntArray(R.array.Exercisedumbel_Buttocks);
        final int[] outfit_buttocks = getResources().getIntArray(R.array.Exerciseoutfit_Buttocks);
        final int[] difficulty_buttocks = getResources().getIntArray(R.array.Exercisedifficulty_Buttocks);

        final String[] name_chest = getResources().getStringArray(R.array.ExerciseName_Chest);
        final String[] description_chest = getResources().getStringArray(R.array.ExerciseDescription_Chest);
        final String[] detail_chest = getResources().getStringArray(R.array.ExerciseDetail_Chest);
        final int[] babel_chest = getResources().getIntArray(R.array.Exercisebabel_Chest);
        final int[] dumbel_chest = getResources().getIntArray(R.array.Exercisedumbel_Chest);
        final int[] outfit_chest = getResources().getIntArray(R.array.Exerciseoutfit_Chest);
        final int[] difficulty_chest = getResources().getIntArray(R.array.Exercisedifficulty_Chest);

        final String[] name_legs = getResources().getStringArray(R.array.ExerciseName_Legs);
        final String[] description_legs = getResources().getStringArray(R.array.ExerciseDescription_Legs);
        final String[] detail_legs = getResources().getStringArray(R.array.ExerciseDetail_Legs);
        final int[] babel_legs = getResources().getIntArray(R.array.Exercisebabel_Legs);
        final int[] dumbel_legs = getResources().getIntArray(R.array.Exercisedumbel_Legs);
        final int[] outfit_legs = getResources().getIntArray(R.array.Exerciseoutfit_Legs);
        final int[] difficulty_legs = getResources().getIntArray(R.array.Exercisedifficulty_Legs);

        final String[] name_shoulders = getResources().getStringArray(R.array.ExerciseName_Shoulder);
        final String[] description_shoulders = getResources().getStringArray(R.array.ExerciseDescription_Shoulder);
        final String[] detail_shoulders = getResources().getStringArray(R.array.ExerciseDetail_Shoulder);
        final int[] babel_shoulders = getResources().getIntArray(R.array.Exercisebabel_Shoulder);
        final int[] dumbel_shoulders = getResources().getIntArray(R.array.Exercisedumbel_Shoulder);
        final int[] outfit_shoulders = getResources().getIntArray(R.array.Exerciseoutfit_Shoulder);
        final int[] difficulty_shoulders = getResources().getIntArray(R.array.Exercisedifficulty_Shoulder);

        final ScrollView scroll = findViewById(R.id.scroll);
        ScrollerViewPager viewpager = findViewById(R.id.viewpager);
        SpringIndicator springIndicator = findViewById(R.id.indicator);
        viewpager.fixScrollSpeed();
        Vector<View> pages = new Vector<>();
        CustomPagerAdapter Padapter = new CustomPagerAdapter(getBaseContext(),pages);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        for(final String s : partname){

            ListView list = new ListView(getBaseContext());
            ListViewAdapter Ladapter = new ListViewAdapter(this);
            boolean isfilter = pref.getBoolean("filter", false);
            boolean isbabel = pref.getBoolean("isBabel", false);
            boolean isdumbel = pref.getBoolean("isDumbel", false);
            boolean isoutfit = pref.getBoolean("isOutfit", false);
            switch(s){
                case "팔":
                    for(int i = 0; i < 17; i++){
                        if(!isfilter || isbabel && babel_arms[i] == 1 || isdumbel && dumbel_arms[i] == 1 || isoutfit && outfit_arms[i] == 1)
                            Ladapter.addItem(name_arms[i], description_arms[i],getResources().getDrawable(R.drawable.arms),difficulty_arms[i],dumbel_arms[i],outfit_arms[i],babel_arms[i],"팔");
                    }
                    break;
                case "어깨":
                    for(int i = 0; i < 19; i++){
                        if(!isfilter || isfilter && ((isbabel && babel_shoulders[i] == 1) || (isdumbel && dumbel_shoulders[i] == 1) || (isoutfit && outfit_shoulders[i] == 1)))
                            Ladapter.addItem(name_shoulders[i], description_shoulders[i],getResources().getDrawable(R.drawable.shoulder),difficulty_shoulders[i],dumbel_shoulders[i],outfit_shoulders[i],babel_shoulders[i],"어깨");
                    }
                    break;
                case "등":
                    for(int i = 0; i < 14; i++){
                        if(!isfilter || isfilter && ((isbabel && babel_back[i] == 1) || (isdumbel && dumbel_back[i] == 1) || (isoutfit && outfit_back[i] == 1)))
                            Ladapter.addItem(name_back[i], description_back[i],getResources().getDrawable(R.drawable.back),difficulty_back[i],dumbel_back[i],outfit_back[i],babel_back[i],"등");
                    }
                    break;
                case "복부":
                    for(int i = 0; i < 13; i++){
                        if(!isfilter || isfilter && ((isbabel && babel_abdomen[i] == 1) || (isdumbel && dumbel_abdomen[i] == 1) || (isoutfit && outfit_abdomen[i] == 1)))
                            Ladapter.addItem(name_abdomen[i], description_abdomen[i],getResources().getDrawable(R.drawable.abdomen),difficulty_abdomen[i],dumbel_abdomen[i],outfit_abdomen[i],babel_abdomen[i],"복부");
                    }
                    break;
                case "대퇴":
                    for(int i = 0; i < 21; i++){
                        if(!isfilter || isfilter && ((isbabel && babel_legs[i] == 1) || (isdumbel && dumbel_legs[i] == 1) || (isoutfit && outfit_legs[i] == 1)))
                            Ladapter.addItem(name_legs[i], description_legs[i],getResources().getDrawable(R.drawable.leg),difficulty_legs[i],dumbel_legs[i],outfit_legs[i],babel_legs[i],"대퇴");
                    }
                    break;
                case "둔부":
                    for(int i = 0; i < 10; i++){
                        if(!isfilter || isfilter && ((isbabel && babel_buttocks[i] == 1) || (isdumbel && dumbel_buttocks[i] == 1) || (isoutfit && outfit_buttocks[i] == 1)))
                            Ladapter.addItem(name_buttocks[i], description_buttocks[i],getResources().getDrawable(R.drawable.buttocks),difficulty_buttocks[i],dumbel_buttocks[i],outfit_buttocks[i],babel_buttocks[i],"둔부");
                    }
                    break;
                case "가슴":
                    for(int i = 0; i < 15; i++){
                        if(!isfilter || isfilter && ((isbabel && babel_chest[i] == 1) || (isdumbel && dumbel_chest[i] == 1) || (isoutfit && outfit_chest[i] == 1)))
                            Ladapter.addItem(name_chest[i], description_chest[i],getResources().getDrawable(R.drawable.chest),difficulty_chest[i],dumbel_chest[i],outfit_chest[i],babel_chest[i],"가슴");
                    }
                    break;
            }
            list.setAdapter(Ladapter);
            list.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    scroll.requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ExerciseActivity.this, ExerInfoActivity.class);
                    switch(s){
                        case "팔":
                            intent.putExtra("youtubeURL", "https://www.youtube.com/watch?v=3VPNc9HAdmU");
                            intent.putExtra("ExerciseTitle", name_arms[position]);
                            intent.putExtra("ExerciseDescription", description_arms[position]);
                            intent.putExtra("ExerciseInfo", detail_arms[position]);
                            intent.putExtra("ExerciseDifficulty", difficulty_arms[position]);
                            intent.putExtra("ExerciseBabel", babel_arms[position]);
                            intent.putExtra("ExerciseDumbel", dumbel_arms[position]);
                            intent.putExtra("ExerciseOutfit", outfit_arms[position]);
                            intent.putExtra("ExercisePart", s);
                            break;
                        case "어깨":
                            intent.putExtra("youtubeURL", "https://www.youtube.com/watch?v=3VPNc9HAdmU");
                            intent.putExtra("ExerciseTitle", name_shoulders[position]);
                            intent.putExtra("ExerciseDescription", description_shoulders[position]);
                            intent.putExtra("ExerciseInfo", detail_shoulders[position]);
                            intent.putExtra("ExerciseDifficulty", difficulty_shoulders[position]);
                            intent.putExtra("ExerciseBabel", babel_shoulders[position]);
                            intent.putExtra("ExerciseDumbel", dumbel_shoulders[position]);
                            intent.putExtra("ExerciseOutfit", outfit_shoulders[position]);
                            intent.putExtra("ExercisePart", s);
                            break;
                        case "등":
                            intent.putExtra("youtubeURL", "https://www.youtube.com/watch?v=3VPNc9HAdmU");
                            intent.putExtra("ExerciseTitle", name_back[position]);
                            intent.putExtra("ExerciseDescription", description_back[position]);
                            intent.putExtra("ExerciseInfo", detail_back[position]);
                            intent.putExtra("ExerciseDifficulty", difficulty_back[position]);
                            intent.putExtra("ExerciseBabel", babel_back[position]);
                            intent.putExtra("ExerciseDumbel", dumbel_back[position]);
                            intent.putExtra("ExerciseOutfit", outfit_back[position]);
                            intent.putExtra("ExercisePart", s);
                            break;
                        case "가슴":
                            intent.putExtra("youtubeURL", "https://www.youtube.com/watch?v=3VPNc9HAdmU");
                            intent.putExtra("ExerciseTitle", name_chest[position]);
                            intent.putExtra("ExerciseDescription", description_chest[position]);
                            intent.putExtra("ExerciseInfo", detail_chest[position]);
                            intent.putExtra("ExerciseDifficulty", difficulty_chest[position]);
                            intent.putExtra("ExerciseBabel", babel_chest[position]);
                            intent.putExtra("ExerciseDumbel", dumbel_chest[position]);
                            intent.putExtra("ExerciseOutfit", outfit_chest[position]);
                            intent.putExtra("ExercisePart", s);
                            break;
                        case "대퇴":
                            intent.putExtra("youtubeURL", "https://www.youtube.com/watch?v=3VPNc9HAdmU");
                            intent.putExtra("ExerciseTitle", name_legs[position]);
                            intent.putExtra("ExerciseDescription", description_legs[position]);
                            intent.putExtra("ExerciseInfo", detail_legs[position]);
                            intent.putExtra("ExerciseDifficulty", difficulty_legs[position]);
                            intent.putExtra("ExerciseBabel", babel_legs[position]);
                            intent.putExtra("ExerciseDumbel", dumbel_legs[position]);
                            intent.putExtra("ExerciseOutfit", outfit_legs[position]);
                            intent.putExtra("ExercisePart", s);
                            break;
                        case "둔부":
                            intent.putExtra("youtubeURL", "https://www.youtube.com/watch?v=3VPNc9HAdmU");
                            intent.putExtra("ExerciseTitle", name_buttocks[position]);
                            intent.putExtra("ExerciseDescription", description_buttocks[position]);
                            intent.putExtra("ExerciseInfo", detail_buttocks[position]);
                            intent.putExtra("ExerciseDifficulty", difficulty_buttocks[position]);
                            intent.putExtra("ExerciseBabel", babel_buttocks[position]);
                            intent.putExtra("ExerciseDumbel", dumbel_buttocks[position]);
                            intent.putExtra("ExerciseOutfit", outfit_buttocks[position]);
                            intent.putExtra("ExercisePart", s);
                            break;
                        case "복부":
                            intent.putExtra("youtubeURL", "https://www.youtube.com/watch?v=3VPNc9HAdmU");
                            intent.putExtra("ExerciseTitle", name_abdomen[position]);
                            intent.putExtra("ExerciseDescription", description_abdomen[position]);
                            intent.putExtra("ExerciseInfo", detail_abdomen[position]);
                            intent.putExtra("ExerciseDifficulty", difficulty_abdomen[position]);
                            intent.putExtra("ExerciseBabel", babel_abdomen[position]);
                            intent.putExtra("ExerciseDumbel", dumbel_abdomen[position]);
                            intent.putExtra("ExerciseOutfit", outfit_abdomen[position]);
                            intent.putExtra("ExercisePart", s);
                            break;
                    }
                    startActivity(intent);
                }
            });
            pages.add(list);
        }
        viewpager.setAdapter(Padapter);
        final TextView txt_part = findViewById(R.id.txt_part);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String[] part = new String[]{"어깨 운동", "등 운동", "팔 운동", "가슴 운동", "복근 운동", "대퇴 운동", "둔부 운동"};
                txt_part.setText(part[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        springIndicator.setViewPager(viewpager);
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
}