package com.pjcribbin.healthtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void displayHistory(View view) {
        setContentView(R.layout.activity_history);
    }

    public void displayStats(View view) {
        setContentView(R.layout.activity_stats);
    }
}
