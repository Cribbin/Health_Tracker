package com.pjcribbin.healthtracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class History extends AppCompatActivity {
    private final static String TAG = "PJ_Health_Tracker";
    private static SQLiteDatabase db;
    private String clickedMealName;
    private String clickedMealId;
    private String clickedMealDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        openDatabase();

        populateListViewWithMealEntries();
    }

    private void populateListViewWithMealEntries() {
        ListView mealList = (ListView) findViewById(R.id.meal_history_list);
        registerForContextMenu(mealList);
        Cursor c;

        try {
            c = db.rawQuery("SELECT _id, meal_name, meal_type, timestamp " +
                            "FROM Meal INNER JOIN Meal_Entry ON _id = meal_id", null);

            mealList.setAdapter(
                    new SimpleCursorAdapter(this, R.layout.meal_record_row, c, new String[]{"_id", "meal_name", "meal_type", "timestamp"}, new int[]{R.id.meal_id, R.id.meal_name, R.id.meal_type, R.id.meal_time}, 0)
            );
        } catch (Exception e) {
            Log.e(TAG, "Error setting up list from database\nStack Trace:\n" + Log.getStackTraceString(e));
            Toast.makeText(getApplicationContext(), "Error setting up list", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        clickedMealName = ((TextView) info.targetView.findViewById(R.id.meal_name)).getText().toString();
        clickedMealDate = ((TextView) info.targetView.findViewById(R.id.meal_time)).getText().toString();
        clickedMealId = ((TextView) info.targetView.findViewById(R.id.meal_id)).getText().toString();
        menu.setHeaderTitle(clickedMealName + "\n(" + clickedMealDate + ")");

        if (v.getId()==R.id.meal_history_list) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_history_options, menu);
        }
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
