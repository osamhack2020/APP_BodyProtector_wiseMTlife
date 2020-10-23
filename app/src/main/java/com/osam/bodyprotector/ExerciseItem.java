package com.osam.bodyprotector;

import android.graphics.drawable.Drawable;

public class ExerciseItem {
    public String ExerciseName;
    public String ExerciseDescription;
    public String ExerPart;
    public Drawable ExerciseImg;
    public int ExerciseDifficulty; // 0 : easy, 1 : medium, 2 : hard
    public int isDumbel;
    public int isOutfit;
    public int isBabel;

    public ExerciseItem(){}

    public ExerciseItem(String ExerciseName, String ExerciseDescription, Drawable ExerciseImg, int ExerciseDifficulty, int isDumbel, int isOutfit, int isBabel, String ExerPart){
        this.ExerciseName = ExerciseName;
        this.ExerciseDescription = ExerciseDescription;
        this.ExerPart = ExerPart;
        this.ExerciseDifficulty = ExerciseDifficulty;
        this.ExerciseImg = ExerciseImg;
        this.isDumbel = isDumbel;
        this.isOutfit = isOutfit;
        this.isBabel = isBabel;
    }
}
