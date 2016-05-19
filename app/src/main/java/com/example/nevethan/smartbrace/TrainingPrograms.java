package com.example.nevethan.smartbrace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


/*
    This class contains all training programs. The purpose of this class is to demonstrate a Listview
 */
public class TrainingPrograms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_programs);

        String[] programs = {
            "Program 1",
            "Program 2",
            "Program 3",
            "Program 4",
            "Program 5",
            "Program 6"
        };
        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, programs);
        ListView listview = (ListView) findViewById(R.id.listView2);
        listview.setAdapter(listAdapter);
    }
}
