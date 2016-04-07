package com.example.nevethan.smartbrace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Exercises extends AppCompatActivity {

    ListView listView;

    String[] web = {
            "Exercise 1",
            "Exercise 2",
            "Exercise 3"
    };

    Integer[] imageId = {
            R.drawable.bluetooth,
            R.drawable.bluetooth,
            R.drawable.bluetooth,
            //R.drawable.image2,
            //R.drawable.image3
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        //CustomList customList = new CustomList(Exercises.this, web, imageId);
        ListAdapter listAdapter = new CustomList(this, web, imageId);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(Exercises.this, "You Clicked at " + web[+position], Toast.LENGTH_SHORT).show();
            }
        });
    }
}
