package com.miniprojeto.telmomanique.fitnessexercisetracking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Exercise;

import java.util.ArrayList;


public class NewExerciseFragment extends Fragment {

    private ArrayList<Exercise> exercises;

    public NewExerciseFragment( ArrayList<Exercise> exercises ){
        this.exercises = exercises;
    }


    public static NewExerciseFragment newInstance( ArrayList<Exercise> exercises ) {
        NewExerciseFragment fragment = new NewExerciseFragment( exercises );

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_exercise, container, false);
    }
}