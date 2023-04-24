package com.example.fitnessapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class NewWorkoutActivity extends Activity {
    private static String TAG = "New Workout Activity";
    private static final int GET_WORKOUTS = 9021;
    FloatingActionButton fab;

    private RecyclerView recyclerView;
    private RviewAdapter rviewAdapter;

    private ArrayList<ItemCard> addedExercises = new ArrayList<>();
    private ArrayList<ActiveExercise> activeExercises = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        recyclerView = findViewById(R.id.active_workout_recycler_view);
        recyclerView.setHasFixedSize(true);

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

        fab = findViewById(R.id.addActive);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewWorkoutActivity.this, ExercisesActivity.class);
                intent.putExtra("isEdit", "true");
                startActivityForResult(intent, GET_WORKOUTS);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_WORKOUTS && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<ItemCard> exerciseData = (ArrayList<ItemCard>) data.getSerializableExtra("exercisesAdded");
            // Use receivedCustomObjectsList as needed
            for (ItemCard element : exerciseData) {
                ActiveExercise e;
                if(element.getItemDesc().equals("Weighted"))
                    e = new ActiveExercise(element.getItemName());
                else
                    e = new ActiveExercise(element.getItemName(), false);
                activeExercises.add(e);
                Log.d(TAG, "onActivityResult: " + element);
            }
            ActiveExerciseAdapter activeExerciseAdapter = new ActiveExerciseAdapter(NewWorkoutActivity.this, activeExercises);
            recyclerView.setAdapter(activeExerciseAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(NewWorkoutActivity.this));
        }
    }
}
