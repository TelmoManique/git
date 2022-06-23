package com.miniprojeto.telmomanique.fitnessexercisetracking.objects;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Routine {
    private User user;
    private String date; //YYYY-MM-DD HH:MM:SS
    private String location;
    private ArrayList<Exercise> exercises = new ArrayList<Exercise>();

    public void addExercise( Exercise e ){
        if( e != null)
            exercises.add( e );
    }
    public void removeExercise(int pos) {
        exercises.remove( pos );
    }

    public Map<String , Integer> getMuscleMap(){
        Map<String , Integer> muscleMap = new HashMap<>();
        for( Exercise e : exercises ){
            String muscleString = e.getMuscleGroup();
            if( !muscleMap.containsValue(e.getMuscleGroup()) )
                muscleMap.put( muscleString , 1 );
            else
                muscleMap.put( muscleString , muscleMap.get(muscleString)+1 );
        }
        return muscleMap;
    }
    public String getMostExercised(){
        String muscle = "Empty";
        int x = -1;
        for (Map.Entry<String, Integer> entry : getMuscleMap().entrySet()) {
            if( entry.getValue() > x ){
                muscle = entry.getKey();
                x = entry.getValue();
            }
        }
        return muscle;
    }

    public Map<String , Integer> getTypeMap(){
        Map<String , Integer> typeMap = new HashMap<>();
        for( Exercise e : exercises ){
            String typeString = e.getExerciseType();
            if( !typeMap.containsValue( e.getExerciseType() ))
                typeMap.put( typeString , 1 );
            else
                typeMap.put( typeString , typeMap.get(typeString)+1 );
        }
        return typeMap;
    }
    public String getMostType(){
        String type = "Empty";
        int x = -1;
        for (Map.Entry<String, Integer> entry : getTypeMap().entrySet()) {
            if( entry.getValue() > x ){
                type = entry.getKey();
                x = entry.getValue();
            }
        }
        return type;
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

    public ArrayList<Exercise> getExercises(){
        return  exercises;
    }

}
