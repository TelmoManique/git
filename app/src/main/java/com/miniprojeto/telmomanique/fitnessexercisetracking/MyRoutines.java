package com.miniprojeto.telmomanique.fitnessexercisetracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Exercise;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Firebase;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Routine;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.User;

import java.util.ArrayList;

public class MyRoutines extends AppCompatActivity {
    private String TAG = "DEBUG_MYROUTINES";

    private Firebase firebase;
    private User u;
    private ArrayList<Routine> routines = new ArrayList<Routine>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_routines);

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
        getAllRoutines();
    }

    private void getAllRoutines(){

        firebase.getRoutinesCollection().document( "users" ).collection( u.getId() ).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "start");
                            ArrayList<QueryDocumentSnapshot> routines = new ArrayList<QueryDocumentSnapshot>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                routines.add( document );
                                Log.d(TAG, "" + document.getId());
                            }
                            parseRoutines( routines );
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    } // END getAllRoutines()

    private void parseRoutines( ArrayList<QueryDocumentSnapshot> routines ){
        for( QueryDocumentSnapshot documents : routines ){
            Routine r = new Routine();
            r.setDate( documents.getId() );

            firebase.getRoutinesCollection().document( "users" ).collection( u.getId() ).document( r.getDate() ).collection( "exercises" ).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Exercise e = new Exercise();
                                    e.setName( document.getId() );
                                    e.setReps( Integer.parseInt(document.getString("reps")));
                                    e.setTime( Integer.parseInt(document.getString("time")));
                                    e.setWeight( Integer.parseInt(document.getString("weight")));
                                    r.addExercise(e);
                                }
                                addRoutine( r );
                                Log.d(TAG, "parseRoutines() done");
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    } // END parseRoutines()

    private void addRoutine( Routine r ){
        this.routines.add( r );

        Log.d(TAG, "r.date: " + r.getDate());
        for(Exercise e : r.getExercises() ){
            Log.d(TAG, "e.name: " + e.getName());
            Log.d(TAG, "e.reps: " + e.getReps());
            Log.d(TAG, "e.weight: " + e.getWeight());
            Log.d(TAG, "e.time: " + e.getTime());
        }
        //TODO ADD ROUTINE TO SCREEN
    } // END addRoutine()
}