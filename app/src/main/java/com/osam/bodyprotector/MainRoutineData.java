package com.osam.bodyprotector;

public class MainRoutineData {
    public Exercise exercise;
    public int countperset;
    public int set;
    public int weight;
    public int percent;

    public MainRoutineData(Exercise exercise, int countperset, int set, int weight, int percent){
        this.exercise = exercise;
        this.countperset = countperset;
        this.set = set;
        this.weight = weight;
        this.percent = percent;
    }
}
