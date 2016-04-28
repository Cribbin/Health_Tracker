package com.pjcribbin.healthtracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddFood extends AppCompatActivity {
    private final static String TAG = "PJ_Health_Tracker";
    private ArrayList<String> foodSelected;
    private static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        openDatabase();
        setUpFoodList();
    }


    // Sets up the list view of food in database
    private void setUpFoodList() {
        foodSelected = new ArrayList<>();
        final ListView foodList = (ListView) findViewById(R.id.food_list);
        Cursor c;

        try {
            c = db.rawQuery("SELECT * FROM Food ORDER BY food_name ASC", null);

            foodList.setAdapter(
                    new SimpleCursorAdapter(this, R.layout.food_row, c, new String[]{"_id", "food_name", "calories", "carbohydrates", "fat", "protein", "sodium", "sugar"}, new int[]{R.id.food_id, R.id.food_name, R.id.num_calories, R.id.num_carbohyrates, R.id.num_fats, R.id.num_protein, R.id.num_sodium, R.id.num_sugar}, 0)
            );
        } catch (Exception e) {
            Log.e(TAG, "Error setting up list from database");
            e.printStackTrace();
        }

        try {
            foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = (TextView) view.findViewById(R.id.food_id);
                    int foodId = Integer.parseInt((String) textView.getText());
                    view.findViewById(R.id.food_row).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.selected));

                    foodSelected.add(String.valueOf(foodId));
                    Log.i(TAG, "Item added");
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error on item click", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error on crete Food item");
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    // Called when New Food Button is clicked
    public void addNewFood(View view) {
        Intent i = new Intent(getApplicationContext(), AddNewFood.class);
        startActivity(i);
    }


    // Called when Add to Meal button is clicked
    public void addMeal(View view) {
        Intent i = new Intent(getApplicationContext(), AddNewMeal.class);

        if (foodSelected.size() > 0) {
            i.putStringArrayListExtra("foodIdList", foodSelected);
            startActivity(i);
        } else
            Toast.makeText(getApplicationContext(), "You must select at least one food", Toast.LENGTH_SHORT).show();
    }


    // Sets up the readable database
    private void openDatabase() {
        try {
            Database dbHelper = new Database(this);
            db = dbHelper.getReadableDatabase();
        } catch (Exception e) {
            Log.e(TAG, "Error opening database");
            e.printStackTrace();
        }
    }
}
