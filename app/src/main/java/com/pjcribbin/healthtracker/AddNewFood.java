package com.pjcribbin.healthtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class AddNewFood extends AppCompatActivity {
    //SQLiteDatabase database = this.openOrCreateDatabase("Fitness", MODE_PRIVATE, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void addFood(View view) {
        Intent i = new Intent(getApplicationContext(), AddFood.class);
        startActivity(i);
    }
}
