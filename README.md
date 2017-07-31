# Android数据库框架-----ORMLite 的基本用法 
  <p>ORMLite 是一款非要流行的Android平台上的数据库框架，性能优秀，代码简洁；</p> 
<p>&nbsp;</p> 
<p>简述: 优点： 1.轻量级；2.使用简单，易上手；3.封装完善；4.文档全面。</p> 
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 缺点：1.基于反射，效率较低（本人还没有觉得效率低）；2.缺少中文翻译文档</p> 
<p>&nbsp;</p> 
<p>准备工作：</p> 
<ol> 
 <li> <p>jar包 地址：<a href="http://ormlite.com/releases/" target="_blank" rel="nofollow">http://ormlite.com/releases/ </a></p> </li> 
 <li> <p>集成方法：把jar包复制到as的libs文件夹下，并且引用jar包即可</p> </li> 
</ol> 
<p>&nbsp;</p> 
<p>&nbsp;之后创建一个类User，并完成相关配置</p> 
<pre><code class="language-java">@DatabaseTable(tableName="tb_user")//标明数据库中的一张表,表名tb_user
public class User {

    @DatabaseField(generatedId = true)//generatedId 表示id为主键且自动生成
    private int id;
    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField(columnName = "desc")
    private String desc;

    public User()
    {
    }

    public User(String name, String desc)
    {
        this.name = name;
        this.desc = desc;
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

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }
}</code></pre> 
<p>基本的数据库操作</p> 
<pre><code class="language-java">public class DatabaseHelper extends OrmLiteSqliteOpenHelper  {


    private static final String TABLE_NAME = "sqlite-test.db";

    /**
     * userDao ，每张表对于一个
     */
    private Dao&lt;User, Integer&gt; userDao;

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
    public Dao&lt;User, Integer&gt; getUserDao() throws SQLException
    {
        if (userDao == null)
        {
            userDao = getDao(User.class);
        }
        return userDao;
    }
    //释放资源
    @Override
    public void close() {
        super.close();
    }
}</code></pre> 
<p>MainActivity.Java</p> 
<p>得到操作对象</p> 
<pre><code class="language-java"> DatabaseHelper helper = DatabaseHelper.getHelper(this);  </code></pre> 
<p><span style="color:#008000"><strong>1：添加</strong></span></p> 
<pre><code class="language-java"> User user= new User("zhangqie"+ni++, "2B青年");
    try {
            helper.getUserDao().create(user);//返回&gt;0则成功
        } catch (SQLException e)
        {
        }</code></pre> 
<p><span style="color:#008000"><strong>2：删除</strong></span></p> 
<pre><code class="language-java">  try
        {
            return helper.getUserDao().deleteById(id);
        } catch (SQLException e)
        {
        }
        return 0;</code></pre> 
<p><span style="color:#008000"><strong>3：修改</strong></span></p> 
<pre><code class="language-java"> User user= new User("zhangqie----android", "2B青年");
      user.setId(1);//修改Id=1的
 try
        {
            return helper.getUserDao().update(user);
        } catch (SQLException e)
        {
        }
        return 0;</code></pre> 
<p><span style="color:#008000"><strong>4：查询</strong></span></p> 
<pre><code class="language-java"> try
         {
             List&lt;User&gt; users=helper.getUserDao().queryForAll();
        } catch (SQLException e)
        {
        }</code></pre> 
<p>以上的实现方式是基本的使用方式；只有一个类User操作的，万一有多个呢，就不好操作了；</p> 
<p>&nbsp;</p> 
<p>接下来的这种方式：通过一个<span style="color:#008000"><strong>DatabaseHelper类就可以完成所有类的数据库操作</strong></span></p> 
<p><span style="color:#008000"><span style="color:#000000">整个DatabaseHelper使用单例只对外公布出一个对象，参考文章：http://www.touchlab.co/2011/10/single-sqlite-connectio</span></span></p> 
<pre><code class="language-java">public class DatabaseHelpers extends OrmLiteSqliteOpenHelper {

    private static final String TABLE_NAME = "sqlite-test.db";

    private Map&lt;String,Dao&gt; daos=new HashMap&lt;String, Dao&gt;();

    public DatabaseHelpers(Context context){
        super(context,TABLE_NAME,null,4);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
            //多个类在此添加即可
            //TableUtils.createTable(connectionSource, Article.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try
        {
            TableUtils.dropTable(connectionSource, User.class, true);
            //TableUtils.dropTable(connectionSource,Article.class,true);//多个类在此添加即可
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //整个DatabaseHelper使用单例只对外公布出一个对象，保证app中只存在一个SQLite Connection
    private static DatabaseHelpers instance;

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DatabaseHelpers getHelper(Context context)
    {
        context = context.getApplicationContext();
        if (instance == null)
        {
            synchronized (DatabaseHelper.class)
            {
                if (instance == null)
                    instance = new DatabaseHelpers(context);
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
}
     </code></pre> 
<p>我已User为例介绍即可</p> 
<p>用一个UserDao来完成相关操作，<strong><span style="color:#008000">多个实体类建立多个&nbsp; XXDao操作了即可</span></strong></p> 
<pre><code class="language-java">public class UserDao {

    private Context context;
    //通过此集合和DatabaseHelper的Map集合相对应  Dao中的类 如User 就可以随意变换了
    private Dao&lt;User, Integer&gt; userDaoOpe;
    private DatabaseHelpers helper;

    public UserDao(Context context)
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
    public List&lt;User&gt; query()
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

}</code></pre> 
<p>得到操作对象UserDao</p> 
<pre><code class="language-java">UserDao userDaos=new UserDao(this);</code></pre> 
<p><span style="color:#008000"><strong>1：添加</strong></span></p> 
<pre><code class="language-java"> User u1 = new User("zq"+(ni+=5), "2B青年");
  userDaos.add(u1);</code></pre> 
<p><span style="color:#008000"><strong>2：删除</strong></span></p> 
<pre><code class="language-java"> int is=  userDaos.delete(2);//成功  1  失败 0</code></pre> 
<p><span style="color:#008000"><strong>3：修改</strong></span></p> 
<pre><code class="language-java">  User u2 = new User("张三丰", "老道");
        u2.setId(1);
   userDaos.update(u2);</code></pre> 
<p><span style="color:#008000"><strong>4：查询</strong></span></p> 
<pre><code class="language-java">List&lt;User&gt; users=userDaos.query()；
String username="";
  for (int i=0;i&lt;users.size();i++){
       username+=+users.get(i).getId()+"-----"+users.get(i).getName()+"\n";
  }
 textView.setText(username);   </code></pre> 
<p>两种效果差不多，实现方式不同而已，运行效果如下：</p> 
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img alt="" src="https://static.oschina.net/uploads/img/201707/31143835_qWCr.gif"></p> 
<p>&nbsp;</p> 
<p><span style="color:#000000">由于代码太多，就不一一贴出来了，直接下载源码即可&nbsp; <span style="color:#33cccc"><strong><span style="color:#000000"></span></strong></span></span></p> 
<p>&nbsp;</p> 
<p>上面简单介绍了如何使用ORMLite框架，<a href="https://my.oschina.net/zhangqie/blog/1492707" rel="nofollow">Android数据库框架-----ORMLite关联表的使用 &nbsp;</a>将对其用法进行深入的介绍。</p> 
<p>&nbsp;</p> 
<p><strong><span style="color:#008080">不足之处请留言指正！有问题的可以给我留言！谢谢！</span></strong></p>
