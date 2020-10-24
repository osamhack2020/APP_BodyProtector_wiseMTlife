package com.osam.bodyprotector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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

        Intent intent = getIntent();

        final String youtubeURL = intent.getExtras().getString("youtubeURL",null);
        final String ExerciseTitle = intent.getExtras().getString("ExerciseTitle",null);
        final String ExerciseDescription = intent.getExtras().getString("ExerciseDescription",null);
        final String ExerciseInfo = intent.getExtras().getString("ExerciseInfo",null);
        final int ExerciseDiffiuclty = intent.getExtras().getInt("ExerciseDifficulty",0);
        final int ExerciseBabel = intent.getExtras().getInt("ExerciseBabel",0);
        final int ExerciseDumbel = intent.getExtras().getInt("ExerciseDumbel",0);
        final int ExerciseOutfit = intent.getExtras().getInt("ExerciseOutfit",0);

        String _id = youtubeURL.substring(youtubeURL.lastIndexOf("=")+1); //맨마지막 '/'뒤에 id가있으므로 그것만 파싱해줌
        String __id = youtubeURL.substring(youtubeURL.lastIndexOf("/")+1); //맨마지막 '/'뒤에 id가있으므로 그것만 파싱해줌
        String id = _id;

        if(__id.length()<_id.length())
            id=__id;

        String yURL ="https://img.youtube.com/vi/"+ id+ "/" + "default.jpg"; //유튜브 썸네일 불러오는 방법

        Glide.with(this).load(yURL).into(VyoutubeThumbnail);

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
    }
}