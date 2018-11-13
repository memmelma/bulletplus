package com.mmr.marius.bulletplus;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "com.marius.auth";

    private FirebaseAuth mAuth;
    private EditText mEmail;
    private EditText mPassword;
    private Button mEmailSignInButton;
    private Button mEmailRegisterButton;

    private SharedPreferences loginDetails;
    private SharedPreferences.Editor loginDetailsEditor;
    //private Boolean saveLogin //checkbox

    //TODO FIX empty email and password make app crash

    //TODO fix register and pop up of sign in screen when already signed in (move sign in methode and inflate only if failed)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                loginDetailsEditor.putString("email", email);
                loginDetailsEditor.putString("password", password);
                loginDetailsEditor.commit();

                Log.i(TAG, email + " - " + password);

                signIn(email, password);
            }
        });

        mEmailRegisterButton = (Button) findViewById(R.id.goto_register_in_button);
        mEmailRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AuthActivity.this, RegisterActivity.class));
            }
        });

        loginDetails = getSharedPreferences("loginDetails", MODE_PRIVATE);
        loginDetailsEditor = loginDetails.edit();

        String email = loginDetails.getString("email", "");
        String password = loginDetails.getString("password", "");

        if(email != "" && password != ""){
            signIn(email,password);
        }

    }

    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(AuthActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();

                            Intent resultIntent = new Intent();
                            //resultIntent.putExtra("email", email);

                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            signOut();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    //TODO add signOut button
    private void signOut(){
        mAuth.signOut();
        loginDetailsEditor.clear();
        loginDetailsEditor.commit();
    }
}
