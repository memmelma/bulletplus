package com.mmr.marius.bulletplus;

import java.util.Date;

public class LongTermGoal {
    private static final String TAG = "com.marius.longtermgoal";
    private Date created;
    private String title;
    private long category;

    public LongTermGoal(){
        //needed for FireBase
    }

    public LongTermGoal(String title, long category){
        this.title = title;
        this.created = new Date();
        this.category = category;
    }

    public Date getCreated() {
        return created;
    }

    public String getTitle() {
        return title;
    }

    public long getCategory() {
        return category;
    }

}
