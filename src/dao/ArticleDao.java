package dao;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import bean.Article;
import bean.User;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class ArticleDao
{
	private Dao<Article, Integer> articleDaoOpe;
	private DatabaseHelper helper;

	@SuppressWarnings("unchecked")
	public ArticleDao(Context context)
	{
		try
		{
			helper = DatabaseHelper.getHelper(context);
			articleDaoOpe = helper.getDao(Article.class);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 添加一个Article
	 * 
	 * @param article
	 */
	public void add(Article article)
	{
		try
		{
			articleDaoOpe.create(article);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 通过Id得到一个Article
	 * 
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
	 * 
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
	 * 
	 * @param userId
	 * @return
	 */
	public List<Article> listByUserId(int userId)
	{
		try
		{
			QueryBuilder<Article, Integer> articleBuilder = articleDaoOpe
					.queryBuilder();
			QueryBuilder userBuilder = helper.getDao(User.class).queryBuilder();
			articleBuilder.join(userBuilder);
			
			
			/*Where<Article, Integer> where = articleBuilder.where();
			where.eq("user_id", 1);
			where.and();
			where.eq("title", "ORMLite数据库");
			
			return where.query();*/
			
			// 或者
			 /*return articleDaoOpe.queryBuilder().//
					where().//
					eq("user_id", 1).and().//
					eq("title", "ORMLite数据库").query();*/
			
			 //复杂的查询
			/*where.or(
					//
					where.and(//
							where.eq("user_id", 1), where.eq("name", "xxx")),
					where.and(//
							where.eq("user_id", 2), where.eq("name", "yyy")));*/

			return articleDaoOpe.queryBuilder().where().eq("user_id", userId)
					.query();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
