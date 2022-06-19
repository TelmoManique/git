package com.miniprojeto.telmomanique.fitnessexercisetracking.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Routine {
    private User user;
    private String date; //YYYY-MM-DD HH:MM:SS
    private String location;
    private ArrayList<Exercise> exercises = new ArrayList<Exercise>();

    public void addExercise( Exercise e ){
        if( e != null)
            exercises.add( e );
    }

    //GETTSER & SETTERS

    public void setUser( User u){
        if( u != null)
            this.user = u;
    }
    public User getUser(){
        return this.user;
    }

    public void setDate( String date ){
        if( date != null)
            this.date = date;
    }
    public String getDate(){
        return this.date;
    }

    public void setLocation( String location){
        if( location != null)
            this.location = location;
    }
    public String getLocation(){
        return this.location;
    }

    public Collection<Exercise> getExercises(){
        return  Collections.unmodifiableList( exercises );
    }
}
