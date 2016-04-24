package com.pjcribbin.healthtracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class AddFood extends AppCompatActivity {
    private final static String TAG = "PJ";
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        Database dbHelper = new Database(this);
        Log.i("PJ", dbHelper.getDatabaseName());

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT _id, food_name FROM Food", null);
        c.getCount();

        ListView foodList = (ListView) findViewById(R.id.food_list);

        foodList.setAdapter(
                new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, new String[] {"food_name", "food_name"}, new int[] {android.R.id.text1}, 0)
        );
        db.close();
    }

    public void addNewFood(View view) {
        Intent i = new Intent(getApplicationContext(), AddNewFood.class);
        startActivity(i);
    }
}
