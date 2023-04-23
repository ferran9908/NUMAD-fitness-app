package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExploreActivity extends AppCompatActivity {

    private ArrayList<Routine> routines = new ArrayList<>();
    private static String TAG = "Explore Activity";
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        recyclerView = findViewById(R.id.explore_recycler_view);
        recyclerView.setHasFixedSize(true);
        getRoutinesFromDb();

    }

    private void getRoutinesFromDb() {
        DatabaseReference routinesRef = FirebaseDatabase.getInstance().getReference("routines");

        routinesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot routineSnapshot : userSnapshot.getChildren()) {
                        String routineName = routineSnapshot.child("routineName").getValue(String.class);
                        ArrayList<ExerciseModel> exercises = new ArrayList<>();
                        for (DataSnapshot exerciseSnapshot : routineSnapshot.child("exercises").getChildren()) {
                            String name = exerciseSnapshot.child("name").getValue(String.class);
                            Boolean isWeighted = exerciseSnapshot.child("isWeighted").getValue(Boolean.class);
                            exercises.add(new ExerciseModel(name, isWeighted));
                        }
                        Routine routine = new Routine(routineName, exercises);
                        routines.add(routine);
                    }
                }
                RoutineAdapter routineAdapter = new RoutineAdapter(ExploreActivity.this, routines);
                        recyclerView.setAdapter(routineAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ExploreActivity.this));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

    }
}