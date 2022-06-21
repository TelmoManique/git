package com.miniprojeto.telmomanique.fitnessexercisetracking.recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miniprojeto.telmomanique.fitnessexercisetracking.R;
import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Exercise;

import java.util.ArrayList;

public class ExerciseInRoutineRecyclerViewAdapter extends RecyclerView.Adapter<ExerciseInRoutineRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Exercise> exerciseList;
    private ExerciseInRoutineRecyclerViewInterface exeInRowRycyclerViewInterface;

    public ExerciseInRoutineRecyclerViewAdapter(Context context , ArrayList<Exercise> exerciseList, ExerciseInRoutineRecyclerViewInterface exeInRowRwInterface ){
        this.context = context;
        this.exerciseList = exerciseList;
        this.exeInRowRycyclerViewInterface = exeInRowRwInterface;
    }
    @NonNull
    @Override
    public ExerciseInRoutineRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_routine_exercise_list_row , parent, false);
        return new ExerciseInRoutineRecyclerViewAdapter.MyViewHolder( view , exeInRowRycyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseInRoutineRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.nameView.setText(exerciseList.get(position).getName());
        holder.repView.setText(exerciseList.get(position).getReps());
        holder.weightView.setText(exerciseList.get(position).getWeight());
        holder.timeView.setText(exerciseList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameView, repView, weightView, timeView;

        public MyViewHolder(@NonNull View itemView, ExerciseInRoutineRecyclerViewInterface exeInRowRycyclerViewInterface) {
            super(itemView);

            nameView = itemView.findViewById(R.id.textViewExerciseName);
            repView = itemView.findViewById(R.id.textViewRepsInfo);
            weightView = itemView.findViewById(R.id.textViewWeightInfo);
            timeView = itemView.findViewById(R.id.textViewTimeInfo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( exeInRowRycyclerViewInterface != null ){
                        int pos = getAdapterPosition();
                        if( pos != RecyclerView.NO_POSITION)
                            exeInRowRycyclerViewInterface.onExerciseClick(pos);
                    }
                }
            });
        }
    } // END class MyViewHolder
}
