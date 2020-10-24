package com.osam.bodyprotector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    private ListView PostList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button bt_exercise = (Button)findViewById(R.id.bt_exercise);
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

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User value = dataSnapshot.getValue(User.class);
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