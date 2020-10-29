package com.osam.bodyprotector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import github.chenupt.springindicator.viewpager.ScrollerViewPager;


public class HomeActivity extends AppCompatActivity {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference().child("post");

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private class ViewHolder {
        public ImageView png;
        public ImageView StarBt;
        public TextView Title;
        public TextView main;
        public TextView StarCount;
    }

    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(firebaseAuth.getUid())) {
                    p.starCount = p.starCount - 1;
                    p.stars.remove(firebaseAuth.getUid());
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;
                    p.stars.put(firebaseAuth.getUid(), true);
                }

                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed,
                                   DataSnapshot currentData) {
                // Transaction completed
                Log.d("hi", "postTransaction:onComplete:" + databaseError);
            }
        });
    }


    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<PostData> LPostData = new ArrayList<PostData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }


        public void addItem(String uri, String title, String main, String uid, int starCount, HashMap<String, Boolean> stars, User user, String image_name, String post_uid){
            PostData addInfo = null;
            addInfo = new PostData();
            addInfo.uri = uri;
            addInfo.title = title;
            addInfo.main = main;
            addInfo.uid = uid;
            addInfo.starCount = starCount;
            addInfo.stars = stars;
            addInfo.user = user;
            addInfo.image_name = image_name;
            addInfo.post_uid = post_uid;

            LPostData.add(addInfo);
        }


        @Override
        public int getCount() {
            return LPostData.size();
        }

        @Override
        public Object getItem(int position) {
            return LPostData.get(position);
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
                convertView = inflater.inflate(R.xml.postitem, null);

                holder.png = (ImageView) convertView.findViewById(R.id.post_image);
                holder.StarBt = (ImageView) convertView.findViewById(R.id.image_starBt);
                holder.Title = (TextView) convertView.findViewById(R.id.text_name);
                holder.main = (TextView) convertView.findViewById(R.id.text_main);
                holder.StarCount = (TextView) convertView.findViewById(R.id.text_StarCount);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            final PostData mData = LPostData.get(position);

            if(mData.stars.containsKey(firebaseAuth.getUid())){
                holder.StarBt.setImageResource(R.drawable.baseline_favorite_black_18dp);
            }

            if (mData.uri != null) {
                holder.png.setVisibility(View.VISIBLE);
                Glide.with(getBaseContext()).load(mData.uri).into(holder.png);
            }else{
                holder.png.setVisibility(View.GONE);
            }
            holder.StarBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStarClicked(myRef.child(mData.uid));
                    if(mData.stars.containsKey(firebaseAuth.getUid())){
                        holder.StarBt.setImageResource(R.drawable.baseline_favorite_border_black_18dp);
                    }
                    else{
                        holder.StarBt.setImageResource(R.drawable.baseline_favorite_black_18dp);
                    }
                }
            });

            holder.StarCount.setText(Integer.toString(mData.starCount));
            holder.Title.setText(mData.title);
            holder.main.setText(mData.main);

            return convertView;
        }
    }

    private static class RoutineAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<MainRoutineData> RoutineData = new ArrayList<MainRoutineData>();

        public RoutineAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }


        private class ViewHolder {
            public ImageView ExerImg;
            public TextView ExerciseName;
            public TextView ExerciseCount;
            public TextView ExerciseDifficulty;
            public TextView ExercisePercent;
        }

        public void addItem(Exercise exercise, int countperset, int set, int weight, int percent){
            MainRoutineData addInfo = null;
            addInfo = new MainRoutineData(exercise, countperset, set, weight, percent);

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
                convertView = inflater.inflate(R.layout.mainroutineitem, null);

                holder.ExerImg = convertView.findViewById(R.id.mainroutine_img);
                holder.ExerciseName = convertView.findViewById(R.id.mainroutine_title);
                holder.ExerciseCount = convertView.findViewById(R.id.mainroutine_count);
                holder.ExerciseDifficulty = convertView.findViewById(R.id.mainroutine_difficulty);
                holder.ExercisePercent = convertView.findViewById(R.id.mainroutine_percent);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            final MainRoutineData mData = RoutineData.get(position);

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
            holder.ExercisePercent.setText("현재 " + mData.percent + "% 완료");

            if(mData.exercise.ExerciseName.equals("휴식")){
                holder.ExerciseCount.setText("근육 휴식 기간");
                holder.ExerciseDifficulty.setText("");
                holder.ExercisePercent.setText("현재 100% 완료");
            }

            return convertView;
        }
    }

    final RoutineAdapter[] adapters = new RoutineAdapter[7];

    private ScrollerViewPager viewpager;
    private ListView PostList;
    private TextView txt_date;
    private TextView txt_percent;
    private ScrollView scrollview;
    private Button bt_routineEdit;

    String[] date_array = {"월요일","화요일","수요일","목요일","금요일","토요일","일요일"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        final Gson gson = new Gson();

        scrollview = (ScrollView)findViewById(R.id.scrollviewHome);

        viewpager = (ScrollerViewPager)findViewById(R.id.mainroutine_viewpager);
        ExerciseRoutine routine = gson.fromJson(pref.getString("ExerciseRoutine", null), ExerciseRoutine.class);
        Vector<View> pages = new Vector<View>();

        ExerciseRoutineSelectActivity.CustomPagerAdapter pagerAdapter = new ExerciseRoutineSelectActivity.CustomPagerAdapter(getBaseContext(), pages);

        Calendar cal = Calendar.getInstance();
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH) + 1;
        final int date = cal.get(Calendar.DATE);
        final int dateofweek = (cal.get(Calendar.DAY_OF_WEEK) - 1) % 7;

        for(int i = 0; i < 7; i++){
            ListView list = new ListView(getBaseContext());
            RoutineAdapter adapter = new RoutineAdapter(getBaseContext());
            list.setAdapter(adapter);
            for(Exercise E : routine.Daily.get((i + dateofweek) % 7)){
                if(E.ExerciseName.equals("휴식")){
                    adapter.addItem(E, routine.count, routine.set, routine.weight, 100);
                } else{
                    adapter.addItem(E, routine.count, routine.set, routine.weight, 0);
                }
            }
            adapter.notifyDataSetChanged();
            final int finalI = i;
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                }
            });

            list.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    scrollview.requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
            adapters[i] = adapter;
            pages.add(list);
        }
        viewpager.setAdapter(pagerAdapter);
        txt_date = findViewById(R.id.txt_date2);
        txt_date.setText(year + "." + month + "." + date + "( " +date_array[dateofweek] + " )");
        txt_percent = findViewById(R.id.txt_routineSum);
        int sum = 0;
        int cnt = 0;
        for(MainRoutineData data : adapters[dateofweek].RoutineData){
            sum += data.percent;
            cnt++;
        }
        int percent = 0;
        if(sum != 0) percent = sum / cnt;
        txt_percent.setText("일일 달성률 " + percent + "%");
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int sum = 0;
                int cnt = 0;
                for(MainRoutineData data : adapters[position].RoutineData){
                    sum += data.percent;
                    cnt++;
                }
                int percent = 0;
                if(sum != 0) percent = sum / cnt;
                txt_percent.setText("일일 달성률 " + percent + "%");
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, position);
                int _year = cal.get(Calendar.YEAR);
                int _month = cal.get(Calendar.MONTH) + 1;
                int _date = cal.get(Calendar.DATE);
                int _dateofweek = (cal.get(Calendar.DAY_OF_WEEK) - 1) % 7;
                txt_date.setText(_year + "." + _month + "." + _date + "( " +date_array[_dateofweek] + " )");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bt_routineEdit = findViewById(R.id.bt_routineEdit);

        bt_routineEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listjson = pref.getString("ExerciseList", null);
                if(listjson == null){
                    Toast.makeText(HomeActivity.this, "수정할 루틴이 없습니다. 새로 만들어 주십시오.", Toast.LENGTH_SHORT).show();
                }
                else {
                    ExerciseRoutine routine = new ExerciseRoutine();
                    int i = dateofweek;
                    for(RoutineAdapter adapter : adapters){
                        for(MainRoutineData data : adapter.RoutineData){
                            routine.Daily.get(i % 7).add(data.exercise);
                        }
                        i++;
                    }
                    String json = gson.toJson(routine);
                    Intent intent = new Intent(HomeActivity.this, ExerciseRoutineSelectActivity.class);
                    intent.putExtra("json", listjson);
                    intent.putExtra("routine", json);
                    startActivity(intent);
                }
            }
        });
        Button bt_exercise = (Button)findViewById(R.id.bt_exercise);
        Button bt_routine = (Button)findViewById(R.id.bt_routine);
        bt_routine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ExerciseRoutineActivity.class);
                startActivity(intent);
            }
        });

        bt_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ExerciseActivity.class);
                startActivity(intent);
            }
        });
        PostList = (ListView)findViewById(R.id.post_list);
        final ListViewAdapter adapter = new ListViewAdapter(this);
        PostList.setAdapter(adapter);
        adapter.addItem(null,"new post","",null,0, new HashMap<String, Boolean>(), new User(), null, null);


        PostList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollview.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.LPostData.clear();
                adapter.addItem(null,"new post","",null,0, new HashMap<String, Boolean>(),new User(), null, null);
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    Post post = fileSnapshot.getValue(Post.class);
                    adapter.addItem(post.image_path,post.postname,post.postmain,post.postuid,post.starCount, (HashMap<String, Boolean>) post.stars,post.user, post.image_name, post.postuid);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("HI", "값을 읽는데 실패했습니다.", error.toException());
            }
        });


        PostList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Intent intent = new Intent(HomeActivity.this, WriteActivity.class);
                    startActivity(intent);
                }
                else{
                    PostData data = adapter.LPostData.get(position);
                    Intent intent = new Intent(HomeActivity.this, PostActivity.class);
                    intent.putExtra("postname", data.title);
                    intent.putExtra("postmain", data.main);
                    intent.putExtra("postimage", data.uri);
                    intent.putExtra("postuser", data.user.uuid);
                    intent.putExtra("imagename", data.image_name);
                    intent.putExtra("postuid", data.post_uid);
                    startActivity(intent);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for(RoutineAdapter adapter : adapters){
            adapter.notifyDataSetChanged();
        }

    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.xml.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_setting:
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
        }
        return true;
    }
}