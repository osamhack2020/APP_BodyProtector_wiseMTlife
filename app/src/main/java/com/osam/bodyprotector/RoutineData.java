package com.osam.bodyprotector;

public class RoutineData {
    public Exercise exercise;
    public int countperset;
    public int set;
    public int weight;

    public RoutineData(Exercise exercise, int countperset, int set, int weight){
        this.exercise = exercise;
        this.countperset = countperset;
        this.set = set;
        this.weight = weight;
    }
}
