package com.mmr.marius.bulletplus;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Note {

    private String title;
    private String description;
    private int priority;
    private String Uid;

    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        this.Uid = user.getUid();

        //Log.i("hi", title + description + priority + Uid);
    }

    public Note(){
        //empty constructor needed for FireBase
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public String getUid(){ return Uid; }
}
