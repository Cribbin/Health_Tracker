package com.pjcribbin.healthtracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class History extends AppCompatActivity {
    private final static String TAG = "PJ_Health_Tracker";
    private static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        openDatabase();

        populateListViewWithMealEntries();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void populateListViewWithMealEntries() {
        ListView mealList = (ListView) findViewById(R.id.meal_history_list);
        Cursor c;

        try {
            c = db.rawQuery("SELECT _id, meal_name, meal_type, timestamp " +
                            "FROM Meal INNER JOIN Meal_Entry ON _id = meal_id", null);

            mealList.setAdapter(
                    new SimpleCursorAdapter(this, R.layout.meal_record_row, c, new String[]{"_id", "meal_name", "meal_type", "timestamp"}, new int[]{R.id.meal_id, R.id.meal_name, R.id.meal_type, R.id.meal_time}, 0)
            );
        } catch (Exception e) {
            Log.e(TAG, "Error setting up list from database");
            e.printStackTrace();
        }
    }

    private void openDatabase() {
        try {
            Database dbHelper = new Database(this);
            db = dbHelper.getWritableDatabase();
            db.execSQL("PRAGMA foreign_keys = ON");
        } catch (Exception e) {
            Log.e(TAG, "Error opening database");
            e.printStackTrace();
        }
    }
}
