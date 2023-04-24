package com.example.fitnessapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CalendarView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CalendarActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //Nave Bar actions
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        // Handle the home action
                        Intent intentHome = new Intent(CalendarActivity.this, NewActivity.class);
                        startActivity(intentHome);
                        finish();

                        break;
                    case R.id.action_gym:
                        // Handle the search action
                        Intent intentGym = new Intent(CalendarActivity.this, GymActivity.class);
                        startActivity(intentGym);
                        finish();

                        break;
                    case R.id.action_profile:
                        // Handle the settings action
                        Intent intent = new Intent(CalendarActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });


        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setDate(System.currentTimeMillis());

    }
}
