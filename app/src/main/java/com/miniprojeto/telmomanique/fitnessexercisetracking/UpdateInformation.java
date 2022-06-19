package com.miniprojeto.telmomanique.fitnessexercisetracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Firebase;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.User;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateInformation extends AppCompatActivity {
    private String TAG = "DEBUG_UPDATEINFORMATION";

    private Firebase firebase;
    private User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_information);

        // Initialize Firebase Auth
        firebase = Firebase.getInstance();
        u = firebase.getUser();
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
        presentStatus();
    }// END onStart()

    private void presentStatus(){
        firebase.getStatusCollection().document( "users" ).collection( u.getId() ).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                u.setHeight( Integer.parseInt( document.get("height").toString()));
                                u.setWeight((int) Float.parseFloat( document.get("weight").toString()));
                            }
                            EditText weightView = (EditText) findViewById(R.id.editTextViewWeightInfo);
                            String temp = ""+ u.getWeight();
                            weightView.setText( temp );

                            EditText heightView = (EditText) findViewById(R.id.editTextViewHeightInfo);
                            temp = ""+ u.getHeight() ;
                            heightView.setText( temp );
                            Log.d(TAG, "presentStatus() done");
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    } // END presentStatus()

    public void onUpdateInformation( View view ){

        EditText weightView = (EditText) findViewById(R.id.editTextViewWeightInfo);
        u.setWeight((int) (Float.parseFloat( weightView.getText().toString() )));

        EditText heightView = (EditText) findViewById(R.id.editTextViewHeightInfo);
        u.setHeight((int) (Float.parseFloat( heightView.getText().toString() )));

        addStatus();
    } // END onSaveUpdates()

    private void addStatus(){

        Map<String, String> info = new HashMap<>();
        info.put( "height" , String.valueOf( u.getHeight()));
        info.put( "weight" , String.valueOf( u.getWeight()));

        String date = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss",  Calendar.getInstance().getTime() ).toString() ;

        firebase.getStatusCollection().document("users").collection( u.getId() )
                .document( date )
                .set( info )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText( UpdateInformation.this, "Added with success.", Toast.LENGTH_SHORT).show();
                        Intent loginPage = new Intent(UpdateInformation.this, Status.class);
                        startActivity(loginPage);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateInformation.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        Intent loginPage = new Intent(UpdateInformation.this, Status.class);
                        startActivity(loginPage);
                    }
                });
    } // END addStatus()
}