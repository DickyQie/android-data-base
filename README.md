# Android数据库框架-----ORMLite关联表的使用
 <p>上一篇已经对ORMLite框架做了简单的介绍：<a href="https://my.oschina.net/zhangqie/blog/1492700" rel="nofollow">Android数据库框架-----ORMLite 的基本用法</a>~~本篇将介绍项目可能会使用到的一些用法，也为我们的使用ORMLite框架总结出一个较合理的用法。</p> 
<p>&nbsp;</p> 
<p>本文主要介绍两表相互关联的使用，如同外键，相互的查询功能；</p> 
<p>创建 User和Article类</p> 
<pre><code class="language-java">@DatabaseTable(tableName = "tb_user")
public class User 
{
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "name")
    private String name;

    @ForeignCollectionField
    private Collection&lt;Article&gt; articles;

    public Collection&lt;Article&gt; getArticles()
    {
        return articles;
    }

    public void setArticles(Collection&lt;Article&gt; articles)
    {
        this.articles = articles;
    }

    public User()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "User [id=" + id + ", name=" + name + ", articles=" + articles
                + "]";
    }

}</code></pre> 
<pre><code class="language-java">@ForeignCollectionField
private Collection&lt;Article&gt; articles;</code></pre> 
<p>每个User关联一个或多个Article，如果我在User中声明一个Collection&lt;Article&gt; articles，我能否在查询User的时候，一并能够获取到articles的值。</p> 
<pre><code class="language-java">@DatabaseTable(tableName = "tb_article")
public class Article
{
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String title;
    @DatabaseField(canBeNull = true, foreign = true, columnName = "user_id", foreignAutoRefresh = true)
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

}</code></pre> 
<p>在user属性的注解上：@DatabaseField(canBeNull = true, foreign = true, columnName = "user_id", foreignAutoRefresh = true)</p> 
<p>添加foreignAutoRefresh =true，这样；当调用queryForId时，拿到Article对象则直接携带了user；</p> 
<p>&nbsp;</p> 
<p>&nbsp;DatabaseHelpe.java</p> 
<pre><code class="language-java">public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
    private static final String TABLE_NAME = "sqlite-test.db";

    private Map&lt;String, Dao&gt; daos = new HashMap&lt;String, Dao&gt;();

    private DatabaseHelper(Context context)
    {
        super(context, TABLE_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase database,
            ConnectionSource connectionSource)
    {
        try
        {
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Article.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
            ConnectionSource connectionSource, int oldVersion, int newVersion)
    {
        try
        {
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, Article.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private static DatabaseHelper instance;

    /**
     * 单例获取该Helper
     * 
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getHelper(Context context)
    {
        context = context.getApplicationContext();
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

    public synchronized Dao getDao(Class clazz) throws SQLException
    {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className))
        {
            dao = daos.get(className);
        }
        if (dao == null)
        {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close()
    {
        super.close();

        for (String key : daos.keySet())
        {
            Dao dao = daos.get(key);
            dao = null;
        }
    }

}</code></pre> 
<p>Userdao操作类</p> 
<pre><code class="language-java">public class UserDao
{
    private Context context;
    private Dao&lt;User, Integer&gt; userDaoOpe;
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
                new Callable&lt;Void&gt;()
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

}</code></pre> 
<p>ArticleDao操作类</p> 
<pre><code class="language-java">public class ArticleDao
{
    private Dao&lt;Article, Integer&gt; articleDaoOpe;
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
    public List&lt;Article&gt; listByUserId(int userId)
    {
        try
        {
            QueryBuilder&lt;Article, Integer&gt; articleBuilder = articleDaoOpe
                    .queryBuilder();
            QueryBuilder userBuilder = helper.getDao(User.class).queryBuilder();
            articleBuilder.join(userBuilder);
            
            
            /*Where&lt;Article, Integer&gt; where = articleBuilder.where();
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
}</code></pre> 
<span id="OSC_h3_1"></span>
<h3>条件查询QueryBuilder的使用</h3> 
<p>用于where查询，在ArticleDao类中包含的有。更多的用法，去官方文档查看，源码中提供了文档；</p> 
<p><span style="color:#000000">提示：运行测试时，要先添加数据，源码未做错误处理&nbsp; <span style="color:#000000"> <span style="color:#33cccc"> <strong><span style="color:#000000"></span></strong></span></span></span></p> 
<p><strong><span style="color:#008080">不足之处请留言指正！有问题的可以给我留言！谢谢！</span></strong></p> 
