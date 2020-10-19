package com.osam.bodyprotector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class PostActivity extends AppCompatActivity {

    private TextView text_postname;
    private TextView text_postmain;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_layout);

        text_postmain = (TextView)findViewById(R.id.text_postmain);
        text_postname = (TextView)findViewById(R.id.text_postname);
        imgView = (ImageView)findViewById(R.id.image_thumbnail);

        Intent intent = getIntent();

        String title = intent.getExtras().getString("postname");
        String main = intent.getExtras().getString("postmain");
        String uri = intent.getExtras().getString("postimage");

        text_postmain.setText(main);
        text_postname.setText(title);
        Glide.with(getBaseContext()).load(uri).into(imgView);

    }
}