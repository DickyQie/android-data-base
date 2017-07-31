package com.zhangqie.ormlite.utils;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.zhangqie.ormlite.dao2.DatabaseHelpers;
import com.zhangqie.ormlite.entity.Article;
import com.zhangqie.ormlite.entity.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2017/7/28.
 */

public class ArticleDao {

    private Dao<Article, Integer> articleDaoOpe;
    private DatabaseHelpers helper;




    @SuppressWarnings("unchecked")
    public ArticleDao(Context context)
    {
        try
        {
            helper = DatabaseHelpers.getHelper(context);
            articleDaoOpe = helper.getDao(Article.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个Article
     * @param article
     */
    public int add(Article article)
    {
        try
        {
           return articleDaoOpe.create(article);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 通过Id得到一个Article
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public Article getArticleWithUser(int id)
    {
        Article article = null;
        try
        {
            article = articleDaoOpe.queryForId(id);
            helper.getDao(User.class).refresh(article.getUser());

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return article;
    }

    /**
     * 通过Id得到一篇文章
     * @param id
     * @return
     */
    public Article get(int id)
    {
        Article article = null;
        try
        {
            article = articleDaoOpe.queryForId(id);

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return article;
    }

    /**
     * 通过UserId获取所有的文章
     * @param userId
     * @return
     */
    public List<Article> listByUserId(int userId)
    {

        try {
            articleDaoOpe.updateBuilder().updateColumnValue("title","zzz").where().eq("user_id", 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try
        {
            /*return articleDaoOpe.queryBuilder().where().eq("user_id", userId)
                    .query();
*/
            //条件查询
            QueryBuilder<Article, Integer> queryBuilder = articleDaoOpe
                    .queryBuilder();
            Where<Article, Integer> where = queryBuilder.where();
            where.eq("user_id", 1);
            where.and();
            where.eq("title", "one");
            return queryBuilder.query();

            //或者
          /*  articleDaoOpe.queryBuilder().//
                    where().//
                    eq("user_id", 1).and().//
                    eq("name", "xxx");*/


        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
