package com.miniprojeto.telmomanique.fitnessexercisetracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miniprojeto.telmomanique.fitnessexercisetracking.fragments.GeneralStatusFragment;
import com.miniprojeto.telmomanique.fitnessexercisetracking.fragments.MusclePieFragment;
import com.miniprojeto.telmomanique.fitnessexercisetracking.fragments.WeightStatusFragment;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Firebase;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.User;

//TODO FIX FRAMEACTIVITY BACK BUTTON
//TODO ADD MORE STATS
public class Status extends AppCompatActivity {
    private String TAG = "DEBUG_STATUS";

    private Firebase firebase;
    private User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        // Initialize Firebase Auth
        firebase = Firebase.getInstance();

        if( findViewById(R.id.fragment_container_view_status) != null ){
            if (savedInstanceState != null){
                return;
            }

            GeneralStatusFragment generalFragment = new GeneralStatusFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container_view_status, generalFragment)
                    .commit();

            Button generalInfo = findViewById(R.id.buttonGeneralInfo);
            generalInfo.setClickable( false );
        }
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
        getUserInfo();
    }// END onStart()

    private void getUserInfo(){
        firebase.getStatusCollection().document( "users" ).collection( u.getId() ).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                u.setHeight( Integer.parseInt( document.get("height").toString()));
                                u.setWeight( Float.parseFloat( document.get("weight").toString()));
                            }
                            presentWeightHeightBMI();
                            Log.d(TAG, "getUserInfo() done");
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    } // END getUserInfo()

    private void presentWeightHeightBMI(){
        firebase.getStatusCollection().document( "users" ).collection( u.getId() ).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                u.setHeight( Integer.parseInt( document.get("height").toString()));
                                u.setWeight( Float.parseFloat( document.get("weight").toString()));
                            }
                            TextView weightView = (TextView) findViewById(R.id.textViewWeightInfo);
                            String temp = "" + u.getWeight();
                            weightView.setText( temp );

                            TextView heightView = (TextView) findViewById(R.id.textViewHeightInfo);
                            temp = "" + u.getHeight()*0.01;
                            heightView.setText( temp );

                            float bmi = (float) (u.getWeight() / Math.pow( u.getHeight() * 0.01 , 2 ));
                            TextView bmiView = (TextView) findViewById(R.id.textViewBMIInfo);
                            temp = ""+ bmi;
                            bmiView.setText( temp );

                            if( bmi <= 18.5 ){
                                bmiView.setBackgroundColor( Color.parseColor( "#00ffff" ));
                            }
                            else if(  18.5 < bmi  && bmi <= 25 ){
                                bmiView.setBackgroundColor( Color.parseColor( "#00ff00" ));
                            }
                            else if(  25 < bmi  && bmi <= 30 ){
                                bmiView.setBackgroundColor( Color.parseColor( "#ffff00" ));
                            }
                            else if(  30 < bmi ){
                                bmiView.setBackgroundColor( Color.parseColor( "#b45f06" ));
                            }
                            else{
                                bmiView.setBackgroundColor( Color.parseColor( "#980000" ));
                            }
                            Log.d(TAG, "presentWeightHeightBMI() done");
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }// END pressentWeightHeightBMI

    public void onWeight( View view ){
        if( findViewById(R.id.fragment_container_view_status) != null ) {
            WeightStatusFragment weightFragment = new WeightStatusFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_view_status, weightFragment)
                    .setReorderingAllowed(true)
                    .commit();

            Button weightButton = findViewById(R.id.buttonWeightProg);
            Button generalInfo = findViewById(R.id.buttonGeneralInfo);
            Button moreButton = findViewById(R.id.buttonMoreStat);
            weightButton.setClickable( false );
            generalInfo.setClickable( true );
            moreButton.setClickable( true );

        }
    } // END onWeight( View view )

    public void onGeneralInfo( View view ){
        if( findViewById(R.id.fragment_container_view_status) != null ){

            GeneralStatusFragment generalFragment = new GeneralStatusFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view_status, generalFragment)
                    .commit();

            Button weightButton = findViewById(R.id.buttonWeightProg);
            Button generalInfo = findViewById(R.id.buttonGeneralInfo);
            Button moreButton = findViewById(R.id.buttonMoreStat);
            weightButton.setClickable( true );
            generalInfo.setClickable( false );
            moreButton.setClickable( true );
        }
    } // END onGeneralInfo( View view )

    public void onMoreStats( View view ){
        if( findViewById(R.id.fragment_container_view_status) != null ){

            MusclePieFragment moreStatsFragment = new MusclePieFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view_status, moreStatsFragment)
                    .commit();

            Button weightButton = findViewById(R.id.buttonWeightProg);
            Button generalInfo = findViewById(R.id.buttonGeneralInfo);
            Button moreButton = findViewById(R.id.buttonMoreStat);
            weightButton.setClickable( true );
            generalInfo.setClickable( true );
            moreButton.setClickable( false );
        }
    } // END onMoreStats( View view )

    public void onUpdateInfo( View view ){
        //TODO USE A POP-UP/MESSAGEBOX INSTEAD
        Intent updatePage = new Intent(this, UpdateInformation.class);
        startActivity(updatePage);
    }
}