package tinn.meal.ping.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import tinn.meal.ping.R;
import tinn.meal.ping.support.Method;
import tinn.meal.ping.ui.ChildActivity;

public class ChartActivity extends ChildActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.layoutResID = R.layout.activity_chart;
        super.onCreate(savedInstanceState);
        try {
            String title = getIntent().getStringExtra("title");
            toolbar.setTitle(title);

            LineChart chart = findViewById(R.id.chart1);
            List<Entry> entries = new ArrayList<Entry>();
            for (int i = 0; i < 100; i++)
                entries.add(new Entry(i, Method.round(100)));
            LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
            //dataSet.setColor(getActivity().getResources().getColor(R.color.colorPrimary));
            //dataSet.setValueTextColor(getActivity().getResources().getColor(R.color.colorRed)); // styling, ...
            LineData lineData = new LineData(dataSet);
            chart.setData(lineData);
            chart.invalidate();
        } catch (Exception e) {
            Method.log(e);
        }
    }
}
