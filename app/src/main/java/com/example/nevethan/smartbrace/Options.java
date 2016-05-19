package com.example.nevethan.smartbrace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/*
    This class contains the implementation of starting different intent per button action.
    Each method is for its own intent/activity.
 */

public class Options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Button beginTraining = (Button) findViewById(R.id.beginTraining);
        Button trainingPrograms = (Button) findViewById(R.id.trainingPrograms);
        Button exercises = (Button) findViewById(R.id.exercises);
    }

    //Action to see activity_exercises
    public void viewExercises(View view){
        Intent intent = new Intent(this, Exercises.class);
        startActivity(intent);
    }

    //Action to see activity_training_programs
    public void viewPrograms(View view){
        Intent intent = new Intent(this, TrainingPrograms.class);
        startActivity(intent);
    }

    //Action to see activity_begin_training
    public void beginTraining(View view){
        Intent intent = new Intent(this, BeginTraining.class);
        startActivity(intent);
    }

    ////Action to see activity_main.
    public void logout(View view){
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.clear();
        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
