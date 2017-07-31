package com.example.de;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import bean.Article;
import bean.User;
import dao.ArticleDao;
import dao.UserDao;

public class MainActivity extends Activity implements OnClickListener{

	TextView textView;
	String s="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    
    private void initView(){
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        textView= (TextView) findViewById(R.id.text);
    }
    
    @Override
    public void onClick(View v) {
    	// TODO Auto-generated method stub
    	 switch (v.getId()){
         case R.id.btn1:
        	 	testAddArticle();
             break;
         case R.id.btn2:

        	 testGetArticleById();
             break;
         case R.id.btn3:
        	 testGetArticleWithUser();
             break;
         case R.id.btn4:
        	 testListArticlesByUserId();
        	 testGetUserById();
             break;
     }
    }
    
    
    public void testAddArticle()
	{
		User u = new User();
		u.setName("zhangqie");
		new UserDao(this).add(u);
		Article article = new Article();
		article.setTitle("ORMLite数据库");
		article.setUser(u);
		new ArticleDao(this).add(article);

	}

	public void testGetArticleById()
	{
		Article article = new ArticleDao(this).get(1);
		s+="User的Name1："+article.getUser().getName()+"\n";
		textView.setText(s);
		Toast.makeText(getApplicationContext(), "a"+article.getTitle(), 1).show();
	}

	public void testGetArticleWithUser()
	{

		Article article = new ArticleDao(this).getArticleWithUser(1);
		s+="User的Name2："+article.getUser().getName()+"\n";
		textView.setText(s);
		Toast.makeText(getApplicationContext(), "a"+article.getTitle(), 1).show();
	}

	public void testListArticlesByUserId()
	{

		List<Article> articles = new ArticleDao(this).listByUserId(1);
		s+="User的Name3："+articles.get(0).getUser().getName()+"\n";
		textView.setText(s);
		Toast.makeText(getApplicationContext(), "a"+articles.toString(), 1).show();
	}
	
	
	public void testGetUserById()
	{
		User user = new UserDao(this).get(1);
		if (user.getArticles() != null)
			for (Article article : user.getArticles())
			{
				s+="User的Name4："+article.getTitle()+"\n";
				textView.setText(s);
				Toast.makeText(getApplicationContext(), "a"+article.getTitle(), 1).show();
			}
	}

}
