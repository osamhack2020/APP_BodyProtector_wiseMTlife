package com.osam.bodyprotector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExerciseRoutine {
    public List<List<Exercise>> Daily = new ArrayList<>();
    public int count;
    public int set;
    public int weight;

    public ExerciseRoutine(){
        for(int i = 0; i < 7; i++){
            List<Exercise> list = new ArrayList<>();
            Daily.add(list);
        }
    }

}
