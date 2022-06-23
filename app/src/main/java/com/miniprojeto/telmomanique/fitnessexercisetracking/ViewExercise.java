package com.miniprojeto.telmomanique.fitnessexercisetracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.felipecsl.gifimageview.library.GifImageView;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Exercise;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Firebase;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ViewExercise extends AppCompatActivity {
    private String TAG = "DEBUG_VIEWEXERCISE";

    private Firebase firebase;
    private Exercise e;

    private GifImageView gifView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);

        firebase = Firebase.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        if(!firebase.isLoggedIn()){
            Intent loginPage = new Intent(this, Login.class);
            startActivity(loginPage);
            return;
        }
        e = new Exercise();
        e.setName( getIntent().getStringExtra("name").toString());
        e.setMuscleGroup( getIntent().getStringExtra("muscleGroup"));
        e.setExerciseType( getIntent().getStringExtra("type"));
        e.setImage(getIntent().getStringExtra("image"));
        Log.d(TAG, "onStart: " + e.getName());

        TextView namView = findViewById(R.id.textViewName);
        TextView muscleView = findViewById(R.id.textViewMuscleExercisedInfo);
        TextView typeView = findViewById(R.id.textViewTypeInfo);

        namView.setText(e.getName());
        muscleView.setText(e.getMuscleGroup());
        typeView.setText(e.getExerciseType());

        Log.d(TAG, "onStart: " + e.getImage());

       gifView = findViewById(R.id.imageView);

       new RetriveBtyteArray( ).execute( e.getImage() );

       gifView.startAnimation();
    }

    public class RetriveBtyteArray extends AsyncTask<String, Void, byte[]> {

        @Override
        protected byte[] doInBackground(String... strings) {
            try {
                URL url = new URL( strings[0] );

                HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();

                if ( urlCon.getResponseCode() == 200 ){
                    BufferedInputStream in = new BufferedInputStream(urlCon.getInputStream());
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    int nRead;

                    byte[] data = new byte[1024];
                    while ( (nRead = in.read(data, 0, data.length)) != -1){
                        buffer.write(data, 0, nRead);
                    }
                    buffer.flush();
                    return buffer.toByteArray();
                }
            } catch (MalformedURLException malformedURLException) {
                Log.d(TAG, "doInBackground: malformed");
                malformedURLException.printStackTrace();
            } catch (IOException ioException) {
                Log.d(TAG, "doInBackground: other");
                ioException.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            if( bytes != null){
                gifView.setBytes(bytes);
            }
            Log.d(TAG, "onPostExecute: null bytes");
        }

        /*@Override
        protected void onPostExecute(ByteArrayOutputStream byteArrayOutputStream) {
            super.onPostExecute(byteArrayOutputStream);
            gifView.setBytes(byteArrayOutputStream);
        }

         */
    }
}