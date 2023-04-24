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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GymActivity extends Activity {

    private ArrayList<Routine> routines = new ArrayList<>();
    private static final String TAG = "Gym Activity";

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);

        recyclerView = findViewById(R.id.routines_recycler_view);
        recyclerView.setHasFixedSize(true);


//        RoutineAdapter routineAdapter = new RoutineAdapter(this, routines);
//        recyclerView.setAdapter(routineAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        // Handle the home action
                        Intent intentHome = new Intent(GymActivity.this, NewActivity.class);
                        startActivity(intentHome);
                        finish();

                        break;
                    case R.id.action_gym:
                        // Handle the search action
                        Intent intentGym = new Intent(GymActivity.this, GymActivity.class);
                        startActivity(intentGym);
                        finish();

                        break;
                    case R.id.action_profile:
                        // Handle the settings action
                        Intent intent = new Intent(GymActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });

        Button newRoutine = findViewById(R.id.newRoutine);
        newRoutine.setOnClickListener(view -> {


            Intent intent = new Intent(this, NewRoutineActivity.class);
            startActivity(intent);


        });

        Button newWorkout = findViewById(R.id.newWorkout);
        newWorkout.setOnClickListener(view -> {


            Intent intent = new Intent(this, NewWorkoutActivity.class);
            startActivity(intent);


        });

        findViewById(R.id.searchRoutine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GymActivity.this, ExploreActivity.class);
                startActivity(intent);
            }
        });

        getRoutinesFromDb();

    }

    private void getRoutinesFromDb() {
        Log.d(TAG, "getRoutinesFromDb: INSIDE GET ROUTINES");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        DatabaseReference routinesRef = FirebaseDatabase.getInstance().getReference("routines").child(uid);
        routinesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot s: snapshot.getChildren()) {
                        ArrayList<ExerciseModel> data = new ArrayList<>();
                        String routineName = s.child("routineName").getValue(String.class);
                        for (DataSnapshot sn: s.child("exercises").getChildren()) {
                            String exerciseName = sn.child("name").getValue(String.class);
                            Boolean isWeighted = sn.child("isWeighted").getValue(Boolean.class);
                            ExerciseModel exerciseModel = new ExerciseModel(exerciseName, isWeighted);
                            data.add(exerciseModel);
                        }
                        Routine r = new Routine(routineName, data);
                        routines.add(r);
                        RoutineAdapter routineAdapter = new RoutineAdapter(GymActivity.this, routines);
                        recyclerView.setAdapter(routineAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(GymActivity.this));
                    }
                    Log.d(TAG, "onCreate: routines size: " + routines.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
//    private void initRecyclerView() {
//        recyclerView = findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        RoutineAdapter routineAdapter = new RoutineAdapter(this, routines);
//        recyclerView.setAdapter(routineAdapter);
//    }
}
