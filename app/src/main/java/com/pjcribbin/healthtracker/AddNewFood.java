package com.pjcribbin.healthtracker;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class AddNewFood extends AppCompatActivity {
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


        db = this.openOrCreateDatabase("Fitness", MODE_PRIVATE, null);
        Database.createFoodTable(db);

        String query = "INSERT INTO Food (food_name, calories, carbohydrates, fat, protein, sodium, potassium) VALUES (";

        if (!foodName.matches("")) {
            query += "'" + foodName + "', ";
            Log.i("PJ", "Food Name: " + query);
        } else {
            query += "NULL, ";
            Log.i("PJ", "Food Name: " + query);
        }

        if (!calories.matches("")) {
            query += calories + ", ";
            Log.i("PJ", "Calories: " + query);
        } else {
            query += "NULL, ";
            Log.i("PJ", "Calories: " + query);
        }

        if (!carbohydrates.matches("")) {
            query += carbohydrates + ", ";
            Log.i("PJ", "Carbohydrates: " + query);
        } else {
            query += "NULL, ";
            Log.i("PJ", "Carbohydrates: " + query);
        }

        if (!fat.matches("")) {
            query += fat + ", ";
            Log.i("PJ", "Fat: " + query);
        } else {
            query += "NULL, ";
            Log.i("PJ", "Fat: " + query);
        }

        if (!protein.matches("")) {
            query += protein + ", ";
            Log.i("PJ", "Protein: " + query);
        } else {
            query += "NULL, ";
            Log.i("PJ", "Protein: " + query);
        }

        if (!sodium.matches("")) {
            query += sodium + ", ";
            Log.i("PJ", "Sodium: " + query);
        } else {
            query += "NULL, ";
            Log.i("PJ", "Sodium: " + query);
        }

        if (!potassium.matches("")) {
            query += potassium + " )";
            Log.i("PJ", "Potassium: " + query);
        } else {
            query += "NULL) ";
            Log.i("PJ", "Potassium: " + query);
        }

        try {
            Log.i("PJ", "Yes");
        } catch (Exception e) {
            Log.e("PJ", "INSERT INTO Food Error:\n" + query);
            e.printStackTrace();
        }

        /*Intent i = new Intent(getApplicationContext(), AddFood.class);
        startActivity(i);*/
    }
}
