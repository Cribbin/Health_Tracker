package com.pjcribbin.healthtracker;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

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

        String query = "INSERT INTO Food (food_name, calories, carbohydrates, fat, protein, sodium, potassium) VALUES (";

        if (!foodName.matches("")) {
            query += "'" + foodName + "', ";
            Log.i(TAG, "Food Name: " + query);
        } else {
            query += "NULL, ";
            Log.i(TAG, "Food Name: " + query);
        }

        if (!calories.matches("")) {
            query += calories + ", ";
            Log.i(TAG, "Calories: " + query);
        } else {
            query += "NULL, ";
            Log.i(TAG, "Calories: " + query);
        }

        if (!carbohydrates.matches("")) {
            query += carbohydrates + ", ";
            Log.i(TAG, "Carbohydrates: " + query);
        } else {
            query += "NULL, ";
            Log.i(TAG, "Carbohydrates: " + query);
        }

        if (!fat.matches("")) {
            query += fat + ", ";
            Log.i(TAG, "Fat: " + query);
        } else {
            query += "NULL, ";
            Log.i(TAG, "Fat: " + query);
        }

        if (!protein.matches("")) {
            query += protein + ", ";
            Log.i(TAG, "Protein: " + query);
        } else {
            query += "NULL, ";
            Log.i(TAG, "Protein: " + query);
        }

        if (!sodium.matches("")) {
            query += sodium + ", ";
            Log.i(TAG, "Sodium: " + query);
        } else {
            query += "NULL, ";
            Log.i(TAG, "Sodium: " + query);
        }

        if (!potassium.matches("")) {
            query += potassium + " )";
            Log.i(TAG, "Potassium: " + query);
        } else {
            query += "NULL) ";
            Log.i(TAG, "Potassium: " + query);
        }

        try {
            Log.i(TAG, "Yes");
        } catch (Exception e) {
            Log.e(TAG, "INSERT INTO Food Error:\n" + query);
            e.printStackTrace();
        }

        /*Intent i = new Intent(getApplicationContext(), AddFood.class);
        startActivity(i);*/
    }
}
