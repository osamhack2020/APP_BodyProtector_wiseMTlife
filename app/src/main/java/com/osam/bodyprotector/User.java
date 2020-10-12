package com.osam.bodyprotector;

import android.widget.Toast;

public class User{
    public String username;
    public String email;
    public String pw;

    public User(){}

    public User(String username, String email, String pw){
        this.username = username;
        this.email = email;
        this.pw = pw;
    }
}