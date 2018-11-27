package com.mmr.marius.bulletplus;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoadingActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_SIGN_IN = 41;
    private final static String TAG = "com.marius.loading";

    private FirebaseAuth mAuth;
    private PrefSingleton mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();

        mSharedPreferences = PrefSingleton.getInstance();
        String email = mSharedPreferences.getPreference("email");
        String password = mSharedPreferences.getPreference("password");

        //Log.i(TAG, email + password);

        if(email!="" && password!="" && email!=null && password!=null)
            signIn(email, password);
        else{
            Intent i = new Intent(LoadingActivity.this, AuthActivity.class);
            startActivityForResult(i, REQUEST_CODE_SIGN_IN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_CODE_SIGN_IN:
                //Log.i(TAG, "returned from auth");
                //use data.getExtra(...) to retrieve the returned data

                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);

                finish();
                break;
        }
    }

    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");

                            Intent resultIntent = new Intent();
                            setResult(Activity.RESULT_OK, resultIntent);

                            finish();

                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            Intent i = new Intent(LoadingActivity.this, AuthActivity.class);
                            startActivityForResult(i, REQUEST_CODE_SIGN_IN);
                        }
                    }
                });
    }
}
