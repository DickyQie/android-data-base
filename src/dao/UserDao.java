package dao;

import java.sql.SQLException;

import android.content.Context;
import bean.User;

import com.j256.ormlite.dao.Dao;

public class UserDao
{
	private Context context;
	private Dao<User, Integer> userDaoOpe;
	private DatabaseHelper helper;

	public UserDao(Context context)
	{
		this.context = context;
		try
		{
			helper = DatabaseHelper.getHelper(context);
			userDaoOpe = helper.getDao(User.class);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 增加一个用户
	 * 
	 * @param user
	 * @throws SQLException
	 */
	public void add(User user) 
	{
		/*//事务操作
		TransactionManager.callInTransaction(helper.getConnectionSource(),
				new Callable<Void>()
				{

					@Override
					public Void call() throws Exception
					{
						return null;
					}
				});*/
		try
		{
			userDaoOpe.create(user);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

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
