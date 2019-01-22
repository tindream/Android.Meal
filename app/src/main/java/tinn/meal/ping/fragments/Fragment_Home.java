package tinn.meal.ping.fragments;

import android.app.Activity;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tinn.meal.ping.R;
import tinn.meal.ping.WebViewActivity;
import tinn.meal.ping.enums.ILoadListener;
import tinn.meal.ping.enums.IObservableListener;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.support.AsyncAdapter;
import tinn.meal.ping.support.AsyncLoad;
import tinn.meal.ping.support.AsyncTime;
import tinn.meal.ping.support.Cache;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.Method;
import tinn.meal.ping.view.View_About;
import tinn.meal.ping.view.View_Ask;

public class Fragment_Home extends Fragment_Base implements View.OnClickListener {
    private boolean loadComplete;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.fragment_home, container, false);
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().findViewById(R.id.home_minus).setOnClickListener(this);
        getActivity().findViewById(R.id.home_add).setOnClickListener(this);
        getActivity().findViewById(R.id.home_btn).setOnClickListener(this);
        TextView textView = getActivity().findViewById(R.id.home_text);
        textView.setText(Config.Loading + ">Home");
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            //更新界面数据，如果数据还在下载中，就显示加载框
            Load(loadComplete);
        } else {
            //关闭加载框
        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        //去服务器下载数据
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_minus:
                View_About view_about = new View_About();
                view_about.init(getActivity());
                view_about.show();
                break;
            case R.id.home_add:
                View_Ask view_ask = new View_Ask();
                view_ask.init(getActivity());
                view_ask.show("Please Confirm Delete Item");
                break;
            case R.id.home_btn:
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                //将text框中的值传入
                intent.putExtra("title", "日志");
                File file = new File(Environment.getExternalStorageDirectory(), "/Meal/log.txt");
                intent.putExtra("file", "file://" + file.toString());
                //为了接受SecondActivity中的值，不用startAcitivity(intent)
                startActivityForResult(intent, 1000);
                break;
        }
    }

    private void Load(boolean complete) {
        Activity activity = getActivity();
        if (activity == null) return;
        LinearLayout home_load = activity.findViewById(R.id.home_load);
        if (home_load == null) return;
        home_load.setVisibility(complete ? View.GONE : View.VISIBLE);
        if (!complete) {
            ViewGroup.LayoutParams layoutParams = home_load.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            TextView textView = getActivity().findViewById(R.id.load_text);
            textView.setText(Config.Loading);
        }
        getActivity().findViewById(R.id.home_context).setVisibility(complete ? View.VISIBLE : View.GONE);
    }

    public void complete() {
        loadComplete = true;
        Load(true);
    }

    public void error(LoadInfo info) {
        TextView textView = getActivity().findViewById(R.id.load_text);
        String desc = info.type + "\n" + info.msg;
        int end = desc.indexOf("\n");
        int color = getActivity().getColor(R.color.colorRed);
        SpannableString span = new SpannableString(desc);
        span.setSpan(new ForegroundColorSpan(color), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(span); //更新UI
    }
}
