package com.pjcribbin.healthtracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private String clickedFoodId;
    private String clickedFoodName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        openDatabase();
        setUpFoodList();
    }

    // Sets up the list view of food in database
    private void setUpFoodList() {
        final ListView foodList = (ListView) findViewById(R.id.food_list);
        foodSelected = new ArrayList<>();
        registerForContextMenu(foodList);
        Cursor c;

        try {
            c = db.rawQuery("SELECT * FROM Food ORDER BY food_name ASC", null);

            foodList.setAdapter(
                    new SimpleCursorAdapter(this,
                            R.layout.food_row,
                            c,
                            new String[]{"_id",
                                    "food_name",
                                    "calories",
                                    "carbohydrates",
                                    "fat",
                                    "protein",
                                    "sodium",
                                    "sugar"},
                            new int[]{R.id.food_id,
                                    R.id.food_name,
                                    R.id.num_calories,
                                    R.id.num_carbohyrates,
                                    R.id.num_fats,
                                    R.id.num_protein,
                                    R.id.num_sodium,
                                    R.id.num_sugar},
                            0)
            );
        } catch (Exception e) {
            Log.e(TAG, "Error setting up list from database\nStack Trace:\n" + Log.getStackTraceString(e));
            Toast.makeText(getApplicationContext(), "Could not set up food list", Toast.LENGTH_SHORT).show();
        }

        try {
            foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = (TextView) view.findViewById(R.id.food_id);
                    clickedFoodId = (String) textView.getText();

                    textView = (TextView) view.findViewById(R.id.food_name);
                    clickedFoodName = (String) textView.getText();
                    Toast.makeText(getApplicationContext(), clickedFoodName + " added", Toast.LENGTH_SHORT).show();

                    foodSelected.add(clickedFoodId);
                    Log.i(TAG, "Item added");
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error on item click", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error on create Food item\nStack Trace:\n" + Log.getStackTraceString(e));
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        clickedFoodName = ((TextView) info.targetView.findViewById(R.id.food_name)).getText().toString();
        clickedFoodId = ((TextView) info.targetView.findViewById(R.id.food_id)).getText().toString();
        menu.setHeaderTitle(clickedFoodName);

        if (v.getId()==R.id.food_list) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_food_options, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.edit_option:
                Log.v(TAG, "Meal edit option clicked for meal " + clickedFoodId + " (" + clickedFoodName + ")");
                return true;
            case R.id.delete_option:
                Log.v(TAG, "Meal delete option clicked for meal " + clickedFoodId + " (" + clickedFoodName + ")");
                deleteFoodFromDb();
                finish();
                startActivity(getIntent());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteFoodFromDb() {
        try {
            db.delete("Food", "_id = " + clickedFoodId, null);
            Toast.makeText(getApplicationContext(), "Deleted " + clickedFoodName, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Deleted meal " + clickedFoodId + " (" + clickedFoodName + ") from database");
        } catch (Exception e) {
            Log.e(TAG, "Error deleting meal " + clickedFoodId + " (" + clickedFoodName + ") from database\nStack Trace:\n" + Log.getStackTraceString(e));
            Toast.makeText(getApplicationContext(), "Error deleting " + clickedFoodName, Toast.LENGTH_SHORT).show();
        }
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
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        } else
            Toast.makeText(getApplicationContext(), "You must select at least one food", Toast.LENGTH_SHORT).show();
    }


    // Sets up the readable database
    private void openDatabase() {
        try {
            Database dbHelper = new Database(this);
            db = dbHelper.getReadableDatabase();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Could not open database", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error opening database\nStack Trace:\n" + Log.getStackTraceString(e));
        }
    }
}
