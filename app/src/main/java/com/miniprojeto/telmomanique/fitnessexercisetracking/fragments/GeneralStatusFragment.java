package com.miniprojeto.telmomanique.fitnessexercisetracking.fragments;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Firebase;
import com.miniprojeto.telmomanique.fitnessexercisetracking.R;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.User;

public class GeneralStatusFragment extends Fragment {
    private String TAG= "DEBUG_GENERALSTATUSFRAGMENT";

    private Firebase firebase;
    private User u;

    public GeneralStatusFragment(){
        super(R.layout.fragment_general_status);
        firebase = Firebase.getInstance();
        u= firebase.getUser();
        new GetInformation().execute();
    }

    public void getTotalExercised( ){
        firebase.getRoutinesCollection().document( "users" ).collection( u.getId() ).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int total;
                        if (task.isSuccessful()) {
                            int i= 0;
                            String dateS = "No Data Found";
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dateS = document.getId();
                                getTotalExercise( document.getId() );
                                i++;
                            }
                            if(i == 0){
                                TextView date = (TextView) getActivity().findViewById( R.id.textViewLastTimeExercisedInfo );
                                date.setText( "No Data Found" );
                                TextView time = (TextView) getActivity().findViewById( R.id.textViewTotalExerciseTimeInfo );
                                time.setText( "No Data Found" );
                                TextView lifted = (TextView) getActivity().findViewById( R.id.textViewTotalLifetedWeightInfo );
                                lifted.setText( "No Data Found" );
                                Log.d("TAG", "getTotalExercised() success");
                            }
                            TextView date = (TextView) getActivity().findViewById( R.id.textViewLastTimeExercisedInfo );
                            date.setText( dateS );
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }// END getTotalExercised()

    private void getTotalExercise(String id){
        firebase.getRoutinesCollection().document( "users" ).collection( u.getId() ).document( id ).collection("exercises").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        float totalLifted  = 0;
                        float totalTime  = 0;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                int weight = Math.toIntExact(document.getLong("weight"));
                                int reps = Integer.parseInt( document.get("reps").toString() );
                                int time = Integer.parseInt( document.get("time").toString() );
                                Log.d(TAG, "onComplete: totallif B" + totalLifted);

                                if (weight > 0)
                                    totalLifted += ( weight  * reps);
                                totalTime += time;
                            }
                            totalTime /=  60;
                            Log.d(TAG, "onComplete: totallif A" + totalLifted);
                            TextView time = (TextView) getActivity().findViewById( R.id.textViewTotalExerciseTimeInfo );
                            time.setText( "" + totalTime );
                            TextView lifted = (TextView) getActivity().findViewById( R.id.textViewTotalLifetedWeightInfo );
                            lifted.setText( "" + totalLifted );
                            Log.d("TAG", "getTotalExercise() success");
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    } //END getTotalExercise()

    //ASYNC CLASS TO ENSURE IT ALWAYS GETS VALUES
    private class GetInformation extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getTotalExercised();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
