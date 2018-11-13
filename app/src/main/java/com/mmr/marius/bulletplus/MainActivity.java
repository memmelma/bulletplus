package com.mmr.marius.bulletplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private CollectionReference mNotebookRef = db.collection("Notebook");

    private boolean registered = false;

    private NoteAdapter mAdapter;

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
                //you just got back from activity B - deal with resultCode
                //use data.getExtra(...) to retrieve the returned data


                registered = true;
                setUpRecyclerView();
                break;
        }
    }

    private void setUpRecyclerView() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        Query query = mNotebookRef.whereEqualTo("uid", uid).orderBy("priority", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        mAdapter = new NoteAdapter(options);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(registered)
        mAdapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(registered)
        mAdapter.stopListening();
    }
}
