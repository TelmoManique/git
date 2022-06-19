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
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Firebase;

public class Register extends AppCompatActivity {
    private String TAG = "DEBUG_REGISTER";

    private Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        firebase = Firebase.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if( firebase.isLoggedIn() ){
            Intent mainPage = new Intent(this, MainActivity.class);
            startActivity(mainPage);
        }
    }

    public void onRegister( View view ){

        final String mail =  ((EditText) findViewById(R.id.editTextEmail)).getText().toString();
        final String password =  ((EditText) findViewById(R.id.editTextPassword)).getText().toString();
        final String passwordConfirmation =  ((EditText) findViewById(R.id.editTextPasswordConfirmation)).getText().toString();

        if( mail == null
                || password == null
                || passwordConfirmation == null) {
            Log.d(TAG, "Parameters null");
            Toast.makeText(Register.this, "Parameters null", Toast.LENGTH_SHORT).show();
            return;
        }
        if( mail.compareTo("") == 0
                || password.compareTo("") == 0
                || passwordConfirmation.compareTo("") == 0) {
            Log.d(TAG, "Parameters empty");
            Toast.makeText(Register.this, "Parameters empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if( password.compareTo(passwordConfirmation) != 0 ){
            Log.d(TAG, "Passwords do not match");
            Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        firebase.getAuth().createUserWithEmailAndPassword(mail, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    Toast.makeText(Register.this, "User account added.", Toast.LENGTH_SHORT).show();

                    firebase.signOut();
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    } // END onRegister()
}
