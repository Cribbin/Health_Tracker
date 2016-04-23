package com.pjcribbin.healthtracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
                    .setTitle("Do you wish to exit?")
                    .setMessage("Do you want to exit the app?")
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

    public void displayHistory(View view) {
        setContentView(R.layout.activity_history);
    }

    public void displayAddMeal(View view) {
        setContentView(R.layout.activity_add_meal);
    }

    public void displayStats(View view) {
        setContentView(R.layout.activity_stats);
    }

    protected void createDatabase() {
        try {
            SQLiteDatabase database = this.openOrCreateDatabase("Fitness", MODE_PRIVATE, null);

            database.execSQL("CREATE TABLE IF NOT EXISTS Food" +
                    "(food_id INT PRIMARY KEY," +
                    "food_name VARCHAR(32)," +
                    "calories INT," +
                    "fat DOUBLE," +
                    "protein DOUBLE," +
                    "sugar DOUBLE," +
                    "salt DOUBLE," +
                    "fiber DOUBLE)");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
