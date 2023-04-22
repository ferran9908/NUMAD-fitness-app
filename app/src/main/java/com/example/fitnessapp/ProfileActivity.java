package com.example.fitnessapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.Manifest;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends Activity {
    private String TAG = "PROFILE_ACTIVITY";
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 200;


    private double totalWeight = 0;
    private double totalReps = 0;

    private int daysFetched = 0;
    private int[] dailyWeights = new int[7];
    private int[] dailyReps = new int[7];
    private void resetData() {
        daysFetched = 0;
        dailyWeights = new int[7];
        dailyReps = new int[7];
    }
    private void fetchDataForDay(DatabaseReference dayTotalsRef, int index) {
        dayTotalsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Sum up the daily_totals
                int todayWeight = 0;
                int todayReps = 0;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: Key: " + childSnapshot.getKey() );
                    Log.d(TAG, "onDataChange: Value: " + childSnapshot.getValue() );
                    if (childSnapshot.getKey().equals("reps")){
                            Log.d(TAG, "onDataChange: reps: " + childSnapshot.getValue(Long.class));
                            totalReps += childSnapshot.getValue(Long.class);
                            todayReps += childSnapshot.getValue(Long.class);
                        }
                    if(childSnapshot.getKey().equals("weight")) {
                        Log.d(TAG, "onDataChange: weight: " + childSnapshot.getValue(Long.class));
                        totalWeight += childSnapshot.getValue(Long.class);
                        todayWeight += childSnapshot.getValue(Long.class);
                    }
                }

                // Save the data for this day
                dailyWeights[index] = todayWeight;
                dailyReps[index] = todayReps;

                // Call the method to update the UI if all days have been fetched
                daysFetched++;
                if (daysFetched == 7) {
                    updateChartData();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }

    private void updateChartData() {
        // Add the BarEntry instances to the lists
        for (int i = 6; i >= 0; i--) {
            weights.add(new BarEntry((6 - i), dailyWeights[i]));
            reps.add(new BarEntry((6 - i), dailyReps[i]));
        }

        // Update the UI with the data (e.g., updating a chart)
        showBarChart(weights, "Weight Lifted");
    }

    private List<BarEntry> weights = new ArrayList<>();
    private List<BarEntry> reps = new ArrayList<>();


    private long todayWeight = 0;
    private long todayReps = 0;


    private void showBarChart(List<BarEntry> data, String label) {

        BarChart barChart = findViewById(R.id.bar_chart);
        BarDataSet barDataSet = new BarDataSet(data, label);
        BarData chart = new BarData(barDataSet);
        barChart.setData(chart);
        barChart.invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        requestCameraPermission();

        CircleImageView profileImage = findViewById(R.id.profile_image);

        TextView profileName = findViewById(R.id.profile_name);
        TextView followerCount = findViewById(R.id.followers_count);
        TextView followingCount = findViewById(R.id.following_count);
        TextView workoutsCount = findViewById(R.id.workouts_count);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        DatabaseReference firebaseUser = FirebaseDatabase.getInstance().getReference("users").child(uid);
        Log.d(TAG, "onCreate: firebaseUser: user:" + firebaseUser);

        firebaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                        String key = childSnapshot.getKey();
                        if(key.equals("displayName")){
                            profileName.setText(childSnapshot.getValue(String.class));
                        } else if (key.equals("imageUrl")){
                            Glide.with(ProfileActivity.this)
                                .load(childSnapshot.getValue(String.class))
                                .placeholder(R.drawable.ic_profile) // Replace with your own placeholder image
                                .error(R.drawable.ic_gym) // Replace with your own error image
                                .into(profileImage);
                        }else if(key.equals("followers")){
                            followerCount.setText(String.valueOf(childSnapshot.getChildrenCount()));
                        } else if(key.equals("following")){
                            followingCount.setText(String.valueOf(childSnapshot.getChildrenCount()));
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference workoutsRef = FirebaseDatabase.getInstance().getReference("workouts").child(uid);

                workoutsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            workoutsCount.setText(String.valueOf(snapshot.getChildrenCount()));
                        }else{
                            workoutsCount.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "onCancelled: " + error);
                    }
                });

        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();

        DatabaseReference dailyTotalsRef = FirebaseDatabase.getInstance().getReference("users/" + uid + "/daily_totals");

        resetData();

        for (int i = 6; i >= 0; i--) {
            // Get the date for the current day
            cal.setTime(currentDate);
            cal.add(Calendar.DATE, -i);
            Date date = cal.getTime();

            // Convert the date to a string in the format yyyy-MM-dd
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(date);

            // Get the daily_totals for the current day
            DatabaseReference dayTotalsRef = dailyTotalsRef.child(dateString);
            fetchDataForDay(dayTotalsRef, 6 - i);
        }


        // Get a reference to the user's daily_totals
//                DatabaseReference dailyTotalsRef = FirebaseDatabase.getInstance().getReference("users/" + uid + "/daily_totals");
//
//                // Get the current date
//                Calendar cal = Calendar.getInstance();
//                Date currentDate = cal.getTime();
//
//                // Loop through the past 7 days and sum the daily_totals
//                for (int i = 6; i >= 0; i--) {
//                    // Get the date for the current day
//                    cal.setTime(currentDate);
//                    cal.add(Calendar.DATE, -i);
//                    Date date = cal.getTime();
//
//                    // Convert the date to a string in the format yyyy-MM-dd
//                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                    String dateString = dateFormat.format(date);
//
//

                    // Get the daily_totals for the current day
