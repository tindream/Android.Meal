package tinn.meal.ping.data;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.reactivex.ObservableEmitter;
import tinn.meal.ping.enums.ILoadSync;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.enums.SqlType;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.info.loadInfo.ProgressInfo;
import tinn.meal.ping.info.sqlInfo.AdminBaseInfo;
import tinn.meal.ping.info.sqlInfo.GoodInfo;
import tinn.meal.ping.info.sqlInfo.UpdateLogInfo;
import tinn.meal.ping.support.Cache;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.ConverHelper;
import tinn.meal.ping.support.Method;

public class SQLServer {
    public SQLServer() {
    }

    public void checked(ObservableEmitter<LoadInfo> emitter, long version) throws Exception {
        if (Config.Admin.UpdateVersion < version) {
            long value = checked(emitter);
            Config.Admin.UpdateVersion = value;
            new SQLiteServer().updateAdmin("UpdateVersion", Config.Admin.UpdateVersion);
        }
    }

    private long checked(ObservableEmitter<LoadInfo> emitter) throws Exception {
        Connection conn = null;
        try {
            conn = getSQLConnection();

            SQLiteServer localServer = new SQLiteServer();
            List<UpdateLogInfo> list = new ArrayList<UpdateLogInfo>();
            queryList(conn, list, UpdateLogInfo.class, new UpdateLogInfo().getSql() + " where Id > " + Config.Admin.UpdateVersion);
            for (UpdateLogInfo info : list) {
                if (Method.isEmpty(info.Ids)) continue;
                if (info.Name.equals(GoodInfo.class.getSimpleName())) {
                    checked(info.Type, info.Ids, GoodInfo.class, conn, localServer, Cache.GoodList);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Cache.GoodList.sort(Comparator.naturalOrder());
                    }
                } else if (info.Name.equals(AdminBaseInfo.class.getSimpleName())) {
                    checked(info.Type, info.Ids, AdminBaseInfo.class, conn, localServer, new ArrayList<>());
                }
            }
            emitter.onNext(new LoadInfo(LoadType.complete));
            return list.get(list.size() - 1).Id;
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
    }

    private <T extends ILoadSync> void checked(int sqlType, String ids, Class type, Connection conn, SQLiteServer localServer, List<T> sqliteList) throws Exception {
        T t = (T) type.newInstance();
        switch (sqlType) {
            case SqlType.Insert:
                List<T> sqlList = new ArrayList<>();
                queryList(conn, sqlList, type, t.getSql() + " where Id in (" + ids + ")");
                localServer.insert(sqlList, sqliteList, type);
                break;
            case SqlType.Update:
                sqlList = new ArrayList<>();
                queryList(conn, sqlList, type, t.getSql() + " where Id in (" + ids + ")");
                if (type == AdminBaseInfo.class) {
                    loadAdmin(localServer, (List<AdminBaseInfo>) sqlList);
                } else {
                    localServer.update(sqlList, sqliteList, type);
                }
                break;
            case SqlType.Delete:
                localServer.execSQL("delete from " + t.getTable() + " where Id in (" + ids + ")");
                String[] strs = ids.split(",");
                for (String str : strs) {
                    long id = Long.parseLong(str);
                    for (T t1 : sqliteList) {
                        if (t1.getId() == id) {
                            sqliteList.remove(t1);
                            break;
                        }
                    }
                }
                break;
        }
    }

    public void Load(ObservableEmitter<LoadInfo> emitter) throws Exception {
        Connection conn = null;
        try {
            emitter.onNext(new LoadInfo(LoadType.connect));
            conn = getSQLConnection();
            emitter.onNext(new ProgressInfo(5));

            SQLiteServer localServer = new SQLiteServer();
            {
                List<GoodInfo> list = new ArrayList<GoodInfo>();
                queryList(conn, list, GoodInfo.class, new GoodInfo().getSql() + " where IsDelete = 0");

                localServer.sync(list, Cache.GoodList, GoodInfo.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Cache.GoodList.sort(Comparator.naturalOrder());
                }
                emitter.onNext(new ProgressInfo(42));
            }
            {
                List<AdminBaseInfo> list = new ArrayList<AdminBaseInfo>();
                queryList(conn, list, AdminBaseInfo.class, new AdminBaseInfo().getSql());
                loadAdmin(localServer, list);
                emitter.onNext(new ProgressInfo(49));
            }

            Method.log("Load数据完成");
            int count = 0;
            count = 0;
            String sql = "select Top 1 Id from UpdateLogs order by Id desc";
            long value = Long.parseLong(execScalar(conn, sql).toString());
            Config.Admin.UpdateVersion = value;
            new SQLiteServer().updateAdmin("UpdateVersion", Config.Admin.UpdateVersion);
            Method.log("Load图片完成");
            emitter.onNext(new LoadInfo(LoadType.complete));
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
    }

    private void loadAdmin(SQLiteServer localServer, List<AdminBaseInfo> list) {
        for (AdminBaseInfo info : list) {
            switch (info.Name) {
                case "BatteryPercent":
                    if (Config.Admin.BatteryPercent != Integer.parseInt(info.Value)) {
                        Config.Admin.BatteryPercent = Integer.parseInt(info.Value);
                        localServer.updateAdmin("BatteryPercent", Config.Admin.BatteryPercent);
                    }
                    break;
            }
        }
    }

    public byte[] queryCashImage(Connection conn, String table, long id) throws SQLException {
        byte[] bytes = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select Image from " + table + " where Id = " + id;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                bytes = rs.getBytes("Image");
            }
            return bytes;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
    }

    public <T> void queryList(Connection conn, List<T> list, Class type, String sql) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                T info = (T) type.newInstance();
                ConverHelper.setValue(info, rs);
                list.add(info);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
    }

    public Object execScalar(Connection conn, String sql) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getObject("Id");
            }
            return null;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
    }

    public void exec(Connection conn, String sql) throws Exception {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute(sql);
        } finally {
            if (stmt != null) stmt.close();
        }
    }

    // 连接
    public static Connection getSQLConnection() throws Exception {
        Class.forName("net.sourceforge.jtds.jdbc.Driver");//加载驱动换成这个
        String connectDB = "jdbc:jtds:sqlserver://" + Config.Host + ":1433/" + Config.DbName +// 连接字符串换成这个
                ";useunicode=true;characterEncoding=UTF-8";//防止中文乱码
        Connection con = DriverManager.getConnection(connectDB, Config.DbUserName, Config.DbPassword);// 连接数据库对象
        Method.log("连接数据库成功");
        return con;
    }
}
