package com.zhangqie.ormlite.dao2;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.zhangqie.ormlite.entity.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zhangqie on 2017/7/28.
 */

public class UserDaos {

    private Context context;
    private Dao<User, Integer> userDaoOpe;
    private DatabaseHelpers helper;

    public UserDaos(Context context)
    {
        this.context = context;
        try
        {
            helper = DatabaseHelpers.getHelper(context);
            userDaoOpe = helper.getDao(User.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 增加一个用户
     * @param user
     */
    public int add(User user)
    {
        try
        {
           return userDaoOpe.create(user);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 增加一个用户
     * @param id
     */
    public int delete(int id)
    {
        try
        {
           return userDaoOpe.deleteById(id);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
         return 0;
    }
    /**
     * 修改
     * @param user
     */
    public int update(User user)
    {
        try
        {
          return   userDaoOpe.update(user);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 查询
     */
    public List<User> query()
    {
        try {
            return userDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public User get(int id)
    {
        try
        {
            return userDaoOpe.queryForId(id);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
