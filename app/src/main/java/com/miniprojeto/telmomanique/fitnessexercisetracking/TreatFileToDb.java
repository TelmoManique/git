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
    private Map<String, String> exceptions = new HashMap<>();

    public void chooseFile(AssetManager assets) {

        exceptions.put( "Calf Raise" , "https://qph.cf2.quoracdn.net/main-qimg-b298f115722e37446250d8eac5a656d2" );
        exceptions.put( "Deficit Squat" , "https://www.boostupfitness.com/wp-content/uploads/2020/12/deficit-deadlift.gif" );
        exceptions.put( "Goblet Squat", "https://177d01fbswx3jjl1t20gdr8j-wpengine.netdna-ssl.com/wp-content/uploads/2019/05/DB-Goblet-Squat-opt.gif" );
        exceptions.put( "Leg Raise", "https://www.byrdie.com/thmb/zQQn6n8nysIXsBMIEy9Z4L2FOgA=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/leglifts-1584ae42d00d499fbd5d80a799563069.gif" );
        exceptions.put( "Plank", "https://mir-s3-cdn-cf.behance.net/project_modules/max_1200/a8048480486129.5ce2e67ba3247.gif" );
        exceptions.put( "Side Plank", "https://177d01fbswx3jjl1t20gdr8j-wpengine.netdna-ssl.com/wp-content/uploads/2019/06/Side-Plank-Hip-Dips.gif" );
        exceptions.put( "Situp and Throw", "https://assets.myworkouts.io/exercises-media/xszEm92uvgvmxMeZv/medicine_ball_chest_throw_sit-up_(on_wall)_female_v3_gif_capoff.gif" );
        exceptions.put( "Standing Leg Lift", "https://goefem.com/wp-content/uploads/2021/11/1_Proy7WDS3P3Ji-ZSUMuBKg-1.gif" );
        exceptions.put( "Straightup Situp", "https://jensinkler.com/wp-content/uploads/2019/06/Bodyweight-Straight-Legged-Sit-Up.gif" );
        exceptions.put( "Superman", "https://177d01fbswx3jjl1t20gdr8j-wpengine.netdna-ssl.com/wp-content/uploads/2019/06/Superman-Swims-1.gif" );
        exceptions.put( "Tricep Kick-Back", "https://cdn.shopify.com/s/files/1/0250/0362/2496/files/393.gif?v=1644657822" );
        exceptions.put( "Weighted Jumping Jacks", "https://media1.popsugar-assets.com/files/thumbor/8msgpY2pOl_UquCk7JtjI3LTYcg/fit-in/1024x1024/filters:format_auto-!!-:strip_icc-!!-/2020/08/26/840/n/1922729/259ba519347128d9_WEIGHTED_JUMPING_JACKS/i/Move-3-Weighted-Jumping-Jacks.GIF" );

        ArrayList<Exercise> exercises = new ArrayList<Exercise>();
        BufferedReader reader = null;
        ArrayList<String> countMuscleGroup = new ArrayList<>();
        ArrayList<String> countMuscleType = new ArrayList<>();
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
                if(exceptions.containsKey( e.getName()))
                    e.setImage(exceptions.get(e.getName()));
                else
                    e.setImage( infos[5] );
                exercises.add( e );

                if( !countMuscleGroup.contains( e.getMuscleGroup()))
                    countMuscleGroup.add(e.getMuscleGroup());
                if( !countMuscleType.contains( e.getExerciseType()))
                    countMuscleType.add( e.getExerciseType());

               /* Log.d( "DEBUG_R" , "" + i);
                Log.d( "DEBUG_R" , "getName" + e.getName());
                Log.d( "DEBUG_R" , "getExerciseType " + e.getExerciseType());
                Log.d( "DEBUG_R" , "getMuscleGroup " + e.getMuscleGroup());
                Log.d( "DEBUG_R" , "getImage: " + e.getImage());
               // Log.d("DEBUG_Read" ,   "mLine " +mLine );
                */
            }
            Log.d("DEBUG_R", "chooseFile: countMuscleGroup " + countMuscleGroup.size());
            Log.d("DEBUG_R", "chooseFile: countMuscleType " + countMuscleType.size());
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


        /*for( Exercise e : exercises ) {
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
        */
    }
}
