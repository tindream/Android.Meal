package tinn.meal.ping.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import tinn.meal.ping.support.Method;

public class SQLiteHelper extends SQLiteOpenHelper {
    public SQLiteHelper(Context context) {
        super(context, "self.db", null, 7);
    }

    @Override
    //数据库第一次创建时被调用
    public void onCreate(SQLiteDatabase db) {
        Method.log("CreateTables");
        db.execSQL("create table Admins(Id integer primary key autoincrement not null" +
                ", Name nvarchar, Value nvarchar, DateTime datetime, unique(Id asc));");
        db.execSQL("create index main.admin_id on Admins (Id ASC);");

        db.execSQL("create table Goods(Id integer not null" +
                ", [Index] int, Name nvarchar, Price double, Quantity double, unique(Id asc));");
        db.execSQL("create index main.Goods_id on Goods (Id ASC);");
    }

    //软件版本号发生改变时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Method.log("onUpgrade");
    }
}
