package com.osam.bodyprotector;

import android.widget.Toast;

public class User{
    public String username;
    public String email;
    public String pw;
    public String uuid;
    public String regeon;
    public String height;
    public String weight;

    public User(){}

    public User(String username,String uuid, String email, String pw, String regeon, String height, String weight){
        this.username = username;
        this.uuid = uuid;
        this.email = email;
        this.pw = pw;
        this.regeon = regeon;
        this.height = height;
        this.weight = weight;
    }
}