package com.mmr.marius.bulletplus;

import java.util.Date;

public class ShortTermGoal {
    private Date created;
    private String title;
    private String long_term_goal_Id;
    private String category_1;
    private String category_2;
    //TODO interface for ShortTermGoal and LongTermGoals
    public ShortTermGoal(){
        //needed for FireBase
    }

    public ShortTermGoal(String title){
        this.title = title;
        this.created = new Date();
        this.category_1 = category_1;
        this.category_2 = category_2;
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

    public String getCategory_1() {
        return category_1;
    }

    public String getCategory_2() {
        return category_2;
    }
}
