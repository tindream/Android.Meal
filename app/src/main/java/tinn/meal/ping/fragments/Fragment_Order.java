package tinn.meal.ping.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.mapapi.SDKInitializer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import tinn.meal.ping.R;
import tinn.meal.ping.activity.BaiduMapActivity;
import tinn.meal.ping.enums.IListListener;
import tinn.meal.ping.enums.ILoadListener;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.info.loadInfo.SetInfo;
import tinn.meal.ping.info.setInfo.AdapterInfo;
import tinn.meal.ping.info.setInfo.LoaderInfo;
import tinn.meal.ping.support.AsyncListView;
import tinn.meal.ping.support.ViewHolder;

public class Fragment_Order extends Fragment_Base implements IListListener, ILoadListener {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.fragment_order, container, false);
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.load(R.id.order_context, R.id.order_load, R.id.order_text, false);
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        List<SetInfo> list = new ArrayList();
        list.add(new SetInfo(getString(R.string.btn_location)));
        new AsyncListView().setListener(this, this).init(getActivity(), list, R.layout.item_report);
        super.load(R.id.order_context, R.id.order_load, R.id.order_text, true);
    }

    @Override
    public <T> void onReady(ObservableEmitter<LoadInfo> emitter, ViewHolder holder, T object) {
        if (!(object instanceof SetInfo)) return;
        SetInfo obj = (SetInfo) object;

        emitter.onNext(new LoaderInfo(LoadType.setText, holder, R.id.report_name, obj.Message));
    }

    @Override
    public void onReady(LoadInfo info) {
        switch (info.Types) {
            case setAdapter:
                ListView listView = getActivity().findViewById(R.id.order_listView);
                //设置listView的Adapter
                listView.setAdapter(((AdapterInfo) info).adapter);
                listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
                    //我们需要的内容，跳转页面或显示详细信息
                    SetInfo setInfo = (SetInfo) ((AdapterInfo) info).list.get(position);
                    switch (setInfo.Message) {
                        default:
                            Intent intent = new Intent(getActivity(), BaiduMapActivity.class);
                            intent.putExtra("title", setInfo.Message);
                            startActivity(intent);
                            break;
                    }
                });
                break;
        }
    }
}
