package com.miniprojeto.telmomanique.fitnessexercisetracking;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Exercise;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Firebase;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Routine;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NewRoutine extends AppCompatActivity {
    private String TAG  = "DEBUG_NEWROUTINE";
    private String TAG_PERM = TAG + "_PERMS";
    private String TAG_LOC = TAG + "_LOCATION";

    private static final int MY_PERMISSIONS_REQUEST = 99;
    private LocationManager locManager= null;
    private LocationProvider locProvider= null;
    private Location localizacaoAtual;

    private Firebase firebase;
    private User u = new User();
    private Routine r = new Routine();
    private Collection<Exercise> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_routine);

        // Initialize Firebase Auth
        firebase = Firebase.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        if( !firebase.isLoggedIn() ){
            Intent loginPage = new Intent(this, Login.class);
            startActivity(loginPage);
            return;
        }

        locManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Boolean jaTemPermissoes= pedirPermissoesSeNecessario(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE });

        if (jaTemPermissoes)
            iniciarLocalizacao(); //se tiver todas as permissões então pode começar a obter a localização

        u = firebase.getUser();

        //TODO ADD buttonAddExercise AND FOLLOWING POP-UP/TEXTBOX
        /*
            TEXTBOX:
                EXERCISE - (EITHER BY DROP LIST OR NAME)
                RESP - INT
                WEIGHT - INT KG
                TIME - INT SEC
         */
    } // END onStart()

    //Gets all existing exercises in the DB
    private void getExercisesList(){
        ArrayList<Exercise> exercises = new ArrayList<Exercise>();

        firebase.getList_exercisesCollection().get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Exercise e = new Exercise();
                                e.setName( document.getId() );
                                e.setImage( document.getString( "image" ) );
                                e.setDescription( document.getString( "description" ) );
                            }
                            setExercisesList( Collections.unmodifiableList( exercises ) );
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    } // END getExercisesList()

    private void setExercisesList( Collection<Exercise> exercises ){
        this.exercises = exercises;
    }

    public void onNewExercise( View view){
        //TODO GET INFO FROM POP-UP/TEXTBOX
        String exercise = "Dumbell";
        checkExerciseExist( exercise );
    } // END onNewExercise()

    private void checkExerciseExist( String exercise ){

        firebase.getList_exercisesCollection().get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if( document.getId().compareTo( exercise ) == 0 ){
                                    String image = document.getString( "image" );
                                    String description = document.getString( "description" );
                                    addExercise( exercise , image, description);
                                    return;
                                }
                            }
                            //TODO IN CASE THE EXERCISE DOSEN'T EXIST WARN
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    } // END checkExerciseExist()

    private void addExercise( String exercise, String image, String description ){
        //TODO CLOSE POP-UP/TEXTBOX

        Exercise e = new Exercise();
        e.setName( (r.getExercises().size() + 1) + " - " + exercise );
        e.setImage( image );
        e.setDescription( description );

        //TODO GET INFO FROM VIEW
        int reps;
        int weight; //g
        int time; //seconds

        ////
        reps = 10;
        weight = 10;
        time = 10;
        ////

        e.setReps( reps );
        e.setWeight( weight );
        e.setTime( time );

        r.addExercise( e );

        /*
            for(Exercise ex : r.getExercises()){
                Log.d("DEBUG_NEWROUTINE_addExercise", "r.exList.name: " + ex.getName() );
            }
        */

        //TODO ADD EXERCISE TO THE SCREEN
    } // END addExercise()

    public void onSaveRoutine( View view ){
        r.setUser( u );
        String date = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss",  Calendar.getInstance().getTime() ).toString() ;
        r.setDate( date );
        r.setLocation( localizacaoAtual.getLatitude() + ", " +localizacaoAtual.getLongitude());

        Log.d(TAG , "r.date: " + r.getDate());
        Log.d(TAG , "r.u.id : " + r.getUser().getId());
        Log.d(TAG , "r.location: " + r.getLocation());
        addRoutine();
    } // END onSaveRoutine()

    private void addRoutine(){

        for (Exercise e: r.getExercises()) {
            Map<String, Object> exercise = new HashMap<>();
            exercise.put("resp", e.getReps() );
            exercise.put("weight", e.getWeight() );
            exercise.put("time", e.getTime() );

            firebase.getRoutinesCollection().document("users").collection( u.getId() )
                    .document( r.getDate())
                    .collection("exercises")
                    .document( e.getName() )
                    .set( exercise )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            Toast.makeText(NewRoutine.this, "Added with success.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                            Toast.makeText(NewRoutine.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            Intent myRoutinesPage = new Intent( NewRoutine.this, MyRoutines.class);
                            startActivity(myRoutinesPage);
                        }
                    });

            firebase.getRoutinesCollection().document("users").collection( u.getId() )
                    .document( r.getDate())
                    .collection("exercises")
                    .document( e.getName() )
                    .update( "location" , r.getLocation()  )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            Toast.makeText(NewRoutine.this, "Added with success.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                            Toast.makeText(NewRoutine.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            Intent myRoutinesPage = new Intent( NewRoutine.this, MyRoutines.class);
                            startActivity(myRoutinesPage);
                        }
                    });
        }
        Intent myRoutinesPage = new Intent( NewRoutine.this, MyRoutines.class);
        startActivity(myRoutinesPage);
    } // END addRoutine()

    private void setLocation( Location localizacaoAtual ){
        Log.d(TAG, "New Coords: " + localizacaoAtual.toString());
        this.localizacaoAtual = localizacaoAtual;
    }

    /* ________________________LOCATION LOCATION PERMISSIONS_________________________________*/
    private Boolean pedirPermissoesSeNecessario(String[] permissions) {
        Log.d(TAG_PERM, "pedirPermissoesSeNecessario");
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                //A permissão ainda não tinha sido dada anteriormente
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    MY_PERMISSIONS_REQUEST);
            return Boolean.FALSE; //devolve FALSE se a app ainda não tiver todas as permissões
        }
        else
            return Boolean.TRUE; //devolve TRUE se a app não precisar de pedir permissões (porque já as tem)
    } // END pedirPermissoesSeNecessario()

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Boolean temTodasPermissoes=Boolean.TRUE;
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            for (int j = 0; j < grantResults.length; j++) {
                if ( grantResults[j] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG_PERM, "O utilizador deu a permissão: " +
                            permissions[j]);
                } else {
                    temTodasPermissoes=Boolean.FALSE;
                    Log.d(TAG_PERM, "Ooopsss. O utilizador não deu a permissão: "
                            + permissions[j]);
                }
            }
            if (temTodasPermissoes) {
                Log.d(TAG_PERM, "Todas as permissões foram dadas");
                iniciarLocalizacao();
            }

        }
    } // END onRequestPermissionsResult()

    /* ________________________LOCATION LOCATION PERMISSIONS_________________________________*/
    @SuppressLint("MissingPermission")
    public void iniciarLocalizacao() {
        if (locProvider == null)
            locProvider = locManager.getProvider(LocationManager.GPS_PROVIDER);
        final boolean gpsEnabled =
                locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            // Build an alert dialog here that requests that the user enable
            // the location services, then when the user clicks the "OK" button,
            Context context = getApplicationContext();
            CharSequence text = "Please activate your GPS";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                10000, // 10-second interval.
                10, // 10 meters.
                listener);
    } // END iniciarLocalizacao()

    private final LocationListener listener = new LocationListener() {
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG_LOC, "onStatusChanged");
        }
        public void onProviderEnabled(String provider) {
            Log.d(TAG_LOC, "onProviderEnabled");
        }
        public void onProviderDisabled(String provider) {
            Log.d(TAG_LOC, "onProviderDisabled");
        }
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG_LOC, "New Coords");
            localizacaoAtual = location;
        }
    }; // END LocationListener
}