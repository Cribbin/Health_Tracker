package com.pjcribbin.healthtracker;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class PedometerService extends Service {
    private final static String TAG = "PJ_Health_Tracker";
    SQLiteDatabase db;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Pedometer service started");
        Pedometer p = new Pedometer(this);
        p.enableAccelerometerListening();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
