package com.pjcribbin.healthtracker;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;

public class Database extends AppCompatActivity {
    protected static void createFoodTable(SQLiteDatabase database) {
        try {
            database.execSQL("CREATE TABLE IF NOT EXISTS Food" +
                    "(food_id INT PRIMARY KEY," +
                    "food_name VARCHAR(32)," +
                    "calories INT," +
                    "fat DOUBLE," +
                    "protein DOUBLE," +
                    "sugar DOUBLE," +
                    "salt DOUBLE," +
                    "fiber DOUBLE)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void insertIntoTable(SQLiteDatabase db, String tableName) {
      /*  try {

        }
        catch (Exception e) {

        } */
    }
}
