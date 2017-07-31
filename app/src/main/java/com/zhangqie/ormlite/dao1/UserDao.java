package com.zhangqie.ormlite.dao1;

import android.content.Context;

import com.zhangqie.ormlite.entity.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zhangqie on 2017/3/28.
 *
 * User的数据库操作，单独抽取出来为UserDao
 */

public class UserDao {

    private Context context;
    DatabaseHelper helper ;
    public UserDao(Context context)
    {
        this.context = context;
        helper = DatabaseHelper.getHelper(context);
    }



    public int add(User user)
    {
        try
        {
            return helper.getUserDao().create(user);
        } catch (SQLException e)
        {
        }
        return 0;
    }

    public int update(User user)
    {
        try
        {
            return helper.getUserDao().update(user);
        } catch (SQLException e)
        {
        }
        return 0;
    }

    public int delete(int id)
    {
        try
        {
            return helper.getUserDao().deleteById(id);
        } catch (SQLException e)
        {
        }
        return 0;
    }


    public List<User> query()
    {
        try
        {
            return helper.getUserDao().queryForAll();
        } catch (SQLException e)
        {
        }
        return null;
    }

}
