package com.pjcribbin.healthtracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class AddMeal extends AppCompatActivity {
    private final static String TAG = "PJ_Health_Tracker";
    private static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        openDatabase();
        setUpMealList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setUpMealList() {
        ListView mealList = (ListView) findViewById(R.id.meal_list);
        Cursor c;

        try {
            c = db.rawQuery("SELECT * FROM Meal ORDER BY meal_name ASC", null);

            mealList.setAdapter(
                    new SimpleCursorAdapter(this, R.layout.meal_row, c, new String[]{"_id", "meal_name", "meal_type"}, new int[]{R.id.meal_id, R.id.meal_name, R.id.meal_type}, 0)
            );
        } catch (Exception e) {
            Log.e(TAG, "Error setting up list from database");
            e.printStackTrace();
        }

        try {
            mealList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = (TextView) view.findViewById(R.id.meal_id);
                    final String mealId =(String) textView.getText();
                    textView = (TextView) view.findViewById(R.id.meal_name);
                    final String mealName = (String) textView.getText();

                    new AlertDialog.Builder(AddMeal.this)
                            .setIcon(android.R.drawable.ic_menu_add)
                            .setTitle(mealName)
                            .setMessage("Do you want to track the meal " + mealName)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    String query = "INSERT INTO Meal_Entry (meal_id) VALUES (?)";
                                    SQLiteStatement statement = db.compileStatement(query);
                                    statement.bindString(1, mealId);
                                    statement.execute();

                                    Toast.makeText(getApplicationContext(), mealName + " recorded successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error on item click", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error on select meal item");
            e.printStackTrace();
        }
    }

    public void addNewMeal(View view) {
        Intent i = new Intent(getApplicationContext(), AddFood.class);
        startActivity(i);
    }

    private void openDatabase() {
        try {
            Database dbHelper = new Database(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.e(TAG, "Error opening database");
            e.printStackTrace();
        }
    }
}
