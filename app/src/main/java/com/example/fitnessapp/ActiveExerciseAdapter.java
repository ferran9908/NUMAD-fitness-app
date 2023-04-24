package com.example.fitnessapp;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ActiveExerciseAdapter extends RecyclerView.Adapter<ActiveExerciseAdapter.MyViewHolder> {

    private ArrayList<ActiveExercise> exercises = new ArrayList<>();
    private Context context;

    public ArrayList<ActiveExercise> getExercises() {
        return exercises;
    }

    public ArrayList<Integer> getIdx() {
        return idx;
    }

    private ArrayList<Integer> idx = new ArrayList<>();

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

        holder.lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!idx.contains(holder.getAdapterPosition())) {
                    if (e.isWeighted) {
                        holder.weight1.setInputType(InputType.TYPE_NULL);
                        holder.weight1.setFocusable(false);
                        holder.weight2.setInputType(InputType.TYPE_NULL);
                        holder.weight2.setFocusable(false);
                        holder.weight3.setInputType(InputType.TYPE_NULL);
                        holder.weight3.setFocusable(false);
                        e.setWeight1(Double.parseDouble(holder.weight1.getText().toString()));
                        e.setWeight2(Double.parseDouble(holder.weight2.getText().toString()));
                        e.setWeight3(Double.parseDouble(holder.weight3.getText().toString()));
                    }
                    else {
                        e.setWeight1(0);
                        e.setWeight2(0);
                        e.setWeight3(0);
                    }
                    holder.rep1.setInputType(InputType.TYPE_NULL);
                    holder.rep1.setFocusable(false);
                    holder.rep2.setInputType(InputType.TYPE_NULL);
                    holder.rep2.setFocusable(false);
                    holder.rep3.setInputType(InputType.TYPE_NULL);
                    holder.rep3.setFocusable(false);

                    e.setRep1(Integer.parseInt(holder.rep1.getText().toString()));
                    e.setRep2(Integer.parseInt(holder.rep2.getText().toString()));
                    e.setRep3(Integer.parseInt(holder.rep3.getText().toString()));

                    idx.add(holder.getAdapterPosition());
                }
                else {
                    if(!e.isWeighted) {
                        holder.weight1.setText("0");
                        holder.weight2.setText("0");
                        holder.weight3.setText("0");
                        e.setWeight1(0);
                        e.setWeight2(0);
                        e.setWeight3(0);
                    } else {

                        holder.weight1.setInputType(InputType.TYPE_CLASS_NUMBER); // Set the input type to the original value (e.g., number)
                        holder.weight1.setFocusable(true);
                        holder.weight1.setFocusableInTouchMode(true);

                        holder.weight2.setInputType(InputType.TYPE_CLASS_NUMBER);
                        holder.weight2.setFocusable(true);
                        holder.weight2.setFocusableInTouchMode(true);

                        holder.weight3.setInputType(InputType.TYPE_CLASS_NUMBER);
                        holder.weight3.setFocusable(true);
                        holder.weight3.setFocusableInTouchMode(true);

                        holder.rep1.setInputType(InputType.TYPE_CLASS_NUMBER);
                        holder.rep1.setFocusable(true);
                        holder.rep1.setFocusableInTouchMode(true);

                        holder.rep2.setInputType(InputType.TYPE_CLASS_NUMBER);
                        holder.rep2.setFocusable(true);
                        holder.rep2.setFocusableInTouchMode(true);

                        holder.rep3.setInputType(InputType.TYPE_CLASS_NUMBER);
                        holder.rep3.setFocusable(true);
                        holder.rep3.setFocusableInTouchMode(true);

                    }
                    idx.remove(Integer.valueOf(holder.getAdapterPosition()));
                }
            }
        });
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

        private FloatingActionButton lock;
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
            lock = itemView.findViewById(R.id.lock);
        }
    }
}
