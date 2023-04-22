package com.example.fitnessapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class NewRoutineActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_routine);

        Button addExercise = findViewById(R.id.addExercise);
        addExercise.setOnClickListener(view -> {


            Intent intent = new Intent(this, ExercisesActivity.class);
            startActivity(intent);


        });

    }
}
