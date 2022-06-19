package com.miniprojeto.telmomanique.fitnessexercisetracking.objects;

import android.util.Log;

public class User {

    private String id;
    private String email;
    private float weight = -1; //g
    private int height = -1; //cm

    public User(){}

    public User( String id, String email ){
        setId( id );
        setEmail( email );
    }

    public void setId(String id) {
        if (id == null) {
            Log.d("DEBUG_User", "setId null");
            return;
        }
        if (id.compareTo("") == 0) {
            Log.d("DEBUG_User", "setId empty");
            return;
        }

        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    public void setEmail( String email){
        if (email == null) {
            Log.d("DEBUG_User", "setId null");
            return;
        }
        if (email.compareTo("") == 0) {
            Log.d("DEBUG_User", "setId empty");
            return;
        }

        this.email = email;
    }

    public String getEmail(){
        return this.email;
    }

    public void setWeight( float weight){
        if( weight >0 )
            this.weight = weight;
    }
    public float getWeight(){
        if( weight <0 )
            return 0;
        return this.weight;
    }

    public void setHeight( int height ){
        if( height > 0){
            this.height = height;
        }
    }
    public int getHeight(){
        return height < 0 ? 0 : height;
    }
}
