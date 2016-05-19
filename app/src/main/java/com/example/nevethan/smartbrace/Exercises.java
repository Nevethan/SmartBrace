package com.example.nevethan.smartbrace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

/*
    The main purpose of the class/feature is to show the use of a custom Listview.
    The view contains an Image and a title to the image.
 */
public class Exercises extends AppCompatActivity {

    ListView listView;

    String[] titles = {
            "Squat",
            "Knee Stabilization",
            "Quadriceps Stretch,",
            "Side Lying Leg Lift"
    };

    Integer[] imageId = {
            R.drawable.squat,
            R.drawable.knee_stabilization,
            R.drawable.quadriceps_stretch,
            R.drawable.side_lying_leg_lift
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);


        ListAdapter listAdapter = new CustomList(this, titles, imageId);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(Exercises.this, "You Clicked at " + titles[+position], Toast.LENGTH_SHORT).show();
            }
        });
    }
}
