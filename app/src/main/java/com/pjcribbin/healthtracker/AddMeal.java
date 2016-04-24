package com.pjcribbin.healthtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AddMeal extends AppCompatActivity {
    private final static String TAG = "PJ_Health_Tracker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        ListView mealList = (ListView) findViewById(R.id.meal_list);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void addNewMeal(View view) {
        Intent i = new Intent(getApplicationContext(), AddNewMeal.class);
        startActivity(i);
    }
}
