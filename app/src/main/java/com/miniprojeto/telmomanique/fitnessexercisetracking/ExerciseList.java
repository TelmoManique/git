package com.miniprojeto.telmomanique.fitnessexercisetracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Exercise;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Firebase;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.User;
import com.miniprojeto.telmomanique.fitnessexercisetracking.recyclers.ExerciseListRecyclerViewAdapter;
import com.miniprojeto.telmomanique.fitnessexercisetracking.recyclers.ExerciseListRecyclerViewInterface;

import java.util.ArrayList;

public class ExerciseList extends AppCompatActivity implements ExerciseListRecyclerViewInterface {
    //TODO https://rapidapi.com/justin-WFnsXH_t6/api/exercisedb POSSIBLE API
    private String TAG = "DEBUG_EXERCISELIST";
    private Firebase firebase;
    private User u;

    private ArrayList<Exercise> exercises = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        // Initialize Firebase Auth
        firebase = Firebase.getInstance();

        //Used to  read file, clean sanitize information, set up dataBase from file
        //setUpListExercises();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        // Check if user is signed in (non-null) and update UI accordingly.
        if (!firebase.isLoggedIn()) {
            Intent loginPage = new Intent(this, Login.class);
            startActivity(loginPage);
            return;
        }
        u = firebase.getUser();

        new TreatFileToDb().chooseFile(getAssets());
    } // END onStart()

    private void setUpListExercises(){
        firebase.getList_exercisesCollection().get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Exercise e = new Exercise();
                                e.setName( document.getId() );
                                e.setMuscleGroup( document.getString( "muscleGroup" ));
                                e.setExerciseType( document.getString("exerciseType" ));
                                e.setImage( document.getString( "image" ) );
                                exercises.add( e );
                                //Log.d("DEBUG_EXERCISELIST_addExercise", e.getName());
                            }
                            setUpView();
                            Log.d(TAG, "setUpListExercises done.");
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    } // END listExercisesList()

    private void setUpView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerViewExerciseList);

        ExerciseListRecyclerViewAdapter adapter = new ExerciseListRecyclerViewAdapter( this, exercises , this);

        recyclerView.setAdapter( adapter );
        recyclerView.setLayoutManager( new LinearLayoutManager( this ));
        Log.d(TAG, "recyclerView");
    } // END setUpView()

    @Override
    public void onExerciseClick(int position) {
        Exercise e = exercises.get(position);
        Log.d(TAG, "onExerciseClick: " + e.getName());

        //TODO CREATE ViewExercise CLASS

        Intent intent = new Intent(this, ViewExercise.class);
        intent.putExtra ("name" , e.getName());
        intent.putExtra ("muscleGroup" , e.getMuscleGroup());
        intent.putExtra ("type" , e.getExerciseType());
        intent.putExtra ("image" , e.getImage());
        startActivity(intent);

    }// END onExerciseClick()
}
