package tinn.meal.ping.info;

import java.io.File;

import tinn.meal.ping.support.ViewHolder;

public class HolderInfo {

    public ViewHolder viewHolder;
    public int id;
    public String msg;
    public File file;
    public int imageId;

    public Object info;

    //构造函数
    public HolderInfo(ViewHolder viewHolder, int id, String msg) {
        this.viewHolder = viewHolder;
        this.id = id;
        this.msg = msg;
    }

    public HolderInfo(ViewHolder viewHolder, int id, File file) {
        this.viewHolder = viewHolder;
        this.id = id;
        this.file = file;
    }

    public HolderInfo(ViewHolder viewHolder, int id, int imageId) {
        this.viewHolder = viewHolder;
        this.id = id;
        this.imageId = imageId;
    }
}
