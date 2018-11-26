package com.mmr.marius.bulletplus;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

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
