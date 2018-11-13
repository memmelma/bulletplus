package com.mmr.marius.bulletplus;

import java.util.Date;

public class ShortTermGoal {
    private Date created;
    private boolean done_flag;
    private String title;
    private String userId;
    private String long_term_goal_ID;

    //private String category_1;
    //private String category_2;

    public ShortTermGoal(){
        //needed for FireBase
    }

    public ShortTermGoal(String title){
        this.title = title;
        this.done_flag = false;
        this.created = new Date();
        this.userId = new FireBaseHandler().getUserID();
        //TODO connect with long_term_goal
        this.long_term_goal_ID = "1M7ZSWDaYWH9X4aXIvw2";
    }

    public Date getCreated() {
        return created;
    }

    public boolean isDone_flag() {
        return done_flag;
    }

    public String getTitle() {
        return title;
    }

    public String getUserId() {
        return userId;
    }

    public String getLong_term_goal_ID() {
        return long_term_goal_ID;
    }
}
