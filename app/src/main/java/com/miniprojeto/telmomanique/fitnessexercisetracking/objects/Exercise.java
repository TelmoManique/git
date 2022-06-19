package com.miniprojeto.telmomanique.fitnessexercisetracking.objects;

public class Exercise {

    private String name;
    private String image;
    private String description;
    private String muscleGroup;
    private String exerciseType;

    private int reps;
    private int weight; //Kg
    private int time; //seconds

    public  void setName( String name ){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

    public void setImage( String image ){
        this.image = image;
    }
    public String getImage(){
        return this.image;
    }

    public void setDescription( String description ){
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }

    public void setMuscleGroup( String muscleGroup ){
        this.muscleGroup = muscleGroup;
    }
    public String getMuscleGroup(){
        return this.muscleGroup;
    }

    public void setExerciseType( String exerciseType ){
        this.exerciseType = exerciseType;
    }
    public String getExerciseType(){
        return this.exerciseType ;
    }

    public void setReps( int reps ){
        this.reps = reps;
    }
    public int getReps(){
        return this.reps;
    }

    public void setWeight( int weight ){
        this.weight = weight;
    }
    public int getWeight(){
        return this.weight;
    }

    public void setTime( int time ){
        this.time = time;
    }
    public int getTime(){
        return this.time;
    }
}
