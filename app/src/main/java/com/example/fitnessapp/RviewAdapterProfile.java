package com.example.fitnessapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RviewAdapterProfile extends RecyclerView.Adapter<RviewHolderProfile>{

    private final ArrayList<ProfileCard> profileList;
    private ItemClickListener listener;

    private Context context;


    //Constructor
    public RviewAdapterProfile(ArrayList<ProfileCard> profileList, Context context) {
        this.profileList = profileList;
        this.context = context;
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RviewHolderProfile onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_card, parent, false);



        return new RviewHolderProfile(view, listener);
    }

    @Override
    public void onBindViewHolder(RviewHolderProfile holder, int position) {
        ProfileCard currentProfile = profileList.get(position);
        final String[] profile = {profileList.get(position).getProfileName()};

        holder.profileName.setText(currentProfile.getProfileName());
        holder.profileWorkout.setText(currentProfile.getExercise());
        holder.ex1.setText(currentProfile.getWorkout1());
        holder.ex2.setText(currentProfile.getWorkout2());
        holder.ex3.setText(currentProfile.getWorkout3());

        holder.likeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // String link =  itemList.get(position).getItemDesc();

                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }


}
