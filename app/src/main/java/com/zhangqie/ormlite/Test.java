package com.zhangqie.ormlite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangqie.ormlite.dao2.UserDaos;
import com.zhangqie.ormlite.entity.Article;
import com.zhangqie.ormlite.entity.User;
import com.zhangqie.ormlite.utils.ArticleDao;

import java.util.List;

/**
 * Created by Administrator on 2017/7/29.
 */

public class Test extends AppCompatActivity implements View.OnClickListener{

    TextView textView;
    Button btn;
    int ni=1;

    UserDaos userDaos;
    ArticleDao articleDao;

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
        btn= (Button) findViewById(R.id.btn5);

        textView= (TextView) findViewById(R.id.text);
        userDaos=new UserDaos(this);
        articleDao=new ArticleDao(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:

                User u1 = new User("zhangqien"+(ni+=5), "2B青年");
                userDaos.add(u1);
              /*  Article article=new Article();
                article.setTitle("one");
                article.setUser(u1);
                int iss=articleDao.add(article);
                Toast.makeText(getApplicationContext(),iss+"",Toast.LENGTH_LONG).show();*/


                m(userDaos.query());
                break;
            case R.id.btn2:

                int is=  userDaos.delete(2);//成功  1  失败 0
                Toast.makeText(getApplicationContext(),is+"",Toast.LENGTH_LONG).show();
                m(userDaos.query());


                break;
            case R.id.btn3:

                User u2 = new User("张三丰", "老道");
                u2.setId(2);
                userDaos.update(u2);
                m(userDaos.query());

                break;
            case R.id.btn4:

                m(userDaos.query());
/*
                Article article1=articleDao.get(1);
                Article article2=articleDao.getArticleWithUser(1);
                String s=article1.getTitle()+"---"+article2.getTitle();

                List<Article> articles=articleDao.listByUserId(1);
                textView.setText(s+"===="+articles.get(0).getUser().getName()+"---"+article1.getUser().getName()+"---"+article2.getUser().getName());//

                testGetUserById();*/
                break;
        }
    }


    /*public void testGetUserById()
    {
        User user = userDaos.get(1);
        if (user.getArticles() != null)
            for (Article article : user.getArticles())
            {
                Toast.makeText(getApplicationContext(), article.getTitle(), Toast.LENGTH_LONG).show();
            }
    }*/






    public void m(List<User> users){
        String username="";
        for (int i=0;i<users.size();i++){
            username+=+users.get(i).getId()+"-----"+users.get(i).getName()+"\n";
        }
        textView.setText(username);
    }

}
