package com.pjcribbin.healthtracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AddFood extends AppCompatActivity {
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        db = this.openOrCreateDatabase("Fitness", MODE_PRIVATE, null);

        Database.createFoodTable(db);

        ListView foodList = (ListView) findViewById(R.id.food_list);

        final ArrayList<String> food = new ArrayList<>();
        food.add("+ Add new food");

        try {
            Cursor c = db.rawQuery("SELECT * FROM Food", null);

            int nameIndex = c.getColumnIndex("food_name");
            int idIndex = c.getColumnIndex("food_id");
            int numRows = c.getCount();

            if (numRows > 0) {
                c.moveToFirst();

                food.add(c.getString(nameIndex));
                Log.i("PJ", "Count: " + numRows);

                while (c.moveToNext()) {
                    food.add(c.getString(nameIndex));
                }
            } else {
                Log.i("PJ", "No rows found in Food");
            }

        } catch (Exception e) {
            Log.e("PJ", "Error on cursor");
            e.printStackTrace();
        } finally {
            db.close();
        }

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
