package com.example.fitnessapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class GymActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);

        Button newRoutine = findViewById(R.id.newRoutine);
        newRoutine.setOnClickListener(view -> {


            Intent intent = new Intent(this, NewRoutineActivity.class);
            startActivity(intent);


        });

    }
}
