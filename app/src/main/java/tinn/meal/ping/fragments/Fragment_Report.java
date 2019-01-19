package tinn.meal.ping.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tinn.meal.ping.R;
import tinn.meal.ping.support.Config;

public class Fragment_Report extends Fragment_Base implements View.OnClickListener, View.OnLongClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.fragment_report, container, false);
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView textView = getActivity().findViewById(R.id.report_text);
        textView.setText(Config.Loading + ">Report");
        load();
    }

    public void load() {
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
