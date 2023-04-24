package com.example.fitnessapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Routine> routines;

    public RoutineAdapter(Context context, ArrayList<Routine> routines) {
        this.context = context;
        this.routines = routines;
    }

    @NonNull
    @Override
    public RoutineAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.routine_item, parent, false);
        return new RoutineAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineAdapter.MyViewHolder holder, int position) {
        Routine r = routines.get(position);
        holder.routineName.setText(r.getRoutineName());

        StringBuilder s = new StringBuilder();
        for (ExerciseModel e: r.getExercises()) {
            if (s.length() > 0) {
                s.append(", ");
            }
            s.append(e.getName());
        }
        holder.exerciseList.setText(s.toString());
    }

    @Override
    public int getItemCount() {
        return routines.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView routineName;
        TextView exerciseList;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            routineName = itemView.findViewById(R.id.routine_name);
            exerciseList = itemView.findViewById(R.id.exercise_list);
        }
    }
}

