package com.pjcribbin.healthtracker;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
    private static int version = 4;
    private final static String TAG = "PJ_Health_Tracker";

    public Database(Context ctx) {
        super(ctx, "Fitness.db", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.i(TAG, "Creating database");

            db.execSQL("CREATE TABLE IF NOT EXISTS Food" +
                    "(_id INTEGER PRIMARY KEY," +
                    "food_name TEXT," +
                    "calories INTEGER," +
                    "carbohydrates REAL," +
                    "fat REAL," +
                    "protein REAL," +
                    "sodium REAL," +
                    "sugar REAL)");

            db.execSQL("CREATE TABLE IF NOT EXISTS Meal" +
                    "(_id INTEGER PRIMARY KEY)");

            db.execSQL("CREATE TABLE IF NOT EXISTS Food_Meal" +
                    "(food_id INTEGER," +
                    "meal_id INTEGER," +
                    "PRIMARY KEY (food_id, meal_id)" +
                    ")");

            Log.i(TAG, "Database created successfully");

        } catch (Exception e) {
            Log.e(TAG, "Could not create Food table");
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        try {
            db.execSQL("DROP TABLE IF EXISTS Food");

            db.execSQL("CREATE TABLE IF NOT EXISTS Food" +
                    "(_id INTEGER PRIMARY KEY," +
                    "food_name TEXT," +
                    "calories INTEGER," +
                    "carbohydrates REAL," +
                    "fat REAL," +
                    "protein REAL," +
                    "sodium REAL," +
                    "sugar REAL)");

            db.execSQL("DROP TABLE IF EXISTS Meal");

            db.execSQL("CREATE TABLE IF NOT EXISTS Meal" +
                    "(_id INTEGER PRIMARY KEY)");

            db.execSQL("DROP TABLE IF EXISTS Food_Meal");

            db.execSQL("CREATE TABLE IF NOT EXISTS Food_Meal" +
                    "(food_id INTEGER," +
                    "meal_id INTEGER," +
                    "PRIMARY KEY (food_id, meal_id)" +
                    ")");

            Log.i(TAG, "Database updated from version " + oldVer + " to version " + newVer);
        } catch (Exception e) {
            Log.e(TAG, "Database failed to update from version " + oldVer + " to version " + newVer);
            e.printStackTrace();
        }
    }
}
