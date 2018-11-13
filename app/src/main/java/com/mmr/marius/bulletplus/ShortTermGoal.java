package com.mmr.marius.bulletplus;

import java.util.Date;

public class ShortTermGoal {
    private Date created;
    private boolean done_flag;
    private String title;
    private String userId;
    private String long_term_goal_ID;
    private String category_1;
    private String category_2;
    //TODO interface for ShortTermGoal and LongTermGoals
    public ShortTermGoal(){
        //needed for FireBase
    }

    public ShortTermGoal(String title){
        this.title = title;
        this.done_flag = false;
        this.created = new Date();
        this.userId = new FireBaseHandler().getUserID();
        this.category_1 = category_1;
        this.category_2 = category_2;
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

    public String getCategory_1() {
        return category_1;
    }

    public String getCategory_2() {
        return category_2;
    }
}
