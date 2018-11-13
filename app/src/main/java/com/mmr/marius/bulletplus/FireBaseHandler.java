package com.mmr.marius.bulletplus;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class FireBaseHandler {
    private final static String TAG = "com.marius.fbhandler";

    public FireBaseHandler(){

    }

    public void addShortTermGoal(ShortTermGoal stg){
        getShortTermGoals().add(stg);
    }

    public CollectionReference getShortTermGoals(){
        return getCollectionReference("short_term_goals");
    }

    public void addLongTermGoal(LongTermGoal ltg){
        getLongTermGoals().add(ltg);
    }

    public CollectionReference getLongTermGoals(){
        return getCollectionReference("long_term_goals");
    }

    public void addUser(User  user){
        getCollectionReference("users").add(user);
    }

    public FirebaseUser getUser(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
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
