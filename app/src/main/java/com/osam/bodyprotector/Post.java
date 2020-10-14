package com.osam.bodyprotector;

public class Post{
    public User user;
    public String postname;
    public String postmain;

    public Post(){}

    public Post(User user, String postname, String postmain){
        this.user = user;
        this.postname = postname;
        this.postmain = postmain;
    }
}