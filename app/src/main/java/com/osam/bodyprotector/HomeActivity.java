package com.osam.bodyprotector;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import github.chenupt.springindicator.viewpager.ScrollerViewPager;


public class HomeActivity extends AppCompatActivity {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference().child("post");
    private DatabaseReference databaseReference = database.getReference();

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static Activity activity;

    private void savePercent(Date date, RoutineAdapter adapter){
        PercentData data = new PercentData(date);
        int sum = 0;
        int cnt = 0;
        for(MainRoutineData D : adapter.RoutineData){
            data.percent.put(D.exercise.ExerciseName, D.percent);
            data.list.add(D.exercise.ExerciseName);
            data.part.put(D.exercise.ExerciseName, D.exercise.Part);
            cnt++;
            sum += D.percent;
        }
        data.percentSum = sum / cnt;
        String Date = Integer.toString(date.getYear())+ Integer.toString(date.getMonth()) + Integer.toString(date.getDate());
        databaseReference.child("Percent").child(firebaseAuth.getUid()).child(Date).setValue(data);
    }


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


        public void addItem(String uri, String title, String main, String uid, int starCount, HashMap<String, Boolean> stars, User user, String image_name, String post_uid, String Postingtype){
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
            addInfo.PostingType = Postingtype;

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
    private ListView PostList2;
    private ListView PostList3;
    private ListView PostList4;
    private ListView PostList5;
    private TextView txt_date;
    private TextView txt_percent;
    private ScrollView scrollview;
    private Button bt_routineEdit;

    int cur_pagenum = 0;
    boolean has_adapter = false;

    String[] date_array = {"일요일","월요일","화요일","수요일","목요일","금요일","토요일"};

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        activity = HomeActivity.this;

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final Gson gson = new Gson();

        scrollview = (ScrollView)findViewById(R.id.scrollviewHome);
        txt_date = findViewById(R.id.txt_date2);

        viewpager = (ScrollerViewPager)findViewById(R.id.mainroutine_viewpager);
        Vector<View> pages = new Vector<View>();
        ExerciseRoutineSelectActivity.CustomPagerAdapter pagerAdapter = new ExerciseRoutineSelectActivity.CustomPagerAdapter(getBaseContext(), pages);

        String json = pref.getString("ExerciseRoutine", null);
        final Calendar cal = Calendar.getInstance();
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH) + 1;
        final int date = cal.get(Calendar.DATE);
        final int dateofweek = (cal.get(Calendar.DAY_OF_WEEK) - 1) % 7;
        if(json != null){
            MainRoutine routine = gson.fromJson(json, MainRoutine.class);
            if(pref.getString("ExerciseRoutine", null) != null) {
                for (int i = 0; i < 7; i++) {
                    ListView list = new ListView(getBaseContext());
                    final RoutineAdapter adapter = new RoutineAdapter(getBaseContext());
                    list.setAdapter(adapter);
                    for (MainRoutineData E : routine.Daily[(i + dateofweek - 1) % 7]) {
                        adapter.addItem(E.exercise, E.countperset, E.set, E.weight, E.percent);
                    }
                    adapter.notifyDataSetChanged();
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (cur_pagenum == 0) {
                                CustomDialog dl = new CustomDialog(HomeActivity.this);
                                dl.callFunction(adapter.RoutineData.get(position).set, adapter, position, cur_pagenum, txt_percent);
                            } else {
                                Toast.makeText(HomeActivity.this, "운동은 당일에만 진행할 수 있습니다.", Toast.LENGTH_SHORT).show();
                            }
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
                has_adapter = true;
            }

            viewpager.setAdapter(pagerAdapter);
            txt_date.setText(year + "." + month + "." + date + "( " +date_array[dateofweek] + " )");
            txt_percent = findViewById(R.id.txt_routineSum);
            int sum = 0;
            int cnt = 0;
            for(MainRoutineData data2 : adapters[0].RoutineData){
                sum += data2.percent;
                cnt++;
            }
            int percent2 = 0;
            if(sum != 0) percent2 = sum / cnt;
            txt_percent.setText("일일 달성률 " + percent2 + "%");
            viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    cur_pagenum = position;
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
        }
        else{
            txt_date.setText("아직 운동 루틴이 없습니다. 새로 생성해주십시오.");
        }

        bt_routineEdit = findViewById(R.id.bt_routineEdit);

        Button bt_Bluetooth = findViewById(R.id.bt_bluetooth);
        bt_Bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BluetoothActivity.class);
                startActivity(intent);
            }
        });

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
                            routine.Daily.get((i - 1) % 7).add(data.exercise);
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
        PostList2 = (ListView)findViewById(R.id.post_list2);
        PostList3 = (ListView)findViewById(R.id.post_list3);
        PostList4 = (ListView)findViewById(R.id.post_list4);
        PostList5 = (ListView)findViewById(R.id.post_list5);
        final ListViewAdapter adapter = new ListViewAdapter(this);
        final ListViewAdapter adapter2 = new ListViewAdapter(this);
        final ListViewAdapter adapter3 = new ListViewAdapter(this);
        final ListViewAdapter adapter4 = new ListViewAdapter(this);
        final ListViewAdapter adapter5 = new ListViewAdapter(this);
        PostList.setAdapter(adapter);
        PostList2.setAdapter(adapter2);
        PostList3.setAdapter(adapter3);
        PostList4.setAdapter(adapter4);
        PostList5.setAdapter(adapter5);

        PostList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollview.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        PostList2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollview.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        PostList3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollview.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        PostList4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollview.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        PostList5.setOnTouchListener(new View.OnTouchListener() {
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
                adapter2.LPostData.clear();
                adapter3.LPostData.clear();
                adapter4.LPostData.clear();
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    Post post = fileSnapshot.getValue(Post.class);
                    if(post.PostingType.equals("자유게시판")){
                        adapter.addItem(post.image_path,post.postname,post.postmain,post.postuid,post.starCount, (HashMap<String, Boolean>) post.stars,post.user, post.image_name, post.postuid,post.PostingType);
                    }
                    if(post.PostingType.equals("정보게시판")){
                        adapter2.addItem(post.image_path,post.postname,post.postmain,post.postuid,post.starCount, (HashMap<String, Boolean>) post.stars,post.user, post.image_name, post.postuid,post.PostingType);
                    }
                    if(post.PostingType.equals("자랑게시판")){
                        adapter3.addItem(post.image_path,post.postname,post.postmain,post.postuid,post.starCount, (HashMap<String, Boolean>) post.stars,post.user, post.image_name, post.postuid,post.PostingType);
                    }
                    if(post.PostingType.equals("강의게시판")){
                        adapter4.addItem(post.image_path,post.postname,post.postmain,post.postuid,post.starCount, (HashMap<String, Boolean>) post.stars,post.user, post.image_name, post.postuid,post.PostingType);
                    }
                }
                adapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
                adapter3.notifyDataSetChanged();
                adapter4.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("HI", "값을 읽는데 실패했습니다.", error.toException());
            }
        });

        myRef.orderByChild("starcount").limitToFirst(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter5.LPostData.clear();
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    Post post = fileSnapshot.getValue(Post.class);
                    adapter5.addItem(post.image_path,post.postname,post.postmain,post.postuid,post.starCount, (HashMap<String, Boolean>) post.stars,post.user, post.image_name, post.postuid,post.PostingType);
                }
                adapter5.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("HI", "값을 읽는데 실패했습니다.", error.toException());
            }
        });

        Button bt_chart = findViewById(R.id.bt_chart);
        bt_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ChartActivity.class);
                startActivity(intent);
            }
        });

        ImageView img_plus = findViewById(R.id.img_plus);
        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, WriteActivity.class);
                startActivity(intent);
            }
        });
        ImageView img_plus2 = findViewById(R.id.img_plus2);
        img_plus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, WriteActivity.class);
                startActivity(intent);
            }
        });
        ImageView img_plus3 = findViewById(R.id.img_plus3);
        img_plus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, WriteActivity.class);
                startActivity(intent);
            }
        });
        ImageView img_plus4 = findViewById(R.id.img_plus4);
        img_plus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, WriteActivity.class);
                startActivity(intent);
            }
        });
        ImageView img_plus5 = findViewById(R.id.img_plus5);
        img_plus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, WriteActivity.class);
                startActivity(intent);
            }
        });

        PostList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PostData data = adapter.LPostData.get(position);
                Intent intent = new Intent(HomeActivity.this, PostActivity.class);
                intent.putExtra("postname", data.title);
                intent.putExtra("postmain", data.main);
                intent.putExtra("postimage", data.uri);
                intent.putExtra("postuser", data.user.uuid);
                intent.putExtra("imagename", data.image_name);
                intent.putExtra("postuid", data.post_uid);
                intent.putExtra("postingtype", data.PostingType);
                startActivity(intent);
            }
        });
        PostList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PostData data = adapter2.LPostData.get(position);
                Intent intent = new Intent(HomeActivity.this, PostActivity.class);
                intent.putExtra("postname", data.title);
                intent.putExtra("postmain", data.main);
                intent.putExtra("postimage", data.uri);
                intent.putExtra("postuser", data.user.uuid);
                intent.putExtra("imagename", data.image_name);
                intent.putExtra("postuid", data.post_uid);
                intent.putExtra("postingtype", data.PostingType);
                startActivity(intent);
            }
        });
        PostList3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PostData data = adapter3.LPostData.get(position);
                Intent intent = new Intent(HomeActivity.this, PostActivity.class);
                intent.putExtra("postname", data.title);
                intent.putExtra("postmain", data.main);
                intent.putExtra("postimage", data.uri);
                intent.putExtra("postuser", data.user.uuid);
                intent.putExtra("imagename", data.image_name);
                intent.putExtra("postuid", data.post_uid);
                intent.putExtra("postingtype", data.PostingType);
                startActivity(intent);
            }
        });
        PostList4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PostData data = adapter4.LPostData.get(position);
                Intent intent = new Intent(HomeActivity.this, PostActivity.class);
                intent.putExtra("postname", data.title);
                intent.putExtra("postmain", data.main);
                intent.putExtra("postimage", data.uri);
                intent.putExtra("postuser", data.user.uuid);
                intent.putExtra("imagename", data.image_name);
                intent.putExtra("postuid", data.post_uid);
                intent.putExtra("postingtype", data.PostingType);
                startActivity(intent);
            }
        });
        PostList5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PostData data = adapter4.LPostData.get(position);
                Intent intent = new Intent(HomeActivity.this, PostActivity.class);
                intent.putExtra("postname", data.title);
                intent.putExtra("postmain", data.main);
                intent.putExtra("postimage", data.uri);
                intent.putExtra("postuser", data.user.uuid);
                intent.putExtra("imagename", data.image_name);
                intent.putExtra("postuid", data.post_uid);
                intent.putExtra("postingtype", data.PostingType);
                startActivity(intent);
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
    }


    private class CustomDialog {

        private Context context;

        public CustomDialog(Context context) {
            this.context = context;
        }

        public void callFunction(final int set, final RoutineAdapter adapter, final int position, final int cur_num, final TextView txt_p) {
            final Dialog dlg = new Dialog(context);

            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dlg.setContentView(R.layout.custom_dialog_home);
            dlg.show();

            final Spinner spinner_percent = dlg.findViewById(R.id.spinner_percent);
            final Button okButton = dlg.findViewById(R.id.okButton_home);
            final Button cancelButton = dlg.findViewById(R.id.cancelButton_home);
            final TextView txt_percent = dlg.findViewById(R.id.txt_percent);
            Integer[] items = new Integer[set + 1];
            for(int i = 0; i <= set; i++){
                items[i] = i;
            }
            ArrayAdapter<Integer> date_adapter = new ArrayAdapter<Integer>(HomeActivity.this, android.R.layout.simple_spinner_item, items);
            spinner_percent.setAdapter(date_adapter);
            spinner_percent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    final int count = Integer.parseInt(spinner_percent.getSelectedItem().toString());
                    int percent = 0;
                    if(count != 0) percent = count * 100 / set;
                    txt_percent.setText("현재 진행 정도 (" + count + "/"+ set + ") " + percent + "%");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int percent = Integer.parseInt(spinner_percent.getSelectedItem().toString());
                    Calendar cal = Calendar.getInstance();
                    final int dateofweek = cal.get(Calendar.DAY_OF_WEEK);

                    MainRoutineData data = adapter.RoutineData.get(position);
                    data.percent = percent * 100 / set;

                    adapter.RoutineData.set(position, data);
                    Toast.makeText(context, "성공적으로 수정했습니다.", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    for(RoutineAdapter adapter : adapters) {
                        adapter.notifyDataSetChanged();
                    }

                    MainRoutine routine = new MainRoutine();
                    int i = 0;
                    for(RoutineAdapter adapter : adapters){
                        for(MainRoutineData _data : adapter.RoutineData){
                            MainRoutineData Routinedata = new MainRoutineData(_data.exercise, _data.countperset, _data.set, _data.weight, _data.percent);
                            routine.Daily[(i + dateofweek - 2) % 7].add(Routinedata);
                        }
                        i++;
                    }
                    Gson gson = new Gson();
                    String json = gson.toJson(routine);
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                    SharedPreferences.Editor editor = pref.edit();
                    savePercent(new Date(System.currentTimeMillis()),adapter);

                    editor.putString("ExerciseRoutine", json);
                    editor.apply();
                    int sum = 0;
                    int cnt = 0;
                    for(MainRoutineData data2 : adapters[cur_num].RoutineData){
                        sum += data2.percent;
                        cnt++;
                    }
                    Log.d("sum", String.valueOf(cur_pagenum));
                    int percent2 = 0;
                    if(sum != 0) percent2 = sum / cnt;
                    txt_p.setText("일일 달성률 " + percent2 + "%");

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