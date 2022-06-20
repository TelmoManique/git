package com.miniprojeto.telmomanique.fitnessexercisetracking.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miniprojeto.telmomanique.fitnessexercisetracking.R;


public class RoutinesListFragment extends Fragment {

    public RoutinesListFragment() {
        // Required empty public constructor
    }
    public static RoutinesListFragment newInstance() {
        RoutinesListFragment fragment = new RoutinesListFragment();

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
        return inflater.inflate(R.layout.fragment_routines_list, container, false);
    }
}