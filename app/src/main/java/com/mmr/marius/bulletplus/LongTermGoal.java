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
    private String category_1;
    private String category_2;

    public LongTermGoal(){
        //needed for FireBase
    }

    public LongTermGoal(String title, String category_1, String category_2){
        this.title = title;
        this.created = new Date();
        this.category_1 = category_1;
        this.category_2 = category_2;
    }

    public Date getCreated() {
        return created;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory_1() {
        return category_1;
    }

    public String getCategory_2() {
        return category_2;
    }
}
