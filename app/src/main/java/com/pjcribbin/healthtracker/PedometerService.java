package com.pjcribbin.healthtracker;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class PedometerService extends IntentService {
    private final static String TAG = "PJ_Health_Tracker";
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

        // Initialize Values
        previousY = 0;
        currentY = 0;
        numSteps = 0;

        //Enable the listener - We will write this later in the class
        enableAccelerometerListening();
    }

    private void enableAccelerometerListening() {
        // Initialize the Sensor Manager
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    //Event handler for accelerometer events
    private SensorEventListener sensorEventListener =
            new SensorEventListener() {
                // Listens for Change in Acceleration, Displays, and Computes the Steps
                public void onSensorChanged(SensorEvent event) {
                    // Gather the values from accelerometer
                    float y = event.values[1];

                    // Fetch the current Y
                    currentY = y;

                    // Measure if a step is taken
                    if (Math.abs(currentY - previousY) > threshold) {
                        numSteps++;
                        Log.i(TAG, "Number of steps: " + numSteps);
                    }

                    // Store the previous Y
                    previousY = y;
                }

                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    //Empty - Required by Class
                }
            };
}
