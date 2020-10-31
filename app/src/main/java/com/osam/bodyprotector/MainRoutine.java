package com.osam.bodyprotector;

import java.util.ArrayList;

public class MainRoutine {
    public ArrayList<MainRoutineData>[] Daily = new ArrayList[7];

    public MainRoutine(){
        for(int i = 0; i < 7; i++){
            Daily[i] = new ArrayList<MainRoutineData>();
        }
    }
}
