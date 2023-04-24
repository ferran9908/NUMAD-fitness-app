package com.example.fitnessapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import java.util.Random;

public class NewActivity extends Activity {

    private ArrayList<ProfileCard> profileList = new ArrayList<>();
    private ArrayList<WorkoutProfile> workoutProfileList = new ArrayList<>();

    private RecyclerView recyclerView;

    private RecyclerView recyclerView1;
    private WorkoutProfileAdapter workoutProfileAdapter;
    private RviewAdapterProfile rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";

    public static String getRandomRoutineType() {
        String[] routineTypes = {"Core day", "Leg Day", "Push Day", "Pull Day", "Chest Day", "Back Day", "Arm Day"};
        Random random = new Random();
        int randomIndex = random.nextInt(routineTypes.length);
        return routineTypes[randomIndex];
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        init(savedInstanceState);
        init1(savedInstanceState);


        //Nave Bar actions
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        // Handle the home action
                        Intent intentHome = new Intent(NewActivity.this, NewActivity.class);
                        startActivity(intentHome);
                        finish();
                        break;
                    case R.id.action_gym:
                        // Handle the search action
                        Intent intentGym = new Intent(NewActivity.this, GymActivity.class);
                        startActivity(intentGym);
                        finish();
                        break;
                    case R.id.action_profile:
                        // Handle the settings action
                        Intent intent = new Intent(NewActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });

    }

    private void init(Bundle savedInstanceState) {

        initialItemData1(savedInstanceState);
        createRecyclerView();
    }

    private void init1(Bundle savedInstanceState) {
        initialItemData1(savedInstanceState);
    }


    private void createRecyclerView() {


        rLayoutManger = new LinearLayoutManager(this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        rviewAdapter = new RviewAdapterProfile(profileList, this);
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //attributions bond to the item has been changed
                //itemList.get(position).onItemClick(position);

                rviewAdapter.notifyItemChanged(position);
            }

        };
        rviewAdapter.setOnItemClickListener(itemClickListener);

        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);


    }

    private void initialItemData1(Bundle savedInstanceState) {

        // Not the first time to open this Activity
        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
            if (profileList == null || profileList.size() == 0) {

                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);

                // Retrieve keys we stored in the instance
                for (int i = 0; i < size; i++) {

                    // @Ferr Replace with DB data for profiles

                    String profileName = "null";
                    String exercise = "null";
                    String time = "null";
                    String workout1 = "null";
                    String workout2 = " ";
                    String workout3 = " ";

                    ProfileCard profileCard = new ProfileCard(profileName, exercise, time, workout1, workout2, workout3, "");

                    profileList.add(profileCard);
                }
            }
        }
        //The first time to opne this Activity
        else {
            DatabaseReference workoutsRef, usersRef;
            workoutsRef = FirebaseDatabase.getInstance().getReference("workouts");
            usersRef = FirebaseDatabase.getInstance().getReference("users");

            workoutsRef.limitToFirst(10).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userWorkouts : dataSnapshot.getChildren()) {
                        String userId = userWorkouts.getKey();

                        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                String displayName = userSnapshot.child("displayName").getValue(String.class);
                                String imageUrl = userSnapshot.child("imageUrl").getValue(String.class);

                                for (DataSnapshot workoutSnapshot : userWorkouts.getChildren()) {
                                    DataSnapshot workoutDataSnapshot = workoutSnapshot.child("workout").child("exercises");
                                    StringBuilder exercisesBuilder = new StringBuilder();

                                    for (DataSnapshot exerciseDataSnapshot : workoutDataSnapshot.getChildren()) {
//                                        String exerciseName = exerciseDataSnapshot.child("name").getValue(String.class);
                                        String exerciseName = exerciseDataSnapshot.getKey();
                                        exercisesBuilder.append(exerciseName).append("\n");
                                    }

                                    String exercises = exercisesBuilder.toString().trim();
                                    Log.d("EXERCISES", "onDataChange: " + exercises);
                                    String cDisplayName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                                    if (!cDisplayName.equals(displayName)){
                                        // Replace "RoutineType" with the actual routine type for the workout
                                        WorkoutProfile workoutProfile = new WorkoutProfile(displayName, getRandomRoutineType(), exercises, imageUrl);
                                        workoutProfileList.add(workoutProfile);
                                    }

                                }
                                recyclerView1 = findViewById(R.id.recycler_view);
                                WorkoutProfileAdapter workoutProfile = new WorkoutProfileAdapter(NewActivity.this, workoutProfileList);
                                workoutProfileAdapter = workoutProfile;
                                recyclerView1.setAdapter(workoutProfile);
                                recyclerView1.setLayoutManager(new LinearLayoutManager(NewActivity.this));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.w("TAG", "loadUserData:onCancelled", error.toException());
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("TAG", "loadWorkoutData:onCancelled", error.toException());
                }
            });
        }


    }
}