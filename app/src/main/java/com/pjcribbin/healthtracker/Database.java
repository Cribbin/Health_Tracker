package com.pjcribbin.healthtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
    private static int version = 24;
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
                    "(_id INTEGER PRIMARY KEY," +
                    "meal_type TEXT," +
                    "meal_name TEXT)");

            db.execSQL("CREATE TABLE IF NOT EXISTS Food_Meal" +
                    "(food_id INTEGER," +
                    "meal_id INTEGER," +
                    "qty INTEGER," +
                    "PRIMARY KEY (food_id, meal_id)" +
                    ")");

            db.execSQL("CREATE TABLE IF NOT EXISTS Meal_Entry" +
                    "(meal_id INTEGER," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "PRIMARY KEY (meal_id, timestamp)" +
                    ")");

            db.execSQL("CREATE TABLE IF NOT EXISTS Num_Steps" +
                    "(day DATETIME DEFAULT (date('now', 'localtime')) PRIMARY KEY," +
                    "steps INTEGER DEFAULT 0)");

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
                    "(_id INTEGER PRIMARY KEY," +
                    "meal_type TEXT," +
                    "meal_name TEXT)");

            db.execSQL("DROP TABLE IF EXISTS Food_Meal");

            db.execSQL("CREATE TABLE IF NOT EXISTS Food_Meal" +
                    "(food_id INTEGER," +
                    "meal_id INTEGER," +
                    "qty INTEGER," +
                    "PRIMARY KEY (food_id, meal_id)" +
                    ")");

            db.execSQL("DROP TABLE IF EXISTS Meal_Entry");

            db.execSQL("CREATE TABLE IF NOT EXISTS Meal_Entry" +
                    "(meal_id INTEGER," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "PRIMARY KEY (meal_id, timestamp)" +
                    ")");

            db.execSQL("DROP TABLE IF EXISTS Num_Steps");

            db.execSQL("CREATE TABLE IF NOT EXISTS Num_Steps" +
                    "(day DATETIME DEFAULT (date('now', 'localtime')) PRIMARY KEY," +
                    "steps INTEGER DEFAULT 0)");

            Log.i(TAG, "Database updated from version " + oldVer + " to version " + newVer);
        } catch (Exception e) {
            Log.e(TAG, "Database failed to update from version " + oldVer + " to version " + newVer);
            e.printStackTrace();
        }
    }
}
