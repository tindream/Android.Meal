package tinn.meal.ping.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.reactivex.ObservableEmitter;
import tinn.meal.ping.R;
import tinn.meal.ping.data.SQLiteServer;
import tinn.meal.ping.enums.ILoadListener;
import tinn.meal.ping.enums.IObservableListener;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.support.AsyncAdapter;
import tinn.meal.ping.support.Config;

public class Fragment_Load extends Fragment_Base implements ILoadListener, IObservableListener {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AsyncAdapter().setListener(this, this).init(LoadType.local);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.fragment_load, container, false);
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView textView = getActivity().findViewById(R.id.load_text);
        textView.setText(Config.Loading);
    }

    @Override
    public void onReady(ObservableEmitter<LoadInfo> emitter, LoadInfo info) throws Exception {
        switch (info.type) {
            case local:
                new SQLiteServer().Load(emitter);
                break;
        }
    }

    @Override
    public void onReady(LoadInfo info) {
        switch (info.type) {
            case load:
                onListener(LoadType.load);
                break;
            case complete:
                onListener(LoadType.complete);
                break;
            case error:
                TextView textView = getActivity().findViewById(R.id.load_text);
                String desc = info.type + "\n" + info.msg;
                int end = desc.indexOf("\n");
                int color = getActivity().getColor(R.color.colorRed);
                SpannableString span = new SpannableString(desc);
                span.setSpan(new ForegroundColorSpan(color), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(span); //更新UI
                break;
        }
    }
}
