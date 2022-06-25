package com.miniprojeto.telmomanique.fitnessexercisetracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Exercise;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Firebase;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Routine;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.User;
import com.miniprojeto.telmomanique.fitnessexercisetracking.recyclers.RoutineListRecyclerViewAdapter;
import com.miniprojeto.telmomanique.fitnessexercisetracking.recyclers.RoutineListRecyclerViewInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyRoutines extends AppCompatActivity implements RoutineListRecyclerViewInterface {
    private String TAG = "DEBUG_MYROUTINES";

    private Firebase firebase;
    private User u;
    private ArrayList<Routine> routines = new ArrayList<Routine>();
    private ArrayList<String> locations = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_routines);

        firebase = Firebase.getInstance();if(!firebase.isLoggedIn()){
            Intent loginPage = new Intent(this, Login.class);
            startActivity(loginPage);
            return;
        }

        u = firebase.getUser();

        getAllRoutines();
    }

    @Override
    public void onStart() {
        super.onStart();

        setUpView();
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
                            //TODO ADD LOCATION
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
            r.setDate( documents.getId().toString() );
            r.setLocation( documents.get("location").toString());

            firebase.getRoutinesCollection().document( "users" ).collection( u.getId() ).document( r.getDate() ).collection( "exercises" ).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Exercise e = new Exercise();
                                    e.setName( document.getId() );
                                    e.setReps( Math.toIntExact(document.getLong("reps")));
                                    e.setTime( Math.toIntExact(document.getLong("time")));
                                    e.setWeight( Math.toIntExact(document.getLong("weight")));
                                    e.setMuscleGroup( document.getString("exerciseType") );
                                    e.setExerciseType( document.getString("muscleGroup"));
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
        setUpView();
    } // END addRoutine()

    public void onAddRoutine( View view ){
        Intent newRoutinePage = new Intent(this, NewRoutine.class);
        startActivity(newRoutinePage);
    }

    private void setUpView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRoutineList);

        RoutineListRecyclerViewAdapter adapter = new RoutineListRecyclerViewAdapter( this, routines , this);

        recyclerView.setAdapter( adapter );
        recyclerView.setLayoutManager( new LinearLayoutManager( this ));
        Log.d(TAG, "recyclerView");
    } // END setUpView()

    @Override
    public void onRoutineClick(int position) {
        Routine r = routines.get(position);
        Log.d(TAG, "onExerciseClick: " + r.getDate());

        //TODO CREATE ViewRoutine
        //PASS THE FULL OBJECT
        /*Intent intent = new Intent(this, ViewExercise.class);
        startActivity(intent);
         */
    }

}