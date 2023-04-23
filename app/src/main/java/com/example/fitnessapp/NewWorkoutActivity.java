package com.example.fitnessapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NewWorkoutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        //Nave Bar actions
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        // Handle the home action
                        Intent intentHome = new Intent(NewWorkoutActivity.this, NewActivity.class);
                        startActivity(intentHome);

                        break;
                    case R.id.action_gym:
                        // Handle the search action
                        Intent intentGym = new Intent(NewWorkoutActivity.this, GymActivity.class);
                        startActivity(intentGym);

                        break;
                    case R.id.action_profile:
                        // Handle the settings action
                        Intent intent = new Intent(NewWorkoutActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

//        Button newRoutine = findViewById(R.id.newRoutine);
//        newRoutine.setOnClickListener(view -> {
//
//
//            Intent intent = new Intent(this, NewRoutineActivity.class);
//            startActivity(intent);
//
//
//        });
//
//        Button newWorkout = findViewById(R.id.newWorkout);
//        newRoutine.setOnClickListener(view -> {
//
//
//            Intent intent = new Intent(this, NewWorkoutActivity.class);
//            startActivity(intent);
//
//
//        });

    }
}