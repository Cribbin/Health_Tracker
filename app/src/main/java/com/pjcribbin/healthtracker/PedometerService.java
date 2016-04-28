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

    public PedometerService() {
        super("PedometerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, intent.getStringExtra("message"));

        Pedometer p = new Pedometer(this);
        p.enableAccelerometerListening();
    }
}
