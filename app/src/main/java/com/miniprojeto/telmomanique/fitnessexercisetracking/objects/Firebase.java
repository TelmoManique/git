package com.miniprojeto.telmomanique.fitnessexercisetracking.objects;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class Firebase {

    String TAG = "DEBUG_FIREBASE";
    private static Firebase firebase;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference goalsCollection;
    private CollectionReference statusCollection;
    private CollectionReference routinesCollection;
    private CollectionReference list_exercisesCollection;
    private ListenerRegistration registration=null;
    private User u;

    private Firebase(){
        setDb();
        setMAuth();
        if(isLoggedIn()){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            u = new User( currentUser.getUid().toString(),  currentUser.getEmail().toString());
        }
    }

    public boolean isLoggedIn(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
            return true;
        return false;
    }

    public void signOut(){
        mAuth.signOut();
        u = null;
    }

    //GETTES & SETTERS
    public User getUser(){
        return this.u;
    }
    public void setUser( User u ){
        this.u = u;
    }

    public FirebaseAuth getAuth(){
        return this.mAuth;
    }
    public void setMAuth(){
        this.mAuth = FirebaseAuth.getInstance();
    }

    public void setDb(){
        this.db = FirebaseFirestore.getInstance();
    }
    public FirebaseFirestore getDb(){
        return this.db;
    }

    public CollectionReference getGoalsCollection(){
        return db.collection("Goals");
    }

    public CollectionReference getStatusCollection(){
        return db.collection("Status");
    }

    public CollectionReference getRoutinesCollection(){
        return db.collection("Routines");
    }

    public CollectionReference getList_exercisesCollection(){
        return db.collection("List_Exercises");
    }

    public static Firebase getInstance(){
        if( firebase == null ){
            firebase = new Firebase();
        }
        return firebase;
    }
}
