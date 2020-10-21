package com.osam.bodyprotector;

import java.util.HashMap;
import java.util.Map;

public class PostData {
    public String uri;
    public String title;
    public String main;
    public String uid;
    public String image_name;
    public String post_uid;
    public int starCount;
    public User user;
    public Map<String, Boolean> stars = new HashMap<>();
}
