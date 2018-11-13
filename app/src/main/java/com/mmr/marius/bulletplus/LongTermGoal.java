package com.mmr.marius.bulletplus;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

public class LongTermGoal {
    private static final String TAG = "com.marius.longtermgoal";
    private Date created;
    private boolean done_flag;
    private String title;
    private DocumentReference user;
    private DocumentReference category_1;
    private DocumentReference category_2;
    private FireBaseHandler fbh;

    Task<QuerySnapshot> a;
    Task<QuerySnapshot> b;
    Task<QuerySnapshot> c;

    public LongTermGoal(){
        //needed for FireBase
    }

    public LongTermGoal(String title, String category_1_type, String category_2_type){
        this.title = title;
        this.done_flag = false;
        this.created = new Date();


        //TODO references
        this.fbh = new FireBaseHandler();

        a = fbh.getCollectionReference("users").whereEqualTo("user_Id", fbh.getUserID()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                user = queryDocumentSnapshots.getDocuments().get(0).getReference();

            }
        });

        b = fbh.getCollectionReference("categories").whereEqualTo("type", category_1_type).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                QuerySnapshot qs = queryDocumentSnapshots;
                category_1 = queryDocumentSnapshots.getDocuments().get(0).getReference();
            }
        });

        c = fbh.getCollectionReference("categories").whereEqualTo("type", category_2_type).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.i(TAG, "size" + queryDocumentSnapshots.size());
                //Log.i(TAG, queryDocumentSnapshots.getDocuments().get(0).get("type").toString());
                category_2 = queryDocumentSnapshots.getDocuments().get(0).getReference();
                Log.i(TAG, category_2.getId());
                Log.i(TAG, category_2.getPath());
            }
        });

        //Log.i(TAG, category_2.toString());
        //TODO set categories
    }
    public LongTermGoal getThis(){
        return this;
    }
    private String s;
    public void add(){
        Tasks.whenAllSuccess(a,b,c).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> objects) {
                new FireBaseHandler().addLongTermGoal(getThis());
                Log.i(TAG, "all done");
                Log.i(TAG, "1 " + category_1.toString() + " 2 " + category_2.toString() + " user " + user.toString());
            }
        });
    }

}
