package com.mmr.marius.bulletplus;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;

public class FireBaseHandler {
    private final static String TAG = "com.marius.fbhandler";

    private FirebaseAuth mAuth;
    private final String short_term_collection = "fuel";
    private final String long_term_collection = "engine";

    public FireBaseHandler(){
        mAuth = FirebaseAuth.getInstance();
    }

    public void addShortTermGoal(ShortTermGoal stg){
        getCollectionReference(short_term_collection).add(stg);

        getCollectionReference(long_term_collection).whereEqualTo("id", stg.getLong_term_goal_Id());
    }

    public void rmShortTermGoal(String goalId){
        getCollectionReference(short_term_collection).document(goalId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public void setDoneShortTermGoal(String goalId){
        getCollectionReference(short_term_collection).document(goalId).update("done", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document successfully set done!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public CollectionReference getShortTermGoals(){
        return getCollectionReference(short_term_collection);
    }

    public void addLongTermGoal(LongTermGoal ltg){
        getCollectionReference(long_term_collection).add(ltg);
    }

    public  void rmLongTermGoal (String goalId){
        getCollectionReference(long_term_collection).document(goalId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public void setDoneLongTermGoal(String goalId){
        getCollectionReference(long_term_collection).document(goalId).update("done", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document successfully set done!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public CollectionReference getLongTermGoals(){
        return getCollectionReference(long_term_collection);
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

    public Task countLongTermDone(String id){

        final Query query = getShortTermGoals()
                .whereEqualTo("userId", getUserID())
                .whereEqualTo("long_term_goal_Id", id)
                .whereEqualTo("done", true);

        return query.get();
    }

    public int countLongTermTotal(String id){

        Query query = getShortTermGoals()
                .whereEqualTo("userId", getUserID())
                .whereEqualTo("long_term_goal_Id", id);

        return query.get().getResult().size();
    }

    //helper
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
