package com.miniprojeto.telmomanique.fitnessexercisetracking.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miniprojeto.telmomanique.fitnessexercisetracking.R;


public class MoreStatsFragment extends Fragment {
    public MoreStatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_stats, container, false);
    }
}

//TODO https://www.youtube.com/watch?v=vhKtbECeazQ VERIFY MOST TYPE EXERCISES