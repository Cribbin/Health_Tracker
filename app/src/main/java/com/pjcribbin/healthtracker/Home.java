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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Home extends AppCompatActivity {
    private final static String TAG = "PJ_Health_Tracker";
    private static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        openDatabase();
        setUpSteps();

        Intent i = new Intent(getApplicationContext(), PedometerService.class);
        i.putExtra("message", "This is a service message");
        startService(i);
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
        String steps = "";

        try {
            String query = "INSERT INTO Num_Steps (steps) VALUES (?)";
            SQLiteStatement statement = db.compileStatement(query);
            statement.bindString(1, "0");
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "Day already exists");
        }

        try {
            Cursor c = db.rawQuery("SELECT steps FROM Num_Steps WHERE day = date('now', 'localtime')", null);
            c.moveToFirst();
            steps = c.getString(c.getColumnIndex("steps"));

            Log.i(TAG, "Steps: " + steps);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error on grabbing step count");
            Toast.makeText(getApplicationContext(), "Could not retrieve step count", Toast.LENGTH_SHORT).show();
        }

        TextView stepsTextView = (TextView) findViewById(R.id.step_count);
        stepsTextView.setText(steps);
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
        } catch (Exception e) {
            Log.e(TAG, "Error opening database");
            e.printStackTrace();
        }
    }
}
