package com.pjcribbin.healthtracker;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class PedometerService extends IntentService {
    private final static String TAG = "PJ_Health_Tracker";

    public PedometerService() {
        super("PedometerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, intent.getStringExtra("message"));
    }
}
