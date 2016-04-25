package com.pjcribbin.healthtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
    private static int version = 3;
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

            Log.i(TAG, "Database created successfully");

        } catch (Exception e) {
            Log.e(TAG, "Could not create Food table");
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE Food");

        db.execSQL("CREATE TABLE IF NOT EXISTS Food" +
                "(_id INTEGER PRIMARY KEY," +
                "food_name TEXT," +
                "calories INTEGER," +
                "carbohydrates REAL," +
                "fat REAL," +
                "protein REAL," +
                "sodium REAL," +
                "sugar REAL)");

        Log.i(TAG, "Database updated to Version " + newVer);
    }
}
