package com.pjcribbin.healthtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class DateReceiver extends BroadcastReceiver {
    private final static String TAG = "PJ_Health_Tracker";
    private SQLiteDatabase db;
    private Context context;
    public DateReceiver() {
    }

    @Override
    public void onReceive(Context c, Intent intent) {
        this.context = c;
        Thread thread = new Thread(r);
        thread.start();
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "Add new date thread started");

            try {
                Database dbHelper = new Database(context);
                db = dbHelper.getWritableDatabase();
                db.execSQL("PRAGMA foreign_keys = ON");
            } catch (Exception e) {
                Log.e(TAG, "Error opening database");
                e.printStackTrace();
            }

            try {
                String query = "INSERT INTO Num_Steps (steps) VALUES (?)";
                SQLiteStatement statement = db.compileStatement(query);
                statement.bindString(1, "0");
                statement.execute();
                Log.i(TAG, "Today's date added");
            } catch (Exception e) {
                e.printStackTrace();
                Log.w(TAG, "Day already exists");
            }

            Log.i(TAG, "Add new date tread complete");
        }
    };
}
