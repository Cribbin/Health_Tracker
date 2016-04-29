package com.pjcribbin.healthtracker;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        LineChart chart = (LineChart) findViewById(R.id.chart);


        chart.setLogEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setTouchEnabled(true);

        ArrayList<Entry> vals = new ArrayList<>();

        vals.add(new Entry(100, 0));
        vals.add(new Entry(200, 1));
        vals.add(new Entry(50, 2));
        vals.add(new Entry(250, 3));

        LineDataSet set = new LineDataSet(vals, "Steps");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        ArrayList<String> xVals = new ArrayList<>();
        xVals.add("23/06/2015");
        xVals.add("27/06/2015");
        xVals.add("01/07/2015");
        xVals.add("08/07/2015");

        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);

        chart.invalidate();
    }
}
