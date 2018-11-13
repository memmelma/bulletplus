package com.mmr.marius.bulletplus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewNoteActivity extends AppCompatActivity {

    private EditText mEditTextTitle;
    private EditText mEditTextDescription;
    private NumberPicker mNumberPickerPriority;


    private final static String TAG = "com.marius.newnote";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Note");

        mEditTextTitle = findViewById(R.id.edit_text_title);
        mEditTextDescription = findViewById(R.id.edit_text_description);
        mNumberPickerPriority = findViewById(R.id.number_picker_priority);
        mNumberPickerPriority.setMinValue(1); //min prio
        mNumberPickerPriority.setMaxValue(10); //max prio
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;

            //add more cases here for different menu items

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {
        String title = mEditTextTitle.getText().toString();
        String description = mEditTextDescription.getText().toString();
        int priority = mNumberPickerPriority.getValue();

        if(title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and a description",Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference mNotebookRef = FirebaseFirestore.getInstance()
                .collection("Notebook");
        mNotebookRef.add(new Note(title, description, priority));

        LongTermGoal ltg = new LongTermGoal("title", "PERSONAL", "PROFESSIONAL");
        ltg.add();

        //FireBaseHandler fbh = new FireBaseHandler();
        //fbh.addLongTermGoal(ltg);

        Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
        finish();
    }
}
