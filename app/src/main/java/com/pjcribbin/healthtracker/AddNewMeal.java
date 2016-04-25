package com.pjcribbin.healthtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import java.util.ArrayList;

public class AddNewMeal extends AppCompatActivity {
    private final static String TAG = "PJ_Health_Tracker";
    private static ArrayList<String> foodIds = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_meal);

        if (getIntentIfExists()) {
            Log.i(TAG, "Got it!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private boolean getIntentIfExists() {
        if (getIntent() != null) {
            Intent i = getIntent();
            if (i.getStringArrayListExtra("foodIdList") != null) {
                foodIds = i.getStringArrayListExtra("foodIdList");
                return true;
            }
            return false;
        }
        return false;
    }

    public void displayAddFood(View view) {
        Intent i = new Intent(getApplicationContext(), AddFood.class);
        startActivity(i);
    }

    public void addMeal(View view) {
        Log.i(TAG, "Meal added successfully");
    }
}
