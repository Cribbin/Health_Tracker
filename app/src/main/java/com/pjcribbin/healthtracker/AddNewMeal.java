package com.pjcribbin.healthtracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

public class AddNewMeal extends AppCompatActivity {
    private final static String TAG = "PJ_Health_Tracker";
    private static ArrayList<String> foodIds = null;
    private static SQLiteDatabase db;
    private static Cursor cursor;
    private static String mealType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_meal);

        setUpMealTypeSpinner();

        if (getIntentIfExists()) {
            populateFoodListView();

            String output = "";
            for (int i = 0; i < foodIds.size(); i++) {
                output += foodIds.get(i) + "\n";
            }
            Log.i(TAG, "" + output);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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

    public void displayAddFood(View view) {
        Intent i = new Intent(getApplicationContext(), AddFood.class);
        startActivity(i);
    }

    private void populateFoodListView() {
        //Open Database
        try {
            Database dbHelper = new Database(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.e(TAG, "Error opening database");
            e.printStackTrace();
        }

        ListView foodList = (ListView) findViewById(R.id.food_list);

        // Generate query based on food ids given from intent
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
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error retrieving food info from database", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error: Query:\n" + query);
            e.printStackTrace();
        }
    }

    public void addMeal(View view) {
        if (getIntentIfExists()) {
            Log.i(TAG, mealType);

            try {
                // Insert new meal
                String query = "INSERT INTO Meal(meal_type) VALUES(?)";
                SQLiteStatement statement = db.compileStatement(query);
                statement.bindString(1, mealType);
                statement.execute();
            } catch (Exception e) {
                Log.e(TAG, "Error inserting new meal");
                e.printStackTrace();
            }

            try {
                // Get meal ID
                cursor = db.rawQuery("SELECT Max(_id) AS meal_id FROM Meal LIMIT 1", null);

                cursor.moveToFirst();
                int mealId = cursor.getInt(cursor.getColumnIndex("meal_id"));

                Log.i(TAG, "Meal ID = " + mealId + "\nMeal Type: " + mealType);

            /*for (int i = 0; i < foodIds.size(); i++) {
                query += foodIds.get(i) + " OR _id = ";
            }
            query += "_id)";*/

            } catch (Exception e) {
                Log.e(TAG, "Error adding food to meal");
                e.printStackTrace();
            } finally {
                db.close();
            }
        } else {
            Log.e(TAG, "No food");
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
}
