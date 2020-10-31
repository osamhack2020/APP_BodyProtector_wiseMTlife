package com.osam.bodyprotector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Post{
    public User user;
    public String postname;
    public String postmain;
    public String postuid;
    public String image_path;
    public String image_name;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();
    public String PostingType;

    public Post(){}

    public Post(User user, String postname, String postmain, String image_path, String postuid, String image_name, String postingType){
        this.user = user;
        this.postname = postname;
        this.postmain = postmain;
        this.image_path = image_path;
        this.postuid = postuid;
        this.image_name = image_name;
        this.PostingType = postingType;
    }
}