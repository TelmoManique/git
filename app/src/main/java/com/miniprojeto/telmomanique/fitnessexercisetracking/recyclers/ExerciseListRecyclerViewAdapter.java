package com.miniprojeto.telmomanique.fitnessexercisetracking.recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miniprojeto.telmomanique.fitnessexercisetracking.objects.Exercise;
import com.miniprojeto.telmomanique.fitnessexercisetracking.R;

import java.util.ArrayList;

public class ExerciseListRecyclerViewAdapter extends RecyclerView.Adapter<ExerciseListRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Exercise> exerciseList;
    private ExerciseListRecyclerViewInterface exerciseInterface;

    public ExerciseListRecyclerViewAdapter(Context context , ArrayList<Exercise> exerciseList, ExerciseListRecyclerViewInterface exerciseInterface ){
        this.context = context;
        this.exerciseList = exerciseList;
        this.exerciseInterface = exerciseInterface;
    }
    @NonNull
    @Override
    public ExerciseListRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_exercise_list_row , parent, false);
        return new ExerciseListRecyclerViewAdapter.MyViewHolder( view , exerciseInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseListRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.nameView.setText(exerciseList.get(position).getName());
        holder.typeView.setText(exerciseList.get(position).getExerciseType());
        holder.muscleView.setText(exerciseList.get(position).getMuscleGroup());
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameView, typeView, muscleView;

        public MyViewHolder(@NonNull View itemView , ExerciseListRecyclerViewInterface exerciseInterface) {
            super(itemView);

            nameView = itemView.findViewById(R.id.textViewExerciseName);
            typeView = itemView.findViewById(R.id.textViewExerciseType);
            muscleView = itemView.findViewById(R.id.textViewExerciseMuscle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( exerciseInterface != null ){
                        int pos = getAdapterPosition();
                        if( pos != RecyclerView.NO_POSITION)
                            exerciseInterface.onExerciseClick(pos);
                    }
                }
            });
        }
    } // END class MyViewHolder
}
