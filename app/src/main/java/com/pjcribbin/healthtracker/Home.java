package com.pjcribbin.healthtracker;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity {
    private final static String TAG = "PJ_Health_Tracker";
    private static DBFunctions dbFuctions;
    TextView stepsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        openDatabase();
        displayInitialStepCount();

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(stepsReceiver, new IntentFilter("Step Taken")); // Set up BroadcastReceiver to update step count
        startService(new Intent(getBaseContext(), PedometerService.class)); // Start step count
    }

    private void displayInitialStepCount() {
        stepsCount = (TextView) findViewById(R.id.step_count);

        String currentStepCount = getCurrentStepCount();
        stepsCount.setText(currentStepCount);
    }

    private String getCurrentStepCount() {
        String currentStepsCountInDatabase = "0";
        try {
            currentStepsCountInDatabase = dbFuctions.getCurrentStepCountFromDatabase();
        } catch (Exception e) {
            Log.e(TAG, "Could not update steps text view\nStack Trace:\n" + Log.getStackTraceString(e));
        }

        return currentStepsCountInDatabase;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpCalories();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset_option:
                Log.v(TAG, "Reset option clicked");
                buildResetAlertDialog();
                return true;
            case R.id.populate_option:
                Log.v(TAG, "Populate option clicked");
                startThreadToPopulateTablesWithSampleData();
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_lock_power_off)
                    .setTitle("Exit")
                    .setMessage("Do you want to exit Health Tracker?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    private void buildResetAlertDialog() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_delete)
                .setTitle("Reset Data")
                .setMessage("Are you sure you want to remove ALL your data? This will remove all your meals, food and step history and cannot be undone!")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAllData();
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteAllData() {
        try {
            dbFuctions.removeAllDataFromDatabase();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Could not remove data", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error: Could not remove data from tables\nStack Trace:\n" + Log.getStackTraceString(e));
        }

        try {
            dbFuctions.initializeTodayIntoNum_Steps();
            Log.i(TAG, "Today's date added");
        } catch (Exception e) {
            Log.w(TAG, "Day already exists\nStack Trace:\n" + Log.getStackTraceString(e));
        }
    }

    private void startThreadToPopulateTablesWithSampleData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dbFuctions.populateNum_StepsTableWithSampleData();
                    dbFuctions.populateFoodTableWithSampleData();
                    dbFuctions.populateMealsTableWithSampleData();
                    dbFuctions.populateMeal_EntryTableWithSampleData();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error on populating tables (Try to reset records first)", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error on table populate\nStack Trace:\n" + Log.getStackTraceString(e));
                }
            }
        }).start();
        Toast.makeText(getApplicationContext(), "Tables populated", Toast.LENGTH_SHORT).show();
    }

    private void setUpCalories() {
        TextView caloriesCount = (TextView) findViewById(R.id.calories_count);

        try {
            int caloriesConsumedToday = dbFuctions.getCaloriesConsumedToday();
            caloriesCount.setText(String.valueOf(caloriesConsumedToday));
        } catch (Exception e) {
            Log.e(TAG, "Error getting complete calories count\nStack Trace:\n" + Log.getStackTraceString(e));
            Toast.makeText(getApplicationContext(), "Could not retrieve today's calorie count", Toast.LENGTH_SHORT).show();
        }
    }

    public void displayHistory(View view) {
        Intent i = new Intent(getApplicationContext(), History.class);
        startActivity(i);
    }

    public void displayAddMeal(View view) {
        Intent i = new Intent(getApplicationContext(), AddMeal.class);
        startActivity(i);
    }

    public void displayStats(View view) {
        Intent i = new Intent(getApplicationContext(), Stats.class);
        startActivity(i);
    }

    private void openDatabase() {
        try {
            Database dbHelper = new Database(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            db.execSQL("PRAGMA foreign_keys = ON");
            dbFuctions = new DBFunctions(db);
        } catch (Exception e) {
            Log.e(TAG, "Error opening database\nStack Trace:\n" + Log.getStackTraceString(e));
            Toast.makeText(getApplicationContext(), "Could not open database", Toast.LENGTH_SHORT).show();
        }
    }

    private BroadcastReceiver stepsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String steps;
            Log.i(TAG, "Step taken");

            try {
                steps = dbFuctions.getStepsTakenToday();
                Log.i(TAG, "Count: " + steps);

                stepsCount.setText(steps);
            } catch (Exception e) {
                Log.e(TAG, "Error on steps\nStack Trace:\n" + Log.getStackTraceString(e));
            }
        }
    };
}
