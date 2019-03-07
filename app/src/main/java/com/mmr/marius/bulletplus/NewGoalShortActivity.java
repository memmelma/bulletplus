package com.mmr.marius.bulletplus;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class NewGoalShortActivity extends AppCompatActivity {

    private EditText mEditTextGoal;
    private RadioGroup mRadioGroup;
    private RecyclerView mRecyclerView;

    private final static String TAG = "com.marius.newgoal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal_short);

        mEditTextGoal = (EditText) findViewById(R.id.edit_goal_title);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroupCategories);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        FireBaseHandler fbh = new FireBaseHandler();

        Query query = fbh.getLongTermGoals()
                .whereEqualTo("userId", fbh.getUserID())
                .whereEqualTo("done", false)
                .orderBy("created", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<LongTermGoal> options_long = new FirestoreRecyclerOptions.Builder<LongTermGoal>()
                .setQuery(query, LongTermGoal.class)
                .build();

        final GoalAdapterAddShortTerm mAdapterLongTerm = new GoalAdapterAddShortTerm(options_long);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(new Activity()));
        mRecyclerView.setAdapter(mAdapterLongTerm);

        mAdapterLongTerm.startListening();

        FloatingActionButton mButtonAddNote = findViewById(R.id.button_save);
        mButtonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGoal(mAdapterLongTerm);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_goal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveGoal(GoalAdapterAddShortTerm goalAdapterAddShortTerm){

        int selectedId = mRadioGroup.getCheckedRadioButtonId();

        long category;

        switch(selectedId){
            case R.id.radio0:
                category = 0; //"PERSONAL";
                break;
            case R.id.radio1:
                category = 1; //"SOCIAL";
                break;
            case R.id.radio2:
                category = 2; //"HEALTH";
                break;
            case R.id.radio3:
                category = 3; //"PROFESSIONAL";
                break;
            default:
                //should never be the case, because there always is a selected radio button
                category = 0; //"PERSONAL";
                break;
        }

        String goalTitle = mEditTextGoal.getText().toString();

        String longTermGoalId = goalAdapterAddShortTerm.getSelectionId();

        if(goalTitle.trim().isEmpty()){
            Toast.makeText(this, "Please insert a goal",Toast.LENGTH_SHORT).show();
            return;
        }

        //Log.i(TAG, "category " + category + " title " + goalTitle);

        FireBaseHandler fbh = new FireBaseHandler();
        String uid = fbh.getUserID();

        ShortTermGoal stg = new ShortTermGoal(goalTitle, fbh.getUserID(), longTermGoalId, category);
        fbh.addShortTermGoal(stg);
        Toast.makeText(this, "short goal added", Toast.LENGTH_SHORT).show();


        finish();
    }

}
