package com.miniprojeto.telmomanique.fitnessexercisetracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Firebase;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.User;

public class Login extends AppCompatActivity {
    private String TAG = "DEBUG_LOGIN";
    private Firebase fireBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fireBase = Firebase.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if( fireBase.isLoggedIn() ){
            Intent mainPage = new Intent(this, MainActivity.class);
            startActivity(mainPage);
        }
    }

    public void onRegister( View view ){
        Intent registerPage = new Intent ( this, Register.class);
        startActivity(registerPage);
    }

    public void onLogin( View view ) {

        final String mail = ((EditText) findViewById(R.id.editTextEmail)).getText().toString();
        final String password = ((EditText) findViewById(R.id.editTextPassword)).getText().toString();

        if (mail == null
                || password == null) {
            Log.d(TAG, "Parameters null");
            return;
        }
        if (mail.compareTo("") == 0
                || password.compareTo("") == 0) {
            Log.d(TAG, "Parameters empty");
            return;
        }

        fireBase.getAuth().signInWithEmailAndPassword(mail, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");

                    FirebaseUser user = fireBase.getAuth().getCurrentUser();
                    User u = new User( user.getUid() , user.getEmail() );

                    fireBase.setUser( u );
                    finish();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }
            }
        });
    } // END onLogin()
}