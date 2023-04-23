package com.example.fitnessapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class NewRoutineActivity extends Activity {

    private static String TAG = "New Routine Activity";
    private ArrayList<ItemCard> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RviewAdapter rviewAdapter;
    private ArrayList<ItemCard> addedExercises = new ArrayList<>();
    private RecyclerView.LayoutManager rLayoutManger;
    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";

    private static final int ADD_EXERCISES_TO_ROUTINE = 123;



    private void initialItemData(Bundle savedInstanceState) {

        // Not the first time to open this Activity
        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
            if (itemList == null || itemList.size() == 0) {

                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);

                // Retrieve keys we stored in the instance
                for (int i = 0; i < size; i++) {
                    String itemName = savedInstanceState.getString(KEY_OF_INSTANCE + i + "0");
                    String itemDesc = savedInstanceState.getString(KEY_OF_INSTANCE + i + "1");

                    ItemCard itemCard = new ItemCard( itemName, itemDesc);

                    itemList.add(itemCard);
                }
            }
        }
        // The first time to opne this Activity
        else {
//            DatabaseReference workoutsRef = FirebaseDatabase.getInstance().getReference("exercises");
//            workoutsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot exerciseSnapshot: snapshot.getChildren()){
//                        String name = "";
//                        Boolean isWeighted = false;
//                        for (DataSnapshot exerciseData: exerciseSnapshot.getChildren()){
//                            if(exerciseData.getKey().equals("name")){
//                                name = exerciseData.getValue(String.class);
//                            } else {
//                                isWeighted = exerciseData.getValue(Boolean.class);
//                            }
//                        }
//                        ItemCard exercise;
//                        if (isWeighted){
//                            exercise = new ItemCard(name, "Weighted");
//                        } else {
//                            exercise = new ItemCard(name, "Bodyweight");
//                        }
//                        itemList.add(exercise);
//                        rviewAdapter.notifyItemChanged(itemList.size() - 1);
//                        rviewAdapter.notifyDataSetChanged();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//            ItemCard item1 = new ItemCard( "Gmail", "www.gmail.com");
//            ItemCard item2 = new ItemCard("Google", "www.google.com");
//            itemList.add(item1);
//            itemList.add(item2);
        }
    }


    private void init(Bundle savedInstanceState) {

        initialItemData(savedInstanceState);
        createRecyclerView();
    }
    private void createRecyclerView() {

        rLayoutManger = new LinearLayoutManager(this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        rviewAdapter = new RviewAdapter(addedExercises, this);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_routine);

        init(savedInstanceState);
        Button saveRoutine = findViewById(R.id.saveRoutine);
        EditText routineName = findViewById(R.id.textInputEditText);

        saveRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(routineName.getText().toString().equals("") || addedExercises.size() == 0) {
                    Toast.makeText(NewRoutineActivity.this, "Please add a routine name and exercises!", Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = currentUser.getUid();
                    DatabaseReference routineRef = FirebaseDatabase.getInstance().getReference("routines").child(uid);
                    String newRoutineKey = routineRef.push().getKey();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("routineName", routineName.getText().toString());
                    ArrayList<HashMap<String, Object>> finalList = new ArrayList<>();
                    for (ItemCard exercise : addedExercises) {
                        HashMap<String, Object> exercises = new HashMap<>();
                        exercises.put("name", exercise.getItemName());
                        if (exercise.getItemDesc().equals("Weighted")) {
                            exercises.put("isWeighted", true);
                        } else {
                            exercises.put("isWeighted", false);
                        }
                        finalList.add(exercises);
                    }
                    result.put("exercises", finalList);
                    routineRef.child(newRoutineKey).setValue(result);
                    Toast.makeText(NewRoutineActivity.this, "Routine created!", Toast.LENGTH_SHORT).show();
                    finish();
                    Log.d(TAG, "onClick: added routine to Firebase");
                }
            }
        });


        //Nave Bar actions
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.action_home:
//                        // Handle the home action
//                        Intent intentHome = new Intent(NewRoutineActivity.this, NewActivity.class);
//                        startActivity(intentHome);
//
//                        break;
//                    case R.id.action_gym:
//                        // Handle the search action
//                        Intent intentGym = new Intent(NewRoutineActivity.this, GymActivity.class);
//                        startActivity(intentGym);
//
//                        break;
//                    case R.id.action_profile:
//                        // Handle the settings action
//                        Intent intent = new Intent(NewRoutineActivity.this, ProfileActivity.class);
//                        startActivity(intent);
//                        break;
//                }
//                return true;
//            }
//        });

        Button addExercise = findViewById(R.id.addExercise);
        addExercise.setOnClickListener(view -> {
            Intent intent = new Intent(this, ExercisesActivity.class);
            intent.putExtra("isEdit", "true");
            startActivityForResult(intent, ADD_EXERCISES_TO_ROUTINE);
        });

        //Specify what action a specific gesture performs, in this case swiping right or left deletes the entry
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                ItemCard deletedItem = addedExercises.get(viewHolder.getAdapterPosition());
                int position = viewHolder.getAdapterPosition();
                Snackbar.make(recyclerView, deletedItem.getItemName()+" deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // adding on click listener to our action of snack bar.
                                // below line is to add our item to array list with a position.
                                addedExercises.add(position, deletedItem);

                                // below line is to notify item is
                                // added to our adapter class.
                                rviewAdapter.notifyItemInserted(position);
                            }
                        }).show();

                addedExercises.remove(position);

                rviewAdapter.notifyItemRemoved(position);

            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EXERCISES_TO_ROUTINE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<ItemCard> exerciseData = (ArrayList<ItemCard>) data.getSerializableExtra("exercisesAdded");
            // Use receivedCustomObjectsList as needed
            for (ItemCard element : exerciseData) {
                addedExercises.add(element);
                rviewAdapter.notifyItemInserted(addedExercises.size() - 1);
                rviewAdapter.notifyDataSetChanged();
            }
        }
    }
}
