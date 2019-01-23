package tinn.meal.ping.support;

import java.util.ArrayList;
import java.util.List;

import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.info.eventInfo.EventInfo;
import tinn.meal.ping.info.eventInfo.NotifiedEventInfo;
import tinn.meal.ping.info.sqlInfo.BaseInfo;
import tinn.meal.ping.info.sqlInfo.GoodInfo;

public class Cache {
    public static List<GoodInfo> GoodList = new ArrayList<GoodInfo>();
    public static List<EventInfo> NotifiedList = new ArrayList<EventInfo>();

    private static <T extends BaseInfo> T FindInfo(List<T> list, long id) {
        for (int i = 0; i < list.size(); i++) {
            T info = list.get(i);
            if (info.Id == id) return info;
        }
        return null;
    }

    public static GoodInfo FindGood(long id) {
        return FindInfo(GoodList, id);
    }

    public static void addNotified(LoadType type) {
        addNotified(type, null);
    }

    public static void addNotified(LoadType from, String msg) {
        EventInfo e = new NotifiedEventInfo(from);
        if (msg != null)
            e = new NotifiedEventInfo(from, msg);
        Method.hit(e.getMsg());
        NotifiedList.add(0, e);
    }
}
