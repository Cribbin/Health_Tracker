package com.pjcribbin.healthtracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class AddFood extends AppCompatActivity {
    private final static String TAG = "PJ_Health_Tracker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        Database dbHelper = new Database(this);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Food ORDER BY food_name ASC", null);
        c.getCount();

        ListView foodList = (ListView) findViewById(R.id.food_list);

        foodList.setAdapter(
                new SimpleCursorAdapter(this, R.layout.food_row, c, new String[] {"_id", "food_name", "calories", "carbohydrates", "fat", "protein", "sodium", "potassium"}, new int[] {R.id.food_id, R.id.food_name, R.id.num_calories, R.id.num_carbohyrates, R.id.num_fats, R.id.num_protein, R.id.num_sodium, R.id.num_potassium}, 0)
        );
        db.close();
    }

    public void addNewFood(View view) {
        Intent i = new Intent(getApplicationContext(), AddNewFood.class);
        startActivity(i);
    }
}
