package com.osam.bodyprotector;

public class Exercise {
    public String ExerciseName;
    public String Part;
    public int Difficulty; // 0 : easy, 1 : medium, 2 : hard
    public boolean isDumbel;
    public boolean isOutfit;
    public boolean isBabel;

    public Exercise(String ExerciseName, String Part, int Difficulty, boolean isDumbel, boolean isBabel, boolean isOutfit){
        this.ExerciseName = ExerciseName;
        this.Part = Part;
        this.Difficulty = Difficulty;
        this.isBabel = isBabel;
        this.isDumbel = isDumbel;
        this.isOutfit = isOutfit;
    }


    public boolean equals(Object o){
        if(o instanceof Exercise){
            Exercise toCompare = (Exercise) o;
            return this.ExerciseName.equals(toCompare.ExerciseName);
        }
        return false;
    }

}
