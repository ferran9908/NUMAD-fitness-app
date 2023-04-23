package com.example.fitnessapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class NewActivity extends Activity {

    private ArrayList<ProfileCard> profileList = new ArrayList<>();
    ;

    private RecyclerView recyclerView;
    private RviewAdapterProfile rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init(savedInstanceState);


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

                        break;
                    case R.id.action_gym:
                        // Handle the search action
                        Intent intentGym = new Intent(NewActivity.this, GymActivity.class);
                        startActivity(intentGym);

                        break;
                    case R.id.action_profile:
                        // Handle the settings action
                        Intent intent = new Intent(NewActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

    }

    private void init(Bundle savedInstanceState) {

        initialItemData(savedInstanceState);
        createRecyclerView();
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

    private void initialItemData(Bundle savedInstanceState) {

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

                    ProfileCard profileCard = new ProfileCard(  profileName,  exercise,  time,  workout1,  workout2,  workout3 );

                    profileList.add(profileCard);
                }
            }
        }
        //The first time to opne this Activity
        else {
            ProfileCard item1 = new ProfileCard( "Bhoomika", "Leg Day","50min", "Squats", "Lunges", "Push ups" );
            ProfileCard item2 = new ProfileCard("Mohit", "Core Day","20min", "Squats", "Abs Curl", "Push ups");
            ProfileCard item3 = new ProfileCard( "Ferran", "Leg Day","50min", "Squats", "Lunges", "Push ups" );
            ProfileCard item4 = new ProfileCard("Prats", "Core Day","20min", "Squats", "Abs Curl", "Push ups");
            profileList.add(item1);
            profileList.add(item2);
            profileList.add(item3);
            profileList.add(item4);
        }
    }
}
