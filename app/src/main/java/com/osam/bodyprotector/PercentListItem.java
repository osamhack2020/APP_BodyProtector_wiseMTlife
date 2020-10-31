package com.osam.bodyprotector;

import java.util.Date;

public class PercentListItem {
    public String ExerciseName;
    public String ExercisePart;
    public int percent;
    public Date date;


    public PercentListItem(){}

    public PercentListItem(String ExerciseName, String ExercisePart, Date date, int percent){
        this.ExerciseName = ExerciseName;
        this.ExercisePart = ExercisePart;
        this.percent = percent;
        this.date = date;
    }
}
