package com.zhangqie.ormlite.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by zhangqie on 2017/7/28.
 */

@DatabaseTable(tableName = "tb_article")
public class Article {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String title;
    //canBeNull -表示不能为null；foreign=true表示是一个外键;columnName 列名
    @DatabaseField(canBeNull = true, foreign = true, columnName = "user_id",foreignAutoRefresh = true)
    private User user;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    @Override
    public String toString()
    {
        return "Article [id=" + id + ", title=" + title + ", user=" + user
                + "]";
    }
}
