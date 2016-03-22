package com.example.nevethan.smartbrace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }


    public void viewExercises(View view){
        Intent intent = new Intent(this, Exercises.class);
        startActivity(intent);
    }

    public void viewPrograms(View view){
        Intent intent = new Intent(this, TrainingPrograms.class);
        startActivity(intent);
    }
    /*
    public void beginTraining(View view){
        Intent intent = new Intent(this, )
    }*/
}
