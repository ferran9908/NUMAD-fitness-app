package com.example.fitnessapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends Activity {
    private String TAG = "PROFILE_ACTIVITY";
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 200;

    private CircleImageView profileImage;

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
                    if (childSnapshot.getKey().equals("reps")){
                            totalReps += childSnapshot.getValue(Long.class);
                            todayReps += childSnapshot.getValue(Long.class);
                        }
                    if(childSnapshot.getKey().equals("weight")) {
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
                    updateChartData("weights");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }

    private void updateChartData(String type) {
        // Add the BarEntry instances to the lists
        for (int i = 6; i >= 0; i--) {
            weights.add(new BarEntry((6 - i), dailyWeights[i]));
            reps.add(new BarEntry((6 - i), dailyReps[i]));
        }

        // Update the UI with the data (e.g., updating a chart)
        if (type == "weights")
            showBarChart(weights, "Weight Lifted");
        else
            showBarChart(reps, "Reps");
    }

    private List<BarEntry> weights = new ArrayList<>();
    private List<BarEntry> reps = new ArrayList<>();


    private long todayWeight = 0;
    private long todayReps = 0;


    private void showBarChart(List<BarEntry> data, String label) {

        BarChart barChart = findViewById(R.id.bar_chart);
        BarDataSet barDataSet = new BarDataSet(data, label);
        BarData chart = new BarData(barDataSet);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[] {"Day 1", "Day 2", "Day 3", "Day 4", "Day 5", "Day 6", "Day 7"}));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0);
        yAxis.setGranularity(10);

        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.getDescription().setTextColor(Color.WHITE);
        barChart.setDrawValueAboveBar(true);
        barChart.animateY(1000);
        xAxis.setTextColor(Color.BLACK);
        yAxis.setTextColor(Color.BLACK);
        barChart.setData(chart);
        barChart.invalidate();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        requestCameraPermission();

         profileImage = findViewById(R.id.profile_image);

        Button calendar = findViewById(R.id.button_calendar);
        calendar.setOnClickListener(view -> {


            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);


        });

        TextView profileName = findViewById(R.id.profile_name);
        TextView followerCount = findViewById(R.id.followers_count);
        TextView followingCount = findViewById(R.id.following_count);
        TextView workoutsCount = findViewById(R.id.workouts_count);

        MaterialButton weightLifted = findViewById(R.id.weight_chart_button);
        MaterialButton repsMade = findViewById(R.id.reps_chart_button);

        Button exercise = findViewById(R.id.button_exercises);

        exercise.setOnClickListener(view -> {
            Intent intent = new Intent(this, ExercisesActivity.class);
            startActivity(intent);
        });

        repsMade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateChartData("reps");
            }
        });

        weightLifted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateChartData("weights");
            }
        });



        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        DatabaseReference firebaseUser = FirebaseDatabase.getInstance().getReference("users").child(uid);

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



        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCamera();
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

    private void startCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Log.d(TAG, "onActivityResult: BItmap Image:" + imageBitmap.toString());
            uploadImageToFirebase(imageBitmap);
        }
    }

    private void uploadImageToFirebase(Bitmap imageBitmap) {
        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // Create a unique filename
            String imageFilename = "images/" + currentUser.getUid() + "-" + System.currentTimeMillis() + ".jpg";

            // Convert the Bitmap to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();
            // Upload the image to Firebase Storage
            StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(imageFilename);

            UploadTask uploadTask = imageRef.putBytes(imageData);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Get the download URL of the image
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Save the download URL to the Firebase Realtime Database
                    String imageUrl = uri.toString();
                    String userId = currentUser.getUid();
                    FirebaseDatabase.getInstance().getReference("users").child(userId).child("imageUrl").setValue(imageUrl);
                    Glide.with(ProfileActivity.this)
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_profile) // Replace with your own placeholder image
                            .error(R.drawable.ic_gym) // Replace with your own error image
                            .into(profileImage);

                });
            }).addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(ProfileActivity.this, "User is not signed in.", Toast.LENGTH_SHORT).show();
        }
    }

}











