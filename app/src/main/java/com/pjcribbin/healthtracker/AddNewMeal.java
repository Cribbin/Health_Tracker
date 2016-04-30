package com.pjcribbin.healthtracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class AddNewMeal extends AppCompatActivity {
    private final static String TAG = "PJ_Health_Tracker";
    private static ArrayList<String> foodIds = null;
    private static SQLiteDatabase db;
    private static Cursor cursor;
    String mealType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_meal);

        setUpMealTypeSpinner();
        openDatabase();

        if (getIntentIfExists()) {
            populateFoodListView();

            String output = "";
            for (int i = 0; i < foodIds.size(); i++) {
                output += foodIds.get(i) + "\n";
            }
            Log.i(TAG, "" + output);
        }
    }

    // If after adding an item to the meal
    private boolean getIntentIfExists() {
        if (getIntent() != null) {
            Intent i = getIntent();
            if (i.getStringArrayListExtra("foodIdList") != null) {
                foodIds = i.getStringArrayListExtra("foodIdList");
                return true;
            }
            return false;
        }
        return false;
    }


    // Called when Insert Food button in clicked
    public void displayAddFood(View view) {
        Intent i = new Intent(getApplicationContext(), AddFood.class);
        startActivity(i);
    }


    // Generates list view with selected food objects
    private void populateFoodListView() {
        ListView foodList = (ListView) findViewById(R.id.food_list);

        String query = "SELECT * FROM FOOD WHERE (_id = ";
        for (int i = 0; i < foodIds.size(); i++) {
            query += foodIds.get(i) + " OR _id = ";
        }
        query += "-1) ORDER BY food_name ASC";

        try {
            cursor = db.rawQuery(query, null);

            foodList.setAdapter(
                    new SimpleCursorAdapter(this, R.layout.food_row, cursor, new String[]{"_id", "food_name", "calories", "carbohydrates", "fat", "protein", "sodium", "sugar"}, new int[]{R.id.food_id, R.id.food_name, R.id.num_calories, R.id.num_carbohyrates, R.id.num_fats, R.id.num_protein, R.id.num_sodium, R.id.num_sugar}, 0)
            );
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error retrieving food info from database", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error retrieving food info from database\nStack Trace:\n" + Log.getStackTraceString(e));
        }
    }

    public void addMeal(View view) {
        String mealId = "-1";
        String mealName;
        String query = "";
        String checkQuery = "SELECT _id, meal_name FROM Meal Where meal_name = ";

        Cursor c;
        int nameInDb = -1;

        if (getIntentIfExists()) {
            EditText editName = (EditText) findViewById(R.id.meal_name);
            mealName = editName.getText().toString();

            if (mealName.matches("") || mealName.matches("\\s+")) {
                Toast.makeText(getApplicationContext(), "Meal name required", Toast.LENGTH_SHORT).show();
            } else {
                checkQuery += "'" + mealName + "'";
                try {
                    c = db.rawQuery(checkQuery, null);
                    nameInDb = c.getCount();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error checking database", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error checking database\nStack Trace:\n" + Log.getStackTraceString(e));
                }

                if (nameInDb > 0) {
                    Toast.makeText(getApplicationContext(), "Meal name " + mealName + " already exists", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        query = "INSERT INTO Meal (meal_type, meal_name) VALUES (?, ?)";
                        SQLiteStatement statement = db.compileStatement(query);
                        statement.bindString(1, mealType);
                        statement.bindString(2, mealName);
                        statement.execute();
                        Log.i(TAG, "Created Meal " + mealName);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error creating meal", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error inserting new meal\nStack Trace:\n" + Log.getStackTraceString(e));
                    }

                    try {
                        // Get meal ID
                        cursor = db.rawQuery("SELECT Max(_id) AS meal_id, meal_name, meal_type FROM Meal LIMIT 1", null);

                        cursor.moveToFirst();
                        mealId = cursor.getString(cursor.getColumnIndex("meal_id"));
                        mealName = cursor.getString(cursor.getColumnIndex("meal_name"));
                        mealType = cursor.getString(cursor.getColumnIndex("meal_type"));

                        Log.i(TAG, "Meal ID = " + mealId + "\nMeal Name: " + mealName + "\nMeal Type: " + mealType);

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error getting meal", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error getting meal ID\nStack Trace:\n" + Log.getStackTraceString(e));
                    }

                    try {
                        query = "INSERT INTO Food_Meal (food_id, meal_id) VALUES (?, ?)";
                        SQLiteStatement statement;
                        for (int i = 0; i < foodIds.size(); i++) {
                            statement = db.compileStatement(query);
                            statement.bindString(1, foodIds.get(i));
                            statement.bindString(2, mealId);
                            statement.execute();

                            Log.i(TAG, "Inserted " + foodIds.get(i) + " into " + mealId);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error inserting food into Food_Meal\nStack Trace:\n" + Log.getStackTraceString(e));
                        Toast.makeText(getApplicationContext(), "Error inserting into database", Toast.LENGTH_SHORT).show();
                    }

                    Intent i = new Intent(getApplicationContext(), AddMeal.class);
                    startActivity(i);
                }
            }
        } else {
            Log.i(TAG, "No food");
            Toast.makeText(getApplicationContext(), "You must add food before creating meal", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpMealTypeSpinner() {
        Spinner mealTypeSpinner = (Spinner) findViewById(R.id.meal_type_spinner);

        mealTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mealType = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mealType = "Other";
            }
        });
    }

    private void openDatabase() {
        try {
            Database dbHelper = new Database(this);
            db = dbHelper.getWritableDatabase();
            db.execSQL("PRAGMA foreign_keys = ON");
        } catch (Exception e) {
            Log.e(TAG, "Error opening database\nStack Trace:\n" + Log.getStackTraceString(e));
            Toast.makeText(getApplicationContext(), "Could not open database", Toast.LENGTH_SHORT).show();
        }
    }
}
