package com.pjcribbin.healthtracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;

public class AddNewMeal extends AppCompatActivity {
    private final static String TAG = "PJ_Health_Tracker";
    private static ArrayList<String> foodIds = null;
    private static SQLiteDatabase db;
    private static String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_meal);

        Spinner mealTypeSpinner = (Spinner) findViewById(R.id.meal_type_spinner);

        mealTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                s= "Nothing selected";
            }
        });

        if (getIntentIfExists()) {
            makeNewMeal();
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

    private void makeNewMeal() {
        try {
            Database dbHelper = new Database(this);
            String query = "INSERT INTO Meal(meal_type) VALUES(?)";
            SQLiteStatement statement = db.compileStatement(query);

            for (int i = 0; i < foodIds.size(); i++) {
                query += foodIds.get(i) + " OR _id = ";
            }
            query += "_id)";

            db = dbHelper.getWritableDatabase();
            //Cursor c = db.rawQuery(query, null);



        } catch (Exception e) {
            Log.e(TAG, "Error making new meal");
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void addMeal(View view) {
        Log.i(TAG, s);
    }
}
