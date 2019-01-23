package tinn.meal.ping.info.setInfo;

import java.util.List;

import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.support.MyAdapter;

public class AdapterInfo extends LoadInfo {

    public MyAdapter adapter;
    public List list;

    public AdapterInfo(MyAdapter adapter, List list) {
        this.Types = LoadType.setAdapter;
        this.adapter = adapter;
        this.list = list;
    }
}
