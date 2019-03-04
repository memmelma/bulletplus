package com.mmr.marius.bulletplus;

import android.app.Activity;
import android.content.Intent;
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

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "com.marius.auth";

    private FirebaseAuth mAuth;
    private EditText mEmail;
    private EditText mPassword;
    private Button mEmailSignInButton;
    private Button mEmailRegisterButton;

    private PrefSingleton mPrefSingleton;
    //private SharedPreferences loginDetails;
    //private SharedPreferences.Editor loginDetailsEditor;
    //private Boolean saveLogin //checkbox

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mPrefSingleton = PrefSingleton.getInstance();

        mAuth = FirebaseAuth.getInstance();

        mEmail = (EditText) findViewById(R.id.edit_text_email);
        mPassword = (EditText) findViewById(R.id.edit_text_password);

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                Log.i(TAG, "." + email + "." + " - " + password);

                if(!email.matches("") && !password.matches("")){
                    mPrefSingleton.writePreference("email", email);
                    mPrefSingleton.writePreference("password", password);

                    Log.i(TAG, email + " - " + password);

                    signIn(email, password);
                }
                else {
                    Toast.makeText(AuthActivity.this, "Enter email and password.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        mEmailRegisterButton = (Button) findViewById(R.id.goto_register_in_button);
        mEmailRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AuthActivity.this, RegisterActivity.class));
            }
        });

        String email = mPrefSingleton.getPreference("email");
        String password = mPrefSingleton.getPreference("password");

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
                            //FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(AuthActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();

                            Intent resultIntent = new Intent();
                            //resultIntent.putExtra("email", email);
                            setResult(Activity.RESULT_OK, resultIntent);

                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}
