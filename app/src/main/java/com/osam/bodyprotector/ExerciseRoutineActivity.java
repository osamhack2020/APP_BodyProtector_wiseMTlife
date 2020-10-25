package com.osam.bodyprotector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ExerciseRoutineActivity extends AppCompatActivity implements RoutineOptionFragment.CallbackListner{
    String[] messages = {"체력단련실에 덤벨이 있습니까?",
            "체력단련실에 바벨이 있습니까?",
            "체력단련실에 전문 운동기구가 있습니까?",
            "운동 중량을 자동으로 설정하시겠습니까?",
            "근육 휴식기간을 자동으로 설정하시겠습니까?",
            "운동들의 난이도를 자동으로 설정하시겠습니까?",
            "구독한 운동들을 우선 배치하시겠습니까?",
            "루틴을 운동 부위를 기준으로 균등 분배하시겠습니까?"};
    String[] submessages = {"덤벨을 사용하는 운동을 표시합니다.",
            "바벨을 사용하는 운동을 표시합니다.",
            "전문 운동기구를 사용하는 운동을 표시합니다.",
            "BMI 지수를 기준으로 추천 중량을 표시합니다.",
            "일요일을 근육 휴식기간으로 설정합니다.",
            "사용자 설정 정보를 활용하여 운동을 배치합니다.",
            "운동 선택시 구독 여부를 기준으로 배치합니다.",
            "일일 운동 계획을 부위별로 고르게 배치합니다."};
    boolean[] options = new boolean[8];
    int count = 8;

    public void CreateAutoRoutine(boolean[] options){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isDumbel", options[0]);
        editor.putBoolean("isBabel", options[1]);
        editor.putBoolean("isOutfit", options[2]);
        editor.commit();

        int SettingDifficulty = 2;
        String df = pref.getString("difficulty", null);
        if(df != null){
            switch(df){
                case "난이도 하":
                    SettingDifficulty = 0;
                    break;
                case "난이도 중":
                    SettingDifficulty = 1;
                    break;
                case "난이도 상":
                    SettingDifficulty = 2;
                    break;
            }
        }
        boolean isDumbel = options[0];
        boolean isBabel = options[1];
        boolean isOutfit = options[2];

        final String[] name = getResources().getStringArray(R.array.ExerciseName_Arms);
        final int[] babel = getResources().getIntArray(R.array.Exercisebabel_Arms);
        final int[] dumbel = getResources().getIntArray(R.array.Exercisedumbel_Arms);
        final int[] outfit = getResources().getIntArray(R.array.Exerciseoutfit_Arms);
        final int[] difficulty = getResources().getIntArray(R.array.Exercisedifficulty_Arms);

        ArrayList<Exercise> ExerciseList = new ArrayList<Exercise>();
        if(!options[6]) {
            for (int i = 0; i < 17; i++) {
                if ((options[5] && (difficulty[i] <= SettingDifficulty) || !options[5])) {
                    Exercise exercise = new Exercise(name[i], "팔", difficulty[i], dumbel[i] == 1, babel[i] == 1, outfit[i] == 1);
                    ExerciseList.add(exercise);
                }
            }
        }else {
            String json = pref.getString("joinlist",null);
            Gson gson = new Gson();
            ExerJoinList list = gson.fromJson(json, ExerJoinList.class);
            for(Exercise E : list.list){
                if((options[5] && (E.Difficulty <= SettingDifficulty) || !options[5])){
                    ExerciseList.add(E);
                }
            }
        }
        for(Exercise E : ExerciseList){
            if((!isDumbel && E.isDumbel) || (!isBabel && E.isBabel) || (!isOutfit && E.isOutfit)){
                ExerciseList.remove(E);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_routine);

        RoutineOptionFragment ROfrag = new RoutineOptionFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.screen_layout,ROfrag).commit();

        Bundle args = new Bundle();
        args.putString("message", messages[0]);
        args.putString("submessage", submessages[0]);
        args.putInt("count", 0);

        ROfrag.setArguments(args);
    }

    @Override
    public void onRecieveData(boolean bool, int cnt) {
        if(count > cnt) {
            RoutineOptionFragment ROfrag = new RoutineOptionFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.screen_layout, ROfrag).commit();

            options[cnt] = bool;
            Bundle args = new Bundle();
            args.putString("message", messages[cnt]);
            args.putString("submessage", submessages[cnt]);
            args.putInt("count", cnt);

            ROfrag.setArguments(args);
        } else{
            CreateAutoRoutine(options);
        }
    }
}