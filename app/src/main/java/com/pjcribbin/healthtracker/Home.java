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
import android.view.MenuItem;
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
        setUpCalories();

        stepsCount = (TextView) findViewById(R.id.step_count);

        startService(new Intent(getBaseContext(), PedometerService.class)); // Start step count

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_option:
                Log.v(TAG, "Settings option clicked");
                return true;
            case R.id.reset_option:
                Log.v(TAG, "Reset option clicked");
                buildResetAlertDialog();
                return true;
            case R.id.populate_option:
                Log.v(TAG, "Populate option clicked");
                populateTables();
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
            db.execSQL("DELETE FROM Food");
            db.execSQL("DELETE FROM Meal");
            db.execSQL("DELETE FROM Food_Meal");
            db.execSQL("DELETE FROM Meal_Entry");
            db.execSQL("DELETE FROM Num_Steps");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Could not remove data", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error: Could not remove data from tables\nStack Trace:\n" + Log.getStackTraceString(e));
        }

        try {
            String query = "INSERT INTO Num_Steps (steps) VALUES (?)";
            SQLiteStatement statement = db.compileStatement(query);
            statement.bindString(1, "0");
            statement.execute();
            Log.i(TAG, "Today's date added");
        } catch (Exception e) {
            Log.w(TAG, "Day already exists\nStack Trace:\n" + Log.getStackTraceString(e));
        }
    }

    private void populateTables() {
        try {
            // Populate Steps
            db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                    "VALUES ('2016-04-29', 5045)");
            db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                    "VALUES ('2016-04-28', 3000)");
            db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                    "VALUES ('2016-04-27', 5000)");
            db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                    "VALUES ('2016-04-26', 3478)");
            db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                    "VALUES ('2016-04-25', 4812)");
            db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                    "VALUES ('2016-04-24', 7122)");
            db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                    "VALUES ('2016-04-23', 4598)");
            db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                    "VALUES ('2016-04-22', 6444)");
            db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                    "VALUES ('2016-04-21', 5012)");
            db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                    "VALUES ('2016-04-20', 3897)");
            db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                    "VALUES ('2016-04-19', 3924)");
            db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                    "VALUES ('2016-04-18', 4001)");
            db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                    "VALUES ('2016-04-17', 5159)");


            // Populate Food
            db.execSQL("INSERT INTO Food (food_name, calories, carbohydrates, fat, protein, sodium, sugar) " +
                    "VALUES ('Apple', 95, 25, 0.3, 0.5, 1.8, 19)");
            db.execSQL("INSERT INTO Food (food_name, calories, carbohydrates, fat, protein, sodium, sugar) " +
                    "VALUES ('Weetabix Biscuit', 67, 12.85, 0.4, 2.15, 0.05, 0.85)");
            //db.execSQL("INSERT INTO Food (food_name, calories, carbohydrates, fat, protein, sodium, sugar) " +
                    //"VALUES ()");
            //db.execSQL("INSERT INTO Food (food_name, calories, carbohydrates, fat, protein, sodium, sugar) " +
                    //"VALUES ()");
            //db.execSQL("INSERT INTO Food (food_name, calories, carbohydrates, fat, protein, sodium, sugar) " +
                    //"VALUES ()");

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error on populating tables (Try to reset records first)", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error on table populate\nStack Trace:\n" + Log.getStackTraceString(e));
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
        Intent d = new Intent(Intent.ACTION_DATE_CHANGED);

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
            db = dbHelper.getReadableDatabase();
            db.execSQL("PRAGMA foreign_keys = ON");
        } catch (Exception e) {
            Log.e(TAG, "Error opening database\nStack Trace:\n" + Log.getStackTraceString(e));
            Toast.makeText(getApplicationContext(), "Could not open database", Toast.LENGTH_SHORT).show();
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
                    Log.e(TAG, "Error on grabbing step count\nStack Trace:\n" + Log.getStackTraceString(e));
                    error = true;
                }
            }
        }
    };
}
