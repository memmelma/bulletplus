package com.mmr.marius.bulletplus;

import java.util.Date;

public class ShortTermGoal {

    private String title;
    private String long_term_goal_id;
    private String long_term_goal_title;
    private String user_id;
    private Date created;
    private long category;
    private Boolean done;

    public ShortTermGoal(){
        //needed for FireBase
    }

    public ShortTermGoal(String title, String user_id, String long_term_goal_id, String long_term_goal_title, long category){
        this.title = title;
        this.user_id = user_id;
        this.created = new Date();
        this.category = category;
        this.long_term_goal_id = long_term_goal_id;
        this.long_term_goal_title = long_term_goal_title;
        this.done = false;
    }

    public String getTitle() {
        return title;
    }

    public String getUser_id(){
        return user_id;
    }

    public Date getCreated() {
        return created;
    }

    public long getCategory() {
        return category;
    }

    public String getLong_term_goal_id() {
        return long_term_goal_id;
    }

    public String getLong_term_goal_title() {
        return long_term_goal_title;
    }

    public Boolean getDone(){
        return done;
    }
}
