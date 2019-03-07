package com.mmr.marius.bulletplus;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class NewGoalLongActivity extends AppCompatActivity {

    private EditText mEditTextGoal;
    private EditText mEditTextDescription;
    private RadioGroup mRadioGroup;

    private final static String TAG = "com.marius.newgoal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal_long);

        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        //setTitle("Add " + type + " goal");

        mEditTextGoal = (EditText) findViewById(R.id.edit_goal_title);
        mEditTextDescription = (EditText) findViewById(R.id.edit_goal_description);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroupCategories);

        FloatingActionButton mButtonAddNote = findViewById(R.id.button_save);
        mButtonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGoal();
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
            case R.id.save_goal:
                saveGoal();
                return true;

            //add more cases here for different menu items

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveGoal(){

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
        String goalDescription = mEditTextDescription.getText().toString();

        if(goalTitle.trim().isEmpty()){
            Toast.makeText(this, "Please insert a goal",Toast.LENGTH_SHORT).show();
            return;
        }

        FireBaseHandler fbh = new FireBaseHandler();
        String uid = fbh.getUserID();

        LongTermGoal ltg = new LongTermGoal(goalTitle, goalDescription, fbh.getUserID(), category);
        fbh.addLongTermGoal(ltg);
        Toast.makeText(this, "long term goal added", Toast.LENGTH_SHORT).show();

        finish();
    }

}
