package com.mmr.marius.bulletplus;

import java.util.Date;

public class ShortTermGoal {
    private Date created;
    private String title;
    private String long_term_goal_Id;
    private long category;
    public ShortTermGoal(){
        //needed for FireBase
    }

    public ShortTermGoal(String title, long category){
        this.title = title;
        this.created = new Date();
        this.category = category;
        //TODO connect with long_term_goal
        this.long_term_goal_Id = "1M7ZSWDaYWH9X4aXIvw2";
    }

    public Date getCreated() {
        return created;
    }

    public String getTitle() {
        return title;
    }

    public String getLong_term_goal_Id() {
        return long_term_goal_Id;
    }

    public long getCategory() {
        return category;
    }

}
