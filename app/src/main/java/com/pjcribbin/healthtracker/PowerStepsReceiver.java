package com.pjcribbin.healthtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PowerStepsReceiver extends BroadcastReceiver {
    public PowerStepsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, PedometerService.class);
        context.startService(i);
    }
}
