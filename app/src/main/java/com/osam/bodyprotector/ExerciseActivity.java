package com.osam.bodyprotector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.ListFragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Vector;

import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;


public class ExerciseActivity extends AppCompatActivity {

    String[] partname = {"어깨","등","팔","가슴","복근","하체","승모근"};


    class ViewAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Fragment> fragments = new ArrayList<>();

        public ViewAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        void addItem(Fragment fragment){
            fragments.add(fragment);
        }
    }

    private class ListViewAdapter extends BaseAdapter {
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

                holder.ExerImg = (ImageView) convertView.findViewById(R.id.img_exercise);
                holder.check_babel = (ImageView) convertView.findViewById(R.id.check_babel);
                holder.check_dumbel = (ImageView) convertView.findViewById(R.id.check_dumbel);
                holder.check_outfit = (ImageView) convertView.findViewById(R.id.check_outfit);
                holder.ExerciseName = (TextView) convertView.findViewById(R.id.txt_exercisename);
                holder.ExerciseDescription = (TextView) convertView.findViewById(R.id.txt_exercisedescription);
                holder.ExerciseDifficulty = (TextView) convertView.findViewById(R.id.txt_exerInfodifficulty);

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


    private ScrollerViewPager viewpager;
    private SpringIndicator springIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        final ScrollView scroll = (ScrollView)findViewById(R.id.scroll);
        viewpager = (ScrollerViewPager)findViewById(R.id.viewpager);
        springIndicator = (SpringIndicator)findViewById(R.id.indicator);
        ViewAdapter adapter = new ViewAdapter(getSupportFragmentManager());
        viewpager.fixScrollSpeed();
        Vector<View> pages = new Vector<View>();
        CustomPagerAdapter Padapter = new CustomPagerAdapter(getBaseContext(),pages);
        for(String s : partname){
            final String[] name = getResources().getStringArray(R.array.ExerciseName_Arms);
            final String[] description = getResources().getStringArray(R.array.ExerciseDescription_Arms);
            final int[] babel = getResources().getIntArray(R.array.Exercisebabel_Arms);
            final int[] dumbel = getResources().getIntArray(R.array.Exercisedumbel_Arms);
            final int[] outfit = getResources().getIntArray(R.array.Exerciseoutfit_Arms);
            final int[] difficulty = getResources().getIntArray(R.array.Exercisedifficulty_Arms);

            ListView list = new ListView(getBaseContext());
            ListViewAdapter Ladapter = new ListViewAdapter(this);
            for(int i = 0; i < 17; i++){
                Ladapter.addItem(name[i], description[i],getResources().getDrawable(R.drawable.ic_launcher_background),difficulty[i],dumbel[i],outfit[i],babel[i],"arms");
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
                    intent.putExtra("youtubeURL", "https://www.youtube.com/watch?v=3VPNc9HAdmU");
                    intent.putExtra("ExerciseTitle", name[position]);
                    intent.putExtra("ExerciseDescription", description[position]);
                    intent.putExtra("ExerciseInfo", "설명을 쓰자~~");
                    intent.putExtra("ExerciseDifficulty", difficulty[position]);
                    intent.putExtra("ExerciseBabel", babel[position]);
                    intent.putExtra("ExerciseDumbel", dumbel[position]);
                    intent.putExtra("ExerciseOutfit", outfit[position]);
                    startActivity(intent);
                }
            });
            pages.add(list);
        }
        viewpager.setAdapter(Padapter);

        springIndicator.setViewPager(viewpager);
    }


    public class CustomPagerAdapter extends PagerAdapter {

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