package com.mmr.marius.bulletplus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class NewGoalActivity extends AppCompatActivity {

    private EditText mEditTextTitle;
    private EditText mEditTextDescription;
    private NumberPicker mNumberPickerPriority;

    //TODO add long term and short term switch

    private final static String TAG = "com.marius.newgoal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add -placeholder- Goal");

        mEditTextTitle = findViewById(R.id.edit_text_title);
        //mEditTextDescription = findViewById(R.id.edit_text_description);
        //mNumberPickerPriority = findViewById(R.id.number_picker_priority);
        //mNumberPickerPriority.setMinValue(1); //min prio
        //mNumberPickerPriority.setMaxValue(10); //max prio
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
        //TODO write to firebase
        //TODO distinguish between long / short term

        //get inputs and validate them
        /*
        if(title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and a description",Toast.LENGTH_SHORT).show();
            return;
        }
        */

        LongTermGoal ltg = new LongTermGoal("title", "PERSONAL", "PROFESSIONAL");

        FireBaseHandler fbh = new FireBaseHandler();
        String uid = fbh.getUserID();
        fbh.addLongTermGoal(ltg, uid);

        Toast.makeText(this, "Goal added", Toast.LENGTH_SHORT).show();
        finish();
    }


    private void saveNote() {
        String title = mEditTextTitle.getText().toString();
        String description = mEditTextDescription.getText().toString();
        int priority = mNumberPickerPriority.getValue();

        if(title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and a description",Toast.LENGTH_SHORT).show();
            return;
        }

        /*
        CollectionReference mNotebookRef = FirebaseFirestore.getInstance()

                .collection("Notebook");
        mNotebookRef.add(new Note(title, description, priority));
         */

        LongTermGoal ltg = new LongTermGoal("title", "PERSONAL", "PROFESSIONAL");

        FireBaseHandler fbh = new FireBaseHandler();
        String uid = fbh.getUserID();
        fbh.addLongTermGoal(ltg, uid);

        Toast.makeText(this, "Goal added", Toast.LENGTH_SHORT).show();
        finish();
    }
}
