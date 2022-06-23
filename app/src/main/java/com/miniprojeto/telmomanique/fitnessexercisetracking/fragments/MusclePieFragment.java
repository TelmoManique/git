package com.miniprojeto.telmomanique.fitnessexercisetracking.fragments;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miniprojeto.telmomanique.fitnessexercisetracking.R;
import com.miniprojeto.telmomanique.fitnessexercisetracking.TreatFileToDb;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Exercise;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Firebase;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Routine;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MusclePieFragment extends Fragment {
    private String TAG = "DEBUG_STATUS_MORESTATSFRAGMENT";

    private Firebase firebase;
    private User u;

    private ArrayList<Routine> routines = new ArrayList<Routine>();

    public MusclePieFragment() {
        firebase = Firebase.getInstance();
        u = firebase.getUser();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new MusclePieFragment.GetInformation().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_stats, container, false);
    }

    private void getRoutines(){
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
                            Log.d(TAG, "onComplete: ");
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void parseRoutines( ArrayList<QueryDocumentSnapshot> routines ){
        for( QueryDocumentSnapshot documents : routines ){
            Routine r = new Routine();
            r.setDate( documents.getId().toString() );

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
                                drawMuscleGroupChart();
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
    } // END addRoutine()

    private void drawMuscleGroupChart(){
        int total = 0;
        Map<String , Integer> muscleMap = new HashMap<>();
        for(Routine r : routines){
            for (Map.Entry<String, Integer> entry : r.getMuscleMap().entrySet()) {
                total += entry.getValue();
                if( muscleMap.containsKey(entry.getKey()) )
                    muscleMap.put( entry.getKey() , muscleMap.get(entry.getKey()) + entry.getValue());
                else
                    muscleMap.put( entry.getKey() ,  entry.getValue());
            }
        }

        PieChart musclePie = getActivity().findViewById(R.id.pieChartMuscleGroup);

        List<PieEntry> entries = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Integer> entry : muscleMap.entrySet()){
            entries.add( new PieEntry( entry.getValue() , entry.getKey()));
        }

        PieDataSet set = new PieDataSet(entries, "Most Exercised Muscle Group");
        PieData data = new PieData(set);

        Legend l= musclePie.getLegend();
        l.setEnabled(false);

        set.setSliceSpace(8);
        set.setUsingSliceColorAsValueLineColor(true);
        set.getValueLineColor();
        musclePie.setUsePercentValues(true);

        //TODO STYLE
        data.setValueTextColor(Color.BLACK);
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setValueLinePart1OffsetPercentage(10.f);
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(20);
        musclePie.setEntryLabelColor(Color.BLACK);


        musclePie.setData(data);
        musclePie.invalidate(); // refresh
    }

    //ASYNC CLASS TO ENSURE IT ALWAYS GETS VALUES
    private class GetInformation extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getRoutines();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    } // END class GetInformation
}

//TODO https://www.youtube.com/watch?v=vhKtbECeazQ VERIFY MOST TYPE EXERCISES