package com.example.fitnessapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActiveExerciseAdapter extends RecyclerView.Adapter<ActiveExerciseAdapter.MyViewHolder> {

    private ArrayList<ActiveExercise> exercises = new ArrayList<>();
    private Context context;

    public ActiveExerciseAdapter(Context context,ArrayList<ActiveExercise> exercises) {
        this.exercises = exercises;
        this.context = context;
    }

    @NonNull
    @Override
    public ActiveExerciseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.active_workout, parent, false);
        return new ActiveExerciseAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveExerciseAdapter.MyViewHolder holder, int position) {
        ActiveExercise e = exercises.get(position);
        holder.eName.setText(e.getExerciseName());
        if(e.isWeighted) {
            holder.weight1.setText("0");
            holder.weight2.setText("0");
            holder.weight3.setText("0");
        } else {
            holder.weightsTitle.setVisibility(View.GONE);
            holder.weight1.setVisibility(View.GONE);
            holder.weight2.setVisibility(View.GONE);
            holder.weight3.setVisibility(View.GONE);
        }
        holder.rep1.setText("0");
        holder.rep2.setText("0");
        holder.rep3.setText("0");
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView weightsTitle;
        private TextView eName;
        private EditText weight1;
        private EditText weight2;
        private EditText weight3;
        private EditText rep1;
        private EditText rep2;
        private EditText rep3;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            weightsTitle = itemView.findViewById(R.id.sets);
            eName = itemView.findViewById(R.id.exercise_name);
            weight1 = itemView.findViewById(R.id.editText1);
            weight2 = itemView.findViewById(R.id.editText2);
            weight3 = itemView.findViewById(R.id.editText3);
            rep1 = itemView.findViewById(R.id.editText4);
            rep2 = itemView.findViewById(R.id.editText5);
            rep3 = itemView.findViewById(R.id.editText6);

        }
    }
}
