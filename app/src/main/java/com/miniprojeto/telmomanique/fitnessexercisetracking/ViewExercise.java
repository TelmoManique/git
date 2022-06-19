package com.miniprojeto.telmomanique.fitnessexercisetracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Exercise;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Firebase;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.User;

public class ViewExercise extends AppCompatActivity {
    private String TAG = "DEBUG_VIEWEXERCISE";

    private Firebase firebase;
    private Exercise e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);

        firebase = Firebase.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        if(!firebase.isLoggedIn()){
            Intent loginPage = new Intent(this, Login.class);
            startActivity(loginPage);
            return;
        }
        e = new Exercise();
        e.setName( getIntent().getStringExtra("name").toString());
        e.setMuscleGroup( getIntent().getStringExtra("muscleGroup"));
        e.setExerciseType( getIntent().getStringExtra("type"));
        e.setImage(getIntent().getStringExtra("image"));
        Log.d(TAG, "onStart: " + e.getName());

        TextView namView = findViewById(R.id.textViewName);
        TextView muscleView = findViewById(R.id.textViewMuscleExercisedInfo);
        TextView typeView = findViewById(R.id.textViewTypeInfo);

        namView.setText(e.getName());
        muscleView.setText(e.getMuscleGroup());
        typeView.setText(e.getExerciseType());
    }
}