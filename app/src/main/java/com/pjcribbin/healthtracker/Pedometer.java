package com.pjcribbin.healthtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Pedometer {
    Context context;
    private final static String TAG = "PJ_Health_Tracker";
    SQLiteDatabase db;

    private int threshold; // Point at which to trigger a step

    // Values to Calculate Number of Steps
    private float previousY;
    private float currentY;

    public Pedometer(Context context) {
        threshold = 20;
        previousY = 0;
        currentY = 0;

        this.context = context;
        openDatabase();
    }

    public void enableAccelerometerListening() {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    private SensorEventListener sensorEventListener =
            new SensorEventListener() {
                public void onSensorChanged(SensorEvent event) {
                    // Gather the values from accelerometer
                    float y = event.values[1];

                    // Fetch the current Y
                    currentY = y;

                    // Measure if a step is taken
                    if (Math.abs(currentY - previousY) > threshold) {
                        try {
                            db.compileStatement("UPDATE Num_Steps " +
                                    "SET steps = steps + 1 " +
                                    "WHERE day = date('now', 'localtime')").execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "Could not update steps");
                        }
                    }

                    previousY = y;
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
            Log.e(TAG, "Error opening database");
            e.printStackTrace();
        }
    }
}
