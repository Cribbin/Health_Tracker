package com.pjcribbin.healthtracker;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class PedometerService extends IntentService {
    private final static String TAG = "PJ_Health_Tracker";
    SQLiteDatabase db;

    private int threshold; // Point at which to trigger a step

    // Values to Calculate Number of Steps
    private float previousY;
    private float currentY;
    private int numSteps;

    public PedometerService() {
        super("PedometerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, intent.getStringExtra("message"));

        threshold = 10;

        previousY = 0;
        currentY = 0;

        openDatabase();
        enableAccelerometerListening();
    }

    private void enableAccelerometerListening() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
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
                                    "WHERE day = CURRENT_DATE").execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.w(TAG, "Could not update");
                        }
                    }

                    // Store the previous Y
                    previousY = y;
                }

                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    //Empty - Required by Class
                }
            };

    private void openDatabase() {
        try {
            Database dbHelper = new Database(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.e(TAG, "Error opening database");
            e.printStackTrace();
        }
    }
}
