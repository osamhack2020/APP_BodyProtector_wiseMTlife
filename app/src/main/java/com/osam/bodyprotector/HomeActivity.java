package com.osam.bodyprotector;

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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ListView PostList;

    private class ViewHolder {
        public ImageView png;
        public TextView Title;
        public TextView main;
    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<PostData> LPostData = new ArrayList<PostData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        public void addItem(String uri, String title, String main, String uid){
            PostData addInfo = null;
            addInfo = new PostData();
            addInfo.uri = uri;
            addInfo.title = title;
            addInfo.main = main;
            addInfo.uid = uid;

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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.xml.postitem, null);

                holder.png = (ImageView) convertView.findViewById(R.id.post_image);
                holder.Title = (TextView) convertView.findViewById(R.id.text_name);
                holder.main = (TextView) convertView.findViewById(R.id.text_main);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            PostData mData = LPostData.get(position);

            if (mData.uri != null) {
                holder.png.setVisibility(View.VISIBLE);
                Glide.with(getBaseContext()).load(mData.uri).into(holder.png);
            }else{
                holder.png.setVisibility(View.GONE);
            }

            holder.Title.setText(mData.title);
            holder.main.setText(mData.main);

            return convertView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        PostList = (ListView)findViewById(R.id.post_list);
        final ListViewAdapter adapter = new ListViewAdapter(this);
        PostList.setAdapter(adapter);
        adapter.addItem(null,"new post","",null);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("post");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User value = dataSnapshot.getValue(User.class);
                adapter.LPostData.clear();
                adapter.addItem(null,"new post","",null);
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    Post post = fileSnapshot.getValue(Post.class);
                    adapter.addItem(post.image_path,post.postname,post.postmain,post.postuid);
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

}