package com.miniprojeto.telmomanique.fitnessexercisetracking.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Firebase;
import com.miniprojeto.telmomanique.fitnessexercisetracking.R;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.User;

import java.util.ArrayList;
import java.util.List;


public class WeightStatusFragment extends Fragment {
    private String TAG = "DEBUG_STATUS_WEIGHTFRAGMENT";
    private Firebase firebase;
    private User u;

    public WeightStatusFragment() {
        super(R.layout.fragment_general_status);
        firebase = Firebase.getInstance();
        u = firebase.getUser();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new GetInformation().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weight_status, container, false);
    }

    private void getWeight(){
        firebase.getStatusCollection().document( "users" ).collection( u.getId() ).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Float> weights = new ArrayList<Float>();
                            ArrayList<String> dates = new ArrayList<String>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                weights.add( Float.parseFloat( document.get( "weight" ).toString()));
                                dates.add(document.getId());
                            }
                            drawChart( weights, dates );
                            Log.d("TAG", "getWeight() success");
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    } //END getWeights()

    private void drawChart( ArrayList<Float> weights, ArrayList<String> dates ){

        LineChart lineChart = getActivity().findViewById(R.id.lineChart);


        List<Entry> entries = new ArrayList<Entry>();

        List<Float> weightsFinal =  weights;
        for( int i = 0 ; i < weightsFinal.size(); i++ ){
            entries.add( new Entry( i , weightsFinal.get( i ) ));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Weight");
        LineData lineData = new LineData(dataSet);
        lineData.setValueTextSize( 15 );

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //TODO FIX VIEWPORT
        lineChart.setVisibleXRange(10, 10);
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresh
    } // END drawChart()

    //ASYNC CLASS TO ENSURE IT ALWAYS GETS VALUES
    private class GetInformation extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getWeight();
            }

            @Override
            protected Void doInBackground(Void... voids) {
            return null;
        }
    } // END class GetInformation
}