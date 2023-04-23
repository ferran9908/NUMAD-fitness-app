package com.example.fitnessapp;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RviewHolder extends RecyclerView.ViewHolder {

    private static String TAG = "RviewHolder";

    public TextView itemName;
    public TextView itemDesc;

    public ImageView editButton;

    private Boolean isEditable = false;

    private ArrayList<ItemCard> addedExercises;




    public RviewHolder(View itemView, final ItemClickListener listener) {
        super(itemView);
        itemName = itemView.findViewById(R.id.item_name);
        itemDesc = itemView.findViewById(R.id.item_desc);
        editButton = itemView.findViewById(R.id.imageView);

        itemView.setOnClickListener(view -> {

        });



    }

    public RviewHolder(View itemView, final ItemClickListener listener, Boolean isEditable, ArrayList<ItemCard> addedExercises) {
        super(itemView);
        itemName = itemView.findViewById(R.id.item_name);
        itemDesc = itemView.findViewById(R.id.item_desc);
        editButton = itemView.findViewById(R.id.imageView);
        this.isEditable = isEditable;
        this.addedExercises = addedExercises;
//
//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listener != null) {
//                    int position = getLayoutPosition();
//
//                    if (position != RecyclerView.NO_POSITION) {
//
//                        listener.onItemClick(position);
//                    }
//                }
//            }
//        });

        if(isEditable){
            itemView.setOnClickListener(view -> {
                ItemCard item = new ItemCard(itemName.getText().toString(), itemDesc.getText().toString());
                this.addedExercises.add(item);
                editButton.setVisibility(View.GONE);
//                Toast.makeText(ExercisesActivity.this, "added", Toast.LENGTH_SHORT);
                Log.d(TAG, "RviewHolder: Clicked on exercise card: "+ item);
            });
        }




    }
}
