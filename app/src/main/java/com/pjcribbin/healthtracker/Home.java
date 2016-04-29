package com.pjcribbin.healthtracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity {
    private final static String TAG = "PJ_Health_Tracker";
    private static SQLiteDatabase db;
    TextView stepsCount;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            stepsCount.setText((String) message.obj);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        openDatabase();
        setUpSteps();
        setUpCalories();

        stepsCount = (TextView) findViewById(R.id.step_count);

        Intent i = new Intent(getApplicationContext(), PedometerService.class);
        i.putExtra("message", "This is a service message");
        startService(i);

        Thread thread = new Thread(r);
        thread.start();
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

    // TODO Make a tidier setUpSteps where it checks if the current day has been initialized or not
    private void setUpSteps() {
        try {
            String query = "INSERT INTO Num_Steps (steps) VALUES (?)";
            SQLiteStatement statement = db.compileStatement(query);
            statement.bindString(1, "0");
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "Day already exists");
        }
    }

    private void setUpCalories() {
        TextView caloriesCount = (TextView) findViewById(R.id.calories_count);

        try {
            Cursor c = db.rawQuery("SELECT sum(calories) AS cal " +
                    "FROM Meal_Entry INNER JOIN Meal ON Meal_Entry.meal_id = Meal._id " +
                    "INNER JOIN Food_Meal ON Meal._id = Food_Meal.meal_id " +
                    "INNER JOIN Food ON Food_Meal.food_id = Food._id " +
                    "WHERE date(timestamp) = date('now', 'localtime')" , null);

            c.moveToFirst();
            Log.i(TAG, "Calories count: " + c.getInt(c.getColumnIndex("cal")));
            caloriesCount.setText(String.valueOf(c.getInt(c.getColumnIndex("cal"))));
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

    public void displayAddWorkout(View view) {
        Intent i = new Intent(getApplicationContext(), AddWorkout.class);
        startActivity(i);
    }

    public void displayStats(View view) {
        Intent i = new Intent(getApplicationContext(), Stats.class);
        startActivity(i);
    }

    private void openDatabase() {
        try {
            Database dbHelper = new Database(this);
            db = dbHelper.getReadableDatabase();
            db.execSQL("PRAGMA foreign_keys = ON");
        } catch (Exception e) {
            Log.e(TAG, "Error opening database");
            e.printStackTrace();
        }
    }

    Runnable r = new Runnable() {
        String steps = "";
        boolean error = false;
        Cursor c;

        @Override
        public void run() {
            while (!error) {
                try {
                    c = db.rawQuery("SELECT steps FROM Num_Steps WHERE day = date('now', 'localtime')", null);
                    c.moveToFirst();
                    steps = c.getString(c.getColumnIndex("steps"));

                    c.close();
                    Message message = Message.obtain();
                    message.obj = steps;
                    message.setTarget(handler);
                    message.sendToTarget();
                    Thread.sleep(500); // Updates every .5 seconds
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error on grabbing step count");
                    error = true;
                }
            }
        }
    };
}
