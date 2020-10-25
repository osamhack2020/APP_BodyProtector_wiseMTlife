package com.osam.bodyprotector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

public class ExerInfoActivity extends AppCompatActivity {

    private ImageView VyoutubeThumbnail;
    private ImageView VExerciseImg;
    private ImageView VCheckBabel;
    private ImageView VCheckDumbel;
    private ImageView VCheckOutfit;
    private TextView VExerciseTitle;
    private TextView VExerciseDescription;
    private TextView VExerciseInfo;
    private TextView VExerciseDiffiuclty;
    private Button bt_join;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exer_info);

        VyoutubeThumbnail = (ImageView) findViewById(R.id.img_youtubeGo);
        VExerciseImg = (ImageView)findViewById(R.id.img_ExerInfo);
        VCheckBabel = (ImageView)findViewById(R.id.Infocheck_babel);
        VCheckDumbel = (ImageView)findViewById(R.id.Infocheck_dumbel);
        VCheckOutfit = (ImageView)findViewById(R.id.Infocheck_outfit);
        VExerciseTitle = (TextView)findViewById(R.id.txt_ExerInfoName);
        VExerciseDescription = (TextView)findViewById(R.id.txt_ExerInfoDescription);
        VExerciseInfo = (TextView)findViewById(R.id.txt_ExerInfo);
        VExerciseDiffiuclty = (TextView)findViewById(R.id.txt_exerInfodifficulty);
        bt_join = (Button)findViewById(R.id.bt_ExerJoin);

        Intent intent = getIntent();

        final String youtubeURL = intent.getExtras().getString("youtubeURL",null);
        final String ExerciseTitle = intent.getExtras().getString("ExerciseTitle",null);
        final String ExerciseDescription = intent.getExtras().getString("ExerciseDescription",null);
        final String ExerciseInfo = intent.getExtras().getString("ExerciseInfo",null);
        final String ExercisePart = intent.getExtras().getString("ExercisePart",null);
        final int ExerciseDiffiuclty = intent.getExtras().getInt("ExerciseDifficulty",0);
        final int ExerciseBabel = intent.getExtras().getInt("ExerciseBabel",0);
        final int ExerciseDumbel = intent.getExtras().getInt("ExerciseDumbel",0);
        final int ExerciseOutfit = intent.getExtras().getInt("ExerciseOutfit",0);

        String _id = youtubeURL.substring(youtubeURL.lastIndexOf("=")+1);
        String __id = youtubeURL.substring(youtubeURL.lastIndexOf("/")+1);
        String id = _id;

        if(__id.length()<_id.length())
            id=__id;

        String yURL ="https://img.youtube.com/vi/"+ id+ "/" + "maxresdefault.jpg";

        Glide.with(this).load(yURL).into(VyoutubeThumbnail);

        VyoutubeThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeURL));
                startActivity(intent);
            }
        });

        VExerciseTitle.setText(ExerciseTitle);
        VExerciseDescription.setText(ExerciseDescription);
        VExerciseInfo.setText(ExerciseInfo);

        switch(ExerciseDiffiuclty){
            case 0:
                VExerciseDiffiuclty.setText("난이도 하");
                break;
            case 1:
                VExerciseDiffiuclty.setText("난이도 중");
                break;
            case 2:
                VExerciseDiffiuclty.setText("난이도 상");
                break;
        }

        if(ExerciseDumbel == 1){
            VCheckDumbel.setImageResource(android.R.drawable.checkbox_on_background);
        } else{
            VCheckDumbel.setImageResource(android.R.drawable.checkbox_off_background);
        }
        if(ExerciseBabel == 1){
            VCheckBabel.setImageResource(android.R.drawable.checkbox_on_background);
        } else{
            VCheckBabel.setImageResource(android.R.drawable.checkbox_off_background);
        }
        if(ExerciseOutfit == 1){
            VCheckOutfit.setImageResource(android.R.drawable.checkbox_on_background);
        } else{
            VCheckOutfit.setImageResource(android.R.drawable.checkbox_off_background);
        }
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final SharedPreferences.Editor edit = pref.edit();
        final Exercise exercise = new Exercise(ExerciseTitle,ExercisePart,ExerciseDiffiuclty,ExerciseDumbel == 1, ExerciseBabel == 1, ExerciseOutfit == 1);
        final Gson gson = new Gson();
        String value = pref.getString("joinlist", null);
        final ExerJoinList list;
        bt_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String value = pref.getString("joinlist", null);
                ExerJoinList list;

                if (value != null) {
                    list = gson.fromJson(value, ExerJoinList.class);
                    list.list.add(exercise);
                    edit.putString("joinlist", gson.toJson(list));
                } else {
                    ExerJoinList joinlist = new ExerJoinList();
                    joinlist.list.add(exercise);
                    edit.putString("joinlist", gson.toJson(joinlist));
                }
                edit.commit();
                Toast.makeText(ExerInfoActivity.this, ExerciseTitle + " 운동을 구독했습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        if(value != null) {
            list = gson.fromJson(value, ExerJoinList.class);
            if(list.list.contains(exercise)){
                bt_join.setText("구독 취소하기");
                bt_join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.list.remove(exercise);
                        edit.putString("joinlist", gson.toJson(list));
                        edit.commit();
                        Toast.makeText(ExerInfoActivity.this, ExerciseTitle + " 운동을 구독 취소하였습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }

    }
}