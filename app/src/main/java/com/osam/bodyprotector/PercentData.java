package com.osam.bodyprotector;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PercentData {
    public ArrayList<String> list = new ArrayList<>();
    public HashMap<String, Integer> percent = new HashMap<>();
    public HashMap<String, String> part = new HashMap<>();
    public Date date;
    public int percentSum;

    public PercentData(){}

    public PercentData(Date date){
        this.date = date;
        percentSum = 0;
    }
}
