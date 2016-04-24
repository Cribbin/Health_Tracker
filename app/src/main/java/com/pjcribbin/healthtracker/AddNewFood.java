package com.pjcribbin.healthtracker;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewFood extends AppCompatActivity {
    private final static String TAG = "PJ_Health_Tracker";
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void addFood(View view) {
        EditText editFoodName = (EditText) findViewById(R.id.food_name);
        String foodName = editFoodName.getText().toString();

        EditText editCalories = (EditText) findViewById(R.id.food_calories);
        String calories = editCalories.getText().toString();

        EditText editCarbohydrates = (EditText) findViewById(R.id.food_carbohydrates);
        String carbohydrates = editCarbohydrates.getText().toString();

        EditText editFat = (EditText) findViewById(R.id.food_fat);
        String fat = editFat.getText().toString();

        EditText editProtein = (EditText) findViewById(R.id.food_protein);
        String protein = editProtein.getText().toString();

        EditText editSodium = (EditText) findViewById(R.id.food_sodium);
        String sodium = editSodium.getText().toString();

        EditText editPotassium = (EditText) findViewById(R.id.food_potassium);
        String potassium = editPotassium.getText().toString();


        Database dbHelper = new Database(this);
        db = dbHelper.getWritableDatabase();

        db.execSQL("DELETE FROM Food");

        String query = "INSERT INTO Food (food_name, calories, carbohydrates, fat, protein, sodium, potassium) VALUES (?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(query);


        if (foodName.matches("") || foodName.matches("\\s+")) {
            Toast.makeText(getApplicationContext(), "Food name required", Toast.LENGTH_SHORT).show();
        } else {
            statement.bindString(1, foodName);

            if (!calories.matches("")) {
                statement.bindString(2, calories);
            } else {
                statement.bindNull(2);
            }

            if (!carbohydrates.matches("")) {
                statement.bindString(3, carbohydrates);
            } else {
                statement.bindNull(3);
            }

            if (!fat.matches("")) {
                statement.bindString(4, fat);
            } else {
                statement.bindNull(4);
            }

            if (!protein.matches("")) {
                statement.bindString(5, protein);
            } else {
                statement.bindNull(5);
            }

            if (!sodium.matches("")) {
                statement.bindString(6, sodium);
            } else {
                statement.bindNull(6);
            }

            if (!potassium.matches("")) {
                statement.bindString(7, potassium);
            } else {
                statement.bindNull(7);
            }

            try {
                statement.execute();
            } catch (Exception e) {
                Log.e(TAG, "INSERT INTO Food Error:\n" + query);
                e.printStackTrace();
            } finally {
                db.close();
            }

            Intent i = new Intent(getApplicationContext(), AddFood.class);
            startActivity(i);
        }
    }
}
