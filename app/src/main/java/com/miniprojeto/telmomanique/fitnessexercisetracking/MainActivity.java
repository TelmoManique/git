package com.miniprojeto.telmomanique.fitnessexercisetracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Firebase;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.User;

public class MainActivity extends AppCompatActivity {
    private String TAG = "DEBUG_MAINACTIVITY";
    private Firebase firebase;
    private User u;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        firebase = Firebase.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        if(!firebase.isLoggedIn()){
            Intent loginPage = new Intent(this, Login.class);
            startActivity(loginPage);
            return;
        }

        u = firebase.getUser();
        TextView username = (TextView) findViewById(R.id.textView);
        username.setText( u.getEmail() );
    }

    public void onLogout( View view ){
        firebase.signOut();
        Intent mainPage = new Intent(this, MainActivity.class);
        startActivity(mainPage);

    } // END onLogout()

    public void onStatus( View view ){
        Intent statusPage = new Intent(this, Status.class);
        startActivity(statusPage);
    }

    public void onNewRoutine( View view){
        Intent newRoutPage = new Intent(this, NewRoutine.class);
        startActivity(newRoutPage);
    }

    public void onMyRoutines( View view){
        Intent myRoutPage = new Intent(this, MyRoutines.class);
        startActivity(myRoutPage);
    }

    public void onExerciseList( View view ){
        Intent exListPage = new Intent(this, ExerciseList.class);
        startActivity(exListPage);
    }

    public void setFirebase( Firebase firebase ) {
        this.firebase = firebase;
    }
}