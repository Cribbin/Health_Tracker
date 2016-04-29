package com.pjcribbin.healthtracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.DropBoxManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class Stats extends AppCompatActivity {
    private final static String TAG = "PJ_Health_Tracker";
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        openDatabase();

        createLineChart();
    }

    private void createLineChart() {
        ArrayList<ArrayList> stepsHistory = getStepsHistory();
        LineChart chart = (LineChart) findViewById(R.id.chart);

        // Set chart attributes
        chart.setLogEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDescription("Number of steps taken each day");

        ArrayList<Entry> vals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        for (int i = 0; i < stepsHistory.get(0).size(); i ++) {
            vals.add(new Entry(Float.parseFloat((String)stepsHistory.get(0).get(i)), i));
            xVals.add((String)stepsHistory.get(1).get(i));
        }

        LineDataSet set = new LineDataSet(vals, "Steps");
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
            Cursor c = db.rawQuery("SELECT day, steps FROM Num_Steps", null);

            c.moveToFirst();

            while (c.moveToNext()) {
                stepsArrayList.get(0).add(c.getString(c.getColumnIndex("steps")));
                stepsArrayList.get(1).add(c.getString(c.getColumnIndex("day")));
            }
            c.close();
        } catch (Exception e) {
            Log.e(TAG, "Error on gathering steps history\nStack Trace:\n" + Log.getStackTraceString(e));
        }

        for (int i = 0; i < stepsArrayList.get(0).size(); i++) {
            Log.i(TAG, "Steps: " + stepsArrayList.get(0).get(i) + "\nDay: " + stepsArrayList.get(1).get(i));
        }
        return stepsArrayList;
    }

    private void openDatabase() {
        try {
            Database dbHelper = new Database(this);
            db = dbHelper.getReadableDatabase();
            db.execSQL("PRAGMA foreign_keys = ON");
        } catch (Exception e) {
            Log.e(TAG, "Error opening database");
            e.printStackTrace();
        }
    }
}