//                    DatabaseReference dayTotalsRef = dailyTotalsRef.child(dateString);
//                    weights = new ArrayList<>();
//                    reps = new ArrayList<>();
//                    int finalI = i;
//                    dayTotalsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            // Sum up the daily_totals
//                            todayWeight = 0;
//                            todayReps = 0;
//                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//
//                                if (childSnapshot.getKey().equals("reps")){
//                                    Log.d(TAG, "onDataChange: reps: " + childSnapshot.getValue(Long.class));
//                                    totalReps += childSnapshot.getValue(Long.class);
//                                    todayReps += childSnapshot.getValue(Long.class);
//                                }
//                                if(childSnapshot.getKey().equals("weight")) {
//                                    Log.d(TAG, "onDataChange: weight: " + childSnapshot.getValue(Long.class));
//                                    totalWeight += childSnapshot.getValue(Long.class);
//                                    todayWeight += childSnapshot.getValue(Long.class);
//                                }
//
//                            }
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            // Handle any errors
//                        }
//                    });
//                    weights.add(new BarEntry((6 - finalI), todayWeight));
//                    reps.add(new BarEntry((6 - finalI), todayReps));                    // Check if there are less than 7 days of data
//                    if (i > 0 && dayTotalsRef == null) {
//                        break;
//                    }
//                }
//                showBarChart(weights, "Weights Lifted");





//        UserData.getCurrentUserData(new UserData.UserDataCallback() {
//
//            @Override
//            public void onUserDataReceived(User user) {
//                Log.d(TAG, "onUserDataReceived: " + user.getImageUrl());
//                Log.d(TAG, "onUserDataReceived: User: " + user);
//                Glide.with(ProfileActivity.this)
//                        .load(user.getImageUrl())
//                        .placeholder(R.drawable.ic_profile) // Replace with your own placeholder image
//                        .error(R.drawable.ic_gym) // Replace with your own error image
//                        .into(profileImage);
//
//                profileName.setText(user.getDisplayName());
//                if (user.getFollowers() != null)
//                    followerCount.setText(String.valueOf(user.getFollowers().size()));
//                if (user.getFollowing() != null)
//                    followingCount.setText(String.valueOf(user.getFollowing().size()));
//
//                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                String uid = null;
//
//                // Check if a user is logged in
//                if (currentUser != null) {
//                    // Get the user's UID
//                    uid = currentUser.getUid();
//                }
//
//                DatabaseReference workoutsRef = FirebaseDatabase.getInstance().getReference("workouts").child(uid);
//
//                workoutsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()){
//                            workoutsCount.setText(String.valueOf(snapshot.getChildrenCount()));
//                        }else{
//                            workoutsCount.setText("0");
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.e(TAG, "onCancelled: " + error);
//                    }
//                });
//
//
//
//                // Get a reference to the user's daily_totals
//                DatabaseReference dailyTotalsRef = FirebaseDatabase.getInstance().getReference("users/" + uid + "/daily_totals");
//
//                // Get the current date
//                Calendar cal = Calendar.getInstance();
//                Date currentDate = cal.getTime();
//
//                // Loop through the past 7 days and sum the daily_totals
//                for (int i = 6; i >= 0; i--) {
//                    // Get the date for the current day
//                    cal.setTime(currentDate);
//                    cal.add(Calendar.DATE, -i);
//                    Date date = cal.getTime();
//
//                    // Convert the date to a string in the format yyyy-MM-dd
//                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                    String dateString = dateFormat.format(date);
//
//                    // Get the daily_totals for the current day
//                    DatabaseReference dayTotalsRef = dailyTotalsRef.child(dateString);
//                    weights = new ArrayList<>();
//                    reps = new ArrayList<>();
//                    int finalI = i;
//                    dayTotalsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            // Sum up the daily_totals
//                            todayWeight = 0;
//                            todayReps = 0;
//                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//
//                                if (childSnapshot.getKey() == "reps"){
//                                    Log.d(TAG, "onDataChange: reps: " + childSnapshot.getValue(Long.class));
//                                    totalReps += childSnapshot.getValue(Long.class);
//                                    todayReps += childSnapshot.getValue(Long.class);
//                                }
//                                if(childSnapshot.getKey() == "weight") {
//                                    Log.d(TAG, "onDataChange: weight: " + childSnapshot.getValue(Long.class));
//                                    totalWeight += childSnapshot.getValue(Long.class);
//                                    todayWeight += childSnapshot.getValue(Long.class);
//                                }
//
//                            }
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            // Handle any errors
//                        }
//                    });
//                    weights.add(new BarEntry((6 - finalI), todayWeight));
//                    reps.add(new BarEntry((6 - finalI), todayReps));                    // Check if there are less than 7 days of data
//                    if (i > 0 && dayTotalsRef == null) {
//                        break;
//                    }
//                }
//                showBarChart(weights, "Weights Lifted");
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                Log.d(TAG, "onError: " + errorMessage);
//            }
//        });


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PROFILE_IMAGE", "onClick: Profile pic");
                takeProfilePicture();
            }
        });


        //Nav Bar actions
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        // Handle the home action
                        Intent intentHome = new Intent(ProfileActivity.this, NewActivity.class);
                        startActivity(intentHome);

                        break;
                    case R.id.action_gym:
                        // Handle the search action
                        Intent intentGym = new Intent(ProfileActivity.this, GymActivity.class);
                        startActivity(intentGym);

                        break;
                    case R.id.action_profile:
                        // Handle the settings action
                        Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                        startActivity(intent);

                        break;
                }
                return true;
            }
        });

    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
            }
        }
    }

    private void takeProfilePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            CircleImageView profileImage = findViewById(R.id.profile_image);
            Picasso.get().load(getImageUri(this, imageBitmap)).into(profileImage);
        }
    }

    public Uri getImageUri(Context context, Bitmap imageBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), imageBitmap, "Title", null);
        return Uri.parse(path);
    }


}











