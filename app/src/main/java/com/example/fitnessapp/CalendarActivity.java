package com.example.fitnessapp;

import static java.lang.Math.abs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends Activity {

    private List<Long> workoutDatesInMillis = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        TextView streak = findViewById(R.id.streak);
        TextView rest = findViewById(R.id.rest);
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setDate(System.currentTimeMillis());

        Calendar calendar = Calendar.getInstance();

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

                        break;
                    case R.id.action_gym:
                        // Handle the search action
                        Intent intentGym = new Intent(CalendarActivity.this, GymActivity.class);
                        startActivity(intentGym);

                        break;
                    case R.id.action_profile:
                        // Handle the settings action
                        Intent intent = new Intent(CalendarActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        DatabaseReference workoutsRef = FirebaseDatabase.getInstance().getReference("workouts").child(uid);
        List<Long> dates = new ArrayList<>();

        workoutsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot workoutSnapshot : snapshot.getChildren()) {
                    String dateString = workoutSnapshot.child("date").getValue(String.class);
                    int highlightColor = ContextCompat.getColor(CalendarActivity.this, R.color.teal_200);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    try {
                        Date date = format.parse(dateString);
                        Log.d("dates", date.toString());
                        long milliseconds = date.getTime();
                        dates.add(milliseconds);

                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
//                        Calendar calendar = Calendar.getInstance();
//                        int year = calendar.get(Calendar.YEAR);
//                        int month = calendar.get(Calendar.MONTH);
//                        int day = calendar.get(Calendar.DAY_OF_MONTH);

                    // Use year, month, and day as needed


                }
                for (long date : dates) {

                    calendarView.setDate(date);
                }
                calendarView.setDateTextAppearance(R.style.CalendarViewHighlightedDate);
                streak.setText(dates.size() + " Days");
                rest.setText(abs(dates.size() - 30) + " Days");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "Cancelled");
            }
        });
    }
}