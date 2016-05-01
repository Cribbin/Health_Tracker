package com.pjcribbin.healthtracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Stats extends AppCompatActivity {
    private final static String TAG = "PJ_Health_Tracker";
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        openDatabase();

        LineChart stepsChart = (LineChart) findViewById(R.id.chart);
        ArrayList<ArrayList> stepsHistory = getStepsHistory();
        setUpChart(stepsChart, stepsHistory, "Number of steps taken each day", "steps");

        LineChart caloriesChart = (LineChart) findViewById(R.id.calories_chart);
        ArrayList<ArrayList> caloriesHistory = getCaloriesHistory();
        setUpChart(caloriesChart, caloriesHistory, "Number of calories consumed each day", "Calories");
    }

    private void setUpChart(LineChart chart, ArrayList<ArrayList> history, String description, String label) {
        chart.setLogEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setTouchEnabled(true);
        chart.setHighlightPerTapEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        chart.setDescription(description);

        ArrayList<Entry> vals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        for (int i = 0; i < history.get(0).size(); i++) {
            vals.add(new Entry(Float.parseFloat((String) history.get(0).get(i)), i));
            xVals.add((String) history.get(1).get(i));
        }

        LineDataSet set = new LineDataSet(vals, label);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);

        chart.invalidate();
    }

    private ArrayList<ArrayList> getStepsHistory() {
        ArrayList<ArrayList> stepsArrayList = new ArrayList<>();

        stepsArrayList.add(new ArrayList<String>());
        stepsArrayList.add(new ArrayList<Float>());

        try {
            Cursor c = db.rawQuery("SELECT day, steps FROM Num_Steps ORDER BY day DESC LIMIT 365", null);

            c.moveToFirst();
            do {
                stepsArrayList.get(0).add(c.getString(c.getColumnIndex("steps")));
                stepsArrayList.get(1).add(c.getString(c.getColumnIndex("day")));
            } while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            Log.e(TAG, "Error on gathering steps history\nStack Trace:\n" + Log.getStackTraceString(e));
        }

        Collections.reverse(stepsArrayList.get(0));
        Collections.reverse(stepsArrayList.get(1));
        return stepsArrayList;
    }

    private ArrayList<ArrayList> getCaloriesHistory() {
        ArrayList<ArrayList> caloriesArrayList = new ArrayList<>();

        caloriesArrayList.add(new ArrayList<String>());
        caloriesArrayList.add(new ArrayList<Float>());

        try {
            Cursor c = db.rawQuery("SELECT sum(calories) AS cal, date(timestamp) AS day " +
                    "FROM Meal_Entry INNER JOIN Meal ON Meal_Entry.meal_id = Meal._id " +
                    "INNER JOIN Food_Meal ON Meal._id = Food_Meal.meal_id " +
                    "INNER JOIN Food ON Food_Meal.food_id = Food._id " +
                    "GROUP BY day " +
                    "ORDER BY day DESC " +
                    "LIMIT 365", null);

            c.moveToFirst();

            do {
                caloriesArrayList.get(0).add(c.getString(c.getColumnIndex("cal")));
                caloriesArrayList.get(1).add(c.getString(c.getColumnIndex("day")));
            } while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            Log.e(TAG, "Error on gathering calories history\nStack Trace:\n" + Log.getStackTraceString(e));
        }

        Collections.reverse(caloriesArrayList.get(0));
        Collections.reverse(caloriesArrayList.get(1));
        return caloriesArrayList;
    }

    private void openDatabase() {
        try {
            Database dbHelper = new Database(this);
            db = dbHelper.getReadableDatabase();
            db.execSQL("PRAGMA foreign_keys = ON");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Could not open database", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error opening database\nStack Trace:\n" + Log.getStackTraceString(e));
        }
    }
}
