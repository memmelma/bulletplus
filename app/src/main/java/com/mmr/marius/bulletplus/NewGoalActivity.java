package com.mmr.marius.bulletplus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.server.converter.StringToIntConverter;

public class NewGoalActivity extends AppCompatActivity {

    private EditText mEditTextGoal;
    private RadioGroup mRadioGroup;

    private String type;
    private final String type_long = "long-term";
    private final String type_short = "short-term";

    private final static String TAG = "com.marius.newgoal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

        Intent i = getIntent();
        int extra = i.getIntExtra(MainActivity.TAG, 0);

        switch(extra){
            case 0:
                type = type_short;
                break;
            case 1:
                type = type_long;
                break;
            default:
                return;
        }

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add " + type + " goal");

        mEditTextGoal = (EditText) findViewById(R.id.edit_goal_title);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroupCategories);
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

        if(goalTitle.trim().isEmpty()){
            Toast.makeText(this, "Please insert a goal",Toast.LENGTH_SHORT).show();
            return;
        }

        //Log.i(TAG, "category " + category + " title " + goalTitle);

        FireBaseHandler fbh = new FireBaseHandler();
        String uid = fbh.getUserID();

        if(type.equals(type_short)){
            ShortTermGoal stg = new ShortTermGoal(goalTitle, category);
            fbh.addShortTermGoal(stg, uid);
            Toast.makeText(this, type_short + " goal added", Toast.LENGTH_SHORT).show();
        } else{
            LongTermGoal ltg = new LongTermGoal(goalTitle, category);
            fbh.addLongTermGoal(ltg, uid);
            Toast.makeText(this, type_long + " goal added", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

}
