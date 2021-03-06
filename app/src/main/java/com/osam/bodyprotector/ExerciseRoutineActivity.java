package com.osam.bodyprotector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class ExerciseRoutineActivity extends AppCompatActivity implements RoutineOptionFragment.CallbackListner{
    String[] messages = {"체력단련실에 덤벨이 있습니까?", //0
            "체력단련실에 바벨이 있습니까?", //1
            "체력단련실에 전문 운동기구가 있습니까?", //2
            "운동 중량을 자동으로 설정하시겠습니까?", //3
            "근육 휴식기간을 자동으로 설정하시겠습니까?", //4
            "운동들의 난이도를 자동으로 설정하시겠습니까?", //5
            "구독한 운동들을 우선 배치하시겠습니까?", //6
            "루틴을 운동 부위를 기준으로 균등 분배하시겠습니까?"}; //7
    String[] submessages = {"덤벨을 사용하는 운동을 표시합니다.",
            "바벨을 사용하는 운동을 표시합니다.",
            "전문 운동기구를 사용하는 운동을 표시합니다.",
            "BMI 지수를 기준으로 추천 중량을 표시합니다.",
            "일요일을 근육 휴식기간으로 설정합니다.",
            "사용자 설정 정보를 활용하여 운동을 배치합니다.",
            "운동 선택시 구독 여부를 기준으로 배치합니다.",
            "일일 운동 계획을 부위별로 고르게 배치합니다."};
    boolean[] bool_list = new boolean[9];
    int count = 8;

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
    public void onRecieveData(boolean b, int cnt) {
        if(count > cnt) {
            RoutineOptionFragment ROfrag = new RoutineOptionFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.screen_layout, ROfrag).commit();

            bool_list[cnt - 1] = b;
            Bundle args = new Bundle();
            args.putString("message", messages[cnt]);
            args.putString("submessage", submessages[cnt]);
            args.putInt("count", cnt);

            ROfrag.setArguments(args);

        } else{
            final String[] name_arms = getResources().getStringArray(R.array.ExerciseName_Arms);
            final int[] babel_arms = getResources().getIntArray(R.array.Exercisebabel_Arms);
            final int[] dumbel_arms = getResources().getIntArray(R.array.Exercisedumbel_Arms);
            final int[] outfit_arms = getResources().getIntArray(R.array.Exerciseoutfit_Arms);
            final int[] difficulty_arms = getResources().getIntArray(R.array.Exercisedifficulty_Arms);

            final String[] name_abdomen = getResources().getStringArray(R.array.ExerciseName_Abdomen);
            final int[] babel_abdomen = getResources().getIntArray(R.array.Exercisebabel_Abdomen);
            final int[] dumbel_abdomen = getResources().getIntArray(R.array.Exercisedumbel_Abdomen);
            final int[] outfit_abdomen = getResources().getIntArray(R.array.Exerciseoutfit_Abdomen);
            final int[] difficulty_abdomen = getResources().getIntArray(R.array.Exercisedifficulty_Abdomen);

            final String[] name_back = getResources().getStringArray(R.array.ExerciseName_Back);
            final int[] babel_back = getResources().getIntArray(R.array.Exercisebabel_Back);
            final int[] dumbel_back = getResources().getIntArray(R.array.Exercisedumbel_Back);
            final int[] outfit_back = getResources().getIntArray(R.array.Exerciseoutfit_Back);
            final int[] difficulty_back = getResources().getIntArray(R.array.Exercisedifficulty_Back);

            final String[] name_buttocks = getResources().getStringArray(R.array.ExerciseName_Buttocks);
            final int[] babel_buttocks = getResources().getIntArray(R.array.Exercisebabel_Buttocks);
            final int[] dumbel_buttocks = getResources().getIntArray(R.array.Exercisedumbel_Buttocks);
            final int[] outfit_buttocks = getResources().getIntArray(R.array.Exerciseoutfit_Buttocks);
            final int[] difficulty_buttocks = getResources().getIntArray(R.array.Exercisedifficulty_Buttocks);

            final String[] name_chest = getResources().getStringArray(R.array.ExerciseName_Chest);
            final int[] babel_chest = getResources().getIntArray(R.array.Exercisebabel_Chest);
            final int[] dumbel_chest = getResources().getIntArray(R.array.Exercisedumbel_Chest);
            final int[] outfit_chest = getResources().getIntArray(R.array.Exerciseoutfit_Chest);
            final int[] difficulty_chest = getResources().getIntArray(R.array.Exercisedifficulty_Chest);

            final String[] name_legs = getResources().getStringArray(R.array.ExerciseName_Legs);
            final int[] babel_legs = getResources().getIntArray(R.array.Exercisebabel_Legs);
            final int[] dumbel_legs = getResources().getIntArray(R.array.Exercisedumbel_Legs);
            final int[] outfit_legs = getResources().getIntArray(R.array.Exerciseoutfit_Legs);
            final int[] difficulty_legs = getResources().getIntArray(R.array.Exercisedifficulty_Legs);

            final String[] name_shoulders = getResources().getStringArray(R.array.ExerciseName_Shoulder);
            final int[] babel_shoulders = getResources().getIntArray(R.array.Exercisebabel_Shoulder);
            final int[] dumbel_shoulders = getResources().getIntArray(R.array.Exercisedumbel_Shoulder);
            final int[] outfit_shoulders = getResources().getIntArray(R.array.Exerciseoutfit_Shoulder);
            final int[] difficulty_shoulders = getResources().getIntArray(R.array.Exercisedifficulty_Shoulder);

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isDumbel", bool_list[0]);
            editor.putBoolean("isBabel", bool_list[1]);
            editor.putBoolean("isOutfit", bool_list[2]);
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
            boolean isDumbel = bool_list[0];
            boolean isBabel = bool_list[1];
            boolean isOutfit = bool_list[2];
            Gson gson = new Gson();
            ExerciseRoutine exerciseRoutine = new ExerciseRoutine();
            String height_buf = pref.getString("height",null);;
            String weight_buf = pref.getString("weight",null);
            if(height_buf != null && weight_buf != null){
                int height = Integer.parseInt(height_buf);
                int weight = Integer.parseInt(weight_buf);
                float BMI = weight * 100 * 100 / height / height;
                exerciseRoutine.set = 3;
                exerciseRoutine.count = 20;
                exerciseRoutine.weight = (int) (2*(BMI - BMI % 5));
            } else {
                Toast.makeText(this, "설정한 키, 몸무게 값이 없어", Toast.LENGTH_SHORT).show();
                exerciseRoutine.set = 3;
                exerciseRoutine.count = 20;
                exerciseRoutine.weight = 30;
            }


            ArrayList<Exercise> ExerciseList = new ArrayList<Exercise>();
            ArrayList<ArrayList<Exercise>> PartList =new ArrayList<ArrayList<Exercise>>();
            if(!bool_list[6]) {
                ArrayList<Exercise>[] _list = new ArrayList[7];
                for(int i = 0; i < 7; i++){
                    _list[i] = new ArrayList<Exercise>();
                }
                for (int i = 0; i < 17; i++) {
                    if (!bool_list[5] || (difficulty_arms[i] <= SettingDifficulty)) {
                        Exercise exercise = new Exercise(name_arms[i], "팔", difficulty_arms[i], dumbel_arms[i] == 1, babel_arms[i] == 1, outfit_arms[i] == 1);
                        ExerciseList.add(exercise);
                        _list[0].add(exercise);
                    }
                }
                for (int i = 0; i < 19; i++) {
                    if (!bool_list[5] || (difficulty_shoulders[i] <= SettingDifficulty)) {
                        Exercise exercise = new Exercise(name_shoulders[i], "어깨", difficulty_shoulders[i], dumbel_shoulders[i] == 1, babel_shoulders[i] == 1, outfit_shoulders[i] == 1);
                        ExerciseList.add(exercise);
                        _list[1].add(exercise);
                    }
                }
                for (int i = 0; i < 14; i++) {
                    if (!bool_list[5] || (difficulty_back[i] <= SettingDifficulty)) {
                        Exercise exercise = new Exercise(name_back[i], "등", difficulty_back[i], dumbel_back[i] == 1, babel_back[i] == 1, outfit_back[i] == 1);
                        ExerciseList.add(exercise);
                        _list[2].add(exercise);
                    }
                }
                for (int i = 0; i < 13; i++) {
                    if (!bool_list[5] || (difficulty_abdomen[i] <= SettingDifficulty)) {
                        Exercise exercise = new Exercise(name_abdomen[i], "복부", difficulty_abdomen[i], dumbel_abdomen[i] == 1, babel_abdomen[i] == 1, outfit_abdomen[i] == 1);
                        ExerciseList.add(exercise);
                        _list[3].add(exercise);
                    }
                }
                for (int i = 0; i < 21; i++) {
                    if (!bool_list[5] || (difficulty_legs[i] <= SettingDifficulty)) {
                        Exercise exercise = new Exercise(name_legs[i], "대퇴", difficulty_legs[i], dumbel_legs[i] == 1, babel_legs[i] == 1, outfit_legs[i] == 1);
                        ExerciseList.add(exercise);
                        _list[4].add(exercise);
                    }
                }
                for (int i = 0; i < 10; i++) {
                    if (!bool_list[5] || (difficulty_buttocks[i] <= SettingDifficulty)) {
                        Exercise exercise = new Exercise(name_buttocks[i], "둔부", difficulty_buttocks[i], dumbel_buttocks[i] == 1, babel_buttocks[i] == 1, outfit_buttocks[i] == 1);
                        ExerciseList.add(exercise);
                        _list[5].add(exercise);
                    }
                }
                for (int i = 0; i < 15; i++) {
                    if (!bool_list[5] || (difficulty_chest[i] <= SettingDifficulty)) {
                        Exercise exercise = new Exercise(name_chest[i], "가슴", difficulty_chest[i], dumbel_chest[i] == 1, babel_chest[i] == 1, outfit_chest[i] == 1);
                        ExerciseList.add(exercise);
                        _list[6].add(exercise);
                    }
                }
                for(int i = 0; i < 7; i++){
                    PartList.add(_list[i]);
                }
            }
            else {
                String json = pref.getString("joinlist", null);
                if(json != null){
                    ExerJoinList Elist = gson.fromJson(json, ExerJoinList.class);
                    Integer[] cnt_list = new Integer[]{0,0,0,0,0,0,0};
                    ArrayList<Exercise>[] _list = new ArrayList[7];
                    for(int i = 0; i < 7; i++){
                        _list[i] = new ArrayList<Exercise>();
                    }
                    for (Exercise E : Elist.list) {
                        if ((!bool_list[5] || (E.Difficulty <= SettingDifficulty))) {
                            ExerciseList.add(E);
                            switch (E.Part) {
                                case "팔":
                                    if (cnt_list[0] <= 4) {
                                        _list[0].add(E);
                                        cnt_list[0]++;
                                    }
                                    break;
                                case "어깨":
                                    if (cnt_list[1] <= 4) {
                                        _list[1].add(E);
                                        cnt_list[1]++;
                                    }
                                    break;
                                case "등":
                                    if (cnt_list[2] <= 4) {
                                        _list[2].add(E);
                                        cnt_list[2]++;
                                    }
                                    break;
                                case "가슴":
                                    if (cnt_list[3] <= 4) {
                                        _list[3].add(E);
                                        cnt_list[3]++;
                                    }
                                    break;
                                case "복부":
                                    if (cnt_list[4] <= 4) {
                                        _list[4].add(E);
                                        cnt_list[4]++;
                                    }
                                    break;
                                case "대퇴":
                                    if (cnt_list[5] <= 4) {
                                        _list[5].add(E);
                                        cnt_list[5]++;
                                    }
                                    break;
                                case "둔부":
                                    if (!bool_list[4]) {
                                        if (cnt_list[6] <= 4) {
                                            _list[6].add(E);
                                            cnt_list[6]++;
                                        }
                                    } else {
                                        if (cnt_list[6] <= 6) {
                                            _list[cnt_list[6]].add(E);
                                            cnt_list[6]++;
                                        }
                                    }
                                    break;
                            }
                            for (int i = 0; i < 7; i++) {
                                exerciseRoutine.Daily.set(i, _list[i]);
                            }
                        }
                    }
                }
            }
            for(Exercise E : ExerciseList) {
                if ((!isDumbel && E.isDumbel) || (!isBabel && E.isBabel) || (!isOutfit && E.isOutfit)) {
                    ExerciseList.remove(E);
                }
            }
            if(!bool_list[6])
            for(int i = 0; i < 7; i++) {
                for (Exercise E : PartList.get(i)) {
                    if ((!isDumbel && E.isDumbel) || (!isBabel && E.isBabel) || (!isOutfit && E.isOutfit)) {
                        PartList.get(i).remove(E);
                    }
                }
            }
            if(!bool_list[6]){
                for(int i = 0; i < PartList.get(1).size(); i += PartList.get(1).size() / 4){
                    exerciseRoutine.Daily.get(0).add(PartList.get(1).get(i));
                }
                for(int i = 0; i < PartList.get(2).size(); i += PartList.get(2).size() / 4){
                    exerciseRoutine.Daily.get(1).add(PartList.get(2).get(i));
                }
                for(int i = 0; i < PartList.get(3).size(); i += PartList.get(3).size() / 4){
                    exerciseRoutine.Daily.get(2).add(PartList.get(3).get(i));
                }
                for(int i = 0; i < PartList.get(5).size(); i += PartList.get(5).size() / 4){
                    exerciseRoutine.Daily.get(3).add(PartList.get(5).get(i));
                }
                for(int i = 0; i < PartList.get(6).size(); i += PartList.get(6).size() / 4){
                    exerciseRoutine.Daily.get(4).add(PartList.get(6).get(i));
                }
                for(int i = 0; i < PartList.get(4).size(); i += PartList.get(4).size() / 4){
                    exerciseRoutine.Daily.get(5).add(PartList.get(4).get(i));
                }
                if(!bool_list[4]){
                    for(int i = 0; i < PartList.get(0).size(); i += PartList.get(0).size() / 4){
                        exerciseRoutine.Daily.get(6).add(PartList.get(0).get(i));
                    }
                }else{
                    for(int i = 0, c = 0; i < PartList.get(0).size(); i += PartList.get(0).size() / 5,c++){
                        exerciseRoutine.Daily.get(c).add(PartList.get(0).get(i));
                    }
                }
            }
            ExerJoinList FinalExerciseList = new ExerJoinList();
            FinalExerciseList.list = ExerciseList;
            String routinejson = gson.toJson(exerciseRoutine, ExerciseRoutine.class);
            String finaljson = gson.toJson(FinalExerciseList, ExerJoinList.class);
            Intent intent = new Intent(this, ExerciseRoutineSelectActivity.class);
            intent.putExtra("json", finaljson);
            intent.putExtra("routine", routinejson);
            finish();
            startActivity(intent);
        }
    }
}