package com.example.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class WorkoutProfileAdapter extends RecyclerView.Adapter<WorkoutProfileAdapter.MyViewHolder> {

    private String TAG = "WorkoutProfileAdapter";

    private ArrayList<WorkoutProfile> workouts = new ArrayList<>();
    private Context context;

    public WorkoutProfileAdapter(Context context, ArrayList<WorkoutProfile> workouts) {
        this.workouts = workouts;
        this.context = context;
    }

    @NonNull
    @Override
    public WorkoutProfileAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.workout_profile_card, parent, false);
        return new WorkoutProfileAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutProfileAdapter.MyViewHolder holder, int position) {

        WorkoutProfile w = workouts.get(position);
        Log.d(TAG, "onBindViewHolder: " + w.getExercises());
        holder.name.setText(w.getProfileName());
        holder.workoutName.setText(w.getRoutineType());
        holder.exercise.setText(w.getExercises());
        holder.shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openShareWindow();
            }
        });

        Glide.with(context)
                .load(w.getImageUrl())
                .placeholder(R.drawable.ic_profile) // Replace with your own placeholder image
                .error(R.drawable.ic_gym) // Replace with your own error image
                .into(holder.profileImage);
    }

    private void openShareWindow() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareText = "Check out this awesome app!";
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share App");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        context.startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, workoutName, exercise;
        private ImageView likeIcon, shareIcon;
        private CircleImageView profileImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            workoutName = itemView.findViewById(R.id.workout_name);
            exercise = itemView.findViewById(R.id.exercise);
            likeIcon = itemView.findViewById(R.id.like_icon);
            shareIcon = itemView.findViewById(R.id.share_icon);
            profileImage = itemView.findViewById(R.id.profile_image);
        }
    }
}
