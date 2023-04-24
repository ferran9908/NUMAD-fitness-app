package com.example.fitnessapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewWorkoutActivity extends Activity {
    private static String TAG = "New Workout Activity";
    private static final int GET_WORKOUTS = 9021;
    FloatingActionButton fab, save;

    private RecyclerView recyclerView;
    private ActiveExerciseAdapter AeRecyclerViewAdapter;

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



        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AeRecyclerViewAdapter.getIdx().size() == activeExercises.size()) {
                    Toast.makeText(NewWorkoutActivity.this, "Saving your workout...", Toast.LENGTH_SHORT).show();
                    ArrayList<ActiveExercise> eList = AeRecyclerViewAdapter.getExercises();
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = currentUser.getUid();
                    // Create a new workoutId
                    DatabaseReference workoutRef = FirebaseDatabase.getInstance().getReference().child("workouts").child(uid).push();
                    String workoutId = workoutRef.getKey();

                    // Set date
                    LocalDate currentDate = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        currentDate = LocalDate.now();
                    }

                    int totalReps = 0;
                    double totalWeight = 0;

                    workoutRef.child("date").setValue(currentDate.toString());

                    // Save exercises
                    for (ActiveExercise exercise : eList) {
                        DatabaseReference exerciseRef = workoutRef.child("workout").child("exercise").child(exercise.getExerciseName());

                        // Save sets
                        Map<String, Object> set1 = new HashMap<>();
                        set1.put("reps", exercise.getRep1());
                        set1.put("weight", exercise.getWeight1());
                        exerciseRef.child("sets").child("1").setValue(set1);

                        Map<String, Object> set2 = new HashMap<>();
                        set2.put("reps", exercise.getRep2());
                        set2.put("weight", exercise.getWeight2());
                        exerciseRef.child("sets").child("2").setValue(set2);

                        Map<String, Object> set3 = new HashMap<>();
                        set3.put("reps", exercise.getRep3());
                        set3.put("weight", exercise.getWeight3());
                        exerciseRef.child("sets").child("3").setValue(set3);

                        totalReps += exercise.getRep1() + exercise.getRep2() + exercise.getRep3();
                        totalWeight += exercise.getRep1() * exercise.getWeight1() +
                                exercise.getRep2() * exercise.getWeight2() +
                                exercise.getRep3() * exercise.getWeight3();

                    }

                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("daily_totals").child(currentDate.toString());
                    userRef.child("reps").setValue(totalReps);
                    userRef.child("weight").setValue(totalWeight);
                    finish();
                }else {
                    Toast.makeText(NewWorkoutActivity.this, "Please Lock/Save all exercises!", Toast.LENGTH_SHORT).show();
                }
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
            }
            ActiveExerciseAdapter activeExerciseAdapter = new ActiveExerciseAdapter(NewWorkoutActivity.this, activeExercises);
            recyclerView.setAdapter(activeExerciseAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(NewWorkoutActivity.this));
            this.AeRecyclerViewAdapter = activeExerciseAdapter;
        }
    }
}
