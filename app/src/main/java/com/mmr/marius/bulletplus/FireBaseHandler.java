package com.mmr.marius.bulletplus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;

import java.util.Date;


public class FireBaseHandler {
    private final static String TAG = "com.marius.fbhandler";

    private FirebaseAuth mAuth;

    public FireBaseHandler(){
        mAuth = FirebaseAuth.getInstance();
    }

    public void addShortTermGoal(ShortTermGoal stg, String uid){
        getShortTermGoalsUndone(uid).add(stg);
    }

    public void rmShortTermGoal(String goal_id, String uid){
        DocumentReference fromPath = getShortTermGoalsUndone(uid).document(goal_id);
        DocumentReference toPath = getShortTermGoalsDone(uid).document(goal_id);

        fromPath.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

        //moveDocument(fromPath, toPath);
    }

    public CollectionReference getShortTermGoalsUndone(String uid){
        return getCollectionReference("short_term_goals/" + uid + "/undone");
    }

    public CollectionReference getShortTermGoalsDone(String uid){
        return getCollectionReference("short_term_goals/" + uid + "/done");
    }

    public void addLongTermGoal(LongTermGoal ltg, String uid){
        getLongTermGoalsUndone(uid).add(ltg);
    }

    public void rmLongTermGoal(String goal_id, String uid){
        Log.i(TAG, "goal_id " + goal_id);
        DocumentReference fromPath = getLongTermGoalsUndone(uid).document(goal_id);
        DocumentReference toPath = getLongTermGoalsDone(uid).document(goal_id);

        fromPath.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

        //moveDocument(fromPath, toPath);
    }

    public CollectionReference getLongTermGoalsUndone(String uid){
        return getCollectionReference("long_term_goals/" + uid + "/undone");
    }

    public CollectionReference getLongTermGoalsDone(String uid){
        return getCollectionReference("long_term_goals/" + uid + "/done");
    }

    public void addUser(User  user){
        getCollectionReference("users").add(user);
    }

    public FirebaseUser getUser(){
        FirebaseUser u = mAuth.getCurrentUser();
        return mAuth.getCurrentUser();
    }

    public String getUserID(){
        return getUser().getUid();
    }

    public CollectionReference getCollectionReference(String collectionName){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                //.setPersistenceEnabled(true)
                .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);
        return FirebaseFirestore.getInstance().collection(collectionName);
    }

    public void moveDocument(final DocumentReference fromPath, final DocumentReference toPath) {
        fromPath.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        toPath.set(document.getData())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                        fromPath.delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error deleting document", e);
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

}
