package com.pjcribbin.healthtracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class DBFunctions {
    private SQLiteDatabase db;

    DBFunctions(SQLiteDatabase db) {
        this.db = db;
    }

    public void removeAllDataFromDatabase() {
        db.execSQL("DELETE FROM Food");
        db.execSQL("DELETE FROM Meal");
        db.execSQL("DELETE FROM Food_Meal");
        db.execSQL("DELETE FROM Meal_Entry");
        db.execSQL("DELETE FROM Num_Steps");
    }

    public void initializeTodayIntoNum_Steps() {
        String query = "INSERT INTO Num_Steps (steps) VALUES (?)";
        SQLiteStatement statement = db.compileStatement(query);
        statement.bindString(1, "0");
        statement.execute();
    }

    public String getCurrentStepCountFromDatabase() {
        Cursor c = db.rawQuery("SELECT steps FROM Num_Steps WHERE day = date('now', 'localtime')", null);
        if (c.getCount() < 1) {
            db.execSQL("INSERT INTO Num_Steps (steps) VALUES (0)");
            c = db.rawQuery("SELECT steps FROM Num_Steps WHERE day = date('now', 'localtime')", null);
        }
        c.moveToFirst();

        return c.getString(c.getColumnIndex("steps"));
    }
}
