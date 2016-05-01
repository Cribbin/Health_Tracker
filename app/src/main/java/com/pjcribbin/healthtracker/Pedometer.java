package com.pjcribbin.healthtracker;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class Pedometer {
    Context context;
    private final static String TAG = "PJ_Health_Tracker";
    SQLiteDatabase db;
    Intent stepsIntent = new Intent("Step Taken");

    private int threshold; // Point at which to trigger a step

    // Values to Calculate Number of Steps
    private float previousY;
    private float currentY;

    public Pedometer(Context context) {
        threshold = 13;
        previousY = 0;
        currentY = 0;

        this.context = context;
        openDatabase();
    }

    public void enableAccelerometerListening() {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        Log.i(TAG, "Accelerometer listening enabled");
    }

    private SensorEventListener sensorEventListener =
            new SensorEventListener() {
                public void onSensorChanged(SensorEvent event) {
                    // Gather the values from accelerometer
                    currentY = event.values[1];

                    // Measure if a step is taken
                    if (Math.abs(currentY - previousY) > threshold) {
                        try {
                            if ((db.rawQuery("SELECT * FROM Num_Steps WHERE day = date('now', 'localtime')", null)).getCount() > 0)
                                db.compileStatement("UPDATE Num_Steps " +
                                        "SET steps = steps + 1 " +
                                        "WHERE day = date('now', 'localtime')").execute();
                            else {
                                db.execSQL("INSERT INTO Num_Steps (steps) VALUES (1)");
                            }
                            LocalBroadcastManager.getInstance(context).sendBroadcast(stepsIntent);
                        } catch (Exception e) {
                            Log.e(TAG, "Could not update steps\nStack Trace:\n" + Log.getStackTraceString(e));
                        }
                    }

                    previousY = currentY;
                }

                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    //Empty - Required by Class
                }
            };

    private void openDatabase() {
        try {
            Database dbHelper = new Database(context);
            db = dbHelper.getWritableDatabase();
            db.execSQL("PRAGMA foreign_keys = ON");
        } catch (Exception e) {
            Log.e(TAG, "Error opening database\nStack Trace:\n" + Log.getStackTraceString(e));
        }
    }
}
