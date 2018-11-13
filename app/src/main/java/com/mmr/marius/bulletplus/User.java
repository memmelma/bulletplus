package com.mmr.marius.bulletplus;

public class User {
    private String mail;
    private String name;
    private String user_Id;

    private FireBaseHandler fbh;

    public User(){
        //needed for FireBase
    }

    public User(String mail){
        this.mail = mail;
        this.name = "placeholder";

        //no references, pure String
        this.fbh = new FireBaseHandler();
        this.user_Id = fbh.getUserID();
    }
}
