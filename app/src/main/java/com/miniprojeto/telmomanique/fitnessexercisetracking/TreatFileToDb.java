package com.miniprojeto.telmomanique.fitnessexercisetracking;

import android.content.res.AssetManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Exercise;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Firebase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TreatFileToDb {

    private Firebase firebase = Firebase.getInstance();
    private String txtPath = "textFile.txt";

    public void chooseFile(AssetManager assets) {
        ArrayList<Exercise> exercises = new ArrayList<Exercise>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(assets.open( txtPath )));
            String mLine;
            int i = 0;
            while ((mLine = reader.readLine()) != null) {
                i++;

                char find = '"';
                mLine = mLine.replace("/", "&");
                mLine = mLine.replace( find, '/');

                while ( mLine.indexOf('/') > 0 ) {
                    int x = mLine.indexOf('/');
                    int y = mLine.indexOf('/' , x+1 );
                    if( y > 0){
                        String tempBefore = mLine.substring(0 , x);
                        String tempAfter = mLine.substring( y+1 );
                        String sub = mLine.substring(x ,y+1);
                        String []subs = sub.split(",");
                        subs[0] = subs[0].replace("/", " ");
                        mLine = tempBefore + subs[0] + tempAfter;
                    }
                    else
                        mLine = mLine.replace("/" ,"&");

                }

                mLine = mLine.trim();
                String []infos = mLine.split(",");
                if( infos.length < 4 )
                    continue;

                int x = infos[5].indexOf('(');
                int y = infos[5].indexOf(')');
                if( x > 0 ){
                    infos[5] = infos[5].substring( x+1, y);
                    infos[5] = infos[5].replace("&", "/");
                }

                Exercise e = new Exercise();
                e.setName( infos[0] );
                e.setExerciseType( infos[2] );
                e.setMuscleGroup( infos[3] );
                e.setImage( infos[5] );
                exercises.add( e );

               /* Log.d( "DEBUG_R" , "" + i);
                Log.d( "DEBUG_R" , "getName" + e.getName());
                Log.d( "DEBUG_R" , "getExerciseType " + e.getExerciseType());
                Log.d( "DEBUG_R" , "getMuscleGroup " + e.getMuscleGroup());
                Log.d( "DEBUG_R" , "getImage: " + e.getImage());

               // Log.d("DEBUG_Read" ,   "mLine " +mLine );
                */
            }
        } catch (IOException e) {
            Log.d("DEBUG_Read" ,   "Exception" );
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }


        for( Exercise e : exercises ) {
            Map<String, String> exeDetails = new HashMap<String, String>();
            exeDetails.put( "image" , e.getImage() );
            exeDetails.put("exerciseType" , e.getExerciseType());
            exeDetails.put("muscleGroup" , e.getMuscleGroup());


            firebase
                    .getList_exercisesCollection()
                    .document( e.getName() )
                    .set(exeDetails)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TreatFileToDb", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TreatFileToDb", "Error writing document", e);
                        }
                    });
        }
    }
}
