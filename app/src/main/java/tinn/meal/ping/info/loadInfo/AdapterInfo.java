package tinn.meal.ping.info.loadInfo;

import java.util.List;

import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.support.MyAdapter;

public class AdapterInfo extends LoadInfo {

    public MyAdapter adapter;
    public List list;

    public AdapterInfo(MyAdapter adapter) {
        this.type = LoadType.setAdapter;
        this.adapter = adapter;
    }

    public AdapterInfo(MyAdapter adapter, List list) {
        this(adapter);
        this.list = list;
    }
}
