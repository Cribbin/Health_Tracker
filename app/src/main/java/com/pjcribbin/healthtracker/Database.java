package com.pjcribbin.healthtracker;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Database {
    protected static void createFoodTable(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS Food" +
                    "(food_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "food_name VARCHAR(32)," +
                    "calories INTEGER," +
                    "fat DOUBLE," +
                    "protein DOUBLE," +
                    "sugar DOUBLE," +
                    "salt DOUBLE," +
                    "fiber DOUBLE)");

        } catch (Exception e) {
            Log.e("PJ", "Create food table error");
            e.printStackTrace();
        }
    }

    protected static void insertIntoTable(SQLiteDatabase db, String tableName) {

    }
}
