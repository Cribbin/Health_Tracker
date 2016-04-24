package com.pjcribbin.healthtracker;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AddFood extends AppCompatActivity {
    //SQLiteDatabase database = this.openOrCreateDatabase("Fitness", MODE_PRIVATE, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        //Database.createFoodTable(database);

        ListView foodList = (ListView) findViewById(R.id.food_list);

        final ArrayList<String> food = new ArrayList<>();
        food.add("+ Add new food");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, food);

        foodList.setAdapter(arrayAdapter);

        foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent i = new Intent(getApplicationContext(), AddNewFood.class);
                    startActivity(i);
                }
            }
        });
    }
}
