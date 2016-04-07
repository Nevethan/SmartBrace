package com.example.nevethan.smartbrace;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Button beginTraining = (Button) findViewById(R.id.beginTraining);
        Button trainingPrograms = (Button) findViewById(R.id.trainingPrograms);
        Button exercises = (Button) findViewById(R.id.exercises);
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
    }


    public void viewExercises(View view){
        Intent intent = new Intent(this, Exercises.class);
        startActivity(intent);
    }

    public void viewPrograms(View view){
        Intent intent = new Intent(this, TrainingPrograms.class);
        startActivity(intent);
    }

    public void toBluetooth(View view){
        Intent intent = new Intent(this, Bluetooth.class);
        startActivity(intent);
    }

    public void beginTraining(View view){
        Intent intent = new Intent(this, BeginTraining.class);
        startActivity(intent);
    }
}
