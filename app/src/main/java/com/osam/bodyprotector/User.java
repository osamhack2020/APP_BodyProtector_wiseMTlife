package com.osam.bodyprotector;

import android.widget.Toast;

public class User{
    public String username;
    public String email;
    public String pw;
    public String uuid;

    public User(){}

    public User(String username,String uuid, String email, String pw){
        this.username = username;
        this.uuid = uuid;
        this.email = email;
        this.pw = pw;
    }
}