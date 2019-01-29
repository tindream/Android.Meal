package tinn.meal.ping.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import tinn.meal.ping.R;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.Method;

public class Fragment_Report extends Fragment_Base implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.fragment_report, container, false);
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.load(R.id.report_context, R.id.report_load, R.id.report_text, false);
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        LineChart chart = getActivity().findViewById(R.id.report_chart);

        List<Entry> entries = new ArrayList<Entry>();
        for (int i = 0; i < 100; i++)
            entries.add(new Entry(i, Method.round(100)));

        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
//        dataSet.setColor(getActivity().getResources().getColor(R.color.colorPrimary));
//        dataSet.setValueTextColor(getActivity().getResources().getColor(R.color.colorRed)); // styling, ...
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
        super.load(R.id.report_context, R.id.report_load, R.id.report_text, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
