package com.example.fitnessapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class RviewHolderProfile extends RecyclerView.ViewHolder {


    public TextView profileName;
    public TextView profileWorkout;

    public ImageView profilePic;

    public TextView ex1;
    public TextView ex2;
    public TextView ex3;

    public ImageView likeButton;
    public ImageView shareButton;




    public RviewHolderProfile(View itemView, final ItemClickListener listener) {
        super(itemView);
        profileName = itemView.findViewById(R.id.name);
        profileWorkout = itemView.findViewById(R.id.workout_name);
        profilePic = itemView.findViewById(R.id.profile_picture);
        ex1 = itemView.findViewById(R.id.exercise_1);
        ex2 = itemView.findViewById(R.id.exercise_1);
        ex3 = itemView.findViewById(R.id.exercise_1);
        likeButton = itemView.findViewById(R.id.like_icon);
        shareButton = itemView.findViewById(R.id.share_icon);


        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    // @Ferran Add likes update here
                    int position = getLayoutPosition();

                    if (position != RecyclerView.NO_POSITION) {

                        listener.onItemClick(position);
                    }
                }
            }
        });



    }
}

