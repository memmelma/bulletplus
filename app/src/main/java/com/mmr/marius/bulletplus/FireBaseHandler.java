package com.mmr.marius.bulletplus;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class FireBaseHandler {
    private final static String TAG = "com.marius.fbhandler";

    private FirebaseAuth mAuth;

    public FireBaseHandler(){
        mAuth = FirebaseAuth.getInstance();
    }

    public void addShortTermGoal(ShortTermGoal stg, String uid){
        getShortTermGoalsUndone(uid).add(stg);
    }

    public CollectionReference getShortTermGoalsUndone(String uid){
        return getCollectionReference("short_term_goals/" + uid + "/undone");
    }

    public void addLongTermGoal(LongTermGoal ltg, String uid){
        getLongTermGoalsUndone(uid).add(ltg);
    }

    public CollectionReference getLongTermGoalsUndone(String uid){
        return getCollectionReference("long_term_goals/" + uid + "/undone");
    }

    public void addUser(User  user){
        getCollectionReference("users").add(user);
    }

    public FirebaseUser getUser(){
        FirebaseUser user = mAuth.getCurrentUser();

        return user;
    }

    public String getUserID(){
        return getUser().getUid();
    }

    private CollectionReference getCollectionReference(String collectionName){
        return FirebaseFirestore.getInstance().collection(collectionName);
    }

    //TODO implement or remove
    public void query(String collection_name, Query query){

    }

}
