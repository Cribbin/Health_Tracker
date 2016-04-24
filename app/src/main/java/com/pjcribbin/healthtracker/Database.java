package com.pjcribbin.healthtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
    private static int version = 1;

    public Database(Context ctx) {
        super(ctx, "Fitness.db", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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
                    "potassium REAL)");

        } catch (Exception e) {
            Log.e("PJ", "Create food table error");
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        // Upgrade schema if there's any changes
    }
}
