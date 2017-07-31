package com.zhangqie.ormlite.dao1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zhangqie.ormlite.entity.User;

import java.sql.SQLException;

/**
 * Created by zhangqie on 2017/3/28.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper  {


    private static final String TABLE_NAME = "sqlite-test.db";

    /**
     * userDao ，每张表对于一个
     */
    private Dao<User, Integer> userDao;

    private DatabaseHelper(Context context)
    {
        super(context, TABLE_NAME, null, 2);
    }
    //创建表
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource,User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //更新表
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource,User.class,true);//删除操作
            onCreate(sqLiteDatabase, connectionSource);//创建
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getHelper(Context context)
    {
        if (instance == null)
        {
            synchronized (DatabaseHelper.class)
            {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }

        return instance;
    }

    /**
     * 获得userDao
     *
     * @return
     * @throws SQLException
     */
    public Dao<User, Integer> getUserDao() throws SQLException
    {
        if (userDao == null)
        {
            userDao = getDao(User.class);
        }
        return userDao;
    }

    @Override
    public void close() {
        super.close();
    }
}
