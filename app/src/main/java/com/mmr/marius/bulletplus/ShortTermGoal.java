package com.mmr.marius.bulletplus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class ShortTermGoal {

    private String title;
    private String long_term_goal_Id;
    private String userId;
    private Date created;
    private long category;
    private String id;
    private Boolean done;

    public ShortTermGoal(){
        //needed for FireBase
    }

    public ShortTermGoal(String title, String userId, String long_term_goal_Id, long category){
        this.title = title;
        this.userId = userId;
        this.created = new Date();
        this.category = category;
        this.long_term_goal_Id = long_term_goal_Id;
        this.done = false;
    }

    public String getTitle() {
        return title;
    }

    public String getUserId(){
        return userId;
    }

    public Date getCreated() {
        return created;
    }

    public long getCategory() {
        return category;
    }

    public String getLong_term_goal_Id() {
        return long_term_goal_Id;
    }

    public Boolean getDone(){
        return done;
    }
}
