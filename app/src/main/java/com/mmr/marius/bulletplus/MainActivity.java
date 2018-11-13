package com.mmr.marius.bulletplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "com.marius.main";
    private final static int REQUEST_CODE_AUTH = 42;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private CollectionReference mNotebookRef = db.collection("Notebook");

    private boolean authComplete = false;

    private GoalAdapterLongTerm mAdapterLongTerm;
    private GoalAdapterShortTerm mAdapterShortTerm;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent(MainActivity.this, AuthActivity.class);
        startActivityForResult(i, REQUEST_CODE_AUTH);

        setContentView(R.layout.activity_main);
        FloatingActionButton mButtonAddNote = findViewById(R.id.button_add_note);
        mButtonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewNoteActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_CODE_AUTH:
                //use data.getExtra(...) to retrieve the returned data
                authComplete = true;
                uid = new FireBaseHandler().getUserID();
                setUpRecyclerViews();
                break;
        }
    }

    private void setUpRecyclerViews(){
        setUpRecyclerViewLongTermGoals();
        setUpRecyclerViewShortTermGoals();
    }

    private void setUpRecyclerViewLongTermGoals() {

        Log.i(TAG, uid);
        Query query = new FireBaseHandler().getLongTermGoals()
                .whereEqualTo("user_Id", uid);//.orderBy("created", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<LongTermGoal> options = new FirestoreRecyclerOptions.Builder<LongTermGoal>()
                .setQuery(query, LongTermGoal.class)
                .build();

        mAdapterLongTerm = new GoalAdapterLongTerm(options);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view_long_term);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapterLongTerm);

        mAdapterLongTerm.startListening();
    }

    private void setUpRecyclerViewShortTermGoals(){

        Query query = new FireBaseHandler().getShortTermGoals()
                .whereEqualTo("user_Id", uid);//.orderBy("created", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ShortTermGoal> options = new FirestoreRecyclerOptions.Builder<ShortTermGoal>()
                .setQuery(query, ShortTermGoal.class)
                .build();

        mAdapterShortTerm = new GoalAdapterShortTerm(options);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view_short_term);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapterShortTerm);

        mAdapterShortTerm.startListening();
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(authComplete) {
            mAdapterShortTerm.startListening();
            mAdapterLongTerm.startListening();
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(authComplete){
            mAdapterShortTerm.stopListening();
            mAdapterLongTerm.stopListening();
        }

    }
}
