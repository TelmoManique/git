package com.miniprojeto.telmomanique.fitnessexercisetracking.recyclers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miniprojeto.telmomanique.fitnessexercisetracking.R;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Routine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RoutineListRecyclerViewAdapter extends RecyclerView.Adapter<RoutineListRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Routine> routineList;
    private RoutineListRecyclerViewInterface routineInterface;

    public RoutineListRecyclerViewAdapter(Context context, ArrayList<Routine> routineList , RoutineListRecyclerViewInterface routineInterface ){
        this.context = context;
        this.routineList = routineList;
        this.routineInterface = routineInterface;
    }
    @NonNull
    @Override
    public RoutineListRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from( context );
        View view = inflater.inflate(R.layout.recycler_view_routine_row, parent, false);
        return new RoutineListRecyclerViewAdapter.MyViewHolder( view, routineInterface );
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineListRecyclerViewAdapter.MyViewHolder holder, int position) {
        Log.d("DEBUG_ADAPTER", "onBindViewHolder: "+ routineList.get( position ).getDate() );
        holder.dateView.setText( routineList.get( position ).getDate() );
        holder.muscleView.setText( routineList.get( position ).getMostExercised());
        holder.typeView.setText( routineList.get( position ).getMostType() );
        //TODO TRANSFORM IN GEOLOCATION AND GET CITY , STREET
        String location = routineList.get( position ).getLocation();
        String []ll = location.split(",");
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(Double.parseDouble(ll[0]), Double.parseDouble(ll[1]), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.locationView.setText( addresses.get(0).getLocality());
    }

    @Override
    public int getItemCount() {
        return routineList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dateView, muscleView, typeView, locationView;
        public MyViewHolder(@NonNull View itemView, RoutineListRecyclerViewInterface routineInterface) {
            super(itemView);
            dateView = itemView.findViewById(R.id.textViewRoutineDateInfo);
            muscleView = itemView.findViewById(R.id.textViewRoutineMuscleFocusInfo);
            typeView = itemView.findViewById(R.id.textViewRoutineExeTypeInfo);
            locationView = itemView.findViewById(R.id.textViewRoutineLocationInfo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( routineInterface != null ){
                        int pos = getAdapterPosition();
                        if( pos != RecyclerView.NO_POSITION)
                            routineInterface.onRoutineClick(pos);
                    }
                }
            });
        }
    }
}
