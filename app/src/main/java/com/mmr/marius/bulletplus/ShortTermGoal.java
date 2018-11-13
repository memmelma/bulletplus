package com.mmr.marius.bulletplus;

import java.util.Date;

public class ShortTermGoal {
    private Date created;
    private boolean done_flag;
    private String title;
    private String user;
    //private String category_1;
    //private String category_2;

    private FireBaseHandler fbh;

    public ShortTermGoal(){
        //needed for FireBase
    }

    public ShortTermGoal(String title){
        this.title = title;
        this.done_flag = false;
        this.created = new Date();


        //TODO references
        this.fbh = new FireBaseHandler();
        this.user = fbh.getUserID();

    }
}
