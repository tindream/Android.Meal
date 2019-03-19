package tinn.meal.ping.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import tinn.meal.ping.activity.LoginActivity;
import tinn.meal.ping.R;
import tinn.meal.ping.activity.WebActivity;
import tinn.meal.ping.data.SQLiteServer;
import tinn.meal.ping.enums.IListListener;
import tinn.meal.ping.enums.ILoadListener;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.enums.requestType;
import tinn.meal.ping.info.setInfo.AdapterInfo;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.info.setInfo.LoaderInfo;
import tinn.meal.ping.info.loadInfo.SetInfo;
import tinn.meal.ping.support.AsyncListView;
import tinn.meal.ping.support.AsyncWait;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.Method;
import tinn.meal.ping.support.ViewHolder;

public class Fragment_My extends Fragment_Base implements IListListener, ILoadListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.fragment_my, container, false);
        return messageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.load(R.id.my_context, R.id.my_load, R.id.my_text, false);
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        loadUser();

        List<SetInfo> list = new ArrayList();
        list.add(new SetInfo(null));
        list.add(new SetInfo(R.drawable.ic_home, getString(R.string.nav_home)));
        list.add(new SetInfo(R.drawable.ic_report, getString(R.string.nav_report)));
        list.add(new SetInfo(R.drawable.ic_order, getString(R.string.nav_order)));
        list.add(new SetInfo(null));
        list.add(new SetInfo(0, getString(R.string.btn_log)));
        list.add(new SetInfo(0, getString(R.string.nav_about), getString(R.string.version)));
        list.add(new SetInfo(null));
        list.add(new SetInfo(0, getString(R.string.btn_logout)));
        list.add(new SetInfo(0, getString(R.string.btn_close)));
        new AsyncListView().setListener(this, this).init(getActivity(), list, R.layout.item_set);
        super.load(R.id.my_context, R.id.my_load, R.id.my_text, true);
    }

    public void loadUser() {
        if (!isFirstVisible) return;
        TextView textView = getActivity().findViewById(R.id.my_name);
        textView.setText(Config.Admin.Display);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == requestType.loginResult) {
            switch (requestCode) {
                case requestType.loginUpdate:
                    loadUser();
                    break;
            }
        }
    }

    @Override
    public <T> void onReady(ObservableEmitter<LoadInfo> emitter, ViewHolder holder, T object) {
        if (!(object instanceof SetInfo)) return;
        SetInfo obj = (SetInfo) object;

        emitter.onNext(new LoaderInfo(LoadType.setText, holder, R.id.set_name, obj.Message));
        emitter.onNext(new LoaderInfo(LoadType.setText, holder, R.id.set_desc, obj.desc));
        emitter.onNext(new LoaderInfo(LoadType.setImageId, holder, R.id.set_img, obj.imageId));
        if (obj.iHeard) {
            emitter.onNext(new LoaderInfo(LoadType.setLine, holder, R.id.set_name, ""));
            emitter.onNext(new LoaderInfo(LoadType.setLine, holder, R.id.set_desc, ""));
        }
    }

    @Override
    public void onReady(LoadInfo info) {
        switch (info.Types) {
            case setLine:
                LoaderInfo loader = (LoaderInfo) info;
                TextView textView = loader.holder.getView(loader.id);
                textView.setPadding(0, 0, 0, 0);
                textView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorGray));
                Method.setSize(textView, 0, 10 * Config.display.density);
                loader.holder.getView(R.id.set_right).setVisibility(View.GONE);
                break;
            case setImageId:
                loader = (LoaderInfo) info;
                if (loader.imageId == 0) {
                    loader.holder.getView(loader.id).setVisibility(View.GONE);
                }
                break;
            case setAdapter:
                ListView listView = getActivity().findViewById(R.id.my_listView);
                //设置listView的Adapter
                listView.setAdapter(((AdapterInfo) info).adapter);
                listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
                    //我们需要的内容，跳转页面或显示详细信息
                    SetInfo setInfo = (SetInfo) ((AdapterInfo) info).list.get(position);
                    if (setInfo.iHeard) return;
                    TextView set_name = view.findViewById(R.id.set_name);
                    switch (setInfo.Message) {
                        case "日志":
                            Intent intent = new Intent(getActivity(), WebActivity.class);
                            //将text框中的值传入
                            intent.putExtra("title", "日志");
                            File file = new File(Environment.getExternalStorageDirectory(), "/Meal/log.txt");
                            intent.putExtra("file", "file://" + file.toString());
                            startActivity(intent);
                            break;
                        case "关于":
                            Method.show(getActivity());
                            break;
                        case "退出":
                            Method.ask(getActivity(), "Confirm Logout").setListener(obj -> {
                                if (obj.Types == LoadType.confirm) {
                                    Config.Admin.UserId = 0;
                                    new SQLiteServer().updateAdmin("UserId", Config.Admin.UserId);
                                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                                    startActivityForResult(loginIntent, requestType.loginUpdate);
                                }
                            });
                            break;
                        case "关闭":
                            Method.ask(getActivity(), "Confirm Close").setListener(obj -> {
                                if (obj.Types == LoadType.confirm) {
                                    System.exit(0);
                                }
                            });
                            break;
                    }
                });
                break;
        }
    }
}
